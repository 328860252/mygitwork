package com.interphone.bean;

import android.content.Context;
import android.content.Intent;
import android.text.TextUtils;
import com.interphone.AppConstants;
import com.interphone.connection.ConnectAction;
import com.interphone.connection.ICmdParseInterface;
import com.interphone.connection.IConnectInterface;
import com.interphone.connection.agreement.CmdParseImpl;
import java.util.ArrayList;
import java.util.List;
import lombok.Data;

/**
 * Created by Administrator on 2016/5/13.
 */
@Data public class DeviceBean {

    private Context mContext;

    /**
     * 设备id
     */
    public String deviceId;

    /**
     * 写频密码
     * 353605超级密码 ， 全FFFFFF为空密码
     */
    public String password;

    /**
     * 属性
     */
    public String sms;

    public List<ChannelData> listChannel;

    public ProtertyData protertyData;

    public List<PowerTestData> listPower;

    public IConnectInterface mConnect;

    public ICmdParseInterface mParse;

    private final static int ChannelDataSize = 16;//信道个数
    private final static int PwoerDataSize = 3; //调频功率个数

    public void setConnectionInterface(IConnectInterface connectionInterface, Context context) {
        this.mContext = context;
        this.mConnect = connectionInterface;
        if(mParse==null) {
            mParse = new CmdParseImpl(this, context);
        }
        this.mConnect.setCmdParse(mParse);
    }

    public ChannelData getChannelData(int position) {
        if (listChannel == null) {
            listChannel = new ArrayList<ChannelData>();
            for (int i=0; i< ChannelDataSize; i++) {
                ChannelData channelData = new ChannelData();
                channelData.setChannelId(i+1);
                listChannel.add(channelData);
            }
        }
        if (position>= listChannel.size()) {
            return null;
        }
        return listChannel.get(position);
    }

    public ChannelData getChannelDataById(int id) {
        if (listChannel == null) {
            listChannel = new ArrayList<ChannelData>();
            for (int i=0; i< ChannelDataSize; i++) {
                ChannelData channelData = new ChannelData();
                channelData.setChannelId(i+1);
                listChannel.add(channelData);
            }
        }
       for (ChannelData channelData : listChannel) {
           if (channelData.getChannelId() == id) {
               return channelData;
           }
       }
        return listChannel.get(0);
    }

    public PowerTestData getPowerData(int position) {
        if(listPower == null ) {
            listPower = new ArrayList<PowerTestData>();
            for (int i=0; i < PwoerDataSize ; i++) {
                listPower.add(new PowerTestData());
            }
        }
        return listPower.get(position);
    }

    public List<PowerTestData> getListPower() {
        if(listPower==null) {
            listPower = new ArrayList<>();
            for (int i=0; i < PwoerDataSize ; i++) {
                listPower.add(new PowerTestData());
            }
        }
        return listPower;
    }

    /**
     * 密码是否正确， 包括超级密码正确
     * @param psd
     * @return
     */
    public boolean isPasswordRight(String psd) {
        if (TextUtils.isEmpty(password)) return true;
        if ( psd==null) {
            return false;
        }
        if (password.equalsIgnoreCase(psd) || password.equalsIgnoreCase(AppConstants.SUPPER_PASSWORD)) {
            return true;
        }
        return false;
    }

    public ProtertyData getProtertyData() {
        if(protertyData == null) {
            protertyData = new ProtertyData();
        }
        return protertyData;
    }

    public boolean isLink() {
        if(AppConstants.isDemo) {
            return true;
        }
        if(mConnect==null) return false;
        return mConnect.isLink();
    }

    public void stopConnect() {
        if(mConnect!=null) {
            mConnect.stopConncet();
        }
    }

    public void write(byte[] channel) {
        if (!mConnect.isLink()) {
            mContext.sendBroadcast(new Intent(ConnectAction.ACTION_SHOW_TOAST));
            return;
        }
        if(mConnect!=null) {
            AppConstants.isWriteACK = true;
            mConnect.writeAgreement(channel);
            //mConnect.write(CmdPackage.getCmdSuccess());
        }
    }

    public void writeNoEncrypt(byte[] channel) {
        if (!mConnect.isLink()) {
            mContext.sendBroadcast(new Intent(ConnectAction.ACTION_SHOW_TOAST));
            return;
        }
        if(mConnect!=null) {
            AppConstants.isWriteACK = true;
            mConnect.write(channel);
        }
    }

    public IConnectInterface getConnect() {
        return mConnect;
    }
}
