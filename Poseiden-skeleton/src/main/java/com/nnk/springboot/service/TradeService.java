package com.nnk.springboot.service;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.nnk.springboot.domain.Trade;
import com.nnk.springboot.repositories.TradeRepository;

@Service
public class TradeService {

    @Autowired
    private TradeRepository tradeRepository;

    public void save(Trade trade) {
        tradeRepository.save(trade);
    }
    
    public List<Trade> findAll() {
        return tradeRepository.findAll();
    }
    
    public Trade findById(Integer id) {
        return tradeRepository.findById(id).orElse(null);
    }
    
    public void updateTrade(Integer id, Trade updatedTrade) {
        Trade existingTrade = tradeRepository.findById(id).orElse(null);
        if (existingTrade != null) {
            // Mettre à jour les propriétés de existingTrade avec celles de updatedTrade
            existingTrade.setAccount(updatedTrade.getAccount());
            existingTrade.setType(updatedTrade.getType());
            existingTrade.setBuyQuantity(updatedTrade.getBuyQuantity());
            // ... mettre à jour d'autres champs si nécessaire ...

            tradeRepository.save(existingTrade);
        }
    }

    
    public void deleteById(Integer id) {
        tradeRepository.deleteById(id);
    }

    
}
