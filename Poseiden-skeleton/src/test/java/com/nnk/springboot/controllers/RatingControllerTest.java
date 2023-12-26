package com.nnk.springboot.controllers;


import com.nnk.springboot.domain.Rating;
import com.nnk.springboot.service.RatingService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;
import org.springframework.security.test.context.support.WithMockUser;

import static org.mockito.Mockito.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

import java.util.ArrayList;
import java.util.List;

import org.springframework.mock.web.MockHttpServletRequest;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.test.web.servlet.result.MockMvcResultMatchers;


@ExtendWith(MockitoExtension.class)
public class RatingControllerTest {

    private MockMvc mockMvc;

    @Mock
    private RatingService ratingService;

    @InjectMocks
    private RatingController ratingController;

    @BeforeEach
    public void setup() {
        mockMvc = MockMvcBuilders.standaloneSetup(ratingController).build();
    }
    
    @Test
    public void testHomeWithAdmin() throws Exception {
        // Arrange
        List<GrantedAuthority> authorities = new ArrayList<>();
        authorities.add(new SimpleGrantedAuthority("ROLE_ADMIN"));

        Authentication auth = new UsernamePasswordAuthenticationToken("admin", "password", authorities);
        SecurityContextHolder.getContext().setAuthentication(auth);
        MockHttpServletRequest request = new MockHttpServletRequest();
        MockMvc mockMvc = MockMvcBuilders.standaloneSetup(ratingController).build();

        // Act
        mockMvc.perform(MockMvcRequestBuilders.get("/rating/list"))
            .andExpect(MockMvcResultMatchers.status().isOk())
            .andExpect(MockMvcResultMatchers.view().name("rating/list"))
            .andExpect(MockMvcResultMatchers.model().attributeExists("ratings"))
            .andExpect(MockMvcResultMatchers.model().attributeExists("isAdmin"))
            .andExpect(MockMvcResultMatchers.model().attribute("isAdmin", true));
    }

    @Test
    public void testHomeWithoutAdmin() throws Exception {
        // Arrange
        List<GrantedAuthority> authorities = new ArrayList<>();
        authorities.add(new SimpleGrantedAuthority("ROLE_USER"));

        Authentication auth = new UsernamePasswordAuthenticationToken("user", "password", authorities);
        SecurityContextHolder.getContext().setAuthentication(auth);
        MockHttpServletRequest request = new MockHttpServletRequest();
        MockMvc mockMvc = MockMvcBuilders.standaloneSetup(ratingController).build();

        // Act
        mockMvc.perform(MockMvcRequestBuilders.get("/rating/list"))
            .andExpect(MockMvcResultMatchers.status().isOk())
            .andExpect(MockMvcResultMatchers.view().name("rating/list"))
            .andExpect(MockMvcResultMatchers.model().attributeExists("ratings"))
            .andExpect(MockMvcResultMatchers.model().attribute("isAdmin", false));
    }


    
    @Test
    @WithMockUser
    public void testAddRatingForm() throws Exception {
        mockMvc.perform(get("/rating/add"))
               .andExpect(status().isOk())
               .andExpect(view().name("rating/add"))
               .andExpect(model().attributeExists("rating"));
    }

    @Test
    @WithMockUser
    public void testValidate_noErrors() throws Exception {
        Rating rating = new Rating();
        mockMvc.perform(post("/rating/validate").flashAttr("rating", rating))
               .andExpect(status().is3xxRedirection())
               .andExpect(redirectedUrl("/rating/list"));

        verify(ratingService, times(1)).save(rating);
    }

    @Test
    @WithMockUser
    public void testShowUpdateForm() throws Exception {
        Rating rating = new Rating();
        when(ratingService.findById(1)).thenReturn(rating);

        mockMvc.perform(get("/rating/update/1"))
               .andExpect(status().isOk())
               .andExpect(view().name("rating/update"))
               .andExpect(model().attributeExists("rating"));
    }

    @Test
    @WithMockUser
    public void testUpdateRating_noErrors() throws Exception {
        Rating rating = new Rating();
        mockMvc.perform(post("/rating/update/1").flashAttr("rating", rating))
               .andExpect(status().is3xxRedirection())
               .andExpect(redirectedUrl("/rating/list"));

        verify(ratingService, times(1)).updateRating(eq(1), eq(rating));
    }

    @Test
    @WithMockUser
    public void testDeleteRating() throws Exception {
        mockMvc.perform(get("/rating/delete/1"))
               .andExpect(status().is3xxRedirection())
               .andExpect(redirectedUrl("/rating/list"));

        verify(ratingService, times(1)).deleteById(1);
    }
}


