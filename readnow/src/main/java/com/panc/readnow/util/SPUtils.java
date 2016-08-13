package com.panc.readnow.util;

import android.content.Context;
import android.content.SharedPreferences;

public class SPUtils {
	
	
	
	private static final String SPNAME = "config";
	public static void saveBoolean(Context context,String key,boolean value){
		SharedPreferences sharedPreferences = context.getSharedPreferences(SPNAME,Context.MODE_PRIVATE);
		sharedPreferences.edit().putBoolean(key, value).commit();
	}
	public static void saveString(Context context,String key,String value){
		SharedPreferences sharedPreferences = context.getSharedPreferences(SPNAME,Context.MODE_PRIVATE);
		sharedPreferences.edit().putString(key, value).commit();
	}
	public static boolean getBoolean(Context context,String key,boolean defValue){
		SharedPreferences sharedPreferences = context.getSharedPreferences(SPNAME,Context.MODE_PRIVATE);
		return sharedPreferences.getBoolean(key, defValue);
	}
	public static String getString(Context context,String key){
		SharedPreferences sharedPreferences = context.getSharedPreferences(SPNAME,Context.MODE_PRIVATE);
		return sharedPreferences.getString(key, null);
	}
}
