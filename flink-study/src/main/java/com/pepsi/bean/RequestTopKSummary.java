package com.pepsi.bean;

import java.io.Serializable;
import java.util.Set;

/**
 * Created with IntelliJ IDEA.
 * User: pepsi
 * Date: 2019-09-24 00:03
 * Description: No Description
 */
public class RequestTopKSummary implements Serializable {
    private static final long serialVersionUID = 1L;

    private String type;
    private Set<String> keys;

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    public Set<String> getKeys() {
        return keys;
    }

    public void setKeys(Set<String> keys) {
        this.keys = keys;
    }
}
