package com.riotapps.wordbase;

import java.util.List;
import java.util.Map;
import java.util.Set;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.support.v4.app.FragmentActivity;

import com.amazon.inapp.purchasing.BasePurchasingObserver;
import com.amazon.inapp.purchasing.GetUserIdResponse;
import com.amazon.inapp.purchasing.Item;
import com.amazon.inapp.purchasing.ItemDataResponse;
import com.amazon.inapp.purchasing.Offset;
import com.amazon.inapp.purchasing.PurchaseResponse;
import com.amazon.inapp.purchasing.PurchaseUpdatesResponse;
import com.amazon.inapp.purchasing.PurchasingManager;
import com.amazon.inapp.purchasing.Receipt;
import com.google.analytics.tracking.android.EasyTracker;
import com.riotapps.wordbase.billing.AmazonPurchaseObserver;
import com.riotapps.wordbase.billing.IabHelper;
import com.riotapps.wordbase.billing.IabResult;
import com.riotapps.wordbase.billing.Inventory;
import com.riotapps.wordbase.hooks.Player;
import com.riotapps.wordbase.hooks.PlayerService;
import com.riotapps.wordbase.hooks.StoreService;
import com.riotapps.wordbase.services.WordLoaderService;
import com.riotapps.wordbase.utils.*;

public class Splash  extends FragmentActivity {
   
	private static final String TAG = Splash.class.getSimpleName();

    final Context context = this;
    Splash me = this;
    Handler handler;
    public long startTime = System.nanoTime();
    private IabHelper mHelper;
    private IabHelper.QueryInventoryFinishedListener onPurchaseCheck;
    ApplicationContext appContext;
     
