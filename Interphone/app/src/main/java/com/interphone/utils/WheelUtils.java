package com.interphone.utils;

import android.content.Context;
import com.example.administrator.interphone.R;
import com.interphone.bean.DefaultData;
import com.interphone.view.wheel.WheelService;
import com.interphone.view.wheel.WheelView;

/**
 * Created by Administrator on 2016/5/22.
 */
public class WheelUtils {

    /**
     * 模拟频道发送频率
     */
    public final static int  type_analogToneSend = 1;
    /**
     * 模拟频道接收频率
     */
    public final static int  type_analogToneReceive = 2;
    /**
     * 数字频道色码
     */
    public final static int  type_numberToneColor = 3;
    /**
     * TOT时间
     */
    public final static int  type_protertyTot = 4;

    private final int wheelTextSize = 8;
    private static int txtSize =85;

    public static WheelView getWheeilView(Context context ,int type, int selectPosition) {
        WheelView wheelView = null;
        switch(type) {
            case type_analogToneReceive:
            case type_analogToneSend:
                wheelView = WheelService.getBinString(context, DefaultData.getArrayAnalogToneSned(context),  selectPosition, "", txtSize);
                break;
            case type_numberToneColor:
                wheelView = WheelService.getBinString(context, DefaultData.getNumberToneColor(),  selectPosition, "", txtSize);
                break;
            case type_protertyTot:
                wheelView = WheelService.getBinString(context, DefaultData.getPropertyTot(),  selectPosition, "", txtSize);
                break;
        }
        return wheelView;
    }
}
