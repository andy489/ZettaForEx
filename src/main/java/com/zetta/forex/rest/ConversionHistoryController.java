package com.zetta.forex.rest;

import com.zetta.forex.config.GlobalExceptionHandler;
import com.zetta.forex.model.criteria.ConversionHistoryCriteria;
import com.zetta.forex.model.dto.ConversionHistoryResponse;
import com.zetta.forex.model.dto.ErrorResponse;
import com.zetta.forex.model.entity.ConversionHistoryEntity;
import com.zetta.forex.service.ConversionHistoryService;
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
public class ConversionHistoryController {

    private static final Logger LOGGER = LoggerFactory.getLogger(ConversionHistoryController.class);


    private final ConversionHistoryService service;

    @GetMapping("/history")
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
            return ResponseEntity.badRequest().body(new ErrorResponse(e.getMessage()));
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
