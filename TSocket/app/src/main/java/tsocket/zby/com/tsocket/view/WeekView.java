package tsocket.zby.com.tsocket.view;

import android.content.Context;
import android.content.res.TypedArray;
import android.util.AttributeSet;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.CheckBox;
import android.widget.LinearLayout;
import butterknife.BindView;
import butterknife.ButterKnife;
import tsocket.zby.com.tsocket.R;

/**
 * 星期选中值
 * 从星期日 到星期6 （1,2,4,8,16,32,64） ，未选中 对应值为0
 * 比如星期3+星期6  ，值为 72
 * 星期1-7， 值为 127
 * Created by zhuj on 2016/9/21 14:44.
 */
public class WeekView extends LinearLayout {

  @BindView(R.id.cb_day1) CheckBox mCbDay1;
  @BindView(R.id.cb_day2) CheckBox mCbDay2;
  @BindView(R.id.cb_day3) CheckBox mCbDay3;
  @BindView(R.id.cb_day4) CheckBox mCbDay4;
  @BindView(R.id.cb_day5) CheckBox mCbDay5;
  @BindView(R.id.cb_day6) CheckBox mCbDay6;
  @BindView(R.id.cb_day7) CheckBox mCbDay7;

  public WeekView(Context context, AttributeSet attrs) {
    super(context, attrs);
    LayoutInflater layoutInflater = LayoutInflater.from(context);
    View view = layoutInflater.inflate(R.layout.view_week, this);
    ButterKnife.bind(view);
    init(context, attrs);
  }

  private void init(Context context, AttributeSet attrs) {
    TypedArray typedArray = context.obtainStyledAttributes(attrs, R.styleable.DelayMinuteView);
    int text = typedArray.getInt(R.styleable.WeekView_weekValue, 0);
    setWeekValue(text);
  }

  public int getWeekValue() {
    int i =0;
    if (mCbDay7.isChecked()) i += 1;
    if (mCbDay1.isChecked()) i += 2;
    if (mCbDay2.isChecked()) i += 4;
    if (mCbDay3.isChecked()) i += 8;
    if (mCbDay4.isChecked()) i += 16;
    if (mCbDay5.isChecked()) i += 32;
    if (mCbDay6.isChecked()) i += 64;
    return i;
  }

  public void setWeekValue(int weekValue) {
    initCheck(weekValue);
  }

  private void initCheck(int weekValue) {
    mCbDay7.setChecked(weekValue%2 == 1);
    mCbDay1.setChecked(weekValue %4 /2 == 1);
    mCbDay2.setChecked(weekValue %8 /4 == 1);
    mCbDay3.setChecked(weekValue %16 /8 == 1);
    mCbDay4.setChecked(weekValue %32 /16 == 1);
    mCbDay5.setChecked(weekValue %64 /32 == 1);
    mCbDay6.setChecked(weekValue /64 == 1);
  }
}
