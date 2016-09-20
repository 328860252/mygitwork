package tsocket.zby.com.tsocket.adapter;

import android.support.v7.widget.RecyclerView;
import cn.bingoogolapple.androidcommon.adapter.BGARecyclerViewAdapter;
import cn.bingoogolapple.androidcommon.adapter.BGAViewHolderHelper;
import tsocket.zby.com.tsocket.R;
import tsocket.zby.com.tsocket.bean.DeviceBean;

public class DeviceAdapter extends BGARecyclerViewAdapter<DeviceBean> {
  public DeviceAdapter(RecyclerView recyclerView) {
    super(recyclerView, R.layout.device_item);
  }

  @Override protected void fillData(BGAViewHolderHelper viewHolderHelper, int i, DeviceBean deviceBean) {
    //viewHolderHelper.setText(R.id.text_adapterStock_id, stockEntity.getNumber())
    //    .setText(R.id.text_adapterStock_last, stockEntity.getCount())
    //    .setText(R.id.text_adapterStock_version, stockEntity.getVersion())
    //    .setText(R.id.text_adapterStock_name, stockEntity.getName());
  }
}