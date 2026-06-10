package org.byteora.kyra.core;

import java.lang.reflect.ParameterizedType;
import java.lang.reflect.Type;
import java.util.Objects;

public final class EnumSupport {
    private EnumSupport() {
    }

    public static boolean isIEnum(Class<?> type) {
        return type != null && type.isEnum() && IEnum.class.isAssignableFrom(type);
    }

    public static Object toValue(Object enumConstant) {
        return ((IEnum<?>) enumConstant).getValue();
    }

    public static Type valueType(Class<?> enumClass) {
        for (Type genericInterface : enumClass.getGenericInterfaces()) {
            if (genericInterface instanceof ParameterizedType parameterizedType
                    && parameterizedType.getRawType() == IEnum.class) {
                return parameterizedType.getActualTypeArguments()[0];
            }
        }
        return Object.class;
    }

    @SuppressWarnings("unchecked")
    public static <E extends Enum<E>> E parse(Class<E> enumType, Object value) {
        E[] constants = enumType.getEnumConstants();
        if (constants == null || constants.length == 0) {
            throw new IllegalArgumentException("Enum " + enumType.getName() + " has no constants");
        }
        for (E constant : constants) {
            if (Objects.equals(((IEnum<Object>) constant).getValue(), value)) {
                return constant;
            }
        }
        throw new IllegalArgumentException("No " + enumType.getSimpleName() + " constant matches value: " + value);
    }
}
