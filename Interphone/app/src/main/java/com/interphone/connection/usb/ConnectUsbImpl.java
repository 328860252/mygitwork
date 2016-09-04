package com.interphone.connection.usb;

import android.content.Context;
import android.content.Intent;
import com.interphone.AppApplication;
import com.interphone.connection.ConnectAction;
import com.interphone.connection.ICmdParseInterface;
import com.interphone.connection.IConnectInterface;
import com.interphone.connection.agreement.CmdEncrypt;
import com.interphone.connection.agreement.CmdProcess;
import com.interphone.utils.LogUtils;
import com.interphone.utils.MyHexUtils;

/**
 * USB读取数据
 * Created by zhuj on 2016/7/5 9:22.
 */
public class ConnectUsbImpl implements IConnectInterface {

  private final static String  TAG = ConnectUsbImpl.class.getName();

  private int  baudRate = 9600;//波特率
  private byte dataBit = 8;//一个字节多少位
  private byte stopBit = 1;
  private byte parity = 0;
  private byte flowControl = 1;

  private Thread readThread;
  private CmdProcess mCmdProcess;
  private Context mContext;

  public ConnectUsbImpl(Context context) {
    this.mContext = context;
  }

  @Override public boolean connect(String address, String pwd) {
    if (!AppApplication.driver.ResumeUsbList()) {
      return false;
    }
    if (!AppApplication.driver.UartInit()) { //对串口设备进行初始化操作
      return false;
    }
    sendBroadcast(ConnectAction.ACTION_GATT_CONNECTING);
    AppApplication.driver.SetConfig(baudRate, dataBit, stopBit, parity,//配置串口波特率，函数说明可参照编程手册
        flowControl);
    if (readThread !=null) {
      readThread.interrupt();
      readThread = null;
    }
    sendBroadcast(ConnectAction.ACTION_GATT_CONNECTED);
    readThread = new Thread(readRunnable);
    readThread.start();
    return true;
  }

  @Override public void stopConncet() {
    AppApplication.driver.CloseDevice();
  }

  @Override public boolean write(byte[] buffer) {
    if (AppApplication.driver.isConnected()) {
      if (buffer==null) return false;
      int retval = AppApplication.driver.WriteData(buffer, buffer.length);//写数据，第一个参数为需要发送的字节数组，第二个参数为需要发送的字节长度，返回实际发送的字节长度
      if (retval < 0) {
        LogUtils.logD(TAG, "发送失败" );
        return false;
      } else {
        LogUtils.logD(TAG, "发送:" +retval + " :"+ MyHexUtils.buffer2String(buffer));
        return true;
      }
    }
    return false;
  }

  @Override public boolean writeAgreement(byte[] buffer) {
    return write(CmdEncrypt.sendMessage(buffer));
  }

  @Override public boolean isLink() {
    return AppApplication.driver.isConnected();
  }

  @Override public boolean isConnecting() {
    return false;
  }

  @Override public void setCmdParse(ICmdParseInterface cmdParse) {
    mCmdProcess = new CmdProcess(cmdParse);
  }

  @Override public int getConnectType() {
    return IConnectInterface.type_usb;
  }

  //读取数据
  private Runnable readRunnable = new Runnable() {
    @Override public void run() {
      byte[] buffer = new byte[512];
      int length ;
      while (true) {
        length = AppApplication.driver.ReadData(buffer, 512);
        if (length > 0) {
          LogUtils.e(TAG, "readlength " + length + ( mCmdProcess==null) );
          if (mCmdProcess!=null) {
            mCmdProcess.ProcessDataCommand(buffer, length);
          }
        }
      }
    }
  };

  /**
   * 是否支持usb
   * @return
   */
  public static boolean isSupport() {
    return AppApplication.driver.UsbFeatureSupported();
  }

  public static boolean hasDeivce() {
    return AppApplication.driver.EnumerateDevice() != null;
  }

  private void sendBroadcast(String action){
    mContext.sendBroadcast(new Intent(action));
  }
}
