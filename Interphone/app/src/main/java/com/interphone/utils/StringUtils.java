package com.interphone.utils;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import lombok.Data;

/**
 * Created by Administrator on 2016/5/25.
 */
public class StringUtils {

  /**
   * 对外显示的格式
   */
  private static SimpleDateFormat mSimpleDateFormat = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");

    /**
     * 格式化bcd码用的
     */
    private static SimpleDateFormat mSimpleDateFormat2 = new SimpleDateFormat("yyyyMMddHHmmss");

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

    public static byte[] time2BcdByte(String time) {
        Date date;
        try {
            date = mSimpleDateFormat.parse(time);
        } catch (ParseException e) {
            e.printStackTrace();
            date = new Date();
        }
        return  BcdUtils.str2Bcd(mSimpleDateFormat2.format(date));
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

    /**
     * 获得yyyy-MM-dd HH:mm:ss
     * @return
     */
    public static String getTimeString() {
        return mSimpleDateFormat.format(new Date());
    }

    public static String getTimeBcd2String(byte[] buffer) {
       String str = MyHexUtils.buffer2String(buffer).replace(" ", "");
        Date date = null;
        try {
            date = mSimpleDateFormat2.parse(str);
        } catch (ParseException e) {
            e.printStackTrace();
            date = new Date();
        }
        return mSimpleDateFormat.format(date);
    }
}
