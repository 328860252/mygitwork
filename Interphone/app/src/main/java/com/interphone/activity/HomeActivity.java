package com.interphone.activity;

import android.app.Dialog;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.content.res.Resources;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.view.MenuItem;
import android.view.MotionEvent;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.RadioButton;
import android.widget.SeekBar;
import android.widget.TextView;
import butterknife.Bind;
import butterknife.ButterKnife;
import butterknife.OnClick;
import butterknife.OnTouch;
//import com.crashlytics.android.Crashlytics;
import com.example.administrator.interphone.R;
import com.interphone.AppApplication;
import com.interphone.AppConstants;
import com.interphone.bean.DeviceBean;
import com.interphone.connection.ConnectAction;
import com.interphone.connection.IConnectInterface;
import com.interphone.connection.agreement.CmdEncrypt;
import com.interphone.connection.agreement.CmdPackage;
import com.interphone.connection.bluetooth.ConnectBluetoothImpl;
import com.interphone.connection.usb.ConnectUsbImpl;
import com.interphone.utils.MyHexUtils;
import com.interphone.view.wheel.AlertDialogService;

public class HomeActivity extends BaseActivity implements View.OnTouchListener {

  @Bind(R.id.tv_title)            TextView      mTvTitle;
  @Bind(R.id.tv_title_right)      TextView      mTvTitleRight;
  @Bind(R.id.layout_title_left)   LinearLayout  mLayoutTitleLeft;
  @Bind(R.id.layout_content)      LinearLayout  mLayoutContent;
  @Bind(R.id.layout_activity)     LinearLayout  mLayoutActivity;
  @Bind(R.id.tv_title_left)       TextView      mTvTitleLeft;
  @Bind(R.id.btn_usbOTG)          Button        mBtnUsbOTG;
  @Bind(R.id.rb_TX)               RadioButton   mRbTX;
  @Bind(R.id.rb_RX)               RadioButton   mRbRX;
  @Bind(R.id.rb_idel)             RadioButton   mRbIdel;
  @Bind(R.id.btn_channelData)     Button        mBtnChannelData;
  @Bind(R.id.btn_proterty)        Button        mBtnProterty;
  @Bind(R.id.btn_sms)             Button        mBtnSms;
  @Bind(R.id.btn_powerTest)       Button        mBtnPowerTest;
  @Bind(R.id.btn_ptt)             Button        mBtnPtt;
  @Bind(R.id.btn_scan)            Button        mBtnScan;
  @Bind(R.id.btn_monitor)         Button        mBtnMonitor;
  @Bind(R.id.tv_scanRate)         TextView      mTvScanRate;
  @Bind(R.id.sb_volica)           SeekBar       mSbVolica;
  @Bind(R.id.tv_volice_value)     TextView      mTvVoliceValue;

  private DeviceBean dbin;
  private final static int activity_device_list_bluetooth = 11;
  private final static int activity_device_list_usb = 12;

  private boolean isWriteChannel = false;
  private int writeChannelIndex = 0;

  @Override protected void onCreate(Bundle savedInstanceState) {
    super.onCreate(savedInstanceState);
    //Fabric.with(this, new Crashlytics());
    setContentView(R.layout.activity_home);
    ButterKnife.bind(this);

    initViews();

    IntentFilter intentFilter = new IntentFilter();
    intentFilter.addAction(ConnectAction.ACTION_GATT_DISCONNECTED);
    intentFilter.addAction(ConnectAction.ACTION_GATT_CONNECTED);
    intentFilter.addAction(ConnectAction.ACTION_GATT_CONNECTING);
    intentFilter.addAction(ConnectAction.ACTION_SHOW_TOAST);
    registerReceiver(receiver, intentFilter);

    if (AppConstants.isDemo) {
      new Thread(new Runnable() {
        @Override public void run() {
          try {
            Thread.sleep(500);
          } catch (InterruptedException e) {
            e.printStackTrace();
          }
          sendBroadcast(new Intent(ConnectAction.ACTION_GATT_CONNECTED));
          try {
            byte[] cmd1 = new byte[] {
                0x01, 0x03, 0x01, (byte) 0x00, 0x41, 0x05, 0x62, 0x50, 0x41, 0x05, 0x62, 0x50, 0x00, 0x00,
                0x00, 0x00, 0x00, 0x00, 0x00, 0x00, 0x00, 0x00, 0x00, 0x00, 0x00, 0x00, 0x00, 0x00, 0x00,
                0x00
            };
            for (int i=1; i<17 ; i++) {
              Thread.sleep(500);
              cmd1[3] = (byte) i;
              dbin.getMParse().parseData(cmd1);
            }
            dbin.getMParse().parseReceiverCmd(CmdPackage.CMD_TYPE_ACK_CHANNEL_END);

          } catch (InterruptedException e) {
            e.printStackTrace();
          }
        }
      }).start();
    }
  }

