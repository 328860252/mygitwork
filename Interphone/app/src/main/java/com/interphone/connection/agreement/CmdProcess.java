package com.interphone.connection.agreement;

import com.interphone.AppConstants;
import com.interphone.connection.ICmdParseInterface;
import com.interphone.utils.LogUtils;
import com.interphone.utils.MyByteUtils;
import com.interphone.utils.MyHexUtils;

/**
 * 对收到的数据字节，全部缓存起来， 等识别出一条符合协议的数据时，将截取出来的协议数据进行解析
 * Created by zhuj on 2016/7/13 16:19.
 */
public class CmdProcess {

  private final static String  TAG = "cmdProcess";

  private ICmdParseInterface mCmdParse;

  private CmdProcess() {}

  public CmdProcess(ICmdParseInterface iCmdParseInterface) {
    this.mCmdParse = iCmdParseInterface;
  }

  private byte[] data_command = new byte[1024];//在构成一条协议数据时 清空
  private int data_index; //当前缓存的数据长度， 收到的字节个数， 在构成一条协议数据时 清0
  /**
   * 将收到的数据 截取符合协议的子集
   */
  public synchronized void ProcessDataCommand(byte[] command, int length) {
    if (command ==null || length==0) {
      return;
    }
    System.arraycopy(command, 0, data_command, data_index, length);
    data_index += length;
    System.out.println("收到数据~:" + MyHexUtils.buffer2String(command, length));
    System.out.println("收到缓存数据:" + MyHexUtils.buffer2String(data_command, data_index));
    if(data_index <6) return;
    for (int index = 0; index < data_index; index++) {
      if (data_command[index] == 0x36) {
        if (index + 2 <= data_index) {

          if (data_command[index + 1] == 0x07 && data_command[index + 2] == 0x10) {

            //int a = 0xFF;
            //byte dd = data_command[index+7];
            //if ( MyByteUtils.byteToInt(data_command[index +7]) == 0xFF) {
            LogUtils.e("ack结束", "停止ack");
            AppConstants.isWriteACK = false;
            //}
          }
        }
      }
    }
    for (int index = 0; index < data_index; index++) {
      if (data_command[index] ==  CmdEncrypt.CMD_HEAD) { // 收到头码

        int cmdLength = MyByteUtils.byteToInt(data_command[index+6]); //长度
        int cmdEndIndex = index+7+cmdLength;
        if (data_index >= (cmdEndIndex) && data_command[cmdEndIndex] == CmdEncrypt.CMD_END) {
          ProcessData(data_command, index , 8 + cmdLength);// 处理
          data_index = 0;
          data_command = new byte[1024];
          return;
        }
      }
    }
  }

  private void ProcessData(byte[] buffer, int start, int length) {
    LogUtils.logD(TAG, "收到缓存数据:" + MyHexUtils.buffer2String(data_command, data_index));
    if (mCmdParse!= null) {
      byte[] bbb = new byte[length];
      System.arraycopy(buffer, start , bbb , 0, bbb.length);
      byte[] buff = CmdEncrypt.processMessage(bbb);
      mCmdParse.parseData(buff);
    }
  }

}
