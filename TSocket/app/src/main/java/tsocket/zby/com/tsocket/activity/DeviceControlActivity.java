package tsocket.zby.com.tsocket.activity;

import android.content.Intent;
import android.os.Bundle;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.CheckedTextView;
import android.widget.TextView;
import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import cn.bingoogolapple.refreshlayout.BGANormalRefreshViewHolder;
import cn.bingoogolapple.refreshlayout.BGARefreshLayout;
import cn.bingoogolapple.refreshlayout.BGARefreshViewHolder;
import java.util.List;
import tsocket.zby.com.tsocket.R;
import tsocket.zby.com.tsocket.adapter.TimerAdapter;
import tsocket.zby.com.tsocket.bean.DeviceBean;
import tsocket.zby.com.tsocket.bean.TimerBean;
import tsocket.zby.com.tsocket.connection.agreement.CmdPackage;
import tsocket.zby.com.tsocket.view.HeaderLayout;

public class DeviceControlActivity extends BaseActivity
    implements SwipeRefreshLayout.OnRefreshListener, BGARefreshLayout.BGARefreshLayoutDelegate {

  @BindView(R.id.recyclerView) RecyclerView mRecyclerView;
  @BindView(R.id.swiperLayout) BGARefreshLayout mSwiperLayout;
  @BindView(R.id.headerLayout) HeaderLayout headerLayout;
  @BindView(R.id.tv_delay) TextView tvDelay;
  @BindView(R.id.tv_timer) TextView tvTimer;
  @BindView(R.id.ctv_switch) CheckedTextView ctvSwitch;
  private TimerAdapter mTimerAdapter;
  private List<TimerBean> list;
  private DeviceBean mDeviceBean;

  @Override protected void onCreate(Bundle savedInstanceState) {
    super.onCreate(savedInstanceState);
    setContentView(R.layout.activity_device_control);
    ButterKnife.bind(this);

    mDeviceBean = mApp.getDeviceBean();

    BGARefreshViewHolder RefreshViewHolder = new BGANormalRefreshViewHolder(this, true);
    mSwiperLayout.setRefreshViewHolder(RefreshViewHolder);
    mRecyclerView.setLayoutManager(new LinearLayoutManager(this));
    list = mDeviceBean.getTimerBeanList();

    mTimerAdapter = new TimerAdapter(mRecyclerView);
    mTimerAdapter.setDatas(list);
    mRecyclerView.setAdapter(mTimerAdapter);
    mSwiperLayout.setDelegate(this);
  }

  @OnClick(R.id.layout_title_left) public void onBack() {
    finish();
  }

  @Override public void onRefresh() {

  }

  @Override public void onBGARefreshLayoutBeginRefreshing(BGARefreshLayout refreshLayout) {
    mSwiperLayout.beginRefreshing();
  }

  @Override public boolean onBGARefreshLayoutBeginLoadingMore(BGARefreshLayout refreshLayout) {
    return false;
  }

  @OnClick({ R.id.tv_delay, R.id.ctv_switch, R.id.tv_timer }) public void onClick(View view) {
    Intent intent;
    switch (view.getId()) {
      case R.id.tv_delay:
        intent = new Intent(this, DelayActivity.class);
        startActivity(intent);
        break;
      case R.id.ctv_switch:
        mDeviceBean.write(CmdPackage.setSwitch(!ctvSwitch.isChecked()));
        break;
      case R.id.tv_timer:
        intent = new Intent(this, TimerActivity.class);
        startActivity(intent);
        break;
    }
  }

  @OnClick(R.id.ctv_switch) public void onClick() {
  }
}
