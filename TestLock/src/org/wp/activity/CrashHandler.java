package org.wp.activity;

import java.io.File;
import java.io.FileOutputStream;
import java.io.FilenameFilter;
import java.io.PrintWriter;
import java.io.StringWriter;
import java.io.Writer;
import java.lang.Thread.UncaughtExceptionHandler;
import java.lang.reflect.Field;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Arrays;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;
import java.util.Properties;
import java.util.TreeSet;



import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.content.pm.PackageManager.NameNotFoundException;
import android.os.Build;
import android.os.Environment;
import android.os.Looper;
import android.text.format.Time;
import android.util.Log;
import android.view.Gravity;
import android.widget.Toast;

import com.zby.chest.utils.FileUtil;

/**
 * ����з��ͷ��������֣� �����쳣�ļ��Ǳ�����  data\data\��Ŀ�£��������ݸ�ʽ�У� �쳣��Ϣû�л��У� ����������
 * <p>Description: </p>
 * @author zhujiang
 * @date 2014-11-21
 */
public class CrashHandler implements UncaughtExceptionHandler {

	/** Debug Log tag*/ 
	public static final String TAG = "CrashHandler"; 
	/** �Ƿ�����־���,��Debug״̬�¿���, 
	* ��Release״̬�¹ر�����ʾ�������� 
	* */ 
	public static final boolean DEBUG = false; 
	/** ϵͳĬ�ϵ�UncaughtException������ */ 
	private Thread.UncaughtExceptionHandler mDefaultHandler; 
	/** CrashHandlerʵ�� */ 
	private static CrashHandler INSTANCE; 
	/** �����Context���� */ 
	private Context mContext; 
	/** ʹ��Properties�������豸����Ϣ�ʹ����ջ��Ϣ*/ 
	private Properties mDeviceCrashInfo = new Properties(); 
	private static final String VERSION_NAME = "versionName"; 
	private static final String VERSION_CODE = "versionCode"; 
	private static final String STACK_TRACE = "STACK_TRACE"; 
	/** ���󱨸��ļ�����չ�� */ 
	private static final String CRASH_REPORTER_EXTENSION = ".cr"; 
	
	//�����洢�豸��Ϣ���쳣��Ϣ
		private Map<String, String> infos = new HashMap<String, String>();

		//���ڸ�ʽ������,��Ϊ��־�ļ�����һ����
		private DateFormat formatter = new SimpleDateFormat("yyyy-MM-dd-HH-mm-ss");

	
	/** ��ֻ֤��һ��CrashHandlerʵ�� */ 
	private CrashHandler() {} 
	
	/** ��ȡCrashHandlerʵ�� ,����ģʽ*/ 
	public static CrashHandler getInstance() { 
		if (INSTANCE == null) { 
			INSTANCE = new CrashHandler(); 
		} 
		return INSTANCE; 
	} 
	
	/** 
	* ��ʼ��,ע��Context����, 
	* ��ȡϵͳĬ�ϵ�UncaughtException������, 
	* ���ø�CrashHandlerΪ�����Ĭ�ϴ����� 
	* @param ctx 
	*/ 
	public void init(Context ctx) { 
		mContext = ctx; 
		mDefaultHandler = Thread.getDefaultUncaughtExceptionHandler(); 
		Thread.setDefaultUncaughtExceptionHandler(this); 
	} 
	
	/** 
	* ��UncaughtException����ʱ��ת��ú��������� 
	*/ 
	@Override 
	public void uncaughtException(Thread thread, Throwable ex) { 
		if (!handleException(ex) && mDefaultHandler != null) { 
			//����û�û�д�������ϵͳĬ�ϵ��쳣������������ 
			mDefaultHandler.uncaughtException(thread, ex); 
		} else { 
			//Sleepһ���������� 
			try { 
				Thread.sleep(3500); 
			} catch (InterruptedException e) { 
				Log.e(TAG, "Error : ", e); 
			} 
			android.os.Process.killProcess(android.os.Process.myPid()); 
			System.exit(10); 
		} 
		
		//�������
//		Intent intent = new Intent(mContext, MenuDeviceListActivity.class);  
//        intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP |  
//        Intent.FLAG_ACTIVITY_NEW_TASK);  
//        mContext.startActivity(intent);  
		
	} 
	
