package com.interphone.activity;

import android.content.Context;
import android.hardware.usb.UsbDevice;
import android.hardware.usb.UsbDeviceConnection;
import android.hardware.usb.UsbEndpoint;
import android.hardware.usb.UsbInterface;
import android.hardware.usb.UsbManager;
import android.os.Bundle;
import android.util.Log;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.Toast;
import butterknife.Bind;
import butterknife.ButterKnife;
import com.example.administrator.interphone.R;
import java.util.HashMap;

public class DeviceListUsbActivity extends BaseActivity {

  private final String TAG = DeviceListUsbActivity.class.getSimpleName();
  @Bind(R.id.listView) ListView mListView;
  private ArrayAdapter<String> mNewArrayAdapter;


  private UsbManager myUsbManager;
  private UsbDevice myUsbDevice;
  private UsbInterface myInterface;
  private UsbDeviceConnection myDeviceConnection;

  private UsbEndpoint epOut;
  private UsbEndpoint epIn;

  private final int VendorID = 8457;
  private final int ProductID = 30264;

  @Override protected void onCreate(Bundle savedInstanceState) {
    super.onCreate(savedInstanceState);
    setContentView(R.layout.activity_device_list_usb);
    ButterKnife.bind(this);
    //initViews();
    myUsbManager = (UsbManager) getSystemService(Context.USB_SERVICE); // 获取UsbManager
    // 枚举设备
    enumerateDevice();

    findInterface();

    openDevice();

    assignEndpoint();
  }

  /**
   * 分配端点，IN | OUT，即输入输出；此处我直接用1为OUT端点，0为IN，当然你也可以通过判断
   */
  private void assignEndpoint() {
    if (myInterface == null ) {
      return;
    }
    if (myInterface.getEndpoint(1) != null) {
      epOut = myInterface.getEndpoint(1);
    }
    if (myInterface.getEndpoint(0) != null) {
      epIn = myInterface.getEndpoint(0);
    }
  }

  /**
   * 打开设备
   */
  private void openDevice() {
    if (myInterface != null) {
      UsbDeviceConnection conn = null;
      // 在open前判断是否有连接权限；对于连接权限可以静态分配，也可以动态分配权限，可以查阅相关资料
      if (myUsbManager.hasPermission(myUsbDevice)) {
        conn = myUsbManager.openDevice(myUsbDevice);
      }

      if (conn == null) {
        return;
      }

      if (conn.claimInterface(myInterface, true)) {
        myDeviceConnection = conn; // 到此你的android设备已经连上HID设备
        Log.d(TAG, "打开设备成功");
      } else {
        conn.close();
      }
    }
  }

  /**
   * 找设备接口
   */
  private void findInterface() {
    if (myUsbDevice != null) {
      Log.d(TAG, "interfaceCounts : " + myUsbDevice.getInterfaceCount());
      for (int i = 0; i < myUsbDevice.getInterfaceCount(); i++) {
        UsbInterface intf = myUsbDevice.getInterface(i);
        // 根据手上的设备做一些判断，其实这些信息都可以在枚举到设备时打印出来
        if (intf.getInterfaceClass() == 8
            && intf.getInterfaceSubclass() == 6
            && intf.getInterfaceProtocol() == 80) {
          myInterface = intf;
          Log.d(TAG, "找到我的设备接口");
        }
        break;
      }
    }
  }

  /**
   * 枚举设备
   */
  private void enumerateDevice() {
    if (myUsbManager == null)
      return;

    HashMap<String, UsbDevice> deviceList = myUsbManager.getDeviceList();
    Toast.makeText(this," " +deviceList.size(), Toast.LENGTH_LONG).show();
    if (!deviceList.isEmpty()) { // deviceList不为空
      StringBuffer sb = new StringBuffer();
      for (UsbDevice device : deviceList.values()) {
        sb.append(device.toString());
        sb.append("\n");
        // 输出设备信息
        Log.d(TAG, "DeviceInfo: " + device.getVendorId() + " , "
            + device.getProductId());
        Toast.makeText(this," " +device.getVendorId(), Toast.LENGTH_LONG).show();
        // 枚举到设备
        if (device.getVendorId() == VendorID
            && device.getProductId() == ProductID) {
          myUsbDevice = device;
          Log.d(TAG, "枚举设备成功");
        }
      }
    }
  }




