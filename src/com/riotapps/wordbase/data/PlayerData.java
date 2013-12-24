package com.riotapps.wordbase.data;

import java.lang.reflect.Type;
import java.util.Date;
import java.util.List;

import android.content.Context;
import android.content.SharedPreferences;
import android.os.Build;

import com.amazon.inapp.purchasing.Offset;
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import com.riotapps.wordbase.R;
import com.riotapps.wordbase.utils.ApplicationContext;
import com.riotapps.wordbase.utils.Constants;
import com.riotapps.wordbase.utils.FileUtils;
import com.riotapps.wordbase.utils.Storage;
import com.riotapps.wordbase.hooks.Player;
import com.riotapps.wordbase.hooks.StoreItem;

public class PlayerData {

	public static Player getPlayer(){
		//this might change to sqlLite
		 Gson gson = new Gson(); 
		 Type type = new TypeToken<Player>() {}.getType();
	     SharedPreferences settings = Storage.getSharedPreferences();
	     
	    // Logger.w(TAG, "getPlayerFromLocal player=" + settings.getString(Constants.USER_PREFS_PLAYER_JSON, Constants.EMPTY_JSON));
	     String _player = settings.getString(Constants.USER_PREFS_PLAYER_JSON, Constants.EMPTY_STRING);
	     
	     if (_player == Constants.EMPTY_STRING){
	    	 return null;
	     
	     }
	     Player player = gson.fromJson(_player, type);
		 
	     settings = null;
	     gson = null;
	     return player;

	}
	
	public static void savePlayer(Player player){
		Gson gson = new Gson(); 
		SharedPreferences settings = Storage.getSharedPreferences();
        SharedPreferences.Editor editor = settings.edit();
		
		editor.putString(Constants.USER_PREFS_PLAYER_JSON, gson.toJson(player));
		editor.apply();
        
	    settings = null;
		gson = null;
	}
	
	public static int getWordDatabaseVersion(){
		
	    SharedPreferences settings = Storage.getSharedPreferences();
	    int version = settings.getInt(Constants.USER_PREFS_WORD_DATABASE_VERSION, Constants.DEFAULT_WORD_DATABASE_VERSION);
	    
	    settings = null;
	    
	    return version;
	}
	
	public static void saveWordDatabaseVersion(int version){
	    SharedPreferences settings = Storage.getSharedPreferences();
    	SharedPreferences.Editor editor = settings.edit();
	
  		editor.putInt(Constants.USER_PREFS_WORD_DATABASE_VERSION, version);
  		editor.apply();
	  
	    settings = null;
	 
	}	
	
	public static int getRemainingFreeUsesHopperPeek(){
		
	    SharedPreferences settings = Storage.getSharedPreferences();
	    int uses = settings.getInt(Constants.USER_PREFS_FREE_REMAINING_USES_HOPPER_PEEK, Constants.FREE_USES_WORD_HINTS);
	    
	    settings = null;
	    
	    return uses;
	}
	

	public static int getRemainingFreeUsesWordDefinition(){
		
	    SharedPreferences settings = Storage.getSharedPreferences();
	    int uses = settings.getInt(Constants.USER_PREFS_FREE_REMAINING_USES_WORD_DEFINITION, Constants.FREE_USES_WORD_DEFINITION);
	    
	    settings = null;
	    
	    return uses;
	}
	
	public static int getRemainingFreeUsesWordDefinition(Context context){
		
	    SharedPreferences settings = Storage.getSharedPreferences();
	 /*   int uses = settings.getInt(Constants.USER_PREFS_FREE_REMAINING_USES_WORD_DEFINITION, context.getResources().getInteger(R.integer.free_uses_word_definitions));
	    
	    settings = null;
	    
	    return uses;
	    SharedPreferences settings = Storage.getSharedPreferences();
	    */
	    int uses = settings.getInt(Constants.USER_PREFS_FREE_REMAINING_USES_WORD_DEFINITION, 0); //context.getResources().getInteger(R.integer.free_uses_speed_rounds));
	    
	    settings = null;
	    
	    return context.getResources().getInteger(R.integer.free_uses_word_definitions) - uses <= 0 ? 0 :context.getResources().getInteger(R.integer.free_uses_word_definitions) - uses;

	    
	}
	
