package hu.lilacode.hitnsync.service.music;

import android.media.AudioManager;
import android.media.MediaPlayer;

public class GameMusic {
MediaPlayer mediaPlayer;
	
	public GameMusic() {
		String url = "http://webglezek.uw.hu/gamemusic.mp3";
		mediaPlayer = new MediaPlayer();
		mediaPlayer.setAudioStreamType(AudioManager.STREAM_MUSIC);
		try {
			mediaPlayer.setDataSource(url);
			mediaPlayer.prepare();
		} catch (Exception e) {
			e.printStackTrace();
		} 
		
		
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
