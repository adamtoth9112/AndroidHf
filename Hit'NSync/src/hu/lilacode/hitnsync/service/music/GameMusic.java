package hu.lilacode.hitnsync.service.music;

import android.media.AudioManager;
import android.media.MediaPlayer;
import android.media.MediaPlayer.OnPreparedListener;

public class GameMusic {
MediaPlayer mediaPlayer;
	
	public GameMusic() {
		String url = "http://webglezek.uw.hu/gamemusic.mp3";
		mediaPlayer = new MediaPlayer();
		mediaPlayer.setAudioStreamType(AudioManager.STREAM_MUSIC);
		try {
			mediaPlayer.setDataSource(url);
			mediaPlayer.prepareAsync();
		} catch (Exception e) {
			e.printStackTrace();
		} 
		
		
		mediaPlayer.setLooping(true);
		mediaPlayer.setOnPreparedListener(new OnPreparedListener() {
			
			@Override
			public void onPrepared(MediaPlayer mp) {
				mediaPlayer.start();
			}
		});
	}
	
	public void play() {
		if (!mediaPlayer.isPlaying())
			mediaPlayer.start();
	}
	
	public void stop() {
		mediaPlayer.pause();
	}
}
