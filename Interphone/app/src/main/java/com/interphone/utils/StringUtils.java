package com.interphone.utils;

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;

/**
 * Created by Administrator on 2016/5/25.
 */
public class StringUtils {

    private static SimpleDateFormat mSimpleDateFormat = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");

    public static String value2String(int value) {
        if(value ==0 ) {
            return "OFF";
        } else {
            return ""+ value;
        }
    }

    public static String getTime(byte[] bytes) {
        int year = MyByteUtils.byteToInt(bytes[0]) * 256 + MyByteUtils.byteToInt(bytes[1]);
        //月份从0开始
        int month = MyByteUtils.byteToInt(bytes[2])-1;
        int day = MyByteUtils.byteToInt(bytes[3]);
        int hour = MyByteUtils.byteToInt(bytes[4]);
        int minute = MyByteUtils.byteToInt(bytes[5]);
        int second = MyByteUtils.byteToInt(bytes[6]);
        Calendar calendar = Calendar.getInstance();
        calendar.set(year, month, day, hour, minute, second);
        return mSimpleDateFormat.format(calendar.getTime());
    }

    public static String timeFormat(String longStr) {
        String time = "";
        try {
            time = mSimpleDateFormat.format(new Date(Long.parseLong(longStr)));
        } catch (Exception e) {
            e.printStackTrace();
        }
        return time;
    }

    public static String getTimeString() {
        return mSimpleDateFormat.format(new Date());
    }
}
