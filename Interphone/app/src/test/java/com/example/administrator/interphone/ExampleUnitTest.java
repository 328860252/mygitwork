package com.example.administrator.interphone;

import com.interphone.bean.SmsEntity;
import com.interphone.connection.agreement.CmdParseImpl;
import com.interphone.connection.agreement.CmdProcess;
import com.interphone.utils.BcdUtils;
import com.interphone.utils.MyHexUtils;
import com.interphone.utils.StringUtils;
import java.util.ArrayList;
import java.util.List;
import org.junit.Test;

/**
 * To work on unit tests, switch the Test Artifact in the Build Variants view.
 */
public class ExampleUnitTest {

    @Test public void testBCD() {
        String value = "401.5625";
        int valueInt = (int) (Double.parseDouble(value) * 10000);
        byte[] buff = BcdUtils.str2Bcd(""+valueInt);
        String  buffStr = MyHexUtils.buffer2String(buff);
    }

    @Test
    public void testAddition_isCorrect() throws Exception {
        String a ="";
        byte[] bytes = a.getBytes();
        int length = bytes.length;
        byte[] bytes2 = StringUtils.time2byte("2016-08-06 14:22:34");
        String str = MyHexUtils.buffer2String(bytes2);
        System.out.println("xxx");

       List list = new ArrayList<>();
        for (int i=1; i<6; i++) {
            list.add(i);
        }
        System.out.println(""+list.get(3));
        list = list.subList(3,list.size());
        System.out.println(list.size() + "  " +list.get(0));
    }

    @Test
    public void testInistSmsEntityTest() throws  Exception {
        //Context testContext = getInstrumentation().getContext();
        SmsEntity smsEntity = new SmsEntity();
        smsEntity.setDataTime(StringUtils.getTimeString());
        smsEntity.setSend(true);
        smsEntity.setReceiverId("receive");
        smsEntity.setSendId("send");
        smsEntity.setType(2);
        smsEntity.setContent("单元测试");
        //SmsDao smsDao = new SmsDao(testContext);
        //long id = smsDao.insert(smsEntity);
        //
        //List<SmsEntity> list = smsDao.queryReceiverLast100("receive");


    }

    @Test
    public void testCmd() {
        byte[] cmd1 = MyHexUtils.hexStringToByte("03 35 01 06 00 11 B1 05 09 46 29 87 50 10 36");
        byte[] cmd2 = MyHexUtils.hexStringToByte("06");
        byte[] cmd3 = MyHexUtils.hexStringToByte("10");
        byte[] cmd4 = MyHexUtils.hexStringToByte("03 35 01 06 00 11 B1 05 09 ");
        byte[] cmd5 = MyHexUtils.hexStringToByte("46 29 87 50 10 36 06 10  36");
        byte[] cmd6 = MyHexUtils.hexStringToByte("06 10 03 35 01 06 00 11 B1 05 09 46 29 87 50 10 36");
        CmdProcess cp = new CmdProcess(null);
        cp.ProcessDataCommand(cmd1, cmd1.length);
        cp.ProcessDataCommand(cmd2, cmd2.length);
        cp.ProcessDataCommand(cmd3, cmd3.length);
        cp.ProcessDataCommand(cmd4, cmd4.length);
        cp.ProcessDataCommand(cmd5, cmd5.length);
        cp.ProcessDataCommand(cmd6, cmd6.length);
        //byte[] cmd2 = new byte[] {
        //    0x01, 0x02, 0x01, (byte) 0xB4, 0x3, 0x31, 0x32, 0x33, 0x34, 0x35, 0x36, 0x00, 0x00
        //};
        //byte[] cmd3 = new byte[] {
        //    0x01, 0x01, 0x5A, (byte) 0x57, 0x52, 0x30, 0x31, 0x30, 0x31, 0x32, 0x33, 0x34, 0x35, 0x36,
        //    0x37, 0x37, 0x38, 0x38
        //};

    }
}