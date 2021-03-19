package com.plzhans.assignment.api.service.spread;

import lombok.val;
import org.springframework.stereotype.Component;

import java.util.UUID;

/**
 * The type Spread token generator.
 */
@Component
public class SpreadTokenGenerator {

    private static int DEFAULT_TOKEN_LENGTH = 3;

    /**
     * Next token string.
     *
     * @return the string
     */
    public String nextToken() {
        return nextToken(DEFAULT_TOKEN_LENGTH);
    }

    /**
     * Next token string.
     *
     * @param tokenLength the length
     * @return the string
     */
    public String nextToken(int tokenLength) {

        val uuid = UUID.randomUUID().toString();
        int length = uuid.length();

        val token = uuid.substring(uuid.length() - tokenLength, length);
        return token;
    }
}
