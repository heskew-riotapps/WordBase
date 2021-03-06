package com.riotapps.wordbase.utils;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class Validations{
	/**===================================================
	 * method validateEmail
	 *====================================================*/
	public static boolean validateEmail(String email){

		boolean isValid = false; 

		Pattern p = Pattern.compile("^[a-zA-Z0-9._-]+@[a-zA-Z0-9.-]+\\.[a-zA-Z]{2,4}$");

		Matcher m = p.matcher(email);

		isValid = m.matches();

		return isValid;
	}//end method validateEmail
	
	public static boolean validateNickname(String nickname){

		boolean isValid = false; 

		Pattern p = Pattern.compile("^[a-zA-Z0-9 \\.-]+$");

		Matcher m = p.matcher(nickname);

		isValid = m.matches();

		return isValid;
	}//end method validateEmail

	/**===================================================
	 * method validateString
	 *====================================================*/
	public static boolean ValidateString(String string){
		boolean isValid;
		if(string == null || string.equalsIgnoreCase("")){
			isValid = false; 
		}else {
			isValid = true;
		} 
		return isValid;
	}//end method validateString


	/**===================================================
	 * method validateUserAddOrEditForm
	 *====================================================*/
	public static boolean ValidateUserAddOrEditForm(String userName, String email){

		boolean isUserAddOrEditFormValid = false; 

		if(ValidateString(userName) && validateEmail(email)){
			isUserAddOrEditFormValid = true;
		}
		return isUserAddOrEditFormValid;
	}//end method validateUserAddOrEditForm	
} //end of class ClientSideValidation