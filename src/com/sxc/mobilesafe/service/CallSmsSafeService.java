package com.sxc.mobilesafe.service;

import java.lang.reflect.Method;

import com.android.internal.telephony.ITelephony;
import com.sxc.mobilesafe.db.dao.BlackNumberDao;

import android.app.Service;
import android.content.BroadcastReceiver;
import android.content.ContentResolver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.database.ContentObserver;
import android.net.Uri;
import android.os.Handler;
import android.os.IBinder;
import android.telephony.PhoneStateListener;
import android.telephony.SmsMessage;
import android.telephony.TelephonyManager;
import android.telephony.gsm.SmsManager;
import android.util.Log;

public class CallSmsSafeService extends Service {

	public static final String TAG = "CallSmsSafeService";
	private InnerSmsReceiver receiver;
	private BlackNumberDao dao;
	private TelephonyManager tm;
	private MyListener listener;
	@Override
	public IBinder onBind(Intent intent) {
		// TODO Auto-generated method stub
		return null;
	}

	private class InnerSmsReceiver extends BroadcastReceiver {

		@Override
		public void onReceive(Context context, Intent intent) {
			// TODO Auto-generated method stub
			Log.i(TAG, "内部广播接收者，短信到来了");
			//检查发短信的号码是否为黑名单内号码
			//获取号码
			Object[] objs=(Object[]) intent.getExtras().get("pdus");
			for(Object obj:objs){
			SmsMessage smsMessage=SmsMessage.createFromPdu((byte[]) obj);
			//得到发件人
			String sender=smsMessage.getOriginatingAddress();
			String result=dao.findMode(sender);
			if("2".equals(result)|"3".equals(result)){
				Log.i(TAG, "拦截短信");
				//4.4.2以上版本无法拦截
				abortBroadcast();
			}
			//智能拦截,获取短信内容后，从数据库中查询屏蔽信息，含有就拦截
			//语言分词技术
//			String body=smsMessage.getMessageBody();
			}
		}

	}

	@Override
	public void onCreate() {
		// TODO Auto-generated method stub
		super.onCreate();
		tm=(TelephonyManager) getSystemService(TELEPHONY_SERVICE);
		listener=new MyListener();
		tm.listen(listener, PhoneStateListener.LISTEN_CALL_STATE);
		dao=new BlackNumberDao(this);
		receiver = new InnerSmsReceiver();
		//设置接收者优先级,短信注册优先级比清单文件注册优先级要高
		IntentFilter filter=new IntentFilter("android.provider.Telephony.SMS_RECEIVED");
		filter.setPriority(IntentFilter.SYSTEM_HIGH_PRIORITY);
		// 代码注册广播者
		registerReceiver(receiver, filter);
	}

	@Override
	public void onDestroy() {
		// TODO Auto-generated method stub
		super.onDestroy();
		// 取消注册
		unregisterReceiver(receiver);
		receiver = null;
		//取消电话拦截监听
		tm.listen(listener, PhoneStateListener.LISTEN_NONE);
	}
	private class MyListener extends PhoneStateListener{

		@Override
		public void onCallStateChanged(int state, String incomingNumber) {
			// TODO Auto-generated method stub
			super.onCallStateChanged(state, incomingNumber);
			switch (state) {
			case TelephonyManager.CALL_STATE_RINGING:
				String result=dao.findMode(incomingNumber);
				if("1".equals(result)|"3".equals(result)){
					Log.i(TAG, "挂断电话");//有bug
					//删除呼叫记录，记录存储在另外一个应用的联系人私有数据库中，只能用内容服务者
//					deleteCallLog(incomingNumber);
					//因不在一个进程，所以应观察呼叫记录数据库内容的变化
					Uri uri=Uri.parse("content://call_log/calls");
					getContentResolver().registerContentObserver(uri, true, new CallLogObserver(incomingNumber,new Handler()));
					//在另一个进程里面运行的远程服务的方法.呼叫调用后可能还没有生成呼叫记录
					endCall();
				}
				break;

			default:
				break;
			}
		}
		private class CallLogObserver extends ContentObserver{

			private String incomingNumber;
			public CallLogObserver(String incomingNumber,Handler handler) {
				super(handler);
				this.incomingNumber=incomingNumber;
				// TODO Auto-generated constructor stub
			}

			@Override
			public void onChange(boolean selfChange) {
				// TODO Auto-generated method stub
				super.onChange(selfChange);
				Log.i(TAG, "删除呼叫记录。。。。。。");
				getContentResolver().unregisterContentObserver(this);
				deleteCallLog(incomingNumber);
			}
			
		}
		
		public void endCall() {
			// TODO Auto-generated method stub
			//ServiceManager类被隐藏，应该通过反射获得
//			IBinder iBinder=ServiceManager.getService(TELEPHONY_SERVICE);
			try {
				//反射获得ServiceManager类
				//加载ServiceManager的字节码文件
				Class clazz=CallSmsSafeService.class.getClassLoader().loadClass("android.os.ServiceManager");
				Method method=clazz.getDeclaredMethod("getService", String.class);
				IBinder ibinder=(IBinder) method.invoke(null, TELEPHONY_SERVICE);
				ITelephony.Stub.asInterface(ibinder).endCall();
			} catch (Exception e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		}
		
	}

	/**
	 * 利用内容提供者实现删除呼叫记录
	 * @param incomingNumber
	 */
	public void deleteCallLog(String incomingNumber) {
		// TODO Auto-generated method stub
	ContentResolver	resolver=getContentResolver();
	//呼叫记录uri路径
	Uri uri=Uri.parse("content://call_log/calls");
	resolver.delete(uri, "number=?", new String[]{incomingNumber});
	
	}
}
