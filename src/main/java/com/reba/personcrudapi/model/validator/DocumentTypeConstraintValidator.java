package com.reba.personcrudapi.model.validator;

import javax.validation.ConstraintValidator;
import javax.validation.ConstraintValidatorContext;
import java.util.Set;
import java.util.stream.Collectors;
import java.util.stream.Stream;

public class DocumentTypeConstraintValidator implements ConstraintValidator<ValidDocumentType, String> {

    Set<String> values;

    @Override
    public void initialize(ValidDocumentType constraintAnnotation) {
        values = Stream.of(constraintAnnotation.enums().getEnumConstants()).map(Enum::name).collect(Collectors.toSet());
    }

    @Override
    public boolean isValid(String s, ConstraintValidatorContext constraintValidatorContext) {
        return values.contains(s);
    }
}
