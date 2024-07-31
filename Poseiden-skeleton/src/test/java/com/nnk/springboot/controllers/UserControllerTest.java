package com.nnk.springboot.controllers;

import com.nnk.springboot.domain.User;
import com.nnk.springboot.domain.UserDto;
import com.nnk.springboot.service.UserService;
import com.nnk.springboot.repositories.UserRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.ArgumentCaptor;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;
import org.springframework.http.MediaType;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.validation.BindingResult;
import org.springframework.web.servlet.view.InternalResourceViewResolver;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.Mockito.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

import java.util.Arrays;
import java.util.List;
import java.util.Optional;

@ExtendWith(MockitoExtension.class)
public class UserControllerTest {

    private MockMvc mockMvc;

    @Mock
    private UserRepository userRepository;

    @Mock
    private UserService userService;

    @InjectMocks
    private UserController userController;

    
    @BeforeEach
    public void setup() {
        MockitoAnnotations.openMocks(this);

        // Configurez le ThymeleafViewResolver
        InternalResourceViewResolver viewResolver = new InternalResourceViewResolver();
        viewResolver.setPrefix("classpath:/templates/");
        viewResolver.setSuffix(".html");
       
        // Construisez le mockMvc avec le viewResolver configuré
        this.mockMvc = MockMvcBuilders.standaloneSetup(userController)
                                      .setViewResolvers(viewResolver)
                                      .build();
    }

    @Test
    public void testShowRegistrationForm() throws Exception {
        mockMvc.perform(get("/register"))
               .andExpect(status().isOk())
               .andExpect(view().name("register"))
               .andExpect(model().attributeExists("user"));
    }

    @Test
    public void testRegisterUserAccountWithValidData() throws Exception {
        // Create a valid user object
        User user = new User();
        user.setUsername("testuser");
        user.setFullname("John Doe");
        user.setPassword("Password12@");

        // Send a POST request to the /register endpoint
        mockMvc.perform(post("/register")
                .contentType(MediaType.APPLICATION_FORM_URLENCODED)
                .param("username", user.getUsername())
                .param("fullname", user.getFullname())
                .param("password", user.getPassword()))

        // Verify that the response status is 302 (redirect)
        .andExpect(status().is3xxRedirection())

        // Verify that the redirect URL is correct
        .andExpect(redirectedUrl("/login"));
    }
    
    @Test
    @WithMockUser(roles = "ADMIN")
    public void testAddUser() throws Exception {
        mockMvc.perform(get("/admin/user/add"))
               .andExpect(status().isOk())
               .andExpect(view().name("user/add"))
               .andExpect(model().attributeExists("user"));
    }
    
    @Test
    public void testValidateWithValidData() throws Exception {
        // Create a valid user object
        UserDto userDto = new UserDto();
        userDto.setUsername("testuser");
        userDto.setFullname("John Doe");
        userDto.setPassword("Password12@");
        userDto.setRole("user");

        // Send a POST request to the /admin/user/validate endpoint
        mockMvc.perform(post("/admin/user/validate")
                .contentType(MediaType.APPLICATION_FORM_URLENCODED)
                .param("username", userDto.getUsername())
                .param("fullname", userDto.getFullname())
                .param("password", userDto.getPassword())
                .param("role", userDto.getRole()))

        // Verify that the response status is 302 (redirect)
        .andExpect(status().is3xxRedirection())

        // Verify that the redirect URL is correct
        .andExpect(redirectedUrl("/admin/user/list"));
    }
    /*
    @Test
    public void testValidateWithExistingUsername() throws Exception {
        // Create a valid user object with an existing username
        UserDto userDto = new UserDto();
        userDto.setUsername("existingUser");
        userDto.setFullname("John Doe");
        userDto.setPassword("password");

        // Simulate a user already existing with the same username
        User existingUser = new User();
        existingUser.setUsername("existingUser");
        existingUser.setFullname("Existing User");
        existingUser.setPassword("existingPassword");
        userService.saveUser(existingUser);

        // Send a POST request to the /admin/user/validate endpoint
        mockMvc.perform(post("/admin/user/validate")
                .contentType(MediaType.APPLICATION_FORM_URLENCODED)
                .param("username", userDto.getUsername())
                .param("fullname", userDto.getFullname())
                .param("password", userDto.getPassword()))

        // Verify that the response status is 200 (OK)
        .andExpect(status().isOk())

        // Verify that the response content contains the error message for the existing username
        .andExpect(content().string("This username is already used"));
    }*/
    
    @Test
    @WithMockUser(roles = "ADMIN")
    public void testShowUpdateForm() throws Exception {
        User mockUser = new User();
        mockUser.setId(1);
        mockUser.setPassword("password");
        when(userRepository.findById(1)).thenReturn(Optional.of(mockUser));

        mockMvc.perform(get("/admin/user/update/1"))
               .andExpect(status().isOk())
               .andExpect(view().name("user/update"))
               .andExpect(model().attributeExists("user"))
               .andExpect(model().attribute("user", mockUser));
    }
    
