package com.sxc.mobilesafe.service;

import java.util.List;

import com.sxc.mobilesafe.EnterPwdActivity;
import com.sxc.mobilesafe.db.dao.ApplockDao;

import android.app.ActivityManager;
import android.app.ActivityManager.RunningTaskInfo;
import android.app.Service;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.os.IBinder;
import android.util.Log;

/**
 * 看门狗代码 见识系统程序的运行状态
 * 
 * @author master
 * 
 */
public class WatchDogService extends Service {

	private ActivityManager am;
	private boolean flag;
	private ApplockDao dao;
	private InnerReceiver innerReceiver;
	private String tempStopProtectPackname;
	private ScreenOffReceiver offreceiver;
	private ScreenOnReceiver onreceiver;
	//优化数据查询
	private List<String> protectPacknames;
	private Intent intent;
	private DataChangeReceiver dataChangeReceiver;
	@Override
	public IBinder onBind(Intent intent) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public void onCreate() {
		// 锁屏清空tempStopprotectPackname
		onreceiver=new ScreenOnReceiver();
		registerReceiver(onreceiver, new IntentFilter(Intent.ACTION_SCREEN_ON));
		offreceiver=new ScreenOffReceiver();
		registerReceiver(offreceiver, new IntentFilter(Intent.ACTION_SCREEN_OFF));
		dataChangeReceiver=new DataChangeReceiver();
		registerReceiver(dataChangeReceiver, new IntentFilter("com.sxc.mobilesafe.applockchange"));
		am = (ActivityManager) getSystemService(ACTIVITY_SERVICE);
		dao = new ApplockDao(this);
		innerReceiver = new InnerReceiver();
		registerReceiver(innerReceiver, new IntentFilter(
				"com.sxc.mobilesafe.tempstop"));
		protectPacknames=dao.findAll();
		 intent = new Intent(getApplicationContext(),
				EnterPwdActivity.class);
		// 服务没有任务栈信息的，在服务开启activity，要指定这个activity的任务栈
		intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
		flag = true;
		watchDog();
		super.onCreate();
	}

	private void watchDog() {
		new Thread() {
			public void run() {
				while (flag) {
					List<RunningTaskInfo> infos = am.getRunningTasks(1);
					String packname = infos.get(0).topActivity.getPackageName();
					// 已经获得了操作的软件
//					System.out.println("当前操作应用程序packname" + packname);
//					if (dao.find(packname)) {//查询数据库速度太慢
					if(protectPacknames.contains(packname)){//查询内存效率调高很高
						// 判断这个应用程序是否需要临时的停止保护
						if (packname.equals(tempStopProtectPackname)) {
						
						} else {
							// 当前程序需要保护，弹出一个输入密码的界面
							// 保存加密程序的包名
							intent.putExtra("packname", packname);
							startActivity(intent);
						}
					}
					try {
						Thread.sleep(20);
					} catch (InterruptedException e) {
						// TODO Auto-generated catch block
						e.printStackTrace();
					}
				}
			};
		}.start();
	}

	@Override
	public void onDestroy() {
		// TODO Auto-generated method stub
		flag = false;
		unregisterReceiver(innerReceiver);
		innerReceiver = null;
		unregisterReceiver(offreceiver);
		offreceiver = null;
		unregisterReceiver(onreceiver);
		onreceiver = null;
		unregisterReceiver(dataChangeReceiver);
		dataChangeReceiver=null;
		super.onDestroy();
	}

	private class InnerReceiver extends BroadcastReceiver {

		@Override
		public void onReceive(Context context, Intent intent) {
			// TODO Auto-generated method stub
			System.out.println("接收到了自定义临时广播");
			tempStopProtectPackname = intent.getStringExtra("packname");
		}

	}
	private class DataChangeReceiver extends BroadcastReceiver{

		@Override
		public void onReceive(Context context, Intent intent) {
			// TODO Auto-generated method stub
			System.out.println("加锁数据库变化了");
			protectPacknames=dao.findAll();
		}
		
	}
	private class ScreenOffReceiver extends BroadcastReceiver{

		@Override
		public void onReceive(Context context, Intent intent) {
			// TODO Auto-generated method stub
			Log.i("ScreenOffReceiver", "锁屏了---");
			tempStopProtectPackname=null;
			flag = false;
		}
		
	}
	private class ScreenOnReceiver extends BroadcastReceiver{

		@Override
		public void onReceive(Context context, Intent intent) {
			// TODO Auto-generated method stub
			Log.i("ScreenOnReceiver", "屏幕开启了---");
			flag=true;
			watchDog();
		}
		
	}
}
