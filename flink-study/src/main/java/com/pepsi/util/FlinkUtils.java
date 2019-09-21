package com.pepsi.util;

import java.time.Instant;
import java.time.LocalDateTime;
import java.time.ZoneId;
import java.time.format.DateTimeFormatter;
import java.time.format.DateTimeFormatterBuilder;
import java.time.temporal.ChronoField;
import java.time.temporal.TemporalAccessor;
import java.util.Date;

/**
 * Created with IntelliJ IDEA.
 * User: pepsi
 * Date: 2019-09-20 15:46
 * Description: No Description
 */
public class FlinkUtils {


    // 精确到毫秒, e.g. "2019-06-07T07:03:00.445Z"
    public static final DateTimeFormatter ISO_INSTANT_MS = new DateTimeFormatterBuilder().parseCaseInsensitive().appendInstant(3).toFormatter();

    // "e.g. "2019-06-07 07:03:00"
    public static final DateTimeFormatter DATETIME_FORMATTER = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss");

    public static long parseFilebeatTimestamp(String value) {
        TemporalAccessor tempo = ISO_INSTANT_MS.parse(value);
        return tempo.getLong(ChronoField.INSTANT_SECONDS) * 1000 + tempo.getLong(ChronoField.MILLI_OF_SECOND);
    }

    public static String formatFilebeatTimestamp(long value) {
        return ISO_INSTANT_MS.format(Instant.ofEpochMilli(value));
    }

    public static long parseIsoTimestamp(String value) {
        TemporalAccessor tempo = DateTimeFormatter.ISO_OFFSET_DATE_TIME.parse(value);
        return tempo.getLong(ChronoField.INSTANT_SECONDS) * 1000 + tempo.getLong(ChronoField.MILLI_OF_SECOND);
    }

    public static String formatIsoTimestamp(long value) {
        return DateTimeFormatter.ISO_OFFSET_DATE_TIME.format(Instant.ofEpochMilli(value).atZone(ZoneId.systemDefault()));
    }

    public static Date parseTimestamp(String value) {
        return Date.from(LocalDateTime.parse(value, DATETIME_FORMATTER).atZone(ZoneId.systemDefault()).toInstant());
    }

    public static String formatTimestamp(Long time) {
        return DATETIME_FORMATTER.format(LocalDateTime.ofInstant(Instant.ofEpochMilli(time), ZoneId.systemDefault()));
    }


}
