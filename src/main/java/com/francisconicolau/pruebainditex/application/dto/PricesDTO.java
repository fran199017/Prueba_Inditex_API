package com.francisconicolau.pruebainditex.application.dto;


import jakarta.persistence.Column;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class PricesDTO {

    Integer productId;

    Integer brandId;

    LocalDateTime startDate;

    LocalDateTime endDate;

    Integer priceList;

    Integer priority;

    float price;
}
