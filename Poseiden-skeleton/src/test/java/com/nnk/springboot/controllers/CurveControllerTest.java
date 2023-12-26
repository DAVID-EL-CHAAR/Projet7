package com.nnk.springboot.controllers;

import com.nnk.springboot.domain.CurvePoint;
import com.nnk.springboot.service.CurvePointService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.validation.BindingResult;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.mock.web.MockHttpServletRequest;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;
import org.springframework.test.web.servlet.result.MockMvcResultMatchers;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import static org.mockito.Mockito.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@ExtendWith(MockitoExtension.class)
public class CurveControllerTest {

    private MockMvc mockMvc;

    @Mock
    private CurvePointService curvePointService;

    @Mock
    private BindingResult bindingResult;

    @InjectMocks
    private CurveController curveController;

    @BeforeEach
    public void setup() {
        mockMvc = MockMvcBuilders.standaloneSetup(curveController).build();
    }
    
    @Test
    public void testHomeWithAdmin() throws Exception {
        // Arrange
        List<GrantedAuthority> authorities = new ArrayList<>();
        authorities.add(new SimpleGrantedAuthority("ROLE_ADMIN"));

        Authentication auth = new UsernamePasswordAuthenticationToken("admin", "password", authorities);
        SecurityContextHolder.getContext().setAuthentication(auth);
        MockHttpServletRequest request = new MockHttpServletRequest();
        MockMvc mockMvc = MockMvcBuilders.standaloneSetup(curveController).build();

        // Act
        mockMvc.perform(MockMvcRequestBuilders.get("/curvePoint/list"))
                .andExpect(MockMvcResultMatchers.status().isOk())
                .andExpect(MockMvcResultMatchers.view().name("curvePoint/list"))
                .andExpect(MockMvcResultMatchers.model().attributeExists("curvePoints"))
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
        MockMvc mockMvc = MockMvcBuilders.standaloneSetup(curveController).build();

        // Act
        mockMvc.perform(MockMvcRequestBuilders.get("/curvePoint/list"))
            .andExpect(MockMvcResultMatchers.status().isOk())
            .andExpect(MockMvcResultMatchers.view().name("curvePoint/list"))
            .andExpect(MockMvcResultMatchers.model().attributeExists("curvePoints"))
            .andExpect(MockMvcResultMatchers.model().attribute("isAdmin", false));
    }

    @Test
    @WithMockUser
    public void testAddBidForm() throws Exception {
        mockMvc.perform(get("/curvePoint/add"))
               .andExpect(status().isOk())
               .andExpect(view().name("curvePoint/add"));
    }

    @Test
    @WithMockUser
    public void testValidate_noErrors() throws Exception {
        CurvePoint curvePoint = new CurvePoint();
        

        mockMvc.perform(post("/curvePoint/validate").flashAttr("curvePoint", curvePoint))
               .andExpect(status().is3xxRedirection())
               .andExpect(redirectedUrl("/curvePoint/list"));

        verify(curvePointService, times(1)).saveCurvePoint(curvePoint);
    }

    @Test
    @WithMockUser
    public void testShowUpdateForm() throws Exception {
        CurvePoint curvePoint = new CurvePoint();
        when(curvePointService.findById(1)).thenReturn(curvePoint);

        mockMvc.perform(get("/curvePoint/update/1"))
               .andExpect(status().isOk())
               .andExpect(view().name("curvePoint/update"))
               .andExpect(model().attributeExists("curvePoint"));
    }

    @Test
    @WithMockUser
    public void testUpdateBid_noErrors() throws Exception {
        CurvePoint curvePoint = new CurvePoint();
        

        mockMvc.perform(post("/curvePoint/update/1").flashAttr("curvePoint", curvePoint))
               .andExpect(status().is3xxRedirection())
               .andExpect(redirectedUrl("/curvePoint/list"));

        verify(curvePointService, times(1)).updateCurvePoint(eq(1), eq(curvePoint));
    }

    @Test
    @WithMockUser
    public void testDeleteBid() throws Exception {
        mockMvc.perform(get("/curvePoint/delete/1"))
               .andExpect(status().is3xxRedirection())
               .andExpect(redirectedUrl("/curvePoint/list"));

        verify(curvePointService, times(1)).deleteById(1);
    }
}

