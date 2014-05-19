package hu.lilacode.hitnsync.game;

import hu.lilacode.hitnsync.R;
import hu.lilacode.hitnsync.service.music.GameMusic;
import hu.lilacode.hitnsync.service.network.Bluetooth;
import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.Window;
import android.view.WindowManager;

public class GameActivity extends Activity {
	public static Bluetooth bluetooth;
	private GameMusic gameMusic;
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		requestWindowFeature(Window.FEATURE_NO_TITLE);
		getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN,
				WindowManager.LayoutParams.FLAG_FULLSCREEN);
		setContentView(R.layout.activity_game_multi);


		bluetooth = new Bluetooth();
		
		bluetooth.onCreate(this);
		
		gameMusic = new GameMusic();
	}

	@Override
	protected void onStart() {
		super.onStart();
		
		bluetooth.onStart();
	}
	
	@Override
	protected void onPause() {
		super.onPause();
		gameMusic.stop();
	}

	@Override
	protected synchronized void onResume() {
		super.onResume();
		
		bluetooth.onResume();
		gameMusic.play();
	}

	@Override
	protected void onDestroy() {
		super.onDestroy();
		
		bluetooth.onDestroy();

		gameMusic.stop();
	}

	@Override
	protected void onActivityResult(int requestCode, int resultCode, Intent data) {
		bluetooth.onActivityResult(requestCode, resultCode, data);
	}
}
