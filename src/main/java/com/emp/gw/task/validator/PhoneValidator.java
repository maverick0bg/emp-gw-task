package com.emp.gw.task.validator;

import jakarta.validation.ConstraintValidator;
import jakarta.validation.ConstraintValidatorContext;
import java.util.regex.Pattern;

/** PhoneValidator validator. This class is used to validate phone numbers. */
public class PhoneValidator implements ConstraintValidator<PhoneNumberConstraint, String> {

  public static final String REGEX =
      "^\\+?\\d{1,4}?[-.\\s]?\\(?\\d{1,3}?\\)?[-.\\s]?\\d{1,4}[-.\\s]?\\d{1,4}[-.\\s]?\\d{1,9}$";
  public static final Pattern PATTERN = Pattern.compile(REGEX);

  @Override
  public boolean isValid(String value, ConstraintValidatorContext context) {
    return PATTERN.matcher(value).matches();
  }
}
