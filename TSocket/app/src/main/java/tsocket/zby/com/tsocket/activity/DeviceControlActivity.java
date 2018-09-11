package tsocket.zby.com.tsocket.activity;

import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.util.Log;
import android.view.View;
import android.view.ViewGroup;
import android.widget.CheckedTextView;
import android.widget.TextView;
import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import com.yanzhenjie.recyclerview.swipe.Closeable;
import com.yanzhenjie.recyclerview.swipe.OnSwipeMenuItemClickListener;
import com.yanzhenjie.recyclerview.swipe.SwipeMenu;
import com.yanzhenjie.recyclerview.swipe.SwipeMenuCreator;
import com.yanzhenjie.recyclerview.swipe.SwipeMenuItem;
import com.yanzhenjie.recyclerview.swipe.SwipeMenuRecyclerView;
import java.util.List;
import java.util.concurrent.TimeUnit;
import rx.Observable;
import rx.Subscriber;
import rx.Subscription;
import rx.android.schedulers.AndroidSchedulers;
import rx.schedulers.Schedulers;
import tsocket.zby.com.tsocket.AppApplication;
import tsocket.zby.com.tsocket.R;
import tsocket.zby.com.tsocket.adapter.OnItemClickListener;
import tsocket.zby.com.tsocket.adapter.OnTimerSwitchClickListener;
import tsocket.zby.com.tsocket.adapter.TimerAdapter;
import tsocket.zby.com.tsocket.bean.DeviceBean;
import tsocket.zby.com.tsocket.bean.TimerBean;
import tsocket.zby.com.tsocket.connection.ConnectAction;
import tsocket.zby.com.tsocket.connection.agreement.CmdPackage;
import tsocket.zby.com.tsocket.connection.agreement.CmdParseImpl;
import tsocket.zby.com.tsocket.utils.LogUtils;
import tsocket.zby.com.tsocket.view.HeaderLayout;
import tsocket.zby.com.tsocket.view.ListViewDecoration;

public class DeviceControlActivity extends BaseActivity {

  @BindView(R.id.headerLayout) HeaderLayout headerLayout;
  @BindView(R.id.tv_delay) TextView tvDelay;
  @BindView(R.id.tv_timer) TextView tvTimer;
  @BindView(R.id.ctv_switch) CheckedTextView ctvSwitch;
  @BindView(R.id.recycler_view) SwipeMenuRecyclerView mSwipeMenuRecyclerView;
  @BindView(R.id.tv_downCount) TextView mTvDownCount;
  private TimerAdapter mTimerAdapter;
  private List<TimerBean> list;
  private DeviceBean mDeviceBean;
  private final int activity_timer = 11;
  private final int activity_delay = 12;
  //private Subscription mTimeSubscription;

  @Override protected void onCreate(Bundle savedInstanceState) {
    super.onCreate(savedInstanceState);
    setContentView(R.layout.activity_device_control);
    ButterKnife.bind(this);
    initViews();
    subscribePushMessage();

    //Observable.interval(1, TimeUnit.SECONDS).subscribe(new Action1<Long>() {
    //  @Override
    //  public void call(Long aLong) {
    //    byte[] bb = new byte[4];
    //    bb[0] = (byte)0xB1;
    //    bb[1] = (byte) ( aLong%2);
    //    if (mDeviceBean.getParse()==null) {
    //      mDeviceBean.setParse(new CmdParseImpl(mDeviceBean, DeviceControlActivity.this));
    //    }
    //    mDeviceBean.getParse().parseData(bb);
    //    Log.w("test", "fasong ");
    //  }
    //});
    ((AppApplication) getApplication()).startDownCount();
  }

