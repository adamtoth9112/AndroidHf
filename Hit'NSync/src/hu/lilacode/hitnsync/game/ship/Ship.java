package hu.lilacode.hitnsync.game.ship;

import java.io.Serializable;

import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Paint.Style;
import android.view.View;

@SuppressWarnings("serial")
public class Ship implements Serializable {
	public enum Direction {
		Horizontal, Vertical
	};

	public class Position implements Serializable {
		public float x;
		public float y;

		public Position(float x, float y) {
			this.x = x;
			this.y = y;
		}

	}

	public Direction direction = Direction.Horizontal;
	public Position[] coord;
	public int len;
	private transient Paint paintLine;
	private transient View view;
	protected float deltaX, deltaY;

	public Ship(View view, int len, float startCoordX, float startCoordY,
			float deltaX, float deltaY) {
		this.setView(view);
		this.len = len;
		this.deltaX = deltaX;
		this.deltaY = deltaY;
		coord = new Position[len];
		moveShip(startCoordX, startCoordY);

		createPaint();
	}

	public void createPaint() {
		paintLine = new Paint();
		paintLine.setStyle(Style.FILL);
		paintLine.setStrokeWidth(5);
	}

	public void moveShip(float coordX, float coordY) {
		coord[0] = new Position(coordX, coordY);

		for (int i = 0; i < len; i++) {
			if (direction == Direction.Horizontal)
				coord[i] = new Position(coord[0].x + i * deltaX, coord[0].y);
			else
				coord[i] = new Position(coord[0].x, coord[0].y + i * deltaY);
		}
	}

	public void drawShip(Canvas canvas) {

		for (int i = 0; i < len; i++) {
			if (len == 2) {
				paintLine.setColor(Color.rgb(0, 255, 0));
				canvas.drawCircle(coord[i].x + deltaX / 2, coord[i].y + deltaY
						/ 2, getView().getHeight() * 0.8f / 20 - 20, paintLine);
			}
			if (len == 3) {
				paintLine.setColor(Color.rgb(0, 255, 255));
				canvas.drawCircle(coord[i].x + deltaX / 2, coord[i].y + deltaY
						/ 2, getView().getHeight() * 0.8f / 20 - 20, paintLine);
			}
			if (len == 4) {
				paintLine.setColor(Color.rgb(255, 255, 0));
				canvas.drawCircle(coord[i].x + deltaX / 2, coord[i].y + deltaY
						/ 2, getView().getHeight() * 0.8f / 20 - 20, paintLine);
			}
			if (len == 5) {
				paintLine.setColor(Color.rgb(255, 0, 0));
				canvas.drawCircle(coord[i].x + deltaX / 2, coord[i].y + deltaY
						/ 2, getView().getHeight() * 0.8f / 20 - 20, paintLine);
			}

		}
	}

	public void changeDirection() {
		if (direction == Direction.Horizontal)
			direction = Direction.Vertical;
		else
			direction = Direction.Horizontal;
	}

	public View getView() {
		return view;
	}

	public void setView(View view) {
		this.view = view;
	}

}
