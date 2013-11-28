package com.riotapps.wordbase.data;

import java.lang.reflect.Type;
import java.util.List;

import android.content.SharedPreferences;
import android.os.Build;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import com.riotapps.wordbase.R;
import com.riotapps.wordbase.hooks.Purchase;
import com.riotapps.wordbase.hooks.StoreItem;
import com.riotapps.wordbase.utils.ApplicationContext;
import com.riotapps.wordbase.utils.Constants;
import com.riotapps.wordbase.utils.FileUtils;
import com.riotapps.wordbase.utils.Storage;

public class StoreData {
	public static Purchase getPurchaseBySku(String sku){
		SharedPreferences settings = Storage.getPurchaseSharedPreferences();
			
	    String _purchase =  settings.getString(String.format(Constants.PURCHASE_PREFS_ITEM, sku), Constants.EMPTY_STRING);
		
	    if (_purchase.equals(Constants.EMPTY_STRING)){
	    	
	    	return new Purchase(sku);
	    }
	    else {
			Gson gson = new Gson(); 			 
			Type type = new TypeToken<Purchase>() {}.getType();
			Purchase purchase = gson.fromJson(_purchase, type);
			gson = null;
			return purchase;
	    }
	}
	
	public static void savePurchase(Purchase purchase){
		Gson gson = new Gson(); 			 
		 
		SharedPreferences settings = Storage.getPurchaseSharedPreferences();
		SharedPreferences.Editor editor = settings.edit();
		editor.putString(String.format(Constants.PURCHASE_PREFS_ITEM, purchase.getSku()), gson.toJson(purchase));
		// Check if we're running on GingerBread or above
		if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.GINGERBREAD) {
		     // If so, call apply()
		     editor.apply();
		 // if not
		} else {
		     // Call commit()
		     editor.commit();
		} 
	    
		gson = null;
	}
	
	public static void removePurchase(String sku){
	 	SharedPreferences settings = Storage.getPurchaseSharedPreferences();
		SharedPreferences.Editor editor = settings.edit();
		editor.remove(String.format(Constants.PURCHASE_PREFS_ITEM, sku));
	 	// Check if we're running on GingerBread or above
		if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.GINGERBREAD) {
		     // If so, call apply()
		     editor.apply();
		 // if not
		} else {
		     // Call commit()
		     editor.commit();
		} 
	
	}
	
	public static String getCachedInventoryItemPrice(String sku){
		SharedPreferences settings = Storage.getPurchaseSharedPreferences();
		return settings.getString(String.format(Constants.INVENTORY_PREFS_ITEM, sku), "??");
	}
	
	public static void saveCachedInventoryItemPrice(String sku, String price){
 
		SharedPreferences settings = Storage.getPurchaseSharedPreferences();
		SharedPreferences.Editor editor = settings.edit();
		editor.putString(String.format(Constants.INVENTORY_PREFS_ITEM, sku), price);
		// Check if we're running on GingerBread or above
		if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.GINGERBREAD) {
		     // If so, call apply()
		     editor.apply();
		 // if not
		} else {
		     // Call commit()
		     editor.commit();
		} 
 
	}
	/*
	public static List<StoreItem> getStoreItems(){
		
	 	Gson gson = new Gson(); 
		Type type = new TypeToken<List<StoreItem>>() {}.getType();


	    ApplicationContext appContext = (ApplicationContext)ApplicationContext.getAppContext().getApplicationContext(); 
	    
 
	    List<StoreItem> storeItems = gson.fromJson(FileUtils.ReadRawTextFile(appContext, R.raw.store), type);
	    
	 
		gson = null;    
		
		return storeItems;
	}
	*/
	
	
}
