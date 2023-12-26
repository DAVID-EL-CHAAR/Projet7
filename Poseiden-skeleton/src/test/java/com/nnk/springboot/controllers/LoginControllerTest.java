package com.nnk.springboot.controllers;

import com.nnk.springboot.repositories.UserRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;
import org.springframework.web.servlet.view.InternalResourceViewResolver;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@ExtendWith(MockitoExtension.class)
public class LoginControllerTest {

    private MockMvc mockMvc;

    @Mock
    private UserRepository userRepository;

    @InjectMocks
    private LoginController loginController;

    
    @BeforeEach
    public void setup() {
        MockitoAnnotations.openMocks(this);

        // Configurez le ThymeleafViewResolver
        InternalResourceViewResolver viewResolver = new InternalResourceViewResolver();
        viewResolver.setPrefix("classpath:/templates/");
        viewResolver.setSuffix(".html");
       
        // Construisez le mockMvc avec le viewResolver configuré
        this.mockMvc = MockMvcBuilders.standaloneSetup(loginController)
                                      .setViewResolvers(viewResolver)
                                      .build();
    }

    @Test
    public void testLogin() throws Exception {
        mockMvc.perform(get("/login"))
               .andExpect(status().isOk())
               .andExpect(view().name("login"));
    }

    @Test
    public void testGetAllUserArticles() throws Exception {
        mockMvc.perform(get("/secure/article-details"))
               .andExpect(status().isOk())
               .andExpect(view().name("user/list"))
               .andExpect(model().attributeExists("users"));
    }

    @Test
    public void testError() throws Exception {
        mockMvc.perform(get("/error"))
               .andExpect(status().isOk())
               .andExpect(view().name("403"))
               .andExpect(model().attributeExists("errorMsg", "httpServletRequest"));
    }
}