  //private void initViews() {
  //  mNewArrayAdapter = new ArrayAdapter<String>(this, R.layout.device_discover);
  //
  //  mUsbManager = (UsbManager) getSystemService(Context.USB_SERVICE);
  //  HashMap<String, UsbDevice> map = mUsbManager.getDeviceList();
  //  for (UsbDevice device : map.values()) {
  //    mUsbDevice = device;
  //    //Other code
  //    mNewArrayAdapter.add(device.getDeviceName() + "/n" );
  //  }
  //  mNewArrayAdapter.notifyDataSetChanged();
  //  mListView.setAdapter(mNewArrayAdapter);
  //}
  //
  //private static final String ACTION_USB_PERMISSION = "com.android.example.USB_PERMISSION";
  //private final BroadcastReceiver mUsbReceiver = new BroadcastReceiver() {
  //  public void onReceive(Context context, Intent intent) {
  //    String action = intent.getAction();
  //    Log.e(TAG, action);
  //    if (ACTION_USB_PERMISSION.equals(action)) {
  //      synchronized (this) {
  //        mUsbDevice = (UsbDevice) intent.getParcelableExtra(UsbManager.EXTRA_DEVICE);
  //        //允许权限申请
  //        if (intent.getBooleanExtra(UsbManager.EXTRA_PERMISSION_GRANTED, false)) {
  //          if (mUsbDevice != null) {
  //            //call method to set up device communication
  //          }
  //        } else {
  //          Log.d(TAG, "permission denied for device " + mUsbDevice);
  //        }
  //      }
  //    }
  //  }
  //};
  //
  //private void registerBroadcast() {
  //  IntentFilter filter = new IntentFilter(ACTION_USB_PERMISSION);
  //  registerReceiver(mUsbReceiver, filter);
  //}
  //
  //private void requestPermission() {
  //  PendingIntent pi = PendingIntent.getBroadcast(this, 0, new Intent(ACTION_USB_PERMISSION), 0);
  //  if (mUsbManager.hasPermission(mUsbDevice)) {
  //    //Other code
  //  } else {
  //    //没有权限询问用户是否授予权限
  //    mUsbManager.requestPermission(mUsbDevice, pi); //该代码执行后，系统弹出一个对话框，
  //    //询问用户是否授予程序操作USB设备的权限
  //  }
  //}
  //
  //private void sent() {
  //  UsbInterface usbInterface = mUsbDevice.getInterface(0);
  //  //USBEndpoint为读写数据所需的节点
  //  UsbEndpoint inEndpoint = usbInterface.getEndpoint(0);  //读数据节点
  //  UsbEndpoint outEndpoint = usbInterface.getEndpoint(1); //写数据节点
  //  UsbDeviceConnection connection = mUsbManager.openDevice(mUsbDevice);
  //  connection.claimInterface(usbInterface, true);
  //  //发送数据
  //  byte[] byte2 = new byte[64];    //发送与接收字节数与设备outputreport有关
  //  int out =
  //      connection.bulkTransfer(outEndpoint, CmdPackage.getInfo(), CmdPackage.getInfo().length,
  //          5000);
  //  Log.e(TAG, "out:" + out);
  //  //读取数据1   两种方法读取数据
  //  int ret = connection.bulkTransfer(inEndpoint, byte2, byte2.length, 5000);
  //  Log.e(TAG, "ret:" + ret);
  //
  //  //还有第二种读取方式，未验证。
  //  //int outMax = outEndpoint.getMaxPacketSize();
  //  //int inMax = inEndpoint.getMaxPacketSize();
  //  //ByteBuffer byteBuffer = ByteBuffer.allocate(inMax);
  //  //UsbRequest usbRequest = new UsbRequest();
  //  //usbRequest.initialize(connection, inEndpoint);
  //  //usbRequest.queue(byteBuffer, inMax);
  //  //if(connection.requestWait() == usbRequest){
  //  //  byte[] retData = byteBuffer.array();
  //  //}
  //}
}
