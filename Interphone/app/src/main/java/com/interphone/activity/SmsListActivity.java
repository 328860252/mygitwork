package com.interphone.activity;

import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.ListView;
import butterknife.Bind;
import butterknife.ButterKnife;
import butterknife.OnClick;
import com.example.administrator.interphone.R;
import com.interphone.AppApplication;
import com.interphone.adapter.SmsAdapter;
import com.interphone.bean.DeviceBean;
import com.interphone.bean.SmsEntity;
import com.interphone.connection.agreement.CmdPackage;
import com.interphone.database.SmsDao;
import java.util.List;

public class SmsListActivity extends BaseActivity {

  SmsDao mSmsDao;
  List<SmsEntity> mSmsEntityList;
  SmsAdapter mSmsAdapter;
  @Bind(R.id.listView) ListView mListView;
  @Bind(R.id.btn_read) Button mBtnRead;

  private DeviceBean dbin;

  @Override protected void onCreate(Bundle savedInstanceState) {
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

  @OnClick(R.id.btn_read) public void onClick() {
    if (dbin !=null) {
      if (!dbin.isLink()) {
        showToast(R.string.noLink);
        return;
      }
      showToast("开始读取");
      dbin.write(CmdPackage.getSms());
    }
  }

  @Override public void onReceiver(int type, int i) {
    switch(type) {
      case CmdPackage.Cmd_type_sms:
        showToast("读取完毕");
        mSmsEntityList = mSmsDao.queryReceiverLast100("");
        mSmsAdapter.setList(mSmsEntityList);
        mSmsAdapter.notifyDataSetChanged();
        break;
    }
    super.onReceiver(type, i);
  }
}
