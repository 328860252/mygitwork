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
     * 6个字节
     */
    private String pttid;

    /**
     * 活动信道 1-16
     */
    private int activityChannelId;

    public int getActivityChannelId() {
        if (activityChannelId <1 || activityChannelId > 16) return 1;
        return activityChannelId;
    }

    public void setActivityChannelId(int activityChannelId) {
        this.activityChannelId = activityChannelId;
    }

}
