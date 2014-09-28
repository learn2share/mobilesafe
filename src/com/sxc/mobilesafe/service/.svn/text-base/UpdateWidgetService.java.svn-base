package com.sxc.mobilesafe.service;

import java.util.List;
import java.util.Timer;
import java.util.TimerTask;

import com.sxc.mobilesafe.R;
import com.sxc.mobilesafe.receiver.MyWidget;
import com.sxc.mobilesafe.utils.SystemInfoUtils;

import android.app.PendingIntent;
import android.app.Service;
import android.app.ActivityManager.RunningAppProcessInfo;
import android.appwidget.AppWidgetManager;
import android.content.BroadcastReceiver;
import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.os.IBinder;
import android.text.format.Formatter;
import android.util.Log;
import android.widget.RemoteViews;

public class UpdateWidgetService extends Service {

	protected static final String TAG = "UpdateWidgetService";
	private Timer timer;
	private TimerTask task;
	private ScreenOffReceiver offreceiver;
	private ScreenOnReceiver onreceiver;
	/**
	 * widget管理器
	 */
	private AppWidgetManager awm;
	@Override
	public IBinder onBind(Intent intent) {
		// TODO Auto-generated method stub
		return null;
	}
	@Override
	public void onCreate() {
		onreceiver=new ScreenOnReceiver();
		offreceiver=new ScreenOffReceiver();
		registerReceiver(onreceiver, new IntentFilter(Intent.ACTION_SCREEN_ON));
		registerReceiver(offreceiver, new IntentFilter(Intent.ACTION_SCREEN_OFF));
		awm=AppWidgetManager.getInstance(this);
		startTimer();
		super.onCreate();
	}
	private void startTimer() {
		if(timer==null&&task==null){
		timer=new Timer();
		task=new TimerTask() {
			
			@Override
			public void run() {
				// TODO Auto-generated method stub
				Log.i(TAG, "更新widget");
				//设置更新的组件
				ComponentName provider=new ComponentName(UpdateWidgetService.this,MyWidget.class);
				RemoteViews views=new RemoteViews(getPackageName(),R.layout.process_widget);
				views.setTextViewText(R.id.process_count, "正在运行进程："+
				SystemInfoUtils.getRunningProcessCount(getApplicationContext())+"个");
				long size=SystemInfoUtils.getAvailMem(getApplicationContext());
				System.out.println(Formatter.formatFileSize(getApplicationContext(), size));
				views.setTextViewText(R.id.process_memory, "可用内存："+
						Formatter.formatFileSize(getApplicationContext(), size));
				//描述一个动作，该动作由另外一个应用程序执行
				//自定义一个广播事件，杀死后台进度的事件
				Intent intent=new Intent();
				intent.setAction("com.sxc.mobilesafe.killall");
				PendingIntent pendingIntent=PendingIntent.getBroadcast(getApplicationContext(), 0, intent, 
						PendingIntent.FLAG_UPDATE_CURRENT);
				views.setOnClickPendingIntent(R.id.btn_clear, pendingIntent);
				awm.updateAppWidget(provider, views);
			}
		};
		timer.schedule(task, 0,3000);
		}
	}
	@Override
	public void onDestroy() {
		// TODO Auto-generated method stub
		super.onDestroy();
		unregisterReceiver(onreceiver);
		unregisterReceiver(offreceiver);
		onreceiver=null;
		offreceiver=null;
		stopTimer();
	}
	private void stopTimer() {
		if(timer!=null&&task!=null){
		timer.cancel();
		task.cancel();
		timer=null;
		task=null;
		}
	}
	private class ScreenOffReceiver extends BroadcastReceiver{

		@Override
		public void onReceive(Context context, Intent intent) {
			// TODO Auto-generated method stub
			Log.i("ScreenOffReceiver", "锁屏了---");
			stopTimer();
		}
		
	}
	private class ScreenOnReceiver extends BroadcastReceiver{

		@Override
		public void onReceive(Context context, Intent intent) {
			// TODO Auto-generated method stub
			Log.i("ScreenOnReceiver", "屏幕开启了---");
			startTimer();
		}
		
	}

}
