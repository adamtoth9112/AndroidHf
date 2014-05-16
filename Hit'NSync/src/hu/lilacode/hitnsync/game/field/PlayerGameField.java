package hu.lilacode.hitnsync.game.field;

import hu.lilacode.hitnsync.game.ship.Ship;
import hu.lilacode.hitnsync.game.ship.Ship.Direction;

import java.util.ArrayList;

import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Paint.Style;
import android.view.View;

public class PlayerGameField extends GameField {
	public static int[][] gameField;

	public PlayerGameField(View view) {
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

		paintLine.setColor(Color.BLACK);

		canvas.drawRect(0.01f, 0.01f, view.getWidth() * 0.47f,
				view.getHeight() * 0.8f, paintLine);

		for (int i = 1; i < 10; i++) {
			float x = (float) (view.getWidth() * 0.47f / 10.0 * i);
			float y = (float) (view.getHeight() * 0.8f / 10.0 * i);

			canvas.drawLine(x, 0.01f, x, view.getHeight() * 0.8f, paintLine);

			canvas.drawLine(0.01f, y, view.getWidth() * 0.47f, y, paintLine);
		}

		/*
		 * for (int i = 0; i < 10; i++) { for (int j = 0; j < 10; j++) { if
		 * (gameField[i][j] != 0) canvas.drawCircle( i * (view.getWidth() *
		 * 0.47f / 10) + (view.getWidth() * 0.47f / 10) / 2, j *
		 * (view.getHeight() * 0.8f / 10) + (view.getHeight() * 0.8f / 10) / 2,
		 * view.getHeight() * 0.8f / 20 - 10, paintLine);
		 * 
		 * } }
		 */
	}

	public void drawShot(Canvas canvas) {
		for (int i = 0; i < 10; i++) {
			for (int j = 0; j < 10; j++) {
				if (gameField[i][j] == 6) {
					paintLine.setColor(Color.BLACK);
					float x = (float) (view.getWidth() * 0.47f / 10.0 * i);
					float y = (float) (view.getHeight() * 0.8f / 10.0 * j);
					float deltaX = (float) (view.getWidth() * 0.47f / 10.0);
					float deltaY = (float) (view.getHeight() * 0.8f / 10.0);
					canvas.drawCircle(x + deltaX / 2, y + deltaY / 2,
							view.getHeight() * 0.8f / 20 - 20, paintLine);
				}
				if (gameField[i][j] == 7) {
					paintLine.setColor(Color.RED);
					float x = (float) (view.getWidth() * 0.47f / 10.0 * i);
					float y = (float) (view.getHeight() * 0.8f / 10.0 * j);
					float deltaX = (float) (view.getWidth() * 0.47f / 10.0);
					float deltaY = (float) (view.getHeight() * 0.8f / 10.0);
					canvas.drawCircle(x + deltaX / 2, y + deltaY / 2,
							view.getHeight() * 0.8f / 20 - 20, paintLine);
				}
			}
		}
	}

	public void updateField(ArrayList<Ship> ships) {
		for (int i = 0; i < 10; i++) {
			for (int j = 0; j < 10; j++) {
				gameField[i][j] = 0;
			}
		}

		int x, y;
		for (Ship s : ships) {
			boolean ok = true;
			x = (int) (((s.coord[0].x + (view.getWidth() * 0.47f / 10) / 2) / (view
					.getWidth() * 0.47f / 10)));
			y = (int) (((s.coord[0].y + (view.getHeight() * 0.8f / 10) / 2) / (view
					.getHeight() * 0.8f / 10)));

			for (int i = 0; i < s.len; i++) {
				if (y < 10 && x < 10) {
					if (s.direction == Direction.Horizontal) {
						if (gameField[x + 1][y] != 0) {
							ok = false;
						}
					}
					if (s.direction == Direction.Vertical) {
						if (gameField[x][y + i] != 0) {
							ok = false;
						}
					}
				}
			}

			if (ok) {
				for (int i = 0; i < s.len; i++) {

					if (y < 10 && x < 10) {
						if (s.direction == Direction.Horizontal) {
							gameField[x + i][y] = s.len;
						}
						if (s.direction == Direction.Vertical) {
							gameField[x][y + i] = s.len;
						}

					}
				}

			}
		}
	}

	public boolean isPlaceEmpty(int len, float sx, float sy, Direction dir) {
		Ship ship = new Ship(null, len, sx, sy, (view.getWidth() * 0.47f / 10),
				(view.getHeight() * 0.8f / 10));
		ship.direction = dir;

		ship.moveShip(sx, sy);

		if ((int) (ship.coord[ship.len - 1].x / (view.getWidth() * 0.47f / 10)) > 9
				|| (int) (ship.coord[ship.len - 1].y / (view.getHeight() * 0.8f / 10)) > 9)
			return false;

		int x, y;
		for (int s = 0; s < ship.len; s++) {
			x = (int) (ship.coord[s].x / (view.getWidth() * 0.47f / 10));
			y = (int) (ship.coord[s].y / (view.getHeight() * 0.8f / 10));

			if (gameField[x][y] != 0)
				return false;
		}

		return true;
	}
}
