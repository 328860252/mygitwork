package com.zby.chest.utils;

import java.util.Locale;

import com.zby.chest.AppConstants;
import com.zby.chest.R;
import com.zby.chest.model.DeviceBean;

import android.app.ActivityManager;
import android.content.Context;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.content.pm.PackageManager.NameNotFoundException;
import android.content.res.Configuration;
import android.content.res.Resources;
import android.net.wifi.WifiInfo;
import android.net.wifi.WifiManager;
import android.text.TextUtils;
import android.util.DisplayMetrics;


public class Tools {
	
	public static boolean isStringCheck(String str) {
		if(str!=null && !str.equals("")) {
			return true;
		}
		return false;
	}
	
	
	/**
	 * �Ƚ������Ƿ�һ��
	 * @param str
	 * @param str2
	 * @return
	 */
	public static boolean isStringCheck(String str,String str2) {
		if(str!=null && str2!=null && str.equals(str2)) {
			return true;
		}
		return false;
	}
	
	
	/**
	 * ���Ī������
	 * @param mContext
	 * @param type
	 * @return
	 */
	public static String getModeName(Context mContext,int type) {
		String name="";
		switch(type) {
		case DeviceBean.LockMode_auto:
			name = mContext.getString(R.string.unlock_auto);
			break;
		case DeviceBean.LockMode_scroll:
			name = mContext.getString(R.string.unlock_scroll);
			break;
		case DeviceBean.LockMode_password:
			name = mContext.getString(R.string.unlock_password);
			break;
		}
		return name;
	}
	
	public static String getLanguageName(Context mContext,int type) {
		String name="";
		switch(type) {
		case AppConstants.language_en:
			name = mContext.getString(R.string.language_en);
			break;
		case AppConstants.language_zh:
			name = mContext.getString(R.string.language_zh);
			break;
		}
		return name;
	}
	
	/**
	 * �жϱ����Ƿ�Ϊ���ģ��ͷ������ģ����Ǿͷ���ӦΪ
	 * @param mContext
	 * @return
	 */
	public static int getLocalLanguage(Context mContext) {
		Locale local = mContext.getResources().getConfiguration().locale;
		String language = local.getLanguage();
		if(Locale.CHINA.toString().toLowerCase().contains(language.toLowerCase())) {
			return AppConstants.language_zh;
		} else {
			return AppConstants.language_en;
		}
	}
	
	public static boolean isLocalLanguageChina(Context mContext) {
        Locale local = mContext.getResources().getConfiguration().locale;
        String language = local.getLanguage();
        if(Locale.CHINA.toString().toLowerCase().contains(language.toLowerCase())) {
            return true;
        } else {
            return false;
        }
    }
	
	public static void switchLanguage(Context context,int langItem) {
		//�����޸�Ϊ���� ϵͳ������ ����
		Locale locale = getLanguageShort(langItem);
        Resources resources = context.getResources();// ���res��Դ����
        Configuration config = resources.getConfiguration();// ������ö���
        DisplayMetrics dm = resources.getDisplayMetrics();// �����Ļ��������Ҫ�Ƿֱ��ʣ����صȡ�
        config.locale = locale; // ��������
        resources.updateConfiguration(config, dm);
    }
	
	/**
	 * ������Ե�  ��ĸ��д
	 * @param langItem ���Ա��
	 * @return 0�������� �� 1����en �� 2���巵�� zh_tw
	 */
	private static Locale  getLanguageShort(int langItem){
		Locale locale = null;
		switch(langItem) {
		case AppConstants.language_zh:
			locale =  Locale.SIMPLIFIED_CHINESE;
			break;
		case AppConstants.language_en:
			locale = new Locale("en");
			break;
		default:
			locale = Locale.getDefault();
			break;
		}
		return locale;
	}
	
	
	 /* ��ȡ�ֻ�mac��ַ<br/>
	     * ���󷵻�12��0
	     */
	    public static String getMacAddress(Context context) {
	    	String mac = SetupData.getSetupData(context).read("macAddress");
	    	if (mac.equals("")) {
	    		// ��ȡmac��ַ��
		        String macAddress = "000000000000";
		        try {
		            WifiManager wifiMgr = (WifiManager) context
		                    .getSystemService(Context.WIFI_SERVICE);
		            if(!wifiMgr.isWifiEnabled()) {
		            	wifiMgr.setWifiEnabled(true);
		            }
		            WifiInfo info = (null == wifiMgr ? null : wifiMgr
		                    .getConnectionInfo());
		            if (null != info) {
		                if (!TextUtils.isEmpty(info.getMacAddress())) {
		                	macAddress = info.getMacAddress().replace(":", "");
		                	SetupData.getSetupData(context).save("macAddress", macAddress);
		                } else {
		                	return macAddress;
		                }
		            }
		        } catch (Exception e) {
		            // TODO Auto-generated catch block
		            e.printStackTrace();
		            return macAddress;
		        }
		        return macAddress;
	    	} 
	    	return mac;
	        
	    }


        public static String getVersionName(Context helpActivity) {
            // TODO Auto-generated method stub
            PackageManager pm = helpActivity.getPackageManager();
            String vname = "";
            try {
                PackageInfo pf = pm.getPackageInfo(helpActivity.getPackageName(), 0);
                vname = pf.versionName;
            } catch (NameNotFoundException e) {
                // TODO Auto-generated catch block
                e.printStackTrace();
            }
            return vname;
        }
        
       public static String getCurProcessName(Context context) {
        	  int pid = android.os.Process.myPid();
        	  ActivityManager mActivityManager = (ActivityManager) context
        	    .getSystemService(Context.ACTIVITY_SERVICE);
        	  for (ActivityManager.RunningAppProcessInfo appProcess : mActivityManager
        	    .getRunningAppProcesses()) {
        	   if (appProcess.pid == pid) {
        	    return appProcess.processName;
        	   }
        	  }
        	  return null;
        	 }

}
