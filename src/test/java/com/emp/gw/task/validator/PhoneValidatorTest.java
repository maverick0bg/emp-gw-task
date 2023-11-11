package com.emp.gw.task.validator;

import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

class PhoneValidatorTest {

  @Test
  void isValid_willSuccess() {
    final PhoneValidator phoneValidator = new PhoneValidator();
    assertTrue(phoneValidator.isValid("+359-000-111-222", null));
    assertTrue(phoneValidator.isValid("00359-000-111-222235", null));
    assertTrue(phoneValidator.isValid("+1-541-754-3010", null));
    assertTrue(phoneValidator.isValid("1-541-754-3010", null));
    assertTrue(phoneValidator.isValid("001-541-754-3010", null));
    assertTrue(phoneValidator.isValid("191 541 754 3010", null));
    assertTrue(phoneValidator.isValid("1915417543010", null));
    assertTrue(phoneValidator.isValid("191(541)7543010", null));
  }

  @Test
  void isValid_willFail() {
    final PhoneValidator phoneValidator = new PhoneValidator();
    assertFalse(phoneValidator.isValid("359-000-(111)-222", null));
    assertFalse(phoneValidator.isValid("359-000-111-222(235)", null));
    assertFalse(phoneValidator.isValid("359-(00000)-111-222235", null));
  }
}
