package tsocket.zby.com.tsocket.view;

import android.content.Context;
import android.content.res.TypedArray;
import android.util.AttributeSet;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;
import butterknife.BindView;
import butterknife.ButterKnife;
import tsocket.zby.com.tsocket.R;

/**
 * Created by Administrator on 2016/6/18.
 */
public class HeaderLayout extends RelativeLayout {

  @BindView(R.id.tv_title_left) TextView mTvTitleLeft;
  @BindView(R.id.layout_title_left) LinearLayout mLayoutTitleLeft;
  @BindView(R.id.tv_title) TextView mTvTitle;
  @BindView(R.id.tv_title_right) TextView mTvTitleRight;
  @BindView(R.id.layout_title_right) LinearLayout mLayoutTitleRight;

  public HeaderLayout(Context context) {
    super(context);
  }

  public HeaderLayout(Context context, AttributeSet attrs) {
    super(context, attrs);
    LayoutInflater layoutInflater = LayoutInflater.from(context);
    View view = layoutInflater.inflate(R.layout.view_header_layout, this);
    ButterKnife.bind(view);
    init(context, attrs);
  }

  private void init(Context context , AttributeSet attrs) {
    TypedArray typedArray = context.obtainStyledAttributes(attrs, R.styleable.HeaderLayout);
    String title = typedArray.getString(R.styleable.HeaderLayout_textTitle);
    String right = typedArray.getString(R.styleable.HeaderLayout_textRight);
    //int mTitleResId = typedArray.getResourceId(R.styleable.headerLayout_titleIcon, 0);
    int mRightResId = typedArray.getResourceId(R.styleable.HeaderLayout_rightIcon, 0);
    int mLeftResId = typedArray.getResourceId(R.styleable.HeaderLayout_leftIcon, 0);
    boolean mShowRight = typedArray.getBoolean(R.styleable.HeaderLayout_showRight, false);
    boolean mShowBack = typedArray.getBoolean(R.styleable.HeaderLayout_showBack, true);

    if (title!=null) mTvTitle.setText(title);

    if (mRightResId!=0) {//有图片优先显示图片
      mTvTitleRight.setBackgroundResource(mRightResId);
    } else {
      if (right != null) {
        mTvTitleRight.setText(right);
      }
    }
    if (mLeftResId!=0) {//有图片优先显示图片
      mTvTitleLeft.setBackgroundResource(mLeftResId);
      mTvTitleLeft.setText("");
    }
    mLayoutTitleRight.setVisibility(mShowRight ? View.VISIBLE:View.GONE);
    mLayoutTitleLeft.setVisibility(mShowBack ? View.VISIBLE:View.GONE);
    //mTvTitleRight.setText(right);
  }

  public void setTextTitle(int textTitle) {
    mTvTitle.setText(textTitle);
  }

  public void setTextTitle(String textTitle) {
    mTvTitle.setText(textTitle);
  }

  public TextView getLeftText() {
    return mTvTitleLeft;
  }
}
