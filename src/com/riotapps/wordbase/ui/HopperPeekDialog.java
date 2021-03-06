package com.riotapps.wordbase.ui;

import java.util.List;

import com.google.analytics.tracking.android.EasyTracker;
import com.google.analytics.tracking.android.Tracker;
//import com.riotapps.wordbase.Main;
import com.riotapps.wordbase.R;
import com.riotapps.wordbase.hooks.AlphabetService;
import com.riotapps.wordbase.hooks.Game;
import com.riotapps.wordbase.hooks.GameService;
import com.riotapps.wordbase.hooks.Letter;
import com.riotapps.wordbase.hooks.PlayerService;
import com.riotapps.wordbase.hooks.StoreService;
import com.riotapps.wordbase.interfaces.ICloseDialog;
import com.riotapps.wordbase.utils.ApplicationContext;
import com.riotapps.wordbase.utils.Constants;
import com.riotapps.wordbase.utils.Logger;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TableLayout;
import android.widget.TextView;


public class HopperPeekDialog  extends AlertDialog implements View.OnClickListener{
	private static final String TAG = HopperPeekDialog.class.getSimpleName();
	
	private Game game;
	private Context parent;
	private View layout;
	private TextView peek_description;
private Tracker tracker;
	
	public Tracker getTracker() {
		if (this.tracker == null){
			this.tracker = EasyTracker.getTracker();
		}
		return tracker;
	}
	
	public HopperPeekDialog(Context context, String gameId){ //Game game){
		
		super(context);
		this.game = GameService.getGame(gameId);
		this.parent = context;
	
	//	WindowManager.LayoutParams params = new WindowManager.LayoutParams();
	//	params.horizontalMargin = 10f;
		
	//	getWindow().setAttributes(params);
	   getWindow().setBackgroundDrawable(new ColorDrawable(android.graphics.Color.TRANSPARENT));
		//getWindow().setBackgroundDrawable(null);
	}
	
