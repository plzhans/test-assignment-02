package com.plzhans.assignment.api.service.spread;

import lombok.val;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertEquals;

@DisplayName("token 생성기")
public class SpreadTokenGeneratorTest {

    SpreadTokenGenerator tokenGenerator;

    /**
     * Init.
     */
    @BeforeEach
    public void init() {
        this.tokenGenerator = new SpreadTokenGenerator();
    }

    @DisplayName("token 생성 테스트")
    @Test
    public void nextToken_ok(){
        assertEquals(tokenGenerator.nextToken().length(), 3);

        for(int i=1; i<=10;i++){
            val token = tokenGenerator.nextToken(i);
            assertEquals(token.length() , i);
        }
    }

}