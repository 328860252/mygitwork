package com.interphone.bean;

import android.content.Context;
import com.example.administrator.interphone.R;

/**
 * Created by zhuj on 2016/11/16 10:22.
 * 默认数据
 */
public class DefaultData {

  private static String[] arrayAnalogToneSend;
  private static String[] arrayNumberToneColor;
  private static String[] arrayPropertyTot;

  public static final double[] channelRateDefault = new double[] { 462.5625, 462.5875, 462.6125 , 462.6375
      , 462.6625, 462.6875, 462.7125, 467.5625, 467.5875, 467.6125
      , 467.6375, 467.6625, 467.6875, 467.7125, 462.5500, 462.5750};



  public final static String[] getArrayAnalogToneSned(Context mContext) {
    if(arrayAnalogToneSend==null) {
      arrayAnalogToneSend = mContext.getResources().getStringArray(R.array.Tone);
    }
    return arrayAnalogToneSend;
  }

  public final static String[] getNumberToneColor() {
    if(arrayNumberToneColor==null) {
      arrayNumberToneColor = new String[16];
      for(int i=0;i<16; i++){
        arrayNumberToneColor[i] = ""+i;
      }
    }
    return arrayNumberToneColor;
  }

  public final static String[] getPropertyTot() {
    if(arrayPropertyTot==null) {
      arrayPropertyTot = new String[16];
      for(int i=0;i<16; i++){
        arrayPropertyTot[i] = ""+(30+i *10);
      }
    }
    return arrayPropertyTot;
  }
}
