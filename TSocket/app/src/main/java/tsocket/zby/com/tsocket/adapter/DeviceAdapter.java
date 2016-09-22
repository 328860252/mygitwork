package tsocket.zby.com.tsocket.adapter;

import android.support.v7.widget.RecyclerView;

import android.view.View;
import cn.bingoogolapple.androidcommon.adapter.BGARecyclerViewAdapter;
import cn.bingoogolapple.androidcommon.adapter.BGAViewHolderHelper;
import tsocket.zby.com.tsocket.R;
import tsocket.zby.com.tsocket.bean.DeviceBean;

public class DeviceAdapter extends BGARecyclerViewAdapter<DeviceBean> {

  private OnItemClickListener mOnItemClickListener;

  public DeviceAdapter(RecyclerView recyclerView) {
    super(recyclerView, R.layout.list_device_item);
  }

  @Override protected void fillData(BGAViewHolderHelper viewHolderHelper, int i, DeviceBean deviceBean) {
    viewHolderHelper.setText(R.id.tv_name, deviceBean.getName())
        .setText(R.id.tv_mac, deviceBean.getMac());
  }

  @Override protected void setItemChildListener(final BGAViewHolderHelper viewHolderHelper) {
    super.setItemChildListener(viewHolderHelper);
    viewHolderHelper.getConvertView().setOnClickListener(new View.OnClickListener() {
      @Override public void onClick(View view) {
        if (mOnItemClickListener != null) {
          mOnItemClickListener.onClickItem(view, viewHolderHelper.getPosition());
        }
      }
    });
  }

  public void setOnItemClickListener(OnItemClickListener onItemClickListener) {
    mOnItemClickListener = onItemClickListener;
  }

  public interface OnItemClickListener {
    void onClickItem(View view, int position);
  }
}