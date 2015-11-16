package com.jerrybean.qqslidemenu.view;

import com.jerrybean.qqslidemenu.view.SlideMenu.DragState;

import android.content.Context;
import android.os.AsyncTask;
import android.util.AttributeSet;
import android.view.MotionEvent;
import android.widget.LinearLayout;

public class MyLinearLayout extends LinearLayout {

	private SlideMenu slideMenu;
	private int height;

	public MyLinearLayout(Context context) {
		super(context);
	}

	public MyLinearLayout(Context context, AttributeSet attrs) {
		super(context, attrs);
	}

	public MyLinearLayout(Context context, AttributeSet attrs, int defStyle) {
		super(context, attrs, defStyle);
	}
	public void setSlideMenu(SlideMenu slideMenu) {
		this.slideMenu = slideMenu;
	}

	// 打开侧边栏的时候拦截主界面的触摸事件
	@Override
	public boolean onInterceptTouchEvent(MotionEvent ev) {
		if (slideMenu != null) {
			if (slideMenu.getCurrentState() == DragState.ISOPEN) {
				return true;
			}
		}

		return super.onInterceptTouchEvent(ev);
	}

	@Override
	public boolean onTouchEvent(MotionEvent event) {
		if (slideMenu != null
				&& slideMenu.getCurrentState() == DragState.ISOPEN) {
			if (event.getAction() == MotionEvent.ACTION_UP) {
				// 抬起手指的时候，收回侧边栏
				slideMenu.close();
			}
			return true;
		}
		return super.onTouchEvent(event);
	}
	@Override
	protected void onFinishInflate() {
		super.onFinishInflate();
		height = getMeasuredHeight();
	}


}
