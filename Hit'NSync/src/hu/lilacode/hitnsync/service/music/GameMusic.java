package hu.lilacode.hitnsync.service.music;

import hu.lilacode.hitnsync.R;
import android.content.Context;
import android.media.MediaPlayer;

public class GameMusic {
MediaPlayer mediaPlayer;
	
	public GameMusic(Context c) {
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
