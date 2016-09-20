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

/**
 * Created by zhuj on 2016/9/20 16:00.
 */
public class LanguageAdapter extends BaseAdapter {

  private Context mContext;
  private List<String> mList;
  private int mLanguageType = -1;
  private ViewHolder mViewHolder;

  public LanguageAdapter(Context context, List<String> list, int selectType) {
    this.mContext = context;
    this.mList = list;
    this.mLanguageType = selectType;
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
      view = LayoutInflater.from(mContext).inflate(R.layout.language_item, null);
      mViewHolder = new ViewHolder(view);
      view.setTag(mViewHolder);
    } else {
      mViewHolder = (ViewHolder) view.getTag();
    }
    mViewHolder.mTvName.setText(mList.get(i));
    mViewHolder.mIvSelect.setSelected(mLanguageType == i);
    return view;
  }

  public void updateIndex(int language_item) {
    mLanguageType = language_item;
    notifyDataSetChanged();
  }

  static class ViewHolder {
    @BindView(R.id.tv_name) TextView mTvName;
    @BindView(R.id.iv_select) ImageView mIvSelect;

    ViewHolder(View view) {
      ButterKnife.bind(this, view);
    }
  }
}
