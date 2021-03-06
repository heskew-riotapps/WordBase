package com.riotapps.wordbase.utils;
 
import java.util.ArrayList;
import java.util.List;

import com.riotapps.wordbase.hooks.Opponent;
import com.riotapps.wordbase.hooks.OpponentService;
import com.riotapps.wordbase.hooks.Player;
import com.riotapps.wordbase.hooks.PlayerService;
import android.app.Application;
import android.content.Context;
import android.content.Intent;
import android.graphics.Typeface;

public class ApplicationContext extends Application{
	private static final String TAG = ApplicationContext.class.getSimpleName();
    private static Context context;
  //  private WordService wordService;
    private List<Opponent> opponents = null;
    /*
    private Bitmap bgPlacedTileFull;
	private Bitmap bgPlacedTileZoomed;
	private Bitmap bgPlayedTileFull;
	private Bitmap bgPlayedTileZoomed;
	private Bitmap bgLastPlayedTileFull;
	private Bitmap bgLastPlayedTileZoomed;
	
	private Bitmap bgBaseScaled = null;
	private Bitmap bgBaseZoomed = null;
	private Bitmap bg4LScaled = null;
	private Bitmap bg4LZoomed = null;
	private Bitmap bg3LScaled = null;
	private Bitmap bg3LZoomed = null;
	private Bitmap bg3WScaled = null;
	private Bitmap bg3WZoomed = null;
	private Bitmap bg2LScaled = null;
	private Bitmap bg2LZoomed = null; 
	private Bitmap bg2WScaled = null;
	private Bitmap bg2WZoomed = null;	
	private Bitmap bgStarterScaled = null;
	private Bitmap bgStarterZoomed = null;
	private Bitmap bgTrayBaseScaled = null;
	private Bitmap bgTrayEmptyScaled = null;
	private Bitmap bgTrayBaseDragging = null;
	private Bitmap bgTrayBackground = null;
	*/
	
	private static Typeface mainFontTypeface;
	private static Typeface scoreboardFontTypeface;
	private static Typeface scoreboardButtonFontTypeface;
	private static Typeface bonusTypeface;
	private static Typeface letterTypeface; 
	private static Typeface letterValueTypeface; 
	
	//private Playtomic playtomic = null;
	
	private Player player = null;
	private boolean bannerAdHidden = false;
	
	public static long runningTime = 0;
	public static long captureTime = 0;

	public static Typeface getBonusTypeface(){
		if (bonusTypeface == null){
			bonusTypeface = Typeface.createFromAsset(context.getAssets(), Constants.GAME_BOARD_FONT);
		}
		
		return bonusTypeface;
	}
	
	public static Typeface getLetterTypeface(){
		if (letterTypeface == null){
			letterTypeface = Typeface.createFromAsset(context.getAssets(), Constants.GAME_LETTER_FONT);
		}
		
		return letterTypeface;
	}
	public static Typeface getLetterValueTypeface(){
		if (letterValueTypeface == null){
			letterValueTypeface = Typeface.createFromAsset(context.getAssets(), Constants.GAME_LETTER_VALUE_FONT);
		}
		
		return letterValueTypeface;
	}
	
	public static Typeface getMainFontTypeface(){
		if (mainFontTypeface == null){
			mainFontTypeface = Typeface.createFromAsset(context.getAssets(), Constants.MAIN_FONT);
		}
		
		return mainFontTypeface;
	}
	
	public static Typeface getScoreboardFontTypeface(){
		if (scoreboardFontTypeface == null){
			scoreboardFontTypeface = Typeface.createFromAsset(context.getAssets(), Constants.SCOREBOARD_FONT);
		}
		
		return scoreboardFontTypeface;
	}
	
	public static Typeface getScoreboardButtonFontTypeface(){
		if (scoreboardButtonFontTypeface == null){
			scoreboardButtonFontTypeface = Typeface.createFromAsset(context.getAssets(), Constants.SCOREBOARD_BUTTON_FONT);
		}
		
		return scoreboardButtonFontTypeface;
	}
	
	public static void captureTime(String TAG, String text){
	     captureTime = System.nanoTime();
	     Logger.d(TAG, String.format("%1$s - time since last capture=%2$s", text, Utils.convertNanosecondsToMilliseconds(captureTime - runningTime)));
	     runningTime = captureTime;
	}

    public Player getPlayer() {
    	if (player == null){
    		player = PlayerService.getPlayer();
    	}
		return player;
	}

	public void setPlayer(Player player) {
		this.player = player;
	}

	
	
	public boolean isBannerAdHidden() {
		return bannerAdHidden;
	}

	public void setBannerAdsHidden(boolean bannerAdHidden) {
		this.bannerAdHidden = bannerAdHidden;
	}

