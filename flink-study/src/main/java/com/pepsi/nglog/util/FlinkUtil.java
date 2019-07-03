package com.pepsi.nglog.util;

import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.net.InetSocketAddress;
import java.nio.charset.Charset;
import java.nio.charset.StandardCharsets;
import java.time.Instant;
import java.time.ZoneId;
import java.time.format.DateTimeFormatter;
import java.time.format.DateTimeFormatterBuilder;
import java.time.temporal.ChronoField;
import java.time.temporal.TemporalAccessor;
import java.util.*;

/**
 * Created with IntelliJ IDEA.
 * User: pepsi
 * Date: 2019-06-29 22:40
 * Description: No Description
 */
public final  class FlinkUtil {

    private static final Base64.Encoder codec = Base64.getEncoder();


    private static final String[] paths = new String[]{"/pepsi/demo01","/pepsi/demo02","/pepsi/demo03","/pepsi/demo04","/pepsi/demo03"};

    // 精确到毫秒, e.g. "2019-06-07T07:03:00.445Z"
    public static final DateTimeFormatter ISO_INSTANT_MS = new DateTimeFormatterBuilder().parseCaseInsensitive().appendInstant(3).toFormatter();

    private FlinkUtil() {
    }

    public static ClassLoader getDefaultClassLoader() {
        ClassLoader cl = null;
        try {
            cl = Thread.currentThread().getContextClassLoader();
        } catch (Throwable ignored) {
            // Cannot get thread context ClassLoader
        }
        if (cl == null) {
            // Use own class loader.
            cl = FlinkUtil.class.getClassLoader();
            if (cl == null) {
                // May be the bootstrap ClassLoader
                try {
                    cl = ClassLoader.getSystemClassLoader();
                } catch (Throwable ignored) {
                    // Cannot access system ClassLoader - oh well, maybe the caller can live with null...
                }
            }
        }
        return cl;
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

    public static byte[] toBytes(String value) {
        if (value == null || value.isEmpty()) {
            return null;
        }
        return value.getBytes(StandardCharsets.UTF_8);
    }

    public static Properties toProperties(String data) throws IOException {
        Properties props = new Properties();
        if (data != null && !data.isEmpty()) {
            props.load(new ByteArrayInputStream(data.getBytes(StandardCharsets.UTF_8)));
        }
        return props;
    }

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

    public static Properties loadProperties(String prefix, String postfix, String defaultName, String[] profiles) {
        ClassLoader cl = getDefaultClassLoader();
        if (cl == null) {
            return null;
        }

        List<String> profileList = profiles != null && profiles.length > 0 ? new ArrayList<>(Arrays.asList(profiles)) : new ArrayList<>();
        profileList.add("");

        for (String profile : profileList) {
            String cfgName = !profile.isEmpty() ? prefix + profile + postfix : defaultName;
            try (InputStream ins = cl.getResourceAsStream(cfgName)) {
                if (ins != null) {
                    Properties props = new Properties();
                    props.load(ins);
                    return props;
                }
            } catch (Exception ignored) {
            }
        }
        return null;
    }

    public static void encodeSpace(StringBuilder out, String s, int max) {
        if (s != null && !s.isEmpty()) {
            max = Math.min(max, s.length());
            for (int i = 0; i < max; i++) {
                char ch = s.charAt(i);
                switch (ch) {
                    case ' ':
                    case '\t':
                    case '\r':
                    case '\n':
                        out.append("%20");
                        break;
                    default:
                        out.append(ch);
                        break;
                }
            }
        }
    }

    public static String base64Encode(byte[] value) {
        // thread safe
        return codec.encodeToString(value);
    }

    public static boolean isHttpError(int code) {
        return code >= 400;
    }

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
}
