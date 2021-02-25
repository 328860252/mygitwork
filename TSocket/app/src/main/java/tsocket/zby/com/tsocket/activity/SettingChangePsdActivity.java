package tsocket.zby.com.tsocket.activity;

import android.os.Bundle;
import android.widget.Button;
import android.widget.EditText;
import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import tsocket.zby.com.tsocket.AppConstants;
import tsocket.zby.com.tsocket.R;
import tsocket.zby.com.tsocket.bean.DeviceBean;
import tsocket.zby.com.tsocket.connection.agreement.CmdPackage;

public class SettingChangePsdActivity extends BaseActivity {

  @BindView(R.id.et_psd) EditText mEtPsd;
  @BindView(R.id.et_psd_verify) EditText mEtPsdVerify;
  @BindView(R.id.btn_confirm) Button mBtnConfirm;
  private DeviceBean mDeviceBean;

  @Override protected void onCreate(Bundle savedInstanceState) {
    super.onCreate(savedInstanceState);
    setContentView(R.layout.activity_setting_psd);
    ButterKnife.bind(this);
    mDeviceBean = mApp.getDeviceBean();
  }

  @OnClick(R.id.layout_title_left) public void onBack() {
    finish();
  }

  @Override protected void onLanguageChange() {
    super.onLanguageChange();
  }

  @Override protected void onStart() {
    super.onStart();
  }

  @Override protected void onStop() {
    super.onStop();
  }

  @OnClick(R.id.btn_confirm) public void onClick() {
    final  String newPsd = mEtPsd.getText().toString().trim();
    String psd2 = mEtPsdVerify.getText().toString().trim();
    if (newPsd.length()!=6) {
      showToast(R.string.toast_password_min6);
      return;
    }
    if (!newPsd.equals(psd2)) {
      showToast(R.string.toast_psd_notequals);
      return;
    }
    new Thread(new Runnable() {
      @Override public void run() {
        mDeviceBean.writeNoEncrypt(CmdPackage.setPassword(newPsd));
        try {
          Thread.sleep(1000);
        } catch (InterruptedException e) {
          e.printStackTrace();
        }
        mDeviceBean.writeNoEncrypt(CmdPackage.setReboot());
        finish();
      }
    }).start();
  }
}
