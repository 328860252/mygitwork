package com.interphone.connection.agreement;

import com.interphone.utils.LogUtils;
import com.interphone.utils.MyByteUtils;
import com.interphone.utils.MyHexUtils;

public class CmdEncrypt {

	private final static String TAG = CmdEncrypt.class.getSimpleName();

	public final static byte CMD_HEAD = 0x35;
	public final static byte CMD_RESULT = 0x36;
	public final static byte CMD_END = 0x10;
	public final static byte CMD_SUCCESS = 0x06;
	public final static byte CMD_FAIL = 0x05;

	private static int count;

	public static byte[] sendMessage(byte[] buff) {
		if(buff==null || buff.length<2) {
			return null;
		}
		byte[] sendBuff = new byte[buff.length+6];
		sendBuff[0] = CMD_HEAD;
		sendBuff[1] = buff[0];//读 或 写
		sendBuff[2] = buff[1];//指令类型
		//发送包计数
		count++;
		sendBuff[3] = (byte) (count/256);
		sendBuff[4] = (byte) (count%256);
		sendBuff[6] = (byte) (buff.length - 2);//数据长度

		System.arraycopy(buff, 2, sendBuff, 7, buff.length-2);

		sendBuff[sendBuff.length-1] = CMD_END;//数据尾
		int sum = 0;
		for(byte b : sendBuff) {
			sum +=b;
		}
		sendBuff[5] = (byte) (sum%256);//校验和

		LogUtils.d(TAG, "加密:"+MyHexUtils.buffer2String(buff));
		//System.out.println(MyHexUtils.buffer2String(sendBuff));
		return sendBuff;
	}


	public static byte[] processMessage(byte[] buff) {
		if(buff==null || buff.length<8) {
			return null;
		}
		System.out.println("校验："+ MyHexUtils.buffer2String(buff));
		byte[] sendBuff = new byte[MyByteUtils.byteToInt(buff[6])+2];
		sendBuff[0] = buff[1];//读 或 写
		sendBuff[1] = buff[2];//指令类型

		byte sum = 0;
		for(byte b : buff) {//将所有位的值相加， 这里校验位不需要加的
			sum +=b;
		}
		//再将上一步骤多加的 校验位减去
		sum -= buff[5];
		if(buff[5] == sum) {
			System.arraycopy(buff, 7, sendBuff, 2, MyByteUtils.byteToInt(buff[6]));
			return sendBuff;
		}
		return null;
	}



}
