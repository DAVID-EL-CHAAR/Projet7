package com.nnk.springboot;

import com.nnk.springboot.domain.BidList;
import com.nnk.springboot.repositories.BidListRepository;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.Assertions;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import java.util.List;
import java.util.Optional;

@SpringBootTest
public class BidTests {

    @Autowired
    private BidListRepository bidListRepository;

    @Test
    public void bidListTest() {
        BidList bid = new BidList();
        bid.setAccount("Account Test");
        bid.setType("Type Test");
        bid.setBidQuantity(10d);

        // Save
        bid = bidListRepository.save(bid);
        Assertions.assertNotNull(bid.getBidListId());
        Assertions.assertEquals(10d, bid.getBidQuantity(), 0.01);

        // Update
        bid.setBidQuantity(20d);
        bid = bidListRepository.save(bid);
        Assertions.assertEquals(20d, bid.getBidQuantity(), 0.01);

        // Find
        List<BidList> listResult = bidListRepository.findAll();
        Assertions.assertTrue(listResult.size() > 0);

        // Delete
        Integer id = bid.getBidListId();
        bidListRepository.delete(bid);
        Optional<BidList> bidList = bidListRepository.findById(id);
        Assertions.assertFalse(bidList.isPresent());
    }
}
