package com.riotapps.wordbase.hooks;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import android.content.Context;
import android.content.SharedPreferences;

import com.riotapps.wordbase.R;
import com.riotapps.wordbase.billing.Inventory;
import com.riotapps.wordbase.data.StoreData;
import com.riotapps.wordbase.utils.Constants;
import com.riotapps.wordbase.utils.Logger;
import com.riotapps.wordbase.utils.Storage;

public class StoreService {
	private static final String TAG = StoreService.class.getSimpleName();
	
	public static Purchase getPurchaseBySku(String sku){
		return StoreData.getPurchaseBySku(sku);
	}
	/*
	public static List<StoreItem> getStoreItems(){
		
		return StoreData.getStoreItems();
	}
	*/

	public static List<String> getAllSkus(Context context){
		List<String> skus = new ArrayList<String>();
		skus.add(context.getString(R.string.SKU_GOOGLE_PLAY_HOPPER_PEEK));
		skus.add(context.getString(R.string.SKU_GOOGLE_PLAY_HIDE_INTERSTITIAL));
		skus.add(context.getString(R.string.SKU_GOOGLE_PLAY_PREMIUM_UPGRADE));
		skus.add(context.getString(R.string.SKU_GOOGLE_PLAY_WORD_DEFINITIONS));
		skus.add(context.getString(R.string.SKU_GOOGLE_PLAY_WORD_HINTS));
		skus.add(context.getString(R.string.SKU_GOOGLE_PLAY_SPEED_ROUNDS));
		skus.add(context.getString(R.string.SKU_GOOGLE_PLAY_DOUBLE_TIME));
		
		return skus;
	}
	
	public static boolean isHideInterstitialAdPurchased(Context context){
	 	//		return false;
		Purchase purchase = StoreData.getPurchaseBySku(context.getString(R.string.SKU_GOOGLE_PLAY_HIDE_INTERSTITIAL));
		return purchase.isPurchased() || isPremiumUpgradePurchased(context);		
	}

	public static boolean isPremiumUpgradePurchased(Context context){
	//	return false;	
		Purchase purchase = StoreData.getPurchaseBySku(context.getString(R.string.SKU_GOOGLE_PLAY_PREMIUM_UPGRADE));
		return purchase.isPurchased();
	}

	public static boolean isSpeedRoundsPurchased(Context context){
 		Purchase purchase = StoreData.getPurchaseBySku(context.getString(R.string.SKU_GOOGLE_PLAY_SPEED_ROUNDS));
 		return purchase.isPurchased() || isPremiumUpgradePurchased(context);
	}
	
	public static boolean isDoubleTimePurchased(Context context){
 		Purchase purchase = StoreData.getPurchaseBySku(context.getString(R.string.SKU_GOOGLE_PLAY_DOUBLE_TIME));
 		return purchase.isPurchased() || isPremiumUpgradePurchased(context);
	}
	
	public static boolean isHopperPeekPurchased(Context context){
		
		//return true;
		
 		Purchase purchase = StoreData.getPurchaseBySku(context.getString(R.string.SKU_GOOGLE_PLAY_HOPPER_PEEK));
 		return purchase.isPurchased() || isPremiumUpgradePurchased(context);
	}
	
	public static boolean isWordDefinitionLookupPurchased(Context context){
		
//		return false;
		Purchase purchase = StoreData.getPurchaseBySku(context.getString(R.string.SKU_GOOGLE_PLAY_WORD_DEFINITIONS));
		return purchase.isPurchased() || isPremiumUpgradePurchased(context);
	}
	
	public static boolean isWordHintsPurchased(Context context){
		
//		return false;
		Purchase purchase = StoreData.getPurchaseBySku(context.getString(R.string.SKU_GOOGLE_PLAY_WORD_HINTS));
		return purchase.isPurchased() || isPremiumUpgradePurchased(context);
	}
	
	public static boolean isHideBannerAdsPurchased(Context context){
		return isPremiumUpgradePurchased(context);	
	}
	
	public static String getIABPublicKey(Context context){
		return context.getString(R.string.IAB_1) + context.getString(R.string.IAB_2) + context.getString(R.string.IAB_3) + context.getString(R.string.IAB_4) +
				context.getString(R.string.IAB_5) + context.getString(R.string.IAB_6) + context.getString(R.string.IAB_7) + context.getString(R.string.IAB_8) +
			   context.getString(R.string.IAB_9) + context.getString(R.string.IAB_10) + context.getString(R.string.IAB_11) + context.getString(R.string.IAB_12) + context.getString(R.string.IAB_13);
	}

