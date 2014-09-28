package com.sxc.mobilesafe;

import com.sxc.mobilesafe.utils.SmsUtils;
import com.sxc.mobilesafe.utils.SmsUtils.BackUpCallBack;

import android.app.Activity;
import android.app.ProgressDialog;
import android.content.Intent;
import android.drm.ProcessedData;
import android.os.Bundle;
import android.view.View;
import android.widget.Toast;

public class AtoolsActivity extends Activity {

	private ProgressDialog pd;
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_atools);
	}

	/**
	 * 点击进行号码归属地查询
	 * 
	 * @param view
	 */
	public void numberQuery(View view) {
		Intent intent = new Intent(this, NumberAddressQueryActivity.class);
		startActivity(intent);
	}

	/**
	 * 短信备份点击事件
	 * 
	 * @param view
	 *
	 */
	public void smsBackup(View view) {
		pd=new ProgressDialog(this);
		pd.setProgressStyle(ProgressDialog.STYLE_HORIZONTAL);
		pd.setMessage("正在备份短信---");
		pd.show();
		new Thread() {
			public void run() {
				try {
					SmsUtils.backupSms(AtoolsActivity.this,new BackUpCallBack() {
						
						@Override
						public void onSmsBackup(int progress) {
							// TODO Auto-generated method stub
							pd.setProgress(progress);
						}
						
						@Override
						public void beforeBackup(int max) {
							// TODO Auto-generated method stub
							pd.setMax(max);
						}
					});
					// 子线程更新UI，打印Toast
					runOnUiThread(new Runnable() {

						@Override
						public void run() {
							// TODO Auto-generated method stub

							Toast.makeText(AtoolsActivity.this, "备份成功", 0)
									.show();
						}
					});
				} catch (Exception e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
					// 子线程更新UI
					runOnUiThread(new Runnable() {

						@Override
						public void run() {
							// TODO Auto-generated method stub
							Toast.makeText(AtoolsActivity.this, "备份失败", 0)
									.show();
						}
					});
				}finally{
					pd.dismiss();
				}

			};
		}.start();
	}

	/**
	 * 短信还原点击事件
	 * 
	 * @param view
	 */
	public void smsRestore(View view) {

	}
}
