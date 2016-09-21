package tsocket.zby.com.tsocket.connection.ble;

import android.util.Log;
import tsocket.zby.com.tsocket.connection.ICmdParseInterface;
import tsocket.zby.com.tsocket.connection.IConnectInterface;
import tsocket.zby.com.tsocket.connection.agreement.CmdEncrypt;

public class BleImpl implements IConnectInterface {

  private final String TAG = BleImpl.class.getSimpleName();

  private BluetoothLeServiceMulp mService;
  private String mDeviceAddress;

  private boolean isLink;

  public BleImpl(BluetoothLeServiceMulp service) {
    this.mService = service;
  }

  private int count = 0;
  private long lastLinkTime = 0;

  @Override public boolean connect(String address, String pwd) {
    // TODO Auto-generated method stub
    long nowTime = System.currentTimeMillis();
    if (mService.isConnecting(mDeviceAddress)) {
      //if(nowTime - lastLinkTime>2000) {
      //	lastLinkTime = nowTime;
      //	count ++;
      //	if(count > AppConstants.connecting_count) {
      //		mService.close(mDeviceAddress);
      //		count =0;
      //	}
      //}
      return false;
    }
    //count =0;
    //if(nowTime - lastLinkTime<2000) {
    //	return false;
    //}
    //lastLinkTime = nowTime;
    isLink = mService.connect(address);
    if (isLink) {
      mDeviceAddress = address;
    }
    return isLink;
  }

  @Override public void stopConncet() {
    // TODO Auto-generated method stub
    if (mService != null) {
      mService.disconnect(mDeviceAddress);
    }
  }

  @Override public boolean write(byte[] buffer) {
    // TODO Auto-generated method stub
    if (buffer == null) return false;
    return mService.writeLlsAlertLevel(mDeviceAddress, buffer);
  }

  @Override public boolean writeAgreement(byte[] buffer) {
    // TODO Auto-generated method stub
    return write(CmdEncrypt.sendMessage(buffer));
  }

  @Override public boolean isLink() {
    // TODO Auto-generated method stub
    if (mService == null) {
      Log.d(TAG, "service is null");
      return false;
    }
    return mService.isLink(mDeviceAddress);
  }

  @Override public boolean isLink(String mac) {
    if (mService == null|| mac ==null) {
      Log.d(TAG, "service is null");
      return false;
    }
    return mService.isLink(mDeviceAddress);
  }

  public String getDeviceAddress() {
    return mDeviceAddress;
  }

  public void onBleDestory() {
    // TODO Auto-generated method stub
    mService.close(mDeviceAddress);
  }

  @Override public boolean isConnecting() {
    // TODO Auto-generated method stub
    if (mService == null) {
      Log.d(TAG, "service is null");
      return false;
    }
    return mService.isConnecting(mDeviceAddress);
  }

  @Override public int getConnectType() {
    return type_ble;
  }

  @Override public void setCmdParse(ICmdParseInterface cmdParse) {

  }

  public void writeNoresponse(byte[] buffer) {
    // TODO Auto-generated method stub
    mService.writeLlsAlertLevelWait(mDeviceAddress, buffer);
  }
}
