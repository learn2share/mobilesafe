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
 * ���ŵĹ�����
 * 
 * @author master
 * 
 */
public class SmsUtils {
	/**
	 * ���ݶ��ŵĻص��ӿ�,����Ҫ�������ĵĴ����ȡ���������ڽ������
	 * @author master
	 *
	 */
	public interface BackUpCallBack{
		/**
		 * ��ʼ���ݵ�ʱ�����ý��ȵ����ֵ
		 * @param max �ܽ���
		 */
		public void beforeBackup(int max);
		/**
		 * ���ݹ��������ӽ���
		 * @param progress ��ǰ����
		 */
		public void onSmsBackup(int progress);
	}

	/**
	 * �����û��Ķ���
	 * @param context ������
	 * @BackUpCallBack ���ݶ��ŵĽӿ�
	 * @throws Exception 
	 */
	public static void backupSms(Context context,BackUpCallBack callback) throws Exception{
		ContentResolver resolver=context.getContentResolver();
		File file=new File(Environment.getExternalStorageDirectory(),"backup.xml");
		FileOutputStream fos=new FileOutputStream(file);
		//���û�����һ����������������һ���ĸ�ʽд�뵽�ļ���
		XmlSerializer serializer=Xml.newSerializer();//��ȡxml�ļ��������������л�����
		//��ʼ��������
		serializer.setOutput(fos, "utf-8");
		serializer.startDocument("utf-8", true);
		serializer.startTag(null, "smss");
		
		Uri uri=Uri.parse("content://sms/");
		Cursor cursor=resolver.query(uri, new String[]{"body","address","type","date"}, null, null, null);
		int max=cursor.getCount();
		//��ʼ����ǰ���ý����������ֵ
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
			//���ݹ��������ӽ���
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
	 * ��ԭ����
	 * @param context
	 * @param callback
	 */
	public static void restoreSms(Context context,BackUpCallBack callback){
		//1.��ȡsd���ϵ�xml�ļ�
//		Xml.newPullParser()
		//2.��ȡmax
		//��ȡÿһ��������Ϣ��body data type address
		//�Ѷ��Ų��뵽ϵͳ����ϢӦ��
		Uri uri=Uri.parse("content://sms/");
		ContentValues values=new ContentValues();
		context.getContentResolver().insert(uri, values);
	}
}
