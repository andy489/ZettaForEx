package com.zetta.forex.model.dto;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.ToString;
import lombok.experimental.Accessors;

import java.util.Map;

@Getter
@Setter
@Accessors(chain = true)
@NoArgsConstructor
@AllArgsConstructor
@ToString
public class AllRatesResponseDto {

    private Boolean success;

    private String source;

    private String timestamp;

    private Map<String, Double> quotes;
}
