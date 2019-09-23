package com.pepsi.util;

import org.apache.flink.api.java.tuple.Tuple2;

import java.io.BufferedReader;
import java.io.DataOutputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;
import java.net.URLConnection;
import java.util.Map;

/**
 * Created with IntelliJ IDEA.
 * User: pepsi
 * Date: 2019-09-23 22:25
 * Description: No Description
 */
public class SimpleHttpClient {
    public static Tuple2<Integer, String> post(String url, byte[] body, Map<String, String> headers, long timeout, int maxResponse) throws Exception {
        return request("POST", url, body, headers, timeout, maxResponse);
    }

    public static Tuple2<Integer, String> get(String url, Map<String, String> headers, long timeout, int maxResponse) throws Exception {
        return request("GET", url, null, headers, timeout, maxResponse);
    }

    public static Tuple2<Integer, String> request(String method, String url, byte[] body, Map<String, String> headers, long timeout, int maxResponse) throws Exception {
        URL obj = new URL(url);

        int code = 0;
        String response = "";

        HttpURLConnection httpConn = null;
        try {
            URLConnection conn = obj.openConnection();
            if (conn instanceof HttpURLConnection) {
                httpConn = ((HttpURLConnection) conn);

                httpConn.setConnectTimeout((int) timeout);
                httpConn.setReadTimeout((int) timeout);
                httpConn.setRequestMethod(method);

                if (headers != null && !headers.isEmpty()) {
                    for (Map.Entry<String, String> it : headers.entrySet()) {
                        httpConn.setRequestProperty(it.getKey(), it.getValue());
                    }
                }

                if (body != null && body.length > 0) {
                    httpConn.setDoOutput(true);
                    try (DataOutputStream wr = new DataOutputStream(httpConn.getOutputStream())) {
                        wr.write(body, 0, body.length);
                        wr.flush();
                    }
                }

                code = httpConn.getResponseCode();

                String line = null;
                try (BufferedReader in = new BufferedReader(new InputStreamReader(httpConn.getInputStream()))) {
                    StringBuilder sb = new StringBuilder();
                    while ((line = in.readLine()) != null) {
                        sb.append(line);
                        if (sb.length() > maxResponse) {
                            break;
                        }
                    }
                    response = sb.toString();
                }
                return Tuple2.of(code, response);
            }
        } finally {
            if (httpConn != null) {
                httpConn.disconnect();
            }
        }

        return Tuple2.of(code, response);
    }
}
