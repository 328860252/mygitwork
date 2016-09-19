package tsocket.zby.com.tsocket.activity;

import android.app.Activity;
import android.os.Bundle;
import android.support.annotation.StringRes;
import android.view.View;
import android.widget.Toast;
import rx.Subscription;
import tsocket.zby.com.tsocket.R;
import tsocket.zby.com.tsocket.utils.RxBus;

public class BaseActivity extends Activity {

  /**
   * Rx广播替代
   */
  Subscription mRxSubscription;

  private Toast mToast;

  @Override protected void onCreate(Bundle savedInstanceState) {
    super.onCreate(savedInstanceState);
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
}
