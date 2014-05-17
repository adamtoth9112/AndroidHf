package hu.lilacode.hitnsync.menu;

import hu.lilacode.hitnsync.R;
import hu.lilacode.hitnsync.game.view.PlayerStats;
import android.app.Activity;
import android.os.Bundle;
import android.view.View;
import android.widget.TextView;

public class Stats extends Activity {
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.stats);
		
		View table = (View) findViewById(R.id.tableRow1);
		View row1 = (View) findViewById(R.id.tableRow2);
		
		TextView r1f1 = (TextView) findViewById(R.id.textView1);
		TextView r1f2 = (TextView) findViewById(R.id.textView2);
		TextView r1f3 = (TextView) findViewById(R.id.textView3);
		TextView r1f4 = (TextView) findViewById(R.id.textView4);
		
		

	}

}
