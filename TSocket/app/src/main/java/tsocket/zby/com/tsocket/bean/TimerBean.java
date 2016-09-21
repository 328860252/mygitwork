package tsocket.zby.com.tsocket.bean;

import java.util.Calendar;
import lombok.Data;

/**
 * Created by Administrator on 2016/9/19.
 */
@Data
public class TimerBean implements Cloneable{

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
}
