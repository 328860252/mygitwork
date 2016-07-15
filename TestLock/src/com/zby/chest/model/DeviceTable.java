package com.zby.chest.model;

public class DeviceTable {
	
	protected static final String Table_Name= "SceneModeTable";
	
	
	protected static final String Id = "id";
	protected static final String Name="name";//����
	protected static final String Mac = "mac";//�����ڵ��豸
	protected static final String Password = "password";//��������
	protected static final String PairPassword = "pairPassword";//�������
	
	protected static final String LockType = "lockType";
	
	
	/**
	 * @return sql �������
	 */
	protected static final String getTable() {
		String table = "create Table " + Table_Name + " (" +
				Id + " integer primary key autoincrement ," +
				Name +" text," +
				Mac + " text,"+
				LockType + " integer, " +
				PairPassword + " text," +
				Password + " text)";
		return table;
	}
}
