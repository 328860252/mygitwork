package tsocket.zby.com.tsocket.activity;

import android.content.Intent;
import android.os.Bundle;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.view.ViewGroup;
import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import cn.bingoogolapple.androidcommon.adapter.BGAOnItemChildClickListener;
import cn.bingoogolapple.refreshlayout.BGANormalRefreshViewHolder;
import cn.bingoogolapple.refreshlayout.BGARefreshLayout;
import cn.bingoogolapple.refreshlayout.BGARefreshViewHolder;
import java.util.ArrayList;
import java.util.List;
import tsocket.zby.com.tsocket.AppConstants;
import tsocket.zby.com.tsocket.R;
import tsocket.zby.com.tsocket.adapter.DeviceAdapter;
import tsocket.zby.com.tsocket.bean.BluetoothBean;
import tsocket.zby.com.tsocket.bean.DeviceBean;

public class DeviceListActivity extends BaseActivity
    implements SwipeRefreshLayout.OnRefreshListener, BGARefreshLayout.BGARefreshLayoutDelegate {

  @BindView(R.id.recyclerView) RecyclerView mRecyclerView;
  @BindView(R.id.swiperLayout) BGARefreshLayout mSwiperLayout;
  private DeviceAdapter mDeviceAdapter;
  private List<BluetoothBean> list;
  private DeviceBean mDeviceBean;

  @Override protected void onCreate(Bundle savedInstanceState) {
    super.onCreate(savedInstanceState);
    setContentView(R.layout.activity_device_list);
    ButterKnife.bind(this);

    mDeviceBean = mApp.getDeviceBean();

    BGARefreshViewHolder RefreshViewHolder = new BGANormalRefreshViewHolder(this, true);
    mSwiperLayout.setRefreshViewHolder(RefreshViewHolder);
    mRecyclerView.setLayoutManager(new LinearLayoutManager(this));
    list = new ArrayList<>();

    if (AppConstants.isDemo) {
      BluetoothBean deviceBean = new BluetoothBean();
      deviceBean.setName("test1");
      deviceBean.setMac("test1");
      list.add(deviceBean);
    }

    mDeviceAdapter = new DeviceAdapter(mRecyclerView);
    mDeviceAdapter.setDatas(list);
    mRecyclerView.setAdapter(mDeviceAdapter);
    mSwiperLayout.setDelegate(this);
    mDeviceAdapter.setOnItemClickListener(new DeviceAdapter.OnItemClickListener() {
      @Override public void onClickItem(View view, int position) {
        //        mDeviceBean.setConnectionInterface(new BleImpl(mBluetoothLeService), DeviceListActivity.this);
        //        mDeviceBean.setName(list.get(i).getName());
        //        mDeviceBean.getConnect().connect(list.get(i).getMac(), "");
        //mDeviceBean.isLink();
        Intent intent = new Intent(DeviceListActivity.this, DeviceControlActivity.class);
        startActivity(intent);
      }
    });
  }

  @OnClick(R.id.layout_title_left) public void onBack() {
    finish();
  }

  @Override public void onRefresh() {

  }

  @Override public void onBGARefreshLayoutBeginRefreshing(BGARefreshLayout refreshLayout) {
    mSwiperLayout.endRefreshing();
  }

  @Override public boolean onBGARefreshLayoutBeginLoadingMore(BGARefreshLayout refreshLayout) {
    return false;
  }

  @Override protected void onDestroy() {
    super.onDestroy();
  }
}
