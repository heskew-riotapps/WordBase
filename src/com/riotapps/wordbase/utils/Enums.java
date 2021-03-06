package com.riotapps.wordbase.utils;

public class Enums {
	public static enum ResponseHandlerType {
	    CREATE_PLAYER,
	    UPDATE_PLAYER,
	    CREATE_GAME,
	    GET_PLAYER,
	    GET_GAME,
	    FIND_PLAYER_BY_NICKNAME
	}
	
	public static enum RequestType {
		GET,
		PUT,
		POST,
		DELETE
	}
		
	public static enum InstalledFromStore {
		GOOGLE_PLAY,
		AMAZON,
		NO_STORE
	}
}
