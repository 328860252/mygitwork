package tsocket.zby.com.tsocket.bean;

import android.content.Context;
import lombok.Data;
import tsocket.zby.com.tsocket.AppConstants;
import tsocket.zby.com.tsocket.connection.ICmdParseInterface;
import tsocket.zby.com.tsocket.connection.IConnectInterface;
import tsocket.zby.com.tsocket.connection.agreement.CmdParseImpl;

/**
 * Created by zhuj on 2016/9/19 14:14.
 */
public class DeviceBean {

  private boolean onOff;
  private boolean recycle;
  private boolean timerEnable;

  public IConnectInterface mConnect;

  public ICmdParseInterface mParse;

  public void setConnectionInterface(IConnectInterface connectionInterface, Context context) {
    this.mConnect = connectionInterface;
    if (mParse == null) {
      mParse = new CmdParseImpl(this, context);
    }
    this.mConnect.setCmdParse(mParse);
  }

  public boolean isLink() {
    if (AppConstants.isDemo) {
      return true;
    }
    if (mConnect == null) return false;
    return mConnect.isLink();
  }

  public void stopConnect() {
    if (mConnect != null) {
      mConnect.stopConncet();
    }
  }

  public boolean write(byte[] channel) {
    if (!mConnect.isLink()) {
      return false;
    }
    if (mConnect != null) {
      return mConnect.writeAgreement(channel);
      //mConnect.write(CmdPackage.getCmdSuccess());
    }
    return false;
  }

  public boolean writeNoEncrypt(byte[] channel) {
    if (!mConnect.isLink()) {
      return false;
    }
    if (mConnect != null) {
      return mConnect.write(channel);
    }
    return false;
  }

  public IConnectInterface getConnect() {
    return mConnect;
  }


  public boolean isOnOff() {
    return onOff;
  }

  public void setOnOff(boolean onOff) {
    this.onOff = onOff;
  }

  public boolean isRecycle() {
    return recycle;
  }

  public void setRecycle(boolean recycle) {
    this.recycle = recycle;
  }

  public boolean isTimerEnable() {
    return timerEnable;
  }

  public void setTimerEnable(boolean timerEnable) {
    this.timerEnable = timerEnable;
  }
}
