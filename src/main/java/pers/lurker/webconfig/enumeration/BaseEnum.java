package pers.lurker.webconfig.enumeration;

public interface BaseEnum<T> {

    T getCode();

    default String getValue() {
        return null;
    }
}
