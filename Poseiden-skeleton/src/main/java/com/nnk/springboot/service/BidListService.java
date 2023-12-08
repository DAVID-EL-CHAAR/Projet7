package com.nnk.springboot.service;

import java.util.List;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.nnk.springboot.domain.BidList;
import com.nnk.springboot.domain.User;
import com.nnk.springboot.repositories.BidListRepository;
import com.nnk.springboot.repositories.UserRepository;

@Service
public class BidListService {
    @Autowired
    private BidListRepository bidListRepository;

    public void save(BidList bidList) {
        bidListRepository.save(bidList);
    }
    
    public List<BidList> findAllBids() {
        return bidListRepository.findAll();
    }
    /*
    public BidList findById(Integer id) {
        return bidListRepository.findById(id).orElseThrow(() -> new RuntimeException("Bid not found"));
    }
    */
    
    public Optional<BidList> findById(Integer id) {
        return bidListRepository.findById(id);
    }
    
    public void update(BidList bidList) {
        bidListRepository.save(bidList);
    }
    
    public void deleteById(Integer id) {
        bidListRepository.deleteById(id);
    }

}
