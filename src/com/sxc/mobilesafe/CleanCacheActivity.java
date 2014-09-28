package com.sxc.mobilesafe;

import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.util.List;

import android.app.Activity;
import android.content.pm.ApplicationInfo;
import android.content.pm.IPackageDataObserver;
import android.content.pm.IPackageStatsObserver;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.content.pm.PackageManager.NameNotFoundException;
import android.content.pm.PackageStats;
import android.graphics.Color;
import android.os.Bundle;
import android.os.RemoteException;
import android.text.format.Formatter;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ProgressBar;
import android.widget.TextView;

public class CleanCacheActivity extends Activity {

	private ProgressBar pb;
	private TextView tv_scan_status;
	private PackageManager pm;
	private LinearLayout ll_container;
	/**
	 * 4.1.2
	 *     public abstract void getPackageSizeInfo(String packageName,
            IPackageStatsObserver observer);
            4.4.2
                /**
     * Retrieve the size information for a package.
     * Since this may take a little while, the result will
     * be posted back to the given observer.  The calling context
     * should have the {@link android.Manifest.permission#GET_PACKAGE_SIZE} permission.
     *
     * @param packageName The name of the package whose size information is to be retrieved
     * @param userHandle The user whose size information should be retrieved.
     * @param observer An observer callback to get notified when the operation
     * is complete.
     * {@link android.content.pm.IPackageStatsObserver#onGetStatsCompleted(PackageStats, boolean)}
     * The observer's callback is invoked with a PackageStats object(containing the
     * code, data and cache sizes of the package) and a boolean value representing
     * the status of the operation. observer may be null to indicate that
     * no callback is desired.
     *
     * @hide
     *
    public abstract void getPackageSizeInfo(String packageName, int userHandle,
            IPackageStatsObserver observer);

    /**
     * Like {@link #getPackageSizeInfo(String, int, IPackageStatsObserver)}, but
     * returns the size for the calling user.
     *
     * @hide
     *
    public void getPackageSizeInfo(String packageName, IPackageStatsObserver observer) {
        getPackageSizeInfo(packageName, UserHandle.myUserId(), observer);
    
	 */

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_clean_cach);
		pb = (ProgressBar) findViewById(R.id.pb);
		tv_scan_status = (TextView) findViewById(R.id.tv_scan_status);
		ll_container=(LinearLayout) findViewById(R.id.ll_container);
		scanCache();
	}

	/**
	 * 扫描手机里面所有应用程序的缓存信息
	 */
	private void scanCache() {
		pm = getPackageManager();
		new Thread() {
			public void run() {
				Method getPackageSizeInfoMethod = null;
				Method[] methods = PackageManager.class.getMethods();
				for (Method method : methods) {
//					4.4有两个getPackageSizeInfo，该方法不适用4。4版本
					if("getPackageSizeInfo".equals(method.getName())) {
						getPackageSizeInfoMethod = method;
					}
				}
				List<PackageInfo> packInfos = pm.getInstalledPackages(0);
				pb.setMax(packInfos.size());
				int progress = 0;
				for (PackageInfo packInfo : packInfos) {
//					System.out.println(packInfo.packageName);
					try {
						//4.4报错
						getPackageSizeInfoMethod.invoke(pm,
								packInfo.packageName, new MyDataObserver());
						Thread.sleep(50);
					} catch (Exception e) {
						e.printStackTrace();
					}
					progress++;
					pb.setProgress(progress);
				}
	runOnUiThread(new Runnable() {
					
					@Override
					public void run() {
						tv_scan_status.setText("扫描完毕...");
					}
				});
			};
		}.start();

	}

	private class MyDataObserver extends IPackageStatsObserver.Stub {
		@Override
		public void onGetStatsCompleted(PackageStats pStats, boolean succeeded)
				throws RemoteException {
			final long cache = pStats.cacheSize;
			long code = pStats.codeSize;
			long data = pStats.dataSize;
			final String packname = pStats.packageName;
			final ApplicationInfo appInfo;
			try {
				appInfo = pm.getApplicationInfo(packname, 0);
				runOnUiThread(new Runnable() {
					@Override
					public void run() {
						tv_scan_status.setText("正在扫描：" + appInfo.loadLabel(pm));
						if (cache > 0) {
							View view = View.inflate(getApplicationContext(), R.layout.list_item_cacheinfo, null);
							TextView tv_cache = (TextView) view.findViewById(R.id.tv_cache_size);
							tv_cache.setText("缓存大小:"+Formatter.formatFileSize(getApplicationContext(), cache));
							TextView tv_name = (TextView) view.findViewById(R.id.tv_app_name);
							tv_name.setText(appInfo.loadLabel(pm));
							ImageView iv_delete = (ImageView) view.findViewById(R.id.iv_delete);
							//需要一个android.permission.DELETE_CACHE_FILES权限，只有系统应用程序才有这个权限
							iv_delete.setOnClickListener(new OnClickListener() {
								@Override
								public void onClick(View v) {
									//deleteApplicationCacheFiles
									try {
										Method method = PackageManager.class.getMethod("deleteApplicationCacheFiles", String.class,
												IPackageDataObserver.class
												);
										method.invoke(pm, packname,new MypackDataObserver());
									} catch (Exception e) {
										e.printStackTrace();
									}
									
								}
							});
							ll_container.addView(view,0);
						}
					}
				});
			} catch (NameNotFoundException e) {
				e.printStackTrace();
			}
		}
	}
	private class MypackDataObserver extends IPackageDataObserver.Stub{
		@Override
		public void onRemoveCompleted(String packageName, boolean succeeded)
				throws RemoteException {
			System.out.println(packageName+succeeded);
		}
	}
	/**
	 * 清理手机的全部缓存.
	 * @param view
	 */
	public void clearAll(View view){
		Method[] methods = PackageManager.class.getMethods();
		for(Method method:methods){
			if("freeStorageAndNotify".equals(method.getName())){
				try {
					method.invoke(pm, Integer.MAX_VALUE,new MypackDataObserver());
				} catch (Exception e) {
					e.printStackTrace();
				}
				return;
			}
		}
	}
}
