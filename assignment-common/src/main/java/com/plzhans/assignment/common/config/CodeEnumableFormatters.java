package com.plzhans.assignment.common.config;

import com.plzhans.assignment.common.domain.CodeEnumable;
import org.springframework.format.FormatterRegistry;

public class CodeEnumableFormatters {

    /**
     * Registry.
     *
     * @param <T>      the type parameter
     * @param registry the registry
     * @param clazz    the clazz
     */
    public static <T  extends CodeEnumable> void registry(FormatterRegistry registry, Class<T> clazz) {
        registry.addConverter(String.class, clazz, new CodeEnumableConverter(clazz));
    }
}
