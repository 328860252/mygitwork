package com.interphone.activity;

import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.ListView;
import android.widget.TextView;

import com.example.administrator.interphone.R;
import com.interphone.AppApplication;
import com.interphone.adapter.PowerTestAdapter;
import com.interphone.bean.DeviceBean;
import com.interphone.bean.PowerTestData;
import com.interphone.bean.PowerTestSeekBean;
import com.interphone.connection.agreement.CmdPackage;

import java.util.List;

import butterknife.Bind;
import butterknife.ButterKnife;
import butterknife.OnClick;

/**
 * 修改滑动条 调频
 */
public class DevicePowerTestActivity2 extends BaseActivity {

    @Bind(R.id.tv_title)
    TextView mTvTitle;
    @Bind(R.id.cb_hf)
    CheckBox mCbHf; //UHF 或VHF
    @Bind(R.id.listView)
    ListView mListView;
    @Bind(R.id.btn_read)
    Button btnRead;
    @Bind(R.id.btn_send)
    Button btnSend;

    private PowerTestAdapter mPowerTestAdapter;
    private DeviceBean dbin;
    private PowerTestData mPowerTestData;
    private List<PowerTestSeekBean> mList;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_device_power_test2);
        ButterKnife.bind(this);
        initBaseViews(this);
        initViews();
    }

    private void initViews() {
        dbin = ((AppApplication) getApplication()).getDbin();

        mPowerTestAdapter = new PowerTestAdapter(this);
        mList = mPowerTestAdapter.getListCleanValue(mCbHf.isChecked());
        mPowerTestAdapter.setList(mList);
        mListView.setAdapter(mPowerTestAdapter);

        mPowerTestAdapter.setOnScroolListener(new PowerTestAdapter.OnScroolListener() {
            @Override
            public void onSeekbarScroll(int position, int id, int value) {
                if (!dbin.isLink()) {
                    showToast(R.string.noLink);
                    return;
                }
                PowerTestData powerTestData = dbin.getPowerData(position / 3);
                powerTestData.setPowerId(id);
                if (position % 3 == 0) {
                    powerTestData.setPowerLow(value);
                } else if (position % 3 == 1) {
                    powerTestData.setPowerMiddle(value);
                } else {
                    powerTestData.setPowerHigh(value);
                }
                dbin.write(CmdPackage.setPowerTest(powerTestData));
            }
        });
        mCbHf.setChecked(dbin.getListPower().get(0).isUhf());
        initSpinnerVHF(mCbHf.isChecked());
        mCbHf.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                initSpinnerVHF(isChecked);
            }
        });
    }


    private void initSpinnerVHF(boolean isUHF) {
        mList = mPowerTestAdapter.getListCleanValue(isUHF);
        mPowerTestAdapter.setList(mList);
        mPowerTestAdapter.notifyDataSetChanged();
        if (isUHF) {
            mCbHf.setText(R.string.text_UHF);
        } else {
            mCbHf.setText(R.string.text_VHF);
        }

    }


    @OnClick({R.id.btn_read, R.id.btn_send})
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.btn_read:
                if (!dbin.isLink()) {
                    showToast(R.string.noLink);
                    return;
                }
                if (dbin.write(CmdPackage.getPower())) {
                    btnEnable(false);
                }
                break;
            case R.id.btn_send:
                if (!dbin.isLink()) {
                    showToast(R.string.noLink);
                    return;
                }
                //dbin.writeNoEncrypt(CmdPackage.getCmdSuccess());
                if (dbin.write(CmdPackage.setPowerTest(dbin.getListPower()))) {
                    btnEnable(false);
                }
                break;
        }
    }

    @Override
    public void onReceiver(int type, int i) {
        super.onReceiver(type, i);
        switch (type) {
            case CmdPackage.Cmd_type_power:
                mCbHf.setChecked(dbin.getListPower().get(0).isUhf());
                mList = mPowerTestAdapter.getList(dbin, mCbHf.isChecked());
                mPowerTestAdapter.setList(mList);
                mPowerTestAdapter.notifyDataSetChanged();
                btnEnable(true);
                break;
        }
    }

    private void btnEnable(boolean enable) {
        btnSend.setEnabled(enable);
        btnRead.setEnabled(enable);
    }

    //private void savePower() {
    //  PowerTestData powerTestData = mList.get(mSpinnerPowerVHF.getSelectedItemPosition());
    //  try {
    //    powerTestData.setPowerHigh(checkValue(mEtPowerHeight));
    //    powerTestData.setPowerMiddle(checkValue(mEtPowerMiddle));
    //    powerTestData.setPowerLow(checkValue(mEtPowerLow));
    //    int position = mSpinnerPowerVHF.getSelectedItemPosition();
    //    int idPosition = mSpinnerPowerIndex.getSelectedItemPosition(); //第x组数据
    //    int positionId = 1;
    //    if (mCbHf.isChecked()) {
    //      positionId = (position + 1) * (idPosition + 1); //下标是从0开始， 所以都要默认+1
    //    } else {
    //      positionId = 9 + (position + 1) * (idPosition + 1); //uhf 是1-9  vhf 10-18
    //    }
    //    powerTestData.setPowerId(positionId);
    //    showToast(R.string.save);
    //
    //    InputMethodManager imm = (InputMethodManager) getSystemService(Context.INPUT_METHOD_SERVICE);
    //    if (imm != null) {//关闭软键盘
    //      imm.hideSoftInputFromWindow(getWindow().getDecorView().getWindowToken(), 0);
    //    }
    //  } catch (Exception e) {
    //    e.printStackTrace();
    //    showToast(R.string.date_error);
    //  }
    //}

    //private int checkValue(EditText et) {
    //  int value = Integer.parseInt(et.getText().toString());
    //  if (value > 255) {
    //    value = 255;
    //  }
    //  et.setText("" + value);
    //  return value;
    //}
}
