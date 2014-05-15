package hu.lilacode.hitnsync.game.field;

import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Paint.Style;
import android.view.View;

public class EnemyGameField extends GameField {

	public EnemyGameField(View view) {
		this.view = view;
		gameField = new int[10][10];
		for (int i = 0; i < 10; i++) {
			for (int j = 0; j < 10; j++) {
				gameField[i][j] = 0;
			}
		}

		paintLine = new Paint();
		paintLine.setStyle(Style.STROKE);
		paintLine.setStrokeWidth(5);
		paintLine.setTextSize(20);

	}

	public void drawField(Canvas canvas) {

		paintLine.setColor(Color.RED);
		canvas.drawRect(view.getWidth() * 0.53f, 0, view.getWidth(),
				view.getHeight() * 0.8f, paintLine);

		for (int i = 1; i < 10; i++ ) {
			float x = (float) (view.getWidth() * 0.47f / 10.0 * i);
			float y = (float) (view.getHeight() * 0.8f / 10.0 * i);
			
			canvas.drawLine(view.getWidth() * 0.53f + x, 0, view.getWidth() * 0.53f + x, view.getHeight() * 0.8f,
					paintLine);
			
			canvas.drawLine(view.getWidth() * 0.53f, y, view.getWidth(),
					y, paintLine);
		}
	}

	public void drawShot(Canvas canvas) {
		for (int i = 0; i < 10; i++) {
			for (int j = 0; j < 10; j++) {
				int x = (int) (view.getWidth() * 0.47f / 10.0 * (i+11));
				int y = (int) (view.getHeight() * 0.8f / 10.0 * j);
				float deltaX = (float) (view.getWidth() * 0.8f / 10.0) / 2;
				float deltaY = (float) (view.getHeight() * 0.8f / 10.0) / 2;
				
				if (gameField[i][j] == 6) {
					paintLine.setColor(Color.BLACK);
					canvas.drawCircle(x + deltaX, y + deltaY,
							view.getHeight() * 0.8f / 20 - 20, paintLine);
				}
				if (gameField[i][j] == 7) {
					paintLine.setColor(Color.RED);
					canvas.drawCircle(x + deltaX, y + deltaY,
							view.getHeight() * 0.8f / 20 - 20, paintLine);
				}
			}
		}
	}
	
	public void copyField(GameField gm){
		for (int i = 0; i < 10; i++){
			for (int j = 0; j < 10; j++){
				this.gameField[i][j] = gm.gameField[i][j];
			}
		}
	}
}
