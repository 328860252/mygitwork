package tsocket.zby.com.tsocket.bean;

import android.content.Context;
import java.util.ArrayList;
import java.util.List;
import lombok.Data;
import tsocket.zby.com.tsocket.AppConstants;
import tsocket.zby.com.tsocket.connection.ICmdParseInterface;
import tsocket.zby.com.tsocket.connection.IConnectInterface;
import tsocket.zby.com.tsocket.connection.agreement.CmdParseImpl;

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
  private List<TimerBean> mTimerBeanList;
  private int delayNumber;
  private boolean delaySwitch;

  public IConnectInterface connect;
  public ICmdParseInterface parse;

  public void setConnectionInterface(IConnectInterface connectionInterface, Context context) {
    this.connect = connectionInterface;
    if (parse == null) {
      parse = new CmdParseImpl(this, context);
    }
    this.connect.setCmdParse(parse);
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
    if (!connect.isLink()) {
      return false;
    }
    if (connect != null) {
      return connect.writeAgreement(channel);
      //connect.write(CmdPackage.getCmdSuccess());
    }
    return false;
  }

  public boolean writeNoEncrypt(byte[] channel) {
    if (!connect.isLink()) {
      return false;
    }
    if (connect != null) {
      return connect.write(channel);
    }
    return false;
  }

  public List<TimerBean> getTimerBeanList() {
    if (mTimerBeanList==null) {
      mTimerBeanList = new ArrayList<>();
    }
    return mTimerBeanList;
  }

  public void updateTimerBeanList(TimerBean timerBean) {
    for (int i=0; i<mTimerBeanList.size(); i++) {
      if (timerBean.getId()==mTimerBeanList.get(i).getId()) {
        mTimerBeanList.remove(i);
        mTimerBeanList.add(i, timerBean);
        return;
      }
    }
    mTimerBeanList.add(timerBean);
  }

  public void connect() {
    if(connect!=null) {
      connect.connect(mac, "");
    }
  }
}
