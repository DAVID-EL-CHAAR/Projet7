package com.nnk.springboot;

import jakarta.validation.ConstraintValidator;
import jakarta.validation.ConstraintValidatorContext;
import org.passay.*;

import java.util.Arrays;

public class PasswordConstraintValidator implements ConstraintValidator<ValidPassword, String> {

    
    @Override
    public void initialize(ValidPassword arg0) {
    }

    
    @Override
    public boolean isValid(String password, ConstraintValidatorContext context) {
        PasswordValidator validator = new PasswordValidator(Arrays.asList(

                new CharacterRule(EnglishCharacterData.UpperCase,1),
                new LengthRule(8,30),
                new CharacterRule(EnglishCharacterData.Digit, 1),
                new CharacterRule(EnglishCharacterData.Special, 1)));

        // Valide le mot de passe en utilisant le validateur
        final RuleResult result = validator.validate(new PasswordData(password));

        // Retourne vrai si le mot de passe est valide, sinon faux
        if (result.isValid()) {
            return true;
        }
        return false;
    }
}
