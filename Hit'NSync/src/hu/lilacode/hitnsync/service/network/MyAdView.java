package hu.lilacode.hitnsync.service.network;

import hu.lilacode.hitnsync.R;
import android.app.Activity;
import android.widget.RelativeLayout;

import com.google.android.gms.ads.AdRequest;
import com.google.android.gms.ads.AdSize;
import com.google.android.gms.ads.AdView;

public class MyAdView {
	public static final String MY_PUBLISHER_ID = "a15378a3ea48c77";
	public AdView mAdView;

	public MyAdView(Activity activity, AdSize adSize) {

		mAdView = new AdView(activity);
		mAdView.setAdUnitId(MY_PUBLISHER_ID);
		mAdView.setAdSize(AdSize.BANNER);

		RelativeLayout mainLayout = (RelativeLayout) activity
				.findViewById(R.id.layout);
		
        RelativeLayout.LayoutParams params = new RelativeLayout.LayoutParams(
                RelativeLayout.LayoutParams.MATCH_PARENT, RelativeLayout.LayoutParams.WRAP_CONTENT);
        mainLayout.addView(mAdView, params);
        mAdView.loadAd(new AdRequest.Builder().build());
	}

}
