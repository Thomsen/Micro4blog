package com.micro4blog.http;

import java.util.ArrayList;
import java.util.List;

import android.os.Bundle;

public class Micro4blogParameters {
	
	private Bundle mBundleParameters = new Bundle();
	private List<String> listKeys = new ArrayList<String>();

	public Micro4blogParameters() {
		
	}
	
	public void add(String string, String signature) {
		if (this.listKeys.contains(string)) {
			this.mBundleParameters.putString(string, signature);
		} else {
			this.listKeys.add(string);
			this.mBundleParameters.putString(string, signature);
		}
		
	}

	public void addAll(Micro4blogParameters authParams) {
		for(int i=0; i<authParams.size(); i++) {
			this.add(authParams.getKey(i), authParams.getValue(i));
		}
		
	}
	
	public void remove(String key) {
		listKeys.remove(key);
	}
	
	public void remove(int i) {
		String key = this.listKeys.get(i);
		this.mBundleParameters.remove(key);
		listKeys.remove(key);
	}

	public int size() {

		return listKeys.size();
	}
	
	public int getLocation(String key) {

		if (this.listKeys.contains(key)) {
			return this.listKeys.indexOf(key);
		}
		
		return -1;
	}
	
	public String getValue(String key) {
		String rlt = this.mBundleParameters.getString(key);
		return rlt;
	}

	public String getValue(int location) {
		String key = this.listKeys.get(location);
		String rlt = this.mBundleParameters.getString(key);
		
		return rlt;
	}

	public String getKey(int location) {
		if (location >= 0 && location < this.listKeys.size()) {
			return this.listKeys.get(location);
		}
		return "";
	}
	
	public void clear() {
		this.listKeys.clear();
		this.mBundleParameters.clear();
	}

}
