package com.interphone.bean;

import java.io.Serializable;
import lombok.Data;

/**
 * Created by zhuj on 2016/8/1 15:33.
 */
@Data
public class SmsEntity implements Serializable{
  /**
   * 定位
   */
  public final static int TYPE_LOCATION = 1;
  /**
   * 文本
   */
  public final static int TYPE_TEXT = 2;
  private int id;         //id
  private int type;       //
  private String content = "";    //内容
  private String sendId = "";     //是发送短信时， 发送者
  private String receiverId = ""; //是发送短信时， 接收者
  private String dataTime = ""; //时间
  private boolean isSend;// 是发送 ，还是接收
}
