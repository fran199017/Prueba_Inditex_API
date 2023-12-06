package com.francisconicolau.pruebainditex.application.service;

import com.francisconicolau.pruebainditex.application.dto.CreatePriceRequestDTO;
import com.francisconicolau.pruebainditex.application.dto.PricesDTO;
import com.francisconicolau.pruebainditex.domain.model.Prices;

import java.util.List;

public interface PricesService {

    Prices getById(int id);

    PricesDTO createNewPrice(CreatePriceRequestDTO pricesDTO);

    void deletePrice(int id);

    Prices updatePrice(int id, CreatePriceRequestDTO priceDTO);

    List<Prices> findAll(String date, String productId, String brandId) throws Exception;
}
