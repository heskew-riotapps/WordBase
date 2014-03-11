package com.riotapps.wordbase.utils;
/*
 * Copyright (C) 2012 The Android Open Source Project
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *      http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

//import android.annotation.SuppressLint;
import java.io.File;
import java.io.UnsupportedEncodingException;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.util.Arrays;
import java.util.Date;
import java.util.Random;

import android.app.ActivityManager;
import android.content.Context;
import android.content.pm.ApplicationInfo;
import android.content.pm.PackageManager;
import android.content.pm.PackageManager.NameNotFoundException;
import android.graphics.Bitmap;
import android.os.Environment;
import android.os.StatFs;
import android.util.TypedValue;

import com.riotapps.wordbase.R;

/**
 * Class containing some static utility methods.
 */
public class Utils {
    public static final int IO_BUFFER_SIZE = 8 * 1024;
    private static final String TAG = Utils.class.getSimpleName();
    private Utils() {};

    /**
     * Workaround for bug pre-Froyo, see here for more info:
     * http://android-developers.blogspot.com/2011/09/androids-http-clients.html
     */
    public static void disableConnectionReuseIfNecessary() {
        // HTTP connection reuse which was buggy pre-froyo
        if (hasHttpConnectionBug()) {
            System.setProperty("http.keepAlive", "false");
        }
    }

    /**
     * Get the size in bytes of a bitmap.
     * @param bitmap
     * @return size in bytes
     */
   // @SuppressLint("NewApi")
    public static int getBitmapSize(Bitmap bitmap) {
      //  if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.HONEYCOMB_MR1) {
      //      return bitmap.getByteCount();
      //  }
        // Pre HC-MR1
        return bitmap.getRowBytes() * bitmap.getHeight();
    }

    /**
     * Check if external storage is built-in or removable.
     *
     * @return True if external storage is removable (like an SD card), false
     *         otherwise.
     */
 //   @SuppressLint("NewApi")
    public static boolean isExternalStorageRemovable() {
  //      if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.GINGERBREAD) {
  //          return Environment.isExternalStorageRemovable();
  //      }
        return true;
    }

    /**
     * Get the external app cache directory.
     *
     * @param context The context to use
     * @return The external cache dir
     */
  //  @SuppressLint("NewApi")
    public static File getExternalCacheDir(Context context) {
       // if (hasExternalCacheDir()) {
       //     return context.getExternalCacheDir();
       // }

        // Before Froyo we need to construct the external cache dir ourselves
        final String cacheDir = "/Android/data/" + context.getPackageName() + "/cache/";
        return new File(Environment.getExternalStorageDirectory().getPath() + cacheDir);
    }

    /**
     * Check how much usable space is available at a given path.
     *
     * @param path The path to check
     * @return The space available in bytes
     */
  //  @SuppressLint("NewApi")
    public static long getUsableSpace(File path) {
      //  if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.GINGERBREAD) {
      //      return path.getUsableSpace();
      //  }
        final StatFs stats = new StatFs(path.getPath());
        return (long) stats.getBlockSize() * (long) stats.getAvailableBlocks();
    }

    /**
     * Get the memory class of this device (approx. per-app memory limit)
     *
     * @param context
     * @return
     */
    public static int getMemoryClass(Context context) {
        return ((ActivityManager) context.getSystemService(
                Context.ACTIVITY_SERVICE)).getMemoryClass();
    }

    /**
     * Check if OS version has a http URLConnection bug. See here for more information:
     * http://android-developers.blogspot.com/2011/09/androids-http-clients.html
     *
     * @return
     */
    public static boolean hasHttpConnectionBug() {
    	return true;
      //  return Build.VERSION.SDK_INT < Build.VERSION_CODES.FROYO;
    }

    /**
     * Check if OS version has built-in external cache dir method.
     *
     * @return
     */
    public static boolean hasExternalCacheDir() {
    	return false;
       // return Build.VERSION.SDK_INT >= Build.VERSION_CODES.FROYO;
    }

    /**
     * Check if ActionBar is available.
     *
     * @return
     */
    public static boolean hasActionBar() {
    	return false;
        //return Build.VERSION.SDK_INT >= Build.VERSION_CODES.HONEYCOMB;
    }
    
