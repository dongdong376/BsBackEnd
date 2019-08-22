package com.jca.datacommon.annotation.validator;

/**
 * 
 * @author Administrator
 *
 * @param <T>
 */
public class Value<T> {

    private String name;

    private T value;

    private Value(String name, T value) {
        this.name = name;
        this.value = value;
    }

    public static <T> Value<T> of(String name, T value) {
        return new Value<T>(name, value);
    }

    public String getName() {
        return name;
    }

    public T getValue() {
        return value;
    }
}
