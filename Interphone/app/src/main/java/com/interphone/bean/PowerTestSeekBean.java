package com.interphone.bean;

import lombok.Data;

/**
 * Created by Administrator on 2016/5/25.
 * 功率测试 listbean
 */
@Data
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

}
