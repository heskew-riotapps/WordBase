package com.riotapps.wordbase.hooks;

import com.google.gson.annotations.SerializedName;

public class Word {

	@SerializedName("W")
	private String W;
	
	public String getWord() {
		return W;
	}
	public void setWord(String word) {
		this.W = word;
	}
}