    public static String md5(String string) {
        byte[] hash;

        try {
            hash = MessageDigest.getInstance("MD5").digest(string.getBytes("UTF-8"));
        } catch (NoSuchAlgorithmException e) {
            throw new RuntimeException("Huh, MD5 should be supported?", e);
        } catch (UnsupportedEncodingException e) {
            throw new RuntimeException("Huh, UTF-8 should be supported?", e);
        }

        StringBuilder hex = new StringBuilder(hash.length * 2);

        for (byte b : hash) {
            int i = (b & 0xFF);
            if (i < 0x10) hex.append('0');
            hex.append(Integer.toHexString(i));
        }

        return hex.toString();
    }
    
    public static long convertNanosecondsToMilliseconds(long nanoseconds){
    	return Math.round(nanoseconds / 1000000);
    }
    
    public static int convertDensityPixelsToPixels(Context context, int dp){
    	float density = context.getApplicationContext().getResources().getDisplayMetrics().density;
        return Math.round((float)dp * density);
    	
    	//return (int) TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_DIP, dp, context.getResources().getDisplayMetrics());
    }

    public static int convertPixelsToDensityPixels(Context context, int pixels){
    	
    	String resolution = context.getApplicationContext().getString(R.string.derived_device_resolution);
    	int dp = 0;
    	if (resolution.equals(Constants.LDPI)){
    		dp = Math.round(pixels * 1.33f);
    	}
    	else if (resolution.equals(Constants.MDPI)){
    		dp = pixels;
    	}
    	else if (resolution.equals(Constants.HDPI)){
    		dp = Math.round(pixels * .75f);
    	}
    	else if (resolution.equals(Constants.XHDPI)){
    		dp = Math.round(pixels * .5f);
    	}
    	else {
    		dp = Math.round(pixels * .33f);
    	}
    	return dp;
//    	return (int) TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_DIP, pixels, context.getApplicationContext().getResources().getDisplayMetrics());
    }
    
 public static int convertPixelsBasedOnXxhdpiReolution(Context context, int pixels){
    	
    	String resolution = context.getApplicationContext().getString(R.string.derived_device_resolution);
    	int i = 0;
    	if (resolution.equals(Constants.LDPI)){//120
    		i = Math.round(pixels * .25f);
    	}
    	else if (resolution.equals(Constants.MDPI)){//160
    		i = Math.round(pixels * .33f);
    	}
    	else if (resolution.equals(Constants.HDPI)){//240
    		i = Math.round(pixels * .5f);
    	}
    	else if (resolution.equals(Constants.XHDPI)){//320
    		i = Math.round(pixels * .66f);
    	}
    	else if (resolution.equals(Constants.XXHDPI)){//480
    		i = pixels;
    	}
    	else {
    		i = Math.round(pixels * 1.33f); //640
    	} 
    	return i;
//    	return (int) TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_DIP, pixels, context.getApplicationContext().getResources().getDisplayMetrics());
    }
    
    public static String getTimeSinceString(Context context,Date targetDate){
    	//date diff in milliseconds
    	if (targetDate == null) {
    		return "? (date)"; 
    	}
    	
		long diff = System.currentTimeMillis() - targetDate.getTime();
		
		
		long diffSeconds = (long)diff/1000;
		long diffMinutes = (long)diff/60000;
		long diffHours = (long)diff/3600000;
		long diffDays = (long)diff/86400000;
		
		//Logger.d(TAG, "getTimeSinceString System.currentTimeMillis()=" + System.currentTimeMillis() + " diff=" + diff + "  targetDate.getTime()=" +  targetDate.getTime() + " diffSeconds=" + diffSeconds);
	
		
		String timeSince = "";
		if (diffSeconds < 15){
			timeSince = context.getString(R.string.few_seconds_ago);
		}
		else if (diffSeconds < 60){
			timeSince = String.format(context.getString(R.string.seconds_ago), diffSeconds);		
		}
		else if (diffMinutes < 3){
			timeSince = context.getString(R.string.about_a_minute_ago);		
		}
		else if (diffMinutes < 60){
			timeSince = String.format(context.getString(R.string.minutes_ago), diffMinutes);		
		}
		else if (diffHours == 1){
			timeSince = context.getString(R.string.an_hour_ago);		
		}
		else if (diffHours < 23){
			timeSince = String.format(context.getString(R.string.hours_ago), diffHours);		
		}
		else if (diffHours < 36){
			timeSince = context.getString(R.string.a_day_ago);		
		}
		else if (diffHours < 56){
			timeSince = String.format(context.getString(R.string.days_ago), "2");		
		}
		else {
			timeSince = String.format(context.getString(R.string.days_ago), diffDays);		
		}

		
		return timeSince;
    }
    