    @Test
    @WithMockUser(roles = "ADMIN")
    public void testDeleteUser() throws Exception {
        User mockUser = new User();
        mockUser.setId(1);
        when(userRepository.findById(1)).thenReturn(Optional.of(mockUser));

        mockMvc.perform(get("/admin/user/delete/1"))
               .andExpect(status().is3xxRedirection())
               .andExpect(redirectedUrl("/admin/user/list"));

        verify(userRepository, times(1)).delete(mockUser);
        verify(userRepository, times(1)).findAll(); // Pour vérifier si on récupère bien la liste après suppression
    }
    
    @Test
    public void testUpdateUserWithValidData() throws Exception {
        // Create a valid user object
        UserDto userDto = new UserDto();
        userDto.setUsername("updatedUser");
        userDto.setFullname("Updated User");
        userDto.setPassword("Password12@");
        userDto.setRole("user");
        // Simulate an existing user with the given ID
        User existingUser = new User();
        existingUser.setId(1);
        existingUser.setUsername("existingUser");
        existingUser.setFullname("Existing User");
        existingUser.setPassword("existingPassword12@");
        existingUser.setRole("user");
        userDto.setRole(existingUser.getRole());

        // Send a POST request to the /admin/user/update endpoint
        mockMvc.perform(MockMvcRequestBuilders.post("/admin/user/update/{id}", 1)// cherche pourquoi ici on utiliser le builder
                .contentType(MediaType.APPLICATION_FORM_URLENCODED) //savoir aussi pourquoi sa 
                .param("username", userDto.getUsername())
                .param("fullname", userDto.getFullname())
                .param("password", userDto.getPassword())
                .param("role", userDto.getRole()))
                .andExpect(status().is3xxRedirection())
                .andExpect(redirectedUrl("/admin/user/list"));

        // Verify that the user has been updated in the database
        ArgumentCaptor<User> userArgumentCaptor = ArgumentCaptor.forClass(User.class);
        verify(userService, times(1)).saveUser(userArgumentCaptor.capture());
        User updatedUser = userArgumentCaptor.getValue();
        assertThat(updatedUser.getUsername()).isEqualTo("updatedUser");
        assertThat(updatedUser.getFullname()).isEqualTo("Updated User");
        assertThat(updatedUser.getPassword()).isNotEqualTo("Password12@"); // Ensure password is hashed and not plain text
    }
    /*
    @Test
    public void testUpdateUserWithValidData() throws Exception {
        // Create a valid user object
        UserDto userDto = new UserDto();
        userDto.setUsername("updatedUser");
        userDto.setFullname("Updated User");
        userDto.setPassword("Password12@");
        userDto.setRole("user");

        // Simulate an existing user with the given ID
        User existingUser = new User();
        existingUser.setId(1);
        existingUser.setUsername("existingUser");
        existingUser.setFullname("Existing User");
        existingUser.setPassword("existingPassword12@");
        userDto.setRole(existingUser.getRole());
        userService.saveUser(existingUser);

        // Send a POST request to the /admin/user/update endpoint
        mockMvc.perform(post("/admin/user/update/{id}", 1)
                .contentType(MediaType.APPLICATION_FORM_URLENCODED)
                .param("username", userDto.getUsername())
                .param("fullname", userDto.getFullname())
                .param("password", userDto.getPassword())
                .param("role",     userDto.getRole()))

        // Verify that the response status is 302 (redirect)
        .andExpect(status().is3xxRedirection())

        // Verify that the redirect URL is correct
        .andExpect(redirectedUrl("/admin/user/list"));

        // Verify that the user has been updated in the database
        List<User> updatedUsers = userService.getUsers();
        User updatedUser = updatedUsers.stream().filter(user -> user.getId() == 1).findFirst().get();
        assertThat(updatedUser.getUsername()).isEqualTo("updatedUser");
        assertThat(updatedUser.getFullname()).isEqualTo("Updated User");
        assertThat(updatedUser.getPassword()).isNotEqualTo("Password12@"); // Ensure password is hashed and not plain text
    } */

    /*
    @Test
    public void testUpdateUserWithInvalidData() throws Exception {
        // Create an invalid user object
        UserDto userDto = new UserDto();
        userDto.setUsername("");
        userDto.setFullname("Invalid Fullname");
        userDto.setPassword("");

        // Send a POST request to the /admin/user/update endpoint
        mockMvc.perform(post("/admin/user/update/{id}", 1)
                .contentType(MediaType.APPLICATION_FORM_URLENCODED)
                .param("username", userDto.getUsername())
                .param("fullname", userDto.getFullname())
                .param("password", userDto.getPassword()))

        // Verify that the response status is 200 (OK)
        .andExpect(status().isOk())

        // Verify that the response content contains the validation error messages
        .andExpect(content().string("Please enter a username\nPlease enter a valid fullname"));
    }*/
    
    
}
