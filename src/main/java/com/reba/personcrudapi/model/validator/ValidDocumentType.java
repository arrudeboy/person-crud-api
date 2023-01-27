package com.reba.personcrudapi.model.validator;

import com.reba.personcrudapi.model.DocumentType;

import javax.validation.Constraint;
import javax.validation.Payload;
import javax.validation.constraints.NotNull;
import java.lang.annotation.*;

@Documented
@Constraint(validatedBy = DocumentTypeConstraintValidator.class)
@Retention(RetentionPolicy.RUNTIME)
@Target(ElementType.FIELD)
@NotNull
public @interface ValidDocumentType {

    Class<? extends Enum> enums() default DocumentType.class;
    String message() default "must be any of enums: {DNI, CI, CC}";
    Class<?>[] groups() default {};
    Class<? extends Payload>[] payload() default {};
}
