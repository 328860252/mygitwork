package tsocket.zby.com.tsocket.activity;

import android.os.Bundle;
import android.view.View;
import android.widget.CheckBox;
import android.widget.GridLayout;
import android.widget.SeekBar;
import android.widget.TextView;
import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import tsocket.zby.com.tsocket.R;
import tsocket.zby.com.tsocket.bean.DeviceBean;
import tsocket.zby.com.tsocket.connection.agreement.CmdPackage;
import tsocket.zby.com.tsocket.view.DelayMinuteView;

public class DelayActivity extends BaseActivity {

  @BindView(R.id.tv_delay_time) TextView mTvDelayTime;
  @BindView(R.id.search_bar) SeekBar mSearchBar;
  @BindView(R.id.cb_delay) CheckBox mCbDelay;
  @BindView(R.id.layout_grid) GridLayout mLayoutGrid;
  private DeviceBean mDeviceBean;

  @Override protected void onCreate(Bundle savedInstanceState) {
    super.onCreate(savedInstanceState);
    setContentView(R.layout.activity_delay);
    ButterKnife.bind(this);
    initView();
  }

  private void initView() {
    mDeviceBean = mApp.getDeviceBean();
    mSearchBar.setOnSeekBarChangeListener(new SeekBar.OnSeekBarChangeListener() {
      @Override public void onProgressChanged(SeekBar seekBar, int i, boolean b) {
        mTvDelayTime.setText(String.format(getString(R.string.text_delay_minute), seekBar.getProgress()));
      }

      @Override public void onStartTrackingTouch(SeekBar seekBar) {

      }

      @Override public void onStopTrackingTouch(SeekBar seekBar) {
        int procress = seekBar.getProgress();
        initDelayMinute(procress);
      }
    });
    mSearchBar.setProgress(mDeviceBean.getDelayNumber());
    mCbDelay.setChecked(mDeviceBean.isDelaySwitch());
  }

  @OnClick(R.id.layout_title_right) public void onSave() {
    mDeviceBean.write(CmdPackage.setTimerDelay(mCbDelay.isChecked(), 0, mSearchBar.getProgress(), 0));
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
        mSearchBar.setProgress(Integer.parseInt(text));
        initDelayMinute(Integer.parseInt(text));
        break;
    }
  }

  private void initDelayMinute(int number) {
    mTvDelayTime.setText(String.format(getString(R.string.text_delay_minute), number));
    initMinute(""+number);
  }

  private void initMinute(String number) {
    DelayMinuteView view;
    for (int i=0; i< mLayoutGrid.getChildCount(); i++) {
      view = (DelayMinuteView) mLayoutGrid.getChildAt(i);
      if (view.getTextNumber().equals(number)) {
        view.setChecked(true);
      } else {
        view.setChecked(false);
      }
    }
  }
}
