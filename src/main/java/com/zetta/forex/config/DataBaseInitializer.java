package com.zetta.forex.config;

import com.zetta.forex.model.dto.ExchangeRateResponseDto;
import com.zetta.forex.service.ForexApiService;
import org.springframework.boot.CommandLineRunner;
import org.springframework.context.annotation.Configuration;

@Configuration
public class DataBaseInitializer implements CommandLineRunner {

    private final ForexApiService forexApiService;

    public DataBaseInitializer(ForexApiService forexApiService) {
        this.forexApiService = forexApiService;
    }

    @Override
    public void run(String... args) throws Exception {
        ExchangeRateResponseDto rates = forexApiService.getRates();



    }
}