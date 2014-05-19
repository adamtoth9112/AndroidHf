package hu.lilacode.hitnsync.game;

import hu.lilacode.hitnsync.R;
import hu.lilacode.hitnsync.service.music.GameMusic;
import hu.lilacode.hitnsync.service.network.Bluetooth;
import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.Window;
import android.view.WindowManager;

import com.google.android.gms.common.ConnectionResult;
import com.google.android.gms.common.api.GoogleApiClient;
import com.google.android.gms.common.api.GoogleApiClient.ConnectionCallbacks;
import com.google.android.gms.common.api.GoogleApiClient.OnConnectionFailedListener;
import com.google.android.gms.games.Games;
import com.google.android.gms.plus.Plus;

public class GameActivity extends Activity implements ConnectionCallbacks,
		OnConnectionFailedListener {
	public static Bluetooth bluetooth;
	private GameMusic gameMusic;
	private GoogleApiClient myClient;

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

		myClient = new GoogleApiClient.Builder(this).addApi(Plus.API)
				.addScope(Plus.SCOPE_PLUS_PROFILE).addConnectionCallbacks(this)
				.addOnConnectionFailedListener(this).build();
	}

	@Override
	protected void onStart() {
		super.onStart();

		bluetooth.onStart();

		myClient.connect();
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

	@Override
	public void onConnectionFailed(ConnectionResult arg0) {

	}

	@Override
	public void onConnected(Bundle arg0) {
		Games.Achievements.unlock(myClient, getString(R.string.first));
		Log.d("Achievements", "First unlocked");
	}

	@Override
	public void onConnectionSuspended(int arg0) {

	}
}
