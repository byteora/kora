package org.byteora.kyra.json;

import org.byteora.kyra.core.annotation.Reflect;
import org.junit.jupiter.api.Test;

import java.math.BigDecimal;
import java.math.BigInteger;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;

public class JsonCoercionTest {

    @Reflect
    public static final class Scalars {
        public byte b;
        public short s;
        public int i;
        public long l;
        public float f;
        public double d;
        public char c;
        public boolean flag;
        public BigInteger bigInt;
        public BigDecimal bigDec;
        public String text;
    }

    private final JsonMapper mapper = JsonMapper.builder().build();

    @Test
    void coercesNumericStringsToNumbers() {
        String json = "{\"b\":\"1\",\"s\":\"2\",\"i\":\"3\",\"l\":\"4\","
                + "\"f\":\"1.5\",\"d\":\"2.5\",\"bigInt\":\"123\",\"bigDec\":\"1.25\"}";
        Scalars scalars = mapper.fromJson(json, Scalars.class);
        assertEquals((byte) 1, scalars.b);
        assertEquals((short) 2, scalars.s);
        assertEquals(3, scalars.i);
        assertEquals(4L, scalars.l);
        assertEquals(1.5f, scalars.f);
        assertEquals(2.5d, scalars.d);
        assertEquals(new BigInteger("123"), scalars.bigInt);
        assertEquals(new BigDecimal("1.25"), scalars.bigDec);
    }

    @Test
    void coercesNumberToString() {
        Scalars scalars = mapper.fromJson("{\"text\":123}", Scalars.class);
        assertEquals("123", scalars.text);
    }

    @Test
    void coercesBooleanToStringAndBack() {
        Scalars scalars = mapper.fromJson("{\"text\":true,\"flag\":\"true\"}", Scalars.class);
        assertEquals("true", scalars.text);
        assertEquals(true, scalars.flag);
    }

    @Test
    void coercesNumberToBoolean() {
        assertEquals(true, mapper.fromJson("{\"flag\":1}", Scalars.class).flag);
        assertEquals(false, mapper.fromJson("{\"flag\":0}", Scalars.class).flag);
        assertEquals(true, mapper.fromJson("{\"flag\":\"1\"}", Scalars.class).flag);
    }

    @Test
    void truncatesFractionForIntegralTarget() {
        Scalars scalars = mapper.fromJson("{\"i\":3.9,\"l\":\"7.8\"}", Scalars.class);
        assertEquals(3, scalars.i);
        assertEquals(7L, scalars.l);
    }

    @Test
    void failsOnIntegralOverflow() {
        JsonException ex = assertThrows(JsonException.class,
                () -> mapper.fromJson("{\"b\":9999}", Scalars.class));
        assertEquals(true, ex.getMessage().contains("Scalars.b"));
    }

    @Test
    void failsOnNonNumericString() {
        assertThrows(JsonException.class, () -> mapper.fromJson("{\"i\":\"abc\"}", Scalars.class));
    }

    @Test
    void blankStringYieldsDefault() {
        Scalars scalars = mapper.fromJson("{\"i\":\"\",\"bigInt\":\"\"}", Scalars.class);
        assertEquals(0, scalars.i);
        assertEquals(null, scalars.bigInt);
    }
}
