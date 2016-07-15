package com.interphone.activity;

import android.bluetooth.BluetoothAdapter;
import android.bluetooth.BluetoothDevice;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;
import butterknife.Bind;
import butterknife.ButterKnife;
import butterknife.OnClick;
import com.example.administrator.interphone.R;
import com.interphone.AppConstants;
import com.interphone.connection.IConnectInterface;
import com.interphone.connection.usb.ConnectUsbImpl;
import java.util.Set;

public class DeviceListActivity extends BaseActivity implements AdapterView.OnItemClickListener {

  @Bind(R.id.pariesListView) ListView mListViewPaires;
  @Bind(R.id.listView) ListView mNewListView;
  @Bind(R.id.progressBar) ProgressBar mProgressBar;
  @Bind(R.id.btn_scan) Button mBtnScan;
  @Bind(R.id.layout_title_left) LinearLayout mLayoutTitleLeft;
  @Bind(R.id.tv_title) TextView mTvTitle;
  @Bind(R.id.btn_usb) Button mBtnUsb;
  private BluetoothAdapter mBluetoothAdapter;
  private ArrayAdapter<String> mNewArrayAdapter;
  private ArrayAdapter<String> mPairesArrayAdapter;

  private final static int handler_scan_over = 11;
  private final static int handler_scan_start = 12;

  private boolean isShowScanResult = true;

  @Override protected void onCreate(Bundle savedInstanceState) {
    super.onCreate(savedInstanceState);
    setContentView(R.layout.activity_device_list);
    ButterKnife.bind(this);
    //tv_title.setText(R.string.device_list);
    openBluetooth();
    mNewArrayAdapter = new ArrayAdapter<String>(this, R.layout.device_discover);
    mPairesArrayAdapter = new ArrayAdapter<String>(this, R.layout.device_discover);
    mNewListView.setAdapter(mNewArrayAdapter);
    mListViewPaires.setAdapter(mPairesArrayAdapter);
    mNewListView.setOnItemClickListener(this);
    mListViewPaires.setOnItemClickListener(this);
    doDiscovery();
    if (AppConstants.isDemo) {
      mNewArrayAdapter.add("test_00:11:22:33:44:55:66");
      mNewArrayAdapter.notifyDataSetChanged();
    }
  }

  private Handler mHandler = new Handler() {
    public void handleMessage(Message msg) {
      switch (msg.what) {
        case handler_scan_over:
          mBtnScan.setVisibility(View.VISIBLE);
          break;
        case handler_scan_start:
          mBtnScan.setVisibility(View.GONE);
          break;
      }
    }
  };

  /**
   * Start device discover with the BluetoothAdapter
   */
  private void doDiscovery() {
    mNewArrayAdapter.clear();
    mPairesArrayAdapter.clear();
    Set<BluetoothDevice> deviceSet = mBluetoothAdapter.getBondedDevices();
    for (BluetoothDevice device : deviceSet) {
      mPairesArrayAdapter.add(device.getName() + "/n" + device.getAddress());
      mPairesArrayAdapter.notifyDataSetChanged();
    }
    mHandler.sendEmptyMessage(handler_scan_start);
    if (mBluetoothAdapter.isDiscovering()) {
      mBluetoothAdapter.cancelDiscovery();
    }
    mBluetoothAdapter.startDiscovery();
  }

  private void openBluetooth() {
    mBluetoothAdapter = BluetoothAdapter.getDefaultAdapter();
    if (null != mBluetoothAdapter) {
      if (!mBluetoothAdapter.isEnabled()) {
        //mBluetoothAdapter.enable();
        Intent intent = new Intent(BluetoothAdapter.ACTION_REQUEST_ENABLE);
        startActivity(intent);
      }
    } else {
      Toast.makeText(this, getString(R.string.noBluetooth), Toast.LENGTH_LONG).show();
      finish();
    }
  }

  private void foundDevice(BluetoothDevice bbin) {
    for (int i = 0; i < mNewArrayAdapter.getCount(); i++) {
      if (mNewArrayAdapter.getItem(i).toLowerCase().contains(bbin.getAddress().toLowerCase())) {
        return;
      }
    }
    mNewArrayAdapter.add(bbin.getName() + "/n" + bbin.getAddress());
    mNewArrayAdapter.notifyDataSetChanged();
  }

  @Override public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
    isShowScanResult = false;
    mBluetoothAdapter.cancelDiscovery();

    // Get the device MAC address, which is the last 17 chars in the View
    String info = ((TextView) view).getText().toString();
    String address = info.substring(info.length() - 17);

    // Create the result Intent and include the MAC address
    Intent intent = new Intent();
    intent.putExtra("deviceMac", address);

    // Set result and finish this Activity
    setResult(IConnectInterface.type_bluetooth, intent);
    System.gc();
    finish();
  }

  private final BroadcastReceiver mReceiver = new BroadcastReceiver() {
    @Override public void onReceive(Context context, Intent intent) {
      String action = intent.getAction();
      try {
        // When discovery finds a device
        if (BluetoothDevice.ACTION_FOUND.equals(action)) {//发现蓝牙设备
          // Get the BluetoothDevice object from the Intent
          BluetoothDevice device = intent.getParcelableExtra(BluetoothDevice.EXTRA_DEVICE);
          // If it's already paired, skip it, because it's been listed already
          foundDevice(device);
          Log.v("DeviceList ", "found bluetooth--->" + device.getAddress());
        } else if (BluetoothAdapter.ACTION_DISCOVERY_FINISHED.equals(action)) { //搜索结束
          mHandler.sendEmptyMessage(handler_scan_over);
          if (mNewArrayAdapter.getCount() == 0 && isShowScanResult) {
            Toast.makeText(DeviceListActivity.this, R.string.bluetooth_notfound, Toast.LENGTH_SHORT)
                .show();
          }
        }
      } catch (Exception e) {
        e.printStackTrace();
        // TODO: handle exception
      }
    }
  };

  private void registerBroadcast() {
    IntentFilter filter = new IntentFilter(BluetoothDevice.ACTION_FOUND);
    filter.addAction(BluetoothAdapter.ACTION_DISCOVERY_FINISHED);
    this.registerReceiver(mReceiver, filter);
  }

  @OnClick(R.id.btn_scan) public void onClick() {
    doDiscovery();
  }

  @OnClick(R.id.layout_title_left) public void onback() {
    finish();
  }

  @Override protected void onStart() {
    registerBroadcast();
    super.onStart();
  }

  @Override protected void onStop() {
    unregisterReceiver(mReceiver);
    super.onStop();
  }

  @OnClick(R.id.btn_usb) public void onBtn_usb() {
    if (ConnectUsbImpl.isSupport()) {
        setResult(IConnectInterface.type_usb);
        finish();
    } else {
      showToast(R.string.not_support_usb);
    }
  }
}
