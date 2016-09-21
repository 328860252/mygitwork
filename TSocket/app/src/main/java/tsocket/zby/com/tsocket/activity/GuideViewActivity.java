package tsocket.zby.com.tsocket.activity;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.os.Parcelable;
import android.support.v4.view.PagerAdapter;
import android.support.v4.view.ViewPager;
import android.support.v4.view.ViewPager.OnPageChangeListener;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.widget.ImageView;
import java.util.ArrayList;
import tsocket.zby.com.tsocket.AppString;
import tsocket.zby.com.tsocket.R;
import tsocket.zby.com.tsocket.utils.MyImage;
import tsocket.zby.com.tsocket.utils.SharedPerfenceUtils;
import tsocket.zby.com.tsocket.utils.Tools;

/**
 * <p>Description: 引导图</p>
 *
 * @author zhujiang
 * @date 2014-5-28
 */
public class GuideViewActivity extends Activity {

  private static final String TAG = "GuideViewActivity";

  private ViewPager viewPager;
  private ArrayList<View> pageViews;
  private ViewGroup group;
  private ImageView imageView;
  private ImageView[] imageViews;

  //滑动到边上时结束
  private ImageView imageView_guideOver;

  int isScrolledCount = 0;

  /** Called when the activity is first created. */
  @Override public void onCreate(Bundle savedInstanceState) {
    super.onCreate(savedInstanceState);
    requestWindowFeature(Window.FEATURE_NO_TITLE);
    setContentView(R.layout.guide);
    initView();
    imageViews = new ImageView[pageViews.size()];
    // group是R.layou.main中的负责包裹小圆点的LinearLayout.
    group = (ViewGroup) findViewById(R.id.layout_viewGroup);

    viewPager = (ViewPager) findViewById(R.id.guidePages);

    //for (int i = 0; i < pageViews.size(); i++) {
    //  imageView = new ImageView(GuideViewActivity.this);
    //  imageView.setLayoutParams(new LayoutParams(30,30));
    //  imageView.setPadding(50, 10, 50, 10);
    //  imageViews[i] = imageView;
    //  if (i == 0) {
    //    //默认选中第一张图
    //    imageViews[i].setBackgroundResource(R.drawable.green_point);
    //  }else {
    //    imageViews[i].setBackgroundResource(R.drawable.gray_point);
    //  }
    //  group.addView(imageViews[i]);
    //}
    viewPager.setAdapter(new GuidePageAdapter());
    viewPager.setOnPageChangeListener(new GuidePageChangeListener());
    SharedPerfenceUtils.getSetupData(this).saveboolean(AppString.FIRST, false);
  }

