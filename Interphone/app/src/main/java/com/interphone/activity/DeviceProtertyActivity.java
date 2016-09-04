package com.interphone.activity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.Spinner;
import android.widget.TextView;

import com.example.administrator.interphone.R;
import com.interphone.AppApplication;
import com.interphone.AppConstants;
import com.interphone.AppString;
import com.interphone.adapter.StringAdapter;
import com.interphone.bean.DeviceBean;
import com.interphone.bean.ProtertyData;
import com.interphone.connection.agreement.CmdPackage;
import com.interphone.utils.WheelUtils;

import butterknife.Bind;
import butterknife.ButterKnife;
import butterknife.OnClick;

public class DeviceProtertyActivity extends BaseActivity {

    @Bind(R.id.tv_title)
    TextView mTvTitle;
    @Bind(R.id.tv_proterty_tot)
    TextView mTvProtertyTot;
    @Bind(R.id.layout_proterty_tot)
    LinearLayout mLayoutProtertyTot;
    @Bind(R.id.tv_device_id)
    TextView mTvDeviceId;
    @Bind(R.id.tv_proterty_VHF)
    TextView mTvProtertyVHF;
    @Bind(R.id.spinner_proterty_VOX)
    Spinner mSpinnerProtertyVOX;
    @Bind(R.id.spinner_channel)
    Spinner mSpinnerChannel;
    @Bind(R.id.tv_device_mode)
    TextView mTvDeviceMode;
    @Bind(R.id.tv_device_serialNumber)
    TextView mTvDeviceSerialNumber;
    @Bind(R.id.btn_read)
    Button btnRead;
    @Bind(R.id.btn_send)
    Button btnSend;

    private StringAdapter mAdapterProtertyVOX;
    private String[] mArrayProtertyVOX;

    private StringAdapter mAdapterChannelId;
    private String[] mArrayChannelId;

    private DeviceBean dbin;
    private ProtertyData mProtertyData;//当前属性
    private ProtertyData mProtertyWriteData;//往外写时 缓存数据
    private final static int activity_prterty_tot = 11;

    private int totPosition;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
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
        mTvDeviceId.setText(mProtertyData.getUserId().trim());
        mTvDeviceMode.setText(mProtertyData.getDeviceMode());
        mTvProtertyVHF.setText(mProtertyData.getHFValueString());

        mArrayProtertyVOX = getResources().getStringArray(R.array.ProtertyVOX);
        mAdapterProtertyVOX = new StringAdapter(mArrayProtertyVOX, this, mProtertyData.getVox());
        mSpinnerProtertyVOX.setAdapter(mAdapterProtertyVOX);
        mAdapterProtertyVOX.notifyDataSetChanged();

        //信道
        mArrayChannelId = new String[16];
        for (int i = 0; i < 16; i++) {
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

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });
        //监听事件
        mSpinnerChannel.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                mAdapterChannelId.updateIndex(position);
                mAdapterChannelId.notifyDataSetChanged();
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });
    }

    private void initData() {
        try {
            mSpinnerProtertyVOX.setSelection(mProtertyData.getVox());
            mTvProtertyTot.setText(("" + mProtertyData.getTotTime()));
            mSpinnerChannel.setSelection(mProtertyData.getActivityChannelId() - 1);
        } catch (IndexOutOfBoundsException e) {
            e.printStackTrace();
            showToast("数据错误，超出范围");
        }
    }

    @OnClick({R.id.layout_proterty_tot, R.id.btn_read, R.id.btn_send})
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.layout_proterty_tot:
                Intent intent = new Intent(this, WheelActivity.class);
                intent.putExtra(AppString.WheelTypeKey, WheelUtils.type_protertyTot);
                intent.putExtra(AppString.WheelTypeValue, mProtertyData.getTotTime());
                startActivityForResult(intent, activity_prterty_tot);
                break;
            case R.id.btn_read:
//                testCmd();
                if (!dbin.isLink()) {
                    showToast(R.string.noLink);
                    return;
                }
                if (dbin.write(CmdPackage.getProterty())) {
                    btnEnable(false);
                }
                break;
            case R.id.btn_send:
                if (!dbin.isLink()) {
                    showToast(R.string.noLink);
                    return;
                }
                mProtertyWriteData = dbin.getProtertyData();
                mProtertyWriteData.setVox(mSpinnerProtertyVOX.getSelectedItemPosition());
                //mProtertyWriteData.setHFvalue(mSpinnerProtertyVHF.getSelectedItemPosition());
                mProtertyWriteData.setTotTime(Integer.parseInt(mTvProtertyTot.getText().toString()));
                mProtertyWriteData.setActivityChannelId(mSpinnerChannel.getSelectedItemPosition());
                if (dbin.write(CmdPackage.setProteries(mProtertyWriteData))) {
                    btnEnable(false);
                }
                break;
        }
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
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

    @Override
    public void onReceiver(int type, int i) {
        super.onReceiver(type, i);
        switch (type) {
            case CmdPackage.Cmd_type_property:
                mProtertyData = dbin.getProtertyData();
                initData();
                btnEnable(true);
                break;
        }
    }

    private void testCmd() {
        if (AppConstants.isDemo) {
            byte[] cmd6 = new byte[]{0x01, 0x02, 0x01, (byte) 0xB4, 0x3, 0x31, 0x32, 0x33, 0x34, 0x35, 0x36, 0x00, 0x00};
            dbin.getMParse().parseData(cmd6);

            byte[] cmd1 = new byte[]{0x01, 0x01, 0x5A, (byte) 0x57, 0x52, 0x30, 0x31, 0x30, 0x31, 0x32, 0x33, 0x34, 0x35, 0x36, 0x37, 0x37, 0x38, 0x38};
            dbin.getMParse().parseData(cmd1);
        }
    }

    private void btnEnable(boolean enable) {
        btnSend.setEnabled(enable);
        btnRead.setEnabled(enable);
    }
}
