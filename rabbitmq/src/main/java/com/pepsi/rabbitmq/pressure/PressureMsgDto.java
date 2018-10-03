package com.pepsi.rabbitmq.pressure;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.databind.annotation.JsonDeserialize;

import java.io.Serializable;

/**
 * @author pepsi
 * @version 1.0
 * @date 2018/09/25
 * describe:
 */
@JsonDeserialize
@JsonIgnoreProperties(ignoreUnknown = true)
public class PressureMsgDto implements Serializable{
    private static final long serialVersionUID = -773494954886553493L;
    private String name;
    private String vin;
    public PressureMsgDto(){}

    public PressureMsgDto(String name, String vin) {
        this.name = name;
        this.vin = vin;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getVin() {
        return vin;
    }

    public void setVin(String vin) {
        this.vin = vin;
    }

    @Override
    public String toString() {
        return "{name="+name+",vin="+vin+"}";
    }
}
