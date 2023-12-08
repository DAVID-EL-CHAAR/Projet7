package com.nnk.springboot.service;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.nnk.springboot.domain.BidList;
import com.nnk.springboot.domain.CurvePoint;
import com.nnk.springboot.domain.User;
import com.nnk.springboot.repositories.BidListRepository;
import com.nnk.springboot.repositories.CurvePointRepository;
import com.nnk.springboot.repositories.UserRepository;

@Service
public class CurvePointService {

    @Autowired
    private CurvePointRepository curvePointRepository;

    public void saveCurvePoint(CurvePoint curvePoint) {
        curvePointRepository.save(curvePoint);
    }
    
    public List<CurvePoint> findAll() {
        return curvePointRepository.findAll();
    }
    
    public CurvePoint findById(Integer id) {
        return curvePointRepository.findById(id).orElse(null);
    }
    
    public void updateCurvePoint(Integer id, CurvePoint updatedCurvePoint) {
        CurvePoint existingCurvePoint = curvePointRepository.findById(id).orElse(null);
        if (existingCurvePoint != null) {
            // Mettre à jour les propriétés de existingCurvePoint avec celles de updatedCurvePoint
            existingCurvePoint.setTerm(updatedCurvePoint.getTerm());
            existingCurvePoint.setValue(updatedCurvePoint.getValue());
            // ... mettre à jour d'autres champs si nécessaire ...

            curvePointRepository.save(existingCurvePoint);
        }
    }
    
    public void deleteById(Integer id) {
        curvePointRepository.deleteById(id);
    }

}

