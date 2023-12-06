package com.francisconicolau.pruebainditex.infrastructure.web;

import com.francisconicolau.pruebainditex.application.dto.CreatePriceRequestDTO;
import com.francisconicolau.pruebainditex.application.dto.PricesDTO;
import com.francisconicolau.pruebainditex.application.exception.CustomException;
import com.francisconicolau.pruebainditex.application.service.PricesService;
import com.francisconicolau.pruebainditex.domain.model.Prices;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.Valid;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("api/v1/")
public class PricesController {

    private static final String NOT_FOUND_PRICES = "Not found prices";
    private static final String NOT_CREATED_PRICE = "Not created price";

    private static final Logger log = LoggerFactory.getLogger(PricesController.class);


    @Autowired
    PricesService pricesService;

    @GetMapping(value = "prices/findAll")
    @Operation(summary = "Devuelve un listado de precios segun los filtros aplicados eq:, neq:, gt:, lt:",
            description = " Para cada campo se le pueden aplicar varios filtros : " +
                    "eq:value para filtro EQUALS." +
                    "neq:value para filtro NOT EQUALS. " +
                    "gt:value para filtro GREATHER THAN. " +
                    "lt:value para filtro LESS THAN.")
    public ResponseEntity<List<Prices>> findAll(
            @Parameter(description = "Fecha en formato filtro:20200614000000", example = "eq:20200614000000") @RequestParam(required = false) String date,
            @Parameter(description = "Id producto filtro:35455", example = "neq:35456") @RequestParam(required = false) String productId,
            @Parameter(description = "Id brand filtro:1", example = "gt:1") @RequestParam(required = false) String brandId) {
        try {
            var prices = pricesService.findAll(date, productId, brandId);
            return new ResponseEntity<>(prices, HttpStatus.OK);
        } catch (Exception e) {
            log.error(e.getMessage(), e);
            return new ResponseEntity<>(HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }


    @PostMapping(value = "prices/create", produces = "application/json", consumes = "application/json")
    @Operation(summary = "Crear precio", description = "Crea un nuevo precio.")
    public ResponseEntity<PricesDTO> createPrice(@Valid @RequestBody CreatePriceRequestDTO createPriceDTO
    ) {
        try {
            var pricesDto = pricesService.createNewPrice(createPriceDTO);
            if (pricesDto != null) {
                return new ResponseEntity<>(pricesDto, HttpStatus.OK);
            }
            return new ResponseEntity<>(HttpStatus.NOT_FOUND);
        } catch (CustomException e) {
            log.error(e.getMessage(), e);
//            return new ResponseEntity<>( e.getMessage(), HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }
/*
    @PutMapping(value = "prices/{id}")
    public ResponseEntity<Object> updatePrice(@RequestBody(required = true) CreatePriceRequestDTO createPriceDTO,
                                              @PathVariable int id) {
        try {
            Prices prices = pricesService.updatePrice(id, createPriceDTO);
            if (prices != null) {
                return new ResponseEntity<>(prices, HttpStatus.OK);
            }
            return new ResponseEntity<>(NOT_CREATED_PRICE, HttpStatus.OK);
        } catch (Exception e) {
            log.error(e.getMessage(), e);
            return new ResponseEntity<>(HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }*/


    @GetMapping(value = "prices/{id}")
    public ResponseEntity<Object> getPricesById(@PathVariable int id) {
        try {
            Prices prices = pricesService.getById(id);
            if (prices != null) {
                return new ResponseEntity<>(prices, HttpStatus.OK);
            }
            return new ResponseEntity<>(NOT_FOUND_PRICES, HttpStatus.OK);
        } catch (Exception e) {
            log.error(e.getMessage(), e);
            return new ResponseEntity<>(HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    @DeleteMapping(value = "prices/{id}")
    public ResponseEntity<Void> deletePrice(@PathVariable int id) {
        try {
            pricesService.deletePrice(id);
            return new ResponseEntity<>(HttpStatus.OK);
        } catch (Exception e) {
            log.error(e.getMessage(), e);
            return new ResponseEntity<>(HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }


}
