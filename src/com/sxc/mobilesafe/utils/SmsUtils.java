package com.sxc.mobilesafe.utils;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;

import org.xmlpull.v1.XmlSerializer;

import android.app.ProgressDialog;
import android.content.ContentResolver;
import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.net.Uri;
import android.os.Environment;
import android.util.Xml;

/**
 * 短信的工具类
 * 
 * @author master
 * 
 */
public class SmsUtils {
	/**
	 * 备份短信的回调接口,把需要经常更改的代码抽取出来，用于解耦代码
	 * @author master
	 *
	 */
	public interface BackUpCallBack{
		/**
		 * 开始备份的时候，设置进度的最大值
		 * @param max 总进度
		 */
		public void beforeBackup(int max);
		/**
		 * 备份过程中增加进度
		 * @param progress 当前进度
		 */
		public void onSmsBackup(int progress);
	}

	/**
	 * 备份用户的短信
	 * @param context 上下文
	 * @BackUpCallBack 备份短信的接口
	 * @throws Exception 
	 */
	public static void backupSms(Context context,BackUpCallBack callback) throws Exception{
		ContentResolver resolver=context.getContentResolver();
		File file=new File(Environment.getExternalStorageDirectory(),"backup.xml");
		FileOutputStream fos=new FileOutputStream(file);
		//把用户短信一条条读出来，按照一定的格式写入到文件里
		XmlSerializer serializer=Xml.newSerializer();//获取xml文件的生成器（序列化器）
		//初始化生成器
		serializer.setOutput(fos, "utf-8");
		serializer.startDocument("utf-8", true);
		serializer.startTag(null, "smss");
		
		Uri uri=Uri.parse("content://sms/");
		Cursor cursor=resolver.query(uri, new String[]{"body","address","type","date"}, null, null, null);
		int max=cursor.getCount();
		//开始备份前设置进度条的最大值
//		pd.setMax(max);
		callback.beforeBackup(max);
		serializer.attribute(null, "max", max+"");
		int process=0;
		while(cursor.moveToNext()){
			Thread.sleep(500);
			String body=cursor.getString(0);
			String address=cursor.getString(1);
			String type=cursor.getString(2);
			String date=cursor.getString(3);
			serializer.startTag(null, "sms");
			
			serializer.startTag(null, "body");
			serializer.text(body);
			serializer.endTag(null, "body");
			
			serializer.startTag(null, "address");
			serializer.text(address);
			serializer.endTag(null, "address");
			
			serializer.startTag(null, "type");
			serializer.text(type);
			serializer.endTag(null, "type");
			
			serializer.startTag(null, "date");
			serializer.text(date);
			serializer.endTag(null, "date");
			serializer.endTag(null, "sms");
			//备份过程中增加进度
			process++;
//			pd.setProgress(process);
			callback.onSmsBackup(process);
		}
		cursor.close();
		serializer.endTag(null, "smss");
		serializer.endDocument();
		fos.close();
	}
	/**
	 * 还原短信
	 * @param context
	 * @param callback
	 */
	public static void restoreSms(Context context,BackUpCallBack callback){
		//1.读取sd卡上的xml文件
//		Xml.newPullParser()
		//2.读取max
		//读取每一条短信信息，body data type address
		//把短信插入到系统短信息应用
		Uri uri=Uri.parse("content://sms/");
		ContentValues values=new ContentValues();
		context.getContentResolver().insert(uri, values);
	}
}
