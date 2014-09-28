package com.sxc.mobilesafe.receiver;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.telephony.TelephonyManager;
import android.widget.Toast;

public class BootCompleteReceiver extends BroadcastReceiver {

	private SharedPreferences sp;
	private TelephonyManager tm;
	@Override
	public void onReceive(Context context, Intent intent) {
		// TODO Auto-generated method stub
		//读取之前保存的SIM卡信息
		sp=context.getSharedPreferences("config", Context.MODE_PRIVATE);
		tm=(TelephonyManager) context.getSystemService(Context.TELEPHONY_SERVICE);
		String saveSim=sp.getString("sim", "");
		//读取当前的SIM卡信息
		String realSim=tm.getSimSerialNumber();
		//比较是否一致
		if(saveSim.equals(realSim)){
			//SIM卡没有变更
		}else{
			//SIM卡变更，发送短信给安全账户
			System.out.println("SIM卡变更");
			Toast.makeText(context, "sim卡已经变更", 1).show();
		}
	}

}
