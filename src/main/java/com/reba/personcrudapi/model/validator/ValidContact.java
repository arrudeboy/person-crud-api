package com.reba.personcrudapi.model.validator;

import javax.validation.Constraint;
import javax.validation.Payload;
import java.lang.annotation.*;

@Target({ElementType.TYPE, ElementType.ANNOTATION_TYPE})
@Retention(RetentionPolicy.RUNTIME)
@Constraint(validatedBy = ValidContactConstraintValidator.class)
@Documented
public @interface ValidContact {

    String message() default "invalid contact type or invalid contact value for given contact type";
    Class<?>[] groups() default {};
    Class<? extends Payload>[] payload() default {};

}
