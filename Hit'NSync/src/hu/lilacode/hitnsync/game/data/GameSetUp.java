package hu.lilacode.hitnsync.game.data;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.io.Serializable;

import android.os.Environment;

public class GameSetUp implements Serializable {
	public int aiShoot;
	public int aiTotalShoot;
	public int userShoot;
	public int userTotalShoot;
	public int userPoints;
	public int aiPoints;
	public int enemyShipNumber;
	public int aiTalalat;
	public int userTalalat;

	public GameSetUp() {
		aiShoot = 0;
		aiTotalShoot = 0;
		userShoot = 0;
		userTotalShoot = 0;
		userPoints = 0;
		aiPoints = 0;
		enemyShipNumber = 0;
		aiTalalat = 0;
		userTalalat = 0;
	}

	public void save() {
		String file1 = "/Android/data/hu.lilacode.Hit'NSync/gameSetUp";
		ObjectOutputStream oos;

		try {
			File dir = new File(Environment.getExternalStorageDirectory()
					.getPath() + "/Android/data/hu.lilacode.Hit'NSync");
			oos = new ObjectOutputStream(new FileOutputStream(Environment
					.getExternalStorageDirectory().getPath() + file1));
			if (!dir.exists()) {
				dir.mkdir();
			}

			oos.writeObject(this);
			oos.close();
		} catch (Exception e) {
			e.printStackTrace();
		}

	}

	public void load() {
		String file1 = "/Android/data/hu.lilacode.Hit'NSync/gameSetUp";
		ObjectInputStream ois;

		try {
			File dir = new File(Environment.getExternalStorageDirectory()
					.getPath() + "/Android/data/hu.lilacode.Hit'NSync");
			ois = new ObjectInputStream(new FileInputStream(Environment
					.getExternalStorageDirectory().getPath() + file1));
			if (!dir.exists()) {
				dir.mkdir();
			}

			GameSetUp gsu = (GameSetUp) ois.readObject();
			ois.close();

			this.aiTalalat = gsu.aiTalalat;
			this.enemyShipNumber = gsu.enemyShipNumber;
			this.aiPoints = gsu.aiPoints;
			this.userPoints = gsu.userPoints;
			this.aiShoot = gsu.aiShoot;
			this.aiTotalShoot = gsu.aiTotalShoot;
			this.userShoot = gsu.userShoot;
			this.userTotalShoot = gsu.userTotalShoot;
			this.userTalalat = gsu.userTalalat;

		} catch (Exception e) {
			e.printStackTrace();
		}
	}
}
