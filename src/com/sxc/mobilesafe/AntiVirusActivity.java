package com.sxc.mobilesafe;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.security.MessageDigest;
import java.util.List;

import com.sxc.mobilesafe.db.dao.AntivirsuDao;

import android.app.Activity;
import android.content.pm.ApplicationInfo;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.graphics.Color;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.view.animation.Animation;
import android.view.animation.RotateAnimation;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ProgressBar;
import android.widget.TextView;

public class AntiVirusActivity extends Activity {

	protected static final int SCANING = 0;
	protected static final int FINISH = 1;
	private ImageView iv_scan;
	private ProgressBar progressBar1;
	private PackageManager pm;
	private TextView tv_scan_status;
	private LinearLayout ll_container;
	private Handler handler=new Handler(){
		public void handleMessage(android.os.Message msg) {
			switch (msg.what) {
			case SCANING:
				ScanInfo scanInfo= (ScanInfo) msg.obj;
				tv_scan_status.setText("正在扫描:"+scanInfo.name);
				TextView tv=new TextView(getApplicationContext());
				if(scanInfo.isVirus){
					tv.setTextColor(Color.RED);
					tv.setText("发现病毒："+scanInfo.name);
				}else{
					tv.setTextColor(Color.BLACK);
					tv.setText("扫描安全:"+scanInfo.name);
				}
				ll_container.addView(tv,0);
				break;
			case FINISH:
				tv_scan_status.setText("扫描完毕");
				iv_scan.clearAnimation();
				break;

			default:
				break;
			}
		};
	};
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_anti_virus);
		iv_scan=(ImageView) findViewById(R.id.iv_scan);
		ll_container=(LinearLayout) findViewById(R.id.ll_container);
		tv_scan_status=(TextView) findViewById(R.id.tv_scan_status);
		RotateAnimation ra=new RotateAnimation(0, 360,Animation.RELATIVE_TO_SELF,0.5f,Animation.RELATIVE_TO_SELF,0.5f);
		ra.setDuration(1000);
		ra.setRepeatCount(Animation.INFINITE);
		iv_scan.startAnimation(ra);
		progressBar1 =(ProgressBar) findViewById(R.id.progressBar1);
		scanVirus();
	}
	/**
	 * 扫描病毒
	 */
	public void scanVirus(){
		pm=getPackageManager();
		tv_scan_status.setText("正在初始化杀毒引擎-------");
		new Thread(){
			public void run() {
				List<PackageInfo> infos=pm.getInstalledPackages(0);
				try {
					Thread.sleep(500);
				} catch (InterruptedException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
				progressBar1.setMax(infos.size());
				int progress=0;
				for(PackageInfo info:infos){
					//数据目录
//			String datadir=info.applicationInfo.dataDir;
					//apk文件的全路径
					String sourcedir=info.applicationInfo.sourceDir;
					String md5=getFileMd5(sourcedir);
					ScanInfo scaninfo=new ScanInfo();
					scaninfo.name=info.applicationInfo.loadLabel(pm).toString();
					scaninfo.packname=info.packageName;
					//查询md5值是否在病毒数据库里面存在
					if(AntivirsuDao.isVirus(md5)){
						//发现病毒
						scaninfo.isVirus=true;
					}else{
						//没有发现病毒
						scaninfo.isVirus=false;
					}
					Message msg=Message.obtain();
					msg.obj=scaninfo;
					msg.what=SCANING;
					handler.sendMessage(msg);
					
					try {
						Thread.sleep(300);
					} catch (InterruptedException e) {
						// TODO Auto-generated catch block
						e.printStackTrace();
					}
					progress++;
					progressBar1.setProgress(progress);
				}
				Message msg=Message.obtain();
				msg.what=FINISH;
				handler.sendMessage(msg);
				
			};
		}.start();
		
	}
	/**
	 * 扫描信息的内部类
	 * @author master
	 *
	 */
	class ScanInfo{
		String packname;
		String name;
		boolean isVirus;
	}
	/**
	 * 获取文件的Md5值
	 * @param path 文件的全路径
	 * @return
	 */
	private String getFileMd5(String path){
		try {
		// 获取一个文件的特征信息，签名信息
		File file=new File(path);
		//md5
		MessageDigest digest=MessageDigest.getInstance("md5");
		FileInputStream fis;
	
			fis = new FileInputStream(file);
	
		byte[] buffer=new byte[1024];
		int len=-1;
		while((len=fis.read(buffer))!=-1){
			digest.update(buffer, 0, len);
		}
		fis.close();
		byte[] result=digest.digest();
		StringBuffer sb=new StringBuffer();
		for(byte b:result){
			//与运算
			int number=b&0xff;
			String str=Integer.toHexString(number);
			if(str.length()==1){
				sb.append("0");
			}
			sb.append(str);
			}
		return sb.toString();
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
			return "";
		}
	}
}
