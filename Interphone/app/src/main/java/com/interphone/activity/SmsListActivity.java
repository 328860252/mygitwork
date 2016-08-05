package com.interphone.activity;

import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.ListView;

import com.example.administrator.interphone.R;
import com.interphone.AppApplication;
import com.interphone.adapter.SmsAdapter;
import com.interphone.bean.DeviceBean;
import com.interphone.bean.SmsEntity;
import com.interphone.database.SmsDao;

import java.util.List;

import butterknife.Bind;
import butterknife.ButterKnife;

public class SmsListActivity extends BaseActivity {

    SmsDao mSmsDao;
    List<SmsEntity> mSmsEntityList;
    SmsAdapter mSmsAdapter;
    @Bind(R.id.listView)
    ListView mListView;
    @Bind(R.id.btn_read)
    Button btnRead;

    private DeviceBean deviceBean;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_sms_list);
        ButterKnife.bind(this);
        initBaseViews(this);
        initView();
    }

    private void initView() {
        deviceBean = ((AppApplication) getApplication()).getDbin();
        mSmsDao = new SmsDao(this);
        if (getIntent().getBooleanExtra("isSend", true)) {
            mSmsEntityList = mSmsDao.querySendLast100("");
            btnRead.setVisibility(View.GONE);
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



}
