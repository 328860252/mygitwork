package com.interphone.activity;

import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.view.View;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.Spinner;
import android.widget.TextView;
import butterknife.Bind;
import butterknife.ButterKnife;
import butterknife.OnClick;
import com.example.administrator.interphone.R;
import com.interphone.AppApplication;
import com.interphone.AppConstants;
import com.interphone.AppString;
import com.interphone.adapter.StringAdapter;
import com.interphone.bean.DeviceBean;
import com.interphone.bean.ProtertyData;
import com.interphone.connection.agreement.CmdPackage;
import com.interphone.utils.WheelUtils;

public class DeviceProtertyActivity extends BaseActivity {

  @Bind(R.id.tv_title) TextView mTvTitle;
  @Bind(R.id.tv_proterty_tot) TextView mTvProtertyTot;
  @Bind(R.id.layout_proterty_tot) LinearLayout mLayoutProtertyTot;
  @Bind(R.id.et_device_id) EditText mEtDeviceId;
  @Bind(R.id.tv_proterty_VHF) TextView mTvProtertyVHF;
  @Bind(R.id.spinner_proterty_VOX) Spinner mSpinnerProtertyVOX;
  @Bind(R.id.spinner_channel) Spinner mSpinnerChannel;
  @Bind(R.id.tv_device_mode) TextView mTvDeviceMode;
  @Bind(R.id.tv_device_serialNumber) TextView mTvDeviceSerialNumber;
  @Bind(R.id.btn_read) Button btnRead;
  @Bind(R.id.btn_send) Button btnSend;

  private StringAdapter mAdapterProtertyVOX;
  private String[] mArrayProtertyVOX;

  private StringAdapter mAdapterChannelId;
  private String[] mArrayChannelId;

  private DeviceBean dbin;
  private ProtertyData mProtertyData;//当前属性
  private ProtertyData mProtertyWriteData;//往外写时 缓存数据
  private final static int activity_prterty_tot = 11;

  private int totPosition;
  private boolean isWriteChannel = false;//是否在发送 channel数据
  private int  writeChannelIndex = 0 ;//发送的第几个信道

  @Override protected void onCreate(Bundle savedInstanceState) {
    super.onCreate(savedInstanceState);
    setContentView(R.layout.activity_device_proterty);
    ButterKnife.bind(this);
    initBaseViews(this);
    initViews();
  }

  private void initViews() {
    dbin = ((AppApplication) getApplication()).getDbin();
    mProtertyData = dbin.getProtertyData();

    mTvDeviceSerialNumber.setText(mProtertyData.getSerialNumber());
    mEtDeviceId.setText(mProtertyData.getUserId().trim());
    mTvDeviceMode.setText(mProtertyData.getDeviceMode());
    mTvProtertyVHF.setText(mProtertyData.getHFValueString());

    mArrayProtertyVOX = getResources().getStringArray(R.array.ProtertyVOX);
    mAdapterProtertyVOX = new StringAdapter(mArrayProtertyVOX, this, mProtertyData.getVox());
    mSpinnerProtertyVOX.setAdapter(mAdapterProtertyVOX);
    mAdapterProtertyVOX.notifyDataSetChanged();

    //信道
    mArrayChannelId = new String[16];
    for (int i = 0; i < 16; i++) {
      //名字从1开始，1-16
      mArrayChannelId[i] = "信道" + (i + 1);
    }
    mAdapterChannelId =
        new StringAdapter(mArrayChannelId, this, mProtertyData.getActivityChannelId() - 1);
    mSpinnerChannel.setAdapter(mAdapterChannelId);
    mAdapterChannelId.notifyDataSetChanged();

    mSpinnerProtertyVOX.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
      @Override
      public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
        mAdapterProtertyVOX.updateIndex(position);
      }

