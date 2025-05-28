package com.zetta.forex.model.dto;

import lombok.Getter;
import lombok.Setter;
import lombok.ToString;
import lombok.experimental.Accessors;

@Getter
@Setter
@Accessors(chain = true)
@ToString
public class ExchangeRateResponseDto {
    private Boolean success;

    private Long timestamp;

    private String from;

    private String to;

    private Double rate;
}
