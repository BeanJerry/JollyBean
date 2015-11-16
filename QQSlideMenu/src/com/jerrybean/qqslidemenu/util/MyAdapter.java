package com.jerrybean.qqslidemenu.util;

import java.util.ArrayList;

import android.content.Context;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import com.jerrybean.qqslidemenu.R;
import com.jerrybean.qqslidemenu.domain.Friend;

public class MyAdapter extends BaseAdapter {

	private ArrayList<Friend> mList;
	private Context context;
	public MyAdapter(Context context, ArrayList<Friend> friends) {
		this.context = context;
		mList = friends;
	}
	
	@Override
	public int getCount() {
		return mList.size();
	}

	@Override
	public Friend getItem(int position) {
		return mList.get(position);
	}

	@Override
	public long getItemId(int position) {
		return position;
	}

	@Override
	public View getView(int position, View convertView, ViewGroup parent) {
		
		if(convertView == null){
			convertView = View.inflate(context, R.layout.list_friend_item, null);
		}

		ViewHolder holder = ViewHolder.getViewHolder(convertView);
		Friend friend = getItem(position);
		holder.name.setText(friend.getName());
		
		String firstName = friend.getPinYin().charAt(0)+"";
		if(position>0){
			//拿到上一个名字的首字母
			String preFirstName = getItem(position-1).getPinYin().charAt(0)+"";
			holder.first_word.setTag(position);
			//如果首字母相同，则隐藏掉当前item的first_word
			if(preFirstName.equals(firstName)){
				holder.first_word.setVisibility(View.GONE);
			}else{
				holder.first_word.setVisibility(View.VISIBLE);
				holder.first_word.setText(firstName);
			}
		}else{
			holder.first_word.setVisibility(View.VISIBLE);
			holder.first_word.setText(firstName);
		}
		return convertView;
	}
	//ViewHolder的封装
	static class ViewHolder{
		TextView first_word,name;
		public ViewHolder(View convertView) {
			first_word = (TextView) convertView.findViewById(R.id.first_word);
			name = (TextView) convertView.findViewById(R.id.name);
			
		}
		public static ViewHolder getViewHolder(View convertView){
			ViewHolder holder = (ViewHolder) convertView.getTag();
			if(holder==null){
				holder = new ViewHolder(convertView);
				convertView.setTag(holder);
			}
			return holder;
		}
	}
}
