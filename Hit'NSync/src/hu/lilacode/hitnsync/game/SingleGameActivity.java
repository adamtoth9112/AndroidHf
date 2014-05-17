package hu.lilacode.hitnsync.game;

import hu.lilacode.hitnsync.R;
import hu.lilacode.hitnsync.game.field.EnemyGameField;
import hu.lilacode.hitnsync.game.field.PlayerGameField;
import hu.lilacode.hitnsync.service.music.GameMusic;

import java.io.FileInputStream;
import java.io.FileOutputStream;

import android.app.Activity;
import android.app.Dialog;
import android.content.Context;
import android.content.ContextWrapper;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.util.Log;
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
			
			TextView tvDialog = (TextView) dialogbox.findViewById(R.id.dialogtext);
			
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
		String file1 = "userState";
		String file2 = "enemyState";
		FileOutputStream outputStream;

		byte[] user = new byte[100];
		byte[] enemy = new byte[100];

		for (int i = 0; i < 10; i++) {
			for (int j = 0; j < 10; j++) {
				enemy[i * 10 + j] = (byte) EnemyGameField.gameField[i][j];
				user[i * 10 + j] = (byte) PlayerGameField.gameField[i][j];
			}
		}

		try {
			outputStream = openFileOutput(file1, Context.MODE_PRIVATE);
			outputStream.write(user);
			outputStream.close();
			outputStream = openFileOutput(file2, Context.MODE_PRIVATE);
			outputStream.write(enemy);
			outputStream.close();
			setPrefState(true);
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	private void loadGame() {
		String file1 = "userState";
		String file2 = "enemyState";
		FileInputStream inputStream;
		FileInputStream inputStream2;

		try {
			inputStream = openFileInput(file1);
			inputStream2 = openFileInput(file2);
			for (int i = 0; i < 10; i++) {
				for (int j = 0; j < 10; j++) {
					PlayerGameField.gameField[i][j] = (int) inputStream.read();
					EnemyGameField.gameField[i][j] =  (int)inputStream2.read();
				}
			}
			
			
			inputStream.close();
			inputStream2.close();
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
