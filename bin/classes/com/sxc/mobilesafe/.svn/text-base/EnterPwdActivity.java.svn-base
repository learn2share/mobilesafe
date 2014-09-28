package com.sxc.mobilesafe;

import com.sxc.mobilesafe.utils.MD5Utils;

import android.app.Activity;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.pm.ApplicationInfo;
import android.content.pm.PackageManager;
import android.content.pm.PackageManager.NameNotFoundException;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

public class EnterPwdActivity extends Activity {

	private EditText et_password;
	private SharedPreferences sp;
	private String packname;
	private TextView tv_name;
	private ImageView iv_icon;
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_enter_pwd);
		et_password=(EditText) findViewById(R.id.et_password);
		tv_name=(TextView) findViewById(R.id.tv_name);
		iv_icon=(ImageView) findViewById(R.id.iv_icon);
		sp=getSharedPreferences("config", MODE_PRIVATE);
		Intent intent=getIntent();
		//当前要保护的应用程序包名
		packname=intent.getStringExtra("packname");
		PackageManager pm=getPackageManager();
		try {
		ApplicationInfo info=pm.getApplicationInfo(packname, 0);
		tv_name.setText(info.loadLabel(pm));
		iv_icon.setImageDrawable(info.loadIcon(pm));
		} catch (NameNotFoundException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
	}
	//改变返回键，返回位置
	@Override
	public void onBackPressed() {
	//返回桌面,所有的activity最小化了，不执行Ondestory,只执行OnStop方法
//		  <action android:name="android.intent.action.MAIN" />
//          <category android:name="android.intent.category.HOME" />
//          <category android:name="android.intent.category.DEFAULT" />
//          <category android:name="android.intent.category.MONKEY"/>
		Intent intent=new Intent();
		intent.setAction("android.intent.action.MAIN");
		intent.addCategory("android.intent.category.HOME");
		intent.addCategory("android.intent.category.DEFAULT");
		intent.addCategory("android.intent.category.MONKEY");
		startActivity(intent);
	}
	@Override
	protected void onStop() {
		// TODO Auto-generated method stub
		finish();
		super.onStop();
	}
	public void click(View view){
		String pwd=et_password.getText().toString().trim();
		//得到我们设置的安全号码
		String savePassword = sp.getString("password", "");
		if(TextUtils.isEmpty(pwd)){
			Toast.makeText(this, "密码不能为空", 0).show();
			return;
		}
		if(MD5Utils.md5Password(pwd).equals(savePassword)){
			//告诉看门狗这个程序已经输入正确密码，可以临时停止对应用程序的保护
			//自定义广播
			Intent intent=new Intent();
			intent.setAction("com.sxc.mobilesafe.tempstop");
			intent.putExtra("packname", packname);
			sendBroadcast(intent);
			finish();
		}else{
			Toast.makeText(this, "输入密码错误", 0).show();
		}
	}
}
