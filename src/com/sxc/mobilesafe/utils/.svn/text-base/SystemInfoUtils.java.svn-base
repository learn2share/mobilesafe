package com.sxc.mobilesafe.utils;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.InputStreamReader;
import java.util.List;

import android.app.ActivityManager;
import android.app.ActivityManager.MemoryInfo;
import android.app.ActivityManager.RunningAppProcessInfo;
import android.content.Context;

/**
 * 系统信息的工具类
 * @author master
 *
 */
public class SystemInfoUtils {

	/**
	 * 获得正在运行的进程的数量
	 * @param context 上下文
	 * @return 正在运行的进程的数量
	 */
	public static int getRunningProcessCount(Context context){
		//PackageManager包管理器 相当于程序管理器。静态的内容
		//ActivityManager 管理活动的信息。进程管理器
		ActivityManager am=(ActivityManager) context.getSystemService(Context.ACTIVITY_SERVICE);
		List<RunningAppProcessInfo> infos=am.getRunningAppProcesses();
		return infos.size();
	}
	/**
	 * 获取搜集可用的剩余内存
	 * @param context 上下文
	 * @return 剩余内存
	 */
	public static long getAvailMem(Context context){
		ActivityManager am=(ActivityManager) context.getSystemService(Context.ACTIVITY_SERVICE);
		MemoryInfo outInfo=new MemoryInfo();
		am.getMemoryInfo(outInfo);//outInfo用来存放返回信息
		return outInfo.availMem;
	}
	/**
	 * 获取搜集可用总内存
	 * @param context 上下文
	 * @return long byte
	 */
	public static long getTotalMem(Context context){
		//不适用于低版本
//		ActivityManager am=(ActivityManager) context.getSystemService(Context.ACTIVITY_SERVICE);
//		MemoryInfo outInfo=new MemoryInfo();
//		am.getMemoryInfo(outInfo);//outInfo用来存放返回信息
//		return outInfo.totalMem;
		try {
			File file=new File("/proc/meminfo");
			FileInputStream fis=new FileInputStream(file);
			BufferedReader br=new BufferedReader(new InputStreamReader(fis));
			String line=br.readLine();
			StringBuilder sb=new StringBuilder();
			for(char c:line.toCharArray()){
				if(c>='0'&&c<='9'){
					sb.append(c);
				}
			}
			return Long.parseLong(sb.toString())*1024;
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
			return 0;
		}
	}
}
