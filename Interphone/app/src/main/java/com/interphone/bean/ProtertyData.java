package com.interphone.bean;

import lombok.Data;

/**
 * Created by Administrator on 2016/5/13.
 */
@Data
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

}
