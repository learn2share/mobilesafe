package com.sxc.mobilesafe.receiver;

import com.sxc.mobilesafe.service.UpdateWidgetService;

import android.appwidget.AppWidgetManager;
import android.appwidget.AppWidgetProvider;
import android.content.Context;
import android.content.Intent;

public class MyWidget extends AppWidgetProvider {

	@Override
	public void onReceive(Context context, Intent intent) {
		System.out.println("onReceive");
		Intent i=new Intent(context,UpdateWidgetService.class);
		context.startService(i);
		super.onReceive(context, intent);
	}
	@Override
	public void onUpdate(Context context, AppWidgetManager appWidgetManager,
			int[] appWidgetIds) {
		System.out.println("onUpdate");
		Intent intent=new Intent(context,UpdateWidgetService.class);
		context.startService(intent);
		super.onUpdate(context, appWidgetManager, appWidgetIds);
	}
	@Override
	public void onEnabled(Context context) {
		// TODO Auto-generated method stub
		System.out.println("OnEnabled");
		Intent intent=new Intent(context,UpdateWidgetService.class);
		context.startService(intent);
		super.onEnabled(context);
	}
	@Override
	public void onDisabled(Context context) {
		// TODO Auto-generated method stub
		System.out.println("OnDisabled");
		Intent intent=new Intent(context,UpdateWidgetService.class);
		context.stopService(intent);
		super.onDisabled(context);
	}
}
