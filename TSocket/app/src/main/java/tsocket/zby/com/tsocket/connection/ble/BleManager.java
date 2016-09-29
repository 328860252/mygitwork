package tsocket.zby.com.tsocket.connection.ble;

import android.bluetooth.BluetoothAdapter;
import android.bluetooth.BluetoothDevice;
import android.bluetooth.BluetoothManager;
import android.content.Context;
import android.content.Intent;
import android.provider.Settings;
import java.util.HashSet;
import java.util.Set;
import tsocket.zby.com.tsocket.AppConstants;
import tsocket.zby.com.tsocket.connection.ConnectAction;
import tsocket.zby.com.tsocket.utils.LogUtils;
import tsocket.zby.com.tsocket.utils.RxBus;

/**
 * Created by Administrator on 2016/9/22.
 */

public class BleManager {

  private BluetoothAdapter mBluetoothAdapter;
  private BluetoothManager mBluetoothManager;
  private Context mContext;
  private static volatile BleManager mBleManager;
  Thread scanThread;

  Set<String> mMacSet = new HashSet<>();

  private BleManager() {}

  private BleManager(Context context) {
    mContext = context;
  }

  public static BleManager getInstance(Context context) {
    if (mBleManager == null) {
      synchronized (BleManager.class) {
        if (mBleManager==null) {
          mBleManager = new BleManager(context);
        }
      }
    }
    return mBleManager;
  }

  public void bluetoothEnable() {
    if (mBluetoothManager == null) {
      mBluetoothManager = (BluetoothManager) mContext.getSystemService(Context.BLUETOOTH_SERVICE);
      if (mBluetoothManager == null) {
        return;
      }
    }

    if (mBluetoothAdapter == null) {
      mBluetoothAdapter = mBluetoothManager.getAdapter();
      if (mBluetoothAdapter == null) return;
    }
    if (!mBluetoothAdapter.isEnabled()) {
      mBluetoothAdapter.enable();
    }
  }

  public void startScan(boolean isStartScan) {
    if (isStartScan) { //开始搜索
      if (scanThread != null) {
        if (scanThread.isAlive()) {
          scanThread.interrupt();
        }
        scanThread = null;
      }
      scanThread = new Thread(scanRunable);
      if (mBluetoothManager == null) {
        mBluetoothManager = (BluetoothManager) mContext.getSystemService(Context.BLUETOOTH_SERVICE);
        if (mBluetoothManager == null) {
          return;
        }
      }

      if (mBluetoothAdapter == null) {
        mBluetoothAdapter = mBluetoothManager.getAdapter();
        if (mBluetoothAdapter == null) return;
      }
      mMacSet.clear();
      scanThread.start();
    } else { //关闭搜索线程
      if (scanThread!=null) {
        scanThread.interrupt();
        scanThread=null;
      }
    }
  }

  Runnable scanRunable = new Runnable() {

    @Override public void run() {
      // TODO Auto-generated method stub
      if (mBluetoothAdapter != null && !mBluetoothAdapter.isEnabled()) {
        mBluetoothAdapter.enable();
      }
      if (mBluetoothAdapter != null && mBluetoothAdapter.isEnabled()) {
        mBluetoothAdapter.startLeScan(scanCallBack);
      }
      try {
        Thread.sleep(AppConstants.scan_time);
      } catch (InterruptedException e) {
        // TODO Auto-generated catch block
        e.printStackTrace();
      } finally {
        if (mBluetoothAdapter != null) {
          mBluetoothAdapter.stopLeScan(scanCallBack);
        }
        RxBus.getDefault().post(ConnectAction.ACTION_DEVICE_SCAN_FINISH);
      }
    }
  };

  private BluetoothAdapter.LeScanCallback scanCallBack = new BluetoothAdapter.LeScanCallback() {

    @Override public void onLeScan(BluetoothDevice arg0, int arg1, byte[] arg2) {
      // TODO Auto-generated method stub
      if (!mMacSet.contains(arg0.getAddress())) {//避免一次搜索， 同一个设备多次回调
        LogUtils.d("bleManager", "found device :" + arg0.getAddress() + "  " +arg0.getName() );
        mMacSet.add(arg0.getAddress());
        foundDevice(arg0, arg1);
      }
    }
  };

  /**
   * @param device
   * @param arg1
   */
  private void foundDevice(BluetoothDevice device, int arg1) {
    Intent intent = new Intent(ConnectAction.ACTION_BLUETOOTH_FOUND);
    intent.putExtra("mac", device.getAddress());
    intent.putExtra("name", device.getName());
    intent.putExtra("rssi", arg1);
    intent.putExtra("isBonded", device.getBondState()==BluetoothDevice.BOND_BONDED);
    mContext.sendBroadcast(intent);
  }

  public boolean isBonded(String mac) {
    BluetoothDevice bd = mBluetoothAdapter.getRemoteDevice(mac);
    if (bd!=null) {
      return bd.getBondState() == BluetoothDevice.BOND_BONDED;
    }
    return false;
  }
}
