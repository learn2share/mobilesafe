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
		//1,��ȡ��������
		PackageManager pm=getPackageManager();
		//2.�����ֻ�����ϵͳ��ȡ����Ӧ�ó����uid
		List<ApplicationInfo> applicationInfos=	pm.getInstalledApplications(0);
		for(ApplicationInfo applicationInfo:applicationInfos){
			int uid=applicationInfo.uid;
			long tx=TrafficStats.getUidTxBytes(uid);//���͵��ϴ�������byte
			long rx=TrafficStats.getUidRxBytes(uid);//���ص���������byte
			//��������ֵ-1�������Ӧ�ó���û�в������� ���߲���ϵͳ��֧������ͳ��
		}
		TrafficStats.getMobileTxBytes();//��ȡ�ֻ�3g/2g�����ϴ���������
		TrafficStats.getMobileRxBytes();//��ȡ�ֻ�3g/2g�������ص�������
		TrafficStats.getTotalTxBytes();//��ȡ�ֻ�ȫ������ӿڰ���WiFi 3g 2g�ϴ�
		TrafficStats.getTotalRxBytes();//��ȡ�ֻ�3g/2g�������ص�������   ����
		setContentView(R.layout.activity_traffic_manager);
	}
}
