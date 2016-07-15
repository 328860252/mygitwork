package com.zby.chest.agreement;

import android.content.Context;
import android.content.Intent;
import android.util.Log;

import com.zby.chest.LockApplication;
import com.zby.chest.bluetooth.DataProtocolInterface;
import com.zby.chest.model.DeviceBean;
import com.zby.chest.utils.MyByte;
import com.zby.chest.utils.Myhex;

public class CmdDataParse implements DataProtocolInterface{

	private final String TAG = CmdDataParse.class.getSimpleName();
	
	/**
	 * ����״̬�仯
	 */
	public static final int type_lock_onoff = 101;
	
	/**
	 * ����ģʽ�仯
	 */
	public static final int type_lock_mode = 102;
	
	public static final int type_password_error = 103;
	
	
	public static final int type_low_alert = 104;//��ѹ����
	
	public static final int type_binds_remove_success = 105;//�Ӵ��󶨳ɹ�
	public static final int type_binds_remove_fail = 106;//�����ʧ��
	
	/**
	 * �޸Ŀ�������
	 */
	public static final int type_password_unlock_success = 107;
	
	public static final int type_password_verify_error = 108;
	public static final int type_password_verify_success = 109;
	

	public static final int type_binds_success = 110;
	

	public static final int type_binds_fail = 111;
	
	
	/**
	 * ��������޸ĳɹ�
	 */
	public final static int type_password_modify_success= 112;
	
	/**
	 * ���뿪�� ��ȷ
	 */
	public static final int type_password_success = 113;
	
	public static final int type_name= 115;
	
	/**
	 * ���״̬
	 */
	public static final int type_status = 116;
	
	private DeviceBean dbin;
	
	private Context mContext;
	
	public CmdDataParse(DeviceBean dbin, Context mContext) {
		this.dbin = dbin;
		this.mContext =mContext;
	}

	@Override
	public  void parseData( byte[] buffer) {
		// TODO Auto-generated method stub
		int type=0;
		String str = Myhex.buffer2String(buffer);
		Log.d(TAG, "����"+str);
		 if(buffer[0] == (byte) 0xCC && buffer[1] ==(byte) 0x88) {//״̬
			 dbin.setModeType(MyByte.byteToInt(buffer[2]));
			 dbin.setOnOff(buffer[3]==(0x11));
			 type = type_status;
		 }  else if(isEquals(str,"0xCC 0x01 0x01 0x0E 0x0E")) {//�Զ������ɹ�
			if(dbin.getModeType()== DeviceBean.LockMode_auto) {
				dbin.setOnOff(true);
				type= type_lock_onoff;
			}
		} else if( isEquals(str,"0xCC 0x01 0x01 0x1E 0x1E")) {//һ�������ɹ� 
			if(dbin.getModeType() == DeviceBean.LockMode_scroll) {
				dbin.setOnOff(true);
				type= type_lock_onoff;
			}
		} else if(isEquals(str,"0xCC 0x01 0x01 0x2E 0x2E")) {//���뿪���ɹ�
			if(dbin.getModeType() == DeviceBean.LockMode_password) {
				type= type_password_success;
				dbin.setOnOff(true);
			}
		} else if(isEquals(str,"0xCC 0x01 0x01 0x3E 0x3E")) {//�����������
			type = type_password_error;
		} else if(isEquals(str,"0xCC 0x02 0x01 0xC1 0x0E")) {//�볡����
			type= type_lock_onoff;
			dbin.setOnOff(false);
		} else if(isEquals(str,"0xCC 0x02 0x01 0xC8 0xCB")) {//�Զ�����
				type= type_lock_onoff;
				dbin.setOnOff(false);
		} else if(isEquals(str,"0xCC 0x03 0x01 0x4E 0x4C")) {//�������������޸ĳɹ�
			type =type_password_unlock_success;
		} else if(isEquals(str,"0xCC 0x03 0x01 0x5E 0x5C")) {//�������������޸�ʧ��
			type = type_password_error;
		} else if(isEquals(str,"0xCC 0x10 0x01 0x0A 0x1B")) {//�Զ�����ģʽ
			dbin.setModeType(DeviceBean.LockMode_auto);
			type=type_lock_mode;
		} else if(isEquals(str,"0xCC 0x10 0x01 0x0B 0x1A")) {//һ���������óɹ�
			dbin.setModeType(DeviceBean.LockMode_scroll);
			type=type_lock_mode;
		} else if(isEquals(str,"0xCC 0x10 0x01 0x0D 0x1C")) {//�������뿪��ģʽ���óɹ�
			dbin.setModeType(DeviceBean.LockMode_password);
			type=type_lock_mode;
		} else if(isEquals(str,"0xCC 0x40 0x01 0x81 0xC0")) {//��ѹ��ʾ
			type=type_low_alert;
			handlerSendBroadcastName(type);
			return;
		} else if(isEquals(str, "0xAA 02 01 01 02")){//�Ӵ��󶨳ɹ�
			type =type_binds_remove_success;
		} else if(isEquals(str, "0xAA 02 01 00 03")){//�Ӵ���ʧ��
			type = type_binds_remove_fail;
		} else if(isEquals(str, "0xDD  01 01 01 01")){//���������֤��ȷ
			type = type_password_verify_success;
		} else if(isEquals(str, "0xDD  01 01 00 00 ")){//������֤ʧ��
			type = type_password_verify_error;
			LockApplication mapp = (LockApplication) mContext.getApplicationContext();
			if(dbin!=null) {
			    mapp.removeDeviceBinNotStop(dbin.getMac(), true);
			}
		} else if(isEquals(str, "0xDD  02 01 01 02")){//��������޸ĳɹ�
			type = type_password_modify_success;
		}  else if(isEquals(str, "0xDD  02 01 00 03")){//�������
			type = type_password_error;
		}else if(buffer[0] == (byte) 0xAA && buffer[1] ==(byte) 0x01) {//�󶨽��
			int count = buffer[3];
			if(count>3) {
				dbin.setBindCount(3);
				type = type_binds_fail;
			} else if(count <1) {
				dbin.setBindCount(count);
				type = type_binds_fail;
			} else {
				dbin.setBindCount(count);
				type = type_binds_success;
			}
		} 
		else if(isEquals(str, "0xCC 50 00 00 01")) {
			type= type_name;
		}
		if(type>0) {
			handlerSendBroadcast(type);
		}
	}
	
	private  boolean isEquals(String str ,String str2) {
		str = str.replace(" ", "").toLowerCase();
		str2 = str2.replace("0x", "").replace(" ", "").toLowerCase();
		return str.equals(str2);
	}

	private void handlerSendBroadcast(int type) {
		Intent intent = new Intent (BroadcastString.BROADCAST_ACTION);
		intent.putExtra(BroadcastString.BROADCAST_DATA_TYPE, type);
//		intent.putExtra(ConnectBroadcastReceiver.BROADCAST_DATA_KEY, data);
		intent.putExtra(BroadcastString.BROADCAST_DEVICE_MAC, dbin.getMac());
		mContext.sendBroadcast(intent);
	}
	
	private void handlerSendBroadcastName(int type) {
		Intent intent = new Intent (BroadcastString.BROADCAST_ACTION);
		intent.putExtra(BroadcastString.BROADCAST_DATA_TYPE, type);
//		intent.putExtra(ConnectBroadcastReceiver.BROADCAST_DATA_KEY, data);
		String[] nameMac = new String[]{dbin.getName(), dbin.getMac()};
		intent.putExtra("nameMac", nameMac);
		mContext.sendBroadcast(intent);
	}

}
