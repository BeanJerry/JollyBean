package com.jerrybean.qqslidemenu.fragment;

import java.util.ArrayList;
import java.util.Random;

import com.jerrybean.qqslidemenu.R;
import com.jerrybean.qqslidemenu.pager.BasePager;
import com.jerrybean.qqslidemenu.pager.ChatPager;
import com.jerrybean.qqslidemenu.pager.ConstantPager;
import com.jerrybean.qqslidemenu.pager.SocialPager;
import com.jerrybean.qqslidemenu.util.Constant;
import com.jerrybean.qqslidemenu.view.MyLinearLayout;
import com.jerrybean.qqslidemenu.view.NoScrollViewPager;
import com.jerrybean.qqslidemenu.view.SlideMenu;
import com.jerrybean.qqslidemenu.view.SlideMenu.OnDragStateChangeListener;
import com.nineoldandroids.view.ViewHelper;
import com.nineoldandroids.view.ViewPropertyAnimator;

import android.graphics.Color;
import android.support.v4.view.PagerAdapter;
import android.support.v4.view.ViewPager;
import android.support.v4.view.ViewPager.OnPageChangeListener;
import android.text.GetChars;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.CycleInterpolator;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.RadioGroup.OnCheckedChangeListener;
import android.widget.TextView;

public class HomeFragment extends BaseFragment {
	private ListView main_listview;
	public  ImageView iv_head;
	public  MyLinearLayout my_layout;
	private NoScrollViewPager mViewPager;
	private ArrayList<BasePager> mBasePagers;
	private RadioGroup radiogroup;
	private RadioButton rb_chat;
	private RadioButton rb_constant;
	private RadioButton rb_social;
	public View initView() {
		View view = View.inflate(mActivity, R.layout.layout_main, null);
		mViewPager = (NoScrollViewPager) view.findViewById(R.id.vp_main);
		iv_head = (ImageView) view.findViewById(R.id.iv_head);
		my_layout = (MyLinearLayout) view.findViewById(R.id.my_layout);
		//RadioGroup的id
		radiogroup = (RadioGroup) view.findViewById(R.id.radiogroup);
		rb_chat = (RadioButton) view.findViewById(R.id.rb_chat);
		rb_constant = (RadioButton) view.findViewById(R.id.rb_constant);
		rb_social = (RadioButton) view.findViewById(R.id.rb_social);
		
		return view;
	}
	
	public void initData() {
		initPager();
		initRadio();
	}

	private void initRadio() {
		radiogroup.setOnCheckedChangeListener(new OnCheckedChangeListener() {

			@Override
			public void onCheckedChanged(RadioGroup group, int checkedId) {
				switch (checkedId) {
				case R.id.rb_chat:
					mViewPager.setCurrentItem(0,false);
					break;
				case R.id.rb_constant:
					mViewPager.setCurrentItem(1,false);
					break;
				case R.id.rb_social:
					mViewPager.setCurrentItem(2,false);
					break;

				default:
					break;
				}
			}
		});
	}

	private void initPager() {
		mBasePagers = new ArrayList<BasePager>();

		mBasePagers.add(new ChatPager(mActivity));
		mBasePagers.add(new ConstantPager(mActivity));
		mBasePagers.add(new SocialPager(mActivity));
		mViewPager.setAdapter(new MyAdapter());
		
		mViewPager.setOnPageChangeListener(new OnPageChangeListener() {
			
			@Override
			public void onPageSelected(int position) {
				//初始化数据
				mBasePagers.get(position).initData();
			}
			@Override
			public void onPageScrolled(int position, float positionOffset,
					int positionOffsetPixels) {
			}
			@Override
			public void onPageScrollStateChanged(int state) {
				
			}
		});
		mBasePagers.get(0).initData();
	}
	class MyAdapter extends PagerAdapter {

		@Override
		public int getCount() {
			return mBasePagers.size();
		}

		@Override
		public boolean isViewFromObject(View view, Object object) {
			return view == object;
		}

		@Override
		public Object instantiateItem(ViewGroup container, int position) {
			View view = mBasePagers.get(position).mBaseView;
			System.out.println(view + "-=---------------------");
			container.addView(view);
			return view;
		}

		@Override
		public void destroyItem(ViewGroup container, int position, Object object) {
			container.removeView((View) object);
		}
	}
}
