package tsocket.zby.com.tsocket.activity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.CheckBox;
import android.widget.TextView;
import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import tsocket.zby.com.tsocket.AppConstants;
import tsocket.zby.com.tsocket.AppString;
import tsocket.zby.com.tsocket.R;
import tsocket.zby.com.tsocket.bean.DeviceBean;
import tsocket.zby.com.tsocket.bean.TimerBean;
import tsocket.zby.com.tsocket.connection.agreement.CmdPackage;
import tsocket.zby.com.tsocket.view.WeekView;

public class TimerActivity extends BaseActivity {

  @BindView(R.id.tv_timer_start) TextView mTvTimerStart;
  @BindView(R.id.tv_timer_end) TextView mTvTimerEnd;
  @BindView(R.id.tv_delay_start) TextView mTvDelayStart;
  @BindView(R.id.tv_delay_end) TextView mTvDelayEnd;
  @BindView(R.id.cb_delay) CheckBox mCbDelay;
  @BindView(R.id.weekView_value) WeekView mWeekViewValue;

  private DeviceBean mDeviceBean;
  private TimerBean mTimerBean;
  private final int activity_startTimer = 11;
  private final int activity_endTimer = 12;
  private final int activity_openTimer = 13;
  private final int activity_closeTimer = 14;

  @Override protected void onCreate(Bundle savedInstanceState) {
    super.onCreate(savedInstanceState);
    setContentView(R.layout.activity_timer);
    ButterKnife.bind(this);
    initViews();
  }

  private void initViews() {
    mDeviceBean = mApp.getDeviceBean();
    if (getIntent().hasExtra("timer")) { //修改
      TimerBean tb = (TimerBean) getIntent().getSerializableExtra("timer");
      try {
        mTimerBean = (TimerBean) tb.clone();
      } catch (CloneNotSupportedException e) {
        e.printStackTrace();
      }
    }
    if (mTimerBean == null) { //新增
      mTimerBean = TimerBean.getNewTimerBean();
      mTimerBean.setId(mDeviceBean.getNewTimerId());
    }
    mTvTimerStart.setText(mTimerBean.getStartString());
    mTvTimerEnd.setText(mTimerBean.getEndString());
    mTvDelayStart.setText(mTimerBean.getOpenString());
    mTvDelayEnd.setText(mTimerBean.getCloseString());

    mCbDelay.setChecked(mTimerBean.isRecycle());
    mWeekViewValue.setWeekValue(mTimerBean.getWeekValue());
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

  @OnClick({
      R.id.layout_startTimer, R.id.layout_endTimer, R.id.tv_delay_start, R.id.tv_delay_end, R.id.btn_confirm
  }) public void onClick(View view) {
    Intent intent;
    switch (view.getId()) {
      case R.id.layout_startTimer:
        intent = new Intent(this, WheelTimeActivity.class);
        intent.putExtra(AppString.TITLE, getString(R.string.text_timer_start));
        intent.putExtra(AppString.HOUR, mTimerBean.getStartHour());
        intent.putExtra(AppString.MINUTE, mTimerBean.getStartMinute());
        intent.putExtra(AppString.SECOND, mTimerBean.getStartSecond());
        startActivityForResult(intent, activity_startTimer);
        break;
      case R.id.layout_endTimer:
        intent = new Intent(this, WheelTimeActivity.class);
        intent.putExtra(AppString.TITLE, getString(R.string.text_timer_end));
        intent.putExtra(AppString.HOUR, mTimerBean.getEndHour());
        intent.putExtra(AppString.MINUTE, mTimerBean.getEndMinute());
        intent.putExtra(AppString.SECOND, mTimerBean.getEndSecond());
        startActivityForResult(intent, activity_endTimer);
        break;
      case R.id.tv_delay_start:
        intent = new Intent(this, WheelTimeActivity.class);
        intent.putExtra(AppString.TITLE, getString(R.string.text_delay_start));
        intent.putExtra(AppString.SHOW_HOUR, false);
        intent.putExtra(AppString.HOUR, mTimerBean.getOpenHour());
        intent.putExtra(AppString.MINUTE, mTimerBean.getOpenMinute());
        intent.putExtra(AppString.SECOND, mTimerBean.getOpenSecond());
        startActivityForResult(intent, activity_openTimer);
        break;
      case R.id.tv_delay_end:
        intent = new Intent(this, WheelTimeActivity.class);
        intent.putExtra(AppString.TITLE, getString(R.string.text_delay_end));
        intent.putExtra(AppString.SHOW_HOUR, false);
        intent.putExtra(AppString.HOUR, mTimerBean.getCloseHour());
        intent.putExtra(AppString.MINUTE, mTimerBean.getCloseMinute());
        intent.putExtra(AppString.SECOND, mTimerBean.getCloseSecond());
        startActivityForResult(intent, activity_closeTimer);
        break;
      case R.id.btn_confirm:
        if (mWeekViewValue.getWeekValue()==0) {
          showToast(R.string.toast_timer_week_notnull);
          return;
        }
        if (mTimerBean.getId()>9) {
          showToast(R.string.toast_timer_max);
          return ;
        }
        if (mTvTimerStart.getText().toString().compareTo(mTvTimerEnd.getText().toString()) >= 0) {
          showToast(R.string.toast_timer_timerErr);
          return;
        }
        mTimerBean.setRecycle(mCbDelay.isChecked());
        mTimerBean.setWeekValue(mWeekViewValue.getWeekValue());
        if (!mDeviceBean.verifyConflict(mTimerBean) ){
          showToast(R.string.toast_timer_conflict);
         return;
        }
        if (mDeviceBean.write(CmdPackage.setTimer(mTimerBean))) {
          mDeviceBean.updateTimerBeanList(mTimerBean);
          setResult(RESULT_OK);
          finish();
        }
        break;
    }
  }

  @Override protected void onActivityResult(int requestCode, int resultCode, Intent data) {
    super.onActivityResult(requestCode, resultCode, data);
    if (resultCode != RESULT_OK) return;
    int hour = data.getIntExtra(AppString.HOUR, 0);
    int minute = data.getIntExtra(AppString.MINUTE, 0);
    int second = data.getIntExtra(AppString.SECOND, 0);
    switch (requestCode) {
      case activity_startTimer:
        mTimerBean.setStartHour(hour);
        mTimerBean.setStartMinute(minute);
        mTimerBean.setStartSecond(second);
        mTvTimerStart.setText(mTimerBean.getStartString());
        break;
      case activity_endTimer:
        mTimerBean.setEndHour(hour);
        mTimerBean.setEndMinute(minute);
        mTimerBean.setEndSecond(second);
        mTvTimerEnd.setText(mTimerBean.getEndString());
        break;
      case activity_openTimer:
        //mTimerBean.setOpenHour(hour);
        int delayTime = minute * 60 + second;
        if (delayTime < AppConstants.DELAY_TIME_MIN) {
          showToast(R.string.toast_delay_min_error);
          return ;
        }
            mTimerBean.setOpenMinute(minute);
        mTimerBean.setOpenSecond(second);
        mTvDelayStart.setText(mTimerBean.getOpenString());
        break;
      case activity_closeTimer:
        //mTimerBean.setCloseHour(hour);
        mTimerBean.setCloseMinute(minute);
        mTimerBean.setCloseSecond(second);
        mTvDelayEnd.setText(mTimerBean.getCloseString());
        break;
    }
  }
}
