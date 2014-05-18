package hu.lilacode.hitnsync.menu;

import hu.lilacode.hitnsync.R;
import hu.lilacode.hitnsync.game.GameActivity;
import hu.lilacode.hitnsync.game.SingleGameActivity;
import hu.lilacode.hitnsync.game.field.EnemyGameField;
import hu.lilacode.hitnsync.game.field.PlayerGameField;
import hu.lilacode.hitnsync.game.ship.Ship;
import hu.lilacode.hitnsync.game.view.SingleGameView;
import hu.lilacode.hitnsync.service.music.Music;
import hu.lilacode.hitnsync.service.network.MyAdView;

import java.io.FileInputStream;
import java.io.ObjectInputStream;
import java.util.ArrayList;

import android.app.Activity;
import android.app.Dialog;
import android.content.ContextWrapper;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.os.Environment;
import android.util.Log;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.TextView;

import com.google.android.gms.ads.AdSize;
import com.google.android.gms.common.ConnectionResult;
import com.google.android.gms.common.GooglePlayServicesUtil;

public class Menu extends Activity {
	Music music;
	private SharedPreferences prefs;
	private String prefName = "gameState";
	private MyAdView adview;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.menu);

		if (ConnectionResult.SUCCESS == GooglePlayServicesUtil.isGooglePlayServicesAvailable(this)) {
			Log.d("Play Services", "SERVICES: SUCCESS");
		}
		else
			Log.e("Play Services", "SERVICES: FAILED");
			
		
		music = new Music(this);

		prefs = getSharedPreferences(prefName, ContextWrapper.MODE_PRIVATE);
		
		adview = new MyAdView(this, AdSize.BANNER);

		Button btnMulty = (Button) findViewById(R.id.btnMulty);
		Button btnSingle = (Button) findViewById(R.id.btnSingle);
		Button btnOption = (Button) findViewById(R.id.btnOption);
		Button btnStats = (Button) findViewById(R.id.btnStats);
		Button btnCredits = (Button) findViewById(R.id.btnCredits);

		btnMulty.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				Intent i = new Intent(Menu.this, GameActivity.class);
				startActivity(i);
			}
		});

		btnSingle.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				if (prefs.getBoolean(SingleGameActivity.STATE, false))
					doDialog();
				else {
					Intent i = new Intent(Menu.this, SingleGameActivity.class);
					startActivity(i);
				}
			}
		});

		btnOption.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View arg0) {
				Intent i = new Intent(Menu.this, Options.class);
				startActivity(i);
			}
		});

		btnStats.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View arg0) {
				Intent i = new Intent(Menu.this, Stats.class);
				startActivity(i);
			}
		});

		btnCredits.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View arg0) {
				Intent i = new Intent(Menu.this, Credits.class);
				startActivity(i);
			}
		});
	}

	@Override
	protected void onPause() {
		super.onPause();
		music.stop();
		adview.mAdView.pause();
	}

	@Override
	protected void onResume() {
		super.onResume();
		music.play();
		adview.mAdView.resume();
	}
	
	@Override
	protected void onDestroy() {
		super.onDestroy();
		adview.mAdView.destroy();
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
		} catch (Exception e) {
			e.printStackTrace();
		}

	}

	private void setPrefState(boolean hasSave) {
		SharedPreferences.Editor editor = prefs.edit();
		editor.putBoolean(SingleGameActivity.STATE, hasSave);
		editor.commit();
	}

	private void doDialog() {

		final Dialog dialogbox = new Dialog(this, R.style.FullHeightDialog);
		dialogbox.setContentView(R.layout.dialog_box_layout);
		dialogbox.setCancelable(false);

		String dialogText = getResources().getText(R.string.betoltsek)
				.toString();

		TextView tvDialog = (TextView) dialogbox.findViewById(R.id.dialogtext);

		tvDialog.setText(dialogText);

		Button ok = (Button) dialogbox.findViewById(R.id.ok);
		ok.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View v) {
				loadGame();
				dialogbox.dismiss();
				Intent i = new Intent(Menu.this, SingleGameActivity.class);
				startActivity(i);
			}
		});

		Button megse = (Button) dialogbox.findViewById(R.id.megse);
		megse.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View v) {
				dialogbox.dismiss();
				EnemyGameField.initField();
				PlayerGameField.initField();
				setPrefState(false);
				Intent i = new Intent(Menu.this, SingleGameActivity.class);
				startActivity(i);
			}
		});

		dialogbox.show();
	}
	
}
