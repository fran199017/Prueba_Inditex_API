package com.francisconicolau.pruebainditex.domain.mappers.impl;

import com.francisconicolau.pruebainditex.application.dto.PricesDTO;
import com.francisconicolau.pruebainditex.domain.mappers.PricesMapper;
import com.francisconicolau.pruebainditex.domain.model.Prices;
import org.springframework.stereotype.Component;

import java.util.ArrayList;
import java.util.List;

@Component
public class PricesMapperImpl implements PricesMapper {

    @Override
    public PricesDTO fromEntity(Prices e) {
        if (e == null) {
            return null;
        }

        return PricesDTO.builder()
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
    public List<PricesDTO> fromEntityList(List<Prices> e) {
        if (e == null) {
            return null;
        }

        var list = new ArrayList<PricesDTO>(e.size());
        for (Prices prices : e) {
            list.add(fromEntity(prices));
        }
        return list;
    }
}
