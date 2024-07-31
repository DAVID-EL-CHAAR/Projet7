package com.nnk.springboot;

import jakarta.validation.ConstraintValidator;
import jakarta.validation.ConstraintValidatorContext;
import org.passay.*;

import java.util.Arrays;

public class PasswordConstraintValidator implements ConstraintValidator<ValidPassword, String> {

	/**
	 * Initialise le validateur de mot de passe.
	 * Cette méthode est vide car la validation personnalisée du mot de passe n'exige pas d'initialisation spécifique.
	 * Elle est requise par l'interface ConstraintValidator.
	 *
	 * @param arg0 Paramètre de la contrainte de validation de mot de passe.
	 */
    @Override
    public void initialize(ValidPassword arg0) {
    }

    
    /**
     * Vérifie si un mot de passe donné est valide selon les règles définies.
     * Cette méthode utilise le PasswordValidator pour vérifier si le mot de passe respecte certaines règles,
     * comme contenir un nombre minimum de caractères majuscules, de chiffres et de caractères spéciaux,
     * et respecter une longueur minimale et maximale. Elle renvoie vrai si le mot de passe est conforme aux règles.
     *
     * @param password Le mot de passe à valider.
     * @param context Le contexte de la contrainte de validation, utilisé pour indiquer des messages d'erreur.
     * @return true si le mot de passe est valide selon les règles définies, sinon false.
     */
    
    /*@Override
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
    }*/
    
    @Override
    public boolean isValid(String password, ConstraintValidatorContext context) {
        if (password == null || password.isEmpty()) {
            // Mot de passe vide
            context.disableDefaultConstraintViolation();
            context.buildConstraintViolationWithTemplate("Mot de passe vide").addConstraintViolation();
            return false;
        }

        PasswordValidator validator = new PasswordValidator(Arrays.asList(
                new CharacterRule(EnglishCharacterData.UpperCase, 1),
                new LengthRule(8, 30),
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

