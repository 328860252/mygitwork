package com.zby.chest.activity;

import com.zby.chest.R;

import android.app.Activity;
import android.app.TabActivity;
import android.content.Intent;
import android.content.res.Resources;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnCreateContextMenuListener;
import android.widget.ImageView;
import android.widget.TabHost;
import android.widget.TabHost.OnTabChangeListener;
import android.widget.TextView;

/**
 * @author Administrator
 * ��������activity ��ת�޷��� onstart����
 */
public class MainTabActivity extends TabActivity {

	private TabHost tabHost;
	

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_main_host);
		initTab();
	}

	private void initTab() {
		// �õ���tabhost����,��TabActivity�� ����ͨ�������������������
		TabHost tabHost = getTabHost();
		Resources res = getResources();
		tabHost.setup();
	          


		// ����һ��Intent���󣬸ö���ָ����һ������Activity
		Intent sessionIntent = new Intent();
		sessionIntent.setClass(this, HomeActivity.class);
		// ����һ��TabSpec���󣬴�����һ��ҳ
		TabHost.TabSpec refuelSpc = tabHost.newTabSpec("tab1");
		// ���ø�ҳ��Indiacator
		refuelSpc.setIndicator(getTabView(getString(R.string.home),
				res.getDrawable(R.drawable.btn_home)));
		refuelSpc.setContent(sessionIntent);
		// �����úõ�TabSpec������ӵ�TabHost��
		tabHost.addTab(refuelSpc);

		Intent buddylIntent = new Intent(this, ScanActivity.class);
		tabHost.addTab(tabHost
				.newTabSpec("tab2")
				.setIndicator(
						getTabView(getString(R.string.add),
								res.getDrawable(R.drawable.btn_scan)))
				.setContent(buddylIntent));

		Intent taskIntent = new Intent(this, SettingActivity.class);
		tabHost.addTab(tabHost
				.newTabSpec("tab3")
				.setIndicator(
						getTabView(getString(R.string.setting),
								res.getDrawable(R.drawable.btn_setting)))
				.setContent(taskIntent));

		Intent eventIntent = new Intent(this, HelpActivity.class);
		tabHost.addTab(tabHost
				.newTabSpec("tab4")
				.setIndicator(
						getTabView(getString(R.string.help),
								res.getDrawable(R.drawable.btn_help)))
				.setContent(eventIntent));
//		
//		
		 tabHost.setOnTabChangedListener(new OnTabChangeListener(){    
			            @Override  
			             public void onTabChanged(String tabId) {  
			                 if (tabId.equals("tab1")) {   //��һ����ǩ  
			                 }else if (tabId.equals("tab2")) {   //�ڶ�����ǩ  
			                 }else if (tabId.equals("tab3")) {   //��������ǩ  
			                 } else if(tabId.equals("tab4")) {
			                 }
			             }              
			         });   


	}

//	private View getTabView(String text, Drawable drawable) {
//		View view1 = LayoutInflater.from(this)
//				.inflate(R.layout.layout_tab, null);
//		TextView tv1 = (TextView) view1.findViewById(R.id.textView_tab);
//		tv1.setText(text);
//		tv1.setCompoundDrawables(null, drawable, null, null);
//		return view1;
//	}
	
	private View getTabView(String text, Drawable drawable){
		View view1 =  LayoutInflater.from(this).inflate(R.layout.tab_style, null);
		TextView tv1 = (TextView) view1.findViewById(R.id.tab_name);
		tv1.setText(text);
		ImageView iv1 = (ImageView) view1.findViewById(R.id.tab_image);
		iv1.setBackgroundDrawable(drawable);
		return view1;
	}

}
