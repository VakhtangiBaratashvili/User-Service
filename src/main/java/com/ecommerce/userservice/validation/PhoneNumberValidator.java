package com.ecommerce.userservice.validation;

import jakarta.validation.ConstraintValidator;
import jakarta.validation.ConstraintValidatorContext;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class PhoneNumberValidator implements ConstraintValidator<PhoneNumber, String> {
    @Override
    public boolean isValid(String phoneNumber, ConstraintValidatorContext constraintValidatorContext) {
        Pattern pattern = Pattern.compile("^\\+995(5\\d{8}|32\\d{7}|79\\d{7})$");
        Matcher matcher = pattern.matcher(phoneNumber);
        return matcher.matches();
    }
}