      @Override public void onNothingSelected(AdapterView<?> parent) {

      }
    });
    //监听事件
    mSpinnerChannel.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
      @Override
      public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
        mAdapterChannelId.updateIndex(position);
        mAdapterChannelId.notifyDataSetChanged();
      }

      @Override public void onNothingSelected(AdapterView<?> parent) {

      }
    });
  }

  private void initData() {
    try {
      mSpinnerProtertyVOX.setSelection(mProtertyData.getVox());
      mTvProtertyTot.setText(("" + mProtertyData.getTotTime()));
      mEtDeviceId.setText(mProtertyData.getUserId().trim());
      //ID从1-16 ， 下标是0-15
      mSpinnerChannel.setSelection(mProtertyData.getActivityChannelId() - 1);
    } catch (IndexOutOfBoundsException e) {
      e.printStackTrace();
      showToast("数据错误，超出范围");
    }
  }

  @OnClick({ R.id.layout_proterty_tot, R.id.btn_read, R.id.btn_send })
  public void onClick(View view) {
    switch (view.getId()) {
      case R.id.layout_proterty_tot:
        Intent intent = new Intent(this, WheelActivity.class);
        intent.putExtra(AppString.WheelTypeKey, WheelUtils.type_protertyTot);
        intent.putExtra(AppString.WheelTypeValue, mProtertyData.getTotTime());
        startActivityForResult(intent, activity_prterty_tot);
        break;
      case R.id.btn_read:
        testCmd();
        if (!dbin.isLink()) {
          showToast(R.string.noLink);
          return;
        }
        if (dbin.write(CmdPackage.getProterty())) {
          showSendToast(false);
        }
        break;
      case R.id.btn_send:
        if (!dbin.isLink()) {
          showToast(R.string.noLink);
          return;
        }
        if (mEtDeviceId.getText().toString().trim().length() != 6) {
          showToast(R.string.toast_device_id_error);
          return;
        }

        //if (dbin.write(CmdPackage.setProteries(mProtertyWriteData))) {
        //  showSendToast(false);
        //}
        saveProtery();
        new Thread(new Runnable() {
          @Override public void run() {
            if (dbin.write(CmdPackage.setProteries(dbin.getProtertyData()))) {
              mHandler.sendEmptyMessage(1);
            }
            isWriteChannel = true;
            writeChannelIndex = 0;
            mHandler.sendEmptyMessage(2);
            //try {
            //  Thread.sleep(1000);
            //} catch (InterruptedException e) {
            //  e.printStackTrace();
            //}
            //if (dbin.write(CmdPackage.setChannel(dbin.getListChannel()))) {
            //  mHandler.sendEmptyMessage(1);
            //}
          }
        }).start();
        break;
    }
  }

  private void saveProtery() {
    mProtertyWriteData = dbin.getProtertyData();
    mProtertyWriteData.setVox(mSpinnerProtertyVOX.getSelectedItemPosition());
    //mProtertyWriteData.setHFvalue(mSpinnerProtertyVHF.getSelectedItemPosition());
    mProtertyWriteData.setUserId(mEtDeviceId.getText().toString().trim());
    mProtertyWriteData.setTotTime(Integer.parseInt(mTvProtertyTot.getText().toString()));
    //channelId 是1-16， 下标是0开始
    mProtertyWriteData.setActivityChannelId(mSpinnerChannel.getSelectedItemPosition() + 1);
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

  @Override public void onBackPressed() {
    saveProtery();
    super.onBackPressed();
  }

  @Override protected void onActivityResult(int requestCode, int resultCode, Intent data) {
    switch (requestCode) {
      case activity_prterty_tot:
        if (resultCode == RESULT_OK) {
          String value = data.getStringExtra(AppString.WheelTypeValue);
          totPosition = data.getIntExtra(AppString.WheelTypeItem, 0);
          mTvProtertyTot.setText(value);
        }
        break;
    }
    super.onActivityResult(requestCode, resultCode, data);
  }

  @Override public void onReceiver(int type, int i) {
    super.onReceiver(type, i);
    switch (type) {
      case CmdPackage.Cmd_type_property:
        mProtertyData = dbin.getProtertyData();
        initData();
        showSendToast(true);
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
          isWriteChannel = false;
          disDialog();
        }
        break;
    }
  }

  private void testCmd() {
    if (AppConstants.isDemo) {
      byte[] cmd6 = new byte[] {
          0x01, 0x02, 0x01, (byte) 0xB4, 0x3, 0x31, 0x32, 0x33, 0x34, 0x35, 0x36, 0x00, 0x00
      };
      dbin.getMParse().parseData(cmd6);

      byte[] cmd1 = new byte[] {
          0x01, 0x01, 0x5A, (byte) 0x57, 0x52, 0x30, 0x31, 0x30, 0x31, 0x32, 0x33, 0x34, 0x35, 0x36,
          0x37, 0x37, 0x38, 0x38
      };
      dbin.getMParse().parseData(cmd1);
    }
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
}
