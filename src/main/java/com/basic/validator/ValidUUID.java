package com.basic.validator;

import jakarta.validation.Constraint;
import jakarta.validation.Payload;
import jakarta.validation.ReportAsSingleViolation;
import jakarta.validation.constraints.Pattern;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

@Target(ElementType.PARAMETER)
@Constraint(validatedBy={})
@Retention(RetentionPolicy.RUNTIME)
@ReportAsSingleViolation
@Pattern(message = "Invalid UUID", regexp="^[0-9a-f]{8}-[0-9a-f]{4}-[1-5][0-9a-f]{3}-[89ab][0-9a-f]{3}-[0-9a-f]{12}$")
public @interface ValidUUID {
    String message() default "{invalid.uuid}";
    Class<?>[] groups() default {};
    Class<? extends Payload>[] payload() default {};
}