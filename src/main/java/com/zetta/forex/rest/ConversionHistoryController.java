package com.zetta.forex.rest;

import com.zetta.forex.model.criteria.ConversionHistoryCriteria;
import com.zetta.forex.model.dto.ConversionHistoryResponse;
import com.zetta.forex.model.dto.ErrorResponseDto;
import com.zetta.forex.model.entity.ConversionHistoryEntity;
import com.zetta.forex.service.ConversionHistoryService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.enums.ParameterIn;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.time.ZoneId;
import java.time.ZoneOffset;
import java.util.ArrayList;
import java.util.List;

@RestController
@RequestMapping("/api")
@RequiredArgsConstructor
@Tag(name = "Conversion History", description = "Logs every currency conversion")
public class ConversionHistoryController {

    private static final Logger LOGGER = LoggerFactory.getLogger(ConversionHistoryController.class);

    private final ConversionHistoryService service;

    @GetMapping("/history")
    @Operation(
            summary = "Get currency conversion history",
            description = "Retrieves a paginated list of currency conversion records with optional filtering parameters",
            parameters = {
                    @Parameter(
                            name = "fromDate",
                            description = "Start date/time filter (inclusive) in ISO format (yyyy-MM-dd'T'HH:mm:ss)",
                            in = ParameterIn.QUERY,
                            schema = @Schema(type = "string", format = "date-time", example = "2023-01-01T00:00:00")
                    ),
                    @Parameter(
                            name = "toDate",
                            description = "End date/time filter (inclusive) in ISO format (yyyy-MM-dd'T'HH:mm:ss)",
                            in = ParameterIn.QUERY,
                            schema = @Schema(type = "string", format = "date-time", example = "2023-12-31T23:59:59")
                    ),
                    @Parameter(
                            name = "sourceCurrency",
                            description = "Filter by source currency code (3-letter ISO code)",
                            in = ParameterIn.QUERY,
                            schema = @Schema(type = "string", example = "USD")
                    ),
                    @Parameter(
                            name = "targetCurrency",
                            description = "Filter by target currency code (3-letter ISO code)",
                            in = ParameterIn.QUERY,
                            schema = @Schema(type = "string", example = "EUR")
                    ),
                    @Parameter(
                            name = "minAmount",
                            description = "Filter by minimum converted amount",
                            in = ParameterIn.QUERY,
                            schema = @Schema(type = "number", format = "decimal", example = "100.00")
                    ),
                    @Parameter(
                            name = "maxAmount",
                            description = "Filter by maximum converted amount",
                            in = ParameterIn.QUERY,
                            schema = @Schema(type = "number", format = "decimal", example = "1000.00")
                    ),
                    @Parameter(
                            name = "page",
                            description = "Page number (zero-based)",
                            in = ParameterIn.QUERY,
                            schema = @Schema(type = "integer", defaultValue = "0")
                    ),
                    @Parameter(
                            name = "size",
                            description = "Number of items per page",
                            in = ParameterIn.QUERY,
                            schema = @Schema(type = "integer", defaultValue = "10")
                    ),
                    @Parameter(
                            name = "sort",
                            description = "Sorting criteria in the format: property(,asc|desc). Default sort is timestamp,desc",
                            in = ParameterIn.QUERY,
                            schema = @Schema(type = "string", defaultValue = "timestamp,desc")
                    )
            },
            responses = {
                    @ApiResponse(
                            responseCode = "200",
                            description = "Successfully retrieved conversion history",
                            content = @Content(mediaType = "application/json",
                                    schema = @Schema(implementation = Page.class))
                    ),
                    @ApiResponse(
                            responseCode = "400",
                            description = "Invalid request parameters",
                            content = @Content(mediaType = "application/json")
                    ),
                    @ApiResponse(
                            responseCode = "500",
                            description = "Internal server error",
                            content = @Content(mediaType = "application/json")
                    )
            }
    )
    public ResponseEntity<?> getConversionHistory(
            @RequestParam(required = false) @DateTimeFormat(iso = DateTimeFormat.ISO.DATE_TIME) LocalDateTime fromDate,
            @RequestParam(required = false) @DateTimeFormat(iso = DateTimeFormat.ISO.DATE_TIME) LocalDateTime toDate,
            @RequestParam(required = false) String sourceCurrency,
            @RequestParam(required = false) String targetCurrency,
            @RequestParam(required = false) BigDecimal minAmount,
            @RequestParam(required = false) BigDecimal maxAmount,
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "10") int size,
            @RequestParam(defaultValue = "timestamp,desc") String sort) {

        LOGGER.info("Received fromDate: {}, toDate: {}", fromDate, toDate);

        if (fromDate != null) {
            fromDate = fromDate.plusHours(3);
        }

        if (toDate != null) {
            toDate = toDate.plusHours(3);
        }

        // Parse sort parameter
        Sort sortObj;
        try {
            String[] sortParts = sort.split(",");
            if (sortParts.length != 2) {
                throw new IllegalArgumentException("Sort parameter must be in format 'property,direction'");
            }

            String property = sortParts[0].trim();
            String directionStr = sortParts[1].trim().toLowerCase();

            // Validate property exists
            try {
                ConversionHistoryEntity.class.getDeclaredField(property);
            } catch (NoSuchFieldException e) {
                throw new IllegalArgumentException("Invalid sort property: " + property +
                        ". Valid properties are: timestamp, source, target, amount, result");
            }

            // Validate direction
            Sort.Direction direction = Sort.Direction.fromString(directionStr);
            sortObj = Sort.by(direction, property);
        } catch (IllegalArgumentException e) {
            return ResponseEntity.badRequest().body(new ErrorResponseDto().setError(e.getMessage()));
        }

        // Build criteria and query
        ConversionHistoryCriteria criteria = ConversionHistoryCriteria.builder()
                .fromDate(fromDate)
                .toDate(toDate)
                .sourceCurrency(sourceCurrency)
                .targetCurrency(targetCurrency)
                .minAmount(minAmount)
                .maxAmount(maxAmount)
                .build();

        Pageable pageable = PageRequest.of(page, size, sortObj);
        Page<ConversionHistoryEntity> result = service.getConversionHistory(criteria, pageable);

        return ResponseEntity.ok(ConversionHistoryResponse.fromPage(result));
    }

    private Sort parseSortParameters(String[] sort) {
        List<Sort.Order> orders = new ArrayList<>();

        for (String sortParam : sort) {
            String[] parts = sortParam.split(",");
            if (parts.length == 0) continue;

            String property = parts[0].trim();
            validateSortProperty(property); // Check if property exists

            Sort.Direction direction = parts.length > 1
                    ? Sort.Direction.fromString(parts[1].trim().toLowerCase())
                    : Sort.Direction.ASC;

            orders.add(new Sort.Order(direction, property));
        }

        return orders.isEmpty() ? Sort.by(Sort.Direction.DESC, "timestamp") : Sort.by(orders);
    }

    private void validateSortProperty(String property) {
        try {
            ConversionHistoryEntity.class.getDeclaredField(property);
        } catch (NoSuchFieldException e) {
            throw new IllegalArgumentException("Invalid sort property: " + property);
        }
    }

    private LocalDateTime convertToUtc(LocalDateTime dateTime) {
        if (dateTime == null) return null;
        return dateTime.atZone(ZoneId.systemDefault())
                .withZoneSameInstant(ZoneOffset.UTC)
                .toLocalDateTime();
    }

}
