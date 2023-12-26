package com.nnk.springboot.service;

import static org.mockito.Mockito.*;
import static org.junit.jupiter.api.Assertions.*;

import java.util.Arrays;
import java.util.Optional;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import com.nnk.springboot.domain.RuleName;
import com.nnk.springboot.repositories.RuleNameRepository;

public class RuleNameServiceTest {

    @Mock
    private RuleNameRepository ruleNameRepository;

    @InjectMocks
    private RuleNameService ruleNameService;

    @BeforeEach
    public void setup() {
        MockitoAnnotations.initMocks(this);
    }

    @Test
    public void testSave() {
        RuleName ruleName = new RuleName();
        ruleNameService.save(ruleName);
        verify(ruleNameRepository, times(1)).save(ruleName);
    }

    @Test
    public void testFindAll() {
        when(ruleNameRepository.findAll()).thenReturn(Arrays.asList(new RuleName(), new RuleName()));
        assertEquals(2, ruleNameService.findAll().size());
    }

    @Test
    public void testFindById() {
        Integer id = 1;
        when(ruleNameRepository.findById(id)).thenReturn(Optional.of(new RuleName()));
        assertNotNull(ruleNameService.findById(id));
    }

    @Test
    public void testUpdateRuleName() {
        Integer id = 1;
        RuleName existingRuleName = new RuleName();
        RuleName updatedRuleName = new RuleName();
        updatedRuleName.setName("New Name");
        updatedRuleName.setDescription("New Description");
        // Autres propriétés...

        when(ruleNameRepository.findById(id)).thenReturn(Optional.of(existingRuleName));
        ruleNameService.updateRuleName(id, updatedRuleName);

        verify(ruleNameRepository, times(1)).save(existingRuleName);
        assertEquals("New Name", existingRuleName.getName());
        assertEquals("New Description", existingRuleName.getDescription());
        // Vérifications pour autres propriétés...
    }

    @Test
    public void testDeleteById() {
        Integer id = 1;
        ruleNameService.deleteById(id);
        verify(ruleNameRepository, times(1)).deleteById(id);
    }
}
