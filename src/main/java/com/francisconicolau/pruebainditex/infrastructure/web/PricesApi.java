package com.francisconicolau.pruebainditex.infrastructure.web;

import com.francisconicolau.pruebainditex.application.dto.CreatePriceRequestDTO;
import com.francisconicolau.pruebainditex.application.exception.CustomException;
import com.francisconicolau.pruebainditex.application.service.PricesService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import jakarta.validation.Valid;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.Objects;

@RestController
@RequestMapping("api/v1/")
public class PricesApi {

    private static final String NOT_FOUND_PRICES = "Price not found";

    private static final Logger log = LoggerFactory.getLogger(PricesApi.class);


    @Autowired
    PricesService pricesService;

    @GetMapping(value = "prices/findAll")
    @Operation(summary = "Devuelve un listado de precios segun los filtros aplicados eq:, neq:, gt:, lt: bw: (Solo para la fecha)",
            description = " Para cada campo se le pueden aplicar varios filtros : " +
                    "eq:value para filtro EQUALS." +
                    "neq:value para filtro NOT EQUALS. " +
                    "gt:value para filtro GREATHER THAN OR EQUAL. " +
                    "lt:value para filtro LESS THAN OR EQUAL." +
                    "bw:value para filtro BETWEEN ( solo para rango de fechas startDate y endDate ).")
    public ResponseEntity<?> findAll(
            @Parameter(description = "Fecha en formato filtro:20200614000000 (entre startDate y endDate si el filtro es bw:, para los demas filtros (solo buscará la startDate)", example = "bw:20200614000000") @RequestParam(required = false) String date,
            @Parameter(description = "Id producto filtro:35455", example = "neq:35456") @RequestParam(required = false) String productId,
            @Parameter(description = "Id brand filtro:1", example = "gt:1") @RequestParam(required = false) String brandId) {
        try {
            return new ResponseEntity<>(pricesService.findAll(date, productId, brandId), HttpStatus.OK);
        } catch (Exception e) {
            log.error(e.getMessage(), e);
            return new ResponseEntity<>(e.getMessage(), HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }


    @PostMapping(value = "prices/create", produces = "application/json", consumes = "application/json")
    @Operation(summary = "Crear precio", description = "Crea un nuevo precio.")
    public ResponseEntity<?> createPrice(@Valid @RequestBody CreatePriceRequestDTO createPriceDTO
    ) {
        try {
            var pricesDto = pricesService.createNewPrice(createPriceDTO);
            if (pricesDto != null) {
                return new ResponseEntity<>(pricesDto, HttpStatus.OK);
            }
            return new ResponseEntity<>(HttpStatus.NOT_FOUND);
        } catch (CustomException e) {
            log.error(e.getMessage(), e);
            return new ResponseEntity<>( e.getMessage(), HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }
    @PutMapping(value = "prices/{id}")
    @Operation(summary = "Actualiza un precio existente")
    public ResponseEntity<?> updatePrice(@RequestBody CreatePriceRequestDTO createPriceDTO,
                                              @PathVariable int id) {
        try {
            var price = pricesService.updatePrice(id, createPriceDTO);
            return new ResponseEntity<>(Objects.requireNonNullElse(price, NOT_FOUND_PRICES), HttpStatus.OK);
        } catch (CustomException e) {
            log.error(e.getMessage(), e);
            return new ResponseEntity<>(e.getMessage(), HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }


    @GetMapping(value = "prices/{id}")
    public ResponseEntity<Object> getPricesById(@PathVariable int id) {
        try {
            var price = pricesService.getById(id);
            return new ResponseEntity<>(Objects.requireNonNullElse(price, NOT_FOUND_PRICES), HttpStatus.OK);
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
