package com.riotapps.wordbase.data;

import java.lang.reflect.Type;
import java.util.List;

import android.content.SharedPreferences;
import android.os.Build;

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
	
}