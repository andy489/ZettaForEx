package com.zetta.forex.model.validation;

import jakarta.validation.ConstraintValidator;
import jakarta.validation.ConstraintValidatorContext;

import java.math.BigDecimal;

public class CurrencyAmountValidator implements ConstraintValidator<ValidCurrencyAmount, String> {

    private boolean allowNegative;
    private int maxFractionDigits;

    @Override
    public void initialize(ValidCurrencyAmount constraintAnnotation) {
        ConstraintValidator.super.initialize(constraintAnnotation);
        this.allowNegative = constraintAnnotation.allowNegative();
        this.maxFractionDigits = constraintAnnotation.maxFractionDigits();
    }

    @Override
    public boolean isValid(String value, ConstraintValidatorContext context) {
        if (value == null || value.isEmpty()) {
            return false;
        }

        try {
            BigDecimal amount = new BigDecimal(value);

            // Check scale (decimal places)
            if (amount.scale() > maxFractionDigits) {
                return false;
            }

            // Check it's a positive number
            if (amount.compareTo(BigDecimal.ZERO) < 0) {
                return allowNegative;
            }

            // https://regex101.com/
            return value.matches("^\\d+(\\.\\d{1,2})?$");
        } catch (NumberFormatException e) {
            return false;
        }
    }
}
