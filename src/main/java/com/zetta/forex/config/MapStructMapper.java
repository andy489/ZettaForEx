package com.zetta.forex.config;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.zetta.forex.model.dto.ExchangeRateResponseDto;
import com.zetta.forex.model.entity.ExchangeRatesEntity;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.Named;

import java.util.Map;

@Mapper(componentModel = "spring")
public interface MapStructMapper {

    ObjectMapper objectMapper = new ObjectMapper();

    @Mapping(target = "quotes", source = "quotes", qualifiedByName = "mapToString")
    ExchangeRatesEntity toEntity(ExchangeRateResponseDto dto);

    @Mapping(target = "quotes", source = "quotes", qualifiedByName = "stringToMap")
    ExchangeRateResponseDto toDto(ExchangeRatesEntity entity);

    @Named("mapToString")
    static String mapToString(Map<String, Double> map) {
        try {
            return objectMapper.writeValueAsString(map);
        } catch (JsonProcessingException e) {
            throw new RuntimeException("Error converting map to JSON string", e);
        }
    }

    @Named("stringToMap")
    static Map<String, Double> stringToMap(String json) {
        json = json;
        try {
            return objectMapper.readValue(json, new TypeReference<Map<String, Double>>() {
            });
        } catch (JsonProcessingException e) {
            throw new RuntimeException("Error converting JSON string to map", e);
        }
    }
}
