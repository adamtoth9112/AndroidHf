package hu.lilacode.hitnsync.service.network;

import hu.lilacode.hitnsync.R;
import android.app.Activity;
import android.widget.RelativeLayout;

import com.google.ads.AdRequest;
import com.google.ads.AdSize;
import com.google.ads.AdView;

public class MyAdView extends AdView {
	public static final String MY_PUBLISHER_ID = "a15378a3ea48c77";

	public MyAdView(Activity activity, AdSize adSize) {
		super(activity, adSize, MY_PUBLISHER_ID);

		AdRequest adRequest = new AdRequest();

		adRequest.addTestDevice(AdRequest.TEST_EMULATOR);

		RelativeLayout mainLayout = (RelativeLayout) activity.findViewById(R.id.layout);
		
		RelativeLayout.LayoutParams params = new RelativeLayout.LayoutParams(
				RelativeLayout.LayoutParams.WRAP_CONTENT,
				RelativeLayout.LayoutParams.WRAP_CONTENT);
		params.addRule(RelativeLayout.ALIGN_PARENT_BOTTOM, RelativeLayout.TRUE);
		this.setLayoutParams(params);
		
		mainLayout.addView(this);
		
		this.loadAd(adRequest);
	}

}
