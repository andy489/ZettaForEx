package com.zetta.forex.model.validation;


import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.Constraint;
import jakarta.validation.Payload;

import java.lang.annotation.Documented;
import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

@Documented
@Retention(RetentionPolicy.RUNTIME)
@Target({ElementType.FIELD, ElementType.PARAMETER})
@Constraint(validatedBy = CurrencyCodeValidator.class)
@Schema(description = "ISO 4217 currency code", example = "USD", pattern = "^[A-Z]{3}$")
public @interface ValidCurrencyCode {

    String message() default "Invalid currency code";

    Class<?>[] groups() default {};

    Class<? extends Payload>[] payload() default {};
    boolean allowNegative() default false;
    int currencyCodeLength() default 3;
}
