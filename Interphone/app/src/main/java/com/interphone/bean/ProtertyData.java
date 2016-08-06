package com.interphone.bean;

import lombok.Data;

/**
 * Created by Administrator on 2016/5/13.
 */
@Data
public class ProtertyData {

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
     * 活动信道 1-16
     */
    private int activityChannelId;

    public String deviceMode ="";

    /**
     * 设备id
     */
    public String userId ="";

    /**
     * 1 2 3(默认）
     */
    public int status = 0x0C;

    /**
     * 0:VHF 显示136-174MHz  1:UHF 显示400-470MHz
     */
    private int HFvalue;

    /**
     * 序列号
     */
    private String serialNumber;


    public int getActivityChannelId() {
        if (activityChannelId <1 || activityChannelId > 16) return 1;
        return activityChannelId;
    }

    public void setActivityChannelId(int activityChannelId) {
        this.activityChannelId = activityChannelId;
    }

    public String getHFValueString() {
        return isVHF() ?"136-174MHZ":"400-470MHZ";
    }

    public boolean isVHF() {
        return HFvalue==0;
    }
}
