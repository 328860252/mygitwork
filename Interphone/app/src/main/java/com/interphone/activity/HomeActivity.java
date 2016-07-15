package com.interphone.activity;

import android.app.Dialog;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.TextView;
import butterknife.Bind;
import butterknife.ButterKnife;
import butterknife.OnClick;
import com.example.administrator.interphone.R;
import com.interphone.AppApplication;
import com.interphone.AppConstants;
import com.interphone.bean.DeviceBean;
import com.interphone.connection.ConnectAction;
import com.interphone.connection.IConnectInterface;
import com.interphone.connection.agreement.CmdPackage;
import com.interphone.connection.bluetooth.ConnectBluetoothImpl;
import com.interphone.connection.usb.ConnectUsbImpl;
import com.interphone.view.wheel.AlertDialogService;

public class HomeActivity extends BaseActivity {

  @Bind(R.id.tv_title) TextView mTvTitle;
  @Bind(R.id.tv_title_right) TextView mTvTitleRight;
  @Bind(R.id.layout_title_left) LinearLayout mLayoutTitleLeft;
  @Bind(R.id.layout_content) LinearLayout mLayoutContent;
  @Bind(R.id.layout_activity) LinearLayout mLayoutActivity;
  @Bind(R.id.tv_title_left) TextView mTvTitleLeft;
  @Bind(R.id.btn_usbOTG) Button mBtnUsbOTG;

  private DeviceBean dbin;
  private final static int activity_device_list_bluetooth = 11;
  private final static int activity_device_list_usb = 12;

  @Override protected void onCreate(Bundle savedInstanceState) {
    super.onCreate(savedInstanceState);
    setContentView(R.layout.activity_home);
    ButterKnife.bind(this);

    initViews();

    IntentFilter intentFilter = new IntentFilter();
    intentFilter.addAction(ConnectAction.ACTION_GATT_DISCONNECTED);
    intentFilter.addAction(ConnectAction.ACTION_GATT_CONNECTED);
    intentFilter.addAction(ConnectAction.ACTION_GATT_CONNECTING);
    intentFilter.addAction(ConnectAction.ACTION_SHOW_TOAST);
    registerReceiver(receiver, intentFilter);
  }

  private void initViews() {
    mTvTitleRight.setText("设备");
    //mLayoutTitleLeft.setVisibility(View.GONE);

    dbin = ((AppApplication) getApplication()).getDbin();
    //设置true ， 是防止初始化时， 执行onitemnSlelectListener
    //mSpinnerChannel.setSelection(0 , true);
  }

  @Override protected void onActivityResult(int requestCode, int resultCode, Intent data) {
    switch (requestCode) {
      case activity_device_list_bluetooth:
        if (resultCode == IConnectInterface.type_bluetooth) {
          String mac = data.getStringExtra("deviceMac");
          if (dbin.isLink()) {
            dbin.stopConnect();
          }
          if (dbin.getConnect() == null
              || dbin.getConnect().getConnectType() != IConnectInterface.type_bluetooth) {
            dbin.setConnectionInterface(new ConnectBluetoothImpl(this), getApplicationContext());//修改连接通道 为 蓝牙
          }
          dbin.getConnect().connect(mac, "");
        } else if (resultCode == IConnectInterface.type_usb) {
          if (dbin.isLink()) {
            dbin.stopConnect();
          }
          if (dbin.getConnect() == null
              || dbin.getConnect().getConnectType() != IConnectInterface.type_usb) {
            dbin.setConnectionInterface(new ConnectUsbImpl(this) , getApplicationContext()); //修改连接通道为 usb
          }
          dbin.getConnect().connect("", "");
        }
        break;
      case activity_device_list_usb:
        //if (resultCode == RESULT_OK) {
        //    IConnectInterface iConnectInterface = new ConnectUsbImpl();
        //    dbin.setConnect(iConnectInterface);
        //    dbin.getConnect().connect("","");
        //}
        break;
    }
    super.onActivityResult(requestCode, resultCode, data);
  }

  @OnClick(R.id.layout_title_right) public void onClick() {
    Intent intent = new Intent(this, DeviceListActivity.class);
    startActivityForResult(intent, activity_device_list_bluetooth);
  }

