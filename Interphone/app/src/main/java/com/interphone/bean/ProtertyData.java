package com.interphone.bean;

import lombok.Data;

/**
 * Created by Administrator on 2016/5/13.
 */
public class ProtertyData {

    /**
     * 0:VHF 显示136-174MHz  1:UHF 显示400-470MHz
     */
    private int HFvalue;

    /**
     * 30s-180s
     */
    private int totTime;

    /**
     * 0-5
     * 0:OFF , 1 2 3 4 5
     */
    private int vox;

    /**
     * 6个字节
     */
    private String pttid;

    /**
     * 活动信道 1-16
     */
    private int activityChannelId;

    public int getHFvalue() {
        return HFvalue;
    }

    public void setHFvalue(int HFvalue) {
        this.HFvalue = HFvalue;
    }

    public int getTotTime() {
        return totTime;
    }

    public void setTotTime(int totTime) {
        this.totTime = totTime;
    }

    public int getVox() {
        return vox;
    }

    public void setVox(int vox) {
        this.vox = vox;
    }

    public String getPttid() {
        return pttid;
    }

    public void setPttid(String pttid) {
        this.pttid = pttid;
    }

    public int getActivityChannelId() {
        if (activityChannelId ==0) return 1;
        return activityChannelId;
    }

    public void setActivityChannelId(int activityChannelId) {
        this.activityChannelId = activityChannelId;
    }
}
