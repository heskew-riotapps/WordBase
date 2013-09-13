package com.riotapps.wordbase.utils;

import java.lang.reflect.Type;

public class IntentExtra {
	private String name;
	private Object value;
	private Type type;
	
	public IntentExtra (String name, Object value, Type type){
		this.name = name;
		this.value = value;
		this.type = type;
	}
	
	public String getName() {
		return name;
	}
	public void setName(String name) {
		this.name = name;
	}
	public Object getValue() {
		return value;
	}
	public void setValue(Object value) {
		this.value = value;
	}
	public Type getType() {
		return type;
	}
	public void setType(Type type) {
		this.type = type;
	}
	
	
}
