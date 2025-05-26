package com.zetta.forex.service;

import com.zetta.forex.config.MapStructMapper;
import com.zetta.forex.model.dto.ExchangeRateResponseDto;
import com.zetta.forex.model.entity.ExchangeRatesEntity;
import com.zetta.forex.repo.ExchangeRateRepository;
import jakarta.persistence.EntityNotFoundException;
import jakarta.transaction.Transactional;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.web.reactive.function.client.WebClient;

import java.time.Duration;

@Service
@Transactional
public class ForexApiService {

    private static final Logger logger = LoggerFactory.getLogger(ForexApiService.class);

    @Value("${api_layer.api_key}")
    private String apiKey;

    @Value("${api_layer.base_currency_code}")
    private String baseCurrency;

    @Value("${api_layer.all_currency_codes}")
    private String allCurrencyCodes;

    private final WebClient webClient;

    private final MapStructMapper mapper;

    private final ExchangeRateRepository exchangeRateRepository;

    public ForexApiService(WebClient webClient, MapStructMapper mapper, ExchangeRateRepository exchangeRateRepository) {
        this.webClient = webClient;
        this.mapper = mapper;
        this.exchangeRateRepository = exchangeRateRepository;
    }

    public ExchangeRateResponseDto getRates() {
        // Try to fetch from API first
        try {
            ExchangeRateResponseDto exchangeRateResponseDto = webClient.get()
                    .uri(uriBuilder -> uriBuilder
                            .queryParam("access_key", apiKey + "asd")
                            .queryParam("source", baseCurrency)
                            .queryParam("currencies", allCurrencyCodes)
                            .build())
                    .retrieve()
                    .bodyToMono(ExchangeRateResponseDto.class)
                    .timeout(Duration.ofSeconds(3))
                    .block();

            // Map and save to database
            ExchangeRatesEntity exchangeRatesEntity = mapper.toEntity(exchangeRateResponseDto);

            exchangeRateRepository.saveSingleton(exchangeRatesEntity);

            return exchangeRateResponseDto;
        } catch (Exception e) {

            // If API call fails, fall back to database
            ExchangeRatesEntity exchangeRatesEntity = exchangeRateRepository.findSingleton()
                    .orElseThrow(() -> new EntityNotFoundException("Data not found in API or database"));

            return mapper.toDto(exchangeRatesEntity);
        }
    }
}
