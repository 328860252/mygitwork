package tsocket.zby.com.tsocket.activity;

import android.os.Bundle;
//import android.support.v7.app.AppCompatActivity;
import butterknife.ButterKnife;
import butterknife.OnClick;
//import com.crashlytics.android.Crashlytics;
//import io.fabric.sdk.android.Fabric;
import tsocket.zby.com.tsocket.AppString;
import tsocket.zby.com.tsocket.R;
import tsocket.zby.com.tsocket.connection.ble.BleManager;
import tsocket.zby.com.tsocket.utils.Tools;

public class MainActivity extends BaseActivity {

  @Override protected void onCreate(Bundle savedInstanceState) {
    super.onCreate(savedInstanceState);
    setContentView(R.layout.activity_main);
    ButterKnife.bind(this);
    BleManager.getInstance(this).startScan(true);
  }

  @OnClick(R.id.layout_title_right) public void onSave() {
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
}
