package com.interphone.connection.agreement;

import com.interphone.AppConstants;
import com.interphone.bean.ChannelData;
import com.interphone.bean.PowerTestData;
import com.interphone.bean.ProtertyData;
import com.interphone.bean.SmsEntity;
import com.interphone.utils.BcdUtils;
import com.interphone.utils.StringUtils;
import java.util.List;

/**
 * 协议
 */
public class CmdPackage {
  private final static byte get = 0x01;//发送
  private final static byte set = 0x02;//接收

  /** 本机信息 */
  public final static byte Cmd_type_info = 0x02;
  /**
   * 属性合并成本机参数
   */
  public final static byte Cmd_type_property = 0x02;
  /**
   * 信道
   */
  public final static byte Cmd_type_channel = 0x03;
  /**
   * 功率
   */
  public final static byte Cmd_type_power = 0x04;
  /**
   * 短信
   */
  public final static byte Cmd_type_sms = 0x05;

  /**
   * 状态
   */
  public final static byte Cmd_type_status = 0x06;


  /**
   * 按键
   */
  private static byte Cmd_type_press = 0x07;
  //数据错误
  public final static byte Cmd_type_error = 0x7f;

  public static final int CMD_TYPE_ACK = 0x7E;

  public static final int CMD_TYPE_ACK_CHANNEL_END = 0x7D;

  /**
   * 成功返回
   */
  public static byte[] getCmdSuccess() {
    byte[] buff = new byte[3];
    buff[0] = CmdEncrypt.CMD_RESULT;
    buff[1] = CmdEncrypt.CMD_SUCCESS;
    buff[2] = CmdEncrypt.CMD_END;
    return buff;
  }

  /**
   * 失败返回
   */
  public static byte[] getCmdFail() {
    byte[] buff = new byte[3];
    buff[0] = CmdEncrypt.CMD_RESULT;
    buff[1] = CmdEncrypt.CMD_FAIL;
    buff[2] = CmdEncrypt.CMD_END;
    return buff;
  }

  public static byte[] getInfo() {
    byte[] buff = new byte[2];
    buff[0] = get;
    buff[1] = Cmd_type_info;
    return buff;
  }

  public static byte[] getProterty() {
    byte[] buff = new byte[2];
    buff[0] = get;
    buff[1] = Cmd_type_property;
    return buff;
  }

  public static byte[] getChannel() {
    byte[] buff = new byte[2];
    buff[0] = get;
    buff[1] = Cmd_type_channel;
    return buff;
  }

  public static byte[] getPower() {
    byte[] buff = new byte[2];
    buff[0] = get;
    buff[1] = Cmd_type_power;
    return buff;
  }

  public static byte[] getSms() {
    byte[] buff = new byte[2];
    buff[0] = get;
    buff[1] = Cmd_type_sms;
    return buff;
  }

  public static byte[] setChannel(ChannelData channelData) {
    byte[] buff = new byte[29];
    buff[0] = set;
    buff[1] = Cmd_type_channel;
    byte[] buffer;
    buff[2] = (byte) channelData.getChannelId();
    buff[3] = (byte) channelData.getChannelType();

    buffer = BcdUtils.rate2BCD(channelData.getRateReceive());
    System.arraycopy(buffer, 0, buff, 4, buffer.length);
    buffer = BcdUtils.rate2BCD(channelData.getRateSend());
    System.arraycopy(buffer, 0, buff, 8, buffer.length);

    //if(channelData.isNumberTone()) {
    buff[12] = (byte) channelData.getNumberToneLinkmen();
    buff[13] = (byte) channelData.getNumberToneSlot();
    buff[14] = (byte) channelData.getNumberToneColor();
    //} else {
    buff[15] = (byte) channelData.getAnalogToneReceive();
    buff[16] = (byte) channelData.getAnalogToneSend();
    buff[17] = (byte) channelData.getAnalogToneBand();

    buff[18] = (byte) channelData.getPower();
    return buff;
  }

  public static byte[] setChannel(List<ChannelData> list) {
    final int dataLength = 27;
    byte[] buff = new byte[2 + dataLength * 16];
    ChannelData channelData;
    buff[0] = set;
    buff[1] = Cmd_type_channel;
    byte[] buffer;
    for (int i = 0; i < list.size(); i++) {
      channelData = list.get(i);
      buff[dataLength * i + 2] = (byte) channelData.getChannelId();
      buff[dataLength * i + 3] = (byte) channelData.getChannelType();

      buffer = BcdUtils.rate2BCD( channelData.getRateReceive());
      System.arraycopy(buffer, 0, buff, dataLength * i + 4, buffer.length);
      buffer = BcdUtils.rate2BCD( channelData.getRateSend());
      System.arraycopy(buffer, 0, buff, dataLength * i + 8, buffer.length);

      //if(channelData.isNumberTone()) {
      buff[dataLength * i + 12] = (byte) channelData.getNumberToneLinkmen();
      buff[dataLength * i + 13] = (byte) channelData.getNumberToneSlot();
      buff[dataLength * i + 14] = (byte) channelData.getNumberToneColor();
      //} else {
      buff[dataLength * i + 15] = (byte) channelData.getAnalogToneReceive();
      buff[dataLength * i + 16] = (byte) channelData.getAnalogToneSend();
      buff[dataLength * i + 17] = (byte) channelData.getAnalogToneBand();

      //}
      buff[dataLength * i + 18] = (byte) channelData.getPower();
    }
    return buff;
  }

