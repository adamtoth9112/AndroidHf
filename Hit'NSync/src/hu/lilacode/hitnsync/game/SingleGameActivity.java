package hu.lilacode.hitnsync.game;

import hu.lilacode.hitnsync.R;
import hu.lilacode.hitnsync.game.field.EnemyGameField;
import hu.lilacode.hitnsync.game.field.PlayerGameField;
import hu.lilacode.hitnsync.game.ship.Ship;
import hu.lilacode.hitnsync.game.view.SingleGameView;
import hu.lilacode.hitnsync.service.music.GameMusic;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.util.ArrayList;
import java.util.List;

import android.app.Activity;
import android.app.Dialog;
import android.content.ContextWrapper;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.os.Environment;
import android.view.KeyEvent;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.Window;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.TextView;

public class SingleGameActivity extends Activity {
	private GameMusic gameMusic;
	private SharedPreferences prefs;
	private String prefName = "gameState";
	public static final String STATE = "state";

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		requestWindowFeature(Window.FEATURE_NO_TITLE);
		getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN,
				WindowManager.LayoutParams.FLAG_FULLSCREEN);
		gameMusic = new GameMusic();

		prefs = getSharedPreferences(prefName, ContextWrapper.MODE_PRIVATE);

		if (prefs.getBoolean(STATE, false)) {
			doDialog(true);
		}

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
	}

	@Override
	public boolean onKeyDown(int keyCode, KeyEvent event) {
		if (keyCode == KeyEvent.KEYCODE_BACK) {
			doDialog(false);
		}
		return false;
	}

	private void doDialog(final boolean load) {

		final Dialog dialogbox = new Dialog(this, R.style.FullHeightDialog);
		dialogbox.setContentView(R.layout.dialog_box_layout);
		dialogbox.setCancelable(true);

		if (load) {
			String dialogText = getResources().getText(R.string.betoltsek)
					.toString();

			TextView tvDialog = (TextView) dialogbox
					.findViewById(R.id.dialogtext);

			tvDialog.setText(dialogText);
		}

		Button ok = (Button) dialogbox.findViewById(R.id.ok);
		ok.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View v) {
				if (load) {
					loadGame();
					dialogbox.dismiss();
				} else {
					saveGame();
					dialogbox.dismiss();
					finish();
				}
			}
		});

		Button megse = (Button) dialogbox.findViewById(R.id.megse);
		megse.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View v) {
				if (load) {
					dialogbox.dismiss();
					EnemyGameField.initField();
					PlayerGameField.initField();
					setPrefState(false);
				} else {
					dialogbox.dismiss();
					setPrefState(false);
					finish();
				}
			}
		});

		dialogbox.show();
	}

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

	@SuppressWarnings("unchecked")
	private void loadGame() {
		String file1 = "/Android/data/hu.lilacode.Hit'NSync/userState";
		String file2 = "/Android/data/hu.lilacode.Hit'NSync/enemyState";
		String file3 = "/Android/data/hu.lilacode.Hit'NSync/ships";
		ObjectInputStream ois;

		try {
			ois = new ObjectInputStream(new FileInputStream(Environment
					.getExternalStorageDirectory().getPath() + file1));
			PlayerGameField.gameField = (int[][]) ois.readObject();
			ois.close();
			ois = new ObjectInputStream(new FileInputStream(Environment
					.getExternalStorageDirectory().getPath() + file2));
			EnemyGameField.gameField = (int[][]) ois.readObject();
			ois.close();
			ois = new ObjectInputStream(new FileInputStream(Environment
					.getExternalStorageDirectory().getPath() + file3));
			SingleGameView.ships = (ArrayList<Ship>) ois.readObject();
			ois.close();
			setPrefState(false);
		} catch (Exception e) {
			e.printStackTrace();
		}

	}

	private void setPrefState(boolean hasSave) {
		SharedPreferences.Editor editor = prefs.edit();
		editor.putBoolean(STATE, hasSave);
		editor.commit();
	}
}
