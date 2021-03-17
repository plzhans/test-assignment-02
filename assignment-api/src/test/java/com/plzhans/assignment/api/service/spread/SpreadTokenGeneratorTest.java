package com.plzhans.assignment.api.service.spread;

import lombok.val;
import org.junit.Before;
import org.junit.Test;

import static org.junit.Assert.assertEquals;

public class SpreadTokenGeneratorTest {

    SpreadTokenGenerator tokenGenerator;

    /**
     * Init.
     */
    @Before
    public void init() {
        this.tokenGenerator = new SpreadTokenGenerator();
    }

    @Test
    public void nextToken_ok(){
        assertEquals(tokenGenerator.nextToken().length(), 3);

        for(int i=1; i<=10;i++){
            val token = tokenGenerator.nextToken(i);
            assertEquals(token.length() , i);
        }
    }

}