	public static int removeAFreeUseFromHopperPeek(){
	    SharedPreferences settings = Storage.getSharedPreferences();
	    int uses = settings.getInt(Constants.USER_PREFS_FREE_REMAINING_USES_HOPPER_PEEK, Constants.FREE_USES_HOPPER_PEEK);
	    
	    if (uses > 0){
	    	uses -= 1;
	    	SharedPreferences.Editor editor = settings.edit();
		
	  		editor.putInt(Constants.USER_PREFS_FREE_REMAINING_USES_HOPPER_PEEK, uses);
	  		editor.apply();
	    } 
	    
	    settings = null;
	    return uses;
	}
	
	public static int removeAFreeUseFromWordDefinition(){
	    SharedPreferences settings = Storage.getSharedPreferences();
	    int uses = settings.getInt(Constants.USER_PREFS_FREE_REMAINING_USES_WORD_DEFINITION, Constants.FREE_USES_WORD_DEFINITION);
	    
	    if (uses > 0){
	    	uses -= 1;
	    	SharedPreferences.Editor editor = settings.edit();
		
	  		editor.putInt(Constants.USER_PREFS_FREE_REMAINING_USES_WORD_DEFINITION, uses);
	  		editor.apply();
	    } 
	    
	    settings = null;
	    return uses;
	}	
	public static int removeAFreeUseFromWordDefinition(Context context){
	  /*  SharedPreferences settings = Storage.getSharedPreferences();
	    int uses = settings.getInt(Constants.USER_PREFS_FREE_REMAINING_USES_WORD_DEFINITION, context.getResources().getInteger(R.integer.free_uses_word_definitions));
	    
	    if (uses > 0){
	    	uses -= 1;
	    	SharedPreferences.Editor editor = settings.edit();
		
	  		editor.putInt(Constants.USER_PREFS_FREE_REMAINING_USES_WORD_DEFINITION, uses);
	  		editor.apply();
	    } 
	    
	    settings = null;
	    return uses;
	    */
	    
	    SharedPreferences settings = Storage.getSharedPreferences();
	    int uses = settings.getInt(Constants.USER_PREFS_FREE_REMAINING_USES_WORD_DEFINITION, 0);// context.getResources().getInteger(R.integer.free_uses_double_time));
	    
	  //  if (uses > 0){
	    	uses += 1;
	    	SharedPreferences.Editor editor = settings.edit();
		
	  		editor.putInt(Constants.USER_PREFS_FREE_REMAINING_USES_WORD_DEFINITION, uses);
	  		editor.apply();
	   // } 
	    
	    settings = null;
	    return context.getResources().getInteger(R.integer.free_uses_word_definitions) - uses <= 0 ? 0 :context.getResources().getInteger(R.integer.free_uses_word_definitions) - uses;

	}	
	
	public static int getRemainingFreeUsesSpeedRounds(Context context){
		
	    SharedPreferences settings = Storage.getSharedPreferences();
	    int uses = settings.getInt(Constants.USER_PREFS_FREE_REMAINING_USES_SPEED_ROUNDS, 0); //context.getResources().getInteger(R.integer.free_uses_speed_rounds));
	    
	    settings = null;
	    
	    return context.getResources().getInteger(R.integer.free_uses_speed_rounds) - uses <= 0 ? 0 :context.getResources().getInteger(R.integer.free_uses_speed_rounds) - uses;

	}
	
	public static int removeAFreeUseFromSpeedRounds(Context context){
 
	    SharedPreferences settings = Storage.getSharedPreferences();
	    int uses = settings.getInt(Constants.USER_PREFS_FREE_REMAINING_USES_SPEED_ROUNDS, 0);// context.getResources().getInteger(R.integer.free_uses_double_time));
	    
	  //  if (uses > 0){
	    	uses += 1;
	    	SharedPreferences.Editor editor = settings.edit();
		
	  		editor.putInt(Constants.USER_PREFS_FREE_REMAINING_USES_SPEED_ROUNDS, uses);
	  		editor.apply();
	   // } 
	    
	    settings = null;
	    return context.getResources().getInteger(R.integer.free_uses_speed_rounds) - uses <= 0 ? 0 :context.getResources().getInteger(R.integer.free_uses_speed_rounds) - uses;

	    
	}	
	
