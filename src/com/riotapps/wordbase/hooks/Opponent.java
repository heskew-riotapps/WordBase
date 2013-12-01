package com.riotapps.wordbase.hooks;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;

import com.google.gson.annotations.SerializedName;
import com.riotapps.wordbase.R;
import com.riotapps.wordbase.utils.ApplicationContext;
import com.riotapps.wordbase.utils.Constants;
import com.riotapps.wordbase.utils.Logger;
 

public class Opponent{
	
	private static final String TAG = Opponent.class.getSimpleName();
	
	@SerializedName("id")
	private int id;
	
	@SerializedName("name")
	private String name;
 
	@SerializedName("imagePrefix")
	private String imagePrefix;
	
	@SerializedName("skillLevel")
	private int skillLevel;
	
	@SerializedName("opponentGroupId")
	private int opponentGroupId;
	private OpponentRecord record = null;
	private Bitmap smallImage = null;
	
	private int smallResourceId = 0;
	private int mainResourceId = 0;
	
	private OpponentGroup opponentGroup = null;
	
	public int getId() {
		return id;
	}
	public void setId(int id) {
		this.id = id;
	}
	public String getName() {
		return name;
	}
	public void setName(String name) {
		this.name = name;
	}
	public int getNumWins() {
		return this.getRecord().getNumWins();
	}
	 
	public int getNumLosses() {
		return this.getRecord().getNumLosses();
	}
	 
	public int getNumDraws() {
		return this.getRecord().getNumDraws();
	}
	 
	public String getImagePrefix() {
		return imagePrefix;
	}
	public void setImagePrefix(String imagePrefix) {
		this.imagePrefix = imagePrefix;
	}
	public int getSkillLevel() {
		return skillLevel;
	}
	public void setSkillLevel(int skillLevel) {
		this.skillLevel = skillLevel;
	}
	
	
	public int getOpponentGroupId() {
		return opponentGroupId;
	}
	public void setOpponentGroupId(int opponentGroupId) {
		this.opponentGroupId = opponentGroupId;
	}
	
	public OpponentRecord getRecord() {
		//store in local variable
		if (this.record == null) {
			this.record =  OpponentService.getOpponentRecord(this.id);
		}
		return this.record;
	}
	
 
	
	public OpponentGroup getOpponentGroup(){
		
		//store in local variable
		if (this.opponentGroup == null) {
			this.opponentGroup =  OpponentGroupService.getOpponentGroup(this.opponentGroupId);
		}
		return this.opponentGroup;
	
	}
	
//	public String getBadgeDrawable(){
//		return PlayerService.getBadgeDrawable(this.get);
//	}

	public String getDrawableByMode(String mode){
		return this.imagePrefix + Constants.UNDERSCORE + mode;
	}
	
	public String getSkillLevelText(Context context){	
		switch (this.skillLevel){
		case 1:
			return context.getString(R.string.skill_level_1);
		case 2:
			return context.getString(R.string.skill_level_2);
		case 3:
			return context.getString(R.string.skill_level_3);
		case 4:
			return context.getString(R.string.skill_level_4);
		case 5:
			return context.getString(R.string.skill_level_5);
		case 6:
			return context.getString(R.string.skill_level_6);
		}
		return Constants.EMPTY_STRING;
	}

	public String getSkillLevelTextLCase(Context context){	
		switch (this.skillLevel){
		case 1:
			return context.getString(R.string.skill_level_lcase_1);
		case 2:
			return context.getString(R.string.skill_level_lcase_2);
		case 3:
			return context.getString(R.string.skill_level_lcase_3);
		case 4:
			return context.getString(R.string.skill_level_lcase_4);
		case 5:
			return context.getString(R.string.skill_level_lcase_5);
		case 6:
			return context.getString(R.string.skill_level_lcase_6);
		}
		return Constants.EMPTY_STRING;
	}
	
	public Bitmap getSmallBitmap(){
		Logger.d(TAG, "getSmallBitmap id=" + this.id);
		if (this.smallImage == null){
			Logger.d(TAG, "getSmallBitmap smallImage is null");
		 this.preloadBitmaps();
		}
		
		return smallImage;
	
	}
	
	public int getSmallResourceId(){
		if (smallResourceId == 0){
			Context context = ApplicationContext.getAppContext();
			smallResourceId = context.getResources().getIdentifier(context.getString(R.string.namespace) + ":drawable/" + this.getDrawableByMode(Constants.OPPONENT_IMAGE_MODE_SMALL), null, null);  
		}
		return smallResourceId;
	}
	
	public int getMainResourceId(){
		if (mainResourceId == 0){
			Context context = ApplicationContext.getAppContext();
			mainResourceId = context.getResources().getIdentifier(context.getString(R.string.namespace) + ":drawable/" + this.getDrawableByMode(Constants.OPPONENT_IMAGE_MODE_MAIN), null, null);  
		}
		return mainResourceId;
	}
	
	public void preloadResourceIds(){
		int small = this.getSmallResourceId();
		int main = this.getMainResourceId();
	}
	
	public void preloadBitmaps(){
		if (this.smallImage == null){
		  Context context = ApplicationContext.getAppContext();
		  Logger.d(TAG, "preloadBitmaps id=" + this.id);
		  int opponentImageId = context.getResources().getIdentifier(context.getString(R.string.namespace) + ":drawable/" + this.getDrawableByMode(Constants.OPPONENT_IMAGE_MODE_SMALL), null, null);
		  Logger.d(TAG, "preloadBitmaps opponentImageId=" + opponentImageId);
		  this.smallImage = BitmapFactory.decodeResource(context.getResources(), opponentImageId);
		  Logger.d(TAG, "preloadBitmaps after decode");
		}
	}
	
	public void addLossToRecord(){
		this.getRecord().setNumLosses(this.getRecord().getNumLosses() + 1);
	}
	
	public void addWinToRecord(){
		this.getRecord().setNumWins(this.getRecord().getNumWins() + 1);
	}
	
	public void addDrawToRecord(){
		this.getRecord().setNumDraws(this.getRecord().getNumDraws() + 1);
	}
}
