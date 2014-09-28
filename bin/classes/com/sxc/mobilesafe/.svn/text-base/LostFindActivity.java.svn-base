package com.sxc.mobilesafe;

import android.app.Activity;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

public class LostFindActivity extends Activity {
	
	private SharedPreferences sp;
	private TextView tv_safenumber;
	private ImageView iv_protecting;
@Override
protected void onCreate(Bundle savedInstanceState) {
	// TODO Auto-generated method stub
	super.onCreate(savedInstanceState);
	setContentView(R.layout.activity_lost_find);
	sp=getSharedPreferences("config", MODE_PRIVATE);
	//判断一下是否做过设置向导，如果 没有做过，就跳转到设置向导设置页面
	boolean configed=sp.getBoolean("configed", false);
	if(configed){
		//留在当前页面
		setContentView(R.layout.activity_lost_find);
		tv_safenumber = (TextView) findViewById(R.id.tv_safenumber);
		iv_protecting = (ImageView) findViewById(R.id.iv_protecting);
		//得到我们设置的安全号码
		String safenumber = sp.getString("safenumber", "");
		tv_safenumber.setText(safenumber);
		//设置防盗保护的状态
		boolean protecting = sp.getBoolean("protecting", false);
		if(protecting){
			//已经开启防盗保护
			iv_protecting.setImageResource(R.drawable.lock);
		}else{
			//没有开启防盗保护
			iv_protecting.setImageResource(R.drawable.unlock);
		}
		
	}else{
		//跳转到向导设置页面
		Intent intent=new Intent(this,Setup1Activity.class);
		startActivity(intent);
		//关闭当前页面
		finish();
	}
}
/**
 * 重新进入手机防盗设置向导页面
 */
public void reEnterSetup(View view){
	Intent intent=new Intent(this,Setup1Activity.class);
	startActivity(intent);
	//关闭当前页面
	finish();
}
}
