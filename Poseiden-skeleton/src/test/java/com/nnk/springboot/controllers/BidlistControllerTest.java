package com.nnk.springboot.controllers;

import com.nnk.springboot.domain.BidList;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.mock.web.MockHttpServletRequest;
import org.springframework.ui.Model;


import com.nnk.springboot.domain.User;
import com.nnk.springboot.service.BidListService;

import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.security.test.web.servlet.request.SecurityMockMvcRequestPostProcessors;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;
import org.springframework.test.web.servlet.result.MockMvcResultMatchers;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;
import org.springframework.validation.BindingResult;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;
import static org.mockito.Mockito.*;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Optional;
import java.util.Collection;


import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;


@ExtendWith(MockitoExtension.class)
public class BidlistControllerTest {

    private MockMvc mockMvc;

    @Mock
    private BidListService bidListService;
    
    @Mock
    private Authentication auth;

    @InjectMocks
    private BidListController bidListController;

    
    @BeforeEach
    public void setup() {
        mockMvc = MockMvcBuilders.standaloneSetup(bidListController).build();
       
    }
 
    
    @AfterEach
    public void cleanup() {
        SecurityContextHolder.clearContext();
    }

    
    @Test
    public void testHomeWithAdmin() throws Exception {
        // Arrange
        List<GrantedAuthority> authorities = new ArrayList<>();
        authorities.add(new SimpleGrantedAuthority("ROLE_ADMIN"));

        Authentication auth = new UsernamePasswordAuthenticationToken("admin", "password", authorities);
        SecurityContextHolder.getContext().setAuthentication(auth);
        //MockHttpServletRequest request = new MockHttpServletRequest();
        MockMvc mockMvc = MockMvcBuilders.standaloneSetup(bidListController).build();

        // Act
        mockMvc.perform(MockMvcRequestBuilders.get("/bidList/list"))
                .andExpect(MockMvcResultMatchers.status().isOk())
                .andExpect(MockMvcResultMatchers.view().name("bidList/list"))
                .andExpect(MockMvcResultMatchers.model().attributeExists("bidLists"))
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
        //MockHttpServletRequest request = new MockHttpServletRequest();
        MockMvc mockMvc = MockMvcBuilders.standaloneSetup(bidListController).build();

        // Act
        mockMvc.perform(MockMvcRequestBuilders.get("/bidList/list"))
                .andExpect(MockMvcResultMatchers.status().isOk())
                .andExpect(MockMvcResultMatchers.view().name("bidList/list"))
                .andExpect(MockMvcResultMatchers.model().attributeExists("bidLists"))
                .andExpect(MockMvcResultMatchers.model().attributeExists("isAdmin"))
                .andExpect(MockMvcResultMatchers.model().attribute("isAdmin", false));
    }


    @Test
    public void testAddBidForm() throws Exception {
        mockMvc.perform(get("/bidList/add"))
               .andExpect(status().isOk())
               .andExpect(view().name("bidList/add"));
    }


    
    @Test
    public void testValidate() throws Exception {
        BidList bidList = new BidList();
        //suppression de binding result car erreur car est gere automatiquement par spring security
        mockMvc.perform(post("/bidList/validate")
               .flashAttr("bidList", bidList))
               .andExpect(status().is3xxRedirection())
               .andExpect(redirectedUrl("/bidList/list"));

        verify(bidListService, times(1)).save(bidList);
    }


    @Test
    public void testShowUpdateForm() throws Exception {
        BidList bidList = new BidList();
        when(bidListService.findById(1)).thenReturn(Optional.of(bidList));

        mockMvc.perform(get("/bidList/update/1"))
               .andExpect(status().isOk())
               .andExpect(view().name("bidList/update"))
               .andExpect(model().attributeExists("bidList"));
    }

    //bindingResult.hasErrors() qui n'est pas nécessaire. Dans le cadre de tests MockMvc, le BindingResult est généralement géré par le framework Spring et n'a pas besoin d'être mocké explicitement dans la plupart des cas.
    @Test
    public void testUpdateBid() throws Exception {
        BidList bidList = new BidList();

        mockMvc.perform(post("/bidList/update/1")
               .flashAttr("bidList", bidList))
               .andExpect(status().is3xxRedirection())
               .andExpect(redirectedUrl("/bidList/list"));

        verify(bidListService, times(1)).update(bidList);
    }


    @Test
    public void testDeleteBid() throws Exception {
        mockMvc.perform(get("/bidList/delete/1"))
               .andExpect(status().is3xxRedirection())
               .andExpect(redirectedUrl("/bidList/list"));

        verify(bidListService, times(1)).deleteById(1);
    }
}
