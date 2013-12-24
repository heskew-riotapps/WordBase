package com.riotapps.wordbase.billing;

import java.util.Map;

import com.amazon.inapp.purchasing.BasePurchasingObserver;
import com.amazon.inapp.purchasing.GetUserIdResponse;
import com.amazon.inapp.purchasing.Item;
import com.amazon.inapp.purchasing.ItemDataResponse;
import com.amazon.inapp.purchasing.Offset;
import com.amazon.inapp.purchasing.PurchaseResponse;
import com.amazon.inapp.purchasing.PurchaseResponse.PurchaseRequestStatus;
import com.amazon.inapp.purchasing.PurchaseUpdatesResponse;
import com.amazon.inapp.purchasing.PurchasingManager;
import com.amazon.inapp.purchasing.Receipt;
import com.riotapps.wordbase.hooks.PlayerService;
import com.riotapps.wordbase.hooks.StoreService;
import com.riotapps.wordbase.utils.Logger;

import android.app.Activity;

public class AmazonPurchaseObserver extends BasePurchasingObserver {
	   private static final String TAG = "AmazonPurchaseObserver";
	   private boolean rvsProductionMode = false;
	   private String currentUserID = null;
	   
	   public AmazonPurchaseObserver(Activity iapActivity) {
	      super(iapActivity);
	   }
	 
	   public void onSdkAvailable(final boolean isSandboxMode) {
		   this.rvsProductionMode = isSandboxMode;
	   }
	   public void onGetUserIdResponse(final GetUserIdResponse response) {
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
	   }
	   
	   public void onPurchaseResponse(final PurchaseResponse response) { }
	   
	   
	   public void onPurchaseUpdatesResponse(final PurchaseUpdatesResponse response) {          
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