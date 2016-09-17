package com.interphone.activity;

import android.content.Intent;
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
import com.interphone.bean.SmsEntity;
import com.interphone.connection.agreement.CmdPackage;
import com.interphone.database.SmsDao;
import com.interphone.utils.StringUtils;
import java.io.UnsupportedEncodingException;

public class DeviceSmsActivity extends BaseActivity {

    @Bind(R.id.tv_title)
    TextView mTvTitle;
    @Bind(R.id.btn_read_list)
    Button mBtnReadList;
    @Bind(R.id.btn_send_list)
    Button mBtnSendList;
    @Bind(R.id.btn_send)
    Button mBtnSend;
    @Bind(R.id.etSms)
    EditText mEtSms;
    @Bind(R.id.et_sms_receiver)
    EditText mEtSmsReceiver;

    private SmsDao mSmsDao;

    private DeviceBean dbin;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_sms);
        ButterKnife.bind(this);
        initBaseViews(this);
        mTvTitle.setText(R.string.text_sms);
        dbin = ((AppApplication) getApplication()).getDbin();
    }

    @OnClick({R.id.layout_title_left, R.id.btn_read_list, R.id.btn_send_list, R.id.btn_send})
    public void onClick(View view) {
        Intent intent;
        switch (view.getId()) {
            case R.id.layout_title_left:
                finish();
                break;
            case R.id.btn_read_list:
//                if (!dbin.isLink()) {
//                    showToast(R.string.noLink);
//                    return;
//                }
//                dbin.write(CmdPackage.getSms());
                intent = new Intent(DeviceSmsActivity.this, SmsListActivity.class);
                intent.putExtra("isSend", false);
                startActivity(intent);
                break;
            case R.id.btn_send_list:
                intent = new Intent(DeviceSmsActivity.this, SmsListActivity.class);
                intent.putExtra("isSend", true);
                startActivity(intent);
                break;
            case R.id.btn_send:
                if (!dbin.isLink()) {
                    showToast(R.string.noLink);
                    return;
                }
                String receiver = mEtSmsReceiver.getText().toString();
                if (receiver.length() < 6) {
                    showToast(R.string.text_sms_receiver_error);
                    return;
                }
                String sms = mEtSms.getText().toString().trim();
                try {
                    if (sms.getBytes(AppConstants.charSet).length > AppConstants.sms_length) {
                        showToast(R.string.text_sms_tolong);
                        return;
                    }
                    if (dbin.getProtertyData().getUserId() != null) {
                        SmsEntity smsEntity = new SmsEntity();
                        smsEntity.setContent(sms);
                        smsEntity.setSend(true);
                        smsEntity.setSendId(dbin.getProtertyData().getUserId());
                        smsEntity.setReceiverId(mEtSms.getText().toString().trim());
                        smsEntity.setType(SmsEntity.TYPE_TEXT);
                        smsEntity.setDataTime(StringUtils.getTimeString());

                        if (mSmsDao == null) {
                            mSmsDao = new SmsDao(DeviceSmsActivity.this);
                        }
                        mSmsDao.insert(smsEntity);
                        if (dbin.write(CmdPackage.setSms(smsEntity))) {
                            showSendToast(false);
                        }
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

    @Override
    public void onReceiver(int type, int i) {
        super.onReceiver(type, i);
        switch (type) {
            case CmdPackage.Cmd_type_sms:
                mEtSms.setText(dbin.getSms().trim());
                showSendToast(true);
                break;
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