  @OnClick({ R.id.btn_proterty, R.id.btn_channelData, R.id.btn_sms, R.id.btn_powerTest })
  public void onClick(View view) {
    Intent intent = null;
    switch (view.getId()) {
      case R.id.btn_proterty:
        intent = new Intent(this, DeviceProtertyActivity.class);
        startActivity(intent);
        break;
      case R.id.btn_channelData:
        intent = new Intent(this, DeviceChannelDataActivity.class);
        startActivity(intent);
        break;
      case R.id.btn_sms:
        if (dbin != null && !TextUtils.isEmpty(dbin.getPassword())) { //有密码
          AlertDialogService.getInputDialog(this, "", getString(R.string.input_password),
              new AlertDialogService.onMyInputListener() {
                @Override public void onClick(Dialog d, EditText tv) {
                  if (dbin.isPasswordRight(tv.getText().toString())) {
                    Intent intent = new Intent(HomeActivity.this, DeviceSmsActivity.class);
                    startActivity(intent);
                    d.dismiss();
                  } else {
                    tv.setText("");
                    showToast(R.string.password_error);
                  }
                }

                @Override public void onCancel(Dialog d) {

                }
              }).show();
        } else {
          intent = new Intent(HomeActivity.this, DeviceSmsActivity.class);
          startActivity(intent);
        }
        break;
      case R.id.btn_powerTest://调频
        AlertDialogService.getInputDialog(this, "", getString(R.string.input_password),
            new AlertDialogService.onMyInputListener() {
              @Override public void onClick(Dialog d, EditText tv) {
                if (tv.getText().toString().equalsIgnoreCase(AppConstants.Power_password)) {
                  Intent intent = new Intent(HomeActivity.this, DevicePowerTestActivity2.class);
                  startActivity(intent);
                  d.dismiss();
                } else {
                  tv.setText("");
                  showToast(R.string.password_error);
                }
              }

              @Override public void onCancel(Dialog d) {

              }
            }).show();
        break;
    }
  }

  BroadcastReceiver receiver = new BroadcastReceiver() {
    @Override public void onReceive(Context context, Intent intent) {
      String action = intent.getAction();
      if (action.equalsIgnoreCase(ConnectAction.ACTION_GATT_CONNECTED)) {
        showToast(R.string.toast_linked);
        AppConstants.isWriteACK = true;
        dbin.write(CmdPackage.getInfo());
      } else if (action.equalsIgnoreCase(ConnectAction.ACTION_GATT_CONNECTING)) {
        showToast(R.string.toast_linking);
      } else if (action.equalsIgnoreCase(ConnectAction.ACTION_GATT_DISCONNECTED)) {
        showToast(R.string.toast_link_lost);
      } else if (action.equalsIgnoreCase(ConnectAction.ACTION_SHOW_TOAST)) {
        showToast(R.string.noLink);
      }
    }
  };

  @Override protected void onDestroy() {
    unregisterReceiver(receiver);
    if (dbin != null) {
      dbin.stopConnect();
    }
    super.onDestroy();
  }

  //@Override public boolean onCreateOptionsMenu(Menu menu) {
  //    menu.add(0, 1, 1, "ack选项");
  //    return super.onCreateOptionsMenu(menu);
  //}

  @Override public boolean onMenuItemSelected(int featureId, MenuItem item) {
    switch (item.getItemId()) {
      case 1:
        AppConstants.isWriteACK = !AppConstants.isWriteACK;
        if (AppConstants.isWriteACK) {
          showToast("打开ack回复");
        } else {
          showToast("关闭ack回复");
        }
        break;
    }
    return super.onMenuItemSelected(featureId, item);
  }

  @OnClick(R.id.layout_title_left) public void onMenu() {
    openOptionsMenu();
  }

  @OnClick(R.id.btn_usbOTG) public void onUSBOTG() {
    if (dbin.isLink()) {
      dbin.stopConnect();
    }
    if (dbin.getConnect() == null
        || dbin.getConnect().getConnectType() != IConnectInterface.type_usb) {
      dbin.setConnectionInterface(new ConnectUsbImpl(this), getApplicationContext());//修改连接通道 为 usb
    }
    dbin.getConnect().connect("", "");
  }
}
