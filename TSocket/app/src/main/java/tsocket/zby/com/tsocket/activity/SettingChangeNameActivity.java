package tsocket.zby.com.tsocket.activity;

import android.os.Bundle;
import android.widget.EditText;
import android.widget.TextView;
import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import tsocket.zby.com.tsocket.R;
import tsocket.zby.com.tsocket.bean.DeviceBean;

public class SettingChangeNameActivity extends BaseActivity {

  @BindView(R.id.tv_name) TextView mTvName;
  @BindView(R.id.et_name_new) EditText mEtNameNew;
  private DeviceBean mDeviceBean;

  @Override protected void onCreate(Bundle savedInstanceState) {
    super.onCreate(savedInstanceState);
    setContentView(R.layout.activity_setting_name);
    ButterKnife.bind(this);
    mDeviceBean = mApp.getDeviceBean();
    mTvName.setText(mDeviceBean.getName());
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
  }

  @Override protected void onStart() {
    super.onResume();
  }

  @Override protected void onStop() {
    super.onStop();
  }
}
