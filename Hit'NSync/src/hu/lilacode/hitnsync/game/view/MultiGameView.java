package hu.lilacode.hitnsync.game.view;

import hu.lilacode.hitnsync.R;
import hu.lilacode.hitnsync.game.GameActivity;
import hu.lilacode.hitnsync.game.field.EnemyGameField;
import hu.lilacode.hitnsync.game.field.PlayerGameField;
import hu.lilacode.hitnsync.game.ship.Ship;
import hu.lilacode.hitnsync.game.ship.Ship.Direction;

import java.util.ArrayList;

import android.content.Context;
import android.content.res.Resources;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.graphics.Paint;
import android.graphics.Paint.Style;
import android.util.AttributeSet;
import android.view.MotionEvent;
import android.view.View;

public class MultiGameView extends View {
	private PlayerGameField userGameField;
	private EnemyGameField enemyGameField;
	private boolean place = true;
	private boolean play = false;
	private boolean start = true;
	protected Paint paintLine;
	protected ArrayList<Ship> ships;
	protected int shipNum = 0;
	protected Bitmap ocean;

	public MultiGameView(Context context, AttributeSet attrs) {
		super(context, attrs);

		userGameField = new PlayerGameField(this);
		enemyGameField = new EnemyGameField(this);

		paintLine = new Paint();
		paintLine.setStyle(Style.STROKE);
		paintLine.setStrokeWidth(5);

		ships = new ArrayList<Ship>();

		Resources res = getResources();
		ocean = BitmapFactory.decodeResource(res, R.drawable.ocean);
	}

	private void createShip() {
		float w = (getWidth() * 0.47f / 10);
		float h = (getHeight() * 0.8f / 10);

		shipNum++;

		if (shipNum < 3)
			ships.add(new Ship(this, 2, 4 * w, 10 * h,
					(getWidth() * 0.47f / 10), (getHeight() * 0.8f / 10)));
		else
			ships.add(new Ship(this, shipNum, 4 * w, 10 * h,
					(getWidth() * 0.47f / 10), (getHeight() * 0.8f / 10)));

	}

	@Override
	protected void onDraw(Canvas canvas) {
		super.onDraw(canvas);

		drawBackGround(canvas);

		if (start) {
			createShip();
			start = !start;
		}
		for (Ship s : ships)
			s.drawShip(canvas);

		userGameField.drawField(canvas);

		enemyGameField.drawField(canvas);
		if (play) {
			enemyGameField.drawShot(canvas);
		}

	}

	private void drawBackGround(Canvas canvas) {
		int h = (int) getHeight() / ocean.getHeight();
		int w = (int) getWidth() / ocean.getWidth();

		for (int i = 0; i <= h; i++)
			for (int j = 0; j <= w; j++)
				canvas.drawBitmap(ocean, ocean.getWidth() * j,
						ocean.getHeight() * i, null);
	}

	@Override
	public boolean onTouchEvent(MotionEvent event) {
		
		GameActivity.bluetooth.sendMessage(userGameField.gameField);
		int action = event.getAction();
		if (action == MotionEvent.ACTION_DOWN) {
			float w = (getWidth() * 0.47f / 10);
			float h = (getHeight() * 0.8f / 10);
			int x = (int) (event.getX() / w);
			int y = (int) (event.getY() / h);
			boolean emptyPlace;

			if (x < 10 && y < 10) {

				if (userGameField.gameField[x][y] == 0 && shipNum < 6 && place) {

					Ship addedShip = ships.get(shipNum - 1);

					if (x + addedShip.len > 10) {

						emptyPlace = userGameField.isPlaceEmpty(addedShip.len,
								x * w, y * h, Direction.Vertical);

						if (emptyPlace) {
							addedShip.changeDirection();
							addedShip.moveShip(x * w, y * h);
							userGameField.updateField(ships);

							if (shipNum < 5) {
								createShip();
							} else {
								play = true;
								place = false;
								shipNum++;
							}
						}
					} else if (x + addedShip.len < 11) {

						emptyPlace = userGameField.isPlaceEmpty(addedShip.len,
								x * w, y * h, addedShip.direction);

						if (emptyPlace) {

							addedShip.moveShip(x * w, y * h);
							userGameField.updateField(ships);

							if (shipNum < 5) {
								createShip();
							} else {
								play = true;
								place = false;
								shipNum++;

							}
						}
					}
				}

				else if (userGameField.gameField[x][y] == 0 && !place)
					shipNum++;

				else if (userGameField.gameField[x][y] != 0 && shipNum < 7) {
					Ship addedShip;

					addedShip = ships.get(shipNum - 2);
					addedShip.changeDirection();
					addedShip.moveShip(addedShip.coord[0].x,
							addedShip.coord[0].y);

					if ((int) (addedShip.coord[addedShip.len - 1].x / w) > 9
							|| (int) (addedShip.coord[addedShip.len - 1].y / h) > 9) {
						addedShip.changeDirection();
						addedShip.moveShip(addedShip.coord[0].x,
								addedShip.coord[0].y);
						return true;
					}

					if (shipNum == 2)
						emptyPlace = true;
					else {
						emptyPlace = userGameField.isPlaceEmpty(
								addedShip.len - 1, addedShip.coord[1].x,
								addedShip.coord[1].y, addedShip.direction);
					}

					if ((int) (addedShip.coord[0].x / w) == x
							&& (int) (addedShip.coord[0].y / h) == y
							&& (int) (addedShip.coord[addedShip.len - 1].x / w) < 10
							&& (int) (addedShip.coord[addedShip.len - 1].y / h) < 10
							&& emptyPlace) {

						userGameField.updateField(ships);
					} else {
						addedShip.changeDirection();
						addedShip.moveShip(addedShip.coord[0].x,
								addedShip.coord[0].y);
					}
				}
			}

			if (x > 10 && x < 21 && y < 10) {
				if (play) {

					if (enemyGameField.gameField[x - 11][y] == 0) {
						enemyGameField.gameField[x - 11][y] = 6;
					}
					if (userGameField.gameField[x - 11][y] != 0
							&& userGameField.gameField[x - 11][y] != 6
							&& userGameField.gameField[x - 11][y] != 7) {
						enemyGameField.gameField[x - 11][y] = 7;
					}

				}
			}
		}
		invalidate();
		return true;

	}
}
