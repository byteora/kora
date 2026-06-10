package org.byteora.kyra.orm.runtime;

import org.byteora.kyra.core.IEnum;
import org.junit.jupiter.api.Test;

import java.lang.reflect.Proxy;
import java.sql.ResultSet;
import java.sql.SQLException;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertSame;

class TypeConverterIEnumTest {

    enum Status implements IEnum<Integer> {
        ON(1), OFF(0);

        private final int code;

        Status(int code) {
            this.code = code;
        }

        @Override
        public Integer getValue() {
            return code;
        }
    }

    private final TypeConverter typeConverter = new TypeConverter();

    @Test
    void fieldToColumnUsesGetValue() {
        assertEquals(1, typeConverter.fieldToColumn(Status.ON));
        assertEquals(0, typeConverter.fieldToColumn(Status.OFF));
    }

    @Test
    void castUsesParse() throws SQLException {
        ResultSet resultSet = resultSet(1);
        assertSame(Status.ON, typeConverter.cast(resultSet, 1, Status.class));
    }

    private ResultSet resultSet(Object value) {
        return (ResultSet) Proxy.newProxyInstance(
                ResultSet.class.getClassLoader(),
                new Class[]{ResultSet.class},
                (proxy, method, args) -> {
                    if ("getObject".equals(method.getName())) {
                        return value;
                    }
                    throw new UnsupportedOperationException(method.getName());
                });
    }
}
