package com.zetta.forex.config;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.zetta.forex.model.dto.ExchangeRateResponseDto;
import com.zetta.forex.model.dto.ZettaConversionResponseDto;
import com.zetta.forex.model.entity.ConversionHistoryEntity;
import com.zetta.forex.model.entity.ExchangeRatesEntity;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.Named;

import java.time.Instant;
import java.time.LocalDateTime;
import java.time.ZoneOffset;
import java.util.Map;

import java.time.ZoneId;

@Mapper(componentModel = "spring")
public interface MapStructMapper {

    ObjectMapper objectMapper = new ObjectMapper();

    @Mapping(target = "quotes", source = "quotes", qualifiedByName = "mapToString")
    ExchangeRatesEntity toEntity(ExchangeRateResponseDto dto);

    @Mapping(target = "quotes", source = "quotes", qualifiedByName = "stringToMap")
    ExchangeRateResponseDto toDto(ExchangeRatesEntity entity);

    @Mapping(target = "timestamp", source = "timestamp", qualifiedByName = "epochTimeToInstant")
    @Mapping(target = "source", source = "from")
    @Mapping(target = "target", source = "to")
    ConversionHistoryEntity toEntity(ZettaConversionResponseDto zettaConversionResponseDto);

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
        try {
            return objectMapper.readValue(json, new TypeReference<>() {
            });
        } catch (JsonProcessingException e) {
            throw new RuntimeException("Error converting JSON string to map", e);
        }
    }

    @Named("epochTimeToInstant")
    public static Instant epochToInstant(long epochTime) {
        // Determine if the time is in seconds or milliseconds
        // Typically, if the number is very large (> 1e12), it's in milliseconds
        if (String.valueOf(epochTime).length() > 10) {
            // Milliseconds to LocalDateTime
            return Instant.ofEpochMilli(epochTime);
        } else {
            // Seconds to LocalDateTime
            return Instant.ofEpochSecond(epochTime);
        }
    }
}