  private void initViews() {
    mDeviceBean = mApp.getDeviceBean();
    list = mDeviceBean.getTimerBeanList();
    mTimerAdapter = new TimerAdapter(this, list);
    headerLayout.setTextTitle(mDeviceBean.getName());
    ctvSwitch.setChecked(mDeviceBean.isOnOff());
    mTimerAdapter.setTimerSwitchListener(new OnTimerSwitchClickListener() {
      @Override public void onTimerSwitchClick(TimerBean timerBean) {
        timerBean.setEnable(!timerBean.isEnable());
        mDeviceBean.write(CmdPackage.setTimer(timerBean));
        mTimerAdapter.notifyDataSetChanged();
      }
    });

    mSwipeMenuRecyclerView.setLayoutManager(new LinearLayoutManager(this));// 布局管理器。
    //mSwipeMenuRecyclerView.setHasFixedSize(true);// 如果Item够简单，高度是确定的，打开FixSize将提高性能。
    //mSwipeMenuRecyclerView.setItemAnimator(new DefaultItemAnimator());// 设置Item默认动画，加也行，不加也行。
    mSwipeMenuRecyclerView.addItemDecoration(new ListViewDecoration(this));// 添加分割线。
    // 设置菜单创建器。
    mSwipeMenuRecyclerView.setSwipeMenuCreator(swipeMenuCreator);
    // 设置菜单Item点击监听。
    mSwipeMenuRecyclerView.setSwipeMenuItemClickListener(menuItemClickListener);
    mTimerAdapter.setOnItemClickListener(new OnItemClickListener() {
      @Override public void onClickItem(View view, int position) {
        Intent intent = new Intent(DeviceControlActivity.this, TimerActivity.class);
        intent.putExtra("timer", list.get(position));
        startActivityForResult(intent, activity_timer);
      }
    });
    mSwipeMenuRecyclerView.setAdapter(mTimerAdapter);

    //startDownCount();
  }

  @Override protected void onReceiverCmd(Object message) {
    super.onReceiverCmd(message);
    if (message instanceof Byte) {
      byte b = (byte) message;
      switch (b) {
        case CmdParseImpl.type_status://状态
          ctvSwitch.setChecked(mDeviceBean.isOnOff());
          break;
        case CmdParseImpl.type_timer://定时
          mTimerAdapter.notifyDataSetChanged();
          break;
        case CmdParseImpl.type_downCount:
          //startDownCount();
          mTvDownCount.setText(mDeviceBean.getDownCountString());
          break;
          default:
      }
    } else if (message instanceof String) {
          LogUtils.v("deviceList", "onReceiver  "+ message);
          if (message.equals(ConnectAction.ACTION_GATT_SERVICES_DISCOVERED)) {//发现服务才算连接上
            headerLayout.getLeftText().setSelected(false);
          } else if (message.equals(ConnectAction.ACTION_GATT_DISCONNECTED)) {
            headerLayout.getLeftText().setSelected(true);
          }
      }

  }

  @Override protected void onActivityResult(int requestCode, int resultCode, Intent data) {
    super.onActivityResult(requestCode, resultCode, data);
    switch (requestCode) {
      case activity_timer:
        if (resultCode == RESULT_OK) {
          mTimerAdapter.notifyDataSetChanged();
        }
        break;
      case activity_delay:
        if (resultCode == RESULT_OK) {
          //startDownCount();
        }
        break;
        default:
    }
  }

  @OnClick(R.id.layout_title_left) public void onBack() {
    finish();
  }

  @OnClick(R.id.layout_title_right) public void onSetting() {
    Intent intent = new Intent(this, SettingActivity.class);
    startActivity(intent);
  }

  @Override protected void onLanguageChange() {
    tvDelay.setText(R.string.text_delay);
    tvTimer.setText(R.string.text_timer);
    mTimerAdapter.notifyDataSetChanged();
    super.onLanguageChange();
  }

  @OnClick({ R.id.tv_delay, R.id.ctv_switch, R.id.tv_timer }) public void onClick(View view) {
    Intent intent;
    switch (view.getId()) {
      case R.id.tv_delay:
        intent = new Intent(this, DelayActivity.class);
        startActivityForResult(intent, activity_delay);
        break;
      case R.id.ctv_switch:
        ctvSwitch.setChecked(!ctvSwitch.isChecked());
        if (mDeviceBean.write(CmdPackage.setSwitch(ctvSwitch.isChecked()))) {
          //if (mDeviceBean.getDownCountSecond()>0) {
          mDeviceBean.setDownCountSecond(0);
          mTvDownCount.setText("");
          //  //unTimeSubscription();
          //}
        }
        break;
      case R.id.tv_timer:
        intent = new Intent(this, TimerActivity.class);
        startActivityForResult(intent, activity_timer);
        break;
        default:
    }
  }

