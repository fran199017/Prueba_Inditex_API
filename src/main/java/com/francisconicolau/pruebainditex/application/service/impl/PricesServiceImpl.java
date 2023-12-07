package com.francisconicolau.pruebainditex.application.service.impl;

import com.francisconicolau.pruebainditex.application.dto.CreatePriceRequestDTO;
import com.francisconicolau.pruebainditex.application.dto.PriceDTO;
import com.francisconicolau.pruebainditex.application.exception.CustomException;
import com.francisconicolau.pruebainditex.application.service.PricesService;
import com.francisconicolau.pruebainditex.domain.mappers.PricesMapper;
import com.francisconicolau.pruebainditex.domain.model.Price;
import com.francisconicolau.pruebainditex.domain.repository.BrandRepository;
import com.francisconicolau.pruebainditex.domain.repository.PriceRepository;
import com.francisconicolau.pruebainditex.infrastructure.config.ServiceProperties;
import com.francisconicolau.pruebainditex.infrastructure.config.ServicePropertyConst;
import jakarta.persistence.criteria.CriteriaBuilder;
import jakarta.persistence.criteria.CriteriaQuery;
import jakarta.persistence.criteria.Root;
import lombok.Getter;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.time.format.DateTimeParseException;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;

@Service("pricesService")
public class PricesServiceImpl implements PricesService {

    private static final String DATE_FORMAT = "yyyyMMddHHmmss";
    private static final DateTimeFormatter formatter = DateTimeFormatter.ofPattern(DATE_FORMAT);

    //Filters
    public static final String DOS_PUNTOS = ":";
    public static final String FILTER_EQUALS = "eq";
    public static final String FILTER_NOT_EQUALS = "neq";
    public static final String FILTER_GREATHER_THAN = "gt";
    public static final String FILTER_LESS_THAN = "lt";
    public static final String BETWEEN = "bw";

    //Fields
    public static final String START_DATE = "startDate";
    public static final String PRODUCT_ID = "productId";
    public static final String BRAND_ID = "brandId";
    public static final String END_DATE = "endDate";


    @Autowired
    private PriceRepository pricesRepository;

    @Autowired
    private BrandRepository brandRepository;

    @Autowired
    private PricesMapper mapper;

    @Getter
    @Autowired
    private ServiceProperties properties;

    public List<PriceDTO> findAll(String date, String productId, String brandId, Boolean orderByPriority) throws Exception {
        var prices = pricesRepository.findAll(getSpecifications(date, productId, brandId));
        return !orderByPriority && !prices.isEmpty()
                ? mapper.fromEntityList(prices)
                : mapper.fromEntityList(prices.stream()
                .max(Comparator.comparingInt(Price::getPriority))
                .map(Collections::singletonList).orElse(Collections.emptyList()));
    }


    public PriceDTO getById(int id) {
        var pricesOpt = pricesRepository.findById(id);
        return pricesOpt.isEmpty() ? null : mapper.fromEntity(pricesOpt.get());
    }

    public PriceDTO createNewPrice(CreatePriceRequestDTO pricesDTO) {
        var brand = brandRepository.findById(pricesDTO.getBrandId());
        if (brand.isPresent()) {
            var startDate = getDateformatted(pricesDTO.getStartDate());
            var endDate = getDateformatted(pricesDTO.getEndDate());

            var price = new Price();
            price.setBrandId(pricesDTO.getBrandId());
            price.setStartDate(startDate);
            price.setEndDate(endDate);
            price.setPriceList(pricesDTO.getPriceList());
            price.setProductId(pricesDTO.getProductId());
            price.setPrice(pricesDTO.getPrice());
            price.setCurr(pricesDTO.getCurr());
            price.setPriority(pricesDTO.getPriority());

            return mapper.fromEntity(pricesRepository.saveAndFlush(price));
        }
        throw new CustomException(ServicePropertyConst.BRAND_NO_EXISTENTE, properties.getStatusMessage(ServicePropertyConst.BRAND_NO_EXISTENTE));
    }

    public void deletePrice(int id) {
        var priceOpt = pricesRepository.findById(id);
        if (priceOpt.isPresent()) {
            var price = priceOpt.get();
            pricesRepository.delete(price);
        }
    }

