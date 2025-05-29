package com.zetta.forex.service;

import com.zetta.forex.model.entity.ExchangeRatesEntity;
import com.zetta.forex.repo.ExchangeRateRepository;
import com.zetta.forex.util.TestConstants;
import jakarta.transaction.Transactional;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Disabled;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.mockito.Mock;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.web.servlet.MockMvc;

import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;

@SpringBootTest
@AutoConfigureMockMvc
@ActiveProfiles("test")
@Transactional
@Disabled
public class ForexControllerIntegrationTest {

    @Autowired
    private MockMvc mockMvc;

    @Mock
    private static ExchangeRateRepository exchangeRateRepository;

    @BeforeEach
    void setUp(){
        exchangeRateRepository.saveSingleton(TestConstants.EXCHANGE_RATES_ENTITY);
    }

    @Test
    @DisplayName("GET /api/exchange?from=BGN&to=EUR - Success")
    void getExchangeRate_BGNEUR_returnsRate() throws Exception {

        mockMvc.perform(get("/api/exchange")
                        .param("from", "BGN")
                        .param("to", "EUR"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.from").value("BGN"))
                .andExpect(jsonPath("$.to").value("EUR"))
                .andExpect(jsonPath("$.rate").value(0.511117));
    }

    @Test
    @DisplayName("GET /api/exchange?from=EUR&to=BGN - Success")
    void getExchangeRate_EURBGN_returnsRate() throws Exception {

        mockMvc.perform(get("/api/exchange")
                        .param("from", "EUR")
                        .param("to", "BGN"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.from").value("EUR"))
                .andExpect(jsonPath("$.to").value("BGN"))
                .andExpect(jsonPath("$.rate").value(1.956498));
    }

    @Test
    @DisplayName("GET /api/exchange?from=EUR&to=EUR - Success")
    void getExchangeRate_EUREUR_returnsRate() throws Exception {

        mockMvc.perform(get("/api/exchange")
                        .param("from", "EUR")
                        .param("to", "EUR"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.from").value("EUR"))
                .andExpect(jsonPath("$.to").value("EUR"))
                .andExpect(jsonPath("$.rate").value(1.0));
    }

    @Test
    @DisplayName("GET /api/exchange?from=BGN&to=BGN - Success")
    void getExchangeRate_BGNBGN_returnsRate() throws Exception {

        mockMvc.perform(get("/api/exchange")
                        .param("from", "BGN")
                        .param("to", "BGN"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.from").value("BGN"))
                .andExpect(jsonPath("$.to").value("BGN"))
                .andExpect(jsonPath("$.rate").value(1.0));
    }

    @Test
    @DisplayName("GET /api/exchange?from=BGN&to=AAA - Fails")
    void getExchangeRate_BGNAAA_invalidTargetCurrencyCode() throws Exception {

        mockMvc.perform(get("/api/exchange")
                        .param("from", "BGN")
                        .param("to", "AAA"))
                .andExpect(status().isBadRequest())
                .andExpect(jsonPath("$.message").value("Validation failed"));
    }
}
