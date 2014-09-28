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
	// ��ǰ������Ϣ��״̬
	private TextView tv_status;
	// ���г������Ϣ
	List<AppInfo> appInfos;
	// �û�Ӧ����Ϣ����
	private List<AppInfo> userAppInfos;
	// ϵͳ
	private List<AppInfo> systemAppInfos;
	/**
	 * ������������
	 */
	private PopupWindow popupWindow;
	// ������ز���
	private LinearLayout ll_start;
	private LinearLayout ll_uninstall;
	private LinearLayout ll_share;
	
	//���������Ŀ
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
				.setText("sd�����ÿռ䣺" + Formatter.formatFileSize(this, sdsize));
		tv_avail_rom.setText("�ڴ���ÿռ䣺"
				+ Formatter.formatFileSize(this, romsize));
		ll_loading.setVisibility(View.VISIBLE);
		fillData();
		lv_app_manager.setOnScrollListener(new OnScrollListener() {

			@Override
			public void onScrollStateChanged(AbsListView view, int scrollState) {
				// TODO Auto-generated method stub

			}

			// ����ʱ���õķ���
			// firstVisibleItem ��һ���ɼ���Ŀ
			@Override
			public void onScroll(AbsListView view, int firstVisibleItem,
					int visibleItemCount, int totalItemCount) {
				// TODO Auto-generated method stub
				dismissPopupWindow();
				if (userAppInfos != null && systemAppInfos != null) {
					if (firstVisibleItem > userAppInfos.size()) {
						tv_status.setText("ϵͳ����:" + systemAppInfos.size() + "��");
					} else {
						tv_status.setText("�û�����:" + userAppInfos.size() + "��");

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
					// �û�����
					int newposition = position - 1;
					appInfo = userAppInfos.get(newposition);
				} else {
					// ϵͳ����
					int newposition = position - 1 - userAppInfos.size() - 1;
					appInfo = systemAppInfos.get(newposition);
				}
				// System.out.println(appInfo.getPackname());
				// �رվɵĵ�������
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
				// !!!!!!����Ч���Ĳ��ű���Ҫ�����б�����ɫ

				popupWindow.setBackgroundDrawable(new ColorDrawable(
						Color.TRANSPARENT));
				int[] location = new int[2];
				view.getLocationInWindow(location);
				// �����еĿ��߶������أ�Ҫ������Ļ��������
				int dip = 60;
				int px = DensityUtil.dip2px(getApplicationContext(), dip);
				popupWindow.showAtLocation(parent, Gravity.LEFT | Gravity.TOP,
						px, location[1]);
				// �������������Ӷ���
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
		//��������Ŀ������¼�������
		lv_app_manager.setOnItemLongClickListener(new OnItemLongClickListener() {

			@Override
			public boolean onItemLongClick(AdapterView<?> parent, View view,
					int position, long id) {
				if (position == 0) {
					return true;
				} else if (position == (userAppInfos.size() + 1)) {
					return true;
				} else if (position <= userAppInfos.size()) {
					// �û�����
					int newposition = position - 1;
					appInfo = userAppInfos.get(newposition);
				} else {
					// ϵͳ����
					int newposition = position - 1 - userAppInfos.size() - 1;
					appInfo = systemAppInfos.get(newposition);
				}
				ViewHolder holder=(ViewHolder) view.getTag();
				//�ж���Ŀ�Ƿ�����ڳ��������ݿ�����
				if(dao.find(appInfo.getPackname())){
					//���������򣬸��½���Ϊ�򿪵�С��ͼƬ
					dao.delete(appInfo.getPackname());
					holder.iv_status.setImageResource(R.drawable.unlock);
				}else{
					//�������򣬸���Ϊ�رյ���
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
						// �û�����
						userAppInfos.add(info);
					} else {
						systemAppInfos.add(info);
					}
				}
				// ����listview������������
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
		// �Ѿɵĵ�������رյ���
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

		// ����listview�ж��ٸ���Ŀ
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
				tv.setText("�û�����" + userAppInfos.size() + "��");
				return tv;
			} else if (position == (userAppInfos.size() + 1)) {
				TextView tv = new TextView(getApplicationContext());
				tv.setTextColor(Color.WHITE);
				tv.setBackgroundColor(Color.GRAY);
				tv.setText("ϵͳ����" + systemAppInfos.size() + "��");
				return tv;
			} else if (position <= userAppInfos.size()) {
				int newposition = position - 1;// �ı�ռ��һ��λ��
				// �����û�������ʾ
				appInfo = userAppInfos.get(newposition);
			} else {
				// ����ϵͳ�����
				int newposition = position - userAppInfos.size() - 2;
				appInfo = systemAppInfos.get(newposition);
			}
			View view;
			ViewHolder holder;

			if (convertView != null && convertView instanceof RelativeLayout) {
				// ����Ҫ�ж�Ϊ�գ���Ҫ�ж��Ƿ�Ϊ���ʵĴ���ȥ����
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
				// ��view���Ӷ�����Ϣ�Ա�����,��getTag����ȡ������Ϣ
				view.setTag(holder);
			}
			holder.iv_icon.setImageDrawable(appInfo.getIcon());
			holder.tv_name.setText(appInfo.getName());
			if (appInfo.isInRom()) {
				holder.tv_location.setText("�ֻ��ڴ�"+"uid:"+appInfo.getUid());
			} else {
				holder.tv_location.setText("�ⲿ�洢"+"uid:"+appInfo.getUid());
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
		//�������
		ImageView iv_status;
	}

	/**
	 * ��ȡĳ��Ŀ¼�Ŀ��ÿռ�
	 * 
	 * @param path
	 * @return
	 */
	private long getAvailSpace(String path) {
		StatFs statf = new StatFs(path);
		// statf.getBlockCount();//��ȡ�����ĸ���
		statf.getBlockCount();
		long size = statf.getBlockSize();// ��ȡ�����Ĵ�С
		long count = statf.getAvailableBlocks();// ��ȡ���õ�����ĸ���
		return size * count;
	}

	/**
	 * ���ֶ�Ӧ�ĵ���¼�
	 * 
	 * @param v
	 */
	@Override
	public void onClick(View v) {
		// TODO Auto-generated method stub
		dismissPopupWindow();
		switch (v.getId()) {
		case R.id.ll_start:
			Log.i(TAG, "����"+appInfo.getName());
			startApplication();
			break;

		case R.id.ll_uninstall:
			Log.i(TAG, "ж��"+appInfo.getName());
			if(appInfo.isUserApp()){
				
				uninstallApplication();
			}else{
				Toast.makeText(this, "ϵͳӦ��ֻ�л�ȡrootȨ�޺����ж��", 0).show();
			}
			break;
		case R.id.ll_share:
			Log.i(TAG, "����"+appInfo.getName());
			shareApplication();
			break;
		}
	}
	/**
	 * ����Ӧ�ó���
	 */
	private void shareApplication() {
		// TODO Auto-generated method stub
		Intent intent=new Intent();
		intent.setAction("android.intent.action.SEND");
		intent.addCategory("android.intent.category.DEFAULT");
		intent.setType("text/plain");
		intent.putExtra(Intent.EXTRA_TEXT,"�Ƽ���ʹ��һ������,���ƽУ�"+appInfo.getName());
		startActivity(intent);
	}

	/**
	 * ж��һ��Ӧ�ó���
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
		// ˢ�½���
		fillData();
		super.onActivityResult(requestCode, resultCode, data);
	}

	/**
	 * ����һ��Ӧ�ó���
	 */
	private void startApplication(){
		//��ѯ���Ӧ�ó�������activity��������������
		PackageManager pm=getPackageManager();
//		Intent intent=new Intent();
//		intent.setAction("android.intent.action.MAIN");
//		intent.addCategory("android.intent.category.LAUNCHER");
//		//��ѯ���������еľ�������������activity
//		pm.queryIntentActivities(intent,PackageManager.GET_INTENT_FILTERS);
		Intent intent=pm.getLaunchIntentForPackage(appInfo.getPackname());
		if(intent!=null){
			startActivity(intent);
		}else{
			Toast.makeText(this, "�޷�������ǰӦ��", 0).show();
		}
		
		
	}
}