    public PriceDTO updatePrice(int id, CreatePriceRequestDTO priceDTO) {

        var brand = brandRepository.findById(priceDTO.getBrandId());
        if (brand.isPresent()) {
            var priceOpt = pricesRepository.findById(id);
            if (priceOpt.isPresent()) {
                var price = priceOpt.get();
                price.setStartDate(getDateformatted(priceDTO.getStartDate()));
                price.setEndDate(getDateformatted(priceDTO.getEndDate()));
                price.setPrice(priceDTO.getPrice());
                price.setPriceList(priceDTO.getPriceList());
                price.setPriority(priceDTO.getPriority());
                price.setCurr(priceDTO.getCurr());
                price.setBrandId(priceDTO.getBrandId());
                price.setProductId(priceDTO.getProductId());

                return mapper.fromEntity(pricesRepository.saveAndFlush(price));
            }
        }
        throw new CustomException(ServicePropertyConst.BRAND_NO_EXISTENTE, properties.getStatusMessage(ServicePropertyConst.BRAND_NO_EXISTENTE));
    }

    private Specification<Price> getSpecifications(String date, String productId, String brandId) throws Exception {
        List<Specification<Price>> specifications = new ArrayList<>();
        if (date != null && !date.isEmpty()) {
            specifications.add(splitFilterAndGetSpec(date, START_DATE));
        }
        if (productId != null && !productId.isEmpty()) {
            specifications.add(splitFilterAndGetSpec(productId, PRODUCT_ID));
        }
        if (brandId != null && !brandId.isEmpty()) {
            specifications.add(splitFilterAndGetSpec(brandId, BRAND_ID));
        }
        return specifications.stream().reduce(Specification::and).orElse(null);
    }

    /**
     * Método que filtra segun los filtros EQUALS, NOT EQUALS, GREATHER THAN y LESS THAN.
     * <p>
     * Si el campo es startDate y el filtro es bw (BETWEEN) haremos una comparacion comprendida entre startDate y endDate
     * y sino, solo compararemos con el startDate dado que el endpoint solo admite 3 parametros.
     *
     * @param fieldValue valor del campo a comparar
     * @param fieldName  Como se llama la variable en la tabla Price
     * @return Specification<Price>
     * @throws CustomException
     */
    private Specification<Price> splitFilterAndGetSpec(String fieldValue, String fieldName) throws CustomException {

        if (fieldValue.contains(DOS_PUNTOS)) {
            var fieldSeparated = fieldValue.split(DOS_PUNTOS);
            var filter = fieldSeparated[0];


            if (START_DATE.equals(fieldName) && BETWEEN.equals(filter)) {
                return compareBetweenFilterForStartDateAndEndDate(fieldSeparated);
            } else {
                var value = START_DATE.equals(fieldName)
                        ? getDateformatted(fieldSeparated[1])
                        : fieldSeparated[1];

                return switch (filter) {
                    case FILTER_EQUALS -> (Root<Price> root, CriteriaQuery<?> cq, CriteriaBuilder cb) ->
                            cb.equal(root.get(fieldName), value);
                    case FILTER_NOT_EQUALS -> (Root<Price> root, CriteriaQuery<?> cq, CriteriaBuilder cb) ->
                            cb.notEqual(root.get(fieldName), value);
                    case FILTER_GREATHER_THAN -> (Root<Price> root, CriteriaQuery<?> cq, CriteriaBuilder cb) ->
                            cb.greaterThanOrEqualTo(root.get(fieldName), (Comparable) value);
                    case FILTER_LESS_THAN -> (Root<Price> root, CriteriaQuery<?> cq, CriteriaBuilder cb) ->
                            cb.lessThanOrEqualTo(root.get(fieldName), (Comparable) value);
                    default ->
                            throw new CustomException(ServicePropertyConst.FILTRO_NO_ENCONTRADO, properties.getStatusMessage(ServicePropertyConst.FILTRO_NO_ENCONTRADO));
                };
            }

        }
        throw new CustomException(ServicePropertyConst.NO_FILTRO, properties.getStatusMessage(ServicePropertyConst.NO_FILTRO));
    }

    private Specification<Price> compareBetweenFilterForStartDateAndEndDate(String[] fieldSeparated) {

        var value = getDateformatted(fieldSeparated[1]);

        return (Root<Price> root, CriteriaQuery<?> query, CriteriaBuilder cb) ->
                cb.and(
                        cb.lessThanOrEqualTo(root.get("startDate"), value),
                        cb.greaterThanOrEqualTo(root.get("endDate"), value)
                );
    }

    private LocalDateTime getDateformatted(String date) {
        try {
            return LocalDateTime.parse(date, formatter);
        } catch (DateTimeParseException ex) {
            throw new CustomException(ServicePropertyConst.DATE_FORMAT_ERROR, properties.getStatusMessage(ServicePropertyConst.DATE_FORMAT_ERROR));
        }

    }
}
