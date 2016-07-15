package com.interphone.activity;

import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.widget.AdapterView;
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
import com.interphone.bean.ChannelData;
import com.interphone.bean.DeviceBean;
import com.interphone.connection.agreement.CmdPackage;
import com.interphone.utils.WheelUtils;

public class DeviceChannelDataActivity extends BaseActivity {

    @Bind(R.id.tv_title) TextView mTvTitle;
    @Bind(R.id.spinner_channel) Spinner mSpinnerChannel;
    @Bind(R.id.spinner_channel_type) Spinner mSpinnerChannelType;
    @Bind(R.id.tv_analogToneReceive) TextView mTvAnalogToneReceive;
    @Bind(R.id.tv_analogToneSend) TextView mTvAnalogToneSend;
    @Bind(R.id.spinner_analogToneBand) Spinner mSpinnerAnalogToneBand;
    @Bind(R.id.layout_channel_analog) LinearLayout mLayoutChannelAnalog;
    @Bind(R.id.tv_numberToneLinkmen) TextView mTvNumberToneLinkmen;
    @Bind(R.id.spinner_numberToneSlot) Spinner mSpinnerNumberToneSlot;
    @Bind(R.id.tv_numberToneColor) TextView mTvNumberToneColor;
    @Bind(R.id.layout_channel_number) LinearLayout mLayoutChannelNumber;
    @Bind(R.id.et_rate_receive) EditText mEtRateReceive;
    @Bind(R.id.et_rate_send) EditText mEtRateSend;
    @Bind(R.id.spinner_power) Spinner mSpinnerPower;

    private StringAdapter mAdapterChannelId;
    private String[] mArrayChannelId;
    private StringAdapter mAdapterChannelType;
    private String[] mArrayChannelType;
    private StringAdapter mAdapterAnalogToneBand;
    private String[] mArrayAnalogToneBand;
    private StringAdapter mAdapterNumberToneSlot;
    private String[] mArrayNumberToneSlot;
    private StringAdapter mAdapterPower;
    private String[] mArrayPower;

    private DeviceBean dbin;
    private ChannelData mChannelData; //当前真是值
    private ChannelData mChannelWriteData; //往外写的缓存数据
    private final static int activity_analogToneReceive  = 11;
    private final static int activity_analogToneSend = 12;
    private final static int activity_numberToneColo = 13;

    private int toneReceive , toneSend ;


