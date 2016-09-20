package tsocket.zby.com.tsocket.activity;

import android.app.Activity;
import android.os.Bundle;
import android.os.health.ServiceHealthStats;
import android.support.annotation.StringRes;
import android.view.View;
import android.view.Window;
import android.widget.Toast;
import rx.Subscription;
import tsocket.zby.com.tsocket.AppConstants;
import tsocket.zby.com.tsocket.AppString;
import tsocket.zby.com.tsocket.R;
import tsocket.zby.com.tsocket.utils.RxBus;
import tsocket.zby.com.tsocket.utils.SharedPerfenceUtils;

public class BaseActivity extends Activity {

  protected float phone_density;//屏幕密度

  /**
   * Rx广播替代
   */
  Subscription mRxSubscription;

  private Toast mToast;

  private int lastLanguageItem;

  @Override protected void onCreate(Bundle savedInstanceState) {
    super.onCreate(savedInstanceState);
    requestWindowFeature(Window.FEATURE_NO_TITLE);
    phone_density = getResources().getDisplayMetrics().density; //屏幕密度
  }

  /**
   * 显示toast消息， 避免多个重复的toast队列显示
   */
  public void showToast(String str) {
    if (mToast==null) {
      mToast = Toast.makeText(this, str, Toast.LENGTH_LONG);
    }
    mToast.setDuration(Toast.LENGTH_LONG);
    mToast.setText(str);
    mToast.show();
  }

  /**
   * 显示toast
   */
  public void showToast(@StringRes int resId) {
    showToast(getString(resId));
  }

  /**
   * 订阅事件，接收推送消息
   */
  protected void subscribePushMessage() {
    mRxSubscription = RxBus.subscribePushMessage(new RxBus.SubscrieListener() {
      @Override public void subscribeCall(Object object) {
        if(object!=null) {
          onReceiverCmd(object);
        }
      }
    });
  }

  @Override protected void onResume() {
    if (lastLanguageItem==0) {
      lastLanguageItem = SharedPerfenceUtils.getSetupData(this).readInt(AppString.language, AppConstants.language_default);
    } else {
      //跟之前的语言发生了变化
      if (lastLanguageItem != SharedPerfenceUtils.getSetupData(this).readInt(AppString.language, AppConstants.language_default)) {
        onLanguageChange();
      }
    }
    super.onResume();
  }

  /**
   * 取消订阅
   */
  protected void unSubscribePushMessage() {
    RxBus.unSubscribePushMessage(mRxSubscription);
  }

  /**
   * 接收到推送消息
   */
  protected void onReceiverCmd(Object message) {
    if(message==null) return;
  }

  protected void onLanguageChange() {

  }
}
