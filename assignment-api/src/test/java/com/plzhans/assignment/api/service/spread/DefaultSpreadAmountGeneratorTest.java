package com.plzhans.assignment.api.service.spread;

import lombok.val;
import org.junit.Before;
import org.junit.Test;

import static org.junit.Assert.assertEquals;

public class DefaultSpreadAmountGeneratorTest {
    SpreadAmountGenerator amountGenerator;

    /**
     * Init.
     */
    @Before
    public void init() {
        this.amountGenerator = new DefaultSpreadAmountGenerator();
    }

    @Test
    public void generateToList_ok() {
        int totalAmount = 1000;
        int count = 5;
        val list = amountGenerator.generateToList(totalAmount, count);

        for(int amount : list){
            totalAmount -= amount;
        }

        assertEquals(list.length, count);
        assertEquals(totalAmount, 0);
    }
}