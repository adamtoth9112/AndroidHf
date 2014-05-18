package hu.lilacode.hitnsync.game.view;

import hu.lilacode.hitnsync.R;
import hu.lilacode.hitnsync.game.SingleGameActivity;
import hu.lilacode.hitnsync.game.data.GameSetUp;
import hu.lilacode.hitnsync.game.data.Player;
import hu.lilacode.hitnsync.game.field.EnemyGameField;
import hu.lilacode.hitnsync.game.field.PlayerGameField;
import hu.lilacode.hitnsync.game.ship.Ship;
import hu.lilacode.hitnsync.game.ship.Ship.Direction;

import java.util.ArrayList;
import java.util.Random;

import android.content.Context;
import android.content.ContextWrapper;
import android.content.SharedPreferences;
import android.content.res.Resources;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.graphics.Paint;
import android.graphics.Paint.Style;
import android.util.AttributeSet;
import android.view.MotionEvent;
import android.view.View;

public class SingleGameView extends View {
	private PlayerGameField userGameField;
	private EnemyGameField enemyGameField;
	private boolean place = true;
	private boolean play = false;
	public static boolean start = true;
	private boolean drawshot = false;
	private volatile boolean playerTurn = true;
	protected Paint paintLine;
	public static ArrayList<Ship> ships;
	protected int shipNum = 0;
	protected Bitmap ocean;
	private Random rn;
	Player player;
	byte[] kar;
	private Context context;
	GameSetUp gsu;

	private SharedPreferences prefs;
	private String prefName = "gameState";

	public SingleGameView(Context context, AttributeSet attrs) {
		super(context, attrs);

		this.context = context;

		userGameField = new PlayerGameField(this);
		enemyGameField = new EnemyGameField(this);

		paintLine = new Paint();
		paintLine.setStyle(Style.STROKE);
		paintLine.setStrokeWidth(5);

		gsu = new GameSetUp();

		Resources res = getResources();
		ocean = BitmapFactory.decodeResource(res, R.drawable.ocean);

		rn = new Random();

		prefs = context.getSharedPreferences(prefName,
				ContextWrapper.MODE_PRIVATE);

		if (prefs.getBoolean(SingleGameActivity.STATE, false)) {
			play = true;
			drawshot = true;
			place = false;
			start = false;
			for (Ship ship : ships) {
				ship.createPaint();
				ship.setView(this);
			}
			gsu.load();
			invalidate();
		} else {
			enemyGameField.initField();
			userGameField.initField();
			ships = new ArrayList<Ship>();
			AIPlace();
			place = true;
			shipNum = 0;
			play = false;
			start = true;
		}
		player = new Player(this.getContext());
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

		enemyGameField.drawShot(canvas);
		userGameField.drawShot(canvas);

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
		int action = event.getAction();
		if (action == MotionEvent.ACTION_DOWN) {
			float w = (getWidth() * 0.47f / 10);
			float h = (getHeight() * 0.8f / 10);
			int x = (int) (event.getX() / w);
			int y = (int) (event.getY() / h);
			boolean emptyPlace;

			if (x < 10 && y < 10) {

				if (PlayerGameField.gameField[x][y] == 0 && shipNum < 6
						&& place) {

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
								drawshot = true;
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
								drawshot = true;
								shipNum++;
							}
						}
					}
				}

				else if (PlayerGameField.gameField[x][y] == 0 && !place)
					shipNum++;

