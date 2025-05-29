package com.zetta.forex.rest;

import com.zetta.forex.model.dto.ConversionResponseDto;
import com.zetta.forex.model.dto.ErrorResponseDto;
import com.zetta.forex.model.dto.ExchangeRateResponseDto;
import com.zetta.forex.model.validation.ValidCurrencyAmount;
import com.zetta.forex.model.validation.ValidCurrencyCode;
import com.zetta.forex.service.ForexApiService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.tags.Tag;
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
@Tag(name = "Forex API", description = "Currency exchange rate operations")
public class ForexController {

    private final ForexApiService forexApiService;

    // Exchange Rate Endpoint:
    @Operation(
            summary = "Get exchange rate",
            description = "Calculates the exchange rate between two currencies",
            parameters = {
                    @Parameter(
                            name = "from",
                            description = "Source currency code (ISO 4217)",
                            example = "USD",
                            required = true,
                            schema = @Schema(implementation = String.class)
                    ),
                    @Parameter(
                            name = "to",
                            description = "Target currency code (ISO 4217)",
                            example = "EUR",
                            required = true,
                            schema = @Schema(implementation = String.class))
            },
            responses = {
                    @ApiResponse(
                            responseCode = "200",
                            description = "Successful exchange rate calculation",
                            content = @Content(
                                    mediaType = "application/json",
                                    schema = @Schema(implementation = ExchangeRateResponseDto.class))),
                    @ApiResponse(
                            responseCode = "400",
                            description = "Invalid currency code provided"),
                    @ApiResponse(
                            responseCode = "500",
                            description = "Internal server error")
            }
    )
    @GetMapping("/rate")
    public ResponseEntity<ExchangeRateResponseDto> exchange(
            @RequestParam @ValidCurrencyCode String from,
            @RequestParam @ValidCurrencyCode String to) {

        return ResponseEntity.ok(forexApiService.calcRate(from.toUpperCase(), to.toUpperCase()));
    }

    // Currency Conversion Endpoint:
    @Operation(
            summary = "Convert currency amount",
            description = "Converts a specified amount from one currency to another",
            parameters = {
                    @Parameter(
                            name = "amount",
                            description = "Amount to convert (positive decimal number)",
                            example = "100.50",
                            required = true,
                            schema = @Schema(type = "string", format = "decimal", pattern = "^\\d+(\\.\\d{1,2})?$")
                    ),
                    @Parameter(
                            name = "from",
                            description = "Source currency code (ISO 4217)",
                            example = "USD",
                            required = true,
                            schema = @Schema(
                                    implementation = String.class,
                                    pattern = "^[A-Z]{3}$",
                                    minLength = 3,
                                    maxLength = 3
                            )
                    ),
                    @Parameter(
                            name = "to",
                            description = "Target currency code (ISO 4217)",
                            example = "EUR",
                            required = true,
                            schema = @Schema(
                                    implementation = String.class,
                                    pattern = "^[A-Z]{3}$",
                                    minLength = 3,
                                    maxLength = 3
                            )
                    )
            },
            responses = {
                    @ApiResponse(
                            responseCode = "200",
                            description = "Successful currency conversion",
                            content = @Content(
                                    mediaType = "application/json",
                                    schema = @Schema(implementation = ConversionResponseDto.class)
                            )
                    ),
                    @ApiResponse(
                            responseCode = "400",
                            description = "Invalid input parameters",
                            content = @Content(
                                    mediaType = "application/json",
                                    schema = @Schema(implementation = ErrorResponseDto.class)
                            )
                    ),
                    @ApiResponse(
                            responseCode = "404",
                            description = "Currency pair not supported",
                            content = @Content(
                                    mediaType = "application/json",
                                    schema = @Schema(implementation = ErrorResponseDto.class)
                            )
                    ),
                    @ApiResponse(
                            responseCode = "500",
                            description = "Internal server error",
                            content = @Content(
                                    mediaType = "application/json",
                                    schema = @Schema(implementation = ErrorResponseDto.class)
                            )
                    )
            }
    )
    @GetMapping("/exchange")
    public ResponseEntity<ConversionResponseDto> exchange(
            @RequestParam @ValidCurrencyAmount String amount,
            @RequestParam @ValidCurrencyCode String from,
            @RequestParam @ValidCurrencyCode String to) {

        return ResponseEntity.ok(forexApiService.calcAmount(Double.valueOf(amount),
                from.toUpperCase(),
                to.toUpperCase()));
    }
}
