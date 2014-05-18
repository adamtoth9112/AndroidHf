package hu.lilacode.hitnsync.game;

import hu.lilacode.hitnsync.R;
import hu.lilacode.hitnsync.game.field.EnemyGameField;
import hu.lilacode.hitnsync.game.field.PlayerGameField;
import hu.lilacode.hitnsync.game.ship.Ship;
import hu.lilacode.hitnsync.game.view.SingleGameView;
import hu.lilacode.hitnsync.service.music.GameMusic;
import hu.lilacode.hitnsync.service.network.MyAdView;

import java.io.File;
import java.io.FileOutputStream;
import java.io.ObjectOutputStream;
import java.util.List;

import com.google.ads.AdRequest;
import com.google.ads.AdSize;

import android.app.Activity;
import android.app.Dialog;
import android.content.ContextWrapper;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.os.Environment;
import android.util.Log;
import android.view.KeyEvent;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.Window;
import android.view.WindowManager;
import android.widget.Button;

public class SingleGameActivity extends Activity {
	private GameMusic gameMusic;
	private SharedPreferences prefs;
	private String prefName = "gameState";
	private MyAdView adview;
	public static final String STATE = "state";

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		requestWindowFeature(Window.FEATURE_NO_TITLE);
		getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN,
				WindowManager.LayoutParams.FLAG_FULLSCREEN);
		gameMusic = new GameMusic();

		prefs = getSharedPreferences(prefName, ContextWrapper.MODE_PRIVATE);
		adview = new MyAdView(this, AdSize.BANNER);

		setContentView(R.layout.activity_game_single);
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
		adview.loadAd(new AdRequest()); 
	}

	@Override
	public boolean onKeyDown(int keyCode, KeyEvent event) {
		if (keyCode == KeyEvent.KEYCODE_BACK && !SingleGameView.start) {
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
					saveGame();
					dialogbox.dismiss();
					finish();
			}
		});

		Button megse = (Button) dialogbox.findViewById(R.id.megse);
		megse.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View v) {
					dialogbox.dismiss();
					setPrefState(false);
					finish();
			}
		});

		dialogbox.show();
	}

	@SuppressWarnings("unchecked")
	private void saveGame() {
		String file1 = "/Android/data/hu.lilacode.Hit'NSync/userState";
		String file2 = "/Android/data/hu.lilacode.Hit'NSync/enemyState";
		String file3 = "/Android/data/hu.lilacode.Hit'NSync/ships";
		ObjectOutputStream oos;

		try {
			File dir = new File(Environment.getExternalStorageDirectory()
					.getPath() + "/Android/data/hu.lilacode.Hit'NSync");
			if (!dir.exists()) {
				dir.mkdir();
			}
			int[][] field = PlayerGameField.gameField;
			oos = new ObjectOutputStream(new FileOutputStream(Environment
					.getExternalStorageDirectory().getPath() + file1));
			oos.writeObject(field);
			oos.close();

			field = EnemyGameField.gameField;
			oos = new ObjectOutputStream(new FileOutputStream(Environment
					.getExternalStorageDirectory().getPath() + file2));
			oos.writeObject(field);
			oos.close();

			List<Ship> ships = (List<Ship>) SingleGameView.ships.clone();
			oos = new ObjectOutputStream(new FileOutputStream(Environment
					.getExternalStorageDirectory().getPath() + file3));
			oos.writeObject(ships);
			oos.close();

			setPrefState(true);
		} catch (Exception e) {
			e.printStackTrace();
		}
	}
	
	private void setPrefState(boolean hasSave) {
		SharedPreferences.Editor editor = prefs.edit();
		editor.putBoolean(SingleGameActivity.STATE, hasSave);
		Log.d("'NSync","Jatek elmentve");
		editor.commit();
	}

}
