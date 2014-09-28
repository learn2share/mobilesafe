package com.sxc.mobilesafe.db.dao;

import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;

public class NumberAddressQueryUtils {

	private static String path = "data/data/com.sxc.mobilesafe/files/address.db";

	/**
	 * 号码归属地查询
	 * 
	 * @param number
	 * @return
	 */
	public static String queryNumber(String number) {
		// path把address。db这个数据库拷贝到data/data/<包名>/files/address.db
		String address = number;
		SQLiteDatabase database = SQLiteDatabase.openDatabase(path, null,
				SQLiteDatabase.OPEN_READONLY);;
		// 手机号码的正则表达式 ^1[34568]\d{9}$
		if (number.matches("^1[34568]\\d{9}$")) {
			// 手机号码
			Cursor cursor = database
					.rawQuery(
							"select location from data2 where id=(select outkey from data1 where id=?)",
							new String[] { number.substring(0, 7) });
			while (cursor.moveToNext()) {
				String location = cursor.getString(0);
				address = location;
			}
			cursor.close();
		} else {
			// 其他号码,简单处理可以制作特殊号码数据库查询
			switch (number.length()) {
			case 3:
				// 110
				address = "报警电话";
				break;
			case 4:
				// 110
				address = "模拟器电话";
				break;
			case 5:
				// 110
				address = "客服电话";
				break;
			case 7:// 110
				address = "本地电话";
				break;
			case 8:
				// 110
				address = "本地电话";
				break;
				
			default:
				//处理长途电话大于10位 ，0开头
				if(number.length()>=10&&number.startsWith("0")){
					//长途位数处理的有问题010-123456
					Cursor cursor=database.rawQuery(
							"select location from data2 where area=?",
							new String[] { number.substring(1, 3) });
					while(cursor.moveToNext()){
						String location=cursor.getString(0);
						address=location.substring(0, location.length()-2);
					}
					cursor.close();
					// 0855-59790386
					cursor = database.rawQuery(
							"select location from data2 where area = ?",
							new String[] { number.substring(1, 4) });
					while (cursor.moveToNext()) {
						String location = cursor.getString(0);
						address = location.substring(0, location.length() - 2);

					}
				}
				break;
			}
		}
		return address;
	}
}
