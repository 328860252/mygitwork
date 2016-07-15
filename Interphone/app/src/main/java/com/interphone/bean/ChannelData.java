package com.interphone.bean;

import android.content.Context;
import com.interphone.utils.WheelUtils;
import lombok.Data;

/**
 * Created by Administrator on 2016/5/12.
 */
@Data
public class ChannelData {

    private static final String OFF = "OFF";

    private int channelId;

    /**
     * 1数字信道 0模拟
     */
    private int channelType;

    /**
     * 接收频率 BCD码，16进制的10位数  比如数字12 0x12 而不是0x0b
     * "VHF:136-174MHz
     * 最多到小数点后4位， 也就是固定的 xxx.xxxx ，
     * 必须能被5整除 或 6.25整除
     */
    private double rateReceive;

    /**
     * 发射频率 BCD码
     * UHF:400-470MHz"
     */
    private double rateSend;

    /**
     * 模拟信道 接受亚音
     *  0：OFF   ， 1-38：CTCSS， 39-121：CDCSS
     */
    private int analogToneReceive;

    /**
     * 模拟信道发射亚音
     * 0：OFF   ， 1-38：CTCSS， 39-121：CDCSS
     */
    private int analogToneSend;

    /**
     * 模拟信道宽窄带
     * 0 ：12.5KHZ    1:25KHZ
     */
    private int analogToneBand;

    /**
     * 数字信道 联系人索引
     */
    private int numberToneLinkmen;

    /**
     * 数字信道 时隙
     * 0:1 ， 1:显示2
     */
    private int numberToneSlot;

    /**
     * 梳子信道 色码
     * 0-15
     */
    private int numberToneColor;

    /**
     * 1低功率 2中功率 3高功率；
     */
    private int power;

    public boolean isNumberTone() {
        return channelType == 1;
    }

    public String getAnalogToneReceive2Str(Context context) {
        if(analogToneReceive==0) {
            return OFF;
        } else {
            return WheelUtils.getArrayAnalogToneSned(context)[analogToneReceive];
        }
    }

    public String getAnalogToneSend2Str(Context context) {
        if(analogToneSend==0) {
            return OFF;
        } else {
            return WheelUtils.getArrayAnalogToneSned(context)[analogToneSend];
        }
    }

    public String getRateSend2Str() {
        return rate2String(rateSend);
    }

    public String getRateReceive2Str() {
        return rate2String(rateReceive);
    }


    private String rate2String(double rate) {
        if(rate>= 136 && rate <=174) {
            return "VHF";
        } else if(rate>=400 && rate<=470) {
            return "UHF";
        }
        return null;
    }
}
