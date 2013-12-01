package com.riotapps.wordbase.hooks;

import java.util.Date;

import android.content.Context;

import com.google.gson.annotations.SerializedName;
import com.riotapps.wordbase.R;
import com.riotapps.wordbase.utils.Logger;
import com.riotapps.wordbase.utils.Utils;
 

public class CompletedGame {
	@SerializedName("id")
	private String id = "";
	
	@SerializedName("o_n")	
	private String opponentName;
	
	@SerializedName("o_sl")	
	private String opponentSkillLevel;

	@SerializedName("o_sc")	
	private int opponentScore;
	
	@SerializedName("o_ri")	
	private int opponentImageResourceId;

	@SerializedName("p_sc")	
	private int playerScore;

	@SerializedName("cd")	
	private Date completedDate;

	@SerializedName("t")	
	private String gameType;

	public String getId() {
		return id;
	}

	public void setId(String id) {
		this.id = id;
	}

	public String getOpponentName() {
		return opponentName;
	}

	public void setOpponentName(String opponentName) {
		this.opponentName = opponentName;
	}

	public String getOpponentSkillLevel() {
		return opponentSkillLevel;
	}

	public void setOpponentSkillLevel(String opponentSkillLevel) {
		this.opponentSkillLevel = opponentSkillLevel;
	}

	public int getOpponentScore() {
		return opponentScore;
	}

	public void setOpponentScore(int opponentScore) {
		this.opponentScore = opponentScore;
	}

	public int getOpponentImageResourceId() {
		return opponentImageResourceId;
	}

	public void setOpponentImageResourceId(int opponentImageResourceId) {
		this.opponentImageResourceId = opponentImageResourceId;
	}

	public int getPlayerScore() {
		return playerScore;
	}

	public void setPlayerScore(int playerScore) {
		this.playerScore = playerScore;
	}

	public Date getCompletedDate() {
		return completedDate;
	}

	public void setCompletedDate(Date completedDate) {
		this.completedDate = completedDate;
	}

	public String getGameType() {
		return gameType;
	}

	public void setGameType(String gameType) {
		this.gameType = gameType;
	}

	public String getLastActionText(Context context){
		String timeSince = Utils.getTimeSinceString(context, this.getCompletedDate());
		 
		//Logger.d(TAG, "getLastActionText game=" + this.id);
		
		if (this.getOpponentScore() > this.getPlayerScore()){
			return String.format(context.getString(R.string.game_opponent_winner_message), timeSince, this.getOpponentName(), this.getOpponentScore(), this.getPlayerScore());
		}
		else if(this.getPlayerScore() > this.getOpponentScore()){
			return String.format(context.getString(R.string.game_player_winner_message), timeSince, this.getPlayerScore(), this.getOpponentScore());
		}
		else {
			return String.format(context.getString(R.string.game_draw_message), timeSince, this.getPlayerScore(), this.getOpponentScore());
		}	 
	}
}
