package com.interphone;

/**
 * Created by Administrator on 2016/5/13.
 */
public class AppConstants {

    //收到数据是否需要回传ack
    public static boolean isWriteACK = true;

    public final static String charSet = "utf-8";

    public final static boolean isDemo = false;

    /**
     * 功率调频密码
     */
    public final static String Power_password = "695875";

    public static int sms_length = 118;//短信字节长度

    /**
     * 超级密码
     */
    public final static String SUPPER_PASSWORD = "353605";

    public final static int rateMULTPLE = 100000;
    //发送延迟时间，单位毫秒
    public static long wait = 500;
}
