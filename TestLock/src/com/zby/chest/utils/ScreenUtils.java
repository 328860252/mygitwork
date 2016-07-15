package com.zby.chest.utils;

import android.content.Context;
import android.util.DisplayMetrics;
import android.util.TypedValue;
import android.view.Display;
import android.view.WindowManager;

public class ScreenUtils {
	
	public static int dp2sp(Context context, float dpVal) {
		return (int) TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_DIP,  
				  dpVal, context.getResources().getDisplayMetrics());  

	}
	
	/**
	* �����ֻ��ķֱ��ʴ� dp �ĵ�λ ת��Ϊ px(����)
	*/
	public static int dip2px(Context context, float dpValue) {
	  final float scale = context.getResources().getDisplayMetrics().density;
	  return (int) (dpValue * scale + 0.5f);
	}

	/**
	* �����ֻ��ķֱ��ʴ� px(����) �ĵ�λ ת��Ϊ dp
	*/
	public static int px2dip(Context context, float pxValue) {
	  final float scale = context.getResources().getDisplayMetrics().density;
	  return (int) (pxValue / scale + 0.5f);
	} 
	
	
	public static int getPhoneWidth(Context context) {
		DisplayMetrics dm2 = context.getResources().getDisplayMetrics();
		return dm2.widthPixels;
	}



}
