package com.example.administrator.interphone;

import com.interphone.bean.SmsEntity;
import com.interphone.utils.BcdUtils;
import com.interphone.utils.MyHexUtils;
import com.interphone.utils.StringUtils;
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
}