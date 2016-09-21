package tsocket.zby.com.tsocket.connection.ble;

import android.bluetooth.BluetoothAdapter;
import android.bluetooth.BluetoothDevice;
import android.bluetooth.BluetoothManager;
import android.content.Context;
import android.content.Intent;
import android.provider.Settings;
import tsocket.zby.com.tsocket.AppConstants;
import tsocket.zby.com.tsocket.connection.ConnectAction;

/**
 * Created by Administrator on 2016/9/22.
 */

public class BleManager {

  private BluetoothAdapter mBluetoothAdapter;
  private BluetoothManager mBluetoothManager;
  Thread scanThread;

  private Context mContext;

  public BleManager(Context context) {
    mContext = context;
  }

  public void startScan() {
    if (scanThread == null) {
      scanThread = new Thread(scanRunable);
    }
    if (mBluetoothManager == null) {
      mBluetoothManager = (BluetoothManager) mContext.getSystemService(Context.BLUETOOTH_SERVICE);
      if (mBluetoothManager == null) {
        return;
      }
    }

    mBluetoothAdapter = mBluetoothManager.getAdapter();
    if (mBluetoothAdapter == null) {
      return;
    }
    scanThread.start();
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
      }
    }
  };

  private BluetoothAdapter.LeScanCallback scanCallBack = new BluetoothAdapter.LeScanCallback() {

    @Override public void onLeScan(BluetoothDevice arg0, int arg1, byte[] arg2) {
      // TODO Auto-generated method stub
      foundDevice(arg0, arg1);
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
    mContext.sendBroadcast(intent);
  }
}
