package com.interphone.database;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import com.interphone.bean.SmsEntity;
import java.util.ArrayList;
import java.util.List;

/**
 * Created by zhuj on 2016/8/1 14:59.
 */
public class SmsDao {

  private SqliteHelper mSqliteHelper;

  private static String TABLE_NAME = "smsTable";
  private static String Id = "id";         //id
  private static String Content = "content";    //内容
  private static String SendId = "sendId";     //是发送短信时， 发送者
  private static String ReceiverId = "receiverId"; //是发送短信时， 接收者
  private static String DataTime = "dataTime"; //时间
  private static String IsSend = "isSend";// 是发送 ，还是接收
  private static String Type = "type";// 定位 文字

  public static String getCreateTable() {
    StringBuffer buffer = new StringBuffer();
    buffer.append("create table ");
    buffer.append(TABLE_NAME);
    buffer.append(" ("+Id+ " integer primary key autoincrement, ");
    buffer.append(Content + " text,");
    buffer.append(SendId + " text,");
    buffer.append(ReceiverId + " text,");
    buffer.append(DataTime + " text,");
    buffer.append(Type + " integer)");
    buffer.append(IsSend + " integer)");
    return buffer.toString();
  }

  public SmsDao(Context context) {
    mSqliteHelper = new SqliteHelper(context);
  }

  public void insert(SmsEntity sms) {
    SQLiteDatabase sdb = mSqliteHelper.getWritableDatabase();
    sdb.insert(TABLE_NAME, null , entity2content(sms));
    sdb.close();
  }

  public List<SmsEntity> querySendLast100(String deviceId) {
    return query(true,deviceId, 100);
  }

  public List<SmsEntity> queryReceiverLast100(String deviceId) {
    return query(false, deviceId, 100);
  }

  /**
   *
   * @param isSend  是发送的短信 ， 还是接收的
   * @param limitSize
   * @return
   */
  private List<SmsEntity> query(boolean isSend, String deviceId, int limitSize) {
    List<SmsEntity> list = new ArrayList<SmsEntity>();
    SQLiteDatabase sdb =  mSqliteHelper.getReadableDatabase();
    Cursor mCursor = sdb.query(TABLE_NAME, new String[]{IsSend} , IsSend + " = ? limit 0,"+ limitSize, new String[]{(isSend?"1":"0")}, null, null, Id);
    SmsEntity smsEntity;
    while (mCursor.moveToNext()) {
      smsEntity = cursor2Entity(mCursor);
      list.add(smsEntity);
    }
    sdb.close();
    return list;
  }

  private ContentValues entity2content(SmsEntity smsEntity) {
    ContentValues contentValues = new ContentValues();
    contentValues.put(Content, smsEntity.getContent());
    contentValues.put(SendId, smsEntity.getSendId());
    contentValues.put(ReceiverId, smsEntity.getReceiverId());
    contentValues.put(DataTime, smsEntity.getDataTime());
    contentValues.put(IsSend, smsEntity.isSend()?1:0);
    contentValues.put(Type, smsEntity.isSend()?1:0);
    return contentValues;
  }

  private SmsEntity cursor2Entity(Cursor cursor) {
    SmsEntity smsEntity = new SmsEntity();
    smsEntity.setId(cursor.getInt(cursor.getColumnIndex(Id)));
    smsEntity.setSend(cursor.getInt(cursor.getColumnIndex(IsSend))==1);
    smsEntity.setContent(cursor.getString(cursor.getColumnIndex(Content)));
    smsEntity.setReceiverId(cursor.getString(cursor.getColumnIndex(ReceiverId)));
    smsEntity.setSendId(cursor.getString(cursor.getColumnIndex(SendId)));
    smsEntity.setDataTime(cursor.getString(cursor.getColumnIndex(DataTime)));
    smsEntity.setType(cursor.getInt(cursor.getColumnIndex(Type)));
    return smsEntity;
  }

}
