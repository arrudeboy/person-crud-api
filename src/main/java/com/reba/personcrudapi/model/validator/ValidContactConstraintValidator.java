package com.reba.personcrudapi.model.validator;

import com.reba.personcrudapi.dto.ContactDto;
import com.reba.personcrudapi.model.ContactType;
import com.reba.personcrudapi.model.validator.util.ValidationUtils;

import javax.validation.ConstraintValidator;
import javax.validation.ConstraintValidatorContext;
import javax.validation.Valid;
import java.util.stream.Collectors;
import java.util.stream.Stream;

public class ValidContactConstraintValidator implements ConstraintValidator<ValidContact, ContactDto> {

    @Override
    public void initialize(ValidContact constraintAnnotation) {
        // no initialization needed
    }

    @Override
    public boolean isValid(@Valid ContactDto contactDto, ConstraintValidatorContext constraintValidatorContext) {

        var type = contactDto.getType();
        if (!Stream.of(ContactType.values()).map(ContactType::name).collect(Collectors.toSet()).contains(type))
            return false;

        var contactType = ContactType.valueOf(type);
        var contactValue = contactDto.getValue();
        switch (contactType) {
            case EMAIL:
                return ValidationUtils.isValidEmail(contactValue);
            case PHONE:
                return ValidationUtils.isValidPhone(contactValue);
            case WEB_SITE:
                return ValidationUtils.isValidWebSite(contactValue);
            case PIGEON_MESSENGER:
                return ValidationUtils.isValidPigeonMessenger(contactValue);
            default:
                return false;
        }
    }

}
