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
	
	public void sort() {
		
		for (int i=0; i<size()-1; i++) {
			for (int j=i+1; j<size(); j++) {
				if (getKey(i).compareToIgnoreCase(getKey(j)) > 0) {
					swap(i, j, getKey(i), getKey(j));
				}
			}
		}
		
	}
	
	public void swap(int a, int b, String source, String dest) {

		remove(source);
		remove(dest);
		
		listKeys.add(a, dest);
		listKeys.add(b, source);

//		String tmpValue = getValue(source);
//		mBundleParameters.putString(source, getValue(dest));
//		mBundleParameters.putString(dest, getValue(tmpValue));
//		add(source, getValue(dest));
//		add(dest, tmpValue);
	}
	
	public void clear() {
		this.listKeys.clear();
		this.mBundleParameters.clear();
	}

}