	public static void savePurchase(String sku, String token){
		//check for existing purchase for this sku
		
		 Logger.d(TAG, "savePurchase sku=" + sku);
		
		Purchase purchase = new Purchase(sku, new Date());
		purchase.setPurchaseToken(token);
		
		StoreData.savePurchase(purchase);
		
	}
	
	public static void clearPurchase(String sku){
		
		StoreData.removePurchase(sku);
	}
	
	public static void syncPurchases(Inventory inventory, Context context){
		
		 if (inventory.hasPurchase(context.getString(R.string.SKU_GOOGLE_PLAY_HOPPER_PEEK))){
	    	com.riotapps.wordbase.billing.Purchase skuPeek = inventory.getPurchase(context.getString(R.string.SKU_GOOGLE_PLAY_HOPPER_PEEK));
	    	savePurchase(skuPeek.getSku(), skuPeek.getToken()); 
	     }
		 else{
			 clearPurchase(context.getString(R.string.SKU_GOOGLE_PLAY_HOPPER_PEEK));	 
		 }
		 
	     if (inventory.hasPurchase(context.getString(R.string.SKU_GOOGLE_PLAY_PREMIUM_UPGRADE))){
	        	com.riotapps.wordbase.billing.Purchase skuPremium = inventory.getPurchase(context.getString(R.string.SKU_GOOGLE_PLAY_PREMIUM_UPGRADE));
	        	savePurchase(skuPremium.getSku(), skuPremium.getToken()); 
	     }  
	     else{
			 clearPurchase(context.getString(R.string.SKU_GOOGLE_PLAY_PREMIUM_UPGRADE));	 
		 }
	     
	     if (inventory.hasPurchase(context.getString(R.string.SKU_GOOGLE_PLAY_WORD_DEFINITIONS))){
	        	com.riotapps.wordbase.billing.Purchase skuDefs = inventory.getPurchase(context.getString(R.string.SKU_GOOGLE_PLAY_WORD_DEFINITIONS));
	        	savePurchase(skuDefs.getSku(), skuDefs.getToken()); 
	     }  
	     else{
			 clearPurchase(context.getString(R.string.SKU_GOOGLE_PLAY_WORD_DEFINITIONS));	 
		 }
	     
	     if (inventory.hasPurchase(context.getString(R.string.SKU_GOOGLE_PLAY_HIDE_INTERSTITIAL))){
	        	com.riotapps.wordbase.billing.Purchase skuInterstitial = inventory.getPurchase(context.getString(R.string.SKU_GOOGLE_PLAY_HIDE_INTERSTITIAL));
	        	savePurchase(skuInterstitial.getSku(), skuInterstitial.getToken()); 
	     }  
	     else{
			 clearPurchase(context.getString(R.string.SKU_GOOGLE_PLAY_HIDE_INTERSTITIAL));	 
		 }
	     
	     if (inventory.hasPurchase(context.getString(R.string.SKU_GOOGLE_PLAY_WORD_HINTS))){
	        	com.riotapps.wordbase.billing.Purchase skuHints = inventory.getPurchase(context.getString(R.string.SKU_GOOGLE_PLAY_WORD_HINTS));
	        	savePurchase(skuHints.getSku(), skuHints.getToken()); 
	     }  
	     else{
			 clearPurchase(context.getString(R.string.SKU_GOOGLE_PLAY_WORD_HINTS));	 
		 }
	     if (inventory.hasPurchase(context.getString(R.string.SKU_GOOGLE_PLAY_DOUBLE_TIME))){
	        	com.riotapps.wordbase.billing.Purchase skuDoubleTime = inventory.getPurchase(context.getString(R.string.SKU_GOOGLE_PLAY_DOUBLE_TIME));
	        	savePurchase(skuDoubleTime.getSku(), skuDoubleTime.getToken()); 
	     }  
	     else{
			 clearPurchase(context.getString(R.string.SKU_GOOGLE_PLAY_DOUBLE_TIME));	 
		 }
	     if (inventory.hasPurchase(context.getString(R.string.SKU_GOOGLE_PLAY_SPEED_ROUNDS))){
	        	com.riotapps.wordbase.billing.Purchase skuSpeedRounds = inventory.getPurchase(context.getString(R.string.SKU_GOOGLE_PLAY_SPEED_ROUNDS));
	        	savePurchase(skuSpeedRounds.getSku(), skuSpeedRounds.getToken()); 
	     }  
	     else{
			 clearPurchase(context.getString(R.string.SKU_GOOGLE_PLAY_SPEED_ROUNDS));	 
		 }
	} 
	
	

	public static String getCachedInventoryItemPrice(String sku){
	 
		return StoreData.getCachedInventoryItemPrice(sku);
	}
	
	public static void saveCachedInventoryItemPrice(String sku, String price){
		StoreData.saveCachedInventoryItemPrice(sku, price);
	}
 
	
}
