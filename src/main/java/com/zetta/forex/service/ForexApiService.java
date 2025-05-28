package com.zetta.forex.service;

import com.zetta.forex.config.LocalDateTimeProvider;
import com.zetta.forex.config.MapStructMapper;
import com.zetta.forex.model.dto.AllRatesResponseDto;
import com.zetta.forex.model.dto.ConversionResponseDto;
import com.zetta.forex.model.dto.ExchangeRateResponseDto;
import com.zetta.forex.model.entity.ExchangeRatesEntity;
import com.zetta.forex.repo.ExchangeRateRepository;
import jakarta.persistence.EntityNotFoundException;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.web.reactive.function.client.WebClient;

import java.time.Duration;
import java.util.Map;
import java.util.Optional;

import static com.zetta.forex.util.Util.EXCHANGE_RATES_ENTITY;
import static com.zetta.forex.util.Util.roundToNDecimals;

@Service
@Transactional
@RequiredArgsConstructor
public class ForexApiService {

    private static final Logger LOGGER = LoggerFactory.getLogger(ForexApiService.class);

    @Value("${api_layer.debug_mode}")
    private Boolean debugMode;

    @Value("${api_layer.api_key}")
    private String apiKey;

    @Value("${api_layer.all_currency_codes}")
    private String allCurrencyCodes;

    private final String BASE_CURRENCY = "EUR";

    private final WebClient webClient;

    private final MapStructMapper mapper;

    private final ExchangeRateRepository exchangeRateRepository;

    private final LocalDateTimeProvider localDateTimeProvider;

    public AllRatesResponseDto getRates() {
        if (debugMode == null || debugMode) {
            return onFailFallBack();
        }

        // Try to fetch from API first
        try {
            AllRatesResponseDto allRatesResponseDto = webClient.get()
                    .uri(uriBuilder -> uriBuilder
                            .queryParam("access_key", apiKey)
                            .queryParam("source", BASE_CURRENCY)
                            .queryParam("currencies", allCurrencyCodes)
                            .build())
                    .retrieve()
                    .bodyToMono(AllRatesResponseDto.class)
                    .timeout(Duration.ofSeconds(3))
                    .block();

            assert allRatesResponseDto != null;
            if (allRatesResponseDto.getSuccess()) {
                // Map and save to database
                ExchangeRatesEntity exchangeRatesEntity = mapper.toEntity(allRatesResponseDto);

                exchangeRateRepository.saveSingleton(exchangeRatesEntity);

                return allRatesResponseDto;
            } else {
                return onFailFallBack();
            }
        } catch (Exception e) {
            return onFailFallBack();
        }
    }

    private AllRatesResponseDto onFailFallBack() {

        // If API call fails, fall back to database
        Optional<ExchangeRatesEntity> exchangeRatesEntity = exchangeRateRepository.findSingleton();

        if (exchangeRatesEntity.isEmpty()) {
            exchangeRateRepository.saveSingleton(EXCHANGE_RATES_ENTITY);
            exchangeRatesEntity = exchangeRateRepository.findSingleton();

            if (exchangeRatesEntity.isEmpty()) {
                throw new EntityNotFoundException("Data not found in API or database");
            }
        }

        return mapper.toDto(exchangeRatesEntity.get());
    }

    public ExchangeRateResponseDto calcRate(String from, String to) {
        AllRatesResponseDto rates = this.getRates();

        Map<String, Double> quotes = rates.getQuotes();

        double currRate = 1.0;

        ExchangeRateResponseDto exchangeRateResponseDto = new ExchangeRateResponseDto().setSuccess(true)
                .setTimestamp(localDateTimeProvider.getTime())
                .setFrom(from)
                .setTo(to)
                .setRate(currRate);

        if (from.equals(BASE_CURRENCY)) {
            if (to.equals(BASE_CURRENCY)) {
                return exchangeRateResponseDto;
            }

            currRate = roundToNDecimals(quotes.get(BASE_CURRENCY + to), 6);
            return exchangeRateResponseDto
                    .setRate(currRate);

        }

        if (to.equals(BASE_CURRENCY)) {
            currRate = roundToNDecimals(1.0 / quotes.get(BASE_CURRENCY + from), 6);

            return exchangeRateResponseDto
                    .setRate(currRate);
        }

        Double fromBaseRate = quotes.get(BASE_CURRENCY + from);
        Double toBaseRate = quotes.get(BASE_CURRENCY + to);

        currRate = roundToNDecimals(toBaseRate / fromBaseRate, 6);

        return exchangeRateResponseDto
                .setRate(currRate);
    }

    public ConversionResponseDto calcAmount(Double amount, String from, String to) {
        AllRatesResponseDto rates = this.getRates();

        Map<String, Double> quotes = rates.getQuotes();

        ConversionResponseDto conversionResponseDto = new ConversionResponseDto().setSuccess(true)
                .setTimestamp(localDateTimeProvider.getTime())
                .setFrom(from)
                .setTo(to)
                .setResult(amount);

        if (from.equals(BASE_CURRENCY)) {
            if (to.equals(BASE_CURRENCY)) {
                return conversionResponseDto;
            }

            double currRate = roundToNDecimals(quotes.get(BASE_CURRENCY + to), 6);
            return conversionResponseDto.setResult(roundToNDecimals(amount * currRate, 2));
        }

        if (to.equals(BASE_CURRENCY)) {

            double currRate = roundToNDecimals(1.0 / quotes.get(BASE_CURRENCY + from), 6);
            return conversionResponseDto.setResult(roundToNDecimals(amount * currRate, 2));
        }

        Double fromBaseRate = quotes.get(BASE_CURRENCY + from);
        Double toBaseRate = quotes.get(BASE_CURRENCY + to);

        double currRate = toBaseRate / fromBaseRate;

        return conversionResponseDto.setResult(roundToNDecimals(amount * currRate, 2));
    }
}
