package com.interphone.connection;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.os.Handler;
import android.util.Log;
import com.interphone.activity.BaseActivity;

public	class ConnectBroadcastReceiver extends BroadcastReceiver {

	private static String TAG = "ConnectBroadcastReceiver";


	private Handler mHandler;
	private BaseActivity mBaseActivity;


//	public ConnectBroadcastReceiver(Handler handler) {
//		this.mHandler = handler;
////			this.mActivity = activity;
//	}

	public ConnectBroadcastReceiver(BaseActivity baseActivity) {
		this.mBaseActivity = baseActivity;
		//			this.mActivity = activity;
	}

	@Override
	public void onReceive(Context context, Intent intent) {
		int type=  intent.getIntExtra(ConnectAction.BROADCAST_DATA_TYPE, -1);
		int i = intent.getIntExtra(ConnectAction.BROADCAST_DATA_value, -1);
		Log.e(TAG,  " initbroad收到 type ="+type + "  " + i);
		if(mBaseActivity!= null) {
			mBaseActivity.onReceiver(type, i);
		}
		//showToast()
//		if(mHandler!=null) {
//			Message msg = mHandler.obtainMessage();
//			msg.what = ConnectAction.Broad_Cmd;
//			msg.arg1 = type;
//			msg.arg2 = i;
//			String mac = intent.getStringExtra(ConnectAction.BROADCAST_DEVICE_MAC);
//			if(intent.hasExtra("nameMac")) {
//				msg.obj = intent.getStringArrayExtra("nameMac");
//			} else {
//				if(mac!=null) {
//					msg.obj = mac;
//				}
//			}
//			mHandler.sendMessage(msg);
//		} else {
//			Log.e(TAG, hashCode()+"handler ==null , " + type + " " + i);
////				mActivity.onRegister();
//		}
	}

	public void setHandler(Handler mHandler) {
		this.mHandler = mHandler;
	}
	}