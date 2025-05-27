package com.zetta.forex.config;

import com.zetta.forex.model.dto.ExchangeRateResponseDto;
import com.zetta.forex.service.ZettaForexApiService;
import org.springframework.boot.CommandLineRunner;
import org.springframework.context.annotation.Configuration;

@Configuration
public class DataBaseInitializer implements CommandLineRunner {

    private final ZettaForexApiService zettaForexApiService;

    public DataBaseInitializer(ZettaForexApiService zettaForexApiService) {
        this.zettaForexApiService = zettaForexApiService;
    }

    @Override
    public void run(String... args) throws Exception {
        ExchangeRateResponseDto rates = zettaForexApiService.getRates();

        System.out.println(rates);

    }
}