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
        
        User user = new User();
        user.setUsername("testuser");
        user.setFullname("John Doe");
        user.setPassword("Password12@");

        
        mockMvc.perform(post("/register")
                .contentType(MediaType.APPLICATION_FORM_URLENCODED)
                .param("username", user.getUsername())
                .param("fullname", user.getFullname())
                .param("password", user.getPassword()))

        
        .andExpect(status().is3xxRedirection())

        
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
        
        UserDto userDto = new UserDto();
        userDto.setUsername("testuser");
        userDto.setFullname("John Doe");
        userDto.setPassword("Password12@");
        userDto.setRole("user");


        mockMvc.perform(post("/admin/user/validate")
                .contentType(MediaType.APPLICATION_FORM_URLENCODED)
                .param("username", userDto.getUsername())
                .param("fullname", userDto.getFullname())
                .param("password", userDto.getPassword())
                .param("role", userDto.getRole()))

    
        .andExpect(status().is3xxRedirection())

   
        .andExpect(redirectedUrl("/admin/user/list"));
    }

    
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
        
        UserDto userDto = new UserDto();
        userDto.setUsername("updatedUser");
        userDto.setFullname("Updated User");
        userDto.setPassword("Password12@");
        userDto.setRole("user");
        
        User existingUser = new User();
        existingUser.setId(1);
        existingUser.setUsername("existingUser");
        existingUser.setFullname("Existing User");
        existingUser.setPassword("existingPassword12@");
        existingUser.setRole("user");
        userDto.setRole(existingUser.getRole());

        
        mockMvc.perform(MockMvcRequestBuilders.post("/admin/user/update/{id}", 1)
                .contentType(MediaType.APPLICATION_FORM_URLENCODED) 
                .param("username", userDto.getUsername())
                .param("fullname", userDto.getFullname())
                .param("password", userDto.getPassword())
                .param("role", userDto.getRole()))
                .andExpect(status().is3xxRedirection())
                .andExpect(redirectedUrl("/admin/user/list"));

        
        ArgumentCaptor<User> userArgumentCaptor = ArgumentCaptor.forClass(User.class);
        verify(userService, times(1)).saveUser(userArgumentCaptor.capture());
        User updatedUser = userArgumentCaptor.getValue();
        assertThat(updatedUser.getUsername()).isEqualTo("updatedUser");
        assertThat(updatedUser.getFullname()).isEqualTo("Updated User");
        assertThat(updatedUser.getPassword()).isNotEqualTo("Password12@"); 
    }


   
    
}
