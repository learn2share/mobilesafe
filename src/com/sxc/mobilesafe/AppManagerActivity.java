package com.sxc.mobilesafe;

import java.text.Format;
import java.util.ArrayList;
import java.util.List;

import com.sxc.mobilesafe.db.dao.ApplockDao;
import com.sxc.mobilesafe.domain.AppInfo;
import com.sxc.mobilesafe.engine.AppInfoProvider;
import com.sxc.mobilesafe.utils.DensityUtil;

import android.app.Activity;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.graphics.drawable.Drawable;
import android.net.Uri;
import android.os.Bundle;
import android.os.Environment;
import android.os.StatFs;
import android.text.format.Formatter;
import android.util.Log;
import android.view.Gravity;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.View.OnLongClickListener;
import android.view.ViewGroup;
import android.view.animation.AlphaAnimation;
import android.view.animation.Animation;
import android.view.animation.AnimationSet;
import android.view.animation.ScaleAnimation;
import android.widget.AbsListView;
import android.widget.AbsListView.OnScrollListener;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.AdapterView.OnItemLongClickListener;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.PopupWindow;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

public class AppManagerActivity extends Activity implements OnClickListener {

	private static final String TAG ="AppManagerActivity";
	private TextView tv_avail_rom;
	private TextView tv_avail_sd;
	private ListView lv_app_manager;
	private LinearLayout ll_loading;
	// 当前程序信息的状态
	private TextView tv_status;
	// 所有程序包信息
	List<AppInfo> appInfos;
	// 用户应用信息集合
	private List<AppInfo> userAppInfos;
	// 系统
	private List<AppInfo> systemAppInfos;
	/**
	 * 弹出悬浮窗体
	 */
	private PopupWindow popupWindow;
	// 软件相关操作
	private LinearLayout ll_start;
	private LinearLayout ll_uninstall;
	private LinearLayout ll_share;
	
