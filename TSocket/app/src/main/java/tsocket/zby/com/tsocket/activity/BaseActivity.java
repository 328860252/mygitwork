package tsocket.zby.com.tsocket.activity;

import android.annotation.TargetApi;
import android.app.Activity;
import android.os.Build;
import android.os.Bundle;
import android.os.health.ServiceHealthStats;
import android.support.annotation.ColorRes;
import android.support.annotation.StringRes;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.Toast;
import com.readystatesoftware.systembartint.SystemBarTintManager;
import rx.Subscription;
import tsocket.zby.com.tsocket.AppApplication;
import tsocket.zby.com.tsocket.AppConstants;
import tsocket.zby.com.tsocket.AppString;
import tsocket.zby.com.tsocket.R;
import tsocket.zby.com.tsocket.utils.RxBus;
import tsocket.zby.com.tsocket.utils.SharedPerfenceUtils;

public class BaseActivity extends Activity {

  protected float phone_density;//屏幕密度
  protected AppApplication mApp;

  /**
   * Rx广播替代
   */
  Subscription mRxSubscription;

  private Toast mToast;

  private int lastLanguageItem = -1;

  @Override protected void onCreate(Bundle savedInstanceState) {
    super.onCreate(savedInstanceState);
    applyKitKatTranslucency(R.color.layout_header);
    //requestWindowFeature(Window.FEATURE_NO_TITLE);
    phone_density = getResources().getDisplayMetrics().density; //屏幕密度
    mApp = (AppApplication) getApplication();
  }

  /**
   * 显示toast消息， 避免多个重复的toast队列显示
   */
  public void showToast(String str) {
    if (mToast==null) {//沉浸式菜单，会导致toast文字偏移，必须使用ApplicationContext
      mToast = Toast.makeText(getApplicationContext(), str, Toast.LENGTH_LONG);
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
    if (lastLanguageItem== -1) {
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

  /**
   * 应用内修改语言，对于已打开的页面，就无法重新修改,所以手动在赋值一次
   */
  protected void onLanguageChange() {}


  /**
   * Apply KitKat specific translucency.
   */
  protected void applyKitKatTranslucency(@ColorRes int statusBarTintResource) {
    // KitKat translucent navigation/status bar.
    if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.KITKAT) {
      setTranslucentStatus(true);
      SystemBarTintManager mTintManager = new SystemBarTintManager(this);
      mTintManager.setStatusBarTintEnabled(true);

      mTintManager.setStatusBarTintResource(statusBarTintResource);//通知栏所需颜色
    }

  }

  @TargetApi(19)
  private void setTranslucentStatus(boolean on) {
    Window win = getWindow();
    WindowManager.LayoutParams winParams = win.getAttributes();
    final int bits = WindowManager.LayoutParams.FLAG_TRANSLUCENT_STATUS;
    if (on) {
      winParams.flags |= bits;
    } else {
      winParams.flags &= ~bits;
    }
    win.setAttributes(winParams);
  }
}
