package com.interphone.activity;

import android.content.Context;
import android.os.Bundle;
import android.view.View;
import android.view.inputmethod.InputMethodManager;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.TextView;
import butterknife.Bind;
import butterknife.ButterKnife;
import butterknife.OnClick;
import com.example.administrator.interphone.R;
import com.interphone.AppApplication;
import com.interphone.adapter.StringAdapter;
import com.interphone.bean.DeviceBean;
import com.interphone.bean.PowerTestData;
import com.interphone.connection.agreement.CmdPackage;
import java.util.ArrayList;
import java.util.List;

public class DevicePowerTestActivity extends BaseActivity {

    @Bind(R.id.tv_title) TextView mTvTitle;
    @Bind(R.id.spinner_power_id) Spinner mSpinnerPowerIndex; //低中高
    @Bind(R.id.spinner_power_VHF) Spinner mSpinnerPowerVHF; //第一组 第二组 第三组
    @Bind(R.id.et_power_low) EditText mEtPowerLow;
    @Bind(R.id.et_power_middle) EditText mEtPowerMiddle;
    @Bind(R.id.et_power_height) EditText mEtPowerHeight;
    @Bind(R.id.btn_read) Button mBtnRead;
    @Bind(R.id.btn_send) Button mBtnSend;
    @Bind(R.id.cb_hf) CheckBox mCbHf; //UHF 或VHF

    private StringAdapter mAdapterPowerVHF;
    private StringAdapter mAdapterPowerIndex; //低中高
    private String[] mArrayPowerVHF;
    private String[] mArrayPowerIndex;       //低中高

    private DeviceBean dbin;
    private PowerTestData mPowerTestData;
    private List<PowerTestData> mList = new ArrayList<>();

    @Override protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_device_power_test);
        ButterKnife.bind(this);
        initBaseViews(this);
        initViews();
        initPowerData();
    }

    private void initViews() {
        dbin = ((AppApplication) getApplication()).getDbin();
        mList = new ArrayList<>();
        mList.addAll(dbin.getListPower());

        mArrayPowerIndex = getResources().getStringArray(R.array.PowerId);
        mAdapterPowerIndex = new StringAdapter(mArrayPowerIndex, this, 0);
        mSpinnerPowerIndex.setAdapter(mAdapterPowerIndex);
        mAdapterPowerIndex.notifyDataSetChanged();
        initSpinnerVHF(mCbHf.isChecked());
        mCbHf.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                initSpinnerVHF(isChecked);
            }
        });

        mSpinnerPowerIndex.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                mAdapterPowerIndex.updateIndex(position);
            }

            @Override public void onNothingSelected(AdapterView<?> parent) {

            }
        });
        mSpinnerPowerVHF.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                mAdapterPowerVHF.updateIndex(position);
                mEtPowerLow.requestFocus();
                initPowerData();
            }

            @Override public void onNothingSelected(AdapterView<?> parent) {

            }
        });
    }

    private void initSpinnerVHF(boolean isUHF) {
        if (isUHF) {
            mArrayPowerVHF =
                    new String[] { "409.025", "435.025", "469.025" };
        } else {
            mArrayPowerVHF =
                    new String[] { "142.025", "155.025", "172.025" };
        }
        mAdapterPowerVHF = new StringAdapter(mArrayPowerVHF, this, 0);
        mSpinnerPowerVHF.setAdapter(mAdapterPowerVHF);
        mAdapterPowerVHF.notifyDataSetChanged();

        PowerTestData mPowerTestData;
        //第一组 的id值 是   123 （取决于低中高功率选项）
        //第二组 id值是  456 （取决于 低中高功率选项）
        for (int i=0; i< mList.size() ; i++) {
            mPowerTestData = mList.get(i);
            //+1是因为 ，都是从0 开始
            int id = (i+1) * (mSpinnerPowerIndex.getSelectedItemPosition()+1 );
            if (! mCbHf.isChecked()) {
                id =id+9;
            }
            mPowerTestData.setPowerId( id );
        }
    }

    private void initPowerData() {
        try {
            mPowerTestData = mList.get(mSpinnerPowerVHF.getSelectedItemPosition());
            mEtPowerHeight.setText("" + mPowerTestData.getPowerHigh());
            mEtPowerMiddle.setText("" + mPowerTestData.getPowerMiddle());
            mEtPowerLow.setText("" + mPowerTestData.getPowerLow());

            //1 2 3表示 低中高功率，
            int value = mPowerTestData.getPowerId()%3;
            //余值 为  1 2 0 ， 但是对应下表 应该是  0  1 2
            int position = value ==0 ? 2 : (value-1);
            mSpinnerPowerIndex.setSelection(position, true);
            mAdapterPowerIndex.updateIndex(position);
        } catch (IndexOutOfBoundsException e) {
            e.printStackTrace();
            showToast("数据错误，超出范围");
        }
    }

    @OnClick({ R.id.btn_read, R.id.btn_send , R.id.btn_save}) public void onClick(View view) {
        switch (view.getId()) {
            case R.id.btn_read:
                if (!dbin.isLink()) {
                    showToast(R.string.noLink);
                    return;
                }
                dbin.write(CmdPackage.getPower());
                break;
            case R.id.btn_save:
                savePower();
                break;
            case R.id.btn_send:
                if (!dbin.isLink()) {
                    showToast(R.string.noLink);
                    return;
                }
                //dbin.writeNoEncrypt(CmdPackage.getCmdSuccess());
                dbin.write(CmdPackage.setPowerTest(mList));
                break;
        }
    }

    @Override public void onReceiver(int type, int i) {
        super.onReceiver(type, i);
        switch (type) {
            case CmdPackage.Cmd_type_power:
                mList.clear();
                mList.addAll(dbin.getListPower());
                mCbHf.setChecked(dbin.getListPower().get(0).isUhf());
                initPowerData();
                break;
        }
    }

    private void savePower() {
        PowerTestData powerTestData = mList.get(mSpinnerPowerVHF.getSelectedItemPosition());
        try {
            powerTestData.setPowerHigh(checkValue(mEtPowerHeight));
            powerTestData.setPowerMiddle(checkValue(mEtPowerMiddle));
            powerTestData.setPowerLow(checkValue(mEtPowerLow));
            int position = mSpinnerPowerVHF.getSelectedItemPosition();
            int idPosition = mSpinnerPowerIndex.getSelectedItemPosition(); //第x组数据
            int positionId =1;
            if(mCbHf.isChecked()) {
                positionId = (position + 1) * (idPosition + 1); //下标是从0开始， 所以都要默认+1
            } else {
                positionId = 9 +(position + 1) * (idPosition + 1); //uhf 是1-9  vhf 10-18
            }
            powerTestData.setPowerId(positionId);
            showToast(R.string.save);

            InputMethodManager imm = (InputMethodManager) getSystemService(Context.INPUT_METHOD_SERVICE);
            if (imm != null) {//关闭软键盘
                imm.hideSoftInputFromWindow(getWindow().getDecorView().getWindowToken(), 0);
            }
        } catch (Exception e) {
            e.printStackTrace();
            showToast(R.string.date_error);
        }
    }

    private int checkValue(EditText et) {
        int value = Integer.parseInt(et.getText().toString());
        if (value > 255) {
            value = 255;
        }
        et.setText("" + value);
        return value;
    }
}
