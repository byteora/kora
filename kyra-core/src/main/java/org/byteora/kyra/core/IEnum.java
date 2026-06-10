package org.byteora.kyra.core;

/**
 * Lets an {@code enum} control how it is encoded to and decoded from external values such as JSON
 * or database columns, instead of relying on {@link Enum#name()} or {@link Enum#ordinal()}.
 *
 * <p>An enum implements this interface so that serializers call {@link #getValue()} and
 * deserializers resolve constants by matching the encoded value against each constant's
 * {@link #getValue()}.
 *
 * <pre>{@code
 * enum Status implements IEnum<Integer> {
 *     ON(1), OFF(0);
 *     private final int code;
 *     Status(int code) { this.code = code; }
 *     public Integer getValue() { return code; }
 * }
 * }</pre>
 *
 * @param <V> the encoded value type
 */
public interface IEnum<V> {
    /**
     * Returns the encoded value this constant should be written as.
     */
    V getValue();
}
