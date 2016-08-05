package com.interphone.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;
import butterknife.Bind;
import butterknife.ButterKnife;
import com.example.administrator.interphone.R;
import com.interphone.bean.SmsEntity;
import com.interphone.utils.StringUtils;
import java.util.List;

/**
 * Created by zhuj on 2016/8/1 16:59.
 */
public class SmsAdapter extends BaseAdapter {

  private List<SmsEntity> list;
  private Context mContext;
  private ViewHolder mViewHolder;

  public SmsAdapter(Context context, List<SmsEntity> smsEntityList) {
    mContext = context;
    list = smsEntityList;
  }

  @Override public int getCount() {
    return list.size();
  }

  @Override public Object getItem(int position) {
    return list.get(position);
  }

  @Override public long getItemId(int position) {
    return position;
  }

  @Override public View getView(int position, View convertView, ViewGroup parent) {
    if (convertView == null) {
      convertView = LayoutInflater.from(mContext).inflate(R.layout.adapter_sms, null);
      mViewHolder = new ViewHolder(convertView);
      convertView.setTag(mViewHolder);
    } else {
      mViewHolder = (ViewHolder) convertView.getTag();
    }
    SmsEntity smsEntity = list.get(position);
    mViewHolder.mTextViewContent.setText(smsEntity.getContent());
    mViewHolder.mTextViewId.setText(""+(position+1));
    mViewHolder.mTextViewTime.setText(StringUtils.timeFormat(smsEntity.getDataTime()));
    return convertView;
  }

  static class ViewHolder {
    @Bind(R.id.textView_id) TextView mTextViewId;
    @Bind(R.id.textView_time) TextView mTextViewTime;
    @Bind(R.id.textView_content) TextView mTextViewContent;

    ViewHolder(View view) {
      ButterKnife.bind(this, view);
    }
  }
}