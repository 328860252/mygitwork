package tsocket.zby.com.tsocket.connection.agreement;

import android.content.Context;
import tsocket.zby.com.tsocket.bean.DeviceBean;
import tsocket.zby.com.tsocket.connection.ICmdParseInterface;
import tsocket.zby.com.tsocket.utils.LogUtils;
import tsocket.zby.com.tsocket.utils.MyByteUtils;
import tsocket.zby.com.tsocket.utils.MyHexUtils;
import tsocket.zby.com.tsocket.utils.RxBus;

/**
 * Created by Administrator on 2016/5/13.
 * 解析数据
 */
public class CmdParseImpl implements ICmdParseInterface {

  private final static String TAG = CmdParseImpl.class.getSimpleName();

  private DeviceBean mDeviceBean;
  private Context mContext;

  public CmdParseImpl(DeviceBean deviceBean, Context context) {
    this.mContext = context;
    this.mDeviceBean = deviceBean;
  }

  @Override public void parseData(byte[] dataBuff) {
    if (dataBuff == null) return;
    String strBuffer = MyHexUtils.buffer2String(dataBuff);
    LogUtils.logD(TAG, "解析数据:" + strBuffer);
    if (dataBuff.length < 2) {
      return;
    }
    //mDeviceBean.writeNoEncrypt(CmdPackage.getCmdSuccess());
    String str = "";
    byte[] buffer;
    sendBroadcast(MyByteUtils.byteToInt(dataBuff[1]));
  }

  private void sendBroadcast(int type) {
    //Intent intent = new Intent(ConnectAction.ACTION_RECEIVER_DATA);
    //intent.putExtra(ConnectAction.BROADCAST_DATA_TYPE, type);
    //mContext.sendBroadcast(intent);
    RxBus.getDefault().post(type);
  }
}
