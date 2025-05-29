package com.zetta.forex.model.dto;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;
import lombok.experimental.Accessors;

@Getter
@Setter
@Accessors(chain = true)
@ToString
@Schema(description = "Exchange rate response containing conversion details")
public class ExchangeRateResponseDto {
    private Boolean success;

    private Long timestamp;

    private String from;

    private String to;

    @Schema(description = "Exchange rate value", example = "1.05")
    private Double rate;
}
