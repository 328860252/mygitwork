package tsocket.zby.com.tsocket.activity;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.view.Gravity;
import android.view.View;
import android.view.Window;
import android.view.WindowManager.LayoutParams;
import android.widget.LinearLayout;
import butterknife.BindView;
import butterknife.ButterKnife;
import java.util.Date;
import tsocket.zby.com.tsocket.AppString;
import tsocket.zby.com.tsocket.R;
import tsocket.zby.com.tsocket.view.wheel.OnWheelChangedListener;
import tsocket.zby.com.tsocket.view.wheel.WheelService;
import tsocket.zby.com.tsocket.view.wheel.WheelView;

/**
 * <p>Description: 选择执行的定时时间</p>
 * intent传递参数 名, hour, minute，second 不传递值则显示当前时间
 *
 * @author zhujiang
 * @date 2014-7-31
 */
public class WheelTimeActivity extends Activity {

  private final static String TAG = WheelTimeActivity.class.getSimpleName();
  @BindView(R.id.hour_layout) LinearLayout mHourLayout;
  @BindView(R.id.minute_layout) LinearLayout mMinuteLayout;
  @BindView(R.id.second_layout) LinearLayout mSecondLayout;

  //wheel 时间
  private WheelView hour_wheelView;
  private WheelView minute_wheelView;
  private WheelView second_wheelView;
  private final int wheelTextSize = 8;
  private int maxHour = 23, minHour = 0, maxMinute = 59, minMinute = 0, maxSecond = 59, minSecond = 0;

  //handler
  private final int InitWheelView = 3;
  private float phone_density;

  @Override protected void onCreate(Bundle savedInstanceState) {
    // TODO Auto-generated method stub
    super.onCreate(savedInstanceState);
    setContentView(R.layout.activity_wheel_time);
    ButterKnife.bind(this);
    
    phone_density = getResources().getDisplayMetrics().density; //屏幕密度
    Window window = getWindow();
    window.setGravity(Gravity.BOTTOM);
    //背景阴影
    LayoutParams lp2 = getWindow().getAttributes();
    lp2.dimAmount = 0.5f;
    getWindow().setAttributes(lp2);
    getWindow().setLayout(LayoutParams.MATCH_PARENT, LayoutParams.WRAP_CONTENT);
    getWindow().addFlags(LayoutParams.FLAG_DIM_BEHIND);

    initWheelViewThread();
  }

  Handler h = new Handler() {
    public void handleMessage(Message msg) {
      switch (msg.what) {
        case InitWheelView:
          addWheelView();
          initListener();
          initValue();
          break;
        default:
          break;
      }
    }

    ;
  };

  //等待oncreate完成， 给layout高度
  private void initWheelViewThread() {
    new Thread() {
      @Override public void run() {
        int height = 0;
        while (height == 0) {
          if (mHourLayout != null) {
            height = mHourLayout.getHeight();
          }
          try {
            Thread.sleep(10);
          } catch (InterruptedException e) {
            // TODO Auto-generated catch block
          }
        }

        h.sendEmptyMessage(InitWheelView);
        super.run();
      }
    }.start();
  }

  private void addWheelView() {
    hour_wheelView = new WheelView(this);
    minute_wheelView = new WheelView(this);
    second_wheelView = new WheelView(this);

    Date d = new Date();//没有设定初始值，就用当前时间
    int hour = getIntent().getIntExtra(AppString.HOUR, d.getHours());
    int minute = getIntent().getIntExtra(AppString.MINUTE, d.getMinutes());
    int second = getIntent().getIntExtra(AppString.SECOND, d.getSeconds());

    hour_wheelView = WheelService.getBinInt(this, maxHour, minHour, hour, "  H",
        mHourLayout.getHeight() / wheelTextSize, phone_density);
    mHourLayout.addView(hour_wheelView);
    //		hour_wheelView.setTextColor(getResources().getColor(R.color.text_red), getResources().getColor(R.color.line_between));
    //hour_wheelView.setCenterDrawableSourceId(R.drawable.img_layout_wheel);
    hour_wheelView.setBackgroundColor(getResources().getColor(R.color.text_while));

    minute_wheelView = WheelService.getBinInt(this, maxMinute, minMinute, minute, "  M",
        mMinuteLayout.getHeight() / wheelTextSize, phone_density);
    mMinuteLayout.addView(minute_wheelView);

    second_wheelView = WheelService.getBinInt(this, maxSecond, minSecond, second, " S",
        mSecondLayout.getHeight() / wheelTextSize, phone_density);
    mSecondLayout.addView(second_wheelView);

    if (!getIntent().getBooleanExtra(AppString.SHOW_HOUR, true)) {
      mHourLayout.setVisibility(View.GONE);
    }
  }

  private void initListener() {
    hour_wheelView.addChangingListener(new OnWheelChangedListener() {

      @Override public void onChanged(WheelView wheel, int oldValue, int newValue) {
        // TODO Auto-generated method stub
        //nowHour = newValue + minHour;
        //listenerDay(nowHour);
      }
    });
    minute_wheelView.addChangingListener(new OnWheelChangedListener() {

      @Override public void onChanged(WheelView wheel, int oldValue, int newValue) {
        //nowMinute = newValue + minMinute;
        //listenerDay(nowMonth);
      }
    });

    second_wheelView.addChangingListener(new OnWheelChangedListener() {

      @Override public void onChanged(WheelView wheel, int oldValue, int newValue) {
        //nowSecond = newValue + minSecond;
        //listenerDay(nowMonth);
      }
    });
  }

  private void initValue() {
    Intent mIntent = getIntent();
    int hourInt = mIntent.getIntExtra(AppString.HOUR, 0);
    int minInt = mIntent.getIntExtra(AppString.MINUTE, 1);
    int secInt = mIntent.getIntExtra(AppString.SECOND, 0);
    if (hourInt == 0 && minInt == 1) {
      Date date = new Date();
      hourInt = date.getHours();
      minInt = date.getMinutes();
      secInt = date.getSeconds();
    }
    hour_wheelView.setCurrentItem(hourInt);
    minute_wheelView.setCurrentItem(minInt);
    second_wheelView.setCurrentItem(secInt);
  }

  public void btn_confirm(View view) {
    Intent intent = getIntent();
    intent.putExtra("hour", hour_wheelView.getCurrentItem() + minHour);
    intent.putExtra("minute", minute_wheelView.getCurrentItem() + minMinute);
    intent.putExtra("second", second_wheelView.getCurrentItem() + minSecond);
    setResult(Activity.RESULT_OK, intent);
    finish();
  }

  public void btn_back(View view) {
    finish();
  }
}
