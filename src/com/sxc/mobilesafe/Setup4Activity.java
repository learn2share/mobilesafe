package com.sxc.mobilesafe;

import android.app.Activity;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.SharedPreferences.Editor;
import android.os.Bundle;
import android.view.View;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.CompoundButton.OnCheckedChangeListener;

public class Setup4Activity extends BaseSetupActivity {
	
	private CheckBox cb_proteting;
//	private SharedPreferences sp;
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_setup4);
		sp=getSharedPreferences("config", MODE_PRIVATE);
		cb_proteting = (CheckBox) findViewById(R.id.cb_proteting);
		boolean  protecting = sp.getBoolean("protecting", false);
		if(protecting){
			//�ֻ������Ѿ�������
			cb_proteting.setText("�ֻ������Ѿ�����");
			cb_proteting.setChecked(true);
		}else{
			//�ֻ�����û�п���
			cb_proteting.setText("�ֻ�����û�п���");
			cb_proteting.setChecked(false);
			
		}
		
		cb_proteting.setOnCheckedChangeListener(new OnCheckedChangeListener() {
			
			@Override
			public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
				// TODO Auto-generated method stub
				if(isChecked){
					cb_proteting.setText("�ֻ������Ѿ�����");
				}else{
					cb_proteting.setText("�ֻ�����û�п���");
				}
				
				//����ѡ���״̬
				Editor editor = sp.edit();
				editor.putBoolean("protecting", isChecked);
				editor.commit();
				
				
			}
		});
	}
	/**
	 * ��һ��
	 * @param view
	 */
	public void next(View view){
		Editor editor=sp.edit();
		editor.putBoolean("configed", true);
		editor.commit();
		Intent intent= new Intent(this,LostFindActivity.class);
		startActivity(intent);
		finish();
		overridePendingTransition(R.anim.tran_in, R.anim.tran_out);
	}
	@Override
	public void showNext() {
		// TODO Auto-generated method stub
		
	}
	@Override
	public void showPre() {
		// TODO Auto-generated method stub
		Intent intent= new Intent(this,Setup3Activity.class);
		startActivity(intent);
		finish();
		overridePendingTransition(R.anim.tran_pre_in, R.anim.tran_pre_out);
	}
}