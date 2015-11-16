package com.jerrybean.qqslidemenu.pager;

import android.app.Activity;
import android.view.View;

public abstract class BasePager {
	

	public View mBaseView;
	public Activity mActivity;

	public BasePager(Activity activity) {
		mActivity = activity;
		mBaseView = initView();
	}

	public abstract View initView();
	
	public void initData(){
		
	}
}