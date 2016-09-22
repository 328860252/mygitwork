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
    private boolean isRecycle;
    private boolean timerSwitch;

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
        if (weekValue %16 /2 == 1)  sb.append(context.getString(R.string.text_week_3) + " ");
        if (weekValue %32 /16 == 1) sb.append(context.getString(R.string.text_week_4) + " ");
        if (weekValue %64 /32 == 1) sb.append(context.getString(R.string.text_week_5) + " ");
        if (weekValue /64 == 1)     sb.append(context.getString(R.string.text_week_6) + " ");
        return sb.toString();
    }

    @Override public Object clone() throws CloneNotSupportedException {
        return super.clone();
    }
}
