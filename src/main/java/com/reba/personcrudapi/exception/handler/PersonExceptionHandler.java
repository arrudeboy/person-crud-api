package com.reba.personcrudapi.exception.handler;

import com.fasterxml.jackson.databind.exc.InvalidFormatException;
import com.reba.personcrudapi.exception.ContactNotFoundException;
import com.reba.personcrudapi.exception.ContactRemoveNotAllowedException;
import com.reba.personcrudapi.exception.PersonAlreadyExistsException;
import com.reba.personcrudapi.exception.PersonNotFoundException;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.http.HttpStatus;
import org.springframework.validation.FieldError;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestControllerAdvice;
import org.springframework.web.method.annotation.MethodArgumentTypeMismatchException;

import java.util.Collections;
import java.util.HashMap;
import java.util.Map;
import java.util.Objects;

@RestControllerAdvice
public class PersonExceptionHandler {

    @ResponseStatus(HttpStatus.BAD_REQUEST)
    @ExceptionHandler(value = {
            PersonAlreadyExistsException.class, ContactRemoveNotAllowedException.class, MethodArgumentNotValidException.class,
            MethodArgumentTypeMismatchException.class, DataIntegrityViolationException.class})
    public Map<String, String> handleValidationExceptions(Exception ex) {

        Map<String, String> errors = new HashMap<>();
        if (ex instanceof MethodArgumentNotValidException) {
            MethodArgumentNotValidException methodArgNotValidEx = (MethodArgumentNotValidException) ex;
            methodArgNotValidEx.getBindingResult().getAllErrors().forEach((error) -> {
                String fieldName;
                if (error instanceof FieldError)
                    fieldName = ((FieldError) error).getField();
                else
                    fieldName = error.getObjectName();
                String errorMessage = error.getDefaultMessage();
                errors.put(fieldName, errorMessage);
            });
        } else if (ex instanceof MethodArgumentTypeMismatchException) {
            MethodArgumentTypeMismatchException methodArgTypeMismatchEx = (MethodArgumentTypeMismatchException) ex;
            String fieldName = methodArgTypeMismatchEx.getName();
            String errorMessage = String.format("should be of type %s",
                    Objects.requireNonNull(methodArgTypeMismatchEx.getRequiredType()).getName());
            errors.put(fieldName, errorMessage);
        } else if (ex instanceof PersonAlreadyExistsException || ex instanceof ContactRemoveNotAllowedException || ex instanceof DataIntegrityViolationException) {
            errors.put("error", ex.getMessage());
        } else {
            InvalidFormatException invalidFormatEx = (InvalidFormatException) ex.getCause();
            String pathRef = invalidFormatEx.getPathReference();
            String fieldName = pathRef.substring(pathRef.indexOf("\"") + 1, pathRef.lastIndexOf("\""));
            String errorMessage = String.format("'%s' is not a valid value for %s type", invalidFormatEx.getValue(),
                    invalidFormatEx.getTargetType());
            errors.put(fieldName, errorMessage);
        }

        return errors;

    }

    @ResponseStatus(HttpStatus.NOT_FOUND)
    @ExceptionHandler({PersonNotFoundException.class, ContactNotFoundException.class})
    public Map<String, String> handleNotFoundException(Exception ex) {
        return Collections.singletonMap("error", ex.getMessage());
    }
}
