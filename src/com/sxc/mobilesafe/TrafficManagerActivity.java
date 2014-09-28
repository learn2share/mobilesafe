package com.sxc.mobilesafe;

import java.util.List;

import android.app.Activity;
import android.content.pm.ApplicationInfo;
import android.content.pm.PackageManager;
import android.net.TrafficStats;
import android.os.Bundle;

public class TrafficManagerActivity extends Activity {

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onCreate(savedInstanceState);
		//1,获取包管理器
		PackageManager pm=getPackageManager();
		//2.遍历手机操作系统获取所有应用程序的uid
		List<ApplicationInfo> applicationInfos=	pm.getInstalledApplications(0);
		for(ApplicationInfo applicationInfo:applicationInfos){
			int uid=applicationInfo.uid;
			long tx=TrafficStats.getUidTxBytes(uid);//发送的上传的流量byte
			long rx=TrafficStats.getUidRxBytes(uid);//下载的数据流量byte
			//方法返回值-1代表的是应用程序没有产生流量 或者操作系统不支持流量统计
		}
		TrafficStats.getMobileTxBytes();//获取手机3g/2g网络上传的总流量
		TrafficStats.getMobileRxBytes();//获取手机3g/2g网络下载的总流量
		TrafficStats.getTotalTxBytes();//获取手机全部网络接口包括WiFi 3g 2g上传
		TrafficStats.getTotalRxBytes();//获取手机3g/2g网络下载的总流量   下载
		setContentView(R.layout.activity_traffic_manager);
	}
}
