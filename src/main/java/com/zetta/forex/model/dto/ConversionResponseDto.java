package com.zetta.forex.model.dto;

import lombok.Getter;
import lombok.Setter;
import lombok.ToString;
import lombok.experimental.Accessors;

import java.math.BigDecimal;

@Getter
@Setter
@Accessors(chain = true)
@ToString
public class ConversionResponseDto {
    private Boolean success;

    private Long timestamp;

    private String from;

    private String to;

    private BigDecimal amount;

    private BigDecimal result;
}
