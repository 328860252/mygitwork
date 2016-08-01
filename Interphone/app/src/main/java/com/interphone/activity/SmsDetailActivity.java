package com.interphone.activity;

import android.os.Bundle;
import android.widget.TextView;
import butterknife.Bind;
import butterknife.ButterKnife;
import com.example.administrator.interphone.R;
import com.interphone.bean.SmsEntity;
import com.interphone.utils.StringUtils;

public class SmsDetailActivity extends BaseActivity {

  @Bind(R.id.textView_time) TextView mTextViewTime;
  @Bind(R.id.textView_content) TextView mTextViewContent;
  private SmsEntity mEntity;

  @Override protected void onCreate(Bundle savedInstanceState) {
    super.onCreate(savedInstanceState);
    setContentView(R.layout.activity_sms_detail);
    ButterKnife.bind(this);
    initBaseViews(this);
    initViews();
  }

  private void initViews() {
    mEntity = (SmsEntity) getIntent().getSerializableExtra("sms");
    if( mEntity!=null) {
      mTextViewContent.setText(mEntity.getContent());
      mTextViewTime.setText(StringUtils.timeFormat(mEntity.getDataTime()));
    }
  }
}
