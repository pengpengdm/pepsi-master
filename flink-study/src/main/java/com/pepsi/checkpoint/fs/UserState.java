package com.pepsi.checkpoint.fs;

import lombok.Data;

import java.io.Serializable;

/**
 * Created with IntelliJ IDEA.
 * User: pepsi
 * Date: 2019-06-01 22:31
 * Description: No Description
 */
@Data
public class UserState implements Serializable {
    private int count;

    public UserState(int count){
        this.count = count;
    }
}
