package org.byteora.kyra.json;

import org.byteora.kyra.core.annotation.Reflect;
import org.junit.jupiter.api.Test;

import java.util.List;

import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.api.Assertions.assertTrue;

public class JsonErrorMessageTest {

    @Reflect
    public static final class Account {
        public String name;
        public int age;
    }

    @Reflect
    public static final class Order {
        public List<Account> accounts;
    }

    @Test
    void reportsFieldAndExpectedTypeForScalarMismatch() {
        JsonMapper mapper = JsonMapper.builder().build();
        JsonException ex = assertThrows(JsonException.class,
                () -> mapper.fromJson("{\"name\":\"a\",\"age\":\"abc\"}", Account.class));
        String message = ex.getMessage();
        assertTrue(message.contains("Account.age"), message);
        assertTrue(message.contains("int"), message);
        assertTrue(message.contains("VALUE_STRING"), message);
    }

    @Test
    void reportsNestedPathWithCollectionIndex() {
        JsonMapper mapper = JsonMapper.builder().build();
        JsonException ex = assertThrows(JsonException.class,
                () -> mapper.fromJson("{\"accounts\":[{\"name\":\"a\",\"age\":1},{\"name\":\"b\",\"age\":\"xyz\"}]}",
                        Order.class));
        String message = ex.getMessage();
        assertTrue(message.contains("Order.accounts[1].age"), message);
        assertTrue(message.contains("int"), message);
    }

    @Test
    void reportsExpectedStructureWhenTokenIsWrong() {
        JsonMapper mapper = JsonMapper.builder().build();
        JsonException ex = assertThrows(JsonException.class,
                () -> mapper.fromJson("{\"accounts\":5}", Order.class));
        String message = ex.getMessage();
        assertTrue(message.contains("Order.accounts"), message);
        assertTrue(message.contains("START_ARRAY"), message);
    }
}
