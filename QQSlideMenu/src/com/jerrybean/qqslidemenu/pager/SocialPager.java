package com.jerrybean.qqslidemenu.pager;


import android.annotation.SuppressLint;
import android.app.Activity;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.ListView;

import com.jerrybean.qqslidemenu.R;
import com.jerrybean.qqslidemenu.view.ParallaxListView;

public class SocialPager extends BasePager {
	private ParallaxListView listview;
	private String[] indexArr = { "A", "B", "C", "D", "E", "F", "G", "H",
			"I", "J", "K", "L", "M", "N", "O", "P", "Q", "R", "S", "T", "U",
			"V", "W", "X", "Y", "Z" };
	public SocialPager(Activity activity) {
		super(activity);
	}

	@SuppressLint("NewApi") @Override
	public View initView() {
		View view = View.inflate(mActivity, R.layout.pager_social, null);
		listview = (ParallaxListView)view.findViewById(R.id.listview);
		
		listview.setOverScrollMode(ListView.OVER_SCROLL_NEVER);//永远不显示蓝色阴影
		
		//添加header
		View headerView = View.inflate(mActivity,R.layout.layout_header, null);
		ImageView imageView = (ImageView) headerView.findViewById(R.id.imageView);
		listview.setParallaxImageView(imageView);
		
		listview.addHeaderView(headerView);
		
		return view;
	}
	
	@Override
	public void initData() {
		listview.setAdapter(new ArrayAdapter<String>(mActivity,android.R.layout.simple_list_item_1, indexArr));
	}
}
