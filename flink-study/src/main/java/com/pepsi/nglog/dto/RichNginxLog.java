package com.pepsi.nglog.dto;

import lombok.Data;

import java.io.Serializable;

/**
 * Created with IntelliJ IDEA.
 * User: pepsi
 * Date: 2019-06-29 22:32
 * Description: No Description
 */
@Data
public class RichNginxLog  implements Serializable {

    /**
     * Nginx access log 的配置如下
     * <p>
     * log_format main   '{"@timestamp":"$time_iso8601",'
     * '"@source":"$server_addr",'
     * '"hostname":"$hostname",'
     * '"ip":"$http_x_forwarded_for",'
     * '"client":"$remote_addr",'
     * '"request_method":"$request_method",'
     * '"scheme":"$scheme",'
     * '"domain":"$server_name",'
     * '"referer":"$http_referer",'
     * '"request":"$request_uri",'
     * '"args":"$args",'
     * '"size":$body_bytes_sent,'
     * '"status": $status,'
     * '"responsetime":$request_time,'
     * '"upstreamtime":"$upstream_response_time",'
     * '"upstreamaddr":"$upstream_addr",'
     * '"http_user_agent":"$http_user_agent",'
     * '"https":"$https"'
     * '}';
     * <p>
     * <p>
     * *g 例子
     * <p>
     * {
     * "referer": "-",
     * "request": "/hotel-store-management/roomReservation/getRoomStatusList?hotelId=42460",
     * "upstreamaddr": "10.200.25.62:5000",
     * "@source": "10.200.24.169",
     * "scheme": "http",
     * "ip": "-",
     * "request_method": "GET",
     * "http_user_agent": "Apache-HttpClient/4.5.2 (Java/1.8.0_131)",
     * "args": "hotelId=42460",
     * "path": "/var/log/nginx/ali.access.log",
     * "hostname": "prod-infra-nginx-aliapi-3.vm.ahotels.tech",
     * "@timestamp": "2019-06-05T10:41:33.000Z",
     * "size": 683,
     * "domain": "ali.ahotels.tech",
     * "@version": "1",
     * "host": "prod-infra-nginx-aliapi-3.vm.ahotels.tech",
     * "client": "10.200.23.136",
     * "responsetime": 0.011,
     * "https": "",
     * "upstreamtime": "0.011",
     * "status": 200
     * }
     */


    private static final long serialVersionUID = 1L;

    private long timestamp;
    private String domain;
    private String path;
    private int status;
    private long duration;
    private String upstream;


    public long getTimestamp() {
        return timestamp;
    }


}
