package hu.lilacode.hitnsync.game;

import hu.lilacode.hitnsync.R;
import hu.lilacode.hitnsync.service.music.GameMusic;
import android.app.Activity;
import android.os.Bundle;
import android.view.Window;
import android.view.WindowManager;

public class SingleGameActivity extends Activity {
	private GameMusic gameMusic;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		requestWindowFeature(Window.FEATURE_NO_TITLE);
		getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN,
				WindowManager.LayoutParams.FLAG_FULLSCREEN);
		setContentView(R.layout.activity_game_single);
		gameMusic = new GameMusic();
	}
	
	@Override
	protected void onPause() {
		super.onPause();
		gameMusic.stop();
	}
	
	@Override
	protected void onResume() {
		super.onResume();
		gameMusic.play();
	}
}
