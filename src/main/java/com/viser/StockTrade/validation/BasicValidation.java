package com.viser.StockTrade.validation;

import jakarta.validation.Constraint;
import jakarta.validation.Payload;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

@Constraint(validatedBy = BasicStringValidation.class)
@Target({ElementType.FIELD, ElementType.PARAMETER})
@Retention(RetentionPolicy.RUNTIME)
public @interface BasicValidation {

    String message() default "Invalid string";

    int minLength() default 3;

    Class<?>[] groups() default {};

    Class<? extends Payload>[] payload() default {};
}
