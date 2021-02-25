package tsocket.zby.com.tsocket.connection.agreement;

import android.content.Context;
import com.hwangjr.rxbus.RxBus;
import tsocket.zby.com.tsocket.AppApplication;
import tsocket.zby.com.tsocket.AppString;
import tsocket.zby.com.tsocket.bean.DeviceBean;
import tsocket.zby.com.tsocket.bean.TimerBean;
import tsocket.zby.com.tsocket.connection.ICmdParseInterface;
import tsocket.zby.com.tsocket.utils.LogUtils;
import tsocket.zby.com.tsocket.utils.MyByteUtils;
import tsocket.zby.com.tsocket.utils.MyHexUtils;

/**
 * Created by Administrator on 2016/5/13.
 * 解析数据
 */
public class CmdParseImpl implements ICmdParseInterface {

    private final static String TAG = CmdParseImpl.class.getSimpleName();
    public static final byte type_status = (byte) 0xB1;
    public static final byte type_timer = (byte) 0xB2;
    public static final byte type_receiver = (byte) 0xB3;
    public static final byte type_downCount = (byte) 0xB5;

    private DeviceBean mDeviceBean;
    private Context mContext;

    public CmdParseImpl(DeviceBean deviceBean, Context context) {
        this.mContext = context;
        this.mDeviceBean = deviceBean;
    }

    @Override
    public void parseData(byte[] dataBuff) {
        if (dataBuff == null) return;
        String strBuffer = MyHexUtils.buffer2String(dataBuff);
        LogUtils.logD(TAG, "解析数据:" + strBuffer);
        if (dataBuff.length < 2) {
            return;
        }
        switch (dataBuff[0]) {
            case type_status: //状态
                mDeviceBean.setOnOff(dataBuff[1] == 0x01);
                mDeviceBean.setRecycle(dataBuff[2] == 0x01);
                mDeviceBean.setTimerEnable(dataBuff[3] == 0x01);
                break;
            case type_timer://定时
                AppApplication.isReadTime = true;
                TimerBean tb = new TimerBean();
                tb.setId(MyByteUtils.byteToInt(dataBuff[1]));
                tb.setStartHour(MyByteUtils.byteToInt(dataBuff[2]));
                tb.setStartMinute(MyByteUtils.byteToInt(dataBuff[3]));
                tb.setStartSecond(MyByteUtils.byteToInt(dataBuff[4]));
                tb.setEndHour(MyByteUtils.byteToInt(dataBuff[5]));
                tb.setEndMinute(MyByteUtils.byteToInt(dataBuff[6]));
                tb.setEndSecond(MyByteUtils.byteToInt(dataBuff[7]));
                tb.setOpenHour(MyByteUtils.byteToInt(dataBuff[8]));
                tb.setOpenMinute(MyByteUtils.byteToInt(dataBuff[9]));
                tb.setOpenSecond(MyByteUtils.byteToInt(dataBuff[10]));
                tb.setCloseHour(MyByteUtils.byteToInt(dataBuff[11]));
                tb.setCloseMinute(MyByteUtils.byteToInt(dataBuff[12]));
                tb.setCloseSecond(MyByteUtils.byteToInt(dataBuff[13]));
                tb.setWeekValue(MyByteUtils.byteToInt(dataBuff[14]));

                tb.setStatus(MyByteUtils.byteToInt(dataBuff[15]));
                mDeviceBean.updateTimerBeanList(tb);
                break;
            case type_receiver://应答
                break;
            case type_downCount://倒计时信息
                //开关1
                //循环1
                //启用1
                //时
                //分
                //秒
                mDeviceBean.setOnOff(dataBuff[1] == 0x01);
                mDeviceBean.setRecycle(dataBuff[2] == 0x01);
                //mDeviceBean.setTimerEnable(dataBuff[3] == 0x01);
                int downCount = MyByteUtils.byteToInt(dataBuff[4]) * 3600
                        + MyByteUtils.byteToInt(dataBuff[5]) * 60
                        + MyByteUtils.byteToInt(dataBuff[6]);
                mDeviceBean.setDownCountSecond(downCount);
                break;
            default:
        }
        RxBus.get().post(AppString.RXBUS_PUSH_BYTE, dataBuff[0]);
    }

    //private void sendBroadcast(int type) {
    //  //Intent intent = new Intent(ConnectAction.ACTION_RECEIVER_DATA);
    //  //intent.putExtra(ConnectAction.BROADCAST_DATA_TYPE, type);
    //  //mContext.sendBroadcast(intent);
    //  RxBus.getDefault().post(type);
    //}
}
