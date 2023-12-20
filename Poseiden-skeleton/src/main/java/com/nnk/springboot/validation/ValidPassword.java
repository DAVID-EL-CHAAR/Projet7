package com.nnk.springboot.validation;
/*
import javax.validation.Constraint;
import javax.validation.Payload;
import java.lang.annotation.*;
import java.lang.annotation.Target;
import java.lang.annotation.Retention;
import static java.lang.annotation.ElementType.*;  // Pour TYPE, FIELD, ANNOTATION_TYPE
import static java.lang.annotation.RetentionPolicy.*;  // Pour RUNTIME



@Documented
@Constraint(validatedBy = PasswordConstraintValidator.class)
@Target({ TYPE, FIELD, ANNOTATION_TYPE })
@Retention(RUNTIME)
public @interface ValidPassword {

    String message() default "Invalid Password";

    Class<?>[] groups() default {};

    Class<? extends Payload>[] payload() default {};

}
/*@Documented
@Constraint(validatedBy = PasswordConstraintValidator.class)
@Target({ ElementType.METHOD, ElementType.FIELD })
@Retention(RetentionPolicy.RUNTIME)
public @interface ValidPassword {
    String message() default "Le mot de passe doit contenir au moins une lettre majuscule, 8 caractères, un chiffre et un symbole";
    Class<?>[] groups() default {};
    Class<? extends Payload>[] payload() default {};
}
*/
/*
@Documented
@Constraint(validatedBy = PasswordConstraintValidator.class)
@Target({ ElementType.TYPE, ElementType.FIELD, ElementType.ANNOTATION_TYPE })
@Retention(RetentionPolicy.RUNTIME)
public @interface ValidPassword {
    String message() default "Invalid Password";
    Class<?>[] groups() default {};
    Class<? extends Payload>[] payload() default {};
}*/
/*
<dependency>
<groupId>org.passay</groupId>
<artifactId>passay</artifactId>
<version>1.6.1</version>
</dependency>*/

