package tsocket.zby.com.tsocket.activity;

import android.os.Bundle;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import cn.bingoogolapple.refreshlayout.BGANormalRefreshViewHolder;
import cn.bingoogolapple.refreshlayout.BGARefreshLayout;
import cn.bingoogolapple.refreshlayout.BGARefreshViewHolder;
import java.util.ArrayList;
import java.util.List;
import tsocket.zby.com.tsocket.R;
import tsocket.zby.com.tsocket.adapter.DeviceAdapter;
import tsocket.zby.com.tsocket.bean.DeviceBean;

public class DeviceControlActivity extends BaseActivity
    implements SwipeRefreshLayout.OnRefreshListener, BGARefreshLayout.BGARefreshLayoutDelegate {

  @BindView(R.id.recyclerView) RecyclerView mRecyclerView;
  @BindView(R.id.swiperLayout) BGARefreshLayout mSwiperLayout;
  private DeviceAdapter mDeviceAdapter;
  private List<DeviceBean> list;

  @Override protected void onCreate(Bundle savedInstanceState) {
    super.onCreate(savedInstanceState);
    setContentView(R.layout.activity_device_list);
    ButterKnife.bind(this);

    BGARefreshViewHolder RefreshViewHolder = new BGANormalRefreshViewHolder(this, true);
    mSwiperLayout.setRefreshViewHolder(RefreshViewHolder);
    mRecyclerView.setLayoutManager(new LinearLayoutManager(this));
    list = new ArrayList<>();

    mDeviceAdapter = new DeviceAdapter(mRecyclerView);
    mDeviceAdapter.setDatas(list);
    mRecyclerView.setAdapter(mDeviceAdapter);
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
}
