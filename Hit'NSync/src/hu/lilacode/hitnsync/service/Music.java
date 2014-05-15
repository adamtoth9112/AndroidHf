package hu.lilacode.hitnsync.service;

import hu.lilacode.hitnsync.R;
import android.content.Context;
import android.media.MediaPlayer;

public class Music {
	MediaPlayer mediaPlayer;
	
	public Music(Context c) {
		mediaPlayer = MediaPlayer.create(c, R.raw.nsyncbyebyebye);
		mediaPlayer.setLooping(true);
		mediaPlayer.start();
	}
	
	public void play() {
		if (!mediaPlayer.isPlaying())
			mediaPlayer.start();
	}
	
	public void stop() {
		mediaPlayer.pause();
	}
}
