package hu.lilacode.hitnsync;

import hu.lilacode.hitnsync.menu.Menu;

import java.util.Timer;
import java.util.TimerTask;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.Window;
import android.view.WindowManager;

public class Splash extends Activity {

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		requestWindowFeature(Window.FEATURE_NO_TITLE);
		getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN,
				WindowManager.LayoutParams.FLAG_FULLSCREEN);
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_splash);
		int timeSplashScreenShowsInMilliseconds = 2000;
		final Intent i = new Intent(this, Menu.class);
		Timer timer = new Timer();
		timer.schedule(new TimerTask() {
			@Override
			public void run() {
				finish();
				startActivity(i);
			}
		}, timeSplashScreenShowsInMilliseconds);
	}

}
