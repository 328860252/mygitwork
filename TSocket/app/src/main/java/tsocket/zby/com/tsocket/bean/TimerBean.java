package tsocket.zby.com.tsocket.bean;

import android.content.Context;
import java.io.Serializable;
import java.util.Calendar;
import lombok.Data;
import tsocket.zby.com.tsocket.R;

/**
 * Created by Administrator on 2016/9/19.
 */
@Data
public class TimerBean implements Serializable, Cloneable{

    private int id;
    private int startHour;
    private int startMinute;
    private int startSecond;

    private int endHour;
    private int endMinute;
    private int endSecond;

    private int openHour;
    private int openMinute;
    private int openSecond;

    private int closeHour;
    private int closeMinute;
    private int closeSecond;

    private int weekValue;
    private boolean isRecycle;  //循环
    private boolean isEnable; //有效
    private boolean isDelete; //是否删除

    public static TimerBean getNewTimerBean() {
        Calendar calendar = Calendar.getInstance();
        int hour = calendar.get(Calendar.HOUR_OF_DAY);
        int minute = calendar.get(Calendar.MINUTE);
        int secnod = calendar.get(Calendar.SECOND);
        TimerBean timerBean = new TimerBean();
        timerBean.setStartHour(hour);
        timerBean.setStartMinute(minute);
        timerBean.setStartSecond(secnod);
        timerBean.setEndHour(hour);
        timerBean.setEndMinute(minute);
        timerBean.setEndSecond(secnod);

        timerBean.setOpenHour(0);
        timerBean.setOpenMinute(5);
        timerBean.setOpenSecond(0);

        timerBean.setCloseHour(0);
        timerBean.setCloseMinute(5);
        timerBean.setCloseSecond(0);

        timerBean.setRecycle(true);
        timerBean.setWeekValue(127);
        return timerBean;
    }

    public String getStartString() {
       return String.format("%02d:%02d:%02d",getStartHour(),getStartMinute(),getStartSecond());
    }

    public String getEndString() {
        return String.format("%02d:%02d:%02d",getEndHour(),getEndMinute(),getEndSecond());
    }
    public String getOpenString() {
        return String.format("%02d:%02d",getOpenMinute(),getOpenSecond());
    }
    public String getCloseString() {
        return String.format("%02d:%02d",getCloseMinute(),getCloseSecond());
    }

    public String getWeekString(Context context) {
        StringBuffer sb = new StringBuffer();
        if (weekValue%2 == 1)       sb.append(context.getString(R.string.text_week_7) + " ");
        if (weekValue %4 /2 == 1)   sb.append(context.getString(R.string.text_week_1) + " ");
        if (weekValue %8 /4 == 1)   sb.append(context.getString(R.string.text_week_2) + " ");
        if (weekValue %16 /8 == 1)  sb.append(context.getString(R.string.text_week_3) + " ");
        if (weekValue %32 /16 == 1) sb.append(context.getString(R.string.text_week_4) + " ");
        if (weekValue %64 /32 == 1) sb.append(context.getString(R.string.text_week_5) + " ");
        if (weekValue /64 == 1)     sb.append(context.getString(R.string.text_week_6) + " ");
        return sb.toString();
    }

    @Override public Object clone() throws CloneNotSupportedException {
        return super.clone();
    }

    public int getStatus() {
        int status = 0;
        if (isDelete()) status += 1 ; //删除
        if (isEnable()) status += 1 << 1; //有效位，1
        status += 1 << 2; //循环开关位，暂时固定为1
        //if (isDelete()) status += 1 ^4; //倒计时定时1  普通0
        if (isRecycle()) status += 1 <<4;  //循环开关
        return status;
    }

    public void setStatus(int status) {
        isDelete = status % 2==1;
        isEnable = status>>1 %2 ==1;
        isRecycle = status>>4 %2 ==1;
    }
}
