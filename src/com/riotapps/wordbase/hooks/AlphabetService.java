package com.riotapps.wordbase.hooks;

import java.lang.reflect.Type;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import android.content.Context;

import com.riotapps.wordbase.Main;
import com.riotapps.wordbase.R;
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import com.riotapps.wordbase.utils.ApplicationContext;
import com.riotapps.wordbase.utils.FileUtils;
import com.riotapps.wordbase.utils.Logger;
import com.riotapps.wordbase.utils.Utils;

public class AlphabetService {
	private static final String TAG = AlphabetService.class.getSimpleName();
	private static Alphabet alphabet = null;
	
	public AlphabetService(Context context){
		 Gson gson = new Gson();
		 Type type = new TypeToken<Alphabet>() {}.getType();
		 alphabet = gson.fromJson(FileUtils.ReadRawTextFile(context, R.raw.alphabet), type);
	}

	public static int getLetterValue(String character){
		if (AlphabetService.alphabet == null){
			 Gson gson = new Gson();
			 Type type = new TypeToken<Alphabet>() {}.getType();
			 AlphabetService.alphabet = gson.fromJson(FileUtils.ReadRawTextFile( ApplicationContext.getAppContext(), R.raw.alphabet), type);
		}
		
		  for (Letter letter : AlphabetService.alphabet.Letters){
			  if (letter.getCharacter().equals(character)){
				  return letter.getValue();
			  }
		  }
	      return 0;
	}
	
	public static List<Letter> getLetters(){
		if (AlphabetService.alphabet == null){
			 Gson gson = new Gson();
			 Type type = new TypeToken<Alphabet>() {}.getType();
			 AlphabetService.alphabet = gson.fromJson(FileUtils.ReadRawTextFile( ApplicationContext.getAppContext(), R.raw.alphabet), type);
		}
		
		return AlphabetService.alphabet.Letters;
	}
	/*
	def self.get_random_vowels
		return ["A","E","I","O","U"].shuffle.first(1) 
	end
	*/
	private static String[] vowels = null;
	private static String[] consonants = null;
	
	public static String[] getVowels(){
		if (vowels == null){
	 	ApplicationContext appContext = (ApplicationContext)ApplicationContext.getAppContext().getApplicationContext();
		
		vowels = appContext.getResources().getStringArray(R.array.alphabet_vowels);
 
		}
		
		return vowels;		
	}
	
	public static String[] getConsonants(){
		if (consonants == null){
	 	ApplicationContext appContext = (ApplicationContext)ApplicationContext.getAppContext().getApplicationContext();
		
	 	consonants = appContext.getResources().getStringArray(R.array.alphabet_consonants);
 
		}
		
		return consonants;		
	}
	
	public static String getRandomVowel(){
	 	//ApplicationContext appContext = (ApplicationContext)ApplicationContext.getAppContext().getApplicationContext();
		
		String[] letters = getVowels(); //appContext.getResources().getStringArray(R.array.alphabet_vowels);
		
		
		Utils.shuffleArray(letters);
		
		return letters[0];		
	}
	
	public static List<String> getRandomConsonants(){
		//ApplicationContext appContext = (ApplicationContext)ApplicationContext.getAppContext().getApplicationContext();
		
		String[] letters = getConsonants(); //appContext.getResources().getStringArray(R.array.alphabet_consonants);
	 
		Utils.shuffleArray(letters);
		
		List<String> list = new ArrayList<String>();
		
		for (int x = 0; x < 4; x++){
			list.add(letters[x]);
		}
	 
		return list;	
	}
	
	
	public static List<String> getRaceLetters(){
		//ApplicationContext appContext = (ApplicationContext)ApplicationContext.getAppContext().getApplicationContext();
		
		String[] consonants = getConsonants();
		List<String> consonants2  = new ArrayList<String>();
		//remove U from second array, leaving the possibility of only one Q in the mix
		for (String consonant : consonants){
			if (!consonant.equals("Q")){
				consonants2.add(consonant);
			}
		}
		
		
		String[] vowels = getVowels(); //appContext.getResources().getStringArray(R.array.alphabet_consonants); 
		List<String> vowels2  = new ArrayList<String>();
		//remove U from second array, leaving the possibility of only one u in the mix
		for (String vowel : vowels){
			if (!vowel.equals("U")){
				vowels2.add(vowel);
			}
		}
		
		String [] consonantsAll = Utils.concatArrays(consonants, consonants2.toArray(new String[consonants2.size()]));
		String [] vowelsAll = Utils.concatArrays(vowels, vowels2.toArray(new String[vowels2.size()]));
		 
		//up to two of any letter
		Utils.shuffleArray(consonantsAll);
		Utils.shuffleArray(vowelsAll);
		
		List<String> list = new ArrayList<String>();
		
		for (int x = 0; x < 6; x++){
			list.add(consonantsAll[x]);
		}
		for (int x = 0; x < 4; x++){
			list.add(vowelsAll[x]);
		}
	 
		Collections.shuffle(list);
		
		return list;
	}
	
	public static List<String> getHopper(String randomVowel, List<String> randomConsonants){
		ApplicationContext appContext = (ApplicationContext)ApplicationContext.getAppContext().getApplicationContext();
		/*
		   #A - 8
			#B = 2
			#C = 2
			#D = 4
			#E = 12
			#F = 2
			#G = 2
			#H = 4
			#I = 8
			#J = 1
			#K = 1
			#L = 4
			#M = 2
			#N = 6
			#O = 8
			#P = 2
			#Q = 1
			#R = 6
			#S = 5
			#T = 9
			#U = 4
			#V = 1
			#W = 2
			#X = 1
			#Y = 2
			#Z = 1
			*/
		String[] letters = appContext.getResources().getStringArray(R.array.alphabet_spread);
		Utils.shuffleArray(letters);
		
	
		String letterLog = "";
		for (String s : letters)
    	{
			letterLog = letterLog + "<item>" + s + "</item>";
    	}
		Logger.d(TAG, letterLog);
		
		List<String> list = new ArrayList<String>();
		
		for (int x = 0; x < letters.length; x++){
			list.add(letters[x]);
		}
		
		list.add(randomVowel);
	
		for (int x = 0; x < randomConsonants.size(); x++){
			list.add(randomConsonants.get(x));
		}
		
		//shuffle again
		Collections.shuffle(list);
		
		return list;	
	}
}
