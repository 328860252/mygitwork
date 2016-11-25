package com.interphone.bean;

import android.text.TextUtils;
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

    /**
     * 机器型号
     */
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
    private String serialNumber="";

    /**
     * 扫描频率 BCD码，16进制的10位数  比如数字12 0x12 而不是0x0b
     * "VHF:136-174MHz
     * 最多到小数点后4位， 也就是固定的 xxx.xxxx ，
     * 必须能被5整除 或 6.25整除
     */
    private String scanRate;

    /**
     * 写频密码
     * 353605超级密码 ， 全FFFFFF为空密码
     */
    public String password="";

    public String getScanRate() {
        if (TextUtils.isEmpty(scanRate)) return "";
        if (scanRate.equals("00000000")) return "";

        double d = Integer.parseInt(scanRate) / 10000.0d;
        return ""+d;
    }

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
