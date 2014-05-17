package hu.lilacode.hitnsync.menu;

import hu.lilacode.hitnsync.R;
import hu.lilacode.hitnsync.game.GameActivity;
import hu.lilacode.hitnsync.game.SingleGameActivity;
import hu.lilacode.hitnsync.service.music.Music;
import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;

public class Menu extends Activity {
	Music music;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.menu);

		music = new Music(this);
		
		

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
				Intent i = new Intent(Menu.this, SingleGameActivity.class);
				startActivity(i);
			}
		});
		
		btnOption.setOnClickListener(new OnClickListener(){
			@Override
			public void onClick(View arg0) {
				Intent i = new Intent(Menu.this, Options.class);
				startActivity(i);
			}
		});
		
		btnStats.setOnClickListener(new OnClickListener(){
			@Override
			public void onClick(View arg0) {
				Intent i = new Intent(Menu.this, Stats.class);
				startActivity(i);
			}
		});
		
		btnCredits.setOnClickListener(new OnClickListener(){
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
	}
	
	@Override
	protected void onResume() {
		super.onResume();
		music.play();
	}

}
