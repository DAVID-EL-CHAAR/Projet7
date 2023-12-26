package com.nnk.springboot.controllers;

import com.nnk.springboot.domain.RuleName;
import com.nnk.springboot.service.RuleNameService;
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
public class RuleNameControllerTest {

    private MockMvc mockMvc;

    @Mock
    private RuleNameService ruleNameService;

    @InjectMocks
    private RuleNameController ruleNameController;

    @BeforeEach
    public void setup() {
        mockMvc = MockMvcBuilders.standaloneSetup(ruleNameController).build();
    }
    
    @Test
    public void testHomeWithAdmin() throws Exception {
        // Arrange
        List<GrantedAuthority> authorities = new ArrayList<>();
        authorities.add(new SimpleGrantedAuthority("ROLE_ADMIN"));

        Authentication auth = new UsernamePasswordAuthenticationToken("admin", "password", authorities);
        SecurityContextHolder.getContext().setAuthentication(auth);
        MockHttpServletRequest request = new MockHttpServletRequest();
        MockMvc mockMvc = MockMvcBuilders.standaloneSetup(ruleNameController).build();

        // Act
        mockMvc.perform(MockMvcRequestBuilders.get("/ruleName/list"))
            .andExpect(MockMvcResultMatchers.status().isOk())
            .andExpect(MockMvcResultMatchers.view().name("ruleName/list"))
            .andExpect(MockMvcResultMatchers.model().attributeExists("ruleNames"))
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
        MockMvc mockMvc = MockMvcBuilders.standaloneSetup(ruleNameController).build();

        // Act
        mockMvc.perform(MockMvcRequestBuilders.get("/ruleName/list"))
            .andExpect(MockMvcResultMatchers.status().isOk())
            .andExpect(MockMvcResultMatchers.view().name("ruleName/list"))
            .andExpect(MockMvcResultMatchers.model().attributeExists("ruleNames"))
            .andExpect(MockMvcResultMatchers.model().attribute("isAdmin", false));
    }


    @Test
    @WithMockUser
    public void testAddRuleForm() throws Exception {
        mockMvc.perform(get("/ruleName/add"))
               .andExpect(status().isOk())
               .andExpect(view().name("ruleName/add"))
               .andExpect(model().attributeExists("ruleName"));
    }

    @Test
    @WithMockUser
    public void testValidate_noErrors() throws Exception {
        RuleName ruleName = new RuleName();
        mockMvc.perform(post("/ruleName/validate").flashAttr("ruleName", ruleName))
               .andExpect(status().is3xxRedirection())
               .andExpect(redirectedUrl("/ruleName/list"));

        verify(ruleNameService, times(1)).save(ruleName);
    }

    @Test
    @WithMockUser
    public void testShowUpdateForm() throws Exception {
        RuleName ruleName = new RuleName();
        when(ruleNameService.findById(1)).thenReturn(ruleName);

        mockMvc.perform(get("/ruleName/update/1"))
               .andExpect(status().isOk())
               .andExpect(view().name("ruleName/update"))
               .andExpect(model().attributeExists("ruleName"));
    }

    @Test
    @WithMockUser
    public void testUpdateRuleName_noErrors() throws Exception {
        RuleName ruleName = new RuleName();
        mockMvc.perform(post("/ruleName/update/1").flashAttr("ruleName", ruleName))
               .andExpect(status().is3xxRedirection())
               .andExpect(redirectedUrl("/ruleName/list"));

        verify(ruleNameService, times(1)).updateRuleName(eq(1), eq(ruleName));
    }

    @Test
    @WithMockUser
    public void testDeleteRuleName() throws Exception {
        mockMvc.perform(get("/ruleName/delete/1"))
               .andExpect(status().is3xxRedirection())
               .andExpect(redirectedUrl("/ruleName/list"));

        verify(ruleNameService, times(1)).deleteById(1);
    }
}
