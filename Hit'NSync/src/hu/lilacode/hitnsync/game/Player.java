package hu.lilacode.hitnsync.game;

import android.content.Context;
import android.content.ContextWrapper;
import android.content.SharedPreferences;
import android.util.Log;

public class Player extends Thread {
	private SharedPreferences prefs;
	private String prefName = "Gamer";
	public static final String NAME = "name";
	public static final String POINTS = "points";
	public static final String TIME = "time";
	public static final String FIRST_START = "firststart";

	private Context ctx;

	public String name;
	public int time;
	public int points;
	public int shoot;
	
	public Player(){
		name = "Demo";
		time = 0;
		points = 0;
		shoot = 0;
	}

	public Player(Context context) {

		name = "Demo";
		time = 0;
		points = 0;
		shoot = 0;

		setContextOnPref(context);

		// Ment�sek megnyit�sa

		prefs = ctx.getSharedPreferences(prefName, ContextWrapper.MODE_PRIVATE);

		Log.e("Pref", "Pref elkészült");

	}

	public void setContextOnPref(Context context) {
		this.ctx = context;
	}

	public void firstStart() {

		SharedPreferences.Editor editor = prefs.edit();
		editor.putInt(POINTS, 0);
		editor.putString(NAME, "Player");
		editor.putInt(TIME, 0);
		editor.commit();

	}

	public boolean isFirstStart() {
		return prefs.getBoolean(FIRST_START, true);
	}
	
	public void mySetName(String n){
		SharedPreferences.Editor editor = prefs.edit();
		editor.putString(NAME, n);
		editor.commit();
	}
	
	public void mySetTime(int i){
		SharedPreferences.Editor editor = prefs.edit();
		editor.putInt(TIME, i);
		editor.commit();
	}
	
	public void mySetPoints(int i){
		SharedPreferences.Editor editor = prefs.edit();
		editor.putInt(POINTS, i);
		editor.commit();
	}
	
	public int myGetPoints(String myName){
		return prefs.getInt(myName, -1);
	}
	
	public int myGetTime(String myName){
		return prefs.getInt(myName, -1);
	}
	
	public String myGetName(String myName){
		return prefs.getString(myName, "Player");
	}
}
