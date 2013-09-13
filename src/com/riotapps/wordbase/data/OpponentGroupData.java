package com.riotapps.wordbase.data;

import java.lang.reflect.Type;
import java.util.ArrayList;
import java.util.List;

import android.content.SharedPreferences;
import android.os.Build;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import com.riotapps.wordbase.R;
import com.riotapps.wordbase.hooks.Opponent;
import com.riotapps.wordbase.hooks.OpponentGroup;
import com.riotapps.wordbase.utils.ApplicationContext;
import com.riotapps.wordbase.utils.Constants;
import com.riotapps.wordbase.utils.FileUtils;
import com.riotapps.wordbase.utils.Logger;
import com.riotapps.wordbase.utils.Storage;

public class OpponentGroupData {
	
	private static final String TAG = OpponentGroupData.class.getSimpleName();

	public static List<OpponentGroup> getActiveOpponentGroups(){
		Logger.d(TAG, "getActiveOpponentGroups called");
		
		Gson gson = new Gson(); 
		 
		Type type = new TypeToken<List<OpponentGroup>>() {}.getType();
 	
	    List<OpponentGroup> opponentGroups;
	    List<OpponentGroup> activeOpponentGroups = new ArrayList<OpponentGroup>();
	     	    
  
    	ApplicationContext appContext = (ApplicationContext)ApplicationContext.getAppContext().getApplicationContext();
    	 
    	//load all opponent groups shipped in this app version
    	opponentGroups = gson.fromJson(FileUtils.ReadRawTextFile(appContext, R.raw.opponent_groups), type);
    		
    	Logger.d(TAG, "getActiveOpponentGroups - num opponentGroups=" + opponentGroups.size());
    	
    	for (OpponentGroup og : opponentGroups){
    		//add auto activated opponent groups to player's list
    		Logger.d(TAG, "activated check loop og.getName()=" + og.getName() + " isactivated=" + og.isAutoActivated());
    		
    		if (og.isPurchased() || og.isAutoActivated()){
    			Logger.d(TAG, "activated check loop match og.getName()=" + og.getName());
    			
    			OpponentGroup o = new OpponentGroup();
    			o.setId(og.getId());
    			o.setName(og.getName());
    			o.setConstant(og.getConstant());        		
    		 
    			activeOpponentGroups.add(o);		
    		}
    	}
     
    	opponentGroups = null;
		gson = null;
		
		
    	Logger.d(TAG, "getActiveOpponentGroups - num activeOpponentGroups=" + activeOpponentGroups.size());
		
		return activeOpponentGroups;	
	}
	
	//this will likely go away
	public static List<OpponentGroup> getInactiveOpponentGroups(){
		Gson gson = new Gson(); 
		 
		Type type = new TypeToken<List<OpponentGroup>>() {}.getType();
 	
	    List<OpponentGroup> opponentGroups;
	    List<OpponentGroup> inactiveOpponentGroups = new ArrayList<OpponentGroup>();
	     	    
  
    	ApplicationContext appContext = (ApplicationContext)ApplicationContext.getAppContext().getApplicationContext();
    	 
    	//load all opponent groups shipped in this app version
    	opponentGroups = gson.fromJson(FileUtils.ReadRawTextFile(appContext, R.raw.opponent_groups), type);
    		  
    	for (OpponentGroup og : opponentGroups){
    		if (!og.isPurchased() && !og.isAutoActivated()){
    			OpponentGroup o = new OpponentGroup();
    			o.setId(og.getId());
    			o.setName(og.getName());
    			o.setConstant(og.getConstant());        		
    		 
    			inactiveOpponentGroups.add(o);		
    		}
    	}
     
    	opponentGroups = null;
		gson = null;
		return inactiveOpponentGroups;
	}
	
	//this will go away
	public static void saveOpponentGroups___(List<OpponentGroup> opponentGroups){
		Gson gson = new Gson(); 
		SharedPreferences settings = Storage.getSharedPreferences();
	    SharedPreferences.Editor editor = settings.edit();
		
		editor.putString(Constants.USER_PREFS_OPPONENT_GROUPS, gson.toJson(opponentGroups));
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
	
}
