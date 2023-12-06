package com.francisconicolau.pruebainditex.application.service.impl;

import com.francisconicolau.pruebainditex.application.dto.CreatePriceRequestDTO;
import com.francisconicolau.pruebainditex.application.dto.PricesDTO;
import com.francisconicolau.pruebainditex.application.exception.CustomException;
import com.francisconicolau.pruebainditex.application.service.PricesService;
import com.francisconicolau.pruebainditex.domain.mappers.PricesMapper;
import com.francisconicolau.pruebainditex.domain.model.Prices;
import com.francisconicolau.pruebainditex.domain.repository.PricesRepository;
import com.francisconicolau.pruebainditex.infrastructure.config.ServiceProperties;
import com.francisconicolau.pruebainditex.infrastructure.config.ServicePropertyConst;
import jakarta.persistence.criteria.CriteriaBuilder;
import jakarta.persistence.criteria.CriteriaQuery;
import jakarta.persistence.criteria.Root;
import lombok.Getter;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.time.format.DateTimeParseException;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

@Service("pricesService")
public class PricesServiceImpl implements PricesService {

    private static final String DATE_FORMAT = "yyyyMMddHHmmss";
    private static final DateTimeFormatter formatter = DateTimeFormatter.ofPattern(DATE_FORMAT);
    public static final String DOS_PUNTOS = ":";
    public static final String FILTER_EQUALS = "eq";
    public static final String FILTER_NOT_EQUALS = "neq";
    public static final String FILTER_GREATHER_THAN = "gt";
    public static final String FILTER_LESS_THAN = "lt";

    @Autowired
    private PricesRepository pricesRepository;

    @Autowired
    private PricesMapper mapper;

    @Getter
    @Autowired
    private ServiceProperties properties;

    private static final Logger log = LoggerFactory.getLogger(PricesService.class);

    public List<Prices> findAll(String date, String productId, String brandId) throws Exception {
        var prices = pricesRepository.findAll(getSpecifications(date, productId, brandId));
        return prices;
    }


    public Prices getById(int id) {
        Optional<Prices> pricesOpt = pricesRepository.findById(id);
        return pricesOpt.orElse(null);
    }

    public PricesDTO createNewPrice(CreatePriceRequestDTO pricesDTO) {
        var startDate = getDateformatted(pricesDTO.getStartDate());
        var endDate = getDateformatted(pricesDTO.getEndDate());

        Prices prices = new Prices();
        prices.setBrandId(pricesDTO.getBrandId());

        if (startDate != null) {
            prices.setStartDate(startDate);
        } else {
            prices.setStartDate(LocalDateTime.now());
        }

        if (endDate != null) {
            prices.setEndDate(endDate);
        } else {
            prices.setEndDate(LocalDateTime.now());
        }

        prices.setPriceList(pricesDTO.getPriceList());
        prices.setProductId(pricesDTO.getProductId());
        prices.setPrice(pricesDTO.getPrice());
        prices.setCurr(pricesDTO.getCurr());
        prices.setPriority(pricesDTO.getPriority());

        var pricesSaved = pricesRepository.save(prices);
        return mapper.fromEntity(pricesSaved);
    }

    public void deletePrice(int id) {
        Prices price = getById(id);
        if (price != null) {
            pricesRepository.delete(price);
        }
    }

    public Prices updatePrice(int id, CreatePriceRequestDTO priceDTO) {
        Prices prices = getById(id);
        if (prices != null) {
            String startDate = priceDTO.getStartDate();
            String endDate = priceDTO.getEndDate();

            prices.setPrice(priceDTO.getPrice());
            prices.setPriceList(priceDTO.getPriceList());
            prices.setPriority(priceDTO.getPriority());
            prices.setCurr(priceDTO.getCurr());
            prices.setBrandId(priceDTO.getBrandId());
            prices.setProductId(priceDTO.getProductId());

            LocalDateTime dateformattedStart = getDateformatted(startDate);
            prices.setStartDate(dateformattedStart);

            LocalDateTime dateformattedEnd = getDateformatted(endDate);
            prices.setEndDate(dateformattedEnd);

            return pricesRepository.save(prices);

        }
        return null;
    }

    private Specification<Prices> getSpecifications(String date, String productId, String brandId) throws Exception {
        List<Specification<Prices>> specifications = new ArrayList<>();
        if (date != null && !date.isEmpty()) {
            specifications.add(splitFilter(date, "startDate"));
        }
        if (productId != null && !productId.isEmpty()) {
            specifications.add(splitFilter(productId, "productId"));
        }
        if (brandId != null && !brandId.isEmpty()) {
            specifications.add(splitFilter(brandId, "brandId"));
        }
        return specifications.stream().reduce(Specification::and).orElse(null);
    }

    private Specification<Prices> splitFilter(String fieldValue, String fieldName) throws Exception {

        if (fieldValue.contains(DOS_PUNTOS)) {
            var fieldSeparated = fieldValue.split(DOS_PUNTOS);
            var filter = fieldSeparated[0];

            var value = "startDate".equals(fieldName)
                    ? getDateformatted(fieldSeparated[1])
                    : fieldSeparated[1];

            return switch (filter) {
                case FILTER_EQUALS -> (Root<Prices> root, CriteriaQuery<?> cq, CriteriaBuilder cb) ->
                        cb.equal(root.get(fieldName), value);
                case FILTER_NOT_EQUALS -> (Root<Prices> root, CriteriaQuery<?> cq, CriteriaBuilder cb) ->
                        cb.notEqual(root.get(fieldName), value);
                case FILTER_GREATHER_THAN -> (Root<Prices> root, CriteriaQuery<?> cq, CriteriaBuilder cb) ->
                        cb.greaterThanOrEqualTo(root.get(fieldName), (Comparable) value);
                case FILTER_LESS_THAN -> (Root<Prices> root, CriteriaQuery<?> cq, CriteriaBuilder cb) ->
                        cb.lessThanOrEqualTo(root.get(fieldName), (Comparable) value);
                default -> throw new Exception("Filtro no encontrado");
            };
        }
        throw new Exception("Debes aplicar un filtro");
    }

    public LocalDateTime getDateformatted(String date) {
        try {
            return LocalDateTime.parse(date, formatter);
        } catch (DateTimeParseException ex) {
            throw new CustomException(ServicePropertyConst.DATE_FORMAT_ERROR, properties.getStatusMessage(ServicePropertyConst.DATE_FORMAT_ERROR));
        }

    }
}