  private void initViews() {
    mTvTitleRight.setText("设备");
    mLayoutTitleLeft.setVisibility(View.GONE);

    mTvVoliceValue.setText("0");
    dbin = ((AppApplication) getApplication()).getDbin();
    //设置true ， 是防止初始化时， 执行onitemnSlelectListener
    //mSpinnerChannel.setSelection(0 , true);
    mSbVolica.setOnSeekBarChangeListener(new SeekBar.OnSeekBarChangeListener() {
      @Override public void onProgressChanged(SeekBar seekBar, int progress, boolean fromUser) {
        mTvVoliceValue.setText(""+progress);
      }

      @Override public void onStartTrackingTouch(SeekBar seekBar) {

      }

      @Override public void onStopTrackingTouch(SeekBar seekBar) {
        if (dbin!=null) {
          dbin.getProtertyData().setVox(mSbVolica.getProgress());
        }
        if (dbin== null || !dbin.isLink()) {
          showToast(R.string.noLink);
          return;
        }
        new Thread(new Runnable() {
          @Override public void run() {
            if (dbin.write(CmdPackage.setProteries(dbin.getProtertyData()))) {
              mHandler.sendEmptyMessage(1);
            }
            mHandler.sendEmptyMessage(2);
            isWriteChannel = true;
            writeChannelIndex = 0;
            if (AppConstants.isDemo) { //模拟返回数据
              while (writeChannelIndex<dbin.getListChannel().size()) {
                try {
                  Thread.sleep(500);
                } catch (InterruptedException e) {
                  e.printStackTrace();
                }
                dbin.getMParse().parseReceiverCmd(CmdPackage.CMD_TYPE_ACK);
              }
            }
          }
        }).start();
      }
    });
  }

  private Handler mHandler = new Handler() {
    public void handleMessage(Message msg) {
      switch (msg.what) {
        case 1:
          showSendToast(false);
          break;
        case 2:
          showDialog();
          break;
      }
    }
  };

