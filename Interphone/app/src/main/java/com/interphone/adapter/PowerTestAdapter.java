package com.interphone.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.SeekBar;
import android.widget.TextView;
import butterknife.Bind;
import butterknife.ButterKnife;
import com.example.administrator.interphone.R;
import com.interphone.bean.DeviceBean;
import com.interphone.bean.PowerTestData;
import com.interphone.bean.PowerTestSeekBean;
import java.util.ArrayList;
import java.util.List;

/**
 * Created by zhuj on 2016/7/6 17:22.
 */
public class PowerTestAdapter extends BaseAdapter {

    private Context mContext;
    private List<PowerTestSeekBean> list;
    private OnScroolListener mOnScroolListener;

    public PowerTestAdapter(Context context) {
        this.mContext = context;
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

    @Override public View getView(final int position, View convertView, ViewGroup parent) {
        final ViewHolder holder;
        if (convertView != null) {
            holder = (ViewHolder) convertView.getTag();
        } else {
            convertView = LayoutInflater.from(mContext).inflate(R.layout.adapter_power_test, null);
            holder = new ViewHolder(convertView);
            convertView.setTag(holder);
        }
        holder.mTextViewName.setText(list.get(position).getName());
        holder.mSearchBar.setProgress(list.get(position).getValue());
        holder.mTextViewValue.setText(""+list.get(position).getValue());
        holder.mTextViewId.setText(""+list.get(position).getId());
        holder.mSearchBar.setOnSeekBarChangeListener(new SeekBar.OnSeekBarChangeListener() {
            @Override
            public void onProgressChanged(SeekBar seekBar, int progress, boolean fromUser) {
                holder.mTextViewValue.setText(""+progress);
            }

            @Override public void onStartTrackingTouch(SeekBar seekBar) {

            }

            @Override public void onStopTrackingTouch(SeekBar seekBar) {
                if (mOnScroolListener != null) {
                    list.get(position).setValue(seekBar.getProgress());
                    mOnScroolListener.onSeekbarScroll(position, list.get(position).getId(), seekBar.getProgress());
                }
            }
        });
        return convertView;
    }

    public void setList(List<PowerTestSeekBean> list) {
        this.list = list;
    }

    public List<String> getListValue() {
        List<String> listString = new ArrayList<>();
        for (int i=0; i< list.size(); i++) {
            listString.add("" + list.get(i).getValue());
        }
        return listString;
    }


    /**
     * 清零
     * @param isUHF
     * @return
     */
    public List<PowerTestSeekBean> getListCleanValue(boolean isUHF) {
        List<PowerTestSeekBean> list = new ArrayList<>();

        String[] nameStr;
        if (isUHF) {
            nameStr = new String[] { "409.025", "435.025", "469.025" };
        } else {
            nameStr = new String[] { "142.025", "155.025", "172.025" };
        }

        for (int i = 0; i < 3; i++) {
            //编号 uhf 0-9 vhf 10-18
            list.add(new PowerTestSeekBean((isUHF ? 0 : 9) + i * 3 + 1, nameStr[i] + "低频", 0));
            list.add(new PowerTestSeekBean((isUHF ? 0 : 9) + i * 3 + 2, nameStr[i] + "中频", 0));
            list.add(new PowerTestSeekBean((isUHF ? 0 : 9) + i * 3 + 3, nameStr[i] + "高频", 0));
        }
        return list;
    }

    public List<PowerTestSeekBean> getList(DeviceBean dbin, boolean isUHF) {
        List<PowerTestSeekBean> list = new ArrayList<>();

        String[] nameStr;
        if (isUHF) {
            nameStr = new String[] { "409.025", "435.025", "469.025" };
        } else {
            nameStr = new String[] { "142.025", "155.025", "172.025" };
        }

        List<PowerTestData> listData = dbin.getListPower();
        for (int i = 0; i < listData.size(); i++) {
            //编号 uhf 0-9 vhf 10-18
            list.add(new PowerTestSeekBean((isUHF ? 0 : 9) + i * 3 + 1, nameStr[i] + "低频",
                listData.get(i).getPowerLow()));
            list.add(new PowerTestSeekBean((isUHF ? 0 : 9) + i * 3 + 2, nameStr[i] + "中频",
                listData.get(i).getPowerMiddle()));
            list.add(new PowerTestSeekBean((isUHF ? 0 : 9) + i * 3 + 3, nameStr[i] + "高频",
                listData.get(i).getPowerHigh()));
        }
        return list;
    }

    public void setOnScroolListener(OnScroolListener listener) {
        this.mOnScroolListener = listener;
    }

    public interface OnScroolListener {
        public void onSeekbarScroll(int position, int id, int value);
    }

    class ViewHolder {
        @Bind(R.id.textView_id) TextView mTextViewId;
        @Bind(R.id.textView_value) TextView mTextViewValue;
        @Bind(R.id.textView_name) TextView mTextViewName;
        @Bind(R.id.search_bar) SeekBar mSearchBar;

        ViewHolder(View view) {
            ButterKnife.bind(this, view);
        }
    }
}
