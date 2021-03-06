package com.riotapps.wordbase.ui;

import java.lang.reflect.Type;

import android.annotation.TargetApi;
import android.content.Context;
import android.content.SharedPreferences;
import android.os.Build;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import com.riotapps.wordbase.utils.Constants;
import com.riotapps.wordbase.utils.Logger;
import com.riotapps.wordbase.utils.Storage;

@TargetApi(9)
public class GameStateService {
	private static final String TAG = GameStateService.class.getSimpleName();
	
	public static GameState getGameState(Context context, String gameId){
		Logger.d(TAG, "getGameState called gameId=" + gameId);
		
		SharedPreferences settings = Storage.getSharedPreferences(Constants.GAME_STATE); 
	    String gameStatejson = settings.getString(gameId, "");  
	    
	    GameState gameState;
	    
	    if (gameStatejson.length() == 0){
	    	gameState = new GameState();
			gameState.setGameId(gameId);
	    	return gameState;
	    }

	    Gson gson = new Gson();
		Type type = new TypeToken<GameState>() {}.getType();
		gameState = gson.fromJson(gameStatejson, type);
		
		gson = null;
		//just in case
		gameState.setGameId(gameId);
		 return gameState;
	}
	
	public static GameState clearGameState(String gameId){
		Logger.d(TAG, "clearGameState called gameId=" + gameId);
		
		 Gson gson = new Gson();
		 GameState gameState = new GameState();
		 gameState.setGameId(gameId);
		 
		 SharedPreferences settings = Storage.getSharedPreferences(Constants.GAME_STATE);
		 SharedPreferences.Editor editor = settings.edit();
		 editor.putString(gameId, gson.toJson(gameState));
	 
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
		 return gameState;
	}
	
	public static void removeGameState(String gameId){
		
		Logger.d(TAG, "removeGameState called gameId=" + gameId);
		
		 GameState gameState = new GameState();
		 gameState.setGameId(gameId);
		 
		 SharedPreferences settings = Storage.getSharedPreferences(Constants.GAME_STATE);
		 SharedPreferences.Editor editor = settings.edit();
		 editor.remove(gameId);
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
	
	public static void setGameState(GameState gameState){
		
		Logger.d(TAG, "setGameState called gameId=" + gameState.getGameId());
		
		if (gameState != null && gameState.getGameId().length() > 0){
			 Gson gson = new Gson();
			 
			 SharedPreferences settings = Storage.getSharedPreferences(Constants.GAME_STATE);
			 SharedPreferences.Editor editor = settings.edit();
			 editor.putString(gameState.getGameId(), gson.toJson(gameState));
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
}
