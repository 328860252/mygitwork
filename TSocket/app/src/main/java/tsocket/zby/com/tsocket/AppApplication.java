package tsocket.zby.com.tsocket;

import android.app.Application;
import android.content.BroadcastReceiver;
import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.content.ServiceConnection;
import android.os.IBinder;
import com.crashlytics.android.Crashlytics;
import io.fabric.sdk.android.Fabric;
import tsocket.zby.com.tsocket.bean.DeviceBean;
import tsocket.zby.com.tsocket.connection.ble.BluetoothLeService;
import tsocket.zby.com.tsocket.utils.LogUtils;

/**
 * Created by zhuj on 2016/9/19 17:10.
 */
public class AppApplication extends Application {

  private DeviceBean mDeviceBean;

  @Override public void onCreate() {
    super.onCreate();
//    Fabric.with(this, new Crashlytics());
  }

  public DeviceBean getDeviceBean() {
    if (mDeviceBean == null) {
      mDeviceBean = new DeviceBean();
    }
    return mDeviceBean;
  }
}
