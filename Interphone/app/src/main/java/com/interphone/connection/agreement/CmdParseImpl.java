package com.interphone.connection.agreement;

import android.content.Context;
import android.content.Intent;
import com.interphone.AppConstants;
import com.interphone.bean.ChannelData;
import com.interphone.bean.DeviceBean;
import com.interphone.bean.PowerTestData;
import com.interphone.bean.ProtertyData;
import com.interphone.connection.ConnectAction;
import com.interphone.connection.ICmdParseInterface;
import com.interphone.utils.BcdUtils;
import com.interphone.utils.LogUtils;
import com.interphone.utils.MyByteUtils;
import com.interphone.utils.MyHexUtils;
import java.io.UnsupportedEncodingException;

/**
 * Created by Administrator on 2016/5/13.
 * 解析数据
 */
public class CmdParseImpl implements ICmdParseInterface {

    private final static String TAG = CmdParseImpl.class.getSimpleName();

    private DeviceBean mDeviceBean;
    private Context mContext;

    public CmdParseImpl(DeviceBean deviceBean, Context context) {
        this.mContext =context;
        this.mDeviceBean = deviceBean;
    }

    @Override
    public void parseData(byte[] dataBuff) {
        if(dataBuff==null) return;
        String strBuffer = MyHexUtils.buffer2String(dataBuff);
        LogUtils.logD(TAG, "解析数据:"+strBuffer);
        if(dataBuff.length<2) {
            return;
        }
        //mDeviceBean.writeNoEncrypt(CmdPackage.getCmdSuccess());
        String str = "";
        byte[] buffer;
        try {
            switch(dataBuff[1]) {
                case CmdPackage.Cmd_type_info:
                    buffer = new byte[6];
                    //用户id
                    System.arraycopy(dataBuff,2, buffer, 0, 6);
                    str = new String(buffer);
                    mDeviceBean.setDeviceId(str);
                    //用户密码
                    System.arraycopy(dataBuff,8, buffer, 0, 6);
                    str = new String(buffer);
                    mDeviceBean.setPassword(str);
                    break;
                case CmdPackage.Cmd_type_channel:
                    ChannelData channelData;
                    buffer = new byte[4];
                    String bcdStr;
                    int singleLenght =27;//单个数据  字节长度
                    for (int i=0; i<  (dataBuff.length/singleLenght) ; i++) {
                        // -1 是因为 频道值 是 1-16 ， 下标是0-15
                        channelData = mDeviceBean.getChannelData(MyByteUtils.byteToInt(dataBuff[2 + i * singleLenght ]) - 1);
                        if (channelData!=null) {
                            channelData.setChannelType(MyByteUtils.byteToInt(dataBuff[ 3 + i * singleLenght]));
                            //接收频率 ，4个字节的bcd码
                            System.arraycopy(dataBuff, 4 + i * singleLenght, buffer, 0 , 4);
                            //将bcd码数字转成字符串 ，
                            bcdStr = BcdUtils.bcd2Str(buffer);
                            //保留小数点
                            channelData.setRateReceive( Integer.parseInt(bcdStr) / 100000.0d);
                            //发送频率，4个字节的bcd码
                            System.arraycopy(dataBuff, 8 + i * singleLenght, buffer, 0 , 4);
                            bcdStr = BcdUtils.bcd2Str(buffer);
                            channelData.setRateSend( Integer.parseInt(bcdStr) / 100000.0d);
                            //if(channelData.isNumberTone()) {
                            channelData.setNumberToneColor(MyByteUtils.byteToInt(dataBuff[12 + i * singleLenght]));
                            channelData.setNumberToneSlot(MyByteUtils.byteToInt(dataBuff[13 + i * singleLenght]));
                            channelData.setNumberToneLinkmen(MyByteUtils.byteToInt(dataBuff[14 + i * singleLenght]));
                            //} else {
                            channelData.setAnalogToneReceive(MyByteUtils.byteToInt(dataBuff[15 + i * singleLenght]));
                            channelData.setAnalogToneSend(MyByteUtils.byteToInt(dataBuff[16 + i * singleLenght]));
                            channelData.setAnalogToneBand(MyByteUtils.byteToInt(dataBuff[17 + i * singleLenght]));
                            //}
                            channelData.setPower(MyByteUtils.byteToInt(dataBuff[18 + i * singleLenght]));
                          //  LogUtils.d(TAG, " channelData ["+i+"] : " + channelData.toString());

                        }
                    }
                    break;
                case CmdPackage.Cmd_type_property:
                    ProtertyData protertyData = mDeviceBean.getProtertyData();
                    protertyData.setHFvalue(MyByteUtils.byteToInt(dataBuff[2]));
                    protertyData.setTotTime(MyByteUtils.byteToInt(dataBuff[3]));
                    protertyData.setVox(MyByteUtils.byteToInt(dataBuff[4]));
                    protertyData.setActivityChannelId(MyByteUtils.byteToInt(dataBuff[11]));

                    buffer = new byte[6];
                    System.arraycopy(dataBuff, 5, buffer, 0, 6);
                    protertyData.setPttid(new String(buffer, AppConstants.charSet));
                    LogUtils.d(TAG, " proterty : " + protertyData.toString());
                    break;
                case CmdPackage.Cmd_type_power:
                    //对于调频功率 ，似乎不用读取返回数据
                    int singleLength =  4; //单组数据长度
                    PowerTestData powerTestData ;
                    for (int i=0; i< dataBuff.length/ singleLength; i++ ) {
                        powerTestData = mDeviceBean.getPowerData(i);
                        powerTestData.setPowerId( MyByteUtils.byteToInt(dataBuff[i * singleLength + 2]));
                        powerTestData.setPowerLow( MyByteUtils.byteToInt(dataBuff[i * singleLength + 3]));
                        powerTestData.setPowerMiddle( MyByteUtils.byteToInt(dataBuff[i * singleLength + 4]));
                        powerTestData.setPowerHigh( MyByteUtils.byteToInt(dataBuff[i * singleLength + 5]));
                    }

                    break;
                case CmdPackage.Cmd_type_sms:
                    buffer = new byte[dataBuff.length-2-12];
                    System.arraycopy(dataBuff,2+12, buffer, 0, buffer.length);
                    //str = MyHexUtils.buffer2StringNoSpace(buffer);
                    try {
                        str = new String(buffer, AppConstants.charSet);
                        mDeviceBean.setSms(str.replace(" ",""));
                    } catch (UnsupportedEncodingException e) {
                        e.printStackTrace();
                    }
                    break;
            }
            sendBroadcast(MyByteUtils.byteToInt(dataBuff[1]));
        } catch (Exception e) {
            e.printStackTrace();
            sendBroadcast(CmdPackage.Cmd_type_error);
        }

    }

    private void sendBroadcast(int type) {
        Intent intent = new Intent(ConnectAction.ACTION_RECEIVER_DATA);
        intent.putExtra(ConnectAction.BROADCAST_DATA_TYPE, type);
        mContext.sendBroadcast(intent);
    }
}
