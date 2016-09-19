package tsocket.zby.com.tsocket.bean;

import lombok.Data;

/**
 * Created by Administrator on 2016/9/19.
 */
@Data
public class TimerBean {

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
}
