package com.zetta.forex.service;

import com.zetta.forex.config.LocalDateTimeProvider;
import com.zetta.forex.config.MapStructMapper;
import com.zetta.forex.model.dto.ConversionResponseDto;
import com.zetta.forex.model.dto.ExchangeRateResponseDto;
import com.zetta.forex.repo.ExchangeRateRepository;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.math.BigDecimal;
import java.util.Optional;

import static com.zetta.forex.util.TestConstants.ALL_RATES_RESPONSE_DTO;
import static com.zetta.forex.util.TestConstants.CURR_EPOCH_TIME;
import static com.zetta.forex.util.TestConstants.DELTA;
import static com.zetta.forex.util.TestConstants.EXCHANGE_RATES_ENTITY;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
public class ForexApiServiceUnitTest {

    @Mock
    private MapStructMapper mapper;

    @Mock
    private ExchangeRateRepository exchangeRateRepository;

    @Mock
    private LocalDateTimeProvider localDateTimeProvider;

    @InjectMocks
    private ForexApiService forexApiService;

    @Test
    @DisplayName("Get BGN to EUR Exchange Rate (Non-base Currency to base Currency)")
    void testBGNtoEUR_exchangeRate_withValidInput() {
        // Arrange
        when(exchangeRateRepository.findSingleton())
                .thenReturn(Optional.ofNullable(EXCHANGE_RATES_ENTITY));

        when(mapper.toDto(EXCHANGE_RATES_ENTITY))
                .thenReturn(ALL_RATES_RESPONSE_DTO);

        when(localDateTimeProvider.getTimeEpoch())
                .thenReturn(Long.valueOf(CURR_EPOCH_TIME));

        // Act
        ExchangeRateResponseDto exchangeRateResponseDto = forexApiService.calcRate("BGN", "EUR");

        // Assert
        assertEquals(0.511117, exchangeRateResponseDto.getRate(), DELTA,
                "Expected 0.511117 \"BGNEUR\" rate");

        assertEquals("BGN", exchangeRateResponseDto.getFrom(),
                "Expected \"BGN\" as source currency code");

        assertEquals("EUR", exchangeRateResponseDto.getTo(),
                "Expected \"EUR\" as target currency code");


        assertEquals(CURR_EPOCH_TIME, exchangeRateResponseDto.getTimestamp().toString(),
                "Expected timestamps to match");

        assertTrue(exchangeRateResponseDto.getSuccess(),
                "Expected DTO to be successfully created");
    }

    @Test
    @DisplayName("Get EUR to BGN Exchange Rate (Base Currency to Non-base)")
    void testBGNtoEUR_exchangeRate_withValidInputs() {
        // Arrange
        when(exchangeRateRepository.findSingleton())
                .thenReturn(Optional.ofNullable(EXCHANGE_RATES_ENTITY));

        when(mapper.toDto(EXCHANGE_RATES_ENTITY))
                .thenReturn(ALL_RATES_RESPONSE_DTO);

        when(localDateTimeProvider.getTimeEpoch())
                .thenReturn(Long.valueOf(CURR_EPOCH_TIME));

        // Act
        ExchangeRateResponseDto exchangeRateResponseDto = forexApiService.calcRate( "EUR", "BGN");

        // Assert
        assertEquals(1.956498, exchangeRateResponseDto.getRate(), DELTA,
                "Expected 1.956498 \"EURBGN\" rate");

        assertEquals("EUR", exchangeRateResponseDto.getFrom(),
                "Expected \"EUR\" as source currency code");

        assertEquals("BGN", exchangeRateResponseDto.getTo(),
                "Expected \"BGN\" as target currency code");


        assertEquals(CURR_EPOCH_TIME, exchangeRateResponseDto.getTimestamp().toString(),
                "Expected timestamps to match");

        assertTrue(exchangeRateResponseDto.getSuccess(),
                "Expected DTO to be successfully created");
    }

