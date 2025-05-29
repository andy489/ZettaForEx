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
import org.springframework.web.client.RestTemplate;
import org.springframework.web.util.UriComponentsBuilder;

import java.math.BigDecimal;
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

    @Value("${api_layer.url}")
    private String apiUrl;

    private final String BASE_CURRENCY = "EUR";

    private final RestTemplate restTemplate;

    private final MapStructMapper mapper;

    private final ExchangeRateRepository exchangeRateRepository;

    private final LocalDateTimeProvider localDateTimeProvider;

    public AllRatesResponseDto getRates() {
        if (debugMode == null || debugMode) {
            return onFailFallBack();
        }

        // Try to fetch from API first
        try {
            String url = UriComponentsBuilder.fromUriString(apiUrl)
                    .queryParam("access_key", apiKey)
                    .queryParam("source", BASE_CURRENCY)
                    .queryParam("currencies", allCurrencyCodes)
                    .toUriString();

            AllRatesResponseDto allRatesResponseDto = restTemplate.getForObject(url, AllRatesResponseDto.class);

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
                .setTimestamp(localDateTimeProvider.getTimeEpoch())
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
                .setTimestamp(localDateTimeProvider.getTimeEpoch())
                .setFrom(from)
                .setTo(to)
                .setAmount(BigDecimal.valueOf(roundToNDecimals(amount, 2)))
                .setResult(BigDecimal.valueOf(amount));

        if (from.equals(BASE_CURRENCY)) {
            if (to.equals(BASE_CURRENCY)) {
                return conversionResponseDto;
            }

            double currRate = roundToNDecimals(quotes.get(BASE_CURRENCY + to), 6);
            return conversionResponseDto.setResult(BigDecimal.valueOf(roundToNDecimals(amount * currRate, 2)));
        }

        if (to.equals(BASE_CURRENCY)) {

            double currRate = roundToNDecimals(1.0 / quotes.get(BASE_CURRENCY + from), 6);
            return conversionResponseDto.setResult(BigDecimal.valueOf(roundToNDecimals(amount * currRate, 2)));
        }

        Double fromBaseRate = quotes.get(BASE_CURRENCY + from);
        Double toBaseRate = quotes.get(BASE_CURRENCY + to);

        double currRate = toBaseRate / fromBaseRate;

        return conversionResponseDto.setResult(BigDecimal.valueOf(roundToNDecimals(amount * currRate, 2)));
    }
}
