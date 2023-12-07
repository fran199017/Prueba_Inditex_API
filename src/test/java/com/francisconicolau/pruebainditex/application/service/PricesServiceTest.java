package com.francisconicolau.pruebainditex.application.service;

import com.francisconicolau.pruebainditex.application.service.impl.PricesServiceImpl;
import com.francisconicolau.pruebainditex.infrastructure.config.ServiceProperties;
import jakarta.transaction.Transactional;
import lombok.Getter;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit.jupiter.SpringExtension;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

import static org.junit.jupiter.api.Assertions.assertEquals;


@SpringBootTest
@ExtendWith(SpringExtension.class)
@Transactional
class PricesServiceTest {
    @Autowired
    private PricesServiceImpl service;

    @Getter
    @Autowired
    private ServiceProperties properties;

    // Define el formato esperado
    DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd'T'HH:mm:ss");

    String productId = "eq:35455";
    String brand = "eq:1";

    @Test
    @DisplayName("Test 1: petición a las 10:00 del día 14 del producto 35455 para la brand 1 (ZARA)")
    public void test1() throws Exception {
        var date = "bw:20200614100000";

        var actual = service.findAll(date, productId, brand);

        assertEquals(1, actual.size());
        assertEquals(1, actual.get(0).getBrandId());
        assertEquals(35455, actual.get(0).getProductId());
        assertEquals(BigDecimal.valueOf(35.5), actual.get(0).getPrice());
    }

    @Test
    @DisplayName("Test 2: petición a las 16:00 del día 14 del producto 35455   para la brand 1 (ZARA)")
    public void test2() throws Exception {
        var date = "bw:20200614160000";
        var actual = service.findAll(date, productId, brand);

        assertEquals(2, actual.size());
        //First object
        assertEquals(1, actual.get(0).getBrandId());
        assertEquals(35455, actual.get(0).getProductId());
        assertEquals(BigDecimal.valueOf(35.5), actual.get(0).getPrice());
        assertEquals(LocalDateTime.parse("2020-06-14T00:00:00", formatter), actual.get(0).getStartDate());
        assertEquals(LocalDateTime.parse("2020-12-31T23:59:59", formatter), actual.get(0).getEndDate());

        //Second object
        assertEquals(1, actual.get(1).getBrandId());
        assertEquals(35455, actual.get(1).getProductId());
        assertEquals(BigDecimal.valueOf(25.45), actual.get(1).getPrice());
        assertEquals(LocalDateTime.parse("2020-06-14T15:00:00", formatter), actual.get(1).getStartDate());
        assertEquals(LocalDateTime.parse("2020-06-14T18:30:00", formatter), actual.get(1).getEndDate());
    }


}
