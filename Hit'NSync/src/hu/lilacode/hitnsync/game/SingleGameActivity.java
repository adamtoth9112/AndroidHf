package hu.lilacode.hitnsync.game;

import hu.lilacode.hitnsync.R;
import hu.lilacode.hitnsync.service.music.GameMusic;
import android.app.Activity;
import android.app.Dialog;
import android.os.Bundle;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.KeyEvent;
import android.view.Window;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.ImageView;

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
	
	@Override
	public boolean onKeyDown(int keyCode, KeyEvent event) {
		if (keyCode == KeyEvent.KEYCODE_BACK) {
			doDialog();
		}
		return false;
	}
	
	private void doDialog() {

		final Dialog dialogbox = new Dialog(this, R.style.FullHeightDialog);
		dialogbox.setContentView(R.layout.dialog_box_layout);
		dialogbox.setCancelable(true);

		Button ok = (Button) dialogbox.findViewById(R.id.ok);
		ok.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View v) {
				dialogbox.dismiss();
			}
		});

		Button megse = (Button) dialogbox.findViewById(R.id.megse);
		megse.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View v) {
				dialogbox.dismiss();
			}
		});

		dialogbox.show();
	}

}
