package com.nnk.springboot;

import jakarta.validation.Constraint;
import jakarta.validation.Payload;

import java.lang.annotation.*;


@Documented
@Constraint(validatedBy = PasswordConstraintValidator.class)
@Target({ElementType.TYPE, ElementType.FIELD, ElementType.ANNOTATION_TYPE})
@Retention(RetentionPolicy.RUNTIME)
public @interface ValidPassword {

   
    String message() default "le mot de passe doit avoir au moins une lettre majuscule, au moins 8 caractères, au moins un chiffre et un symbole";

    
    Class<?>[] groups() default {};

    
    Class<? extends Payload>[] payload() default {};
}
