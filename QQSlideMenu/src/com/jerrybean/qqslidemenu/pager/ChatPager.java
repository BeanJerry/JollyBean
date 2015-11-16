package com.jerrybean.qqslidemenu.pager;

import android.app.Activity;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.OvershootInterpolator;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;

import com.jerrybean.qqslidemenu.R;
import com.jerrybean.qqslidemenu.util.Constant;
import com.jerrybean.qqslidemenu.view.ChatListView;
import com.nineoldandroids.view.ViewHelper;
import com.nineoldandroids.view.ViewPropertyAnimator;

public class ChatPager extends BasePager {

	private ChatListView mListView;
	public ChatPager(Activity activity) {
		super(activity);
	}
	@Override
	public View initView() {
		View view = View.inflate(mActivity, R.layout.pager_chat, null);
		mListView = (ChatListView) view.findViewById(R.id.chat_listview);
//		mListView.setOverScrollMode(ListView.OVER_SCROLL_NEVER);//永远不显示蓝色阴影
		return view;
	}
	@Override
	public void initData() {
		View view = View.inflate(mActivity, R.layout.layout_header, null);
		mListView.setAdapter(new MyAdapter());
	
	}
	class MyAdapter extends BaseAdapter{
		@Override
		public int getCount() {
			return Constant.NAMES.length;
		}
		@Override
		public Object getItem(int position) {
			return Constant.NAMES[position];
		}
		@Override
		public long getItemId(int position) {
			return position;
		}
		@Override
		public View getView(int position, View convertView, ViewGroup parent) {
			if(convertView == null){
				convertView = View.inflate(mActivity, R.layout.list_chat_item,null);
			}
			ViewHolder holder = ViewHolder.getHolder(convertView);
			
			holder.tv_chat.setText(Constant.NAMES[position]);
			if(position%3==0){
				holder.iv_chat.setImageResource(R.drawable.head_1);
			}else if(position%3==1){
				holder.iv_chat.setImageResource(R.drawable.head_2);
			}else if(position%3==2){
				holder.iv_chat.setImageResource(R.drawable.head_3);
			}
			ViewHelper.setScaleX(holder.tv_chat, 0.5f);
			ViewHelper.setScaleY(holder.tv_chat, 0.5f);
			
			ViewPropertyAnimator.animate(holder.tv_chat)
			.scaleX(1f)
			.setInterpolator(new OvershootInterpolator())
			.setDuration(350)
			.start();
			ViewPropertyAnimator.animate(holder.tv_chat)
			.scaleY(1f)
			.setInterpolator(new OvershootInterpolator())
			.setDuration(350)
			.start();
			return convertView;
		}
	}
	static class ViewHolder{
		TextView tv_chat;
		ImageView iv_chat;
		public ViewHolder(View convertView) {
			tv_chat = (TextView) convertView.findViewById(R.id.tv_chat);
			iv_chat = (ImageView) convertView.findViewById(R.id.iv_chat);
		}
		public static ViewHolder getHolder(View convertView){
			ViewHolder holder = (ViewHolder) convertView.getTag();
			if(holder==null){
				holder = new ViewHolder(convertView);
				convertView.setTag(holder);
			}
			return holder;
		}
	}
}
