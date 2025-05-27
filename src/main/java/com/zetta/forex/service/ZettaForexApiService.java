package com.zetta.forex.service;

import com.zetta.forex.aop.ConversionHistoryMarker;
import com.zetta.forex.config.LocalDateTimeProvider;
import com.zetta.forex.config.MapStructMapper;
import com.zetta.forex.model.dto.ExchangeRateResponseDto;
import com.zetta.forex.model.dto.ZettaConversionResponseDto;
import com.zetta.forex.model.dto.ZettaExchangeRateResponseDto;
import com.zetta.forex.model.entity.ExchangeRatesEntity;
import com.zetta.forex.repo.ExchangeRateRepository;
import jakarta.persistence.EntityNotFoundException;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.web.reactive.function.client.WebClient;

import java.time.Duration;
import java.util.Map;

import static com.zetta.forex.util.Util.roundToNDecimals;

@Service
//@Transactional
@RequiredArgsConstructor
public class ZettaForexApiService {

    private static final int INVALID_CURRENCY_CODE = 1001;
    private static final int NO_SUCH_CURRENCY_CODE = 1002;

    private static final int CURRENCY_LENGTH = 3;

    private static final boolean debug = false;

    private static final Logger LOGGER = LoggerFactory.getLogger(ZettaForexApiService.class);

    @Value("${api_layer.api_key}")
    private String apiKey;

    @Value("${api_layer.base_currency_code}")
    private String baseCurrency;

    @Value("${api_layer.all_currency_codes}")
    private String allCurrencyCodes;

    private final WebClient webClient;

    private final MapStructMapper mapper;

    private final ExchangeRateRepository exchangeRateRepository;

    private final LocalDateTimeProvider localDateTimeProvider;

    public ExchangeRateResponseDto getRates() {
        if (debug) {
            return onFailFallBack();
        }

        // Try to fetch from API first
        try {
            ExchangeRateResponseDto exchangeRateResponseDto = webClient.get()
                    .uri(uriBuilder -> uriBuilder
                            .queryParam("access_key", apiKey)
                            .queryParam("source", baseCurrency)
                            .queryParam("currencies", allCurrencyCodes)
                            .build())
                    .retrieve()
                    .bodyToMono(ExchangeRateResponseDto.class)
                    .timeout(Duration.ofSeconds(3))
                    .block();

            assert exchangeRateResponseDto != null;
            if (exchangeRateResponseDto.getSuccess()) {
                // Map and save to database
                ExchangeRatesEntity exchangeRatesEntity = mapper.toEntity(exchangeRateResponseDto);

                exchangeRateRepository.saveSingleton(exchangeRatesEntity);

                return exchangeRateResponseDto;
            } else {
                return onFailFallBack();
            }
        } catch (Exception e) {
            return onFailFallBack();
        }
    }

    private ExchangeRateResponseDto onFailFallBack() {
        // If API call fails, fall back to database
        ExchangeRatesEntity exchangeRatesEntity = exchangeRateRepository.findSingleton()
                .orElseThrow(() -> new EntityNotFoundException("Data not found in API or database"));

        return mapper.toDto(exchangeRatesEntity);
    }

    public ResponseEntity<ZettaExchangeRateResponseDto> calcRate(String from, String to) {
        ExchangeRateResponseDto rates = this.getRates();

        Map<String, Double> quotes = rates.getQuotes();

        double currRate = 1.0;

        ZettaExchangeRateResponseDto zettaExchangeRateResponseDto = new ZettaExchangeRateResponseDto().setSuccess(true)
                .setTimestamp(localDateTimeProvider.getTime())
                .setFrom(from)
                .setTo(to)
                .setRate(currRate);

        if (from.equals(baseCurrency)) {
            if (to.equals(baseCurrency)) {
                return ResponseEntity.ok(zettaExchangeRateResponseDto);
            }

            currRate = roundToNDecimals(quotes.get(baseCurrency + to), 6);
            return ResponseEntity.ok(zettaExchangeRateResponseDto
                    .setRate(currRate));

        }

        if (to.equals(baseCurrency)) {
            currRate = roundToNDecimals(1.0 / quotes.get(baseCurrency + from), 6);

            return ResponseEntity.ok(zettaExchangeRateResponseDto
                    .setRate(currRate));
        }

        Double fromBaseRate = quotes.get(baseCurrency + from);
        Double toBaseRate = quotes.get(baseCurrency + to);

        currRate = roundToNDecimals(toBaseRate / fromBaseRate, 6);

        return ResponseEntity.ok(zettaExchangeRateResponseDto
                .setRate(currRate));
    }

    public ResponseEntity<ZettaConversionResponseDto> calcAmount(Double amount, String from, String to) {
        ExchangeRateResponseDto rates = this.getRates();

        Map<String, Double> quotes = rates.getQuotes();

        ZettaConversionResponseDto zettaConversionResponseDto = new ZettaConversionResponseDto().setSuccess(true)
                .setTimestamp(localDateTimeProvider.getTime())
                .setFrom(from)
                .setTo(to)
                .setResult(amount);

        if (from.equals(baseCurrency)) {
            if (to.equals(baseCurrency)) {
                return ResponseEntity.ok(zettaConversionResponseDto);
            }

            double currRate = roundToNDecimals(quotes.get(baseCurrency + to), 6);
            return ResponseEntity.ok(zettaConversionResponseDto
                    .setResult(roundToNDecimals(amount * currRate, 2)));
        }

        if (to.equals(baseCurrency)) {

            double currRate = roundToNDecimals(1.0 / quotes.get(baseCurrency + from), 6);
            return ResponseEntity.ok(zettaConversionResponseDto
                    .setResult(roundToNDecimals(amount * currRate, 2)));

        }

        Double fromBaseRate = quotes.get(baseCurrency + from);
        Double toBaseRate = quotes.get(baseCurrency + to);

        double currRate = toBaseRate / fromBaseRate;

        return ResponseEntity.ok(zettaConversionResponseDto
                .setResult(roundToNDecimals(amount * currRate, 2)));
    }
}
