package com.interphone.database;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

/**
 * Created by zhuj on 2016/8/1 15:22.
 */
public class SqliteHelper extends SQLiteOpenHelper {
  public SqliteHelper(Context context) {
    super(context, "InterPhone", null, 1);
  }

  @Override public void onCreate(SQLiteDatabase db) {
    create(db);
  }

  @Override public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {

  }

  private void create(SQLiteDatabase db) {
    db.execSQL(SmsDao.getCreateTable());
  }
}
