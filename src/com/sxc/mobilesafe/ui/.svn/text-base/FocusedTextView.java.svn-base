package com.sxc.mobilesafe.ui;

import android.content.Context;
import android.util.AttributeSet;
import android.view.ViewDebug.ExportedProperty;
import android.widget.TextView;
/**
 * 自定义一个TextView,创建后就有焦点
 * @author Administrator
 *
 */
public class FocusedTextView extends TextView {

	public FocusedTextView(Context context, AttributeSet attrs, int defStyle) {
		super(context, attrs, defStyle);
		// TODO Auto-generated constructor stub
	}

	public FocusedTextView(Context context, AttributeSet attrs) {
		super(context, attrs);
		// TODO Auto-generated constructor stub
	}

	public FocusedTextView(Context context) {
		super(context);
		// TODO Auto-generated constructor stub
	}
	/**
	 * 当前并没有焦点，只是欺骗了Android系统
	 */
	@Override
	@ExportedProperty(category = "focus")
	public boolean isFocused() {
		// TODO Auto-generated method stub
		return true;
	}

}
