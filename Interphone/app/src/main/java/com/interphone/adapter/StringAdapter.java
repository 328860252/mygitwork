package com.interphone.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;
import com.example.administrator.interphone.R;

/**
 * <p>Description: �б�������</p>
 * @author zhujiang
 * @date 2014-9-20
 */
public class StringAdapter extends BaseAdapter {
	private Context mContext;
	private String[] mArray;
	private int index=-1;
	private LayoutInflater mLayoutInflater;// ���ڼ���xml

	public StringAdapter( String[] array, Context mContext) {
		this.mContext = mContext;
		this.mArray = array;
		//this.cmdList = cmdList;
		//this.mConnectInterface = mConnectInterface;
		mLayoutInflater = LayoutInflater.from(mContext);
	}
	
	public StringAdapter( String[] array, Context mContext, int index) {
		this.mContext = mContext;
		this.mArray = array;
		this.index = index;
		//this.cmdList = cmdList;
		//this.mConnectInterface = mConnectInterface;
		mLayoutInflater = LayoutInflater.from(mContext);
	}

	class Holder {
		public TextView ip_textView;
		//public Button sendTest;

	}

	private Holder mHolder = null;

	@Override
	public View getView(final int position, View convertView, ViewGroup parent) {
		// ��ʼ���ؼ�
		if (convertView == null) {

			convertView = mLayoutInflater
					.inflate(R.layout.dialog_list_item, null);
			mHolder = new Holder();
			mHolder.ip_textView = (TextView) convertView
					.findViewById(R.id.textView_ssid);
			//mHolder.sendTest = (Button) convertView.findViewById(R.id.btn_send);
			convertView.setTag(mHolder);
		} else {
			mHolder = (Holder) convertView.getTag();
		}
		mHolder.ip_textView.setText(mArray[position]);
		if(index == position) {
			mHolder.ip_textView.setTextColor(mContext.getResources().getColor(R.color.bg_title));
		} else {
			mHolder.ip_textView.setTextColor(mContext.getResources().getColor(R.color.text_dark));
		}
		return convertView;
	}

	@Override
	public int getCount() {
		// TODO Auto-generated method stub
		return mArray.length;
	}

	@Override
	public Object getItem(int arg0) {
		// TODO Auto-generated method stub
		return mArray[arg0];
	}

	@Override
	public long getItemId(int position) {
		// TODO Auto-generated method stub
		return 0;
	}

	/**
	 * 更新选中项
	 * @param lang
	 */
	public void updateIndex(int lang) {
		// TODO Auto-generated method stub
		this.index = lang;
		notifyDataSetChanged();
	}

	public int getPosition(String value) {
		if(value==null) return 0;
		String str ;
		for(int i=0 ; i< mArray.length; i++) {
			str = mArray[i];
			if(str.equalsIgnoreCase(value)) {
				return i;
			}
		}
		return 0;
	}

}
