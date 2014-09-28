package com.sxc.mobilesafe;

import java.security.acl.LastOwnerException;
import java.util.List;

import com.sxc.mobilesafe.db.dao.BlackNumberDao;
import com.sxc.mobilesafe.domain.BlackNumberInfo;

import android.app.Activity;
import android.app.AlertDialog;
import android.app.AlertDialog.Builder;
import android.content.DialogInterface;
import android.os.Bundle;
import android.text.AlteredCharSequence;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.AbsListView;
import android.widget.AbsListView.OnScrollListener;
import android.widget.BaseAdapter;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

public class CallSmsSafeActivity extends Activity {

	public static final String TAG = "CallSmsSafeActivity";
	private ListView lv_callsms_safe;
	private List<BlackNumberInfo> infos;
	private BlackNumberDao dao;
	private CallSmsSafeAdapter adapter;
	private LinearLayout ll_loading;
	private int offset = 0;
	private int maxnumber = 20;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_call_sms_safe);
		lv_callsms_safe = (ListView) findViewById(R.id.lv_callsms_safe);
		ll_loading = (LinearLayout) findViewById(R.id.ll_loading);
		dao = new BlackNumberDao(this);
		fillData();
		// listview注册一个滚动事件的监听器
		lv_callsms_safe.setOnScrollListener(new OnScrollListener() {

			// 当滚动状态发生变化时
			@Override
			public void onScrollStateChanged(AbsListView view, int scrollState) {
				// TODO Auto-generated method stub
				switch (scrollState) {
				case OnScrollListener.SCROLL_STATE_IDLE:// 空闲状态
//					System.out.println("空闲状态");
					//判断当前listview滚动的位置
					//获取最后一个可见条目在集合里面的位置是19
					int lastPosition=lv_callsms_safe.getLastVisiblePosition();
					System.out.println(lastPosition);
					//集合中有20个item， 位置从0 开始最后一个条目的位置
					if(lastPosition==(infos.size()-1))
					{
						System.out.println("列表被移动到了最后一个位置，加载更多的数据。。");
						offset+=maxnumber;
						fillData();
					}
					break;
				case OnScrollListener.SCROLL_STATE_TOUCH_SCROLL:// 手指触摸滚动
//					System.out.println("手指触摸滚动");
					break;
				case OnScrollListener.SCROLL_STATE_FLING:// 惯性滑行状态
//					System.out.println("惯性滑行状态");
					break;

				default:
					break;
				}
			}

			// 当滚动时
			@Override
			public void onScroll(AbsListView view, int firstVisibleItem,
					int visibleItemCount, int totalItemCount) {
				// TODO Auto-generated method stub

			}
		});
	}

	private void fillData() {
		ll_loading.setVisibility(View.VISIBLE);
		new Thread() {
			public void run() {

				// infos=dao.findAll();
				if(infos==null){
				infos = dao.findPart(offset, maxnumber);
				}else{
					infos.addAll(dao.findPart(offset, maxnumber));
				}
				runOnUiThread(new Runnable() {

					@Override
					public void run() {
						// TODO Auto-generated method stub
						ll_loading.setVisibility(View.INVISIBLE);
						if(adapter==null)
						{
						adapter = new CallSmsSafeAdapter();
						lv_callsms_safe.setAdapter(adapter);
						}else
						{
							adapter.notifyDataSetChanged();
						}
					}
				});
			};
		}.start();
	}

	private class CallSmsSafeAdapter extends BaseAdapter {

		@Override
		public int getCount() {
			// TODO Auto-generated method stub
			return infos.size();
		}

		// 有多少个条目被显示，该方法就被调用多少次
		@Override
		public View getView(final int position, View convertView,
				ViewGroup parent) {
			// TODO Auto-generated method stub
			// Log.i(TAG, "position:"+position+"convertVIew"+convertView);
			View view;
			ViewHolder holder;
			// 优化1. 2.
			// 1.减少内存中创建view的次数
			if (convertView == null) {
				Log.i(TAG, "创建新的view对象：" + position);
				// 把布局文件转化成View对象
				view = View.inflate(getApplicationContext(),
						R.layout.list_item_callsms, null);

				// 减少查找子孩子的个数// findview找到的是
				holder = new ViewHolder();
				holder.tv_number = (TextView) view
						.findViewById(R.id.tv_black_number);
				holder.tv_mode = (TextView) view
						.findViewById(R.id.tv_black_mode);
				holder.iv_delete = (ImageView) view
						.findViewById(R.id.iv_delete);
				// 当view创建出来的时候找到他们的引用，存放在记事本，挡在父类的口袋里
				view.setTag(holder);
			} else {
				Log.i(TAG, "后台有历史的view对象，服用历史缓存的view对象" + position);
				view = convertView;
				// 使用已经记录的引用信息,性能提高5%
				holder = (ViewHolder) view.getTag();
			}
			holder.tv_number.setText(infos.get(position).getNumber());
			String mode = infos.get(position).getMode();
			if ("1".equals(mode)) {
				holder.tv_mode.setText("电话拦截");
			} else if ("2".equals(mode)) {
				holder.tv_mode.setText("短信拦截");
			} else {
				holder.tv_mode.setText("全部拦截");
			}
			holder.iv_delete.setOnClickListener(new OnClickListener() {

				@Override
				public void onClick(View v) {
					// TODO Auto-generated method stub
					// System.out.println("删除："+position);
					AlertDialog.Builder builder = new Builder(
							CallSmsSafeActivity.this);
					builder.setTitle("警告");
					builder.setMessage("确定要删除这条记录吗？");
					builder.setPositiveButton("确定",
							new DialogInterface.OnClickListener() {

								@Override
								public void onClick(DialogInterface dialog,
										int which) {
									// TODO Auto-generated method stub
									// 删除数据库的内容
									dao.delete(infos.get(position).getNumber());
									// 更新界面
									infos.remove(position);
									// 通知listView数据适配器更新
									adapter.notifyDataSetInvalidated();

								}
							});
					builder.setNegativeButton("取消", null);
					builder.show();
				}
			});
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

	private EditText et_blacknumber;
	private CheckBox cb_phone;
	private CheckBox cb_sms;
	private Button bt_ok;
	private Button bt_cancel;

	public void addBlackNumber(View view) {
		AlertDialog.Builder builder = new Builder(this);
		final AlertDialog dialog = builder.create();
		View contentView = View.inflate(this, R.layout.dialog_add_blacknumber,
				null);
		et_blacknumber = (EditText) contentView
				.findViewById(R.id.et_blacknumber);
		cb_phone = (CheckBox) contentView.findViewById(R.id.cb_phone);
		cb_sms = (CheckBox) contentView.findViewById(R.id.cb_sms);
		bt_ok = (Button) contentView.findViewById(R.id.ok);
		bt_cancel = (Button) contentView.findViewById(R.id.cancel);
		dialog.setView(contentView, 0, 0, 0, 0);
		dialog.show();
		bt_cancel.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
				dialog.dismiss();
			}
		});
		bt_ok.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
				String blacknumber = et_blacknumber.getText().toString().trim();
				if (TextUtils.isEmpty(blacknumber)) {
					Toast.makeText(getApplicationContext(), "黑名单不能为空", 0)
							.show();
					return;
				}
				String mode;
				if (cb_phone.isChecked() && cb_sms.isChecked()) {
					// 全部拦截
					mode = "3";
				} else if (cb_phone.isChecked()) {
					// 电话拦截
					mode = "1";
				} else if (cb_sms.isChecked()) {
					// 短信拦截
					mode = "2";
				} else {
					Toast.makeText(getApplicationContext(), "请选择拦截模式", 0)
							.show();
					return;
				}
				// 数据被加载到数据库
				dao.add(blacknumber, mode);
				// 更新ListView里面的内容
				BlackNumberInfo info = new BlackNumberInfo();
				info.setMode(mode);
				info.setNumber(blacknumber);
				infos.add(0, info);
				// 通知listview数据适配器数据更新了。
				adapter.notifyDataSetChanged();
				dialog.dismiss();
			}
		});
	}

	/**
	 * view对象的容器 记录孩子的内存地址 相当于一个记事本
	 * 
	 * @author master
	 * 
	 */
	static class ViewHolder {
		TextView tv_number;
		TextView tv_mode;
		ImageView iv_delete;
	}
}