	/** 
	* �Զ��������,�ռ�������Ϣ 
	* ���ʹ��󱨸�Ȳ������ڴ����. 
	* �����߿��Ը����Լ���������Զ����쳣�����߼� 
	* @param ex 
	* @return true:��������˸��쳣��Ϣ;���򷵻�false 
	*/ 
	private boolean handleException(Throwable ex) { 
		if (ex == null) { 
			Log.w(TAG, "handleException --- ex==null"); 
			return true; 
		} 
		final String msg = ex.getLocalizedMessage(); 
		if(msg == null) {
			return false;
		}
		//ʹ��Toast����ʾ�쳣��Ϣ 
		new Thread() { 
			@Override 
			public void run() { 
				Looper.prepare(); 
				Toast toast = Toast.makeText(mContext, "������������˳�:\r\n" ,
						Toast.LENGTH_LONG);
				toast.setGravity(Gravity.CENTER, 0, 0);
				toast.show();
//				MsgPrompt.showMsg(mContext, "���������", msg+"\n��ȷ���˳�");
				Looper.loop(); 
			} 
		}.start(); 
		//�ռ��豸��Ϣ 
		//collectCrashDeviceInfo(mContext); 
		collectDeviceInfo(mContext);
		//������󱨸��ļ� 
		saveCrashInfo2File(ex); 
		//sendPreviousReportsToServer()
		return true; 
	} 
	
	/** 
	* �ڳ�������ʱ��, ���Ե��øú�����������ǰû�з��͵ı��� 
	*/ 
	public void sendPreviousReportsToServer() { 
		sendCrashReportsToServer(mContext); 
	} 
	/** 
	* �Ѵ��󱨸淢�͸�������,�����²����ĺ���ǰû���͵�. 
	* @param ctx 
	*/ 
	private void sendCrashReportsToServer(Context ctx) { 
		String[] crFiles = getCrashReportFiles(ctx); 
		if (crFiles != null && crFiles.length > 0) { 
		TreeSet<String> sortedFiles = new TreeSet<String>(); 
		sortedFiles.addAll(Arrays.asList(crFiles)); 
		for (String fileName : sortedFiles) { 
		File cr = new File(ctx.getFilesDir(), fileName); 
		postReport(cr); 
		cr.delete();// ɾ���ѷ��͵ı��� 
		} 
		} 
	} 
	private void postReport(File file) { 
		// TODO ���ʹ��󱨸浽������ 
	} 
	
	/** 
	* ��ȡ���󱨸��ļ��� 
	* @param ctx 
	* @return 
	*/ 
	private String[] getCrashReportFiles(Context ctx) { 
		File filesDir = ctx.getFilesDir(); 
		FilenameFilter filter = new FilenameFilter() { 
			public boolean accept(File dir, String name) { 
				return name.endsWith(CRASH_REPORTER_EXTENSION); 
			} 
		}; 
		return filesDir.list(filter); 
	} 
	
	
	/**
     * ���������Ϣ���ļ���
     *
     * @param ex
     * @return  �����ļ�����,���ڽ��ļ����͵�������
     */
    private void saveCrashInfo2File(Throwable ex) {

        StringBuffer sb = new StringBuffer();
        for (Map.Entry<String, String> entry : infos.entrySet()) {
            String key = entry.getKey();
            String value = entry.getValue();
            sb.append(key + "=" + value + "\n\n");
        }

        Writer writer = new StringWriter();
        PrintWriter printWriter = new PrintWriter(writer);
        ex.printStackTrace(printWriter);
        Throwable cause = ex.getCause();
        while (cause != null) {
            cause.printStackTrace(printWriter);
            cause = cause.getCause();
        }
        printWriter.close();
        String result = writer.toString();
        sb.append("exception_msg="+result+"\n\n");
        try {
            long timestamp = System.currentTimeMillis();
            String time = formatter.format(new Date());
            String fileName = "crash-" + time + "-" + timestamp + ".txt";
            if (Environment.getExternalStorageState().equals(Environment.MEDIA_MOUNTED)) {
                File dir = FileUtil.getCrashLogDirectory();
                if(dir != null)
                {
                    FileOutputStream fos = new FileOutputStream(dir.getAbsolutePath()+"/" + fileName);
                    fos.write(sb.toString().getBytes());
                    fos.close();
                }
            }
        } catch (Exception e) {
            Log.e(TAG, "an error occured while writing file...", e);
        }
    }
//	/** 
//	* ���������Ϣ���ļ��� 
//	* @param ex 
//	* @return 
//	*/ 
//	private String saveCrashInfoToFile(Throwable ex) { 
////		Writer info = new StringWriter(); 
////		PrintWriter printWriter = new PrintWriter(info); 
////		ex.printStackTrace(printWriter); 
////		Throwable cause = ex.getCause(); 
////		while (cause != null) { 
////			cause.printStackTrace(printWriter); 
////			cause = cause.getCause(); 
////		} 
////		String result = info.toString(); 
////		printWriter.close(); 
////		mDeviceCrashInfo.put("EXEPTION", ex.getLocalizedMessage());
////		mDeviceCrashInfo.put(STACK_TRACE, result); 
////		try { 
////			//long timestamp = System.currentTimeMillis(); 
////			Time t = new Time("GMT+8"); 
////			t.setToNow(); // ȡ��ϵͳʱ��
////			int date = t.year * 10000 + t.month * 100 + t.monthDay;
////			int time = t.hour * 10000 + t.minute * 100 + t.second;
////			String fileName = "crash-" + date + "-" + time + CRASH_REPORTER_EXTENSION; 
////			Log.v(TAG,"save File:"+fileName);
////			FileOutputStream trace = mContext.openFileOutput(fileName, 
////					Context.MODE_PRIVATE); 
////			mDeviceCrashInfo.store(trace, ""); 
////			trace.flush(); 
////			trace.close(); 
////			return fileName; 
////		} catch (Exception e) { 
////			Log.e(TAG, "an error occured while writing report file...", e); 
////		} 
////		return null; 
//	    Log.e("tag","wwww222");
//		StringBuffer sb = new StringBuffer();
//		for (Map.Entry<String, String> entry : infos.entrySet()) {
//			String key = entry.getKey();
//			String value = entry.getValue();
//			sb.append(key + "=" + value + "\r\n");
//		}
//		
//		Writer writer = new StringWriter();
//		PrintWriter printWriter = new PrintWriter(writer);
//		ex.printStackTrace(printWriter);
//		Throwable cause = ex.getCause();
//		while (cause != null) {
//			cause.printStackTrace(printWriter);
//			cause = cause.getCause();
//		}
//		printWriter.close();
//		String result = writer.toString();
//		sb.append(result);
//		try {
//		    Log.e("tag","wwww1");
//			long timestamp = System.currentTimeMillis();
//			String time = formatter.format(new Date());
//			String fileName =  Environment.getExternalStorageDirectory().getCanonicalPath()+"/Lock"+"/crash-" + time + "-" + timestamp + ".txt";
//			Log.i(TAG,"save filePath:"+fileName);
//			FileOutputStream trace = mContext.openFileOutput(fileName, 
//					Context.MODE_PRIVATE); 
//			//mDeviceCrashInfo.store(trace, ""); 
//			trace.write(sb.toString().getBytes());
//			trace.flush(); 
//			trace.close(); 
//			return fileName; 
//		} catch (Exception e) {
//			Log.e(TAG, "an error occured while writing file...", e);
//		}
//		return null;
//	} 
	
