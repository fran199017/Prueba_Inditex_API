package com.francisconicolau.pruebainditex.domain.mappers;


import com.francisconicolau.pruebainditex.application.dto.PricesDTO;
import com.francisconicolau.pruebainditex.domain.model.Prices;
import org.mapstruct.Mapper;

import org.mapstruct.Mapping;
import org.mapstruct.factory.Mappers;

import java.util.List;

@Mapper(componentModel = "spring")
public interface PricesMapper {

    PricesMapper INSTANCE = Mappers.getMapper(PricesMapper.class);

    @Mapping(target = "productoId", source = "productId")
    PricesDTO fromEntity(Prices entity);

    List<PricesDTO> fromEntityList(List<Prices> entities);
}
