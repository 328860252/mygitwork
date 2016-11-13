package tsocket.zby.com.tsocket.activity;

import android.Manifest;
import android.bluetooth.BluetoothDevice;
import android.content.Intent;
import android.os.Build;
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
import com.tbruyelle.rxpermissions.RxPermissions;
import java.util.ArrayList;
import java.util.List;
import rx.functions.Action1;
import tsocket.zby.com.tsocket.AppConstants;
import tsocket.zby.com.tsocket.R;
import tsocket.zby.com.tsocket.adapter.DeviceAdapter;
import tsocket.zby.com.tsocket.bean.BluetoothBean;
import tsocket.zby.com.tsocket.bean.DeviceBean;
import tsocket.zby.com.tsocket.connection.ConnectAction;
import tsocket.zby.com.tsocket.connection.ble.BleManager;
import tsocket.zby.com.tsocket.utils.LogUtils;

public class DeviceListActivity extends BaseActivity
    implements SwipeRefreshLayout.OnRefreshListener, BGARefreshLayout.BGARefreshLayoutDelegate {

  private final static String TAG = DeviceListActivity.class.getSimpleName();

  @BindView(R.id.recyclerView) RecyclerView mRecyclerView;
  @BindView(R.id.swiperLayout) BGARefreshLayout mSwiperLayout;
  private DeviceAdapter mDeviceAdapter;
  private List<DeviceBean> list;
  private DeviceBean mDeviceBean;

  @Override protected void onCreate(Bundle savedInstanceState) {
    super.onCreate(savedInstanceState);
    setContentView(R.layout.activity_device_list);
    ButterKnife.bind(this);
    BleManager.getInstance(this).bluetoothEnable();
    initViews();
    if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) { //6.0蓝牙搜索需要 location权限
      RxPermissions.getInstance(this)
          .request(Manifest.permission.ACCESS_COARSE_LOCATION,
              Manifest.permission.ACCESS_FINE_LOCATION)
          .subscribe(new Action1<Boolean>() {
            @Override public void call(Boolean aBoolean) {
              mSwiperLayout.beginRefreshing();
            }});
    } else {
      mSwiperLayout.beginRefreshing();
    }

  }

  private void initViews() {
    BGARefreshViewHolder RefreshViewHolder = new BGANormalRefreshViewHolder(this, true);
    mSwiperLayout.setRefreshViewHolder(RefreshViewHolder);
    mRecyclerView.setLayoutManager(new LinearLayoutManager(this));
    list = mApp.getList();

    if (AppConstants.isDemo && list.size()==0) {
      DeviceBean deviceBean = new DeviceBean();
      deviceBean.setName("test1");
      deviceBean.setMac("test1");
      deviceBean.setBonded(true);
      list.add(deviceBean);

      DeviceBean deviceBean2 = new DeviceBean();
      deviceBean2.setName("test2");
      deviceBean2.setMac("test2");
      deviceBean2.setBonded(true);
      list.add(deviceBean2);
    }

    mDeviceAdapter = new DeviceAdapter(mRecyclerView);
    mDeviceAdapter.setDatas(list);
    mRecyclerView.setAdapter(mDeviceAdapter);
    mSwiperLayout.setDelegate(this);
    mDeviceAdapter.setOnItemClickListener(new DeviceAdapter.OnItemClickListener() {
      @Override public void onClickItem(View view, int position) {
        //如果之前有其他设备连接着， 先断开
        if (mApp.getDeviceBean()!= null && !mApp.getDeviceBean().getMac().equals(list.get(position).getMac())) {
          if (mApp.getDeviceBean().isLink()) {
            mApp.getDeviceBean().stopConnect();
          }
        }
        mApp.setDevcieBean(list.get(position));
        mDeviceBean = mApp.getDeviceBean();
        if (!mDeviceBean.isLink()) {
          mDeviceBean.connect();
          showToast(R.string.toast_linking);
        } else {
          if (mDeviceBean.isBonded()) {
            Intent intent = new Intent(DeviceListActivity.this, DeviceControlActivity.class);
            startActivity(intent);
          } else if (BleManager.getInstance(DeviceListActivity.this).isBonded(mDeviceBean.getMac())) {
            Intent intent = new Intent(DeviceListActivity.this, DeviceControlActivity.class);
            startActivity(intent);
          } else {
            mDeviceBean.stopConnect();
            showToast(R.string.toast_no_bond);
          }
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
    BleManager.getInstance(this).startScan(true);
  }

  @Override public boolean onBGARefreshLayoutBeginLoadingMore(BGARefreshLayout refreshLayout) {
    return false;
  }

  @Override protected void onReceiverCmd(Object message) {
    if (message instanceof String) {
      LogUtils.v("deviceList", "onReceiver  "+ message);
      if (message.equals(ConnectAction.ACTION_DEVICE_SCAN_FINISH)) {
        mSwiperLayout.endRefreshing();
      } else if (message.equals("newDeviceBean")) {
        mDeviceAdapter.notifyDataSetChanged();
      } else if (message.equals(ConnectAction.ACTION_GATT_SERVICES_DISCOVERED)) {//发现服务才算连接上
        if (mDeviceBean.isBonded()) {
          showToast(R.string.toast_linked);
          Intent intent = new Intent(DeviceListActivity.this, DeviceControlActivity.class);
          startActivity(intent);
        } else if (BleManager.getInstance(DeviceListActivity.this).isBonded(mDeviceBean.getMac())) {
          Intent intent = new Intent(DeviceListActivity.this, DeviceControlActivity.class);
          startActivity(intent);
        }
      } else if (message.equals(ConnectAction.ACTION_BLUETOOTH_BOUNED)) {
        LogUtils.v("deviceList", "onReceiver  "+ mDeviceBean.isBonded());
        if (mDeviceBean.isBonded()) {
          if (mDeviceBean.isLink()) {
            Intent intent = new Intent(DeviceListActivity.this, DeviceControlActivity.class);
            startActivity(intent);
          } else {
            mDeviceBean.connect();
          }
        } else {
          mDeviceBean.stopConnect();
        }
      }
    }
    super.onReceiverCmd(message);
  }

  @Override protected void onStart() {
    subscribePushMessage();
    mDeviceAdapter.notifyDataSetChanged();
    super.onStart();
  }

  @Override protected void onStop() {
    unSubscribePushMessage();
    if (mSwiperLayout!=null && mSwiperLayout.isShown()) {
      mSwiperLayout.endRefreshing();
    }
    BleManager.getInstance(this).startScan(false);
    super.onStop();
  }

  @Override protected void onDestroy() {
    if (mApp != null) {
      mApp.onTerminate();
    }
    super.onDestroy();
  }
}
