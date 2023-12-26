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

import com.nnk.springboot.domain.Trade;
import com.nnk.springboot.repositories.TradeRepository;

public class TradeServiceTest {

    @Mock
    private TradeRepository tradeRepository;

    @InjectMocks
    private TradeService tradeService;

    @BeforeEach
    public void setup() {
        MockitoAnnotations.initMocks(this);
    }

    @Test
    public void testSave() {
        Trade trade = new Trade();
        tradeService.save(trade);
        verify(tradeRepository, times(1)).save(trade);
    }

    @Test
    public void testFindAll() {
        when(tradeRepository.findAll()).thenReturn(Arrays.asList(new Trade(), new Trade()));
        assertEquals(2, tradeService.findAll().size());
    }

    @Test
    public void testFindById() {
        Integer id = 1;
        when(tradeRepository.findById(id)).thenReturn(Optional.of(new Trade()));
        assertNotNull(tradeService.findById(id));
    }

    @Test
    public void testUpdateTrade() {
        Integer id = 1;
        Trade existingTrade = new Trade();
        Trade updatedTrade = new Trade();
        updatedTrade.setAccount("Account Updated");
        updatedTrade.setType("Type Updated");
        updatedTrade.setBuyQuantity(1000.0);

        when(tradeRepository.findById(id)).thenReturn(Optional.of(existingTrade));
        tradeService.updateTrade(id, updatedTrade);

        verify(tradeRepository, times(1)).save(existingTrade);
        assertEquals("Account Updated", existingTrade.getAccount());
        assertEquals("Type Updated", existingTrade.getType());
        assertEquals(1000.0, existingTrade.getBuyQuantity());
    }

    @Test
    public void testDeleteById() {
        Integer id = 1;
        tradeService.deleteById(id);
        verify(tradeRepository, times(1)).deleteById(id);
    }
}
