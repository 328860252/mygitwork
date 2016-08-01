package com.interphone.activity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ListView;
import butterknife.Bind;
import butterknife.ButterKnife;
import com.example.administrator.interphone.R;
import com.interphone.adapter.SmsAdapter;
import com.interphone.bean.SmsEntity;
import com.interphone.database.SmsDao;
import java.util.List;

public class SmsListActivity extends BaseActivity {

  SmsDao mSmsDao;
  List<SmsEntity> mSmsEntityList;
  SmsAdapter mSmsAdapter;
  @Bind(R.id.listView) ListView mListView;

  @Override protected void onCreate(Bundle savedInstanceState) {
    super.onCreate(savedInstanceState);
    setContentView(R.layout.activity_sms_list);
    ButterKnife.bind(this);
    initBaseViews(this);
    initView();
  }

  private void initView() {
    mSmsDao = new SmsDao(this);
    mSmsEntityList = mSmsDao.querySendLast100();
    mSmsAdapter = new SmsAdapter(this, mSmsEntityList);
    mListView.setAdapter(mSmsAdapter);
    mListView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
      @Override public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
        Intent intent = new Intent(SmsListActivity.this, SmsDetailActivity.class);
        intent.putExtra("sms", mSmsEntityList.get(position));
        startActivity(intent);
      }
    });
  }

}
