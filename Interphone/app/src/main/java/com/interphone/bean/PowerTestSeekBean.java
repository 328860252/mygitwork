package com.interphone.bean;

import lombok.Data;

/**
 * Created by Administrator on 2016/5/25.
 * 功率测试 listbean
 */
public class PowerTestSeekBean {

    private int id;
    private String name;
    private int value;

    private PowerTestSeekBean() {}

    public PowerTestSeekBean(int id, String name ,int value) {
        this.id =id;
        this.name = name;
        this.value = value;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public int getValue() {
        return value;
    }

    public void setValue(int value) {
        this.value = value;
    }
}
