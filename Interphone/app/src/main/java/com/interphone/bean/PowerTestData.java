package com.interphone.bean;

import lombok.Data;

/**
 * Created by Administrator on 2016/5/25.
 * 功率测试
 */
@Data
public class PowerTestData {

    private int powerLow;
    private int powerMiddle;
    private int powerHigh;
    private int powerId;

    /**
     * powerId 1-9 ufh  10-18vhf
     * @return
     */
    public boolean isUhf() {
        return getPowerId()<10;
    }


}
