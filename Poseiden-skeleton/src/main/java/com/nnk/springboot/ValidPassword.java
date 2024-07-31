package com.nnk.springboot;

import jakarta.validation.Constraint;
import jakarta.validation.Payload;

import java.lang.annotation.*;


@Documented
@Constraint(validatedBy = PasswordConstraintValidator.class)
@Target({ElementType.TYPE, ElementType.FIELD, ElementType.ANNOTATION_TYPE})
@Retention(RetentionPolicy.RUNTIME)
public @interface ValidPassword {

   
	/**
     * Le message par défaut qui sera affiché lorsqu'un mot de passe ne répond pas aux critères de validation.
     * Ce message peut être personnalisé lors de l'utilisation de l'annotation.
     *
     * @return Le message de validation par défaut.
     */
    String message() default "le mot de passe doit avoir au moins une lettre majuscule, au moins 8 caractères, au moins un chiffre et un symbole";

    /**
     * Les groupes de validation auxquels cette contrainte appartient.
     * Permet de classer les contraintes en groupes pour une validation sélective.
     *
     * @return Les groupes de validation par défaut.
     */
    
    Class<?>[] groups() default {};


    /**
     * Utilisé par les clients de Bean Validation pour associer des métadonnées aux contraintes d'une manière type-safe.
     * Les métadonnées de charge utile ne sont généralement pas utilisées dans la validation standard,
     * mais peuvent être utiles pour les systèmes de validation étendus.
     *
     * @return Les classes de charge utile par défaut.
     */
    Class<? extends Payload>[] payload() default {};
}
