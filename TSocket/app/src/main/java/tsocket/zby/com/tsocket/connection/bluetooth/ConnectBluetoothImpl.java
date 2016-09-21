package tsocket.zby.com.tsocket.connection.bluetooth;

import android.bluetooth.BluetoothAdapter;
import android.bluetooth.BluetoothDevice;
import android.content.Context;
import tsocket.zby.com.tsocket.AppConstants;
import tsocket.zby.com.tsocket.connection.ICmdParseInterface;
import tsocket.zby.com.tsocket.connection.IConnectInterface;
import tsocket.zby.com.tsocket.connection.agreement.CmdEncrypt;

public class ConnectBluetoothImpl implements IConnectInterface {
	private BluetoothChatManager BTchat;

	private BluetoothAdapter adapter;

	private BluetoothDevice mBluetoothDevice;

	public ConnectBluetoothImpl(Context context) {
		BTchat = new BluetoothChatManager(context);
	}

	@Override
	public boolean connect(String address, String pwd) {
		// TODO Auto-generated method stub
		adapter = BluetoothAdapter.getDefaultAdapter();
		mBluetoothDevice = adapter.getRemoteDevice(address);
		BTchat.connect(mBluetoothDevice);
		return true;
	}

	@Override
	public void stopConncet() {
		// TODO Auto-generated method stub
		if(BTchat!=null) {
			BTchat.stop();
		}
	}

	@Override
	public boolean write(byte[] buffer) {
		// TODO Auto-generated method stub
		if(BTchat != null) {
			return BTchat.write(buffer);
		}
		return false;
	}

	@Override
	public boolean writeAgreement(byte[] buffer) {
		// TODO Auto-generated method stub
		if(BTchat!=null) {
			return BTchat.write(CmdEncrypt.sendMessage(buffer));
		}
		return false;
	}

	@Override
	public boolean isLink() {
		// TODO Auto-generated method stub
		if (AppConstants.isDemo) {
			return true;
		}
		return BTchat.islink();
	}

	@Override public boolean isLink(String mac) {
		return BTchat.islink();
	}

	@Override
	public boolean isConnecting() {
		return false;
	}

	@Override public int getConnectType() {
		return IConnectInterface.type_bluetooth;
	}

	@Override public void setCmdParse(ICmdParseInterface cmdParse) {
		BTchat.setCmdParse(cmdParse);
	}

}