				else if (PlayerGameField.gameField[x][y] != 0 && shipNum < 7) {
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
				if (play && playerTurn) {
					if (EnemyGameField.gameField[x - 11][y] == 6
							|| EnemyGameField.gameField[x - 11][y] == 7) {

					} else {
						gsu.userShoot++;
						gsu.userTotalShoot++;
						if (EnemyGameField.gameField[x - 11][y] == 0) {
							EnemyGameField.gameField[x - 11][y] = 6;
							if (gsu.userShoot == 5) {
								gsu.userPoints = gsu.userPoints - 5;
								gsu.userShoot = 0;
							} else {
								gsu.userPoints--;
							}
							player.points = gsu.userPoints;
						}
						if (EnemyGameField.gameField[x - 11][y] != 0
								&& EnemyGameField.gameField[x - 11][y] != 6
								&& EnemyGameField.gameField[x - 11][y] != 7) {
							if (EnemyGameField.gameField[x - 11][y] == 2) {
								gsu.userPoints = gsu.userPoints + 20;
								gsu.userShoot = 0;
							}
							if (EnemyGameField.gameField[x - 11][y] == 3) {
								gsu.userPoints = gsu.userPoints + 16;
								gsu.userShoot = 0;
							}
							if (EnemyGameField.gameField[x - 11][y] == 4) {
								gsu.userPoints = gsu.userPoints + 14;
								gsu.userShoot = 0;
							}
							if (EnemyGameField.gameField[x - 11][y] == 5) {
								gsu.userPoints = gsu.userPoints + 12;
								gsu.userShoot = 0;
							}
							EnemyGameField.gameField[x - 11][y] = 7;
							gsu.userTalalat++;

							player.points = gsu.userPoints;

							invalidate();
						}

						AIShot();
						gsu.save();

						if (gsu.userTalalat == gsu.enemyShipNumber) {
							play = false;
							player.shoot = gsu.userTotalShoot;

						}
						if (gsu.aiTalalat == 16) {
							play = false;

						}
					}

				}
			}
		}
		invalidate();
		return true;

	}

	void AIShot() {
		int x = 0;
		int y = 0;
		boolean lo = true;

		playerTurn = false;

		synchronized (this) {
			try {
				this.wait(1000);
			} catch (InterruptedException e) {
			}
		}

		while (lo) {
			x = rn.nextInt(10);
			y = rn.nextInt(10);
			if (userGameField.gameField[x][y] == 6
					|| userGameField.gameField[x][y] == 7) {
				lo = true;
			} else {
				lo = false;
			}
		}

		gsu.aiShoot++;
		gsu.aiTotalShoot++;

		if (userGameField.gameField[x][y] == 0) {
			userGameField.gameField[x][y] = 6;
			if (gsu.aiShoot == 5) {
				gsu.aiPoints = gsu.aiPoints - 5;
				gsu.aiShoot = 0;
			} else {
				gsu.aiPoints--;
			}
		}

		if (userGameField.gameField[x][y] != 0
				&& userGameField.gameField[x][y] != 6
				&& userGameField.gameField[x][y] != 7) {

			if (userGameField.gameField[x][y] == 2) {
				gsu.aiPoints = gsu.aiPoints + 20;
				gsu.aiShoot = 0;
			}
			if (userGameField.gameField[x][y] == 3) {
				gsu.aiPoints = gsu.aiPoints + 16;
				gsu.aiShoot = 0;
			}
			if (userGameField.gameField[x][y] == 4) {
				gsu.aiPoints = gsu.aiPoints + 14;
				gsu.aiShoot = 0;
			}
			if (userGameField.gameField[x][y] == 5) {
				gsu.aiPoints = gsu.aiPoints + 12;
				gsu.aiShoot = 0;
			}

			userGameField.gameField[x][y] = 7;

			gsu.aiTalalat++;
		}

		playerTurn = true;
	}

	void AIPlace() {

		boolean muszaj = true;
		for (int i = 0; i < 5; i++) {
			muszaj = true;
			if (i == 0) {
				int d = rn.nextInt(2);
				int x = rn.nextInt(10 - i - 1);
				int y = rn.nextInt(10 - i - 1);
				if (d == 0) {
					for (int j = 0; j < i + 2; j++) {
						enemyGameField.gameField[x][y + j] = i + 2;
					}
				} else {
					for (int j = 0; j < i + 2; j++) {
						enemyGameField.gameField[x + j][y] = i + 2;
					}
				}
			} else {
				while (muszaj) {
					int d = rn.nextInt(2);
					int x = rn.nextInt(10 - i);
					int y = rn.nextInt(10 - i);
					if (d == 0) {
						for (int j = 0; j < i + 1; j++) {
							if (enemyGameField.gameField[x][y + j] != 0) {
								muszaj = true;
							} else {
								muszaj = false;
								enemyGameField.gameField[x][y + j] = i + 1;
							}
						}
					} else {
						for (int j = 0; j < i + 1; j++) {
							if (enemyGameField.gameField[x + j][y] != 0) {
								muszaj = true;
							} else {
								muszaj = false;
								enemyGameField.gameField[x + j][y] = i + 1;
							}
						}
					}
				}
			}
		}
		for (int i = 0; i < 10; i++) {
			for (int j = 0; j < 10; j++) {
				if (enemyGameField.gameField[i][j] != 0) {
					gsu.enemyShipNumber++;
				}
			}

		}
	}

}
