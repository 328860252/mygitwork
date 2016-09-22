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
import tsocket.zby.com.tsocket.connection.ble.BleManager;

public class DeviceListActivity extends BaseActivity
    implements SwipeRefreshLayout.OnRefreshListener, BGARefreshLayout.BGARefreshLayoutDelegate {

  @BindView(R.id.recyclerView) RecyclerView mRecyclerView;
  @BindView(R.id.swiperLayout) BGARefreshLayout mSwiperLayout;
  private DeviceAdapter mDeviceAdapter;
  private List<DeviceBean> list;
  private DeviceBean mDeviceBean;
  private BleManager mBleManager;

  @Override protected void onCreate(Bundle savedInstanceState) {
    super.onCreate(savedInstanceState);
    setContentView(R.layout.activity_device_list);
    ButterKnife.bind(this);

    BGARefreshViewHolder RefreshViewHolder = new BGANormalRefreshViewHolder(this, true);
    mSwiperLayout.setRefreshViewHolder(RefreshViewHolder);
    mRecyclerView.setLayoutManager(new LinearLayoutManager(this));
    list = mApp.getList();

    if (AppConstants.isDemo) {
      DeviceBean deviceBean = new DeviceBean();
      deviceBean.setName("test1");
      deviceBean.setMac("test1");
      list.add(deviceBean);
    }
    String str = getString(R.string.text_adapter_timer);
    String str2 = String.format(str, "23123", "23123'");

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
        mApp.setDevcieBean(list.get(position));
        mDeviceBean = mApp.getDeviceBean();
        if (!mDeviceBean.isLink()) {
          mDeviceBean.connect();
        } else {
          Intent intent = new Intent(DeviceListActivity.this, DeviceControlActivity.class);
          startActivity(intent);
        }
      }
    });
  }

  @OnClick(R.id.layout_title_left) public void onBack() {
    finish();
  }

  @Override public void onRefresh() {
    //if (mBleManager == null) {
    //  mBleManager = new BleManager(this);
    //}
    //mBleManager.startScan();
  }

  @Override public void onBGARefreshLayoutBeginRefreshing(BGARefreshLayout refreshLayout) {
    //mSwiperLayout.endRefreshing();
    //if (mBleManager == null) {
    //  mBleManager = new BleManager(this);
    //}
    //mBleManager.startScan();
  }

  @Override public boolean onBGARefreshLayoutBeginLoadingMore(BGARefreshLayout refreshLayout) {
    return false;
  }

  @Override protected void onDestroy() {
    super.onDestroy();
  }
}
