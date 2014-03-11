package com.riotapps.wordbase.services;

import com.riotapps.wordbase.hooks.WordService;
import com.riotapps.wordbase.utils.ApplicationContext;
import com.riotapps.wordbase.utils.Logger;
import com.riotapps.wordbase.utils.Utils;

import android.app.Service;
import android.content.Context;
import android.content.Intent;
import android.os.AsyncTask;
import android.os.IBinder;

public class WordLoaderService extends Service {
	private static final String TAG = WordLoaderService.class.getSimpleName();
	
	public long runningTime = System.nanoTime();
	public long captureTime = System.nanoTime();
	private boolean wordsLoaded = false;
	private LoadWordsTask runningTask;
	private boolean isProcessing = false;
	private Context context = this;
	
	public static boolean isLoading = true;
	
   @Override
   public IBinder onBind(Intent intent) {
      return null;
   }

   @Override
   public void onCreate() {
      //code to execute when the service is first created
	   Logger.d(TAG, "onCreate called");
	 
   }

   @Override
   public void onDestroy() {
      //code to execute when the service is shutting down
	   this.runningTask = null;
   }

   @Override
   public int onStartCommand(Intent intent, int flags, int startId) {
      //code to execute when the service is starting up
	   Logger.d(TAG, "onStartCommand called");
	
	   if (!isProcessing){
		   this.isProcessing = true;
		   this.runningTask = new LoadWordsTask();
		   this.runningTask.execute("");
	   }
	  // this.stopSelf();
	   return START_STICKY;
   }
   
	private void captureTime(String text){
	     this.captureTime = System.nanoTime();
	     Logger.d(TAG, String.format("%1$s - time since last capture=%2$s", text, Utils.convertNanosecondsToMilliseconds(this.captureTime - this.runningTime)));
	     this.runningTime = this.captureTime;

	}
	

	 private class LoadWordsTask extends AsyncTask<String, Void, String> {

         @Override
         protected String doInBackground(String... params) {
        	//   ApplicationContext appContext = (ApplicationContext)getApplicationContext();
        	   try{
        		   
        		    captureTime("sqlite initialize (copy database) starting"); 
        	    	WordService.createDatabase(context);
        	        captureTime("sqlite initialize (copy database) ended");
        	   
        	    	WordService wordService = new WordService(context);
        	    	//DatabaseHelper db = new DatabaseHelper(this);
        	     //   this.captureTime("sqlite initialize (copy database) starting");   
        	    //	wordService.initialize(this);
        	       // this.captureTime("sqlite initialize (copy database) ended");
        	    	
        	  
        	    	Logger.d(TAG, "does cast exist as a word? " + wordService.doesWordExist("cast"));
        	        captureTime("sqlite check for cast ended");
        	    	   
        	    	Logger.d(TAG, "does castcc exist as a word? " + wordService.doesWordExist("castcc"));
        	        captureTime("sqlite check for castcc ended");
        	          
        	    	Logger.d(TAG, "does ghilnoos exist as an index? " + wordService.doesIndexExist("ghilnoos"));
        	        captureTime("sqlite check for ghilnoos ended");
        	           
        	    	Logger.d(TAG, "does ssuwyddddddd exist as an index? " + wordService.doesIndexExist("ssuwyddddddd"));
        	        captureTime("sqlite check for ssuwyddddddd ended");
        	        
        	      //  wordService.tempAddIndexes();
        	      //  this.captureTime("sqlite adding indexes ended");
        	        
        	        
        	    	Logger.d(TAG, "does cast exist as a word? " + wordService.doesWordExist("cast"));
        	        captureTime("sqlite check for cast ended");
        	    	   
        	    	Logger.d(TAG, "does castcc exist as a word? " + wordService.doesWordExist("castcc"));
        	        captureTime("sqlite check for castcc ended");
        	          
        	    	Logger.d(TAG, "does ghilnoos exist as an index? " + wordService.doesIndexExist("ghilnoos"));
        	        captureTime("sqlite check for ghilnoos ended");
        	           
        	    	Logger.d(TAG, "does ssuwyddddddd exist as an index? " + wordService.doesIndexExist("ssuwyddddddd"));
        	        captureTime("sqlite check for ssuwyddddddd ended");
        	        
        	    	wordService.finish();
        	    	wordService = null;
        	     
        	   }
        	   catch (Exception e){
        		   Logger.d(TAG, e.toString());
        	   }
        	   Logger.d(TAG, "all words loaded");
               return "Executed";
         }      

         @Override
         protected void onPostExecute(String result) {
        	 WordLoaderService.isLoading = false; 
        	 stopSelf();
         }

         @Override
         protected void onPreExecute() {
         }

         @Override
         protected void onProgressUpdate(Void... values) {
         }
   }
}
