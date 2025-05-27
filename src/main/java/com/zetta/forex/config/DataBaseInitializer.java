package com.zetta.forex.config;

import com.zetta.forex.model.dto.ExchangeRateResponseDto;
import com.zetta.forex.service.ZettaForexApiService;
import lombok.RequiredArgsConstructor;
import org.springframework.boot.CommandLineRunner;
import org.springframework.context.annotation.Configuration;

@Configuration
@RequiredArgsConstructor
public class DataBaseInitializer implements CommandLineRunner {

    private final ZettaForexApiService zettaForexApiService;

    @Override
    public void run(String... args) throws Exception {
        ExchangeRateResponseDto rates = zettaForexApiService.getRates();

        System.out.println(rates);

    }
}