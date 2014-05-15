package hu.lilacode.hitnsync.game.field;

import java.util.ArrayList;

import hu.lilacode.hitnsync.game.ship.Ship;
import hu.lilacode.hitnsync.game.ship.Ship.Direction;
import android.graphics.Canvas;
import android.graphics.Paint;
import android.view.View;

public abstract class GameField {
	public int[][] gameField;
	protected View view;
	protected Paint paintLine;
	
	public void drawField(Canvas canvas){
		
	}
	
	public void drawShot(Canvas canvas){
		
	}
	
	public boolean isPlaceEmpty(int len, float sx, float sy, Direction dir){
		return false;
	}
	
	public void updateField(ArrayList<Ship> ships){
		
	}
	
	public void copyField(GameField gf){
		
	}
}
