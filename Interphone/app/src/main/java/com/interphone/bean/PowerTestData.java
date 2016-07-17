package com.interphone.bean;

import lombok.Data;

/**
 * Created by Administrator on 2016/5/25.
 * 功率测试
 */
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

    public int getPowerLow() {
        return powerLow;
    }

    public void setPowerLow(int powerLow) {
        this.powerLow = powerLow;
    }

    public int getPowerMiddle() {
        return powerMiddle;
    }

    public void setPowerMiddle(int powerMiddle) {
        this.powerMiddle = powerMiddle;
    }

    public int getPowerHigh() {
        return powerHigh;
    }

    public void setPowerHigh(int powerHigh) {
        this.powerHigh = powerHigh;
    }

    public int getPowerId() {
        return powerId;
    }

    public void setPowerId(int powerId) {
        this.powerId = powerId;
    }
}
