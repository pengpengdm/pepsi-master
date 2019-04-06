package com.pepsi.interview.thread.ConsumerProducer;

import lombok.Data;
import lombok.ToString;

/**
 * @author pepsi
 * @version 1.0
 * @date 2019/01/22
 * describe:
 */
@Data
@ToString
public class IPhone {
    private int id;
    private String version;

    public IPhone(int id,String version){
        this.id = id;
        this.version = version;
    }
}
