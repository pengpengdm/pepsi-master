package com.pepsi.util;

import java.net.InetSocketAddress;
import java.nio.charset.Charset;
import java.nio.charset.StandardCharsets;
import java.time.Instant;
import java.time.LocalDateTime;
import java.time.ZoneId;
import java.time.format.DateTimeFormatter;
import java.time.format.DateTimeFormatterBuilder;
import java.time.temporal.ChronoField;
import java.time.temporal.TemporalAccessor;
import java.util.*;

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

    /***
     * 将IP和端口转换成 InetSocketAddress
     * @param connectionStr
     * @param defaultPort
     * @return
     */
    public static List<InetSocketAddress> parseNetworkAddress(String connectionStr, int defaultPort) {
        if (connectionStr == null || connectionStr.isEmpty()) {
            return Collections.emptyList();
        }
        List<InetSocketAddress> result = new ArrayList<>();
        String[] hostPorts = connectionStr.split(",");
        for (String it : hostPorts) {
            String[] hp = it.split(":");
            if (hp.length > 0) {
                String host = hp[0];
                host = host.trim();
                if (!host.isEmpty()) {
                    int port = defaultPort;
                    if (hp.length > 1) {
                        String s = hp[1];
                        s = s.trim();
                        if (!s.isEmpty()) {
                            try {
                                port = Integer.parseInt(s);
                            } catch (NumberFormatException ignored) {
                            }
                        }
                    }
                    result.add(new InetSocketAddress(host, port));
                }
            }
        }
        return result;
    }


    public static Charset getCharset() {
        return StandardCharsets.UTF_8;
    }

    public static boolean checkNonEmpty(byte[] data) {
        return data != null && data.length > 0;
    }

    public static boolean checkNonEmpty(String data) {
        return data != null && data.length() > 0;
    }

    public static boolean checkNonEmpty(List<byte[]> data) {
        return data != null && data.size() > 0;
    }

    public static boolean checkNonEmpty(Map<String, String[]> map) {
        return map != null && map.size() > 0;
    }

    public static byte[][] toArray(List<byte[]> data) {
        if (data != null && !data.isEmpty()) {
            byte[][] result = new byte[data.size()][];
            for (int i = 0; i < data.size(); i++) {
                result[i] = data.get(i);
            }
            return result;
        }
        return null;
    }

}
