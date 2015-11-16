package com.jerrybean.qqslidemenu.pager;

import java.util.ArrayList;
import java.util.Collections;

import android.app.Activity;
import android.os.Handler;
import android.view.View;
import android.view.animation.OvershootInterpolator;
import android.widget.ListView;
import android.widget.TextView;

import com.jerrybean.qqslidemenu.R;
import com.jerrybean.qqslidemenu.domain.Friend;
import com.jerrybean.qqslidemenu.util.MyAdapter;
import com.jerrybean.qqslidemenu.view.QuickIndexBar;
import com.jerrybean.qqslidemenu.view.QuickIndexBar.onTouchLetterListener;
import com.nineoldandroids.view.ViewHelper;
import com.nineoldandroids.view.ViewPropertyAnimator;

public class ConstantPager extends BasePager {
	  	private QuickIndexBar quickIndex;
		private TextView currentWord;
		private ListView listView;
		private ArrayList<Friend> friends = new ArrayList<Friend>();
	public ConstantPager(Activity activity) {
		super(activity);
	}
	@Override
	public View initView() {
		View view = View.inflate(mActivity, R.layout.pager_constant, null);
		quickIndex = (QuickIndexBar) view.findViewById(R.id.quickindex);
        currentWord = (TextView) view.findViewById(R.id.textview);
        listView = (ListView) view.findViewById(R.id.listView);
        listView.setOverScrollMode(ListView.OVER_SCROLL_NEVER);//永远不显示蓝色阴影
        return view;
	}
	@Override
	public void initData() {
		if(friends.size()==0){
			//添加数据
			fillList();
		}
        //对数据进行排序
        Collections.sort(friends);
		listView.setAdapter(new MyAdapter(mActivity,friends));
		
		 quickIndex.setOnTouchLetterListener(new onTouchLetterListener() {
	        	@Override
				public void onTouchLetter(String quickIndex) {
					//System.out.println("quickIndex letter"+quickIndex);
					for(int i = 0;i<friends.size();i++){
						String firstword = friends.get(i).getPinYin().charAt(0)+"";
						 if(firstword.equals(quickIndex)){
						
							 listView.setSelection(i);
							
							 break;
						 }
					}
					showCurrentWord(quickIndex);
	        	}
			});
	        ViewHelper.setScaleX(currentWord, 0);
			ViewHelper.setScaleY(currentWord, 0);
	    }
		private boolean isScale = false;
		private Handler mHandler = new Handler();
		protected void showCurrentWord(String quickIndex) {
			 currentWord.setText(quickIndex);
			 if(!isScale){
				 isScale = true;
				 ViewPropertyAnimator.animate(currentWord)
				 .scaleX(1f).setInterpolator(new OvershootInterpolator(5))
				 .setDuration(350).start();
				 ViewPropertyAnimator.animate(currentWord)
				 .scaleY(1f).setInterpolator(new OvershootInterpolator(5))
				 .setDuration(350).start();
			 }
			
			 
			 mHandler.removeCallbacksAndMessages(null);
			 mHandler.postDelayed(new Runnable() {
				 @Override
				public void run() {
					ViewPropertyAnimator.animate(currentWord)
					.scaleX(0).setInterpolator(new OvershootInterpolator(5))
					.setDuration(450).start();
					ViewPropertyAnimator.animate(currentWord)
					.scaleY(0).setInterpolator(new OvershootInterpolator(5))
					.setDuration(450).start();
					isScale = false;
				 }
			}, 1000);
	}
	private void fillList() {
		// 虚拟数据
		friends.add(new Friend("李伟"));
		friends.add(new Friend("张三"));
		friends.add(new Friend("阿三"));
		friends.add(new Friend("阿四"));
		friends.add(new Friend("段誉"));
		friends.add(new Friend("段正淳"));
		friends.add(new Friend("张三丰"));
		friends.add(new Friend("陈坤"));
		friends.add(new Friend("林俊杰1"));
		friends.add(new Friend("陈坤2"));
		friends.add(new Friend("王二a"));
		friends.add(new Friend("林俊杰a"));
		friends.add(new Friend("张四"));
		friends.add(new Friend("林俊杰"));
		friends.add(new Friend("王二"));
		friends.add(new Friend("王二b"));
		friends.add(new Friend("赵四"));
		friends.add(new Friend("杨坤"));
		friends.add(new Friend("赵子龙"));
		friends.add(new Friend("杨坤1"));
		friends.add(new Friend("李伟1"));
		friends.add(new Friend("宋江"));
		friends.add(new Friend("宋江1"));
		friends.add(new Friend("李伟3"));
	}
}
