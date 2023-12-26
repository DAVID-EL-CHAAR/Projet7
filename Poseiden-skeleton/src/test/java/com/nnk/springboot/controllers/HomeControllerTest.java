package com.nnk.springboot.controllers;

import com.nnk.springboot.domain.User;
import com.nnk.springboot.service.UserService;

import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletRequestWrapper;
import jakarta.servlet.http.HttpSession;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;
import org.springframework.web.servlet.view.InternalResourceViewResolver;
import org.thymeleaf.spring6.view.ThymeleafViewResolver;
import org.springframework.security.test.context.support.WithMockUser;

import static org.hamcrest.CoreMatchers.instanceOf;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;


import java.security.Principal;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.mock.web.MockHttpServletRequest;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.context.SecurityContext;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.context.SecurityContextImpl;
import org.springframework.test.web.servlet.result.MockMvcResultMatchers;

//@SpringBootTest
//@AutoConfigureMockMvc
@ExtendWith(MockitoExtension.class)
public class HomeControllerTest {

    private MockMvc mockMvc;

    @Mock
    private UserService userService;

    @InjectMocks
    private HomeController homeController;

    @BeforeEach
    public void setup() {
        MockitoAnnotations.openMocks(this);

        // Configurez le ThymeleafViewResolver
        InternalResourceViewResolver viewResolver = new InternalResourceViewResolver();
        viewResolver.setPrefix("classpath:/templates/");
        viewResolver.setSuffix(".html");
       
        // Construisez le mockMvc avec le viewResolver configuré
        this.mockMvc = MockMvcBuilders.standaloneSetup(homeController)
                                      .setViewResolvers(viewResolver)
                                      .build();
    }
    
  
 
    
        @Test
        public void shouldReturnHomeWView() throws Exception {
            // Mock Authentication
            Authentication auth = new UsernamePasswordAuthenticationToken("user", "password", Arrays.asList(new SimpleGrantedAuthority("ROLE_USER")));
            SecurityContextHolder.getContext().setAuthentication(auth);

            
            
            // Mock Principal
            Principal principal = mock(Principal.class);
            when(principal.getName()).thenReturn("user");
            User mockUser = new User();
            mockUser.setUsername("user");
            when(userService.findByUsername("user")).thenReturn(mockUser);

            // Mock HttpServletRequest
            HttpServletRequest request = mock(HttpServletRequest.class);

            // Call the homeW method
            mockMvc.perform(get("/homeW").principal(principal)) //utilisation de .andexpect sans le builder pour view resolver et utilisation de principal
                    .andExpect(status().isOk())
                    .andExpect(view().name("homeW"))
                    .andExpect(model().attributeExists("isAdmin"))
                    .andExpect(model().attribute("isAdmin", false))
                    .andExpect(model().attributeExists("user"))
                    .andExpect(model().attribute("user", instanceOf(User.class)));
        }
   


        @Test
        public void shouldReturnHomeWViewAdmin() throws Exception {
            // Mock Authentication
            Authentication auth = new UsernamePasswordAuthenticationToken("user", "password", Arrays.asList(new SimpleGrantedAuthority("ROLE_ADMIN")));
            SecurityContextHolder.getContext().setAuthentication(auth);

            
            
            // Mock Principal
            Principal principal = mock(Principal.class);
            when(principal.getName()).thenReturn("user");
            User mockUser = new User();
            mockUser.setUsername("user");
            when(userService.findByUsername("user")).thenReturn(mockUser);

            // Mock HttpServletRequest
            HttpServletRequest request = mock(HttpServletRequest.class);

            // Call the homeW method
            mockMvc.perform(get("/homeW").principal(principal))
                    .andExpect(status().isOk())
                    .andExpect(view().name("homeW"))
                    .andExpect(model().attributeExists("isAdmin"))
                    .andExpect(model().attribute("isAdmin", true))
                    .andExpect(model().attributeExists("user"))
                    .andExpect(model().attribute("user", instanceOf(User.class)));
        }
   

    @Test
    public void testHome() throws Exception {
        mockMvc.perform(get("/home"))
               .andExpect(status().isOk())
               .andExpect(view().name("home"));
    }

    @Test
    @WithMockUser(roles = "ADMIN")
    public void testAdminHome() throws Exception {
        mockMvc.perform(get("/admin/home"))
               .andExpect(status().is3xxRedirection())
               .andExpect(redirectedUrl("/bidList/list"));
    }
}
