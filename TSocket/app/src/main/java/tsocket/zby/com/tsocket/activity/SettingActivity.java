package tsocket.zby.com.tsocket.activity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.TextView;
import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import tsocket.zby.com.tsocket.R;
import tsocket.zby.com.tsocket.view.HeaderLayout;

public class SettingActivity extends BaseActivity {

  @BindView(R.id.tv_change_name) TextView mTvChangeName;
  @BindView(R.id.tv_changePsd) TextView mTvChangePsd;
  @BindView(R.id.tv_help) TextView mTvHelp;
  @BindView(R.id.tv_language) TextView mTvLanguage;
  @BindView(R.id.layout_header) HeaderLayout mLayoutHeader;

  @Override protected void onCreate(Bundle savedInstanceState) {
    super.onCreate(savedInstanceState);
    setContentView(R.layout.activity_setting);
    ButterKnife.bind(this);
  }

  @OnClick(R.id.layout_title_right) public void onSave() {
  }

  @OnClick(R.id.layout_title_left) public void onBack() {
    finish();
  }

  @Override protected void onReceiverCmd(Object message) {
    super.onReceiverCmd(message);
  }

  @Override protected void onLanguageChange() {
    super.onLanguageChange();
    mLayoutHeader.setTextTitle(R.string.text_setting);
    mTvChangeName.setText(R.string.text_change_name);
    mTvChangePsd.setText(R.string.text_change_psd);
    mTvHelp.setText(R.string.text_help);
    mTvLanguage.setText(R.string.text_language);
  }

  @Override protected void onStart() {
    super.onResume();
  }

  @Override protected void onStop() {
    super.onStop();
  }

  @OnClick({
      R.id.layout_changeName, R.id.layout_changePsd, R.id.layout_help, R.id.layout_language
  }) public void onClick(View view) {
    Intent intent;
    switch (view.getId()) {
      case R.id.layout_changeName:
        intent = new Intent(this, SettingChangeNameActivity.class);
        startActivity(intent);
        break;
      case R.id.layout_changePsd:
        intent = new Intent(this, SettingChangePsdActivity.class);
        startActivity(intent);
        break;
      case R.id.layout_help:
        intent = new Intent(this, GuideViewActivity.class);
        startActivity(intent);
        break;
      case R.id.layout_language:
        intent = new Intent(this, SettingLanguageActivity.class);
        startActivity(intent);
        break;
    }
  }
}
