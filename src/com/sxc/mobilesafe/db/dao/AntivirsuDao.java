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
 * 病毒数据库查询业务类
 * @author Administrator
 *
 */
public class AntivirsuDao {
	/**
	 * 查询一个md5是否在病毒数据库存在
	 * @param md5
	 * @return
	 */
	public static boolean isVirus(String md5){
		boolean result=false;
		String path="/data/data/com.sxc.mobilesafe/files/antivirus.db";
		//打开病毒数据库文件
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
