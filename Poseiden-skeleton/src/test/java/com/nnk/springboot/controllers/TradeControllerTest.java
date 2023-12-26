package com.nnk.springboot.controllers;

import com.nnk.springboot.domain.Trade;
import com.nnk.springboot.service.TradeService;
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
public class TradeControllerTest {

    private MockMvc mockMvc;

    @Mock
    private TradeService tradeService;

    @InjectMocks
    private TradeController tradeController;

    @BeforeEach
    public void setup() {
        mockMvc = MockMvcBuilders.standaloneSetup(tradeController).build();
    }
    
    @Test
    public void testHomeWithAdmin() throws Exception {
        // Arrange
        List<GrantedAuthority> authorities = new ArrayList<>();
        authorities.add(new SimpleGrantedAuthority("ROLE_ADMIN"));

        Authentication auth = new UsernamePasswordAuthenticationToken("admin", "password", authorities);
        SecurityContextHolder.getContext().setAuthentication(auth);
        MockHttpServletRequest request = new MockHttpServletRequest();
        MockMvc mockMvc = MockMvcBuilders.standaloneSetup(tradeController).build();

        // Act
        mockMvc.perform(MockMvcRequestBuilders.get("/trade/list"))
            .andExpect(MockMvcResultMatchers.status().isOk())
            .andExpect(MockMvcResultMatchers.view().name("trade/list"))
            .andExpect(MockMvcResultMatchers.model().attributeExists("trades"))
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
        MockMvc mockMvc = MockMvcBuilders.standaloneSetup(tradeController).build();

        // Act
        mockMvc.perform(MockMvcRequestBuilders.get("/trade/list"))
            .andExpect(MockMvcResultMatchers.status().isOk())
            .andExpect(MockMvcResultMatchers.view().name("trade/list"))
            .andExpect(MockMvcResultMatchers.model().attributeExists("trades"))
            .andExpect(MockMvcResultMatchers.model().attribute("isAdmin", false));
    }


    @Test
    @WithMockUser
    public void testAddTradeForm() throws Exception {
        mockMvc.perform(get("/trade/add"))
               .andExpect(status().isOk())
               .andExpect(view().name("trade/add"))
               .andExpect(model().attributeExists("trade"));
    }

    @Test
    @WithMockUser
    public void testValidate_noErrors() throws Exception {
        Trade trade = new Trade();
        mockMvc.perform(post("/trade/validate").flashAttr("trade", trade))
               .andExpect(status().is3xxRedirection())
               .andExpect(redirectedUrl("/trade/list"));

        verify(tradeService, times(1)).save(trade);
    }

    @Test
    @WithMockUser
    public void testShowUpdateForm() throws Exception {
        Trade trade = new Trade();
        when(tradeService.findById(1)).thenReturn(trade);

        mockMvc.perform(get("/trade/update/1"))
               .andExpect(status().isOk())
               .andExpect(view().name("trade/update"))
               .andExpect(model().attributeExists("trade"));
    }

    @Test
    @WithMockUser
    public void testUpdateTrade_noErrors() throws Exception {
        Trade trade = new Trade();
        mockMvc.perform(post("/trade/update/1").flashAttr("trade", trade))
               .andExpect(status().is3xxRedirection())
               .andExpect(redirectedUrl("/trade/list"));

        verify(tradeService, times(1)).updateTrade(eq(1), eq(trade));
    }

    @Test
    @WithMockUser
    public void testDeleteTrade() throws Exception {
        mockMvc.perform(get("/trade/delete/1"))
               .andExpect(status().is3xxRedirection())
               .andExpect(redirectedUrl("/trade/list"));

        verify(tradeService, times(1)).deleteById(1);
    }
}