	public static int getRemainingFreeUsesDoubleTime(Context context){
		
	    SharedPreferences settings = Storage.getSharedPreferences();
	    int uses = settings.getInt(Constants.USER_PREFS_FREE_REMAINING_USES_DOUBLE_TIME, 0); //context.getResources().getInteger(R.integer.free_uses_double_time));
	    
	    settings = null;
	    
	    return context.getResources().getInteger(R.integer.free_uses_double_time) - uses <= 0 ? 0 :context.getResources().getInteger(R.integer.free_uses_double_time) - uses;
 
	}
	
	public static int removeAFreeUseFromDoubleTime(Context context){
	    SharedPreferences settings = Storage.getSharedPreferences();
	    int uses = settings.getInt(Constants.USER_PREFS_FREE_REMAINING_USES_DOUBLE_TIME, 0);// context.getResources().getInteger(R.integer.free_uses_double_time));
	    
	  //  if (uses > 0){
	    	uses += 1;
	    	SharedPreferences.Editor editor = settings.edit();
		
	  		editor.putInt(Constants.USER_PREFS_FREE_REMAINING_USES_DOUBLE_TIME, uses);
	  		editor.apply();
	   // } 
	    
	    settings = null;
	    return context.getResources().getInteger(R.integer.free_uses_double_time) - uses <= 0 ? 0 :context.getResources().getInteger(R.integer.free_uses_double_time) - uses;
	}	
	
	public static int addToToWordHintsPreviewsUsed(){
	    SharedPreferences settings = Storage.getSharedPreferences();
	    int uses = settings.getInt(Constants.USER_PREFS_FREE_USAGES_WORD_HINTS, 0);
	    
	//    if (uses > 0){
	    	uses += 1;
	    	SharedPreferences.Editor editor = settings.edit();
		
	  		editor.putInt(Constants.USER_PREFS_FREE_USAGES_WORD_HINTS, uses);
	  		editor.apply();
	 //   } 
	    
	    settings = null;
	    return Constants.FREE_USES_WORD_HINTS - uses <= 0 ? 0 : Constants.FREE_USES_WORD_HINTS - uses;
	}
	
	public static int getRemainingFreeUsesWordHints(){
		
	    SharedPreferences settings = Storage.getSharedPreferences();
	    int uses = settings.getInt(Constants.USER_PREFS_FREE_USAGES_WORD_HINTS, 0);
	    
	    settings = null;
	    
	    return Constants.FREE_USES_WORD_HINTS - uses <= 0 ? 0 : Constants.FREE_USES_WORD_HINTS - uses;
	}
	
	
	public static long getLastInterstitialPurchaseReminderTime(){
 
	    SharedPreferences settings = Storage.getSharedPreferences();
	    long uses = settings.getLong(Constants.USER_PREFS_LAST_INTERSTITIAL_PURCHASE_REMINDER, Constants.DEFAULT_HIDE_AD_PURCHASE_REMINDER);
	    
	    settings = null;
	    
	    return uses;
	}
	
	public static void setLastInterstitialPurchaseReminderTime(){
	    SharedPreferences settings = Storage.getSharedPreferences();
	 
	    SharedPreferences.Editor editor = settings.edit();
		
	  	editor.putLong(Constants.USER_PREFS_LAST_INTERSTITIAL_PURCHASE_REMINDER, new Date().getTime());
	  	editor.apply();

	    settings = null;
	  
	}
	

	public static Offset getPersistedOffset(String userId){
		Gson gson = new Gson(); 
		Type type = new TypeToken<Offset>() {}.getType();
	    
		SharedPreferences settings = Storage.getSharedPreferences();
	    String _offset = settings.getString(String.format(Constants.USER_PREFS_AMAZON_PURCHASE_OFFSET, userId), Constants.EMPTY_STRING);
 	     
	    Offset o = null;
	    
		 if (_offset == Constants.EMPTY_STRING){
			 o = Offset.BEGINNING;
		 }
		 else {
			 o = gson.fromJson(_offset, type);
		 }
	    	    
	     settings = null;
	     gson = null;
	     return o;
	}
	
	public static void setPersistedOffset(Offset offset, String userId){

		    Gson gson = new Gson(); 
			SharedPreferences settings = Storage.getSharedPreferences();
	        SharedPreferences.Editor editor = settings.edit();
			
			editor.putString(String.format(Constants.USER_PREFS_AMAZON_PURCHASE_OFFSET, userId), gson.toJson(offset));
			editor.apply();
	        
		    settings = null;
			gson = null;
	}

}
