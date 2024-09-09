package com.viser.StockTrade.validation;

import jakarta.validation.ConstraintValidator;
import jakarta.validation.ConstraintValidatorContext;

public class ValueValidator implements ConstraintValidator<ValidValue, String> {

    @Override
    public void initialize(ValidValue constraintAnnotation) {
    }

    /**
     * Validates if the given string value matches a specific pattern for numeric values.
     *
     * @param value the string value to be validated
     * @param context the context in which the constraint is being evaluated
     * @return {@code true} if the value is {@code null}, empty, or matches the pattern, {@code false} otherwise
     */
    @Override
    public boolean isValid(String value, ConstraintValidatorContext context) {
        if (value == null || value.isEmpty()) {
            return true;
        }
        return value.matches("\\d{8,11}");
    }
}
