package com.interphone;

import android.app.Application;
import android.content.Context;
import android.hardware.usb.UsbManager;
import cn.wch.ch34xuartdriver.CH34xUARTDriver;
import com.crashlytics.android.Crashlytics;
import com.interphone.bean.DeviceBean;
import com.interphone.connection.IConnectInterface;
import com.interphone.connection.bluetooth.ConnectBluetoothImpl;
import io.fabric.sdk.android.Fabric;

/**
 * Created by Administrator on 2016/5/21.
 */
public class AppApplication extends Application {

  public static CH34xUARTDriver driver;// 需要将CH34x的驱动类写在APP类下面，使得帮助类的生命周期与整个应用程序的生命周期是相同的
  IConnectInterface mIConnectInterface;
  DeviceBean dbin;

  private static final String ACTION_USB_PERMISSION = "cn.wch.wchusbdriver.USB_PERMISSION";

  @Override public void onCreate() {
    super.onCreate();
    Fabric.with(this, new Crashlytics());
    //usb
    driver = new CH34xUARTDriver((UsbManager) getSystemService(Context.USB_SERVICE), this,
        ACTION_USB_PERMISSION);

    //bluetooth,默认是蓝牙连接类型
    mIConnectInterface = new ConnectBluetoothImpl(this);
    dbin = new DeviceBean();
    dbin.setConnectionInterface(mIConnectInterface, this);

    // 异常处理，不需要处理时注释掉这两句即可！
    //if (!BuildConfig.DEBUG) {//debug下不需要
    //  CrashHandler crashHandler = CrashHandler.getInstance();
    //  // 注册crashHandler
    //  crashHandler.init(getApplicationContext());
    //
    //  /**
    //   * 这里有个问题， 要判断网络状况，  在这里调用，就只是在启动软件的时候，发送以前的错误信息到服务器，
    //   * 如果要及时的  发生错误就发送信息，  就得将这个sendPreviousReportsToServer函数在handlerException中调用
    //   */
    //  //发送错误报告到服务器
    //  crashHandler.sendPreviousReportsToServer();
    //}
  }

  public DeviceBean getDbin() {
    return dbin;
  }
}
