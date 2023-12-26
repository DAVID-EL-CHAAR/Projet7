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

import com.nnk.springboot.domain.CurvePoint;
import com.nnk.springboot.repositories.CurvePointRepository;

public class CurvePointServiceTest {

    @Mock
    private CurvePointRepository curvePointRepository;

    @InjectMocks
    private CurvePointService curvePointService;

    @BeforeEach
    public void setup() {
        MockitoAnnotations.initMocks(this);
    }

    @Test
    public void testSaveCurvePoint() {
        CurvePoint curvePoint = new CurvePoint();
        curvePointService.saveCurvePoint(curvePoint);
        verify(curvePointRepository, times(1)).save(curvePoint);
    }

    @Test
    public void testFindAll() {
        when(curvePointRepository.findAll()).thenReturn(Arrays.asList(new CurvePoint(), new CurvePoint()));
        assertEquals(2, curvePointService.findAll().size());
    }

    @Test
    public void testFindById() {
        Integer id = 1;
        when(curvePointRepository.findById(id)).thenReturn(Optional.of(new CurvePoint()));
        assertNotNull(curvePointService.findById(id));
    }

    @Test
    public void testUpdateCurvePoint() {
        Integer id = 1;
        CurvePoint existingCurvePoint = new CurvePoint();
        CurvePoint updatedCurvePoint = new CurvePoint();
        updatedCurvePoint.setTerm(10.0);
        updatedCurvePoint.setValue(20.0);

        when(curvePointRepository.findById(id)).thenReturn(Optional.of(existingCurvePoint));
        curvePointService.updateCurvePoint(id, updatedCurvePoint);

        verify(curvePointRepository, times(1)).save(existingCurvePoint);
        assertEquals(10.0, existingCurvePoint.getTerm());
        assertEquals(20.0, existingCurvePoint.getValue());
    }

    @Test
    public void testDeleteById() {
        Integer id = 1;
        curvePointService.deleteById(id);
        verify(curvePointRepository, times(1)).deleteById(id);
    }
}
