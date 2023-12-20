package com.nnk.springboot.validation;
/*
import javax.validation.ConstraintValidator;
import javax.validation.ConstraintValidatorContext;
import java.util.regex.Pattern;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.passay.PasswordValidator;
import org.passay.PasswordData;
import org.passay.RuleResult;
import org.passay.LengthRule;
import org.passay.UppercaseCharacterRule;
import org.passay.DigitCharacterRule;
import org.passay.SpecialCharacterRule;
import org.passay.NumericalSequenceRule;
import org.passay.AlphabeticalSequenceRule;
import org.passay.QwertySequenceRule;
import org.passay.WhitespaceRule;
import javax.validation.ConstraintValidator;
import javax.validation.ConstraintValidatorContext;
import java.util.Arrays;
import java.util.stream.Collectors;

import org.springframework.stereotype.Component;

@Component
public class PasswordConstraintValidator implements ConstraintValidator<ValidPassword, String> {

    @Override
    public void initialize(ValidPassword arg0) {
    }

    @Override
    public boolean isValid(String password, ConstraintValidatorContext context) {
        PasswordValidator validator = new PasswordValidator(Arrays.asList(
           new LengthRule(8, 30), 
           new UppercaseCharacterRule(1), 
           new DigitCharacterRule(1), 
           new SpecialCharacterRule(1), 
           new NumericalSequenceRule(3,false), 
           new AlphabeticalSequenceRule(3,false), 
           new QwertySequenceRule(3,false),
           new WhitespaceRule()));

        RuleResult result = validator.validate(new PasswordData(password));
        if (result.isValid()) {
            return true;
        }
        context.disableDefaultConstraintViolation();
        context.buildConstraintViolationWithTemplate(
          validator.getMessages(result).stream().collect(Collectors.joining(",")))
          .addConstraintViolation();
        return false;
    }
}

/*
@Component
public class PasswordConstraintValidator implements ConstraintValidator<ValidPassword, String> {
	private static final Logger logger = LoggerFactory.getLogger(PasswordConstraintValidator.class);

    private static final String PASSWORD_PATTERN =
    		"^(?=.*[0-9])(?=.*[a-z])(?=.*[A-Z])(?=.*[@#$%^&+=])(?=\\S+$).{8,}$";

    @Override
    public void initialize(ValidPassword constraintAnnotation) {
    }

    @Override
    public boolean isValid(String password, ConstraintValidatorContext context) {
    	logger.info("Validation du mot de passe en cours");
        return validatePassword(password);
    }

    private boolean validatePassword(String password) {
        Pattern pattern = Pattern.compile(PASSWORD_PATTERN);
        return pattern.matcher(password).matches();
    }
}
*/

/*
import org.passay.PasswordValidator;
import org.passay.PasswordData;
import org.passay.RuleResult;
import org.passay.LengthRule;
import org.passay.UppercaseCharacterRule;
import org.passay.DigitCharacterRule;
import org.passay.SpecialCharacterRule;
import org.passay.NumericalSequenceRule;
import org.passay.AlphabeticalSequenceRule;
import org.passay.QwertySequenceRule;
import org.passay.WhitespaceRule;
import javax.validation.ConstraintValidator;
import javax.validation.ConstraintValidatorContext;
import java.util.Arrays;
import java.util.stream.Collectors;

public class PasswordConstraintValidator implements ConstraintValidator<ValidPassword, String> {

    @Override
    public void initialize(ValidPassword arg0) {
    }

    @Override
    public boolean isValid(String password, ConstraintValidatorContext context) {
        PasswordValidator validator = new PasswordValidator(Arrays.asList(
           new LengthRule(8, 30), 
           new UppercaseCharacterRule(1), 
           new DigitCharacterRule(1), 
           new SpecialCharacterRule(1), 
           new NumericalSequenceRule(3, false), 
           new AlphabeticalSequenceRule(3, false), 
           new QwertySequenceRule(3, false),
           new WhitespaceRule()));

        RuleResult result = validator.validate(new PasswordData(password));
        if (result.isValid()) {
            return true;
        }
        context.disableDefaultConstraintViolation();
        context.buildConstraintViolationWithTemplate(
          validator.getMessages(result).stream().collect(Collectors.joining(",")))
          .addConstraintViolation();
        return false;
    }
}
*/