	public void onCreate(){
        super.onCreate();
        ApplicationContext.context = getApplicationContext();
     //   this.wordService = new WordService();
    /*    try {
			playtomic = Playtomic.getInstance(
					this.getResources().getInteger(R.integer.playtomics_game_id), 
			        this.getString(R.string.playtomics_guid),
			        this.getString(R.string.playtomics_api_key),
			        this);
		} catch (NotFoundException e) {
			// TODO Auto-generated catch block
			Logger.d(TAG, "NotFoundException="  + e.toString());
		} catch (Exception e) {
			// TODO Auto-generated catch block
			Logger.d(TAG, "Exception="  + e.toString());		}
        Playtomic.Log().view();
        */
       // new Thread(new Runnable() {
       //     public void run() {
       //     	wordService.loadAll();
       //     }
       // }).start();
    }

    @Override
	public void onTerminate() {
		// TODO Auto-generated method stub
		super.onTerminate();
	}

	public static Context getAppContext() {
        return ApplicationContext.context;
    }
/*
	public WordService getWordService() {
		return wordService;
	}

	public void setWordService(WordService wordService) {
		this.wordService = wordService;
	}
	*/
/*
	public Bitmap getBgPlacedTileFull() {
		return bgPlacedTileFull;
	}

	public void setBgPlacedTileFull(Bitmap bgPlacedTileFull) {
		this.bgPlacedTileFull = bgPlacedTileFull;
	}

	public Bitmap getBgPlacedTileZoomed() {
		return bgPlacedTileZoomed;
	}

	public void setBgPlacedTileZoomed(Bitmap bgPlacedTileZoomed) {
		this.bgPlacedTileZoomed = bgPlacedTileZoomed;
	}

	public Bitmap getBgPlayedTileFull() {
		return bgPlayedTileFull;
	}

	public void setBgPlayedTileFull(Bitmap bgPlayedTileFull) {
		this.bgPlayedTileFull = bgPlayedTileFull;
	}

	public Bitmap getBgPlayedTileZoomed() {
		return bgPlayedTileZoomed;
	}

	public void setBgPlayedTileZoomed(Bitmap bgPlayedTileZoomed) {
		this.bgPlayedTileZoomed = bgPlayedTileZoomed;
	}

	public Bitmap getBgLastPlayedTileFull() {
		return bgLastPlayedTileFull;
	}

	public void setBgLastPlayedTileFull(Bitmap bgLastPlayedTileFull) {
		this.bgLastPlayedTileFull = bgLastPlayedTileFull;
	}

	public Bitmap getBgLastPlayedTileZoomed() {
		return bgLastPlayedTileZoomed;
	}

	public void setBgLastPlayedTileZoomed(Bitmap bgLastPlayedTileZoomed) {
		this.bgLastPlayedTileZoomed = bgLastPlayedTileZoomed;
	}

	public Bitmap getBgBaseScaled() {
		return bgBaseScaled;
	}

	public void setBgBaseScaled(Bitmap bgBaseScaled) {
		this.bgBaseScaled = bgBaseScaled;
	}

	public Bitmap getBgBaseZoomed() {
		return bgBaseZoomed;
	}

	public void setBgBaseZoomed(Bitmap bgBaseZoomed) {
		this.bgBaseZoomed = bgBaseZoomed;
	}

	public Bitmap getBg4LScaled() {
		return bg4LScaled;
	}

	public void setBg4LScaled(Bitmap bg4lScaled) {
		bg4LScaled = bg4lScaled;
	}

	public Bitmap getBg4LZoomed() {
		return bg4LZoomed;
	}

	public void setBg4LZoomed(Bitmap bg4lZoomed) {
		bg4LZoomed = bg4lZoomed;
	}

	public Bitmap getBg3LScaled() {
		return bg3LScaled;
	}

	public void setBg3LScaled(Bitmap bg3lScaled) {
		bg3LScaled = bg3lScaled;
	}

	public Bitmap getBg3LZoomed() {
		return bg3LZoomed;
	}

	public void setBg3LZoomed(Bitmap bg3lZoomed) {
		bg3LZoomed = bg3lZoomed;
	}

	public Bitmap getBg3WScaled() {
		return bg3WScaled;
	}

	public void setBg3WScaled(Bitmap bg3wScaled) {
		bg3WScaled = bg3wScaled;
	}

	public Bitmap getBg3WZoomed() {
		return bg3WZoomed;
	}

	public void setBg3WZoomed(Bitmap bg3wZoomed) {
		bg3WZoomed = bg3wZoomed;
	}

	public Bitmap getBg2LScaled() {
		return bg2LScaled;
	}

	public void setBg2LScaled(Bitmap bg2lScaled) {
		bg2LScaled = bg2lScaled;
	}

	public Bitmap getBg2LZoomed() {
		return bg2LZoomed;
	}

	public void setBg2LZoomed(Bitmap bg2lZoomed) {
		bg2LZoomed = bg2lZoomed;
	}

	public Bitmap getBg2WScaled() {
		return bg2WScaled;
	}

	public void setBg2WScaled(Bitmap bg2wScaled) {
		bg2WScaled = bg2wScaled;
	}

	public Bitmap getBg2WZoomed() {
		return bg2WZoomed;
	}

	public void setBg2WZoomed(Bitmap bg2wZoomed) {
		bg2WZoomed = bg2wZoomed;
	}

	public Bitmap getBgStarterScaled() {
		return bgStarterScaled;
	}

	public void setBgStarterScaled(Bitmap bgStarterScaled) {
		this.bgStarterScaled = bgStarterScaled;
	}

	public Bitmap getBgStarterZoomed() {
		return bgStarterZoomed;
	}

	public void setBgStarterZoomed(Bitmap bgStarterZoomed) {
		this.bgStarterZoomed = bgStarterZoomed;
	}

	public Bitmap getBgTrayBaseScaled() {
		return bgTrayBaseScaled;
	}

	public void setBgTrayBaseScaled(Bitmap bgTrayBaseScaled) {
		this.bgTrayBaseScaled = bgTrayBaseScaled;
	}

	public Bitmap getBgTrayEmptyScaled() {
		return bgTrayEmptyScaled;
	}

	public void setBgTrayEmptyScaled(Bitmap bgTrayEmptyScaled) {
		this.bgTrayEmptyScaled = bgTrayEmptyScaled;
	}

	public Bitmap getBgTrayBaseDragging() {
		return bgTrayBaseDragging;
	}

	public void setBgTrayBaseDragging(Bitmap bgTrayBaseDragging) {
		this.bgTrayBaseDragging = bgTrayBaseDragging;
	}

	public Bitmap getBgTrayBackground() {
		return bgTrayBackground;
	}

	public void setBgTrayBackground(Bitmap bgTrayBackground) {
		this.bgTrayBackground = bgTrayBackground;
	}
*/

