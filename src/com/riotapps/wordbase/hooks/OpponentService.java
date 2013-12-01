package com.riotapps.wordbase.hooks;

import java.util.List;

import com.riotapps.wordbase.data.OpponentData;
import com.riotapps.wordbase.utils.ApplicationContext;
import com.riotapps.wordbase.utils.Logger;

public class OpponentService {
	private static final String TAG = OpponentService.class.getSimpleName();
	/*
	public static List<Opponent> getOpponentsFromLocal(){
		return OpponentData.getLocalOpponents();
		
	//	ApplicationContext appContext = (ApplicationContext)ApplicationContext.getAppContext().getApplicationContext();
	 //   appContext.setPlayer(player);
	 
	}
*/
	public static List<Opponent> getActivatedOpponents(){
		Logger.d(TAG, "getActivatedOpponents called");
		//loop through activated opponents and load bitmaps??????
//		return OpponentData.getActivatedOpponents();
		List<Opponent> opponents = OpponentData.getActivatedOpponents();
		
		for (Opponent o : opponents){
			o.preloadBitmaps();
			o.preloadResourceIds();
		}
		return opponents;
			 
	}
	
	
	public static Opponent getOpponent(int id){
		ApplicationContext appContext = (ApplicationContext)ApplicationContext.getAppContext().getApplicationContext();
		for (Opponent o : appContext.getOpponents()){
			if (o.getId() == id){
				return o;
			}
		}
		return null;
	}
	/*
	public static void saveOpponent(Opponent opponent){
		ApplicationContext appContext = (ApplicationContext)ApplicationContext.getAppContext().getApplicationContext();
		
		List<Opponent> opponents = appContext.getOpponents();
		int num = opponents.size();
		for(int i = 0; i < num; i++){
			if (opponents.get(i).getId() == opponent.getId()){
				opponents.remove(i);
				opponents.add(opponent);
				break;
			}
		}
		OpponentData.saveOpponents(opponents);
		appContext.setOpponents(opponents);
	}
	*/
 
	public static OpponentRecord getOpponentRecord(int opponentId){
		return OpponentData.getOpponentRecord(opponentId);
	}
	
	public static void saveOpponentRecord(int opponentId, OpponentRecord record){
		ApplicationContext appContext = (ApplicationContext)ApplicationContext.getAppContext().getApplicationContext();
 
		OpponentData.saveOpponentRecord(opponentId, record);
		
		appContext.setOpponents(getActivatedOpponents());
	}

}
