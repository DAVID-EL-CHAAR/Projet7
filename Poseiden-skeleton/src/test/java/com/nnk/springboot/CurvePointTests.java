package com.nnk.springboot;

import com.nnk.springboot.domain.CurvePoint;
import com.nnk.springboot.repositories.CurvePointRepository;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.Assertions;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import java.util.List;
import java.util.Optional;

@SpringBootTest
public class CurvePointTests {

    @Autowired
    private CurvePointRepository curvePointRepository;

    @Test
    public void curvePointTest() {
    	CurvePoint curvePoint = new CurvePoint();

    	// Définition des valeurs
    	curvePoint.setCurveId(10);
    	curvePoint.setTerm(10d);
    	curvePoint.setValue(30d);

        // Save
        curvePoint = curvePointRepository.save(curvePoint);
        Assertions.assertNotNull(curvePoint.getId());
        Assertions.assertEquals(10, curvePoint.getCurveId());

        // Update
        curvePoint.setCurveId(20);
        curvePoint = curvePointRepository.save(curvePoint);
        Assertions.assertEquals(20, curvePoint.getCurveId());

        // Find
        List<CurvePoint> listResult = curvePointRepository.findAll();
        Assertions.assertTrue(listResult.size() > 0);

        // Delete
        Integer id = curvePoint.getId();
        curvePointRepository.delete(curvePoint);
        Optional<CurvePoint> curvePointList = curvePointRepository.findById(id);
        Assertions.assertFalse(curvePointList.isPresent());
    }
}
