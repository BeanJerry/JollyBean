package com.jerrybean.qqslidemenu.fragment;

import com.jerrybean.qqslidemenu.R;
import com.jerrybean.qqslidemenu.util.Constant;

import android.graphics.Color;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.BaseAdapter;
import android.widget.ListView;
import android.widget.TextView;

public class MenuFragment extends BaseFragment {

	public ListView menu_listview;
	@Override
	public View initView() {
		View view = View.inflate(mActivity, R.layout.layout_menu, null);
		menu_listview = (ListView) view.findViewById(R.id.menu_listview);
		return view;
	}

	@Override
	public void initData() {
		menu_listview.setAdapter(new ArrayAdapter<String>(mActivity,
				android.R.layout.simple_list_item_1, Constant.sCheeseStrings) {
			@Override
			public View getView(int position, View convertView, ViewGroup parent) {
				TextView textView = (TextView) super.getView(position,
						convertView, parent);
				textView.setTextColor(Color.WHITE);
				textView.setTextSize(18);
				return textView;
			}
		});
	}

}
