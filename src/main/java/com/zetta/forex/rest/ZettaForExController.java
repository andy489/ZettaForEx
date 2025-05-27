package com.zetta.forex.rest;

import com.zetta.forex.model.validation.ValidCurrencyAmount;
import com.zetta.forex.model.validation.ValidCurrencyCode;
import com.zetta.forex.service.ZettaForexApiService;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

@Validated
@RestController
@RequestMapping("/api")
public class ZettaForExController {

    private final ZettaForexApiService zettaForexApiService;

    public ZettaForExController(ZettaForexApiService zettaForexApiService) {
        this.zettaForexApiService = zettaForexApiService;
    }

    // Exchange Rate Endpoint:
    @GetMapping("/exchange")
    public ResponseEntity<Object> exchange(
            @RequestParam @ValidCurrencyCode String from,
            @RequestParam @ValidCurrencyCode String to) {

        return zettaForexApiService.calcRate(from.toUpperCase(), to.toUpperCase());
    }

    // Currency Conversion Endpoint:
    @GetMapping("/convert")
    public ResponseEntity<Object> exchange(
            @RequestParam @ValidCurrencyAmount String amount,
            @RequestParam @ValidCurrencyCode String from,
            @RequestParam @ValidCurrencyCode String to) {

        return zettaForexApiService.calcAmount(Double.valueOf(amount), from.toUpperCase(), to.toUpperCase());
    }
}
