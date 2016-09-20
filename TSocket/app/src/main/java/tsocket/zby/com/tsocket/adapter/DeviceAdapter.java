package tsocket.zby.com.tsocket.adapter;

import android.support.v7.widget.RecyclerView;

import cn.bingoogolapple.androidcommon.adapter.BGAOnItemChildClickListener;
import cn.bingoogolapple.androidcommon.adapter.BGARecyclerViewAdapter;
import cn.bingoogolapple.androidcommon.adapter.BGAViewHolderHelper;
import tsocket.zby.com.tsocket.R;
import tsocket.zby.com.tsocket.bean.BluetoothBean;
import tsocket.zby.com.tsocket.bean.DeviceBean;

public class DeviceAdapter extends BGARecyclerViewAdapter<BluetoothBean> {

  public DeviceAdapter(RecyclerView recyclerView) {
    super(recyclerView, R.layout.device_item);
  }

  @Override protected void fillData(BGAViewHolderHelper viewHolderHelper, int i, BluetoothBean deviceBean) {
    viewHolderHelper.setText(R.id.tv_name, deviceBean.getName())
        .setText(R.id.tv_mac, deviceBean.getMac());
  }
}