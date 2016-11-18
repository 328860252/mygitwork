package com.interphone.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;
import com.example.administrator.interphone.R;
import java.util.List;

/**
 * <p>Description: �б�������</p>
 * @author zhujiang
 * @date 2014-9-20
 */
public class FileNameAdapter extends BaseAdapter {
	private Context mContext;
	private List<String> mArray;
	private LayoutInflater mLayoutInflater;// ���ڼ���xml

	public FileNameAdapter( List<String> array, Context mContext) {
		this.mContext = mContext;
		this.mArray = array;
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
		mHolder.ip_textView.setText(mArray.get(position));
		return convertView;
	}

	@Override
	public int getCount() {
		// TODO Auto-generated method stub
		return mArray.size();
	}

	@Override
	public Object getItem(int arg0) {
		// TODO Auto-generated method stub
		return mArray.get(arg0);
	}

	@Override
	public long getItemId(int position) {
		// TODO Auto-generated method stub
		return 0;
	}

}
