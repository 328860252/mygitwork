package com.zby.chest.model;

/**
 * @author Administrator
 *	�����¼��Ļ���  �͵��
 */
public interface DeviceListener {
	void onDeviceScroll(DeviceBean dbin);
	void onDeviceItemClick(DeviceBean dbin);
	void onDeviceLongClick(DeviceBean deviceBean);
	void onDeviceLongLongClick(DeviceBean deviceBean);
}
