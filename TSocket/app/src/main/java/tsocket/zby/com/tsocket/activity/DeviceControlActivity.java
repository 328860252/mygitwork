package tsocket.zby.com.tsocket.activity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.CheckedTextView;
import android.widget.ListView;
import android.widget.TextView;
import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import java.util.List;
import tsocket.zby.com.tsocket.R;
import tsocket.zby.com.tsocket.adapter.TimerAdapter;
import tsocket.zby.com.tsocket.bean.DeviceBean;
import tsocket.zby.com.tsocket.bean.TimerBean;
import tsocket.zby.com.tsocket.connection.agreement.CmdPackage;
import tsocket.zby.com.tsocket.connection.ble.BleManager;
import tsocket.zby.com.tsocket.view.HeaderLayout;

public class DeviceControlActivity extends BaseActivity {

  @BindView(R.id.headerLayout) HeaderLayout headerLayout;
  @BindView(R.id.tv_delay) TextView tvDelay;
  @BindView(R.id.tv_timer) TextView tvTimer;
  @BindView(R.id.ctv_switch) CheckedTextView ctvSwitch;
  @BindView(R.id.listView) ListView mListView;
  private TimerAdapter mTimerAdapter;
  private List<TimerBean> list;
  private DeviceBean mDeviceBean;
  private final int activity_timer = 11;

  @Override protected void onCreate(Bundle savedInstanceState) {
    super.onCreate(savedInstanceState);
    setContentView(R.layout.activity_device_control);
    ButterKnife.bind(this);
    initViews();
  }

  private void initViews() {
    mDeviceBean = mApp.getDeviceBean();
    list = mDeviceBean.getTimerBeanList();
    mTimerAdapter = new TimerAdapter(this, list);
    mListView.setAdapter(mTimerAdapter);
    headerLayout.setTextTitle(mDeviceBean.getName());
    mTimerAdapter.setListener(new TimerAdapter.OnTimerSwitchClickListener() {
      @Override public void onTimerSwitchClick(TimerBean timerBean) {
        timerBean.setTimerSwitch(!timerBean.isTimerSwitch());
        mDeviceBean.write(CmdPackage.setTimer(timerBean));
        mTimerAdapter.notifyDataSetChanged();
      }
    });
    mListView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
      @Override public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
        Intent intent = new Intent(DeviceControlActivity.this, TimerActivity.class);
        intent.putExtra("timer", list.get(i));
        startActivity(intent);
      }
    });
    mListView.setOnLongClickListener(new View.OnLongClickListener() {
      @Override public boolean onLongClick(View view) {
        return false;
      }
    });
  }

  @Override protected void onActivityResult(int requestCode, int resultCode, Intent data) {
    super.onActivityResult(requestCode, resultCode, data);
    switch (requestCode) {
      case activity_timer:
        if (resultCode == RESULT_OK) {
          mTimerAdapter.notifyDataSetChanged();
        }
        break;
    }
  }

  @OnClick(R.id.layout_title_left) public void onBack() {
    finish();
  }

  @OnClick(R.id.layout_title_right) public void onSetting() {
    Intent intent = new Intent(this, SettingActivity.class);
    startActivity(intent);
  }

  @Override protected void onLanguageChange() {
    tvDelay.setText(R.string.text_delay);
    tvTimer.setText(R.string.text_timer);
    mTimerAdapter.notifyDataSetChanged();
    super.onLanguageChange();
  }

  @OnClick({ R.id.tv_delay, R.id.ctv_switch, R.id.tv_timer }) public void onClick(View view) {
    Intent intent;
    switch (view.getId()) {
      case R.id.tv_delay:
        intent = new Intent(this, DelayActivity.class);
        startActivity(intent);
        break;
      case R.id.ctv_switch:
        ctvSwitch.setChecked(!ctvSwitch.isChecked());
        mDeviceBean.write(CmdPackage.setSwitch(ctvSwitch.isChecked()));
        break;
      case R.id.tv_timer:
        intent = new Intent(this, TimerActivity.class);
        startActivityForResult(intent, activity_timer);
        break;
    }
  }
}
