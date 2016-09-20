package tsocket.zby.com.tsocket.activity;

import android.content.Intent;
import android.os.Bundle;
import tsocket.zby.com.tsocket.AppConstants;
import tsocket.zby.com.tsocket.AppString;
import tsocket.zby.com.tsocket.utils.SharedPerfenceUtils;

public class LogoActivity extends BaseActivity {

  @Override protected void onCreate(Bundle savedInstanceState) {
    super.onCreate(savedInstanceState);
    new Thread(new Runnable() {
      @Override public void run() {
        try {
          Thread.sleep(AppConstants.DELAY_TIME);
        } catch (InterruptedException e) {
          e.printStackTrace();
        }
        Intent intent;
        if (SharedPerfenceUtils.getSetupData(LogoActivity.this)
            .readBoolean(AppString.FIRST, true)) {
          intent = new Intent(LogoActivity.this, GuideViewActivity.class);
        } else {
          intent = new Intent(LogoActivity.this, DeviceListActivity.class);
        }
        startActivity(intent);
        finish();
      }
    }).start();
  }
}
