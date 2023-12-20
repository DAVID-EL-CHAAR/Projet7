package com.nnk.springboot;

import com.nnk.springboot.PasswordConstraintValidator;
import org.passay.PasswordData;
import org.passay.PasswordValidator;
import org.passay.RuleResult;
import org.passay.EnglishCharacterData;
import org.passay.CharacterRule;
import org.passay.LengthRule;

import jakarta.validation.ConstraintValidatorContext;
import com.nnk.springboot.PasswordConstraintValidator;
import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.*;

public class PasswordValidatorTest {

    private PasswordConstraintValidator validator = new PasswordConstraintValidator();

    @Test
    public void testValidPassword() {
        assertTrue(validator.isValid("ValidPassword1!", null));
    }

    @Test
    public void testShortPassword() {
        assertFalse(validator.isValid("Shor1!", null));
    }

    @Test
    public void testPasswordWithoutDigit() {
        assertFalse(validator.isValid("Password!", null));
    }

    @Test
    public void testPasswordWithoutSpecialChar() {
        assertFalse(validator.isValid("Password1", null));
    }

    @Test
    public void testPasswordWithoutUppercase() {
        assertFalse(validator.isValid("password1!", null));
    }
}