  @Override public void onReceiver(int type, int i) {
    super.onReceiver(type, i);
    switch (type) {
      case CmdPackage.Cmd_type_property:
        mSbVolica.setProgress(dbin.getProtertyData().getVox());
        break;
      case CmdPackage.Cmd_type_status:
        if (dbin != null) {
          switch (dbin.getProtertyData().getStatus()) {
            case 0x0A:
              mRbTX.setChecked(true);
              break;
            case 0x0B:
              mRbRX.setChecked(true);
              break;
            default:
              mRbIdel.setChecked(true);
              break;
          }
          mTvScanRate.setText(dbin.getProtertyData().getScanRate());
        }
        break;
      case CmdPackage.CMD_TYPE_ACK:
        if (!isWriteChannel) return;
        if (writeChannelIndex < dbin.getListChannel().size()) {
          AppConstants.isWriteACK = false;
          if (dbin.write(CmdPackage.setChannel(dbin.getChannelData(writeChannelIndex)))) {
            mHandler.sendEmptyMessage(1);
          }
          writeChannelIndex++;
        } else {
          disDialog();
          isWriteChannel = false;
        }
        break;
      case CmdPackage.CMD_TYPE_ACK_CHANNEL_END:
        disDialog();
        break;
    }
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
            dbin.setConnectionInterface(new ConnectBluetoothImpl(this),
                getApplicationContext());//修改连接通道 为 蓝牙
          }
          dbin.getConnect().connect(mac, "");
        } else if (resultCode == IConnectInterface.type_usb) {
          if (dbin.isLink()) {
            dbin.stopConnect();
          }
          if (dbin.getConnect() == null
              || dbin.getConnect().getConnectType() != IConnectInterface.type_usb) {
            dbin.setConnectionInterface(new ConnectUsbImpl(this),
                getApplicationContext()); //修改连接通道为 usb
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

  @OnTouch({ R.id.btn_ptt, R.id.btn_scan, R.id.btn_monitor })
  public boolean onTouch(View v, MotionEvent event) {
    boolean isPress;
    if (event.getAction() == MotionEvent.ACTION_DOWN) {
      isPress = true;
    } else if (event.getAction() == MotionEvent.ACTION_UP) {
      isPress = false;
    } else {
      return false;
    }
    switch (v.getId()) {
      case R.id.btn_ptt:
        if (dbin.write(CmdPackage.setPTT(isPress))){
          showSendToast(false);
        }
        break;
      case R.id.btn_scan:
        if (dbin.write(CmdPackage.setScan(isPress))) {
          showSendToast(false);
        }
        break;
      case R.id.btn_monitor:
        if (dbin.write(CmdPackage.setMonitor(isPress))) {
          showSendToast(false);
        }
        break;
    }
    return false;
  }

  private boolean isLink() {
    if (!dbin.isLink()) {
      showToast(R.string.noLink);
      return false;
    }
    return true;
  }

  private void showSendToast(boolean isReceiver) {
    if (isReceiver) {
      showToast("读取成功");
    } else {
      showToast("发送成功");
    }
    //btnSend.setEnabled(isReceiver);
    //btnRead.setEnabled(isReceiver);
  }

  @OnClick({ R.id.btn_proterty, R.id.btn_channelData, R.id.btn_sms, R.id.btn_powerTest })
  public void onClick(View view) {
    Intent intent = null;
    switch (view.getId()) {
      case R.id.btn_proterty:
        //testCmd();
        intent = new Intent(this, DeviceProtertyActivity.class);
        startActivity(intent);
        break;
      case R.id.btn_channelData:
        intent = new Intent(this, DeviceChannelDataActivity.class);
        startActivity(intent);
        break;
      case R.id.btn_sms:
        //if (dbin != null && !TextUtils.isEmpty(dbin.getPassword())) { //有密码
        //  AlertDialogService.getInputDialog(this, "", getString(R.string.input_password),
        //      new AlertDialogService.onMyInputListener() {
        //        @Override public void onClick(Dialog d, EditText tv) {
        //          if (dbin.isPasswordRight(tv.getText().toString())) {
        //            Intent intent = new Intent(HomeActivity.this, DeviceSmsActivity.class);
        //            startActivity(intent);
        //            d.dismiss();
        //          } else {
        //            tv.setText("");
        //            showToast(R.string.password_error);
        //          }
        //        }
        //
        //        @Override public void onCancel(Dialog d) {
        //
        //        }
        //      }).show();
        //} else {
        intent = new Intent(HomeActivity.this, DeviceSmsActivity.class);
        startActivity(intent);
        //}
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
        mRbIdel.setChecked(true);
        AppConstants.isWriteACK = true;
        dbin.write(CmdPackage.getInfo());
        showDialog();
        new Thread(new Runnable() {
          @Override public void run() {
            try {
              Thread.sleep(AppConstants.wait);
              AppConstants.isWriteACK = true;
              dbin.write(CmdPackage.getChannel());
            } catch (InterruptedException e) {
              e.printStackTrace();
            }
          }
        }).start();
      } else if (action.equalsIgnoreCase(ConnectAction.ACTION_GATT_CONNECTING)) {
        showToast(R.string.toast_linking);
      } else if (action.equalsIgnoreCase(ConnectAction.ACTION_GATT_DISCONNECTED)) {
        mRbIdel.setChecked(true);
        showToast(R.string.toast_link_lost);
        disDialog();
      } else if (action.equalsIgnoreCase(ConnectAction.ACTION_SHOW_TOAST)) {
        showToast(R.string.noLink);
      }
    }
  };

  @Override protected void onResume() {
    if (dbin!=null) {
      mSbVolica.setProgress(dbin.getProtertyData().getVox());
    }
    super.onResume();
  }

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
    if (ConnectUsbImpl.isSupport()) {
      //       if (!ConnectUsbImpl.hasDeivce()) {
      //         showToast(R.string.bluetooth_notfound);
      //        return ;
      //      }
      if (dbin.isLink()) {
        dbin.stopConnect();
      }
      if (dbin.getConnect() == null
          || dbin.getConnect().getConnectType() != IConnectInterface.type_usb) {
        dbin.setConnectionInterface(new ConnectUsbImpl(this),
            getApplicationContext());//修改连接通道 为 usb
      }
      dbin.getConnect().connect("", "");
    }
  }

  private void testCmd() {
    if (AppConstants.isDemo) {
      byte[] cmd1 = MyHexUtils.hexStringToByte("35 01 06 00 11 B1 05 09 46 29 87 50 10");
      byte[] cmd2 = CmdEncrypt.processMessage(cmd1);
      byte[] cmd6 = new byte[] { 0x01, 0x06, 0x0A };
      dbin.getMParse().parseData(cmd2);
    }
  }
}
