package tsocket.zby.com.tsocket.bean;

import android.content.Context;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;
import lombok.Data;
import tsocket.zby.com.tsocket.AppConstants;
import tsocket.zby.com.tsocket.connection.ICmdParseInterface;
import tsocket.zby.com.tsocket.connection.IConnectInterface;
import tsocket.zby.com.tsocket.connection.agreement.CmdEncrypt;
import tsocket.zby.com.tsocket.connection.agreement.CmdParseImpl;
import tsocket.zby.com.tsocket.connection.agreement.CmdProcess;
import tsocket.zby.com.tsocket.utils.LogUtils;
import tsocket.zby.com.tsocket.utils.MyHexUtils;

/**
 * Created by zhuj on 2016/9/19 14:14.
 */
@Data
public class DeviceBean {

    private String mac;

    private String name;
    private boolean onOff;
    private boolean recycle;
    private boolean timerEnable;
    private List<TimerBean> mTimerBeanList = new ArrayList<>();
    ;
    private boolean delaySwitch;

    private int downCountSecond; //单位秒
    private boolean isBonded;

    public IConnectInterface connect;
    public ICmdParseInterface parse;
    private CmdProcess proccess;

    public void setConnectionInterface(IConnectInterface connectionInterface, Context context) {
        this.connect = connectionInterface;
        if (parse == null) {
            parse = new CmdParseImpl(this, context);
        }
        if (proccess == null) {
            proccess = new CmdProcess(parse);
        }
        if (connect != null) {
            this.connect.setCmdParse(parse);
        }
    }

    public boolean isLink() {
        if (AppConstants.isDemo) {
            return true;
        }
        if (connect == null) return false;
        return connect.isLink(mac);
    }

    public void stopConnect() {
        if (connect != null) {
            connect.stopConncet();
        }
    }

    public boolean write(byte[] channel) {
        if (AppConstants.isDemo) {
            LogUtils.writeLogToFile("cmd_log",
                    MyHexUtils.buffer2String(CmdEncrypt.sendMessage(channel)));
            return true;
        }
        if (connect == null || !connect.isLink()) {
            return false;
        }
        if (connect != null) {
            return connect.writeAgreement(channel);
            //connect.write(CmdPackage.getCmdSuccess());
        }
        return false;
    }

    public boolean writeNoEncrypt(byte[] channel) {
        if (AppConstants.isDemo) {
            LogUtils.writeLogToFile("cmd_log", MyHexUtils.buffer2String(channel));
            return true;
        }
        if (connect == null || !connect.isLink()) {
            return false;
        }
        if (connect != null) {
            return connect.write(channel);
        }
        return false;
    }

    public List<TimerBean> getTimerBeanList() {
        if (mTimerBeanList == null) {
            mTimerBeanList = new ArrayList<>();
        }
        return mTimerBeanList;
    }

    public void updateTimerBeanList(TimerBean timerBean) {
        for (int i = 0; i < mTimerBeanList.size(); i++) {
            if (timerBean.getId() == mTimerBeanList.get(i).getId()) {
                mTimerBeanList.remove(i);
                mTimerBeanList.add(i, timerBean);
                return;
            }
        }
        mTimerBeanList.add(timerBean);
    }

    public void connect() {
        if (connect != null) {
            connect.connect(mac, "");
        }
    }

    public int getNewTimerId() {
        Set<Integer> listNumber = new HashSet<>();
        for (TimerBean tben : mTimerBeanList) {
            listNumber.add(tben.getId());
        }
        int id = 1;
        while (listNumber.contains(id)) {
            id++;
        }
        return id;
    }

    public String getDownCountString() {
        downCountSecond--;
        if (downCountSecond > 60) {
            return downCountSecond / 60 + "min";
        } else if (downCountSecond > 0) {
            return downCountSecond % 60 + "sec";
        } else {
            return "";
        }
    }

    /**
     * 校验是否冲突
     */
    public boolean verifyConflict(TimerBean timerBean) {
        for (TimerBean tb : getTimerBeanList()) {
            if (tb.getId() == timerBean.getId()) {//遇到修改的定时就是自己本身，不做校验
                continue;
            }
            if ((tb.getWeekValue() & timerBean.getWeekValue()) > 0) { //表示有相同的星期位 同时为1
                //时间比较，
                int startCompareTo = tb.getStartString().compareTo(timerBean.getStartString());
                int endCompareTo = tb.getEndString().compareTo(timerBean.getEndString());
                int start2endCompareTo = tb.getStartString().compareTo(timerBean.getEndString());
                int end2startCompareTo = tb.getEndString().compareTo(timerBean.getStartString());
                if (startCompareTo == 0 || endCompareTo == 0) { //有同样的开始时间 或 同样的结束时间
                    return false;
                } else if (startCompareTo > 0
                        && start2endCompareTo < 0) { //tb开始时间比timerBean大，但比timerBean的结束时间 小
                    return false;
                } else if (startCompareTo < 0
                        && end2startCompareTo > 0) {//tb开始时间比timerBean开始小， 但比timerBean结束时间大
                    return false;
                }
            }
        }
        return true;
    }
}
