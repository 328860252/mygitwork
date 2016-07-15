package com.interphone.view.wheel;

import android.content.Context;
import android.content.res.TypedArray;
import android.util.AttributeSet;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;
import butterknife.Bind;
import butterknife.ButterKnife;
import com.example.administrator.interphone.R;

/**
 * Created by Administrator on 2016/6/18.
 */
public class HeaderLayout extends RelativeLayout {

  @Bind(R.id.tv_title_left) TextView mTvTitleLeft;
  @Bind(R.id.layout_title_left) LinearLayout mLayoutTitleLeft;
  @Bind(R.id.tv_title) TextView mTvTitle;
  @Bind(R.id.tv_title_right) TextView mTvTitleRight;
  @Bind(R.id.layout_title_right) LinearLayout mLayoutTitleRight;
  @Bind(R.id.layout_title) RelativeLayout mLayoutTitle;

  public HeaderLayout(Context context) {
    super(context);
  }

  public HeaderLayout(Context context, AttributeSet attrs) {
    super(context, attrs);
    LayoutInflater layoutInflater = LayoutInflater.from(context);
    View view = layoutInflater.inflate(R.layout.layout_title, this);
    ButterKnife.bind(view);
    init(context, attrs);
  }

  private void init(Context context , AttributeSet attrs) {
    TypedArray typedArray = context.obtainStyledAttributes(attrs, R.styleable.headerLayout);
    String title = typedArray.getString(R.styleable.headerLayout_textTitle);
    String right = typedArray.getString(R.styleable.headerLayout_textRight);
    mTvTitle.setText(title);
    mTvTitleRight.setText(right);
  }
}