    @Override protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_device_channel_data);
        ButterKnife.bind(this);
        initBaseViews(this);
        initViews();
        runOnUiThread(new Runnable() {
            @Override public void run() {
                initData();
            }
        });
    }

    public void btn_ack(View v) {
        if(dbin!=null) {
            dbin.writeNoEncrypt(CmdPackage.getCmdSuccess());
        }
    }

    private void initViews() {
        //默认是第一个信道
        dbin = ((AppApplication) getApplication()).getDbin();
        mChannelData = dbin.getChannelDataById(dbin.getProtertyData().getActivityChannelId());
        //channelId, 1~16
        mArrayChannelId = new String[16];
        for (int i = 0; i < 16; i++) {
            mArrayChannelId[i] = "信道" + (i + 1);
        }
        mAdapterChannelId = new StringAdapter(mArrayChannelId, this, 0);
        mSpinnerChannel.setAdapter(mAdapterChannelId);
        mAdapterChannelId.notifyDataSetChanged();

        //信道类型 模拟 和数字
        mArrayChannelType = getResources().getStringArray(R.array.ChannelType);
        mAdapterChannelType = new StringAdapter(mArrayChannelType, this, 0);
        mSpinnerChannelType.setAdapter(mAdapterChannelType);
        mAdapterChannelType.notifyDataSetChanged();

        //宽窄带 1 2
        mArrayAnalogToneBand = getResources().getStringArray(R.array.ChannelAnalogToneBand);
        mAdapterAnalogToneBand = new StringAdapter(mArrayAnalogToneBand, this, 0);
        mSpinnerAnalogToneBand.setAdapter(mAdapterAnalogToneBand);
        mAdapterAnalogToneBand.notifyDataSetChanged();

        mArrayNumberToneSlot = getResources().getStringArray(R.array.ChannelNumberToneSlot);
        mAdapterNumberToneSlot = new StringAdapter(mArrayNumberToneSlot, this, 0);
        mSpinnerNumberToneSlot.setAdapter(mAdapterNumberToneSlot);
        mAdapterNumberToneSlot.notifyDataSetChanged();

        //功率
        mArrayPower = getResources().getStringArray(R.array.ChannelPower);
        mAdapterPower = new StringAdapter(mArrayPower, this, 0);
        mSpinnerPower.setAdapter(mAdapterPower);
        mAdapterPower.notifyDataSetChanged();

        //监听事件
        mSpinnerChannel.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                mChannelData = dbin.getChannelData(position);
                mAdapterChannelId.updateIndex(position);
                mAdapterChannelId.notifyDataSetChanged();
                initData();
            }

            @Override public void onNothingSelected(AdapterView<?> parent) {

            }
        });
        mSpinnerChannelType.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                mAdapterChannelType.updateIndex(position);
                mAdapterChannelType.notifyDataSetChanged();
                mChannelData.setChannelType(position);
                initChannelType();
            }

            @Override public void onNothingSelected(AdapterView<?> parent) {

            }
        });
        mSpinnerNumberToneSlot.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                mAdapterNumberToneSlot.updateIndex(position);
                mAdapterNumberToneSlot.notifyDataSetChanged();
                mChannelData.setNumberToneSlot(position);
            }

            @Override public void onNothingSelected(AdapterView<?> parent) {

            }
        });
        mSpinnerAnalogToneBand.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                mAdapterAnalogToneBand.updateIndex(position);
                mChannelData.setAnalogToneBand(position);
                mAdapterAnalogToneBand.notifyDataSetChanged();
            }

            @Override public void onNothingSelected(AdapterView<?> parent) {

            }
        });
        mSpinnerPower.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                mAdapterPower.updateIndex(position);
                mChannelData.setPower(position);
                mAdapterPower.notifyDataSetChanged();
            }

            @Override public void onNothingSelected(AdapterView<?> parent) {

            }
        });
    }

    private void initData() {
        //设置true ， 是防止初始化时， 执行onitemnSlelectListener
        try {
            mSpinnerChannelType.setSelection(mChannelData.getChannelType(), true);
            mSpinnerPower.setSelection(mChannelData.getPower(), true);
            mEtRateReceive.setText(""+mChannelData.getRateReceive());
            mEtRateSend.setText(""+mChannelData.getRateSend());
            initChannelType();
        } catch (IndexOutOfBoundsException e) {
            e.printStackTrace();
            showToast("数据错误，超出范围");
        }
    }

    /**
     * 初始化信道
     */
    private void initChannelType() {
        if (mSpinnerChannelType.getSelectedItemPosition() == 1) {
            mLayoutChannelAnalog.setVisibility(View.GONE);
            mLayoutChannelNumber.setVisibility(View.VISIBLE);
            mTvNumberToneColor.setText("" + mChannelData.getNumberToneColor());
            mTvNumberToneLinkmen.setText("" + mChannelData.getNumberToneLinkmen());

            mSpinnerNumberToneSlot.setSelection(mChannelData.getNumberToneSlot(), true);
            mAdapterNumberToneSlot.notifyDataSetChanged();
        } else {
            mLayoutChannelAnalog.setVisibility(View.VISIBLE);
            mLayoutChannelNumber.setVisibility(View.GONE);
            mTvAnalogToneReceive.setText(mChannelData.getAnalogToneReceive2Str(this));
            mTvAnalogToneSend.setText(mChannelData.getAnalogToneSend2Str(this));
            mSpinnerAnalogToneBand.setSelection(mChannelData.getAnalogToneBand(), true);
            mAdapterAnalogToneBand.notifyDataSetChanged();
        }
    }

    @OnClick({
            R.id.layout_analogToneReceive, R.id.layout_analogToneSend, R.id.layout_numberLinkMen,
            R.id.layout_numberToneColo, R.id.btn_read, R.id.btn_send })
    public void onClick(View view) {
        Intent intent ;
        switch (view.getId()) {
            case R.id.layout_analogToneReceive:
                intent = new Intent(this, WheelActivity.class);
                intent.putExtra(AppString.WheelTypeKey, WheelUtils.type_analogToneReceive);
                intent.putExtra(AppString.WheelTypeValue, mChannelData.getAnalogToneReceive());
                startActivityForResult(intent, activity_analogToneReceive);
                break;
            case R.id.layout_analogToneSend:
                intent = new Intent(this, WheelActivity.class);
                intent.putExtra(AppString.WheelTypeKey, WheelUtils.type_analogToneSend);
                intent.putExtra(AppString.WheelTypeValue, mChannelData.getAnalogToneSend());
                startActivityForResult(intent, activity_analogToneSend);
                break;
            case R.id.layout_numberLinkMen:
                break;
            case R.id.layout_numberToneColo:
                intent = new Intent(this, WheelActivity.class);
                intent.putExtra(AppString.WheelTypeKey, WheelUtils.type_numberToneColor);
                intent.putExtra(AppString.WheelTypeValue, mChannelData.getNumberToneColor());
                startActivityForResult(intent, activity_numberToneColo);
                break;
            case R.id.btn_read:
                if (!dbin.isLink()) {
                    showToast(R.string.noLink);
                    return;
                }
                //有多条信息返回， 要添加ack收取
                AppConstants.isWriteACK = true;
                dbin.write(CmdPackage.getChannel());
                break;
            case R.id.btn_send:
                if (!dbin.isLink()) {
                    showToast(R.string.noLink);
                    return;
                }
                mChannelWriteData = dbin.getChannelData(mSpinnerChannel.getSelectedItemPosition());
				
                try {
                    String rateReceiveStr = mEtRateReceive.getText().toString();
                    String rateSendStr = mEtRateSend.getText().toString();

                    double finalRate = checkRate(rateReceiveStr);
                    mChannelWriteData.setRateReceive(finalRate);
                    mEtRateReceive.setText(""+finalRate);

                    finalRate = checkRate(rateSendStr);
                    mChannelWriteData.setRateSend(finalRate);
                    mEtRateSend.setText(""+finalRate);

                    mChannelWriteData.setChannelType(mSpinnerChannelType.getSelectedItemPosition());
                    mChannelWriteData.setPower(mSpinnerPower.getSelectedItemPosition());
                    //mChannelData.setRateSend(mEtRateSend.getText().toString());
                    //mChannelData.setRateReceive(mEtRateReceive.getText().toString());
                    if(mChannelData.isNumberTone()) {
                        mChannelWriteData.setNumberToneColor(Integer.parseInt(mTvNumberToneColor.getText().toString()));
                        mChannelWriteData.setNumberToneSlot((Integer) mSpinnerNumberToneSlot.getSelectedItem());
                    } else {
                        mChannelWriteData.setAnalogToneBand(mSpinnerAnalogToneBand.getSelectedItemPosition());
                        mChannelWriteData.setAnalogToneSend(toneSend);
                        mChannelWriteData.setAnalogToneReceive(toneReceive);
                    }
                    dbin.write(CmdPackage.setChannel(mChannelWriteData));
                } catch (Exception e) {
                    showToast(R.string.text_data_error);
                    e.printStackTrace();
                }
                break;
        }
    }

    @Override protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        String value;
        int position;
        switch(requestCode) {
            case activity_analogToneReceive:
                if(resultCode == RESULT_OK) {
                    toneReceive = data.getIntExtra(AppString.WheelTypeItem, 0);
                    value = data.getStringExtra(AppString.WheelTypeValue);
                    mTvAnalogToneReceive.setText(value);
                }
                break;
            case activity_analogToneSend:
                if(resultCode == RESULT_OK) {
                    toneSend = data.getIntExtra(AppString.WheelTypeItem, 0);
                    value = data.getStringExtra(AppString.WheelTypeValue);
                    mTvAnalogToneSend.setText(value);
                }
                break;
            case activity_numberToneColo:
                if(resultCode == RESULT_OK) {
                    position = data.getIntExtra(AppString.WheelTypeItem, 0);
                    mTvNumberToneColor.setText(""+position);
                }
                break;
        }
        super.onActivityResult(requestCode, resultCode, data);
    }

    @Override public void onReceiver(int type, int i) {
        super.onReceiver(type, i);
        switch(type) {
            case CmdPackage.Cmd_type_channel:
                mChannelData = dbin.getChannelData(mSpinnerChannel.getSelectedItemPosition());
                initData();
                break;
        }
    }

    public  double checkRate(String rateDouble){
        int value  = 1000000;
        //扩大一百万倍
        int dd = (int) (Double.parseDouble(rateDouble) * value);
        if ( dd % 500 ==0) { //能被0.0005整除，扩大就变500

        } else { //输入的小数
            int va = dd % 625;
            dd = dd - va; //取0.000625的倍数， 扩大后未 625
        }
        if ( dd < 136*value) {
            dd = 142025000;
        } else if ( dd > 400 * value && dd < 470* value) { //136-174 400-470
        } else if (dd <174*value){
        } else {
            dd = 462562500;
        }
        return dd/1000000.0d;
    }

    public int toneCase(String str) {
        if (TextUtils.isEmpty(str)) {
            return 0;
        }
        if( str.equalsIgnoreCase("OFF")) {
            return 0;
        } else {
            return Integer.parseInt(str);
        }
    }
}
