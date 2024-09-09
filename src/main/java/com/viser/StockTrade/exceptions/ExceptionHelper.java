package com.viser.StockTrade.exceptions;

import org.springframework.validation.BindingResult;

public class ExceptionHelper {

    /**
     * Throws a {@link ValidationException} if there are validation errors.
     *
     * This method checks if the {@link BindingResult} contains any validation errors. If errors are present, it throws
     * a {@link ValidationException} with the first error message and the provided redirect URL.
     *
     * @param result the {@link BindingResult} object that contains validation errors
     * @param redirectUrl the URL to redirect to if a validation exception is thrown
     * @throws ValidationException if there are validation errors in the {@link BindingResult}
     */
    public static void throwValidationException(BindingResult result, String redirectUrl) throws ValidationException {
        if (result.hasErrors()) {
            throw new ValidationException(result.getAllErrors().getFirst().getDefaultMessage(), redirectUrl);
        }
    }

    /**
     * Throws a {@link NotFoundException} if the given entity is null or, for Boolean entities, false.
     *
     * This method checks if the provided entity is null or, if the entity is of type {@link Boolean}, checks if it is
     * false. If either condition is met, it throws a {@link NotFoundException} with the specified message and redirect URL.
     *
     * @param <T> the type of the entity being checked
     * @param entity the entity to check for null or false value
     * @param message the message to include in the {@link NotFoundException} if the entity is null or false
     * @param redirectUrl the URL to redirect to if a {@link NotFoundException} is thrown
     * @throws NotFoundException if the entity is null or, if it's a {@link Boolean}, it is false
     */
    public static <T> void throwNotFoundException(T entity, String message, String redirectUrl) throws NotFoundException {
        if (entity == null) {
            throw new NotFoundException(message, redirectUrl);
        }
        if (entity instanceof Boolean && !(Boolean) entity) {
            throw new NotFoundException(message, redirectUrl);
        }
    }

    /**
     * Throws a {@link NameExistException} if the specified condition is true.
     *
     * This method checks the boolean flag `nameExist`. If this flag is true, indicating that the name already exists,
     * it throws a {@link NameExistException} with the provided message and redirect URL.
     *
     * @param nameExist a boolean flag indicating whether the name exists
     * @param message the message to include in the {@link NameExistException} if the name exists
     * @param redirectUrl the URL to redirect to if a {@link NameExistException} is thrown
     * @throws NameExistException if the `nameExist` flag is true
     */
    public static void throwNameExistException(boolean nameExist, String message, String redirectUrl) throws NameExistException {
        if (nameExist) {
            throw new NameExistException(message, redirectUrl);
        }
    }
}
