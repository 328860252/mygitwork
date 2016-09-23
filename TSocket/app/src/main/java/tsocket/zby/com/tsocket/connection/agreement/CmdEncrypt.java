package tsocket.zby.com.tsocket.connection.agreement;

import tsocket.zby.com.tsocket.utils.LogUtils;
import tsocket.zby.com.tsocket.utils.MyByteUtils;
import tsocket.zby.com.tsocket.utils.MyHexUtils;

public class CmdEncrypt {

	private final static String TAG = CmdEncrypt.class.getSimpleName();

	public static byte[] sendMessage(byte[] buff) {
		if(buff==null || buff.length<3) {
			return null;
		}
		byte[] sendBuff = new byte[buff.length+2];
		sendBuff[0] = (byte) (buff.length+1);//长度 ，
		System.arraycopy(buff, 0, sendBuff, 1, buff.length);

		byte checkByte =
				(byte) (sendBuff[0] ^ sendBuff[1] ^ sendBuff[sendBuff.length-3] ^ sendBuff[sendBuff.length-2]);

		sendBuff[sendBuff.length-1] = checkByte;
		LogUtils.d(TAG, "加密:"+MyHexUtils.buffer2String(buff));
		//System.out.println(MyHexUtils.buffer2String(sendBuff));
		return sendBuff;
	}


	public static byte[] processMessage(byte[] buff) {
		if(buff==null || buff.length<4) {
			return null;
		}
		System.out.println("校验："+ MyHexUtils.buffer2String(buff));
		int length = MyByteUtils.byteToInt(buff[0])+1;
		byte[] sendBuff = new byte[length-2];
		if(MyByteUtils.byteToInt(buff[0]) == (length-1)) { //长度判断
			System.arraycopy(buff, 1, sendBuff, 0, sendBuff.length);
			String str = MyHexUtils.buffer2String(sendBuff);
			return sendBuff;
		}
		return null;
	}
}