    @Test
    @DisplayName("Get EUR to EUR Exchange Rate (Base to Base Currency)")
    void testEURtoEUR_exchangeRate_withValidInputs() {
        // Arrange
        when(exchangeRateRepository.findSingleton())
                .thenReturn(Optional.ofNullable(EXCHANGE_RATES_ENTITY));

        when(mapper.toDto(EXCHANGE_RATES_ENTITY))
                .thenReturn(ALL_RATES_RESPONSE_DTO);

        when(localDateTimeProvider.getTimeEpoch())
                .thenReturn(Long.valueOf(CURR_EPOCH_TIME));

        // Act
        ExchangeRateResponseDto exchangeRateResponseDto = forexApiService.calcRate( "EUR", "EUR");

        // Assert
        assertEquals(1.0, exchangeRateResponseDto.getRate(), DELTA,
                "Expected 1.0 \"EUREUR\" rate");

        assertEquals("EUR", exchangeRateResponseDto.getFrom(),
                "Expected \"EUR\" as source currency code");

        assertEquals("EUR", exchangeRateResponseDto.getTo(),
                "Expected \"EUR\" as target currency code");


        assertEquals(CURR_EPOCH_TIME, exchangeRateResponseDto.getTimestamp().toString(),
                "Expected timestamps to match");

        assertTrue(exchangeRateResponseDto.getSuccess(),
                "Expected DTO to be successfully created");
    }

    @Test
    @DisplayName("Get GBP to BGN Exchange Rate (Non-base to Non-base Currency)")
    void testGBPtoBGN_exchangeRate_withValidInputs() {
        // Arrange
        when(exchangeRateRepository.findSingleton())
                .thenReturn(Optional.ofNullable(EXCHANGE_RATES_ENTITY));

        when(mapper.toDto(EXCHANGE_RATES_ENTITY))
                .thenReturn(ALL_RATES_RESPONSE_DTO);

        when(localDateTimeProvider.getTimeEpoch())
                .thenReturn(Long.valueOf(CURR_EPOCH_TIME));

        // Act
        ExchangeRateResponseDto exchangeRateResponseDto = forexApiService.calcRate( "GBP", "BGN");

        // Assert
        assertEquals(2.333996, exchangeRateResponseDto.getRate(),
                "Expected 2.333996 \"GBPBGN\" rate");

        assertEquals("GBP", exchangeRateResponseDto.getFrom(),
                "Expected \"GBP\" as source currency code");

        assertEquals("BGN", exchangeRateResponseDto.getTo(),
                "Expected \"BGN\" as target currency code");


        assertEquals(CURR_EPOCH_TIME, exchangeRateResponseDto.getTimestamp().toString(),
                "Expected timestamps to match");

        assertTrue(exchangeRateResponseDto.getSuccess(),
                "Expected DTO to be successfully created");
    }


    @Test
    @DisplayName("Get GB to BG Exchange Rate (Invalid to Invalid Currency)")
    void testGBPtoBGN_exchangeRate_withInvalidInputs_throwsNullPointerException() {
        // Arrange
        when(exchangeRateRepository.findSingleton())
                .thenReturn(Optional.ofNullable(EXCHANGE_RATES_ENTITY));

        when(mapper.toDto(EXCHANGE_RATES_ENTITY))
                .thenReturn(ALL_RATES_RESPONSE_DTO);

        when(localDateTimeProvider.getTimeEpoch())
                .thenReturn(Long.valueOf(CURR_EPOCH_TIME));

        // Act & Assert
        assertThrows(NullPointerException.class, () -> forexApiService.calcRate( "GB", "BG"),
                "Expects NullPointerException since \"BG\" and \"GB\" are not present in \"quotes\" map keys.");
    }



