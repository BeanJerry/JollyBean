package com.jerrybean.qqslidemenu;

import java.util.Random;

import android.app.Activity;
import android.graphics.Color;
import android.os.Bundle;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.view.animation.CycleInterpolator;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.TextView;

import com.jerrybean.qqslidemenu.util.Constant;
import com.jerrybean.qqslidemenu.view.MyLinearLayout;
import com.jerrybean.qqslidemenu.view.SlideMenu;
import com.jerrybean.qqslidemenu.view.SlideMenu.OnDragStateChangeListener;
import com.nineoldandroids.view.ViewHelper;
import com.nineoldandroids.view.ViewPropertyAnimator;

public class MainActivity extends Activity {

	private ListView main_listview;
	private ListView menu_listview;
	private ImageView iv_head;
	private MyLinearLayout my_layout;
	private SlideMenu slideMenu;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		requestWindowFeature(Window.FEATURE_NO_TITLE);
		setContentView(R.layout.activity_main);
		initView();

		initData();
	}

	private void initView() {
		
		main_listview = (ListView) findViewById(R.id.main_listview);
		menu_listview = (ListView) findViewById(R.id.menu_listview);
		iv_head = (ImageView) findViewById(R.id.iv_head);
		my_layout = (MyLinearLayout) findViewById(R.id.my_layout);
		slideMenu = (SlideMenu) findViewById(R.id.slidemenu);
	}

	private void initData() {
		main_listview.setAdapter(new ArrayAdapter<String>(this,
				android.R.layout.simple_list_item_1, Constant.NAMES) {
			@Override
			public View getView(int position, View convertView,
					ViewGroup parent) {
				View view = convertView==null?super.getView(position, convertView, parent):convertView;
				//先缩小view
				ViewHelper.setScaleX(view, 0.5f);
				ViewHelper.setScaleY(view, 0.5f);
				//以属性动画放大
				ViewPropertyAnimator.animate(view).scaleX(1).setDuration(350).start();
				ViewPropertyAnimator.animate(view).scaleY(1).setDuration(350).start();
				return view;
			}
		});

		menu_listview.setAdapter(new ArrayAdapter<String>(this,
				android.R.layout.simple_list_item_1, Constant.sCheeseStrings){
			@Override
			public View getView(int position, View convertView,
					ViewGroup parent) {
				TextView textView = (TextView) super.getView(position, convertView, parent);
				textView.setTextColor(Color.WHITE);
				textView.setTextSize(getResources().getDimension(R.dimen.textSize));
				return textView;
			}
		});
		
		slideMenu.setOnDragStateChangeListener(new OnDragStateChangeListener() {
			@Override
			public void onOpen() {
				System.out.println("打开了");
				//打开侧边栏的时候条目随机滚动
				menu_listview.smoothScrollToPosition(new Random().nextInt(menu_listview.getCount()));
			}
			
			@Override
			public void onDraging(float fraction) {
				ViewHelper.setAlpha(iv_head,1-fraction);
			}
			
			@Override
			public void onClose() {
				System.out.println("关闭了");
				ViewPropertyAnimator.animate(iv_head)
				.translationXBy(15)
				.setInterpolator(new CycleInterpolator(4))
				.setDuration(300)
				.start();
			}
		});
		
		my_layout.setSlideMenu(slideMenu);
	}

}
