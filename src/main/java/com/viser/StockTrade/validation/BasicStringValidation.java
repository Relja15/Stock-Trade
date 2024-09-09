package com.viser.StockTrade.validation;

import jakarta.validation.ConstraintValidator;
import jakarta.validation.ConstraintValidatorContext;

public class BasicStringValidation implements ConstraintValidator<BasicValidation, String> {

    private int minLength;

    @Override
    public void initialize(BasicValidation constraintAnnotation) {
        this.minLength = constraintAnnotation.minLength();
    }

    /**
     * Validates a given string based on certain criteria.
     *
     * @param value the string value to be validated
     * @param context the context in which the constraint is being evaluated
     * @return {@code true} if the value is valid according to the criteria, {@code false} otherwise
     */
    @Override
    public boolean isValid(String value, ConstraintValidatorContext context) {
        if (value == null || value.isEmpty()) {
            return true;
        }
        if (value.trim().isEmpty()) {
            return false;
        }
        return value.trim().length() >= minLength;
    }
}
