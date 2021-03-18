package com.plzhans.assignment.api.repository.cache;

import org.springframework.stereotype.Repository;

@Repository
public interface CacheRepository {

    <T> void setValue(String key, T value, int expiredSeconds);

    <T> T getValue(String key);
}
