package org.wp.activity;

import android.app.Application;

/**
 * �ڿ���Ӧ��ʱ�����Activity�򽻵�����Applicationʹ�õľ���Խ����ˡ�
 * Application����������Ӧ�ó����ȫ��״̬�ģ�����������Դ�ļ���
 * ��Ӧ�ó���������ʱ��Application�����ȴ�����Ȼ��Ż�������(Intent)������Ӧ��Activity����Service��
 * �ڱ��Ľ���Application��ע��δ�����쳣��������
 */
public class CrashApplication extends Application {
	
	
	@Override
	public void onCreate() {
		super.onCreate();
		
		// �쳣��������Ҫ����ʱע�͵������伴�ɣ�  
	    CrashHandler crashHandler = CrashHandler.getInstance();   
	    // ע��crashHandler   
	    crashHandler.init(getApplicationContext());   

	    /**
	     * �����и����⣬ Ҫ�ж�����״����  ��������ã���ֻ�������������ʱ�򣬷�����ǰ�Ĵ�����Ϣ����������
	     * ���Ҫ��ʱ��  ��������ͷ�����Ϣ��  �͵ý����sendPreviousReportsToServer������handlerException�е���
	     */
		//���ʹ��󱨸浽������ 
		crashHandler.sendPreviousReportsToServer();
	          
	}
}