package com.riotapps.wordbase.utils;

import com.riotapps.wordbase.BuildConfig;
 
import android.util.Log;

public class Logger {
	
	public static final boolean LOG_OK = true; 
	public static final boolean LOG_GAME_SURFACE_VIEW_OK = true; 
	public static final boolean LOG_TIMER_CAPTURE_ONLY = false; 

	public static void w(String tag, String msg){
		if (BuildConfig.DEBUG && LOG_OK) {
			Log.w((tag==null?"UNKNOWN_TAG":tag), (msg==null?"unknown message":msg), null);
		}
	}
	
	public static void w(String tag, String msg, Exception e){  
		//is logging on?
		 if (BuildConfig.DEBUG && LOG_OK) {    
			Log.w((tag==null?"UNKNOWN_TAG":tag), (msg==null?"unknown message":msg), (e==null?null:e));
		 }
	}
	
	public static void d(String tag, String msg){
		 if (BuildConfig.DEBUG && LOG_OK) {
			 Logger.d((tag==null?"UNKNOWN_TAG":tag), msg, null);
		 }
	}
	
	
	public static void d(String tag, String msg, Exception e){
		//is logging on?
		 if (BuildConfig.DEBUG && LOG_OK) {
			//if (
			//		(!LOG_GAME_SURFACE_VIEW_OK && tag.equals(GameSurfaceView.class.getSimpleName())) || 
			//		!tag.equals(GameSurfaceView.class.getSimpleName())
			//   ){
			if (LOG_TIMER_CAPTURE_ONLY){
				if (msg.indexOf("time since last capture") > -1){
					Log.d((tag==null?"UNKNOWN_TAG":tag), (msg==null?"unknown message":msg), (e==null?null:e));			
				}
			}
			else{
				Log.d((tag==null?"UNKNOWN_TAG":tag), (msg==null?"unknown message":msg), (e==null?null:e));			
			}
		 }
		//}
	}
	
	public static void longInfo(String tag, String str) {
	    if(str.length() > 1000) {
	        Log.d(tag, str.substring(0, 1000));
	        longInfo(tag, str.substring(1000));
	    } else
	        Log.d(tag, str);
	}

	public static void e(String tag, String msg){
		Logger.e((tag==null?"UNKNOWN_TAG":tag), (msg==null?"unknown message":msg), null);
		
	}
	
	public static void e(String tag, String msg, Exception e){
		////is logging on?
		if (BuildConfig.DEBUG && LOG_OK) {
			Log.e((tag==null?"UNKNOWN_TAG":tag), (msg==null?"unknown message":msg), (e==null?null:e));
		 }
	}
	
	public static void v(String tag, String msg){
		//is logging on?
		if (BuildConfig.DEBUG && LOG_OK) {
			Log.v((tag==null?"UNKNOWN_TAG":tag), (msg==null?"unknown message":msg));
		 }
	}
	
	public static void i(String tag, String msg){
		//is logging on?
		if (BuildConfig.DEBUG && LOG_OK) {
			Log.i((tag==null?"UNKNOWN_TAG":tag), (msg==null?"unknown message":msg));
		}
	}
}
