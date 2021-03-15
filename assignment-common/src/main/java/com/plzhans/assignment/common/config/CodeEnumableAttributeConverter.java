package com.plzhans.assignment.common.config;

import com.plzhans.assignment.common.domain.CodeEnumable;

import javax.persistence.AttributeConverter;
import java.lang.reflect.ParameterizedType;
import java.util.Arrays;
import java.util.Map;
import java.util.function.Function;
import java.util.stream.Collectors;

public class CodeEnumableAttributeConverter<T extends CodeEnumable> implements AttributeConverter<T, Integer> {

    private final Map<Integer, T> map;

    public CodeEnumableAttributeConverter() {
        Class<T> clazz = (Class<T>) ((ParameterizedType)this.getClass().getGenericSuperclass()).getActualTypeArguments()[0];
        this.map = Arrays.stream(clazz.getEnumConstants())
                .collect(Collectors.toMap(CodeEnumable::getInt, Function.identity()));
    }

    @Override
    public Integer convertToDatabaseColumn(CodeEnumable value) {
        return value.getInt();
    }

    @Override
    public T convertToEntityAttribute(Integer key) {
        T value = this.map.get(key);
        if (value == null) {
            throw new IllegalArgumentException(String.format("%s is invalid value.", key));
        }
        return value;
    }
}

