package com.riotapps.wordbase.ui;

import java.util.List;

import com.riotapps.wordbase.hooks.Game;
import com.riotapps.wordbase.hooks.PlayedTile;
import com.riotapps.wordbase.hooks.WordService;

import android.graphics.Canvas;
import android.util.Log;
import android.view.SurfaceHolder;

public class WordLoaderThread extends Thread {
	 private static final String TAG = WordLoaderThread.class.getSimpleName();
	
	 private WordService wordService;
	 private Game game;
	 private String contextPlayerId;
 
	 private Object pauseLock = new Object();  
	 private boolean paused;
 
	  public WordLoaderThread(WordService wordService, Game game, String contextPlayerId) {
	    this.wordService = wordService;
	    this.game = game;
	    this.contextPlayerId = contextPlayerId;
	 }
	  
	 
	 
	 public void onPause() {
		    synchronized (pauseLock) {
		        paused = true;
		    }
		}
	 
	 public void onResume() {
		    synchronized (pauseLock) {
		        paused = false;
		        pauseLock.notifyAll();
		    }
		}
	  
	 @Override
	 public void run() {
		 
	//	 for(PlayedTile tile : this.game.getPlayedTiles()){
	//		 this.wordService.loadList(tile.getLatestPlayedLetter().getLetter());
	//	 }
		 
	//	 for(String letter : this.game.getContextPlayerGame().getTrayLetters()){
	//		 this.wordService.loadList(letter);
	//	 }
	 }
	 
	}
