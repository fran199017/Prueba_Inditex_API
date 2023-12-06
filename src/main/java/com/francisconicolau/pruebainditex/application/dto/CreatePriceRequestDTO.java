package com.francisconicolau.pruebainditex.application.dto;

import jakarta.validation.constraints.*;
import lombok.Builder;
import lombok.Data;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@Data
@Builder
public class CreatePriceRequestDTO {
    public static final String CAMPO_NO_NULO = "Campo no nulo";
    public static final String FORMATO_FECHA = "El formato debe ser yyyyMMddHHmmss";

    @NotNull(message = CAMPO_NO_NULO)
    Integer brandId;

    @NotNull(message = CAMPO_NO_NULO)
    @Pattern(regexp = "\\d{4}\\d{2}\\d{2}\\d{2}\\d{2}\\d{2}", message = FORMATO_FECHA)
    String startDate;

    @NotNull(message = CAMPO_NO_NULO)
    @Pattern(regexp = "\\d{4}\\d{2}\\d{2}\\d{2}\\d{2}\\d{2}", message = FORMATO_FECHA)
    String endDate;

    @NotNull(message = CAMPO_NO_NULO)
    Integer priceList;

    @NotNull(message = CAMPO_NO_NULO)
    Integer productId;

    @NotNull(message = CAMPO_NO_NULO)
    @Min(value = 0, message = "La prioridad solo puede ser 0 o 1")
    @Max(value = 1, message = "La prioridad solo puede ser 0 o 1")
    Integer priority;

    @NotNull(message = CAMPO_NO_NULO)
    float price;

    @NotNull(message = CAMPO_NO_NULO)
    @Size(min = 3, max = 3, message = "LA ISO de la moneda solo permite 3 caracteres")
    String curr;

}
