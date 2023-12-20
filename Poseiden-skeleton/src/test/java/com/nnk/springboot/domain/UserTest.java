package com.nnk.springboot.domain;

/*
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.Assertions;
import org.springframework.boot.test.context.SpringBootTest;


import javax.validation.Validation;
import javax.validation.Validator;
import javax.validation.ValidatorFactory;
import javax.validation.ConstraintViolation;
import java.util.Set;
import com.nnk.springboot.domain.Trade;
import com.nnk.springboot.repositories.TradeRepository;
import org.junit.Assert;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;

import java.util.List;
import java.util.Optional;

@SpringBootTest
class UserTest {

    private final Validator validator;

    UserTest() {
        ValidatorFactory factory = Validation.buildDefaultValidatorFactory();
        validator = factory.getValidator();
    }

    @Test
    void whenPasswordIsValid_thenNoConstraintViolations() {
        User user = new User();
        user.setPassword("ValidPassword1!");

        Set<ConstraintViolation<User>> violations = validator.validate(user);
        Assertions.assertTrue(violations.isEmpty());
    }

    @Test
    void whenPasswordIsInvalid_thenConstraintViolations() {
        User user = new User();
        user.setPassword("invalid"); // Mot de passe ne respectant pas l'expression régulière

        Set<ConstraintViolation<User>> violations = validator.validate(user);
        Assertions.assertFalse(violations.isEmpty());
    }
}
*/