	/**
	 * �ռ��豸������Ϣ
	 * @param ctx
	 */
	public void collectDeviceInfo(Context ctx) {
		try {
			PackageManager pm = ctx.getPackageManager();
			PackageInfo pi = pm.getPackageInfo(ctx.getPackageName(), PackageManager.GET_ACTIVITIES);
			if (pi != null) {
				String versionName = pi.versionName == null ? "null" : pi.versionName;
				String versionCode = pi.versionCode + "";
				infos.put("versionName", versionName);
				infos.put("versionCode", versionCode);
			}
		} catch (NameNotFoundException e) {
			Log.e(TAG, "an error occured when collect package info", e);
		}
		Field[] fields = Build.class.getDeclaredFields();
		for (Field field : fields) {
			try {
				field.setAccessible(true);
				infos.put(field.getName(), field.get(null).toString());
				Log.d(TAG, field.getName() + " : " + field.get(null));
			} catch (Exception e) {
				Log.e(TAG, "an error occured when collect crash info", e);
			}
		}
	}

//	/** 
//	* �ռ�����������豸��Ϣ 
//	* 
//	* @param ctx 
//	*/ 
//	public void collectCrashDeviceInfo(Context ctx) { 
//		try { 
//			PackageManager pm = ctx.getPackageManager(); 
//			PackageInfo pi = pm.getPackageInfo(ctx.getPackageName(), 
//					PackageManager.GET_ACTIVITIES); 
//			if (pi != null) { 
//				mDeviceCrashInfo.put(VERSION_NAME, 
//						pi.versionName == null ? "not set" : pi.versionName); 
//				mDeviceCrashInfo.put(VERSION_CODE, ""+pi.versionCode); 
//			} 
//		} catch (NameNotFoundException e) { 
//			Log.e(TAG, "Error while collect package info", e); 
//		} 
//		//ʹ�÷������ռ��豸��Ϣ.��Build���а��������豸��Ϣ, 
//		//����: ϵͳ�汾��,�豸������ �Ȱ������Գ����������Ϣ 
//		//������Ϣ��ο�����Ľ�ͼ 
//		Field[] fields = Build.class.getDeclaredFields(); 
//		for (Field field : fields) { 
//			try { 
//				field.setAccessible(true); 
//				mDeviceCrashInfo.put(field.getName(), ""+field.get(null)); 
//				if (DEBUG) { 
//					Log.d(TAG, field.getName() + " : " + field.get(null)); 
//				} 
//			} catch (Exception e) { 
//				Log.e(TAG, "Error while collect crash info", e); 
//			} 
//		} 
//	} 

}

