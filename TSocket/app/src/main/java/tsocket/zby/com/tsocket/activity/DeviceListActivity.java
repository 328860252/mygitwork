package tsocket.zby.com.tsocket.activity;

import android.content.BroadcastReceiver;
import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.content.ServiceConnection;
import android.os.Bundle;
import android.os.IBinder;
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
import tsocket.zby.com.tsocket.connection.agreement.CmdEncrypt;
import tsocket.zby.com.tsocket.connection.agreement.CmdPackage;
import tsocket.zby.com.tsocket.connection.ble.BluetoothLeService;
import tsocket.zby.com.tsocket.utils.LogUtils;
import tsocket.zby.com.tsocket.utils.MyHexUtils;

public class DeviceListActivity extends BaseActivity
    implements SwipeRefreshLayout.OnRefreshListener, BGARefreshLayout.BGARefreshLayoutDelegate {

  @BindView(R.id.recyclerView) RecyclerView mRecyclerView;
  @BindView(R.id.swiperLayout) BGARefreshLayout mSwiperLayout;
  private DeviceAdapter mDeviceAdapter;
  private List<DeviceBean> list;
  private DeviceBean mDeviceBean;

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

  ServiceConnection serviceConnection ;
  private BluetoothLeService mBluetoothLeService;
  private void bindService() {
    serviceConnection  = new ServiceConnection() {

      @Override
      public void onServiceDisconnected(ComponentName service) {
        // TODO Auto-generated method stub
        mBluetoothLeService = null;
      }

      @Override
      public void onServiceConnected(ComponentName arg0, IBinder service) {
        // TODO Auto-generated method stub
        mBluetoothLeService = ((BluetoothLeService.LocalBinder) service)
            .getService();
        if (!mBluetoothLeService.initialize()) {
        }
      }
    };
    Intent gattServiceIntent = new Intent(this, BluetoothLeService.class);
    bindService(gattServiceIntent, serviceConnection, BIND_AUTO_CREATE);
  }

  private final BroadcastReceiver mGattUpdateReceiver = new BroadcastReceiver() {
    @Override
    public void onReceive(Context context, Intent intent) {
      final String action = intent.getAction();
      final String mac = intent.getStringExtra("mac");
      if (BluetoothLeService.ACTION_GATT_CONNECTED.equals(action)) {
      } else if (BluetoothLeService.ACTION_GATT_DISCONNECTED.equals(action)) {
      } else if (BluetoothLeService.ACTION_GATT_SERVICES_DISCOVERED.equals(action)) {
        ////蓝牙连接成功就自动检验密码
        new Thread(new Runnable() {

          @Override
          public void run() {
            // TODO Auto-generated method stub
            LogUtils.d("tag", "接受广播1 " +list.size()+" mac =" + mac );
            if(mDeviceBean!=null) {
              try {
                Thread.sleep(300);
              } catch (InterruptedException e) {
                // TODO Auto-generated catch block
                e.printStackTrace();
              }
              //mDeviceBean.write(CmdPackage.set))
            }
          }
        }).start();
      } else if (BluetoothLeService.ACTION_DATA_AVAILABLE.equals(action)) { //解析数据
        String buffer = intent.getStringExtra(BluetoothLeService.EXTRA_DATA);
        LogUtils.v("tag",mac+ "接受数据:"+ buffer);
        if(mBluetoothLeService!=null) {
          if(mDeviceBean!=null) {
            //mDeviceBean.get().parseData(MyHexUtils.hexStringToByte(buffer));
          }
        }
      }
    }
  };


  private static IntentFilter makeGattUpdateIntentFilter() {
    final IntentFilter intentFilter = new IntentFilter();
    intentFilter.addAction(BluetoothLeService.ACTION_GATT_CONNECTED);
    intentFilter.addAction(BluetoothLeService.ACTION_GATT_DISCONNECTED);
    intentFilter
        .addAction(BluetoothLeService.ACTION_GATT_SERVICES_DISCOVERED);
    intentFilter.addAction(BluetoothLeService.ACTION_DATA_AVAILABLE);
    return intentFilter;
  }

  @Override protected void onDestroy() {
    super.onDestroy();
  }
}