	public List<Opponent> getOpponents() {
		Logger.d(TAG, "getOpponents called");
		if (this.opponents == null){
			
			this.opponents = OpponentService.getActivatedOpponents();
		}
		return opponents;
	}

	public void setOpponents(List<Opponent> opponents) {
		this.opponents = opponents;
	}
	
//	public void closeApp(){
//		Intent startMain = new Intent(Intent.ACTION_MAIN);
//		startMain.addCategory(Intent.CATEGORY_HOME);
//		startMain.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
//		startActivity(startMain);
//	}
	
	public void startNewActivity(Context context, String activity){
		startNewActivity(context, activity, new ArrayList<IntentExtra>());
	}
	
	public void startNewActivity(Context context, String activity, List<IntentExtra> extras){
 		if (activity.equals(Constants.ACTIVITY_CLASS_ABOUT)){
 			openNewActivity(context, com.riotapps.wordbase.About.class, extras);
 		}
		else if (activity.equals(Constants.ACTIVITY_CLASS_COMPLETED_GAMES)){
 			openNewActivity(context, com.riotapps.wordbase.CompletedGames.class, extras);
		}
		else if (activity.equals(Constants.ACTIVITY_CLASS_FULL_RULES)){
 			openNewActivity(context, com.riotapps.wordbase.FullRules.class, extras);
		}
		else if (activity.equals(Constants.ACTIVITY_CLASS_GAME_HISTORY)){
 			openNewActivity(context, com.riotapps.wordbase.GameHistory.class, extras);
		}
		else if (activity.equals(Constants.ACTIVITY_CLASS_GAME_LOOKUP)){
 			openNewActivity(context, com.riotapps.wordbase.GameLookup.class, extras);
		}
		else if (activity.equals(Constants.ACTIVITY_CLASS_GAME_SURFACE)){
 			openNewActivity(context, com.riotapps.wordbase.GameSurface.class, extras);
		}
		else if (activity.equals(Constants.ACTIVITY_CLASS_MAIN)){
 			openNewActivity(context, com.riotapps.wordbase.Main.class, extras);
		}
		else if (activity.equals(Constants.ACTIVITY_CLASS_SPLASH)){
 			openNewActivity(context, com.riotapps.wordbase.Splash.class, extras);

		}
		else if (activity.equals(Constants.ACTIVITY_CLASS_STORE)){
 			openNewActivity(context, com.riotapps.wordbase.Store.class, extras);
		}
	}
	
	private void openNewActivity(Context context, Class<?> cls, List<IntentExtra> extras){
		
		Intent intent = new Intent(context, com.riotapps.wordbase.Store.class);
		for (IntentExtra extra : extras){
			if (extra.getType().equals(String.class)){
				intent.putExtra(extra.getName(), (String)extra.getValue());
			}
			else if (extra.getType().equals(Boolean.class)){
				intent.putExtra(extra.getName(), (Boolean)extra.getValue());
			}
			else if (extra.getType().equals(Integer.class)){
				intent.putExtra(extra.getName(), (Integer)extra.getValue());
			}
		}
		
		context.startActivity(intent); 
	}
    
    
}
