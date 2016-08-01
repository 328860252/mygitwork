package com.interphone.utils;

import java.text.SimpleDateFormat;
import java.util.Date;

/**
 * Created by Administrator on 2016/5/25.
 */
public class StringUtils {

    private static SimpleDateFormat mSimpleDateFormat = new SimpleDateFormat("MM-dd HH:mm:ss");

    public static String value2String(int value) {
        if(value ==0 ) {
            return "OFF";
        } else {
            return ""+ value;
        }
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
}
