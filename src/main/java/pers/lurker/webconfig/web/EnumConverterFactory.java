package pers.lurker.webconfig.web;

import org.springframework.core.convert.converter.Converter;
import org.springframework.core.convert.converter.ConverterFactory;
import pers.lurker.webconfig.enumeration.BaseEnum;

import java.util.HashMap;
import java.util.Map;
import java.util.WeakHashMap;

/**
 * 接收枚举类型参数的自动绑定
 */
public class EnumConverterFactory implements ConverterFactory<String, BaseEnum> {

    private static final Map<Class, Converter> converterMap = new WeakHashMap<>();

    @Override
    public <T extends BaseEnum> Converter<String, T> getConverter(Class<T> targetType) {
        return converterMap.computeIfAbsent(targetType, k -> converterMap.put(k, new IntegerStrToEnum<T>(targetType)));
    }

    class IntegerStrToEnum<T extends BaseEnum> implements Converter<String, T> {
        private Map<String, T> enumMap = new HashMap<>();

        public IntegerStrToEnum(Class<T> enumType) {
            T[] enums = enumType.getEnumConstants();
            for(T e : enums) {
                enumMap.put(e.getCode() + "", e);
            }
        }


        @Override
        public T convert(String source) {
            T result = enumMap.get(source);
            if(result == null) {
                throw new IllegalArgumentException("No element matches " + source);
            }
            return result;
        }
    }

}
