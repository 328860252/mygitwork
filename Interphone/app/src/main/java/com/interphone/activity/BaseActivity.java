package com.interphone.activity;

import android.app.Activity;
import android.content.Context;
import android.content.IntentFilter;
import android.os.Bundle;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;
import com.example.administrator.interphone.R;
import com.interphone.AppApplication;
import com.interphone.AppConstants;
import com.interphone.bean.DeviceBean;
import com.interphone.connection.ConnectAction;
import com.interphone.connection.ConnectBroadcastReceiver;
import com.interphone.connection.agreement.CmdPackage;

public class BaseActivity extends Activity {

    protected float phone_density;//屏幕密度
    protected int phone_width, phone_height;//屏幕宽高


    TextView tv_title;//标题
    LinearLayout layout_back;//返回layout
    LinearLayout layout_menu;//右上角按钮layout
    TextView tv_back , tv_menu;//标题栏中 左边的图片 和 右边的图片

    private ConnectBroadcastReceiver mReceiver;
    private Toast mToast;
    private DeviceBean dbin;

    @Override protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        requestWindowFeature(Window.FEATURE_NO_TITLE);

        //Log.e("tag", " init  home oncraete() ");

        //屏幕宽度
        WindowManager wm = (WindowManager) this
                .getSystemService(Context.WINDOW_SERVICE);
        phone_width = wm.getDefaultDisplay().getWidth();// 屏幕宽度
        phone_height  = wm.getDefaultDisplay().getHeight();// 屏幕宽度
        phone_density =  getResources().getDisplayMetrics().density; //屏幕密度
    }

    protected void initBaseViews(Activity v) {
        //View v = LayoutInflater.from(this).inflate(R.layout.fragment_title, null);
        tv_title = (TextView) v.findViewById(R.id.tv_title);
        layout_back = (LinearLayout) v.findViewById(R.id.layout_title_left);
        layout_menu = (LinearLayout) v.findViewById(R.id.layout_title_right);
        tv_back = (TextView) v.findViewById(R.id.tv_title_left);
        tv_menu = (TextView) v.findViewById(R.id.tv_title_right);




        layout_back.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {
                // TODO Auto-generated method stub
                finish();
            }
        });
    }

    private void registerConnectBroadcast() {
        if(mReceiver==null) {
            mReceiver = new ConnectBroadcastReceiver(this);
        }
        IntentFilter filter = new IntentFilter(ConnectAction.ACTION_RECEIVER_DATA);
        registerReceiver(mReceiver, filter);
    }

    public void unRegisterConnectBroadcast() {
        if(mReceiver!=null) {
            unregisterReceiver(mReceiver);
        }
    }

    /**
     * 接收消息  
     * @param type
     * @param i
     */
    public void onReceiver(int type, int i) {
        if (type == CmdPackage.Cmd_type_error) {
            showToast("数据解析错误");
        } else {
            showToast("收到协议数据");
            if (AppConstants.isWriteACK) {
                if( dbin==null) {
                    dbin = ((AppApplication) getApplication()).getDbin();
                }
                dbin.writeNoEncrypt(CmdPackage.getCmdSuccess());
            }
        }
    }

    @Override protected void onStop() {
        unRegisterConnectBroadcast();
        super.onStop();
    }

    @Override protected void onStart() {
        registerConnectBroadcast();
        super.onStart();
    }


    void showToast(int resId) {
        if(mToast==null) {
            mToast = Toast.makeText(this, resId, Toast.LENGTH_LONG);
        }
        mToast.setText(resId);
        mToast.setDuration(Toast.LENGTH_LONG);
        mToast.show();
    }
    void showToast(String str) {
        if(mToast==null) {
            mToast = Toast.makeText(this, str, Toast.LENGTH_LONG);
        }
        mToast.setText(str);
        mToast.setDuration(Toast.LENGTH_LONG);
        mToast.show();
    }

}
