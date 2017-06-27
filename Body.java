package com.lab;

public class Body extends Segment {
	public Body(String type, String name){
		super(type,name);
	}
	
	@Override
	public Body addAttribute(String key, String value) throws Exception {
		super.addAttribute(key, value);
		return this;
	}

}
