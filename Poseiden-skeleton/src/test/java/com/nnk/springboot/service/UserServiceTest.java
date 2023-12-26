package com.nnk.springboot.service;

import static org.mockito.Mockito.*;
import static org.junit.jupiter.api.Assertions.*;

import java.util.Arrays;
import java.util.List;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.security.crypto.password.PasswordEncoder;

import com.nnk.springboot.domain.User;
import com.nnk.springboot.repositories.UserRepository;

public class UserServiceTest {

    @Mock
    private UserRepository userRepository;

    @Mock
    private PasswordEncoder passwordEncoder;

    @InjectMocks
    private UserService userService;

    @BeforeEach
    public void setup() {
        MockitoAnnotations.initMocks(this);
    }

    @Test
    public void testRegisterNewUserAccount() {
        User userDto = new User();
        userDto.setUsername("testuser");
        userDto.setPassword("password");
        userDto.setFullname("Test User");

        when(userRepository.findByUsername("testuser")).thenReturn(null);
        when(passwordEncoder.encode("password")).thenReturn("encodedPassword");

        User userToReturn = new User();
        userToReturn.setUsername(userDto.getUsername());
        userToReturn.setPassword("encodedPassword"); // Encoded password
        userToReturn.setFullname(userDto.getFullname());
        userToReturn.setRole("USER");

        when(userRepository.save(any(User.class))).thenReturn(userToReturn);

        User savedUser = userService.registerNewUserAccount(userDto);

        assertNotNull(savedUser);
        assertEquals("testuser", savedUser.getUsername());
        assertEquals("encodedPassword", savedUser.getPassword());
        assertEquals("USER", savedUser.getRole());
    }

    @Test
    public void testRegisterNewUserAccount_UsernameExists() {
        User userDto = new User();
        userDto.setUsername("existinguser");

        when(userRepository.findByUsername("existinguser")).thenReturn(new User());

        assertThrows(RuntimeException.class, () -> {
            userService.registerNewUserAccount(userDto);
        });
    }

    @Test
    public void testGetUsers() {
        when(userRepository.findAll()).thenReturn(Arrays.asList(new User(), new User()));
        List<User> users = userService.getUsers();
        assertEquals(2, users.size());
    }

    @Test
    public void testFindByUsername() {
        String username = "testuser";
        when(userRepository.findByUsername(username)).thenReturn(new User());

        assertNotNull(userService.findByUsername(username));
    }

    @Test
    public void testSaveUser() {
        User user = new User();
        userService.saveUser(user);
        verify(userRepository, times(1)).save(user);
    }
}
