package com.riotapps.wordbase;

 
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import com.google.ads.AdRequest;
import com.google.ads.AdView;
import com.google.analytics.tracking.android.EasyTracker;
import com.riotapps.wordbase.hooks.Game;
import com.riotapps.wordbase.hooks.GameService;
import com.riotapps.wordbase.hooks.PlayedWord;
import com.riotapps.wordbase.hooks.Player;
import com.riotapps.wordbase.hooks.StoreService;
import com.riotapps.wordbase.ui.MenuUtils;
import com.riotapps.wordbase.utils.ApplicationContext;
import com.riotapps.wordbase.utils.Constants;
import com.riotapps.wordbase.utils.IntentExtra;
import com.riotapps.wordbase.utils.Logger;

import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Bundle;
import android.support.v4.app.FragmentActivity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.AdapterView.OnItemClickListener;

public class GameHistory extends FragmentActivity{
	private static final String TAG = GameHistory.class.getSimpleName();
	private Game game;
	private Player player;
 
	private ListView lvWords;
	private Context context = this;
	private int opponentImageId;
 	  Bitmap playerBM = null;
 	  Bitmap opponentBM = null;

	ApplicationContext appContext;
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onCreate(savedInstanceState);
		setContentView(R.layout.gamehistory);

	 	Intent i = getIntent();
	 	String gameId = i.getStringExtra(Constants.EXTRA_GAME_ID);
	 	this.game = GameService.getGame(gameId); 
	 	
		this.appContext = (ApplicationContext)this.getApplicationContext(); 
		
	    this.player = this.appContext.getPlayer();
	 	GameService.loadScoreboard(this, this.game);
	 	
	  	
	    this.opponentImageId = context.getResources().getIdentifier(this.getString(R.string.namespace) + ":drawable/" + this.game.getOpponent().getDrawableByMode(Constants.OPPONENT_IMAGE_MODE_SMALL), null, null);
	    	
	    this.opponentBM = BitmapFactory.decodeResource(getResources(), this.opponentImageId); 
	    this.playerBM = BitmapFactory.decodeResource(getResources(), R.drawable.you);
	     
	 	this.loadList();   
	 	MenuUtils.hideMenu(this);
	 	this.setupFonts();
	 	
	 	AdView adView = (AdView)this.findViewById(R.id.adView);
    	if (StoreService.isHideBannerAdsPurchased(this)){	
			adView.setVisibility(View.GONE);
		}
    	else {
    		adView.loadAd(new AdRequest());
    	}
	}

@Override
	public void onBackPressed() {
		// TODO Auto-generated method stub
		
		playerBM = null;
		opponentBM = null;
		lvWords = null;
		super.onBackPressed();
	}

@Override
protected void onStart() {
	 
	super.onStart();
	 EasyTracker.getInstance().activityStart(this);
}


@Override
protected void onStop() {
 
	super.onStop();
	EasyTracker.getInstance().activityStop(this);
}

private void loadList(){
 
	
	Collections.reverse(this.game.getPlayedWords());
 
	PlayedWordArrayAdapter adapter = new PlayedWordArrayAdapter(this, this.game.getPlayedWords().toArray(new PlayedWord[this.game.getPlayedWords().size()]));

	this.lvWords = (ListView) findViewById(R.id.lvWords);
	this.lvWords.setAdapter(adapter); 
	
 
	lvWords.setOnItemClickListener(new OnItemClickListener() {
        @Override
        public void onItemClick(AdapterView<?> parent, View view, int position,
                long id) {
        	
        //	final WordLookupDialog dialog = new WordLookupDialog(GameHistory.this, view.getTag().toString());
	       	 
	    //	dialog.show();
        	
        	//this will eventually call wordnik for definition lookup
        	List<IntentExtra> extras = new ArrayList<IntentExtra>();
        	extras.add(new IntentExtra(Constants.EXTRA_GAME_ID, game.getId(), String.class));	
        	extras.add(new IntentExtra(Constants.EXTRA_WORD_LOOKUP, view.getTag().toString(), String.class));	
			((ApplicationContext)GameHistory.this.getApplication()).startNewActivity(GameHistory.this, Constants.ACTIVITY_CLASS_GAME_LOOKUP, extras);
        	
         	//Intent intent = new Intent(context, GameLookup.class);
        	//intent.putExtra(Constants.EXTRA_GAME_ID, game.getId());
        	//intent.putExtra(Constants.EXTRA_WORD_LOOKUP, view.getTag().toString());
			//startActivity(intent);
        }
    });

}

	private void setupFonts(){
		TextView header = (TextView)findViewById(R.id.header);	
		header.setTypeface(ApplicationContext.getMainFontTypeface());
	}

	private class PlayedWordArrayAdapter extends ArrayAdapter<PlayedWord> {
   	  private final GameHistory context;
   	  private final PlayedWord[] values;
   	  private final int wordCount;
   	  LayoutInflater inflater;

   	  
   	//  public ArrayList<Integer> selectedIds = new ArrayList<Integer>();

   	  public PlayedWordArrayAdapter(GameHistory context, PlayedWord[] values) {
   	    super(context, R.layout.gamehistoryitem, values);
    	    this.context = context;
    	    this.values = values;
    	    this.wordCount = values.length;
    	    
    	    this.inflater = (LayoutInflater) context
	    	        .getSystemService(Context.LAYOUT_INFLATER_SERVICE);
    	  }

    	  @Override
    	  public View getView(int position, View rowView, ViewGroup parent) {
    		 
    		  if ( rowView == null ) {
    			  rowView = inflater.inflate(R.layout.gamehistoryitem, parent, false);
    		  }
    		  
	    	   PlayedWord word = values[position];
	    	 
	    	   TextView tvWord = (TextView) rowView.findViewById(R.id.tvWord);
	    	   TextView tvTurnInfo = (TextView) rowView.findViewById(R.id.tvTurnInfo);
	    	   
	    	   tvWord.setTypeface(ApplicationContext.getScoreboardFontTypeface());
	    	   tvTurnInfo.setTypeface(ApplicationContext.getScoreboardFontTypeface());
 
	     	   ImageView ivPlayer = (ImageView)rowView.findViewById(R.id.ivPlayer);
	    //	   imageLoader.loadImage(player.getImageUrl(), ivPlayer); 
	    	   
	    	   String name = word.isOpponentPlay() ? game.getOpponent().getName() : player.getName(context);
	    	   
	    	   if ( word.isOpponentPlay()){
	    		   ivPlayer.setImageBitmap(GameHistory.this.opponentBM);
	    		   //ivPlayer.setImageResource(GameHistory.this.opponentImageId);
	    	   }
	    	   else{
	    		   ivPlayer.setImageBitmap(GameHistory.this.playerBM);
	    	   }
	    	   
	    	  
	    	   tvWord.setText(word.getWord());
	    	   tvTurnInfo.setText(String.format(this.context.getString(R.string.game_history_turn_info), name, word.getTurn(), word.getPointsScored()));
 
	    	   Logger.d(TAG, "adapter position=" + position + " count=" + this.wordCount); 
	     	   
	    	   rowView.setTag(word.getWord());
	    	   return rowView;
    	  } 
		} 		  
 
    }