  //private void startDownCount() {
    //unTimeSubscription();
    //mTimeSubscription = Observable.interval(1, TimeUnit.SECONDS)
    //    .subscribeOn(Schedulers.io())
    //    .observeOn(AndroidSchedulers.mainThread())
    //    //.unsubscribeOn(AndroidSchedulers.mainThread())
    //    .subscribe(new Subscriber<Long>() {
    //      @Override public void onCompleted() {
    //        Log.e("------ onCompleted ", "onCompleted");
    //        mTvDownCount.setText("");
    //        this.unsubscribe();
    //      }
    //
    //      @Override public void onError(Throwable e) {
    //        e.printStackTrace();
    //        Log.e("------ onError ", "onCompleted");
    //      }
    //
    //      @Override public void onNext(Long aLong) {
    //        Log.e("------ onNext ", "aLong : " + aLong);
    //        if (mDeviceBean.getDownCountSecond()>0) {
    //          mTvDownCount.setText(mDeviceBean.getDownCountString());
    //        } else {
    //          mTvDownCount.setText("");
    //          this.unsubscribe();
    //        }
    //      }
    //    });
  //}

  /**
   * 菜单点击监听。
   */
  private OnSwipeMenuItemClickListener menuItemClickListener = new OnSwipeMenuItemClickListener() {
    /**
     * Item的菜单被点击的时候调用。
     * @param closeable       closeable. 用来关闭菜单。
     * @param adapterPosition adapterPosition. 这个菜单所在的item在Adapter中position。
     * @param menuPosition    menuPosition. 这个菜单的position。比如你为某个Item创建了2个MenuItem，那么这个position可能是是 0、1，
     * @param direction       如果是左侧菜单，值是：SwipeMenuRecyclerView#LEFT_DIRECTION，如果是右侧菜单，值是：SwipeMenuRecyclerView#RIGHT_DIRECTION.
     */
    @Override public void onItemClick(Closeable closeable, int adapterPosition, int menuPosition,
        int direction) {
      closeable.smoothCloseMenu();// 关闭被点击的菜单。
      //if (direction == SwipeMenuRecyclerView.RIGHT_DIRECTION) {
      //  Toast.makeText(mContext, "list第" + adapterPosition + "; 右侧菜单第" + menuPosition, Toast.LENGTH_SHORT).show();
      //} else if (direction == SwipeMenuRecyclerView.LEFT_DIRECTION) {
      //  Toast.makeText(mContext, "list第" + adapterPosition + "; 左侧菜单第" + menuPosition, Toast.LENGTH_SHORT).show();
      //}
      // TODO 如果是删除：推荐调用Adapter.notifyItemRemoved(position)，不推荐Adapter.notifyDataSetChanged();
      if (menuPosition == 0) {// 删除按钮被点击。
        TimerBean timerBean = list.get(adapterPosition);
        timerBean.setDelete(true);
        mDeviceBean.write(CmdPackage.setTimer(timerBean));
        list.remove(adapterPosition);
        mTimerAdapter.notifyItemRemoved(adapterPosition);
      }
    }
  };

  /**
   * 菜单创建器。在Item要创建菜单的时候调用。
   */
  private SwipeMenuCreator swipeMenuCreator = new SwipeMenuCreator() {
    @Override
    public void onCreateMenu(SwipeMenu swipeLeftMenu, SwipeMenu swipeRightMenu, int viewType) {
      // MATCH_PARENT 自适应高度，保持和内容一样高；也可以指定菜单具体高度，也可以用WRAP_CONTENT。
      int height = ViewGroup.LayoutParams.MATCH_PARENT;
      SwipeMenuItem deleteItem =
          new SwipeMenuItem(DeviceControlActivity.this).setBackgroundDrawable(R.drawable.btn_delete)
              //.setImage(R.mipmap.ic_launcher) // 图标。
              .setText("删除") // 文字。
              .setTextColor(Color.WHITE) // 文字颜色。
              .setTextSize(16) // 文字大小。
              .setWidth((int) (phone_density * 100)).setHeight(height);
      swipeRightMenu.addMenuItem(deleteItem);// 添加一个按钮到右侧侧菜单。.

      // 上面的菜单哪边不要菜单就不要添加。
    }
  };

  @Override protected void onResume() {
    headerLayout.setTextTitle(mDeviceBean.getName());
    super.onResume();
  }

  @Override protected void onDestroy() {
    //unTimeSubscription();
    unSubscribePushMessage();
    super.onDestroy();
  }

  //private void unTimeSubscription() {
  //  if (mTimeSubscription!=null) {
  //    if (!mTimeSubscription.isUnsubscribed()) {
  //      mTimeSubscription.unsubscribe();
  //    }
  //    mTimeSubscription = null;
  //  }
  //}
}
