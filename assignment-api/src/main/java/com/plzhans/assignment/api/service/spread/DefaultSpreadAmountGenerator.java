package com.plzhans.assignment.api.service.spread;

import java.util.Random;

/**
 * The type Default spread amount generator.
 */
public class DefaultSpreadAmountGenerator implements SpreadAmountGenerator {

    /**
     * The Random.
     */
    Random random;

    /**
     * Instantiates a new Default spread amount generator.
     */
    public DefaultSpreadAmountGenerator() {
        this.random = new Random();
    }

    private int getRandomRange(int min, int max) {
        return this.random.nextInt(max - min + 1) + min;
    }

    @Override
    public int[] generateToList(int totalAmount, int totalCount) {

        int[] results = new int[totalCount];

        if (totalCount > 1) {
            int tempTotal = totalAmount;
            for (int i = 1; i < totalCount; i++) {
                if(tempTotal == 0){
                    break;
                }
                results[i] = getRandomRange(1, tempTotal);
                tempTotal -= results[i];
            }
            results[0] = tempTotal;
        }

        return results;
    }
}
