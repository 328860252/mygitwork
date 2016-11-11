package com.interphone.connection.agreement;

import com.google.common.primitives.Bytes;
import com.interphone.AppConstants;
import com.interphone.connection.ICmdParseInterface;
import com.interphone.utils.LogUtils;
import com.interphone.utils.MyByteUtils;
import com.interphone.utils.MyHexUtils;
import java.util.ArrayList;
import java.util.List;

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

  private List<Byte> dataList = new ArrayList<Byte>();
  private int data_index; //当前缓存的数据长度， 收到的字节个数
  /**
   * 将收到的数据 截取符合协议的子集, 和 应答数据
   */
  public synchronized void ProcessDataCommand(byte[] command, int length) {
    if (command ==null || length==0) {
      return;
    }
    for (byte b : command) {
      dataList.add(b);
    }
    System.out.println("收到新数据~:" + MyHexUtils.buffer2String(command, length));
    System.out.println("已缓存数据:" + MyHexUtils.buffer2String(dataList));
    String str = MyHexUtils.buffer2String(dataList);
    byte b;
    for (int index = 0; index < dataList.size(); index++) {
      b = dataList.get(index);
      if (dataList.get(index) ==  CmdEncrypt.CMD_HEAD) { // 收到头码
        //最少的协议长度
        if (index + 6 > dataList.size()) continue;
        //0x35开始后， 第6个字节为 协议长度
        int cmdLength = MyByteUtils.byteToInt(dataList.get(index+6)); //长度
        //结束字节 位置,
        int cmdEndIndex = index+7+cmdLength;
        if (dataList.size() < cmdEndIndex) continue;
        byte[] rightCmd = Bytes.toArray(dataList.subList(index, cmdEndIndex + 1));
        if (dataList.size() >= (cmdEndIndex) && dataList.get(cmdEndIndex) == CmdEncrypt.CMD_END) {
          ProcessData(rightCmd, 0 , rightCmd.length);// 处理
          //直接跳到协议尾巴位置处
          index = cmdEndIndex;
          data_index = index;
        }
      } else if (dataList.get(index) == 0x36) { //回复头码
        if (index +2 >= dataList.size()) {
          break;
        }
        if (index + 2 <= dataList.size()) {
          if (dataList.get(index + 1) == 0x07 && dataList.get(index + 2) == 0x10) { //表示多组数据接收完毕
            System.out.println("停止ACK");
            AppConstants.isWriteACK = false;
            index +=2;
            data_index = index;
          }
          if (dataList.get(index + 1) == 0x06 && dataList.get(index + 2) == 0x10) { //表示收到数据
            index +=2;
            System.out.println("收到ACK");
            //mCmdParse.parseReceiverCmd();
            data_index = index;
          }
        }
      }
    }
    if (data_index!=0) {
      dataList = dataList.subList(data_index+1, dataList.size());
      data_index=0;
    }
    System.out.println("剩下:" + MyHexUtils.buffer2String(dataList));
  }

  private void ProcessData(byte[] buffer, int start, int length) {
    //LogUtils.logD(TAG, "收到缓存数据:" + MyHexUtils.buffer2String(buffer, length));
    //System.out.println("解码协议:" + MyHexUtils.buffer2String(buffer, length));
    //if (mCmdParse!= null) {
    //  byte[] bbb = new byte[length];
    //  System.arraycopy(buffer, start , bbb , 0, bbb.length);
      byte[] buff = CmdEncrypt.processMessage(buffer);
      System.out.println("解析数据:" + MyHexUtils.buffer2String(buff));
    //  mCmdParse.parseData(buff);
    //}
  }

}
