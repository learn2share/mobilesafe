package com.sxc.mobilesafe.engine;

import java.io.File;
import java.util.ArrayList;
import java.util.List;

import android.content.Context;
import android.content.pm.ApplicationInfo;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.graphics.drawable.Drawable;

import com.sxc.mobilesafe.domain.AppInfo;

/**
 * 业务方法，提供手机里面安装的所以的应用信息
 * @author master
 *
 */
public class AppInfoProvider {

	/**
	 * 获取所有安装应用程序的信息
	 * @param context 上下文
	 * @return
	 */
	public static List<AppInfo> getAppInfos(Context context){
		PackageManager pm=context.getPackageManager();
		//返回所以的安装在系统上的应用程序包信息
		List<PackageInfo> packInfos=pm.getInstalledPackages(0);
		List<AppInfo> appInfos=new ArrayList<AppInfo>();
		for(PackageInfo packInfo:packInfos){
			AppInfo appInfo=new AppInfo();
			//packInfo 相当于一个应用程序apk包的清单文件 Manifest
			String packname=packInfo.packageName;
			Drawable icon=packInfo.applicationInfo.loadIcon(pm);
			String name=packInfo.applicationInfo.loadLabel(pm).toString();
			int flags=packInfo.applicationInfo.flags;//应用程序信息的标记,相当于用户提交的答卷
			int uid=packInfo.applicationInfo.uid;
//			File rcvfile=new File("/proc/uid_stat/"+uid+"/tcp_rcv");
//			File sndfile=new File("/proc/uid_stat/"+uid+"/tcp_snd");
			//2.3以后
			appInfo.setUid(uid);
			if((flags&ApplicationInfo.FLAG_SYSTEM)==0){
				//用户程序
				appInfo.setUserApp(true);
			}else{
				//系统程序
				appInfo.setUserApp(false);
			}
			if((flags&ApplicationInfo.FLAG_EXTERNAL_STORAGE)==0){
				//手机内存
				appInfo.setInRom(true);
			}else{
				//手机外存储设备
				appInfo.setInRom(false);
			}
			appInfo.setPackname(packname);
			appInfo.setIcon(icon);
			appInfo.setName(name);
			appInfos.add(appInfo);
		}
		return appInfos;
	}
	
}
