package com.zetta.forex.model.validation;


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
@Constraint(validatedBy = CurrencyAmountValidator.class)
public @interface ValidCurrencyAmount {

    String message() default "Invalid currency amount format";

    Class<?>[] groups() default {};

    Class<? extends Payload>[] payload() default {};
    boolean allowNegative() default false;
    int maxFractionDigits() default 2;
}
