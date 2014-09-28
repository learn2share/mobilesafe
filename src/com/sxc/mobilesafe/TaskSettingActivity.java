package com.sxc.mobilesafe;

import com.sxc.mobilesafe.service.AutoCleanService;
import com.sxc.mobilesafe.utils.ServiceUtils;

import android.app.Activity;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.SharedPreferences.Editor;
import android.os.Bundle;
import android.os.CountDownTimer;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.CompoundButton.OnCheckedChangeListener;

public class TaskSettingActivity extends Activity {

	private CheckBox cb_show_system;
	private CheckBox cb_auto_clean;
	private SharedPreferences sp;
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_task_setting);
		sp=getSharedPreferences("config",MODE_PRIVATE);
		cb_show_system=(CheckBox) findViewById(R.id.cb_show_system);
		cb_show_system.setChecked(sp.getBoolean("showsystem", false));
		cb_auto_clean=(CheckBox) findViewById(R.id.cb_auto_clean);
		cb_show_system.setOnCheckedChangeListener(new OnCheckedChangeListener() {
			
			@Override
			public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
				// TODO Auto-generated method stub
				Editor editor=sp.edit();
				editor.putBoolean("showsystem", isChecked);
				editor.commit();
			}
		});
		cb_auto_clean.setOnCheckedChangeListener(new OnCheckedChangeListener() {
			
			@Override
			public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
				// �����Ĺ㲥�¼���һ������Ĺ㲥�¼������嵥�ʼ������ù㲥�������ǲ�����Ч��
				//�˹㲥�¼�ֻ���ڴ�����ע����Ч
				Intent intent =new Intent(TaskSettingActivity.this,AutoCleanService.class);
				if(isChecked){
				startService(intent);
				}else{
					stopService(intent);
				}
			}
		});
//		CountDownTimer cdt=new CountDownTimer(3000,1000) {
//			
//			@Override
//			public void onTick(long millisUntilFinished) {
//				// TODO Auto-generated method stub
//				
//			}
//			
//			@Override
//			public void onFinish() {
//				// TODO Auto-generated method stub
//				
//			}
//		};
//		cdt.start();
	}
	@Override
	protected void onStart() {
		// TODO Auto-generated method stub
		super.onStart();
		boolean running=ServiceUtils.isServiceRunning(this, "com.sxc.mobilesafe.service.AutoCleanService");
		cb_auto_clean.setChecked(running);
	}
}
