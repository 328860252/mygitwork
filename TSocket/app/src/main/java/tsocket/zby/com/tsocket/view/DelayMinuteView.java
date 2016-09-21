package tsocket.zby.com.tsocket.view;

import android.content.Context;
import android.content.res.TypedArray;
import android.util.AttributeSet;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.TextView;
import butterknife.BindView;
import butterknife.ButterKnife;
import tsocket.zby.com.tsocket.R;

/**
 * Created by zhuj on 2016/9/21 14:44.
 */
public class DelayMinuteView extends LinearLayout {

  @BindView(R.id.tv_minute_number) TextView mTvMinuteNumber;
  @BindView(R.id.tv_minute) TextView mTvMinute;

  private boolean mChecked;

  public DelayMinuteView(Context context, AttributeSet attrs) {
    super(context, attrs);
    LayoutInflater layoutInflater = LayoutInflater.from(context);
    View view = layoutInflater.inflate(R.layout.view_delay_minute, this);
    ButterKnife.bind(view);
    init(context, attrs);
  }

  private void init(Context context, AttributeSet attrs) {
    TypedArray typedArray = context.obtainStyledAttributes(attrs, R.styleable.DelayMinuteView);
    String text = typedArray.getString(R.styleable.DelayMinuteView_text);
    boolean right = typedArray.getBoolean(R.styleable.DelayMinuteView_check, false);
    mTvMinuteNumber.setText(text);
    setChecked(right);
  }

  public boolean isChecked() {
    return mChecked;
  }

  public String getTextNumber() {
    return mTvMinuteNumber.getText().toString();
  }

  public void setChecked(boolean checked) {
    mChecked = checked;
    mTvMinute.setSelected(checked);
    mTvMinuteNumber.setSelected(checked);
  }

}
