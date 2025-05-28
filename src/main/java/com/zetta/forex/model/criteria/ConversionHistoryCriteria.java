package com.zetta.forex.model.criteria;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.ToString;
import lombok.experimental.Accessors;

import java.math.BigDecimal;
import java.time.LocalDateTime;

@Getter
@Setter
@Accessors(chain = true)
@ToString
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class ConversionHistoryCriteria {
    private LocalDateTime fromDate;
    private LocalDateTime toDate;
    private String sourceCurrency;
    private String targetCurrency;
    private BigDecimal minAmount;
    private BigDecimal maxAmount;
    private BigDecimal minResult;
    private BigDecimal maxResult;
}
