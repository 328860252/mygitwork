package tsocket.zby.com.tsocket.activity;

import android.os.Bundle;
import android.text.TextUtils;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import java.io.UnsupportedEncodingException;
import tsocket.zby.com.tsocket.AppConstants;
import tsocket.zby.com.tsocket.R;
import tsocket.zby.com.tsocket.bean.DeviceBean;
import tsocket.zby.com.tsocket.connection.agreement.CmdPackage;

public class SettingChangeNameActivity extends BaseActivity {

  @BindView(R.id.tv_name) TextView mTvName;
  @BindView(R.id.et_name_new) EditText mEtNameNew;
  @BindView(R.id.btn_confirm) Button mBtnConfirm;
  private DeviceBean mDeviceBean;

  @Override protected void onCreate(Bundle savedInstanceState) {
    super.onCreate(savedInstanceState);
    setContentView(R.layout.activity_setting_name);
    ButterKnife.bind(this);
    mDeviceBean = mApp.getDeviceBean();
    String str = getString(R.string.text_oldName);
    mTvName.setText(String.format(str, mDeviceBean.getName()));
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

  @OnClick(R.id.btn_confirm) public void onClick() {
    final String name = mEtNameNew.getText().toString();
    try {
      byte[] nameBuff = name.getBytes(AppConstants.charset);
      if (TextUtils.isEmpty(name.trim())) {
        showToast(R.string.toast_name_enmpty);
        return;
      }
      if (nameBuff.length > 20) {
        showToast(R.string.toast_name_tolong);
        return;
      }
      new Thread(new Runnable() {
        @Override public void run() {
          byte[] buff = CmdPackage.setName(name);
          try {
            mDeviceBean.writeNoEncrypt(buff);
            mDeviceBean.setName(name);
            Thread.sleep(AppConstants.SEND_TIME_DEALY);
            mDeviceBean.writeNoEncrypt(CmdPackage.setReboot());
            finish();
          } catch (InterruptedException e) {
            e.printStackTrace();
          }
        }
      }).start();
    } catch (UnsupportedEncodingException e) {
      e.printStackTrace();
    }
  }
}
