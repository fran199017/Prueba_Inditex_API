package com.francisconicolau.pruebainditex.domain.mappers.impl;

import com.francisconicolau.pruebainditex.application.dto.PriceDTO;
import com.francisconicolau.pruebainditex.domain.mappers.PricesMapper;
import com.francisconicolau.pruebainditex.domain.model.Price;
import org.springframework.stereotype.Component;

import java.util.ArrayList;
import java.util.List;

@Component
public class PricesMapperImpl implements PricesMapper {

    @Override
    public PriceDTO fromEntity(Price e) {
        if (e == null) {
            return null;
        }

        return PriceDTO.builder()
                .productId(e.getProductId())
                .brandId(e.getBrandId())
                .startDate(e.getStartDate())
                .endDate(e.getEndDate())
                .priceList(e.getPriceList())
                .priority(e.getPriority())
                .price(e.getPrice())
                .build();
    }

    @Override
    public List<PriceDTO> fromEntityList(List<Price> e) {
        if (e == null) {
            return null;
        }

        var list = new ArrayList<PriceDTO>(e.size());
        for (Price price : e) {
            list.add(fromEntity(price));
        }
        return list;
    }
}
