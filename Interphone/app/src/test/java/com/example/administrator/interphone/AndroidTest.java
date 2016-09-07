package com.example.administrator.interphone;

import android.content.Context;
import android.test.AndroidTestCase;
import com.interphone.bean.SmsEntity;
import com.interphone.database.SmsDao;
import com.interphone.utils.StringUtils;
import java.util.List;

/**
 * Created by zhuj on 2016/8/6 15:11.
 */
public class AndroidTest extends AndroidTestCase {

  public void testInistSmsEntityTest() throws  Exception {
    Context testContext = getContext();
    SmsEntity smsEntity = new SmsEntity();
    smsEntity.setDataTime(StringUtils.getTimeString());
    smsEntity.setSend(true);
    smsEntity.setReceiverId("receive");
    smsEntity.setSendId("send");
    smsEntity.setType(2);
    smsEntity.setContent("单元测试");
    SmsDao smsDao = new SmsDao(testContext);
    long id = smsDao.insert(smsEntity);

    List<SmsEntity> list = smsDao.queryReceiverLast100("receive");


  }
}
