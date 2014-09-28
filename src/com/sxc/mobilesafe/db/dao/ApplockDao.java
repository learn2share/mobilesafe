package com.sxc.mobilesafe.db.dao;

import java.util.ArrayList;
import java.util.List;

import android.content.ContentValues;
import android.content.Context;
import android.content.Intent;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.text.style.IconMarginSpan;

import com.sxc.mobilesafe.db.ApplockDBOpenHelper;
import com.sxc.mobilesafe.db.BlackNumberDBOpenHelper;
import com.sxc.mobilesafe.domain.BlackNumberInfo;

/**
 * 程序锁的dao
 * @author Administrator
 *
 */
public class ApplockDao {

	private ApplockDBOpenHelper helper;
	private Context context;
	/**
	 * 构造方法
	 * @param context
	 */
	public ApplockDao(Context context){
		helper=new ApplockDBOpenHelper(context);
		this.context=context;
	}
	/**
	 * 查询程序是否加锁
	 * @param packname
	 * @return
	 */
	public boolean find(String packname){
		boolean result=false;
		SQLiteDatabase db=helper.getReadableDatabase();
		Cursor cursor=db.query("applock", null, "packname=?", new String[]{packname},null,null,null);
		if(cursor.moveToNext()){
			result=true;
		}
		cursor.close();
		db.close();
		return result;
		
	}

	/**
	 * 查询程序全部的包名
	 * @param packname
	 * @return
	 */
	public List<String> findAll(){
		List<String> protectPacknames=new ArrayList<String>();
		SQLiteDatabase db=helper.getReadableDatabase();
		Cursor cursor=db.query("applock",new String[]{"packname"},null,null,null,null,null);
		while(cursor.moveToNext()){
		protectPacknames.add(cursor.getString(0));
		}
		cursor.close();
		db.close();
		return protectPacknames;
		
	}
	/**
	 * 添加一个要锁定应用程序的包名
	 * @param packname
	 */
	public void add(String packname){
		SQLiteDatabase db=helper.getWritableDatabase();
		ContentValues values=new ContentValues();
		values.put("packname", packname);
		db.insert("applock", null, values);
		db.close();
		Intent intent=new Intent();
		intent.setAction("com.sxc.mobilesafe.applockchange");
		context.sendBroadcast(intent);
	}
	/**
	 * 删除一个锁屏应用程序
	 * @param packname 包名
	 */
	public void delete(String packname){
		SQLiteDatabase db=helper.getWritableDatabase();
		db.delete("applock", "packname=?", new String[]{packname});
		db.close();
		Intent intent=new Intent();
		intent.setAction("com.sxc.mobilesafe.applockchange");
		context.sendBroadcast(intent);
	}
}
