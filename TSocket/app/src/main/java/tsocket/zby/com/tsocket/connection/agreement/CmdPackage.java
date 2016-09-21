package tsocket.zby.com.tsocket.connection.agreement;

import java.io.UnsupportedEncodingException;
import java.util.Calendar;
import tsocket.zby.com.tsocket.AppConstants;
import tsocket.zby.com.tsocket.bean.TimerBean;

/**
 * 协议
 */
public class CmdPackage {

    private final static int TYPE_SWITCH = 0xA1;
    private final static int TYPE_TIMER_SET = 0xA2;
    private final static int TYPE_TIMER_GET = 0xA3;
    private final static int TYPE_TIMER_REFRESH = 0xA4;
    private final static int TYPE_TIMER_DELAY = 0xA7;
    private final static int TYPE_TIMER_ENABLE = 0xA8;

    private final static int TYPE_SWITCH_ON = 0xA5;
    private final static int TYPE_SWITCH_OFF = 0x5A;

    public static byte[] setSwitch(boolean onOff) {
        byte[] buff = new byte[4];
        buff[0] = (byte) TYPE_SWITCH;
        buff[1] = (byte) (onOff ? TYPE_SWITCH_ON : TYPE_SWITCH_OFF);
        return buff;
    }

    public static byte[] setTimer(TimerBean timerBean) {
        byte[] buff = new byte[16];
        buff[0] = (byte)  TYPE_TIMER_SET;
        buff[1] = (byte)  timerBean.getId();
        buff[2] = (byte)  timerBean.getStartHour();
        buff[3] = (byte)  timerBean.getStartMinute();
        buff[4] = (byte)  timerBean.getStartSecond();
        buff[5] = (byte)  timerBean.getEndHour();
        buff[6] = (byte)  timerBean.getEndMinute();
        buff[7] = (byte)  timerBean.getEndSecond();
        buff[8] = (byte)  timerBean.getOpenHour();
        buff[9] = (byte)  timerBean.getOpenMinute();
        buff[10] = (byte) timerBean.getOpenSecond();
        buff[11] = (byte) timerBean.getCloseHour();
        buff[12] = (byte) timerBean.getCloseMinute();
        buff[13] = (byte) timerBean.getCloseSecond();
        buff[14] = (byte) timerBean.getWeekValue();
        buff[15] = (byte) timerBean.getWeekValue();
        return buff;
    }

    public static byte[] getTimer() {
        byte[] buff = new byte[3];
        buff[0] = (byte) TYPE_TIMER_GET;
        return buff;
    }

    public static byte[] setTimerCheck() {
        Calendar calendar = Calendar.getInstance();
        byte[] buff = new byte[9];
        buff[0] = (byte) TYPE_TIMER_REFRESH;
        buff[1] = (byte) (calendar.get(Calendar.YEAR) / 256);
        buff[2] = (byte) (calendar.get(Calendar.YEAR) % 256);
        buff[3] = (byte) (calendar.get(Calendar.MONTH)+1); //月份从0 开始
        int week = calendar.get(Calendar.DAY_OF_WEEK) -1; //星期日是1， 星期6是7
        if (week ==0 ) week = 7;
        buff[4] = (byte) (week);
        buff[5] = (byte) (calendar.get(Calendar.DAY_OF_MONTH));
        buff[6] = (byte) (calendar.get(Calendar.HOUR_OF_DAY));
        buff[7] = (byte) (calendar.get(Calendar.MINUTE));
        buff[8] = (byte) (calendar.get(Calendar.SECOND));
        return buff;
    }

    ;

    public static byte[] setTimerDelay(boolean onOff, int hour, int minute, int second) {
        byte[] buff = new byte[4];
        buff[0] = (byte) TYPE_TIMER_GET;
        buff[1] = (byte) (onOff ? 0x01 : 0x00);
        return buff;
    }

    public static byte[] setTimerEnable(boolean enable) {
        byte[] buff = new byte[4];
        buff[0] = (byte) TYPE_TIMER_ENABLE;
        buff[1] = (byte) (enable ? 0x01 : 0x00);
        return buff;
    }

    public static byte[] setName(String name) {
        byte[] buff = null;
        try {
            buff = ("WR+NAME+" + name + "\"").getBytes(AppConstants.charset);
        } catch (UnsupportedEncodingException e) {
            e.printStackTrace();
        }
        return buff;
    }

    public static byte[] setPassword(String password) {
        byte[] buff = null;
        try {
            buff = ("WR+PINCODE+" + password + "\"").getBytes(AppConstants.charset);
        } catch (UnsupportedEncodingException e) {
            e.printStackTrace();
        }
        return buff;
    }

    public static byte[] setReboot() {
        byte[] buff = null;
        try {
            buff = ("WR+RESTART+"  + "\"").getBytes(AppConstants.charset);
        } catch (UnsupportedEncodingException e) {
            e.printStackTrace();
        }
        return buff;
    }


}