    public static void shuffleArray(String[] ar)
    {
      Random rnd = new Random();
      for (int i = ar.length - 1; i >= 0; i--)
      {
        int index = rnd.nextInt(i + 1);
        // Simple swap
        String a = ar[index];
        ar[index] = ar[i];
        ar[i] = a;
      }
    }
    
    public static int coinFlip(){
    	return (int)(Math.random() * 2);
    }
    
    public static int getRandomNumberFromRange(int min, int max){
    	
    	return min + (int)(Math.random() * ((max - min) + 1));
 
    }
    
    public static String getOppositeDirection(String direction){
    	if (direction.equals(Constants.DIRECTION_LEFT)){
			return Constants.DIRECTION_RIGHT;
		}
    	if (direction.equals(Constants.DIRECTION_RIGHT)){
			return Constants.DIRECTION_LEFT;
		}
    	if (direction.equals(Constants.DIRECTION_ABOVE)){
			return Constants.DIRECTION_BELOW;
		}
    	if (direction.equals(Constants.DIRECTION_BELOW)){
			return Constants.DIRECTION_ABOVE;
		}
    	return "";
    }
    
    public static <T> T[] concatArrays(T[] first, T[] second) {
    	  T[] result = Arrays.copyOf(first, first.length + second.length);
    	  System.arraycopy(second, 0, result, first.length, second.length);
    	  return result;
    	}
    
    public static int getResourceId(Context context, String drawableFileName){
	    return context.getResources().getIdentifier(context.getString(R.string.namespace) + ":drawable/" + drawableFileName, null, null);	
    }
    
    public static Enums.InstalledFromStore fromAppStore(Context context){
    //	PackageManager pm = getPackageManager();
    //	String installationSource = pm.getInstallerPackageName(getPackageName());
    	String installer = context.getPackageManager().getInstallerPackageName(context.getString(R.string.namespace)); 
    	
    	//might just change manifest to make it point to specific store when building for that store

    	if (installer != null && installer.equals(Constants.GOOGLE_PLAY_STORE)){
    		return Enums.InstalledFromStore.GOOGLE_PLAY;
    	} 
    	else if (installer != null && installer.equals(Constants.AMAZON_APPSTORE)){
    		return Enums.InstalledFromStore.AMAZON;
    	} 
    	else {
    		ApplicationInfo ai;
			try {
				ai = context.getPackageManager().getApplicationInfo(context.getPackageName(),PackageManager.GET_META_DATA);
				
				String defaultAppStore = (String) ai.metaData.get("DefaultAppStore");
	    		
	    	    if (defaultAppStore.equals(Constants.GOOGLE_PLAY_STORE)){
	    	    	return Enums.InstalledFromStore.GOOGLE_PLAY;
	    	    }
	    	    else if (defaultAppStore.equals(Constants.AMAZON_APPSTORE)){
	        		return Enums.InstalledFromStore.AMAZON;
	        	} 
			} catch (NameNotFoundException e) {
				Logger.d(TAG, e.getMessage());
			}
    	   
			return Enums.InstalledFromStore.NO_STORE;
     
    	}
    	//<package name="com.package.bla.bla" ... installer="com.amazon.venezia">
    	//<package name="com.package.bla.bla" ... installer="com.android.vending">
    }
    
    public static String appStoreTitle(Context context){
    	Enums.InstalledFromStore store = fromAppStore(context);
    	
    	if (store ==  Enums.InstalledFromStore.GOOGLE_PLAY){
    		return context.getString(R.string.app_store_google_play);
    	} 
    	else if (store == Enums.InstalledFromStore.AMAZON){
      		return context.getString(R.string.app_store_amazon);
    	}
    	else {
      		return context.getString(R.string.app_store_unspecified);
       	}

     }
    	
	public static String trimEnd(String input)
	{
		return input.replaceAll("\\s+$", "");	
	}	
     
 
    
}