  /**
   * 初始化控件
   */
  private void initView() {
    // TODO Auto-generated method stub

    pageViews = new ArrayList<View>();
    ImageView view1 = new ImageView(this);
    ImageView view2 = new ImageView(this);
    ImageView view3 = new ImageView(this);
    ImageView view4 = new ImageView(this);
    ImageView view5 = new ImageView(this);
    ImageView view6 = new ImageView(this);
    if (Tools.isLocalLanguageChina(this)) {
      view1.setBackgroundDrawable(MyImage.decodeFileBitmapDrawable(getResources(), R.mipmap.guide1, 800, 600, new MyImage.ScalingLogic()));
      view2.setBackgroundDrawable(MyImage.decodeFileBitmapDrawable(getResources(), R.mipmap.guide2, 800, 600, new MyImage.ScalingLogic()));
      view3.setBackgroundDrawable(MyImage.decodeFileBitmapDrawable(getResources(), R.mipmap.guide3, 800, 600, new MyImage.ScalingLogic()));
      view4.setBackgroundDrawable(MyImage.decodeFileBitmapDrawable(getResources(), R.mipmap.guide4, 800, 600, new MyImage.ScalingLogic()));
      view5.setBackgroundDrawable(MyImage.decodeFileBitmapDrawable(getResources(), R.mipmap.guide5, 800, 600, new MyImage.ScalingLogic()));
      view6.setBackgroundDrawable(MyImage.decodeFileBitmapDrawable(getResources(), R.mipmap.guide6, 800, 600, new MyImage.ScalingLogic()));
    } else {
      view1.setBackgroundDrawable(MyImage.decodeFileBitmapDrawable(getResources(), R.mipmap.guide1_en, 800, 600, new MyImage.ScalingLogic()));
      view2.setBackgroundDrawable(MyImage.decodeFileBitmapDrawable(getResources(), R.mipmap.guide2_en, 800, 600, new MyImage.ScalingLogic()));
      view3.setBackgroundDrawable(MyImage.decodeFileBitmapDrawable(getResources(), R.mipmap.guide3_en, 800, 600, new MyImage.ScalingLogic()));
      view4.setBackgroundDrawable(MyImage.decodeFileBitmapDrawable(getResources(), R.mipmap.guide4_en, 800, 600, new MyImage.ScalingLogic()));
      view5.setBackgroundDrawable(MyImage.decodeFileBitmapDrawable(getResources(), R.mipmap.guide5_en, 800, 600, new MyImage.ScalingLogic()));
      view6.setBackgroundDrawable(MyImage.decodeFileBitmapDrawable(getResources(), R.mipmap.guide6_en, 800, 600, new MyImage.ScalingLogic()));
    }
    pageViews.add(view1);
    pageViews.add(view2);
    pageViews.add(view3);
    pageViews.add(view4);
    pageViews.add(view5);
    pageViews.add(view6);
  }

  /** 指引页面Adapter */
  class GuidePageAdapter extends PagerAdapter {

    @Override public int getCount() {
      return pageViews.size();
    }

    @Override public boolean isViewFromObject(View arg0, Object arg1) {
      return arg0 == arg1;
    }

    @Override public int getItemPosition(Object object) {
      // TODO Auto-generated method stub
      return super.getItemPosition(object);
    }

    @Override public void destroyItem(View arg0, int arg1, Object arg2) {
      // TODO Auto-generated method stub
      ((ViewPager) arg0).removeView(pageViews.get(arg1));
    }

    @Override public Object instantiateItem(View arg0, int arg1) {
      // TODO Auto-generated method stub
      ((ViewPager) arg0).addView(pageViews.get(arg1));
      return pageViews.get(arg1);
    }

    @Override public void restoreState(Parcelable arg0, ClassLoader arg1) {
      // TODO Auto-generated method stub

    }

    @Override public Parcelable saveState() {
      // TODO Auto-generated method stub
      return null;
    }

    @Override public void startUpdate(View arg0) {
      // TODO Auto-generated method stub

    }

    @Override public void finishUpdate(View arg0) {
      // TODO Auto-generated method stub

    }
  }

  /**
   * 指引页面改监听器, 小圆点显示正处于第几个页面
   */
  class GuidePageChangeListener implements OnPageChangeListener {

    @Override public void onPageScrollStateChanged(int arg0) {
      // TODO Auto-generated method stub
    }

    @Override public void onPageScrolled(int arg0, float arg1, int arg2) {
      // TODO Auto-generated method stub
      // 最后一页划不动。
      // 进入最后一页，arg0 会出现一次
      // 然后滑动，arg0 就会多次出现
      if (arg0 == (imageViews.length - 1)) {
        isScrolledCount++;
        if (isScrolledCount > 1) {// 表示着最后一个界面往右滑动了
          //第一次启动，是引导页，要到设备列表界面
          //之后都是当做帮助页
          if (SharedPerfenceUtils.getSetupData(GuideViewActivity.this).readBoolean(AppString.FIRST, true) ) {
            SharedPerfenceUtils.getSetupData(GuideViewActivity.this).saveboolean(AppString.FIRST, false);
            Intent intent = new Intent(GuideViewActivity.this, DeviceListActivity.class);
            startActivity(intent);
          }
          finish();
        }
      } else {
        isScrolledCount = 0;
      }
    }

    @Override public void onPageSelected(int arg0) {

    }
  }
}