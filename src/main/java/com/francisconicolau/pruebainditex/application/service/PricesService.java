package com.francisconicolau.pruebainditex.application.service;

import com.francisconicolau.pruebainditex.application.dto.CreatePriceRequestDTO;
import com.francisconicolau.pruebainditex.application.dto.PriceDTO;

import java.util.List;

public interface PricesService {

    PriceDTO getById(int id);

    PriceDTO createNewPrice(CreatePriceRequestDTO pricesDTO);

    void deletePrice(int id);

    PriceDTO updatePrice(int id, CreatePriceRequestDTO priceDTO);

    List<PriceDTO> findAll(String date, String productId, String brandId, Boolean orderByPriority) throws Exception;
}
