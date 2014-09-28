package com.sxc.mobilesafe;

import com.sxc.mobilesafe.db.dao.NumberAddressQueryUtils;

import android.app.Activity;
import android.os.Bundle;
import android.os.Vibrator;
import android.text.Editable;
import android.text.TextUtils;
import android.text.TextWatcher;
import android.util.Log;
import android.view.View;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.view.animation.Interpolator;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

public class NumberAddressQueryActivity extends Activity {

	private static final String TAG = "NumberAddressQueryActivity";
	private EditText ed_phone;
	private TextView result;
	//系统提供的震动效果
	private Vibrator vibrator;
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_numbera_address_query);
		ed_phone=(EditText) findViewById(R.id.ed_phone);
		result=(TextView) findViewById(R.id.result);
		//振动器初始化
		vibrator=(Vibrator) getSystemService(VIBRATOR_SERVICE);
		ed_phone.addTextChangedListener(new TextWatcher() {
			
			/**
			 * 当文本发生变化时回调
			 */
			@Override
			public void onTextChanged(CharSequence s, int start, int before, int count) {
				// TODO Auto-generated method stub
				if(s!=null&s.length()>=3){
					//查询数据库，并显示结果
					String address=NumberAddressQueryUtils.queryNumber(s.toString());
					result.setText(address);
				}
			}
			
			/**
			 * 当文本发生变化之前回调
			 */
			@Override
			public void beforeTextChanged(CharSequence s, int start, int count,
					int after) {
				// TODO Auto-generated method stub
				
			}
			
			/**
			 * 当文本发生变化之后回调
			 */
			@Override
			public void afterTextChanged(Editable s) {
				// TODO Auto-generated method stub
				
			}
		});
	}
	/**
	 * 查询号码归属地
	 * @param view
	 */
	public void numberAddressQuery(View view){
		String phone=ed_phone.getText().toString().trim();
		if(TextUtils.isEmpty(phone)){
			Toast.makeText(this,"号码为空", 0).show();
		     Animation shake = AnimationUtils.loadAnimation(this, R.anim.shake);
		     ed_phone.startAnimation(shake);
		     //震动
//		     vibrator.vibrate(1000);
		     long[] pattern={200,200,300,300,500,500};
		     //-1不重复 0循环震动1从300开始震动
		     vibrator.vibrate(pattern, -1);
			return;
		}else{
			//去数据库查询
			String address=NumberAddressQueryUtils.queryNumber(phone);
			result.setText(address);
			//1.网络查询；2.数据库查询
			//写一个工具类，去查询数据库
			Log.i(TAG, phone+"号码归属地为-----");
		}
	}
}
