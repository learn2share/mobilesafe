package com.sxc.mobilesafe.db.dao;

import java.util.ArrayList;
import java.util.List;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;

import com.sxc.mobilesafe.db.BlackNumberDBOpenHelper;
import com.sxc.mobilesafe.domain.BlackNumberInfo;

/**
 * �������ݿ��ѯҵ����
 * @author Administrator
 *
 */
public class AntivirsuDao {
	/**
	 * ��ѯһ��md5�Ƿ��ڲ������ݿ����
	 * @param md5
	 * @return
	 */
	public static boolean isVirus(String md5){
		boolean result=false;
		String path="/data/data/com.sxc.mobilesafe/files/antivirus.db";
		//�򿪲������ݿ��ļ�
		SQLiteDatabase db=SQLiteDatabase.openDatabase(path,null,SQLiteDatabase.OPEN_READONLY);
		Cursor cursor=db.rawQuery("select * from datable where md5=?", new String[]{md5});
		if(cursor.moveToNext()){
			result=true;
		}
		cursor.close();
		db.close();
		return result;
		
	}
	
}