   // public void test(){}
    
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.splash);
      
        this.captureTime("onCreate starting");    
        this.startBackgroundService();
        //sendMessage(this, "123", "message from Wordsmash");
        
      //  Playtomic.Log(//).play();
 	 	
        this.captureTime("handleRouting starting");
        
        Player player = PlayerService.getPlayer();
        
        this.appContext = (ApplicationContext)this.getApplicationContext();
		this.appContext.setPlayer(player);

		
		 // compute your public key and store it in base64EncodedPublicKey
		
		
		//pub logic in to wait for purchase check, perhaps kick it off from inventory listener
		this.prepareInAppPurchasing();
		
        
      //  this.handleRouting();
		 /*
        new Handler().postDelayed(new Runnable() {
            public void run() {

            	Intent intent;
            	if (appContext.getPlayer().getActiveGameId().length() > 0){
            		intent = new Intent(Splash.this, com.riotapps.wordbase.GameSurface.class);
            		intent.putExtra(Constants.EXTRA_GAME_ID, appContext.getPlayer().getActiveGameId());
            	}
            	else{
            		intent = new Intent(Splash.this, com.riotapps.wordbase.Main.class);
            	}
            	//intent.addFlags(Intent.FLAG_ACTIVITY_NO_HISTORY);
            	Splash.this.startActivity(intent); 
            	//Splash.this.finish();

                // transition from splash to main  
            //    overridePendingTransition(R.animator.activityfadein,
             //          R.animator.splashfadeout);
                
            
        		
              	

            }
        }, Constants.SPLASH_DELAY_DURATION);
        
        */
         
        this.captureTime("onCreate ended");
        
     }
    
    @Override
	protected void onResume() {
		// TODO Auto-generated method stub
    	Logger.d(TAG, "onResume starting");
		super.onResume();
		if (Utils.fromAppStore(this) == Enums.InstalledFromStore.AMAZON){
	    	Logger.d(TAG, "AMAZON starting");
			PurchasingManager.initiateGetUserIdRequest();
			Set<String> skus = StoreService.getAllAmazonSkus(this);
			PurchasingManager.initiateItemDataRequest(skus);
	    	
    	}
		
	}

	public void prepareInAppPurchasing(){
		Logger.d(TAG, "prepareInAppPurchasing starting");
    	if (Utils.fromAppStore(this) == Enums.InstalledFromStore.AMAZON){
    		PurchasingManager.registerObserver(new AmazonPurchaseObserver(this));
    	}
    	else {
    		//google play
    	try{
			
            mHelper = new IabHelper(this, StoreService.getIABPublicKey(this));
            mHelper.startSetup(new IabHelper.OnIabSetupFinishedListener() {
            	   public void onIabSetupFinished(IabResult result) {
            		   onSetupFinished(result);
            	   }
            	});
    		}
    		catch (Exception e){
    			Logger.d(TAG, "mHelper e=" + e.getMessage());
    		}
    	}
    }
    
    public void onSetupFinished(IabResult result){
    	  if (!result.isSuccess()) {
 	         // Oh noes, there was a problem.
 	         Logger.d(TAG, "Problem setting up In-app Billing: " + result);
 	      }    
    	  else{
 	         // Hooray, IAB is fully set up!
 	      Logger.d(TAG, "In-app Billing is ready to go...almost ");
 	      
			IabHelper.QueryInventoryFinishedListener mGotInventoryListener 
						= new IabHelper.QueryInventoryFinishedListener() {
				public void onQueryInventoryFinished(IabResult result, Inventory inventory) {
					onPurchaseCheck(result, inventory);
				
				}
			};
			mHelper.queryInventoryAsync(true, StoreService.getAllSkus(this), mGotInventoryListener);

    	  }
    	
    }
    
    @Override
    public void onDestroy() {
       super.onDestroy();
       if (mHelper != null) mHelper.dispose();
       mHelper = null;
    }
    
	 public void onPurchaseCheck(IabResult result, Inventory inventory){
		 Logger.d(TAG, "onPurchaseCheck");
		  if (result.isFailure()) {
		         // handle error here
			  Logger.d(TAG, "onPurchaseCheck failure=" + result.getMessage());
		       } 
	       else {
	         // does the user have the premium upgrade?
	    	   StoreService.syncPurchases(inventory, this);
	    	   
	    	   List<String> skus = StoreService.getAllSkus(this);
	    	   for (String sku : skus){
	    		   if (inventory.hasDetails(sku)){
	    			   StoreService.saveCachedInventoryItemPrice(sku, inventory.getSkuDetails(sku).getPrice());
	    		   }
	    	   }
	       }
		  
		  this.route();
	      	
	 }
	 
	 public void route(){
		 if (appContext.getPlayer().getActiveGameId().length() > 0){
			 	((ApplicationContext)this.getApplication()).startNewActivity(this, Constants.ACTIVITY_CLASS_GAME_SURFACE);

	       	}
	      	else{
			 	((ApplicationContext)this.getApplication()).startNewActivity(this, Constants.ACTIVITY_CLASS_MAIN);
	      	}

	 }
	 
	    
    private void startBackgroundService(){
     	this.startService(new Intent(this, WordLoaderService.class));
    }
   
    
	 @Override
	protected void onStart() {
		// TODO Auto-generated method stub
		super.onStart();
		 EasyTracker.getInstance().activityStart(this);
	}


	@Override
	protected void onStop() {
		// TODO Auto-generated method stub
		super.onStop();
		 EasyTracker.getInstance().activityStop(this);
	}

	    
    public void captureTime(String text){
    	ApplicationContext.captureTime(TAG, text);
	  
	}
    private void handleRouting(){
    	captureTime("handleProcessing starting");
 
    	//perhaps put a thread sleep in here for a small delay
    	try {
			Thread.sleep(Constants.SPLASH_DELAY_DURATION);
		} catch (InterruptedException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
    	Intent intent;
		
      	intent = new Intent(this, com.riotapps.wordbase.Main.class);
    	intent.addFlags(Intent.FLAG_ACTIVITY_NO_HISTORY);
    	this.startActivity(intent); 
     }

	@Override
	protected void onPause() {
		// TODO Auto-generated method stub
		super.onPause();
 		finish();
	}

 	
	private class AmazonPurchaseObserver extends BasePurchasingObserver {
		   private static final String TAG = "AmazonPurchaseObserver";
		   private boolean rvsProductionMode = false;
		   private String currentUserID = null;
		   
		   public AmazonPurchaseObserver(Activity iapActivity) {
		      super(iapActivity);
		   }
		 
		   public void onSdkAvailable(final boolean isSandboxMode) {
			   Logger.d(TAG, "AmazonPurchaseObserver onSdkAvailable " + isSandboxMode);
			   this.rvsProductionMode = isSandboxMode;
		   }
		   public void onGetUserIdResponse(final GetUserIdResponse response) {
			   Logger.d(TAG, "AmazonPurchaseObserver.onGetUserIdResponse starting");
			   if (response.getUserIdRequestStatus() == GetUserIdResponse.GetUserIdRequestStatus.SUCCESSFUL) {
			            currentUserID = response.getUserId();
			            PurchasingManager.initiatePurchaseUpdatesRequest(PlayerService.getPersistedOffset(this.currentUserID));
			        }
			        else { 
			            // Fail gracefully. 
			        } 
		   }
		   
		//   private Offset getPersistedOffset() {
		        // Retrieve the offset you have previously persisted. 
		        // If no offset exists or the app is dealing exclusively with consumables
		        // use Offset.BEGINNING.
		  //  }
		   
		   public void onItemDataResponse(final ItemDataResponse response) {
			   Logger.d(TAG, "AmazonPurchaseObserver.onItemDataResponse starting status=" + response.getItemDataRequestStatus());
			      switch (response.getItemDataRequestStatus()) {
		           case SUCCESSFUL_WITH_UNAVAILABLE_SKUS: 
		        	   // If the requestStatus is successful, retrieve the item data map (item type, icon url, 
		        	   //localized price, title and description) keyed on sku for display in the app.

		               for (final String s : response.getUnavailableSkus()) { 
		                   Logger.v(TAG, "Unavailable SKU:" + s); 
		               }
		   
		           case SUCCESSFUL: 
		               final Map<String, Item> items = response.getItemData();
		               for (final String key : items.keySet()) {
		                   Item i = items.get(key);
		                   StoreService.saveCachedInventoryItemPrice(i.getSku(), i.getPrice());
		                   
		                 //  Logger.v(TAG, String.format("Item: %s\n Type: %s\n SKU: %s\n Price: %s\n Description: %s\n“, i.getTitle(), 
		                 //        i.getItemType(), i.getSku(), i.getPrice(), i.getDescription()));
		               }
		               break;
		 
		           case FAILED: // Fail gracefully on failed responses.
		               Logger.v(TAG, "ItemDataRequestStatus: FAILED");
		               
		               //update StoreService.isStoreOpen?
		               
		               break;
		        }
			      //redierct herer
			      if (appContext.getPlayer().getActiveGameId().length() > 0){
					 	((ApplicationContext)Splash.this.getApplication()).startNewActivity(Splash.this, Constants.ACTIVITY_CLASS_GAME_SURFACE);
		 
			       	}
			      	else{
					 	((ApplicationContext)Splash.this.getApplication()).startNewActivity(Splash.this, Constants.ACTIVITY_CLASS_MAIN);
			      	}
		   }
		   
		   public void onPurchaseResponse(final PurchaseResponse response) { }
		   
		   
		   public void onPurchaseUpdatesResponse(final PurchaseUpdatesResponse response) {  
			   Logger.d(TAG, "AmazonPurchaseObserver.onGetUserIdResponse starting status=" + response.getPurchaseUpdatesRequestStatus());
		        switch (response.getPurchaseUpdatesRequestStatus()) {
		            case SUCCESSFUL:
		                // Check for revoked SKUs
		                for (final String sku : response.getRevokedSkus()) {
		                    Logger.v(TAG, "Revoked Sku:" + sku);
		                    StoreService.clearPurchase(sku, this.currentUserID);
		                }  
		 
		                PlayerService.setPersistedOffset(response.getOffset(), this.currentUserID);
		                
		                // Process receipts
		                for (final Receipt receipt : response.getReceipts()) {
		                    switch (receipt.getItemType()) {
		                        case ENTITLED: // Re-entitle the customer 
		                        	StoreService.savePurchase(receipt.getSku(), receipt.getPurchaseToken(), this.currentUserID);
		                        break;
		                    }
		                }
		                
		                final Offset newOffset = response.getOffset();
		                if (response.isMore()) {
		                    Logger.v(TAG, "Initiating Another Purchase Updates with offset: "
		                  + newOffset.toString());
		                    PurchasingManager.initiatePurchaseUpdatesRequest(newOffset);
		                }
		                break;
		                 
		           case FAILED:
		                // Provide the user access to any previously persisted entitlements.
		                break;
		        }
		}
	}
}
	
 
