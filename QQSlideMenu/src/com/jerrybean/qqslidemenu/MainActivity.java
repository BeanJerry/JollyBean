package com.jerrybean.qqslidemenu;

import java.util.Random;

import com.jerrybean.qqslidemenu.fragment.HomeFragment;
import com.jerrybean.qqslidemenu.fragment.MenuFragment;
import com.jerrybean.qqslidemenu.view.MyLinearLayout;
import com.jerrybean.qqslidemenu.view.SlideMenu;
import com.jerrybean.qqslidemenu.view.SlideMenu.DragState;
import com.jerrybean.qqslidemenu.view.SlideMenu.OnDragStateChangeListener;
import com.nineoldandroids.view.ViewHelper;
import com.nineoldandroids.view.ViewPropertyAnimator;

import android.app.Activity;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentActivity;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.Window;
import android.view.animation.CycleInterpolator;
import android.widget.ImageView;
import android.widget.ListView;



public class MainActivity extends FragmentActivity {

	
	
	private SlideMenu slideMenu;
	private MenuFragment mMenuFragment;
	private HomeFragment mHomeFragment;
	private ImageView iv_head;
	private MyLinearLayout my_layout;
	private ListView menu_listview;
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		requestWindowFeature(Window.FEATURE_NO_TITLE);
		setContentView(R.layout.activity_main);
		slideMenu = (SlideMenu) findViewById(R.id.slidemenu);
		initFragment();
	}
	@Override
	protected void onStart() {
		super.onStart();
		MenuFragment menuFragment = getMenuFragment();
		menu_listview = menuFragment.menu_listview;
		HomeFragment homeFragment = getHomeFragment();
		iv_head = homeFragment.iv_head;
		my_layout = homeFragment.my_layout;
		initSlideMenu();
	}
	private void initSlideMenu() {
		slideMenu.setOnDragStateChangeListener(new OnDragStateChangeListener() {
			@Override
			public void onOpen() {
				System.out.println("打开了");
				// 打开侧边栏的时候条目随机滚动
				menu_listview.smoothScrollToPosition(new Random()
						.nextInt(menu_listview.getCount()));
			}
			@Override
			public void onDraging(float fraction) {
				ViewHelper.setAlpha(iv_head, 1 - fraction);
			}
			@Override
			public void onClose() {
				System.out.println("关闭了");
				ViewPropertyAnimator.animate(iv_head).translationXBy(15)
						.setInterpolator(new CycleInterpolator(4))
						.setDuration(300).start();
			}
		});

		my_layout.setSlideMenu(slideMenu);
		//给头部标签添加点击事件
		iv_head.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View v) {
				 if(slideMenu!=null&&slideMenu.getCurrentState() == DragState.ISCLOSE){
					slideMenu.open();
				}
			}
		});
	}
	private void initFragment() {
		 FragmentManager fm = getSupportFragmentManager();
		//开启事物
		 FragmentTransaction transaction = fm.beginTransaction();
		 mHomeFragment = new HomeFragment();
		 mMenuFragment = new MenuFragment();
		 transaction.replace(R.id.fl_main,mHomeFragment,"TAG_MAIN");
		 transaction.replace(R.id.fl_menu,mMenuFragment,"TAG_MENU");
		 transaction.commit();
	}
	/**
	 * @return 侧边栏的fragment
	 */
	public MenuFragment getMenuFragment() {
		FragmentManager fm = getSupportFragmentManager();
		MenuFragment fragment  = (MenuFragment) fm.findFragmentByTag("TAG_MENU");
		return fragment;
	}
	/**
	 * @return 主界面的fragment
	 */
	public HomeFragment getHomeFragment() {
		FragmentManager fm = getSupportFragmentManager();
		HomeFragment fragment  = (HomeFragment) fm.findFragmentByTag("TAG_MAIN");
		return fragment;
	}
}
