package hu.lilacode.hitnsync.menu;

import hu.lilacode.hitnsync.R;
import android.app.Activity;
import android.os.Bundle;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.EditText;

public class Options extends Activity {

	String name;
	private EditText etname;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.option);

		Button btnAdd = (Button) findViewById(R.id.btnAdd);
		etname = (EditText) findViewById(R.id.NameField);

		btnAdd.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View arg0) {
				name = etname.getText().toString();
				etname.setText("");
				
			}
		});
	}

}
