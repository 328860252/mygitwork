package tsocket.zby.com.tsocket.activity;

import android.os.Bundle;
import android.view.View;
import android.widget.CheckBox;
import android.widget.SeekBar;
import android.widget.TextView;
import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import java.util.ArrayList;
import java.util.List;
import tsocket.zby.com.tsocket.AppString;
import tsocket.zby.com.tsocket.R;
import tsocket.zby.com.tsocket.bean.DeviceBean;
import tsocket.zby.com.tsocket.connection.agreement.CmdPackage;
import tsocket.zby.com.tsocket.utils.SharedPerfenceUtils;
import tsocket.zby.com.tsocket.view.DelayMinuteView;

public class DelayActivity extends BaseActivity {

  @BindView(R.id.tv_delay_time) TextView mTvDelayTime;
  @BindView(R.id.search_bar) SeekBar mSearchBar;
  @BindView(R.id.cb_delay) CheckBox mCbDelay;
  @BindView(R.id.tv_minute_10) DelayMinuteView mTvMinute10;
  @BindView(R.id.tv_minute_20) DelayMinuteView mTvMinute20;
  @BindView(R.id.tv_minute_30) DelayMinuteView mTvMinute30;
  @BindView(R.id.tv_minute_40) DelayMinuteView mTvMinute40;
  @BindView(R.id.tv_minute_50) DelayMinuteView mTvMinute50;
  @BindView(R.id.tv_minute_60) DelayMinuteView mTvMinute60;
  private DeviceBean mDeviceBean;
  private List<DelayMinuteView> mViewList = new ArrayList<>();

  /**
   * 定时是1 - 60 ， 但是seekbar 默认最小值为0 ，范围为0-59 ，所以全部要加上1
   */
  private final int SEEKBAR_MIN = 1;

  @Override protected void onCreate(Bundle savedInstanceState) {
    super.onCreate(savedInstanceState);
    setContentView(R.layout.activity_delay);
    ButterKnife.bind(this);
    initView();
  }

  private void initView() {
    mDeviceBean = mApp.getDeviceBean();
    mViewList.add(mTvMinute10);
    mViewList.add(mTvMinute20);
    mViewList.add(mTvMinute30);
    mViewList.add(mTvMinute40);
    mViewList.add(mTvMinute50);
    mViewList.add(mTvMinute60);
    mSearchBar.setOnSeekBarChangeListener(new SeekBar.OnSeekBarChangeListener() {
      @Override public void onProgressChanged(SeekBar seekBar, int i, boolean b) {
        mTvDelayTime.setText(
            String.format(getString(R.string.text_delay_minute), getSeekbarProgressValue()));
      }

      @Override public void onStartTrackingTouch(SeekBar seekBar) {

      }

      @Override public void onStopTrackingTouch(SeekBar seekBar) {
        initDelayMinute(getSeekbarProgressValue());
      }
    });
    //刚好是相反的
    mCbDelay.setChecked(!mDeviceBean.isOnOff());
    int lastPro = SharedPerfenceUtils.getSetupData(this).readInt(AppString.Last_Delay_Timer, 1);
    setSeekbarProgressValue(lastPro);
    mTvDelayTime.setText(
        String.format(getString(R.string.text_delay_minute), getSeekbarProgressValue()));
  }

  @OnClick(R.id.layout_title_right) public void onSave() {
    int minute = getSeekbarProgressValue();
    if (mDeviceBean.write(CmdPackage.setTimerDelay(mCbDelay.isChecked(), 0, minute, 0))) {
      SharedPerfenceUtils.getSetupData(this).saveInt(AppString.Last_Delay_Timer, minute);
      mDeviceBean.setDownCountSecond(minute * 60);
      setResult(RESULT_OK);
      finish();
    }
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
      R.id.tv_minute_10, R.id.tv_minute_20, R.id.tv_minute_30, R.id.tv_minute_40, R.id.tv_minute_50,
      R.id.tv_minute_60
  }) public void onClick(View view) {
    switch (view.getId()) {
      case R.id.tv_minute_10:
      case R.id.tv_minute_20:
      case R.id.tv_minute_30:
      case R.id.tv_minute_40:
      case R.id.tv_minute_50:
      case R.id.tv_minute_60:
        String text = ((DelayMinuteView) view).getTextNumber();
        setSeekbarProgressValue(Integer.parseInt(text));
        initDelayMinute(Integer.parseInt(text));
        break;
    }
  }

  private void initDelayMinute(int number) {
    mTvDelayTime.setText(String.format(getString(R.string.text_delay_minute), number));
    initMinute("" + number);
  }

  private void initMinute(String number) {
    DelayMinuteView view;
    for (int i = 0; i < mViewList.size(); i++) {
      view = mViewList.get(i);
      if (view.getTextNumber().equals(number)) {
        view.setChecked(true);
      } else {
        view.setChecked(false);
      }
    }
  }

  private int getSeekbarProgressValue() {
    return mSearchBar.getProgress() + SEEKBAR_MIN;
  }

  private void setSeekbarProgressValue(int value) {
    mSearchBar.setProgress(value - SEEKBAR_MIN);
  }
}
