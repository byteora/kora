package org.byteora.kyra.json;

import org.byteora.kyra.core.annotation.Reflect;
import org.junit.jupiter.api.Test;

import java.time.Instant;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.time.OffsetDateTime;
import java.time.ZoneOffset;
import java.time.ZonedDateTime;
import java.util.Date;

import static org.junit.jupiter.api.Assertions.assertEquals;

public class JsonDateTimeTest {

    private final JsonMapper mapper = JsonMapper.builder().build();

    @Reflect
    public static final class Holder {
        public Date legacyDate;
        public Instant instant;
        public LocalDate localDate;
        public LocalDateTime localDateTime;
        public LocalTime localTime;
        public OffsetDateTime offsetDateTime;
        public ZonedDateTime zonedDateTime;
    }

    @Test
    void serializesLegacyDateAsIso8601() {
        Date date = Date.from(Instant.parse("2026-06-11T02:51:00Z"));
        assertEquals("\"2026-06-11T02:51:00Z\"", mapper.toJson(date));
    }

    @Test
    void roundTripsLegacyDate() {
        Date date = Date.from(Instant.parse("2026-06-11T02:51:00Z"));
        String json = mapper.toJson(date);
        assertEquals(date, mapper.fromJson(json, Date.class));
    }

    @Test
    void readsLegacyDateFromEpochMillis() {
        Date date = Date.from(Instant.parse("2026-06-11T02:51:00Z"));
        assertEquals(date, mapper.fromJson(Long.toString(date.getTime()), Date.class));
    }

    @Test
    void roundTripsJavaTimeTypes() {
        Holder holder = new Holder();
        holder.legacyDate = Date.from(Instant.parse("2026-06-11T02:51:00Z"));
        holder.instant = Instant.parse("2026-06-11T02:51:00Z");
        holder.localDate = LocalDate.parse("2026-06-11");
        holder.localDateTime = LocalDateTime.parse("2026-06-11T11:51:00");
        holder.localTime = LocalTime.parse("11:51:00");
        holder.offsetDateTime = OffsetDateTime.parse("2026-06-11T11:51:00+09:00");
        holder.zonedDateTime = ZonedDateTime.of(2026, 6, 11, 11, 51, 0, 0, ZoneOffset.ofHours(9));

        String json = mapper.toJson(holder);
        Holder decoded = mapper.fromJson(json, Holder.class);

        assertEquals(holder.legacyDate, decoded.legacyDate);
        assertEquals(holder.instant, decoded.instant);
        assertEquals(holder.localDate, decoded.localDate);
        assertEquals(holder.localDateTime, decoded.localDateTime);
        assertEquals(holder.localTime, decoded.localTime);
        assertEquals(holder.offsetDateTime, decoded.offsetDateTime);
        assertEquals(holder.zonedDateTime, decoded.zonedDateTime);
    }
}
