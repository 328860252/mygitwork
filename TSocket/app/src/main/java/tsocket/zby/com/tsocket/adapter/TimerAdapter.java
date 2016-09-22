package tsocket.zby.com.tsocket.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;
import butterknife.BindView;
import butterknife.ButterKnife;
import java.util.List;
import tsocket.zby.com.tsocket.R;
import tsocket.zby.com.tsocket.bean.TimerBean;

public class TimerAdapter extends BaseAdapter {

  private Context mContext;
  private List<TimerBean> mList;
  private ViewHolder mHolder;
  private OnTimerSwitchClickListener mListener;

  public TimerAdapter(Context context, List<TimerBean> list) {
    this.mContext = context;
    this.mList = list;
  }

  @Override public int getCount() {
    return mList.size();
  }

  @Override public Object getItem(int i) {
    return mList.get(i);
  }

  @Override public long getItemId(int i) {
    return 0;
  }

  @Override public View getView(int i, View view, ViewGroup viewGroup) {
    if (view == null) {
      view = LayoutInflater.from(mContext).inflate(R.layout.list_timer_item, null);
      mHolder = new ViewHolder(view);
      view.setTag(mHolder);
    } else {
      mHolder = (ViewHolder) view.getTag();
    }
    final TimerBean timerBean = mList.get(i);
    mHolder.mTvTimer.setText(mContext.getString(R.string.text_adapter_timer, timerBean.getStartString(), timerBean.getEndString()));
    mHolder.mTvRecycle.setText(mContext.getString(R.string.text_adapter_recyclestr, timerBean.getOpenString(), timerBean.getCloseString()));
    mHolder.mTvWeek.setText(mContext.getString(R.string.text_adapter_week, timerBean.getWeekString(mContext)));
    mHolder.mIvTimerSwitch.setSelected(timerBean.isRecycle());
    mHolder.mIvTimerSwitch.setOnClickListener(new View.OnClickListener() {
      @Override public void onClick(View view) {
        if (mListener != null) {
          mListener.onTimerSwitchClick(timerBean);
        }
      }
    });
    return view;
  }

  static class ViewHolder {
    @BindView(R.id.tv_timer) TextView mTvTimer;
    @BindView(R.id.tv_recycle) TextView mTvRecycle;
    @BindView(R.id.tv_week) TextView mTvWeek;
    @BindView(R.id.iv_timer_switch) ImageView mIvTimerSwitch;

    ViewHolder(View view) {
      ButterKnife.bind(this, view);
    }
  }

  public void setListener(OnTimerSwitchClickListener listener) {
    mListener = listener;
  }

  public interface OnTimerSwitchClickListener {
    void onTimerSwitchClick(TimerBean timerBean);
  }
}