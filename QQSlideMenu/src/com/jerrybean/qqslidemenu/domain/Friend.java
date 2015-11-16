package com.jerrybean.qqslidemenu.domain;

import com.jerrybean.qqslidemenu.util.PinYinUtil;


public class Friend implements Comparable<Friend>{

	private String name;
	private String pinyin;

	public Friend(String name) {
		this.name = name;
		
		setPinYin(PinYinUtil.getPinyin(name));
	}
	public void setPinYin(String pinyin) {
		this.pinyin = pinyin;
	}
	
	public String getPinYin(){
		return pinyin;
	}
	public String getName() {
		return name;
	}
	public void setName(String name) {
		this.name = name;
	}
	
	@Override
	public int compareTo(Friend another) {
		return getPinYin().compareTo(another.getPinYin());
	}

	
	
}