    @Test
    @DisplayName("Get GBP to BGN Amount Converted (Non-base to Base Currency)")
    void testGBPtoBGN_amountConverted_withValidInputs() {
        // Arrange
        when(exchangeRateRepository.findSingleton())
                .thenReturn(Optional.ofNullable(EXCHANGE_RATES_ENTITY));

        when(mapper.toDto(EXCHANGE_RATES_ENTITY))
                .thenReturn(ALL_RATES_RESPONSE_DTO);

        when(localDateTimeProvider.getTimeEpoch())
                .thenReturn(Long.valueOf(CURR_EPOCH_TIME));

        // Act
        ConversionResponseDto conversionResponseDto =
                forexApiService.calcAmount(220.50, "GBP", "BGN");

        // Assert
        assertEquals(0, BigDecimal.valueOf(514.65).compareTo(conversionResponseDto.getResult()),
                "Expected 514.65 \"GBPBGN\" as amount");

        assertEquals("GBP", conversionResponseDto.getFrom(),
                "Expected \"GBP\" as source currency code");

        assertEquals("BGN", conversionResponseDto.getTo(),
                "Expected \"BGN\" as target currency code");


        assertEquals(CURR_EPOCH_TIME, conversionResponseDto.getTimestamp().toString(),
                "Expected timestamps to match");

        assertTrue(conversionResponseDto.getSuccess(),
                "Expected DTO to be successfully created");
    }

    @Test
    @DisplayName("Get USD to USD Amount Converted (Non-base to Base Currency)")
    void testGBPtoGBP_amountConverted_withValidInputs() {
        // Arrange
        when(exchangeRateRepository.findSingleton())
                .thenReturn(Optional.ofNullable(EXCHANGE_RATES_ENTITY));

        when(mapper.toDto(EXCHANGE_RATES_ENTITY))
                .thenReturn(ALL_RATES_RESPONSE_DTO);

        when(localDateTimeProvider.getTimeEpoch())
                .thenReturn(Long.valueOf(CURR_EPOCH_TIME));

        // Act
        ConversionResponseDto conversionResponseDto =
                forexApiService.calcAmount(1200.5, "USD", "USD");

        // Assert
        assertEquals(0, BigDecimal.valueOf(1200.5).compareTo(conversionResponseDto.getResult()),
                "Expected 1200.5 \"USDUSD\" as amount");

        assertEquals("USD", conversionResponseDto.getFrom(),
                "Expected \"USD\" as source currency code");

        assertEquals("USD", conversionResponseDto.getTo(),
                "Expected \"USD\" as target currency code");


        assertEquals(CURR_EPOCH_TIME, conversionResponseDto.getTimestamp().toString(),
                "Expected timestamps to match");

        assertTrue(conversionResponseDto.getSuccess(),
                "Expected DTO to be successfully created");
    }

    @Test
    @DisplayName("Get EUR to EUR Amount Converted (Base to Base Currency)")
    void testEURtoEUR_amountConverted_withValidInputs() {
        // Arrange
        when(exchangeRateRepository.findSingleton())
                .thenReturn(Optional.ofNullable(EXCHANGE_RATES_ENTITY));

        when(mapper.toDto(EXCHANGE_RATES_ENTITY))
                .thenReturn(ALL_RATES_RESPONSE_DTO);

        when(localDateTimeProvider.getTimeEpoch())
                .thenReturn(Long.valueOf(CURR_EPOCH_TIME));

        // Act
        ConversionResponseDto conversionResponseDto =
                forexApiService.calcAmount(333.0, "EUR", "EUR");

        // Assert
        assertEquals(0, BigDecimal.valueOf(333.0).compareTo(conversionResponseDto.getResult()),
                "Expected 333.0 \"EUREUR\" as amount");

        assertEquals("EUR", conversionResponseDto.getFrom(),
                "Expected \"EUR\" as source currency code");

        assertEquals("EUR", conversionResponseDto.getTo(),
                "Expected \"EUR\" as USD currency code");


        assertEquals(CURR_EPOCH_TIME, conversionResponseDto.getTimestamp().toString(),
                "Expected timestamps to match");

        assertTrue(conversionResponseDto.getSuccess(),
                "Expected DTO to be successfully created");
    }
}
