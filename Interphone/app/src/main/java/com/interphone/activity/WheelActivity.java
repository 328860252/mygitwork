package com.interphone.activity;

import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.view.Display;
import android.view.Gravity;
import android.view.View;
import android.view.WindowManager;
import android.widget.LinearLayout;
import android.widget.TextView;
import butterknife.Bind;
import butterknife.ButterKnife;
import butterknife.OnClick;
import com.example.administrator.interphone.R;
import com.interphone.AppString;
import com.interphone.utils.WheelUtils;
import com.interphone.view.wheel.OnWheelChangedListener;
import com.interphone.view.wheel.WheelView;

public class WheelActivity extends BaseActivity {

    @Bind(R.id.tv_back)         TextView        mTvBack;
    @Bind(R.id.tv_title)        TextView        mTvTitle;
    @Bind(R.id.tv_confirm)      TextView        mTvConfirm;
    @Bind(R.id.layout_wheel)    LinearLayout    mLayoutWheel;
    private WheelView mWheelView;

    private final int handler_InitWheelView = 3;
    private int mWheelType;

    @Override protected void onCreate(Bundle savedInstanceState) {
        // TODO Auto-generated method stub
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_wheel_list);
        ButterKnife.bind(this);

		WindowManager m =getWindow().getWindowManager();
		Display d = m.getDefaultDisplay();  //为获取屏幕宽、高
		WindowManager.LayoutParams p = getWindow().getAttributes();  //获取对话框当前的参数值
		//p.height = (int) (d.getHeight() * 1.0);   //高度设置为屏幕的1.0
		p.width = (int) (d.getWidth() * 1.0);    //宽度设置为屏幕的0.8
		p.alpha = 0.8f;      //设置本身透明度
		p.dimAmount = 0.9f;      //设置黑暗度
		getWindow().setAttributes(p);
		getWindow().setGravity(Gravity.BOTTOM);

        initViews();
        initWheelViewThread();
    }

    private void initViews() {
        mWheelType = getIntent().getIntExtra(AppString.WheelTypeKey, 1);
    }

    Handler h = new Handler() {
        public void handleMessage(Message msg) {
            switch (msg.what) {
                case handler_InitWheelView:
                    addWheelView();
                    initListener();
                    initValue();
                    break;

                default:
                    break;
            }
        }
    };

    //等待oncreate完成， 给layout高度
    private void initWheelViewThread() {
        new Thread() {
            @Override public void run() {
                int height = 0;
                while (height == 0) {
                    if (mLayoutWheel != null) {
                        height = mLayoutWheel.getHeight();
                    }
                }
                h.sendEmptyMessage(handler_InitWheelView);
                super.run();
            }
        }.start();
    }

    private void addWheelView() {
        mWheelView = WheelUtils.getWheeilView(this, mWheelType, 0);
        mLayoutWheel.addView(mWheelView);
    }

    private void initListener() {
        mWheelView.addChangingListener(new OnWheelChangedListener() {

            @Override public void onChanged(WheelView wheel, int oldValue, int newValue) {
                // TODO Auto-generated method stub
            }
        });
    }

    private void initValue() {
        //Intent mIntent = getIntent();
        //int hourInt = mIntent.getIntExtra(AppString.WheelTypeValue, 0);

    }


    @OnClick({ R.id.tv_back, R.id.tv_confirm }) public void onClick(View view) {
        switch (view.getId()) {
            case R.id.tv_back:
                finish();
                break;
            case R.id.tv_confirm:
                int item = mWheelView.getCurrentItem();
                String value = mWheelView.getAdapter().getItem(item);
                Intent intent = getIntent().putExtra(AppString.WheelTypeValue , value);
                intent.putExtra(AppString.WheelTypeItem , item);
                setResult(RESULT_OK, intent);
                finish();
                break;
        }
    }
}
