package com.zetta.forex.model.validation;

import jakarta.validation.ConstraintValidator;
import jakarta.validation.ConstraintValidatorContext;
import org.springframework.beans.factory.annotation.Value;

import java.util.List;

public class CurrencyCodeValidator implements ConstraintValidator<ValidCurrencyCode, String> {

    private int currencyCodeLength;

    @Value("${api_layer.all_currency_codes}")
    private List<String> allCurrencyCodes;


    @Override
    public void initialize(ValidCurrencyCode constraintAnnotation) {
        ConstraintValidator.super.initialize(constraintAnnotation);
        this.currencyCodeLength = constraintAnnotation.currencyCodeLength();

    }

    @Override
    public boolean isValid(String value, ConstraintValidatorContext context) {

        if (value.length() != currencyCodeLength) {
            return false;
        }

        return allCurrencyCodes.contains(value.toUpperCase());
    }
}
