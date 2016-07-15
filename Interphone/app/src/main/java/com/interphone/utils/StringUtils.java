package com.interphone.utils;

/**
 * Created by Administrator on 2016/5/25.
 */
public class StringUtils {

    public static String value2String(int value) {
        if(value ==0 ) {
            return "OFF";
        } else {
            return ""+ value;
        }
    }
}
