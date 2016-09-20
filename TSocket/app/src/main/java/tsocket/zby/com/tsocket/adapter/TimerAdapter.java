package tsocket.zby.com.tsocket.adapter;

import android.support.v7.widget.RecyclerView;

import java.util.Timer;

import cn.bingoogolapple.androidcommon.adapter.BGARecyclerViewAdapter;
import cn.bingoogolapple.androidcommon.adapter.BGAViewHolderHelper;
import tsocket.zby.com.tsocket.R;
import tsocket.zby.com.tsocket.bean.BluetoothBean;
import tsocket.zby.com.tsocket.bean.TimerBean;

public class TimerAdapter extends BGARecyclerViewAdapter<TimerBean> {

  public TimerAdapter(RecyclerView recyclerView) {
    super(recyclerView, R.layout.device_item);
  }

  @Override protected void fillData(BGAViewHolderHelper viewHolderHelper, int i, TimerBean deviceBean) {
//    viewHolderHelper.setText(R.id.tv_name, deviceBean.getName())
//        .setText(R.id.tv_mac, deviceBean.getMac());
  }
}