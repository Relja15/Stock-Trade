package com.viser.StockTrade.validation;

import jakarta.validation.ConstraintValidator;
import jakarta.validation.ConstraintValidatorContext;

public class BasicStringValidation implements ConstraintValidator<BasicValidation, String> {

    private int minLength;

    @Override
    public void initialize(BasicValidation constraintAnnotation) {
        this.minLength = constraintAnnotation.minLength();
    }

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
