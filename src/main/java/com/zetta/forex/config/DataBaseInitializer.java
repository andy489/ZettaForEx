package com.zetta.forex.config;

import com.zetta.forex.service.ForexApiService;
import lombok.RequiredArgsConstructor;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.boot.CommandLineRunner;
import org.springframework.context.annotation.Configuration;

@Configuration
@RequiredArgsConstructor
public class DataBaseInitializer implements CommandLineRunner {

    private static final Logger LOGGER = LoggerFactory.getLogger(DataBaseInitializer.class);


    private final ForexApiService forexApiService;

    @Override
    public void run(String... args) throws Exception {

        LOGGER.info("Initializing DB: {}", forexApiService.getRates());
    }
}