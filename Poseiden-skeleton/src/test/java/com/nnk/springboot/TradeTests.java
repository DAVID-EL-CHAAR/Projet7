package com.nnk.springboot;

import com.nnk.springboot.domain.Trade;
import com.nnk.springboot.repositories.TradeRepository;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.Assertions;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import java.util.List;
import java.util.Optional;

@SpringBootTest
public class TradeTests {

    @Autowired
    private TradeRepository tradeRepository;

    @Test
    public void tradeTest() {
        // Création de l'objet Trade avec des setters
        Trade trade = new Trade();
        trade.setAccount("Trade Account");
        trade.setType("Type");

        // Save
        trade = tradeRepository.save(trade);
        Assertions.assertNotNull(trade.getTradeId());
        Assertions.assertEquals("Trade Account", trade.getAccount());

        // Update
        trade.setAccount("Trade Account Update");
        trade = tradeRepository.save(trade);
        Assertions.assertEquals("Trade Account Update", trade.getAccount());

        // Find
        List<Trade> listResult = tradeRepository.findAll();
        Assertions.assertTrue(listResult.size() > 0);

        // Delete
        Integer id = trade.getTradeId();
        tradeRepository.delete(trade);
        Optional<Trade> tradeList = tradeRepository.findById(id);
        Assertions.assertFalse(tradeList.isPresent());
    }
}

