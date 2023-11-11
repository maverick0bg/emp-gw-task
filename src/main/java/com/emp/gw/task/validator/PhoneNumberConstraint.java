package com.emp.gw.task.validator;

import jakarta.validation.Constraint;
import jakarta.validation.Payload;
import java.lang.annotation.Documented;
import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

/** PhoneNumberConstraint validator. This class is used to validate phone numbers. */

@Documented
@Constraint(validatedBy = PhoneValidator.class)
@Target({ElementType.METHOD, ElementType.FIELD, ElementType.PARAMETER})
@Retention(RetentionPolicy.RUNTIME)
public @interface PhoneNumberConstraint {
  String message() default "Invalid country code";

  Class<?>[] groups() default {};

  Class<? extends Payload>[] payload() default {};

}
