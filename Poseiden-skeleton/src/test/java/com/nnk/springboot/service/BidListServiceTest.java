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

import com.nnk.springboot.domain.BidList;
import com.nnk.springboot.repositories.BidListRepository;

public class BidListServiceTest {

    @Mock
    private BidListRepository bidListRepository;

    @InjectMocks
    private BidListService bidListService;

    @BeforeEach
    public void setup() {
        MockitoAnnotations.initMocks(this);
    }

    @Test
    public void testSaveBid() {
        BidList bid = new BidList();
        bidListService.save(bid);
        verify(bidListRepository, times(1)).save(bid);
    }

    @Test
    public void testFindAllBids() {
        when(bidListRepository.findAll()).thenReturn(Arrays.asList(new BidList(), new BidList()));
        assertEquals(2, bidListService.findAllBids().size());
    }

    @Test
    public void testFindById() {
        Integer id = 1;
        when(bidListRepository.findById(id)).thenReturn(Optional.of(new BidList()));
        assertTrue(bidListService.findById(id).isPresent());
    }

    @Test
    public void testUpdateBid() {
        BidList bid = new BidList();
        bidListService.update(bid);
        verify(bidListRepository, times(1)).save(bid);
    }

    @Test
    public void testDeleteById() {
        Integer id = 1;
        bidListService.deleteById(id);
        verify(bidListRepository, times(1)).deleteById(id);
    }
}