	//被点击的条目
	private AppInfo appInfo;
	private AppManagerAdapter adapter;
	private ApplockDao dao;
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_app_manager);
		tv_avail_rom = (TextView) findViewById(R.id.tv_avail_rom);
		tv_avail_sd = (TextView) findViewById(R.id.tv_avail_sd);
		lv_app_manager = (ListView) findViewById(R.id.lv_app_manager);
		ll_loading = (LinearLayout) findViewById(R.id.ll_loading);
		tv_status = (TextView) findViewById(R.id.tv_status);
		dao=new ApplockDao(this);
		long sdsize = getAvailSpace(Environment.getExternalStorageDirectory()
				.getAbsolutePath());
		long romsize = getAvailSpace(Environment.getDataDirectory()
				.getAbsolutePath());
		tv_avail_sd
				.setText("sd卡可用空间：" + Formatter.formatFileSize(this, sdsize));
		tv_avail_rom.setText("内存可用空间："
				+ Formatter.formatFileSize(this, romsize));
		ll_loading.setVisibility(View.VISIBLE);
		fillData();
		lv_app_manager.setOnScrollListener(new OnScrollListener() {

			@Override
			public void onScrollStateChanged(AbsListView view, int scrollState) {
				// TODO Auto-generated method stub

			}

			// 滚动时调用的方法
			// firstVisibleItem 第一个可见条目
			@Override
			public void onScroll(AbsListView view, int firstVisibleItem,
					int visibleItemCount, int totalItemCount) {
				// TODO Auto-generated method stub
				dismissPopupWindow();
				if (userAppInfos != null && systemAppInfos != null) {
					if (firstVisibleItem > userAppInfos.size()) {
						tv_status.setText("系统程序:" + systemAppInfos.size() + "个");
					} else {
						tv_status.setText("用户程序:" + userAppInfos.size() + "个");

					}
				}
			}

		});
		lv_app_manager.setOnItemClickListener(new OnItemClickListener() {

			@Override
			public void onItemClick(AdapterView<?> parent, View view,
					int position, long id) {
				// TODO Auto-generated method stub
				if (position == 0) {
					return;
				} else if (position == (userAppInfos.size() + 1)) {
					return;
				} else if (position <= userAppInfos.size()) {
					// 用户程序
					int newposition = position - 1;
					appInfo = userAppInfos.get(newposition);
				} else {
					// 系统程序
					int newposition = position - 1 - userAppInfos.size() - 1;
					appInfo = systemAppInfos.get(newposition);
				}
				// System.out.println(appInfo.getPackname());
				// 关闭旧的弹出窗体
				dismissPopupWindow();
				// TextView contentView =new TextView(getApplicationContext());
				View contentView = View.inflate(getApplicationContext(),
						R.layout.popup_app_item, null);
				ll_start = (LinearLayout) contentView
						.findViewById(R.id.ll_start);
				ll_uninstall = (LinearLayout) contentView
						.findViewById(R.id.ll_uninstall);
				ll_share = (LinearLayout) contentView
						.findViewById(R.id.ll_share);
				ll_start.setOnClickListener(AppManagerActivity.this);
				ll_share.setOnClickListener(AppManagerActivity.this);
				ll_uninstall.setOnClickListener(AppManagerActivity.this);
				popupWindow = new PopupWindow(contentView, -2, -2);
				// !!!!!!动画效果的播放必须要求窗体有背景颜色

				popupWindow.setBackgroundDrawable(new ColorDrawable(
						Color.TRANSPARENT));
				int[] location = new int[2];
				view.getLocationInWindow(location);
				// 代码中的宽高都是像素，要考虑屏幕适配问题
				int dip = 60;
				int px = DensityUtil.dip2px(getApplicationContext(), dip);
				popupWindow.showAtLocation(parent, Gravity.LEFT | Gravity.TOP,
						px, location[1]);
				// 给弹出窗口增加动画
				ScaleAnimation sa = new ScaleAnimation(0.3f, 1.0f, 0.3f, 1.0f,
						Animation.RELATIVE_TO_SELF, 0,
						Animation.RELATIVE_TO_SELF, 0.5f);
				sa.setDuration(300);
				AlphaAnimation aa = new AlphaAnimation(0.5f, 1.0f);
				aa.setDuration(300);
				AnimationSet set = new AnimationSet(false);
				set.addAnimation(aa);
				set.addAnimation(sa);
				contentView.startAnimation(set);

			}

		});
		//程序锁条目长点击事件监听器
		lv_app_manager.setOnItemLongClickListener(new OnItemLongClickListener() {

			@Override
			public boolean onItemLongClick(AdapterView<?> parent, View view,
					int position, long id) {
				if (position == 0) {
					return true;
				} else if (position == (userAppInfos.size() + 1)) {
					return true;
				} else if (position <= userAppInfos.size()) {
					// 用户程序
					int newposition = position - 1;
					appInfo = userAppInfos.get(newposition);
				} else {
					// 系统程序
					int newposition = position - 1 - userAppInfos.size() - 1;
					appInfo = systemAppInfos.get(newposition);
				}
				ViewHolder holder=(ViewHolder) view.getTag();
				//判断条目是否存在在程序锁数据库里面
				if(dao.find(appInfo.getPackname())){
					//已锁定程序，更新界面为打开的小锁图片
					dao.delete(appInfo.getPackname());
					holder.iv_status.setImageResource(R.drawable.unlock);
				}else{
					//锁定程序，更新为关闭的锁
					dao.add(appInfo.getPackname());
					holder.iv_status.setImageResource(R.drawable.lock);
				}
				return true;
			}
		});
	}

	private void fillData() {
		new Thread() {
			public void run() {
				appInfos = AppInfoProvider.getAppInfos(AppManagerActivity.this);
				userAppInfos = new ArrayList<AppInfo>();
				systemAppInfos = new ArrayList<AppInfo>();
				for (AppInfo info : appInfos) {
					if (info.isUserApp()) {
						// 用户程序
						userAppInfos.add(info);
					} else {
						systemAppInfos.add(info);
					}
				}
				// 加载listview的数据适配器
				runOnUiThread(new Runnable() {

					@Override
					public void run() {
					if(adapter==null){
						adapter=new AppManagerAdapter();
						lv_app_manager.setAdapter(adapter);
					}else{
						adapter.notifyDataSetChanged();
					}
					ll_loading.setVisibility(View.INVISIBLE);
					}
				});

			};
		}.start();
	}

	private void dismissPopupWindow() {
		// 把旧的弹出窗体关闭掉。
		if (popupWindow != null && popupWindow.isShowing()) {
			popupWindow.dismiss();
			popupWindow = null;
		}
	}

	@Override
	protected void onDestroy() {
		// TODO Auto-generated method stub
		dismissPopupWindow();
		super.onDestroy();
	}

	private class AppManagerAdapter extends BaseAdapter {

		// 控制listview有多少个条目
		@Override
		public int getCount() {
			// TODO Auto-generated method stub
			// return appInfos.size();
			return userAppInfos.size() + 1 + systemAppInfos.size() + 1;
		}

		@Override
		public View getView(int position, View convertView, ViewGroup parent) {
			// TODO Auto-generated method stub
			
			if (position == 0) {
				TextView tv = new TextView(getApplicationContext());
				tv.setTextColor(Color.WHITE);
				tv.setBackgroundColor(Color.GRAY);
				tv.setText("用户程序：" + userAppInfos.size() + "个");
				return tv;
			} else if (position == (userAppInfos.size() + 1)) {
				TextView tv = new TextView(getApplicationContext());
				tv.setTextColor(Color.WHITE);
				tv.setBackgroundColor(Color.GRAY);
				tv.setText("系统程序：" + systemAppInfos.size() + "个");
				return tv;
			} else if (position <= userAppInfos.size()) {
				int newposition = position - 1;// 文本占用一个位置
				// 留给用户程序显示
				appInfo = userAppInfos.get(newposition);
			} else {
				// 留给系统程序的
				int newposition = position - userAppInfos.size() - 2;
				appInfo = systemAppInfos.get(newposition);
			}
			View view;
			ViewHolder holder;

			if (convertView != null && convertView instanceof RelativeLayout) {
				// 不仅要判断为空，还要判断是否为合适的代码去复用
				view = convertView;
				holder = (ViewHolder) view.getTag();
			} else {
				view = View.inflate(getApplicationContext(),
						R.layout.list_item_appinfo, null);
				holder = new ViewHolder();
				holder.iv_icon = (ImageView) view
						.findViewById(R.id.iv_app_icon);
				holder.tv_location = (TextView) view
						.findViewById(R.id.tv_app_location);
				holder.tv_name = (TextView) view.findViewById(R.id.tv_app_name);
				holder.iv_status=(ImageView) view.findViewById(R.id.iv_status);
				// 给view添加额外信息以便重用,用getTag来获取额外信息
				view.setTag(holder);
			}
			holder.iv_icon.setImageDrawable(appInfo.getIcon());
			holder.tv_name.setText(appInfo.getName());
			if (appInfo.isInRom()) {
				holder.tv_location.setText("手机内存"+"uid:"+appInfo.getUid());
			} else {
				holder.tv_location.setText("外部存储"+"uid:"+appInfo.getUid());
			}
		if(dao.find(appInfo.getPackname())){
			holder.iv_status.setImageResource(R.drawable.lock);
		}else{
			holder.iv_status.setImageResource(R.drawable.unlock);
		}
		
			return view;
		}

		@Override
		public Object getItem(int position) {
			// TODO Auto-generated method stub
			return null;
		}

		@Override
		public long getItemId(int position) {
			// TODO Auto-generated method stub
			return 0;
		}

	}

	static class ViewHolder {
		TextView tv_location;
		TextView tv_name;
		ImageView iv_icon;
		//程序加锁
		ImageView iv_status;
	}

	/**
	 * 获取某个目录的可用空间
	 * 
	 * @param path
	 * @return
	 */
	private long getAvailSpace(String path) {
		StatFs statf = new StatFs(path);
		// statf.getBlockCount();//获取分区的个数
		statf.getBlockCount();
		long size = statf.getBlockSize();// 获取分区的大小
		long count = statf.getAvailableBlocks();// 获取可用的区块的个数
		return size * count;
	}

	/**
	 * 布局对应的点击事件
	 * 
	 * @param v
	 */
	@Override
	public void onClick(View v) {
		// TODO Auto-generated method stub
		dismissPopupWindow();
		switch (v.getId()) {
		case R.id.ll_start:
			Log.i(TAG, "启动"+appInfo.getName());
			startApplication();
			break;

		case R.id.ll_uninstall:
			Log.i(TAG, "卸载"+appInfo.getName());
			if(appInfo.isUserApp()){
				
				uninstallApplication();
			}else{
				Toast.makeText(this, "系统应用只有获取root权限后才能卸载", 0).show();
			}
			break;
		case R.id.ll_share:
			Log.i(TAG, "分享"+appInfo.getName());
			shareApplication();
			break;
		}
	}
	/**
	 * 分享应用程序
	 */
	private void shareApplication() {
		// TODO Auto-generated method stub
		Intent intent=new Intent();
		intent.setAction("android.intent.action.SEND");
		intent.addCategory("android.intent.category.DEFAULT");
		intent.setType("text/plain");
		intent.putExtra(Intent.EXTRA_TEXT,"推荐您使用一款软件,名称叫："+appInfo.getName());
		startActivity(intent);
	}

	/**
	 * 卸载一个应用程序
	 */
	private void uninstallApplication() {
		// TODO Auto-generated method stub
//		   <action android:name="android.intent.action.VIEW" />
//           <action android:name="android.intent.action.DELETE" />
//           <category android:name="android.intent.category.DEFAULT" />
//           <data android:scheme="package" />
		Intent intent=new Intent();
		intent.setAction("android.intent.action.VIEW");
		intent.setAction("android.intent.action.DELETE");
		intent.addCategory("android.intent.category.DEFAULT");
		intent.setData(Uri.parse("package:"+appInfo.getPackname()));
		startActivityForResult(intent, 0);
		
	}
	@Override
	protected void onActivityResult(int requestCode, int resultCode, Intent data) {
		// 刷新界面
		fillData();
		super.onActivityResult(requestCode, resultCode, data);
	}

	/**
	 * 开启一个应用程序
	 */
	private void startApplication(){
		//查询这个应用程序的入口activity，把他开启起来
		PackageManager pm=getPackageManager();
//		Intent intent=new Intent();
//		intent.setAction("android.intent.action.MAIN");
//		intent.addCategory("android.intent.category.LAUNCHER");
//		//查询出来了所有的具有启动能力的activity
//		pm.queryIntentActivities(intent,PackageManager.GET_INTENT_FILTERS);
		Intent intent=pm.getLaunchIntentForPackage(appInfo.getPackname());
		if(intent!=null){
			startActivity(intent);
		}else{
			Toast.makeText(this, "无法启动当前应用", 0).show();
		}
		
		
	}
}
