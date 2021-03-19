package com.plzhans.assignment.api.service.spread;

import lombok.val;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertEquals;

@DisplayName("DefaultSpreadAmountGenerator - Test")
public class DefaultSpreadAmountGeneratorTest {
    SpreadAmountGenerator amountGenerator;

    /**
     * Init.
     */
    @BeforeEach
    public void init() {
        this.amountGenerator = new DefaultSpreadAmountGenerator();
    }

    @DisplayName("뿌리기 분배")
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