	protected void onCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onCreate(savedInstanceState);
//		this.setContentView(BUTTON1);
      //  this.setProgressStyle(R.style.CustomProgressStyle);
		
	}

	@Override
	public void onStart() {
		// TODO Auto-generated method stub
		super.onStart();

		LayoutInflater inflater = getLayoutInflater();//(LayoutInflater) context.getSystemService(LAYOUT_INFLATER_SERVICE);
        this.layout = inflater.inflate(R.layout.hopperpeekdialog, null); 
//                                        (ViewGroup) findViewById(R.id.progress_root));
		
		 //loop through letters, filling the views
        
     //   this.setContentView(this.layout);
        this.setCanceledOnTouchOutside(false);
		LinearLayout llOK = (LinearLayout) this.layout.findViewById(R.id.llOK);
		TextView tvOK = (TextView) this.layout.findViewById(R.id.tvOK);
		TextView tvAlertTitle = (TextView) this.layout.findViewById(R.id.tvAlertTitle);

		Button bNoThanks = (Button)this.layout.findViewById(R.id.bNoThanks);
		Button bStore = (Button)this.layout.findViewById(R.id.bStore);
		
		
		tvOK.setTypeface(ApplicationContext.getMainFontTypeface());
		bNoThanks.setTypeface(ApplicationContext.getMainFontTypeface());
		bStore.setTypeface(ApplicationContext.getMainFontTypeface());
		tvAlertTitle.setTypeface(ApplicationContext.getMainFontTypeface());
		
		//Logger.d(TAG, "free hopper peeks=" + PlayerService.getRemainingFreeUsesHopperPeek());
		
		this.peek_description = (TextView)this.layout.findViewById(R.id.peek_description);
		this.peek_description.setTypeface(ApplicationContext.getMainFontTypeface());
		
		if (!StoreService.isHopperPeekPurchased(this.parent) && PlayerService.getRemainingFreeUsesHopperPeek() == 0){
		
			this.peek_description.setText(this.parent.getString(R.string.hopper_peek_purchase_offer));
			TableLayout tblLetters =  (TableLayout)this.layout.findViewById(R.id.tblLetters);
			tblLetters.setVisibility(View.GONE);
			tvOK.setVisibility(View.GONE);
		}
		else{
			this.peek_description.setText(String.format(this.parent.getString(R.string.gameboard_hopper_peek_dialog_description), String.valueOf(this.game.getTotalNumLetterCountLeftInHopperAndOpponentTray()), this.game.getOpponent().getName()));
			
			this.loadLetters();	
			
			if (!StoreService.isHopperPeekPurchased(this.parent)){
				tvOK.setVisibility(View.GONE);
			
			
				int remainingFreeUses  = PlayerService.removeAFreeUseFromHopperPeek();
				if (remainingFreeUses > 1){
					this.peek_description.setText(String.format(this.parent.getString(R.string.hopper_peek_preview), this.peek_description.getText(), String.valueOf(remainingFreeUses)));
				}
				else if (remainingFreeUses == 1){
					this.peek_description.setText(String.format(this.parent.getString(R.string.hopper_peek_1_preview_left), this.peek_description.getText()));					
				}
				else{
					this.peek_description.setText(String.format(this.parent.getString(R.string.hopper_peek_no_previews_left), this.peek_description.getText()));					
				}
			}
			else{
				
				llOK.setOnClickListener(this);
				bStore.setVisibility(View.GONE);
				bNoThanks.setVisibility(View.GONE);
			}
		}
		 

		ImageView close = (ImageView) this.layout.findViewById(R.id.img_close);
		//if button is clicked, close the custom dialog
		close.setOnClickListener(new View.OnClickListener() {
	 		@Override
			public void onClick(View v) {
	 			
				HopperPeekDialog.this.close();
			}
		});
		
		this.setOnCancelListener(new DialogInterface.OnCancelListener() {
			@Override
			public void onCancel(DialogInterface dialog) {
 
				HopperPeekDialog.this.close();
				
			}
		});
		
		//LayoutParams params = new LayoutParams();
		//params.horizontalMargin = 10f;
		 if (bNoThanks.getVisibility() == View.VISIBLE){
    		 bStore.setOnClickListener(this);
		     bNoThanks.setOnClickListener(this);
    	 }
		 
		this.setContentView(this.layout);
		 
		this.trackEvent(Constants.TRACKER_ACTION_HOPPER_PEEK, String.valueOf(this.game.getTurn()), this.game.getHopper().size());
	     
	 }
			
	private void trackEvent(String action, String label, long value){
		try{
			this.getTracker().sendEvent(Constants.TRACKER_CATEGORY_HOPPER_PEEK, action,label, value);
		}
		catch (Exception e){
  			Logger.d(TAG, "trackEvent action=" + (action == null ? "null" : action) 
  					 + " label=" + (label == null ? "null" : label)  + " value=" + value +" e=" + e.toString());
  			
		}
	}
		
	
	@Override
	public void dismiss(){
		
		this.game = null;
 		this.layout = null;
		this.peek_description = null;
		this.tracker = null;
			
		super.dismiss();	
	}

	private void close(){
		this.dismiss();
		this.trackEvent(Constants.TRACKER_ACTION_HOPPER_PEEK, Constants.TRACKER_LABEL_HOPPER_PEEK_CLOSE, Constants.TRACKER_DEFAULT_OPTION_VALUE);
		((ICloseDialog)this.parent).dialogClose(Constants.RETURN_CODE_HOPPER_PEEK_CLOSE);
	}
	
	 
	@Override
	public void onClick(View v) {
		if (v.getId() == R.id.llOK) {
			this.close();
		} else if (v.getId() == R.id.bStore) {
			this.dismiss();
			
			//Intent intent = new Intent(this.parent, com.riotapps.wordbase.Store.class);
			this.trackEvent(Constants.TRACKER_ACTION_HOPPER_PEEK, Constants.TRACKER_LABEL_HOPPER_PEEK_GO_TO_STORE, Constants.TRACKER_DEFAULT_OPTION_VALUE);
			((ApplicationContext)((Activity) parent).getApplication()).startNewActivity(parent, Constants.ACTIVITY_CLASS_STORE);
			
			//	this.parent.startActivity(intent);
		} else if (v.getId() == R.id.bNoThanks) {
			this.dismiss();
			this.trackEvent(Constants.TRACKER_ACTION_HOPPER_PEEK, Constants.TRACKER_LABEL_HOPPER_PEEK_DECLINE_STORE, Constants.TRACKER_DEFAULT_OPTION_VALUE);
			((ICloseDialog)this.parent).dialogClose(Constants.RETURN_CODE_HOPPER_PEEK_CLOSE);
		} else {
			this.close();
		}
	}
	
	private void loadLetters(){
		List<Letter> letters = AlphabetService.getLetters();
		
		TextView tvLetter1 = (TextView)this.layout.findViewById(R.id.tvLetter1);
		TextView tvLetter2 = (TextView)this.layout.findViewById(R.id.tvLetter2);
		TextView tvLetter3 = (TextView)this.layout.findViewById(R.id.tvLetter3);
		TextView tvLetter4 = (TextView)this.layout.findViewById(R.id.tvLetter4);
		TextView tvLetter5 = (TextView)this.layout.findViewById(R.id.tvLetter5);
		TextView tvLetter6 = (TextView)this.layout.findViewById(R.id.tvLetter6);
		TextView tvLetter7 = (TextView)this.layout.findViewById(R.id.tvLetter7);
		TextView tvLetter8 = (TextView)this.layout.findViewById(R.id.tvLetter8);
		TextView tvLetter9 = (TextView)this.layout.findViewById(R.id.tvLetter9);
		TextView tvLetter10 = (TextView)this.layout.findViewById(R.id.tvLetter10);
		TextView tvLetter11 = (TextView)this.layout.findViewById(R.id.tvLetter11);
		TextView tvLetter12 = (TextView)this.layout.findViewById(R.id.tvLetter12);
		TextView tvLetter13 = (TextView)this.layout.findViewById(R.id.tvLetter13);
		TextView tvLetter14 = (TextView)this.layout.findViewById(R.id.tvLetter14);
		TextView tvLetter15 = (TextView)this.layout.findViewById(R.id.tvLetter15);
		TextView tvLetter16 = (TextView)this.layout.findViewById(R.id.tvLetter16);
		TextView tvLetter17 = (TextView)this.layout.findViewById(R.id.tvLetter17);
		TextView tvLetter18 = (TextView)this.layout.findViewById(R.id.tvLetter18);
		TextView tvLetter19 = (TextView)this.layout.findViewById(R.id.tvLetter19);
		TextView tvLetter20 = (TextView)this.layout.findViewById(R.id.tvLetter20);
		TextView tvLetter21 = (TextView)this.layout.findViewById(R.id.tvLetter21);
		TextView tvLetter22 = (TextView)this.layout.findViewById(R.id.tvLetter22);
		TextView tvLetter23 = (TextView)this.layout.findViewById(R.id.tvLetter23);
		TextView tvLetter24 = (TextView)this.layout.findViewById(R.id.tvLetter24);
		TextView tvLetter25 = (TextView)this.layout.findViewById(R.id.tvLetter25);
		TextView tvLetter26 = (TextView)this.layout.findViewById(R.id.tvLetter26);
		this.peek_description = (TextView)this.layout.findViewById(R.id.peek_description);
		
		tvLetter1.setTypeface(ApplicationContext.getMainFontTypeface());
		tvLetter2.setTypeface(ApplicationContext.getMainFontTypeface());
		tvLetter3.setTypeface(ApplicationContext.getMainFontTypeface());
		tvLetter4.setTypeface(ApplicationContext.getMainFontTypeface());
		tvLetter5.setTypeface(ApplicationContext.getMainFontTypeface());
		tvLetter6.setTypeface(ApplicationContext.getMainFontTypeface());
		tvLetter7.setTypeface(ApplicationContext.getMainFontTypeface());
		tvLetter8.setTypeface(ApplicationContext.getMainFontTypeface());
		tvLetter9.setTypeface(ApplicationContext.getMainFontTypeface());
		tvLetter10.setTypeface(ApplicationContext.getMainFontTypeface());
		tvLetter11.setTypeface(ApplicationContext.getMainFontTypeface());
		tvLetter12.setTypeface(ApplicationContext.getMainFontTypeface());
		tvLetter13.setTypeface(ApplicationContext.getMainFontTypeface());
		tvLetter14.setTypeface(ApplicationContext.getMainFontTypeface());
		tvLetter15.setTypeface(ApplicationContext.getMainFontTypeface());
		tvLetter16.setTypeface(ApplicationContext.getMainFontTypeface());
		tvLetter17.setTypeface(ApplicationContext.getMainFontTypeface());
		tvLetter18.setTypeface(ApplicationContext.getMainFontTypeface());
		tvLetter19.setTypeface(ApplicationContext.getMainFontTypeface());
		tvLetter20.setTypeface(ApplicationContext.getMainFontTypeface());
		tvLetter21.setTypeface(ApplicationContext.getMainFontTypeface());
		tvLetter22.setTypeface(ApplicationContext.getMainFontTypeface());
		tvLetter23.setTypeface(ApplicationContext.getMainFontTypeface());
		tvLetter24.setTypeface(ApplicationContext.getMainFontTypeface());
		tvLetter25.setTypeface(ApplicationContext.getMainFontTypeface());
		tvLetter26.setTypeface(ApplicationContext.getMainFontTypeface());
		
		String baseText = parent.getString(R.string.hopper_peek_letter);
		
		tvLetter1.setText(String.format(baseText, letters.get(0).getCharacter(), this.game.getNumLettersLeftInHopperAndOpponentTray(letters.get(0).getCharacter())));
		tvLetter2.setText(String.format(baseText, letters.get(1).getCharacter(), this.game.getNumLettersLeftInHopperAndOpponentTray(letters.get(1).getCharacter())));
		tvLetter3.setText(String.format(baseText, letters.get(2).getCharacter(), this.game.getNumLettersLeftInHopperAndOpponentTray(letters.get(2).getCharacter())));
		tvLetter4.setText(String.format(baseText, letters.get(3).getCharacter(), this.game.getNumLettersLeftInHopperAndOpponentTray(letters.get(3).getCharacter())));
		tvLetter5.setText(String.format(baseText, letters.get(4).getCharacter(), this.game.getNumLettersLeftInHopperAndOpponentTray(letters.get(4).getCharacter())));
		tvLetter6.setText(String.format(baseText, letters.get(5).getCharacter(), this.game.getNumLettersLeftInHopperAndOpponentTray(letters.get(5).getCharacter())));
		tvLetter7.setText(String.format(baseText, letters.get(6).getCharacter(), this.game.getNumLettersLeftInHopperAndOpponentTray(letters.get(6).getCharacter())));
		tvLetter8.setText(String.format(baseText, letters.get(7).getCharacter(), this.game.getNumLettersLeftInHopperAndOpponentTray(letters.get(7).getCharacter())));
		tvLetter9.setText(String.format(baseText, letters.get(8).getCharacter(), this.game.getNumLettersLeftInHopperAndOpponentTray(letters.get(8).getCharacter())));
		tvLetter10.setText(String.format(baseText, letters.get(9).getCharacter(), this.game.getNumLettersLeftInHopperAndOpponentTray(letters.get(9).getCharacter())));
		tvLetter11.setText(String.format(baseText, letters.get(10).getCharacter(), this.game.getNumLettersLeftInHopperAndOpponentTray(letters.get(10).getCharacter())));
		tvLetter12.setText(String.format(baseText, letters.get(11).getCharacter(), this.game.getNumLettersLeftInHopperAndOpponentTray(letters.get(11).getCharacter())));
		tvLetter13.setText(String.format(baseText, letters.get(12).getCharacter(), this.game.getNumLettersLeftInHopperAndOpponentTray(letters.get(12).getCharacter())));
		tvLetter14.setText(String.format(baseText, letters.get(13).getCharacter(), this.game.getNumLettersLeftInHopperAndOpponentTray(letters.get(13).getCharacter())));
		tvLetter15.setText(String.format(baseText, letters.get(14).getCharacter(), this.game.getNumLettersLeftInHopperAndOpponentTray(letters.get(14).getCharacter())));
		tvLetter16.setText(String.format(baseText, letters.get(15).getCharacter(), this.game.getNumLettersLeftInHopperAndOpponentTray(letters.get(15).getCharacter())));
		tvLetter17.setText(String.format(baseText, letters.get(16).getCharacter(), this.game.getNumLettersLeftInHopperAndOpponentTray(letters.get(16).getCharacter())));
		tvLetter18.setText(String.format(baseText, letters.get(17).getCharacter(), this.game.getNumLettersLeftInHopperAndOpponentTray(letters.get(17).getCharacter())));
		tvLetter19.setText(String.format(baseText, letters.get(18).getCharacter(), this.game.getNumLettersLeftInHopperAndOpponentTray(letters.get(18).getCharacter())));
		tvLetter20.setText(String.format(baseText, letters.get(19).getCharacter(), this.game.getNumLettersLeftInHopperAndOpponentTray(letters.get(19).getCharacter())));
		tvLetter21.setText(String.format(baseText, letters.get(20).getCharacter(), this.game.getNumLettersLeftInHopperAndOpponentTray(letters.get(20).getCharacter())));
		tvLetter22.setText(String.format(baseText, letters.get(21).getCharacter(), this.game.getNumLettersLeftInHopperAndOpponentTray(letters.get(21).getCharacter())));
		tvLetter23.setText(String.format(baseText, letters.get(22).getCharacter(), this.game.getNumLettersLeftInHopperAndOpponentTray(letters.get(22).getCharacter())));
		tvLetter24.setText(String.format(baseText, letters.get(23).getCharacter(), this.game.getNumLettersLeftInHopperAndOpponentTray(letters.get(23).getCharacter())));
		tvLetter25.setText(String.format(baseText, letters.get(24).getCharacter(), this.game.getNumLettersLeftInHopperAndOpponentTray(letters.get(24).getCharacter())));
		tvLetter26.setText(String.format(baseText, letters.get(25).getCharacter(), this.game.getNumLettersLeftInHopperAndOpponentTray(letters.get(25).getCharacter())));
		 		
	}
	
	
	
}
