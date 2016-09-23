package tsocket.zby.com.tsocket.adapter;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;
import butterknife.BindView;
import butterknife.ButterKnife;
import com.yanzhenjie.recyclerview.swipe.SwipeMenuAdapter;
import java.util.List;
import tsocket.zby.com.tsocket.R;
import tsocket.zby.com.tsocket.bean.TimerBean;

public class TimerAdapter extends SwipeMenuAdapter<TimerAdapter.TimerViewHolder> {

  private Context mContext;
  private List<TimerBean> mList;
  private OnTimerSwitchClickListener mListener;

  private OnItemClickListener mOnItemClickListener;

  public TimerAdapter(Context context, List<TimerBean> list) {
    this.mContext = context;
    this.mList = list;
  }

  @Override public View onCreateContentView(ViewGroup parent, int viewType) {
    return LayoutInflater.from(parent.getContext()).inflate(R.layout.list_timer_item, parent, false);
  }

  @Override
  public TimerAdapter.TimerViewHolder onCompatCreateViewHolder(View realContentView, int viewType) {
    return new TimerViewHolder(realContentView);
  }

  @Override public void onBindViewHolder(TimerViewHolder holder, int position) {
    final TimerBean timerBean = mList.get(position);
    holder.mTvTimer.setText(String.format(mContext.getString(R.string.text_adapter_timer), timerBean.getStartString(), timerBean.getEndString()));
    if (timerBean.isRecycle()) {
      holder.mTvRecycle.setText(String.format(mContext.getString(R.string.text_adapter_recyclestr), timerBean.getOpenString(), timerBean.getCloseString()));
    } else {
      holder.mTvRecycle.setText("");
    }
    holder.mTvWeek.setText(String.format(mContext.getString(R.string.text_adapter_week), timerBean.getWeekString(mContext)));
    holder.mIvTimerSwitch.setSelected(timerBean.isEnable());
    holder.mIvTimerSwitch.setOnClickListener(new View.OnClickListener() {
      @Override public void onClick(View view) {
        if (mListener != null) {
          mListener.onTimerSwitchClick(timerBean);
        }
      }
    });
    holder.setOnItemClickListener(mOnItemClickListener);
  }

  @Override public int getItemCount() {
    return mList.size();
  }

  static class TimerViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener {
    @BindView(R.id.tv_timer) TextView mTvTimer;
    @BindView(R.id.tv_recycle) TextView mTvRecycle;
    @BindView(R.id.tv_week) TextView mTvWeek;
    @BindView(R.id.iv_timer_switch) ImageView mIvTimerSwitch;
    OnItemClickListener mClickListener;

    TimerViewHolder(View view) {
      super(view);
      itemView.setOnClickListener(this);
      ButterKnife.bind(this, view);
    }

    @Override public void onClick(View view) {
      if (mClickListener != null) {
        mClickListener.onClickItem(view , getAdapterPosition());
      }
    }

    public void setOnItemClickListener(OnItemClickListener onItemClickListener) {
      this.mClickListener = onItemClickListener;
    }
  }

  public void setTimerSwitchListener(OnTimerSwitchClickListener listener) {
    mListener = listener;
  }

  public void setOnItemClickListener(OnItemClickListener onItemClickListener) {
    mOnItemClickListener = onItemClickListener;
  }
}