package com.plzhans.assignment.common.config;

import com.plzhans.assignment.common.domain.CodeEnumable;
import org.springframework.core.convert.converter.Converter;

import java.util.Arrays;
import java.util.Map;
import java.util.function.Function;
import java.util.stream.Collectors;

/**
 * The type Code enumable converter.
 *
 * @param <T> the type parameter
 */
public class CodeEnumableConverter<T extends CodeEnumable> implements Converter<String, T> {
    private final Map<Integer, T> map;

    /**
     * Instantiates a new Code enumable converter.
     *
     * @param clazz the clazz
     */
    public CodeEnumableConverter(Class<T> clazz) {
        this.map = Arrays.stream(clazz.getEnumConstants())
                .collect(Collectors.toMap(CodeEnumable::getInt, Function.identity()));
    }

    @Override
    public T convert(String key) {
        T value = this.map.get(Integer.valueOf(key));
        if (value == null) {
            throw new IllegalArgumentException(String.format("%s is invalid value.", key));
        }
        return value;
    }
}
