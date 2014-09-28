package com.sxc.mobilesafe.service;

import java.util.List;

import android.app.ActivityManager;
import android.app.ActivityManager.RunningAppProcessInfo;
import android.app.Service;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.os.IBinder;
import android.util.Log;

public class AutoCleanService extends Service {

	
	private ScreenOffReceiver receiver;
	private ActivityManager am;
	@Override
	public IBinder onBind(Intent intent) {
		// TODO Auto-generated method stub
		return null;
	}
	@Override
	public void onCreate() {
		// TODO Auto-generated method stub
		super.onCreate();
		am=(ActivityManager) getSystemService(ACTIVITY_SERVICE);
		receiver=new ScreenOffReceiver();
		registerReceiver(receiver, new IntentFilter(Intent.ACTION_SCREEN_OFF));
	}
	
	@Override
	public void onDestroy() {
		super.onDestroy();
		unregisterReceiver(receiver);
		receiver=null;
	}
	private class ScreenOffReceiver extends BroadcastReceiver{

		@Override
		public void onReceive(Context context, Intent intent) {
			// TODO Auto-generated method stub
			Log.i("ScreenOffReceiver", "À¯∆¡¡À---");
			List<RunningAppProcessInfo> infos=am.getRunningAppProcesses();
			for(RunningAppProcessInfo info:infos){
				am.killBackgroundProcesses(info.processName);
			}
		}
		
	}
}
