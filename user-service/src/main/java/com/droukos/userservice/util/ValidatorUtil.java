package com.droukos.userservice.util;

import static com.droukos.userservice.environment.security.HttpExceptionFactory.badRequest;
import static com.droukos.userservice.environment.security.HttpExceptionFactory.badRequest;

import java.util.List;
import java.util.stream.Collectors;
import org.springframework.context.support.DefaultMessageSourceResolvable;
import org.springframework.validation.BeanPropertyBindingResult;
import org.springframework.validation.Errors;
import org.springframework.validation.Validator;

public class ValidatorUtil {

  private ValidatorUtil() {}

  public static void validate(Object data, Validator validator) {
    Errors errors = new BeanPropertyBindingResult(data, "data");
    validator.validate(data, errors);

    if (errors.hasErrors())
      throw badRequest(
          errors.getFieldErrors().stream()
              .map(DefaultMessageSourceResolvable::getCode)
              .collect(Collectors.toList())
              .toString());
  }

  public static List<String> testValidate(Object data, Validator validator) {
    Errors errors = new BeanPropertyBindingResult(data, "data");
    validator.validate(data, errors);

    return getErrorsList(errors);
  }

  public static List<String> getErrorsList(Errors errors) {
    return errors.getFieldErrors().stream()
        .map(DefaultMessageSourceResolvable::getCode)
        .collect(Collectors.toList());
  }
}
