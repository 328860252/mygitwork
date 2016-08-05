package com.interphone.activity;

import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import butterknife.Bind;
import butterknife.ButterKnife;
import butterknife.OnClick;
import com.example.administrator.interphone.R;
import com.interphone.AppApplication;
import com.interphone.AppConstants;
import com.interphone.bean.DeviceBean;
import com.interphone.connection.agreement.CmdPackage;
import java.io.UnsupportedEncodingException;

public class DeviceSmsActivity extends BaseActivity {

    @Bind(R.id.tv_title) TextView mTvTitle;
    @Bind(R.id.btn_read) Button mBtnRead;
    @Bind(R.id.btn_send) Button mBtnSend;
    @Bind(R.id.etSms) EditText mEtSms;
    @Bind(R.id.et_sms_receiver) EditText mEtSmsReceiver;

    private DeviceBean dbin;

    @Override protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_sms);
        ButterKnife.bind(this);
        initBaseViews(this);
        mTvTitle.setText(R.string.text_sms);
        dbin = ((AppApplication) getApplication()).getDbin();
    }

    @OnClick({ R.id.layout_title_left, R.id.btn_read, R.id.btn_send })
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.layout_title_left:
                finish();
                break;
            case R.id.btn_read:
                if (!dbin.isLink()) {
                    showToast(R.string.noLink);
                    return;
                }
                dbin.write(CmdPackage.getSms());
                break;
            case R.id.btn_send:
                if (!dbin.isLink()) {
                    showToast(R.string.noLink);
                    return;
                }
                String receiver = mEtSmsReceiver.getText().toString();
                if(receiver.length()<6) {
                    showToast(R.string.text_sms_receiver_error);
                    return ;
                }
                String sms =mEtSms.getText().toString().trim();
                try {
                    if(sms.getBytes(AppConstants.charSet).length > AppConstants.sms_length) {
                        showToast(R.string.text_sms_tolong);
                        return ;
                    }
                    if (dbin.getUserId()==null) {
                        dbin.write(CmdPackage.setSms(receiver, dbin.getUserId(), mEtSms.getText().toString()));
                    } else {
                        AppConstants.isWriteACK = true;
                        dbin.write(CmdPackage.getInfo());
                    }
                } catch (UnsupportedEncodingException e) {
                    e.printStackTrace();
                }
                break;
        }
    }

    @Override public void onReceiver(int type, int i) {
        super.onReceiver(type, i);
        switch(type) {
            case CmdPackage.Cmd_type_sms:
                mEtSms.setText(dbin.getSms().trim());
                break;
        }
    }
}
