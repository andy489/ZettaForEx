package com.zetta.forex.rest;

import com.zetta.forex.model.dto.ConversionResponseDto;
import com.zetta.forex.model.dto.ExchangeRateResponseDto;
import com.zetta.forex.model.validation.ValidCurrencyAmount;
import com.zetta.forex.model.validation.ValidCurrencyCode;
import com.zetta.forex.service.ForexApiService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

@Validated
@RestController
@RequestMapping("/api")
@RequiredArgsConstructor
public class ForexController {

    private final ForexApiService forexApiService;

    // Exchange Rate Endpoint:
    @GetMapping("/exchange")
    public ResponseEntity<ExchangeRateResponseDto> exchange(
            @RequestParam @ValidCurrencyCode String from,
            @RequestParam @ValidCurrencyCode String to) {

        return ResponseEntity.ok(forexApiService.calcRate(from.toUpperCase(), to.toUpperCase()));
    }

    // Currency Conversion Endpoint:
    @GetMapping("/convert")
    public ResponseEntity<ConversionResponseDto> exchange(
            @RequestParam @ValidCurrencyAmount String amount,
            @RequestParam @ValidCurrencyCode String from,
            @RequestParam @ValidCurrencyCode String to) {

        return ResponseEntity.ok(forexApiService.calcAmount(Double.valueOf(amount),
                from.toUpperCase(),
                to.toUpperCase()));
    }
}