  public static byte[] setProteries(ProtertyData protertyData) {
    byte[] buff = new byte[43];
    buff[0] = set;
    buff[1] = Cmd_type_property;
//    buff[24] = (byte) d.getHFvalue();

    byte[] strbuff;
    strbuff = protertyData.getDeviceMode().getBytes();
    if (strbuff.length>=6) {
      System.arraycopy(strbuff, 0, buff, 2, 6);
    }

    strbuff = protertyData.getPassword().getBytes();
    if (strbuff.length>=6) {
      System.arraycopy(strbuff, 0, buff, 8, 6);
    }

    strbuff = protertyData.getSerialNumber().getBytes();
    if (strbuff.length>=6) {
      System.arraycopy(strbuff, 0, buff, 14, 10);
    }

    buff[25] = (byte) protertyData.getTotTime();
    buff[26] = (byte) protertyData.getVox();
    buff[33] = (byte) protertyData.getActivityChannelId();
    strbuff = protertyData.getUserId().getBytes();
    System.arraycopy(strbuff, 0, buff, 27, strbuff.length);
      return buff;
  }

  public static byte[] setSms(SmsEntity smsEntity) {
    try {
      byte[] buff = new byte[2 + 22 + smsEntity.getContent().getBytes(AppConstants.charSet).length];
      buff[0] = set;
      buff[1] = Cmd_type_sms;
      int index = 5;//起始位置
      buff[4] = (byte) smsEntity.getType();
      //接收id
      byte[] receIdUuff = smsEntity.getReceiverId().getBytes();
      System.arraycopy(receIdUuff, 0, buff, index, receIdUuff.length);
      index += receIdUuff.length;
      //发送id
      byte[] sendIdBuff = smsEntity.getSendId().getBytes();
      System.arraycopy(sendIdBuff, 0, buff, index, sendIdBuff.length);
      index += 6;
      //时间 年月日时分秒
      byte[] dataBuff = StringUtils.time2BcdByte(smsEntity.getDataTime());
      System.arraycopy(dataBuff, 0, buff, index, dataBuff.length);
      index += dataBuff.length;
      //内容
      byte[] smsBuff = smsEntity.getContent().getBytes(AppConstants.charSet);
      System.arraycopy(smsBuff, 0, buff, index, smsBuff.length);
      return buff;
    } catch (Exception e) {
      e.printStackTrace();
    }
     return null;
  }

  public static void main(String[] args) {
    CmdEncrypt.processMessage(CmdEncrypt.sendMessage(getInfo()));
    //CmdEncrypt.processMessage(CmdEncrypt.sendMessage(setProteries(0,30,1,"123456")));
  }

  /**
   * 调频数据
   *
   * @param list 3组调频数据
   */
  public static byte[] setPowerTest(List<PowerTestData> list) {
    int singleLength = 4; //单组数据长度int singleLength =  4; //单组数据长度
    byte[] buff = new byte[list.size() * singleLength + 2];
    buff[0] = set;
    buff[1] = Cmd_type_power;
    PowerTestData pbean;
    for (int i = 0; i < list.size(); i++) {
      pbean = list.get(i);
      //uhf 就发  1 2 3， vhf 就发  0A 0B 0C
      //buff[i * singleLength +2] = (byte) ((pbean.isUhf() ? 9:0) + i +1);
      buff[i * singleLength + 2] = (byte) (pbean.getPowerId());
      buff[i * singleLength + 3] = (byte) pbean.getPowerLow();
      buff[i * singleLength + 4] = (byte) pbean.getPowerMiddle();
      buff[i * singleLength + 5] = (byte) pbean.getPowerHigh();
    }
    return buff;
  }

  public static byte[] setPowerTest(PowerTestData powerTestData) {
    byte[] buff = new byte[7];
    buff[0] = 0x03;
    buff[1] = Cmd_type_power;

    buff[2] = (byte) powerTestData.getPowerId();
    buff[3] = (byte) 0x01;
    buff[4] = (byte) powerTestData.getPowerLow();
    buff[5] = (byte) powerTestData.getPowerMiddle();
    buff[6] = (byte) powerTestData.getPowerHigh();
    return buff;
  }

  public static byte[] setPTT(boolean isPress) {
    byte[] buff = new byte[3];
    buff[0] = 0x02;
    buff[1] = Cmd_type_press;
    buff[2] = (byte) (isPress ? 0x01: 0x04);
    return buff;
  }

  public static byte[] setScan(boolean isPress) {
    byte[] buff = new byte[3];
    buff[0] = 0x02;
    buff[1] = Cmd_type_press;
    buff[2] = (byte) (isPress ? 0x02: 0x05);
    return buff;
  }

  public static byte[] setMonitor(boolean isPress) {
    byte[] buff = new byte[3];
    buff[0] = 0x02;
    buff[1] = Cmd_type_press;
    buff[2] = (byte) (isPress ? 0x03 : 0x06);
    return buff;
  }

  //public static byte[] setPowerTest(List<PowerTestSeekBean> list) {
  //	int singleLength =  4; //单组数据长度int singleLength =  4; //单组数据长度
  //	byte[] buff = new byte[list.size()* singleLength + 2];
  //	buff[0] = 0x03;
  //	buff[1] = Cmd_type_power;
  //	PowerTestData pbean ;
  //	for(int i=0; i<list.size(); i++) {
  //		pbean = list.get(i);
  //
  //		buff[i * singleLength +2] = (byte) pbean.getPowerId();
  //		buff[i * singleLength +3] = (byte) pbean.getPowerLow();
  //		buff[i * singleLength +4] = (byte) pbean.getPowerMiddle();
  //		buff[i * singleLength +5] = (byte) pbean.getPowerHigh();
  //	}
  //	return buff;
  //}
}
