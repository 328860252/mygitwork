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
import java.util.ArrayList;
import java.util.List;
import tsocket.zby.com.tsocket.bean.BluetoothBean;
import tsocket.zby.com.tsocket.bean.DeviceBean;
import tsocket.zby.com.tsocket.connection.IConnectInterface;
import tsocket.zby.com.tsocket.connection.ble.BleImpl;
import tsocket.zby.com.tsocket.connection.ble.BluetoothLeService;
import tsocket.zby.com.tsocket.connection.ble.BluetoothLeServiceMulp;
import tsocket.zby.com.tsocket.utils.LogUtils;
import tsocket.zby.com.tsocket.utils.Tools;

/**
 * Created by zhuj on 2016/9/19 17:10.
 */
public class AppApplication extends Application {

  private DeviceBean mDeviceBean;
  ServiceConnection serviceConnection;
  private BluetoothLeServiceMulp mBluetoothLeService;
  private List<DeviceBean> list = new ArrayList<>();
  private IConnectInterface mInterface;

  @Override public void onCreate() {
    super.onCreate();
    Fabric.with(this, new Crashlytics());
    if (Tools.isMainProcess(this)) {
      bindService();
      IntentFilter interFilter = new IntentFilter(BluetoothLeServiceMulp.ACTION_BLUETOOTH_FOUND);
      registerReceiver(receiver, interFilter);
      registerReceiver(mGattUpdateReceiver ,makeGattUpdateIntentFilter());
    }
  }

  public DeviceBean getDeviceBean() {
    if (mDeviceBean == null) {
      mDeviceBean = new DeviceBean();
    }
    return mDeviceBean;
  }

  @Override public void onTerminate() {
    super.onTerminate();
  }

  private void bindService() {
    serviceConnection = new ServiceConnection() {

      @Override public void onServiceDisconnected(ComponentName service) {
        // TODO Auto-generated method stub
        mBluetoothLeService = null;
      }

      @Override public void onServiceConnected(ComponentName arg0, IBinder service) {
        // TODO Auto-generated method stub
        mBluetoothLeService = ((BluetoothLeServiceMulp.LocalBinder) service).getService();
        if (!mBluetoothLeService.initialize()) {
          //蓝牙无法初始化
        }
        if (mInterface == null) {
          mInterface = new BleImpl(mBluetoothLeService);
        }
      }
    };
    Intent gattServiceIntent = new Intent(this, BluetoothLeServiceMulp.class);
    bindService(gattServiceIntent, serviceConnection, BIND_AUTO_CREATE);
  }

  private final BroadcastReceiver mGattUpdateReceiver = new BroadcastReceiver() {
    @Override public void onReceive(Context context, Intent intent) {
      final String action = intent.getAction();
      final String mac = intent.getStringExtra("mac");
      if (BluetoothLeService.ACTION_GATT_CONNECTED.equals(action)) {
      } else if (BluetoothLeService.ACTION_GATT_DISCONNECTED.equals(action)) {
      } else if (BluetoothLeService.ACTION_GATT_SERVICES_DISCOVERED.equals(action)) {
        ////蓝牙连接成功就自动检验密码
        new Thread(new Runnable() {

          @Override public void run() {
            // TODO Auto-generated method stub
            LogUtils.d("tag", "接受广播1 " + " mac =" + mac);
            if (mDeviceBean != null) {
              try {
                Thread.sleep(300);
              } catch (InterruptedException e) {
                // TODO Auto-generated catch block
                e.printStackTrace();
              }
              //mDeviceBean.write(CmdPackage.set))
            }
          }
        }).start();
      } else if (BluetoothLeService.ACTION_DATA_AVAILABLE.equals(action)) { //解析数据
        String buffer = intent.getStringExtra(BluetoothLeService.EXTRA_DATA);
        LogUtils.v("tag", mac + "接受数据:" + buffer);
        if (mBluetoothLeService != null) {
          if (mDeviceBean != null) {
            //mDeviceBean.get().parseData(MyHexUtils.hexStringToByte(buffer));
          }
        }
      }
    }
  };

  private BroadcastReceiver receiver = new BroadcastReceiver() {
    public void onReceive(android.content.Context arg0, android.content.Intent intent) {
      if(intent.getAction().equals(BluetoothLeServiceMulp.ACTION_BLUETOOTH_FOUND)) {//发现了蓝牙设备
        String mac =intent.getStringExtra("mac");
        String name =intent.getStringExtra("name");
        int rssi =intent.getIntExtra("rssi", 100);
        //autoLink(mac, name, rssi);
      }
    };
  };

  //private void autoLink(String mac, String name, int rssi) {
  //  DeviceBean bean;
  //  synchronized (list) {
  //    for(int i=0; i<list.size(); i++) {
  //      bean = list.get(i);
  //      LogUtils.d("app", "bean.mac: " + bean.getMac() + "  " + mac + "  " + bean.getMac().equals(mac) + "  "+ bean.isLink());
  //      if(bean.getMac().equals(mac)) {
  //        if(bean.getConnect()==null) {
  //          bean.setConnectionInterface(mInterface, this);
  //        }
  //        if(!bean.isLink()) { //没连上，就要连上
  //          bean.connect();
  //        }
  //        return ;
  //      }
  //    }
  //  }
  //}

  private static IntentFilter makeGattUpdateIntentFilter() {
    final IntentFilter intentFilter = new IntentFilter();
    intentFilter.addAction(BluetoothLeService.ACTION_GATT_CONNECTED);
    intentFilter.addAction(BluetoothLeService.ACTION_GATT_DISCONNECTED);
    intentFilter.addAction(BluetoothLeService.ACTION_GATT_SERVICES_DISCOVERED);
    intentFilter.addAction(BluetoothLeService.ACTION_DATA_AVAILABLE);
    return intentFilter;
  }

  public List<DeviceBean> getList() {
    return list;
  }
}
