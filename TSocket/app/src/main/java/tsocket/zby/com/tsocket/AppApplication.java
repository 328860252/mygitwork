package tsocket.zby.com.tsocket;

import android.app.Application;
import android.content.BroadcastReceiver;
import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.content.ServiceConnection;
import android.os.IBinder;
import android.widget.Toast;
import com.crashlytics.android.Crashlytics;
import io.fabric.sdk.android.Fabric;
import java.util.ArrayList;
import java.util.List;
import tsocket.zby.com.tsocket.bean.DeviceBean;
import tsocket.zby.com.tsocket.connection.ConnectAction;
import tsocket.zby.com.tsocket.connection.IConnectInterface;
import tsocket.zby.com.tsocket.connection.agreement.CmdPackage;
import tsocket.zby.com.tsocket.connection.ble.BleImpl;
import tsocket.zby.com.tsocket.connection.ble.BluetoothLeServiceMulp;
import tsocket.zby.com.tsocket.utils.LogUtils;
import tsocket.zby.com.tsocket.utils.MyHexUtils;
import tsocket.zby.com.tsocket.utils.RxBus;
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
      IntentFilter interFilter = new IntentFilter(ConnectAction.ACTION_BLUETOOTH_FOUND);
      registerReceiver(receiver, interFilter);
      registerReceiver(mGattUpdateReceiver ,makeGattUpdateIntentFilter());
    }
  }

  public DeviceBean getDeviceBean() {
    return mDeviceBean;
  }

  public void setDevcieBean(DeviceBean devcieBean) {
    this.mDeviceBean = devcieBean;
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
      final String mac = intent.getStringExtra(ConnectAction.BROADCAST_DEVICE_MAC);
      if (ConnectAction.ACTION_GATT_CONNECTED.equals(action)) {
      } else if (ConnectAction.ACTION_GATT_DISCONNECTED.equals(action)) {
        Toast.makeText(getApplicationContext(), R.string.toast_linkLost, Toast.LENGTH_LONG).show();
      } else if (ConnectAction.ACTION_GATT_SERVICES_DISCOVERED.equals(action)) {
        ////蓝牙连接成功就自动检验密码
        new Thread(new Runnable() {

          @Override public void run() {
            // TODO Auto-generated method stub
            LogUtils.d("application", "接受广播连接成功 " + " mac =" + mac);
            if (mDeviceBean != null) {
              mDeviceBean.write(CmdPackage.getTimer());
              try {
                Thread.sleep(AppConstants.SEND_TIME_DEALY);
              } catch (InterruptedException e) {
                // TODO Auto-generated catch block
                e.printStackTrace();
              }
              mDeviceBean.write(CmdPackage.getTimer());
              try {
                Thread.sleep(AppConstants.SEND_TIME_DEALY);
              } catch (InterruptedException e) {
                // TODO Auto-generated catch block
                e.printStackTrace();
              }
              mDeviceBean.write(CmdPackage.setTimerCheck());
              try {
                Thread.sleep(AppConstants.SEND_TIME_DEALY);
              } catch (InterruptedException e) {
                // TODO Auto-generated catch block
                e.printStackTrace();
              }
              mDeviceBean.write(CmdPackage.getDownCountTimer());

            }
          }
        }).start();
        RxBus.getDefault().post(action);
      } else if (ConnectAction.ACTION_RECEIVER_DATA.equals(action)) { //解析数据
        byte[] buffer = intent.getByteArrayExtra(ConnectAction.BROADCAST_DATA_value);
        LogUtils.v("tag", mac + "接受数据:" + MyHexUtils.buffer2String(buffer));
        if (mBluetoothLeService != null) {
          if (mDeviceBean != null) {
            mDeviceBean.getProccess().ProcessDataCommand(buffer,buffer.length);
          }
        }
      }
    }
  };

  private BroadcastReceiver receiver = new BroadcastReceiver() {
    public void onReceive(android.content.Context arg0, android.content.Intent intent) {
      if (intent.getAction().equals(ConnectAction.ACTION_BLUETOOTH_FOUND)) {//发现了蓝牙设备
        String mac =intent.getStringExtra("mac");
        String name =intent.getStringExtra("name");
        addOrUpdateDeviceBean(name, mac);
        //int rssi =intent.getIntExtra("rssi", 100);
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
    intentFilter.addAction(ConnectAction.ACTION_GATT_CONNECTED);
    intentFilter.addAction(ConnectAction.ACTION_GATT_DISCONNECTED);
    intentFilter.addAction(ConnectAction.ACTION_GATT_SERVICES_DISCOVERED);
    intentFilter.addAction(ConnectAction.ACTION_RECEIVER_DATA);
    return intentFilter;
  }

  public List<DeviceBean> getList() {
    return list;
  }

  public void addOrUpdateDeviceBean(String name, String mac) {
    DeviceBean dbin;
    for (int i = 0; i < list.size(); i++) {
      if (list.get(i).getMac().equals(mac)) {
        list.get(i).setName(name);
        return;
      }
    }
    dbin = new DeviceBean();
    dbin.setName(name);
    dbin.setMac(mac);
    dbin.setConnectionInterface(mInterface, this);
    list.add(dbin);
    RxBus.getDefault().post("newDeviceBean");
  }
}
