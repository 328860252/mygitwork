package com.interphone.activity;

import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.ListView;

import com.example.administrator.interphone.R;
import com.interphone.AppApplication;
import com.interphone.AppConstants;
import com.interphone.adapter.SmsAdapter;
import com.interphone.bean.DeviceBean;
import com.interphone.bean.SmsEntity;
import com.interphone.connection.agreement.CmdPackage;
import com.interphone.database.SmsDao;
import com.interphone.utils.StringUtils;

import java.util.List;

import butterknife.Bind;
import butterknife.ButterKnife;
import butterknife.OnClick;

public class SmsListActivity extends BaseActivity {

    SmsDao mSmsDao;
    List<SmsEntity> mSmsEntityList;
    SmsAdapter mSmsAdapter;
    @Bind(R.id.listView)
    ListView mListView;
    @Bind(R.id.btn_read)
    Button mBtnRead;
    @Bind(R.id.btn_read)
    Button btnRead;

    private DeviceBean dbin;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_sms_list);
        ButterKnife.bind(this);
        initBaseViews(this);
        initView();
    }

    private void initView() {
        dbin = ((AppApplication) getApplication()).getDbin();
        mSmsDao = new SmsDao(this);
        if (getIntent().getBooleanExtra("isSend", true)) {
            mSmsEntityList = mSmsDao.querySendLast100("");
            mBtnRead.setVisibility(View.GONE);
        } else {
            mSmsEntityList = mSmsDao.queryReceiverLast100("");
        }
        mSmsAdapter = new SmsAdapter(this, mSmsEntityList);
        mListView.setAdapter(mSmsAdapter);
        //        mListView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
        //            @Override
        //            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
        //                Intent intent = new Intent(SmsListActivity.this, SmsDetailActivity.class);
        //                intent.putExtra("sms", mSmsEntityList.get(position));
        //                startActivity(intent);
        //            }
        //        });
    }

    @OnClick(R.id.btn_read)
    public void onClick() {
        if (dbin != null) {
            if (!dbin.isLink()) {
                showToast(R.string.noLink);
                return;
            }
            showToast("开始读取");
            if (dbin.write(CmdPackage.getSms())) {
                btnEnable(true);
            }
        }
//        testCmd();
    }

    @Override
    public void onReceiver(int type, int i) {
        switch (type) {
            case CmdPackage.Cmd_type_sms:
                showToast("读取完毕");
                mSmsEntityList = mSmsDao.queryReceiverLast100("");
                mSmsAdapter.setList(mSmsEntityList);
                mSmsAdapter.notifyDataSetChanged();
                btnEnable(true);
                break;
        }
        super.onReceiver(type, i);
    }

    private void testCmd() {
        if (AppConstants.isDemo) {
            SmsEntity smsEntity = new SmsEntity();
            smsEntity.setContent("haha哈哈");
            smsEntity.setType(2);
            smsEntity.setDataTime(StringUtils.getTimeString());
            smsEntity.setReceiverId("123456");
            smsEntity.setSendId("123456");

            dbin.getMParse().parseData(CmdPackage.setSms(smsEntity));

            byte[] cmd1 = new byte[]{0x01, 0x05, 0x02, (byte) 0x00, 0x02, 0x30, 0x31, 0x32, 0x33, 0x32, 0x33, 0x34, 0x35, 0x36, 0x37, 0x07, 0x66, 0x03, 0x04, 0x05, 0x06, 0x07, 0x31, 0x32, 0x33, 0x34};
            dbin.getMParse().parseData(cmd1);
        }
    }

    private void btnEnable(boolean enable) {
        btnRead.setEnabled(enable);
    }
}

