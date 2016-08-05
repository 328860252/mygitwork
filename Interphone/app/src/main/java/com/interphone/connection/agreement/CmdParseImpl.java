package com.interphone.connection.agreement;

import android.content.Context;
import android.content.Intent;
import com.interphone.AppConstants;
import com.interphone.bean.ChannelData;
import com.interphone.bean.DeviceBean;
import com.interphone.bean.PowerTestData;
import com.interphone.bean.ProtertyData;
import com.interphone.bean.SmsEntity;
import com.interphone.connection.ConnectAction;
import com.interphone.connection.ICmdParseInterface;
import com.interphone.database.SmsDao;
import com.interphone.utils.BcdUtils;
import com.interphone.utils.LogUtils;
import com.interphone.utils.MyByteUtils;
import com.interphone.utils.MyHexUtils;
import java.io.UnsupportedEncodingException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;

/**
 * Created by Administrator on 2016/5/13.
 * 解析数据
 */
public class CmdParseImpl implements ICmdParseInterface {

    private final static double DEFAULT_RATE_VHF= 134.025;//默认VHF频段
    private final static double DEFAULT_RATE_UHF = 460.025;//默认UHF频段

    private final static String TAG = CmdParseImpl.class.getSimpleName();
    private SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");

    private DeviceBean mDeviceBean;
    private Context mContext;
    private SmsDao mSmsDao;

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
                    mDeviceBean.setDeviceMode(str);
                    mDeviceBean.setHFvalue(MyByteUtils.byteToInt(dataBuff[6]));
                    //用户密码
                    System.arraycopy(dataBuff,8, buffer, 0, 6);
                    str = new String(buffer);
                    mDeviceBean.setPassword(str);
                    //序列号
                    buffer = new byte[4];
                    System.arraycopy(dataBuff,12, buffer, 0, 4);
                    str = new String(buffer);
                    mDeviceBean.setSerialNumber(str);
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
                            if (bcdStr.equals("FFFFFFFF")) {
                                if (mDeviceBean.isVHF()) {
                                    channelData.setRateReceive( DEFAULT_RATE_VHF + i);
                                } else {
                                    channelData.setRateReceive( DEFAULT_RATE_UHF + i);
                                }
                            } else {
                                //保留小数点
                                channelData.setRateReceive( Integer.parseInt(bcdStr) / 100000.0d);
                            }
                            //发送频率，4个字节的bcd码
                            System.arraycopy(dataBuff, 8 + i * singleLenght, buffer, 0 , 4);
                            bcdStr = BcdUtils.bcd2Str(buffer);
                            if (bcdStr.equals("FFFFFFFF")) {//全是ff 则显示默认vhf 或 默认ufh值
                                //根据信道id， 依次累加+1
                                if (mDeviceBean.isVHF()) {
                                    channelData.setRateSend( DEFAULT_RATE_VHF + i);
                                } else {
                                    channelData.setRateSend( DEFAULT_RATE_UHF + i);
                                }
                            } else {
                                channelData.setRateSend( Integer.parseInt(bcdStr) / 100000.0d);
                            }
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
                    //protertyData.setHFvalue(MyByteUtils.byteToInt(dataBuff[2]));
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
                    buffer = new byte[dataBuff.length-2-22];
                    System.arraycopy(dataBuff,2+22, buffer, 0, buffer.length);
                    //str = MyHexUtils.buffer2StringNoSpace(buffer);
                    try {
                        str = new String(buffer, AppConstants.charSet);
                        mDeviceBean.setSms(str.replace(" ",""));
                    } catch (UnsupportedEncodingException e) {
                        e.printStackTrace();
                    }
                    int total = MyByteUtils.byteToInt(dataBuff[2]);
                    int index = MyByteUtils.byteToInt(dataBuff[3]);
                    int type =  MyByteUtils.byteToInt(dataBuff[4]);
                    if (type == SmsEntity.TYPE_TEXT) { //只显示文本，定位短信不处理
                        SmsEntity smsEntity ;
                        if (mSmsDao==null) {
                            mSmsDao = new SmsDao(mContext);
                        }
                        smsEntity = new SmsEntity();
                        smsEntity.setContent(str);
                        smsEntity.setReceiverId(mDeviceBean.getUserId());
                        smsEntity.setSend(false);
                        smsEntity.setType(type);
                        buffer = new byte[7];
                        System.arraycopy(dataBuff,2+15, buffer, 0, buffer.length);
                        smsEntity.setDataTime(getTime(buffer));
                        mSmsDao.insert(smsEntity);
                    }
                    // 总数是0-100， 但是index是0-99
                    if  (total-index<2) {//有多条短信数据， 等待收到最后一条，才发送广播说收到数据完毕
                        break;
                    } else {
                       return;
                    }
                case CmdPackage.Cmd_type_status:
                    int status = MyByteUtils.byteToInt(dataBuff[2]);
                    mDeviceBean.setStatus(status);
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

    private String getTime(byte[] bytes) {
        int year = MyByteUtils.byteToInt(bytes[0]) * 256 + MyByteUtils.byteToInt(bytes[1]);
        //月份从0开始
        int month = MyByteUtils.byteToInt(bytes[2])-1;
        int day = MyByteUtils.byteToInt(bytes[3]);
        int hour = MyByteUtils.byteToInt(bytes[4]);
        int minute = MyByteUtils.byteToInt(bytes[5]);
        int second = MyByteUtils.byteToInt(bytes[6]);
        Calendar calendar = Calendar.getInstance();
        calendar.set(year, month, day, hour, minute, second);
        return sdf.format(calendar.getTime());
    }
}
