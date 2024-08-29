package com.viser.StockTrade.validation;

import jakarta.validation.ConstraintValidator;
import jakarta.validation.ConstraintValidatorContext;

public class ValueValidator implements ConstraintValidator<ValidValue, String> {

    @Override
    public void initialize(ValidValue constraintAnnotation) {
    }

    @Override
    public boolean isValid(String value, ConstraintValidatorContext context) {
        if (value == null || value.isEmpty()) {
            return true;
        }
        return value.matches("\\d{8,11}");
    }
}
