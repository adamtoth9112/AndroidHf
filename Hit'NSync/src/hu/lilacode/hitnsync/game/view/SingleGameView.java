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

	protected Bitmap splash1;
	protected Bitmap splash2;
	protected Bitmap splash3;

	protected Bitmap ship2;
	protected Bitmap ship3;
	protected Bitmap ship4;
	protected Bitmap ship5;

	protected Bitmap shootShip2;
	protected Bitmap shootShip3;
	protected Bitmap shootShip4;
	protected Bitmap shootShip5;

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
		splash1 = BitmapFactory.decodeResource(res, R.drawable.mellea);
		splash2 = BitmapFactory.decodeResource(res, R.drawable.melleb);
		splash3 = BitmapFactory.decodeResource(res, R.drawable.mellec);

		ship2 = BitmapFactory.decodeResource(res, R.drawable.kettes);
		ship3 = BitmapFactory.decodeResource(res, R.drawable.harmas);
		ship4 = BitmapFactory.decodeResource(res, R.drawable.negyes);
		ship5 = BitmapFactory.decodeResource(res, R.drawable.otos);

		shootShip2 = BitmapFactory.decodeResource(res, R.drawable.kettest);
		shootShip3 = BitmapFactory.decodeResource(res, R.drawable.harmast);
		shootShip4 = BitmapFactory.decodeResource(res, R.drawable.negyest);
		shootShip5 = BitmapFactory.decodeResource(res, R.drawable.otost);

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

		for (Ship s : ships) {
			float w = (getWidth() * 0.47f / 10);
			float h = (getHeight() * 0.8f / 10);
			if (s.coord[0].x == (4 * w) && s.coord[0].y == (10 * h)) {
				s.drawShip(canvas);
			}
		}

		userGameField.drawField(canvas);
		enemyGameField.drawField(canvas);

		drawUserField(canvas);
		drawEnemyField(canvas);

		drawRandomUserShoot(canvas);
		drawRandomAIShoot(canvas);
	}

	private void drawBackGround(Canvas canvas) {
		int h = (int) getHeight() / ocean.getHeight();
		int w = (int) getWidth() / ocean.getWidth();

		for (int i = 0; i <= h; i++)
			for (int j = 0; j <= w; j++)
				canvas.drawBitmap(ocean, ocean.getWidth() * j,
						ocean.getHeight() * i, null);
	}

	private void drawUserField(Canvas canvas) {
		float X = (float) (getWidth() * 0.47f / 10.0);
		float Y = (float) (getHeight() * 0.8f / 10.0);
		float deltaX = X / 4;
		float deltaY = Y / 4;
		for (int i = 0; i < 10; i++) {
			for (int j = 0; j < 10; j++) {
				if (userGameField.gameField[i][j] == 2) {
					float sx = X * i + deltaX;
					float sy = Y * j + deltaY;
					canvas.drawBitmap(ship2, sx, sy, null);
				}
				if (userGameField.gameField[i][j] == 3) {
					float sx = X * i + deltaX;
					float sy = Y * j + deltaY;
					canvas.drawBitmap(ship3, sx, sy, null);
				}
				if (userGameField.gameField[i][j] == 4) {
					float sx = X * i + deltaX;
					float sy = Y * j + deltaY;
					canvas.drawBitmap(ship4, sx, sy, null);
				}
				if (userGameField.gameField[i][j] == 5) {
					float sx = X * i + deltaX;
					float sy = Y * j + deltaY;
					canvas.drawBitmap(ship5, sx, sy, null);
				}

				if (userGameField.gameField[i][j] == 10) {
					float sx = X * i + deltaX;
					float sy = Y * j + deltaY;
					canvas.drawBitmap(splash1, sx, sy, null);
				}
				if (userGameField.gameField[i][j] == 11) {
					float sx = X * i + deltaX;
					float sy = Y * j + deltaY;
					canvas.drawBitmap(splash2, sx, sy, null);
				}
				if (userGameField.gameField[i][j] == 12) {
					float sx = X * i + deltaX;
					float sy = Y * j + deltaY;
					canvas.drawBitmap(splash3, sx, sy, null);
				}

				if (userGameField.gameField[i][j] == 22) {
					float sx = X * i + deltaX;
					float sy = Y * j + deltaY;
					canvas.drawBitmap(shootShip2, sx, sy, null);
				}
				if (userGameField.gameField[i][j] == 23) {
					float sx = X * i + deltaX;
					float sy = Y * j + deltaY;
					canvas.drawBitmap(shootShip3, sx, sy, null);
				}
				if (userGameField.gameField[i][j] == 24) {
					float sx = X * i + deltaX;
					float sy = Y * j + deltaY;
					canvas.drawBitmap(shootShip4, sx, sy, null);
				}
				if (userGameField.gameField[i][j] == 25) {
					float sx = X * i + deltaX;
					float sy = Y * j + deltaY;
					canvas.drawBitmap(shootShip5, sx, sy, null);
				}
			}
		}
	}

	private void drawEnemyField(Canvas canvas) {
		float X = (float) (getWidth() * 0.47f / 10.0);
		float Y = (float) (getHeight() * 0.8f / 10.0);
		float deltaX = X / 4;
		float deltaY = Y / 4;
		for (int i = 0; i < 10; i++) {
			for (int j = 0; j < 10; j++) {
				if (enemyGameField.gameField[i][j] == 22) {
					float sx = X * (i + 11) + deltaX;
					float sy = Y * j + deltaY;
					canvas.drawBitmap(shootShip2, sx, sy, null);
				}
				if (enemyGameField.gameField[i][j] == 23) {
					float sx = X * (i + 11) + deltaX;
					float sy = Y * j + deltaY;
					canvas.drawBitmap(shootShip3, sx, sy, null);
				}
				if (enemyGameField.gameField[i][j] == 24) {
					float sx = X * (i + 11) + deltaX;
					float sy = Y * j + deltaY;
					canvas.drawBitmap(shootShip4, sx, sy, null);
				}
				if (enemyGameField.gameField[i][j] == 25) {
					float sx = X * (i + 11) + deltaX;
					float sy = Y * j + deltaY;
					canvas.drawBitmap(shootShip5, sx, sy, null);
				}

				if (enemyGameField.gameField[i][j] == 10) {
					float sx = X * (i + 11) + deltaX;
					float sy = Y * j + deltaY;
					canvas.drawBitmap(splash1, sx, sy, null);
				}
				if (enemyGameField.gameField[i][j] == 11) {
					float sx = X * (i + 11) + deltaX;
					float sy = Y * j + deltaY;
					canvas.drawBitmap(splash2, sx, sy, null);
				}
				if (enemyGameField.gameField[i][j] == 12) {
					float sx = X * (i + 11) + deltaX;
					float sy = Y * j + deltaY;
					canvas.drawBitmap(splash3, sx, sy, null);
				}
			}
		}
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
					if (EnemyGameField.gameField[x - 11][y] == 10
							|| EnemyGameField.gameField[x - 11][y] == 11
							|| EnemyGameField.gameField[x - 11][y] == 12
							|| EnemyGameField.gameField[x - 11][y] == 22
							|| EnemyGameField.gameField[x - 11][y] == 23
							|| EnemyGameField.gameField[x - 11][y] == 24
							|| EnemyGameField.gameField[x - 11][y] == 25) {

					} else {
						gsu.userShoot++;
						gsu.userTotalShoot++;
						if (EnemyGameField.gameField[x - 11][y] == 0) {
							int min = 10;
							int max = 13;
							int rnn = min + (rn.nextInt(max - min));
							EnemyGameField.gameField[x - 11][y] = rnn;
							if (gsu.userShoot == 5) {
								gsu.userPoints = gsu.userPoints - 5;
								gsu.userShoot = 0;
							} else {
								gsu.userPoints--;
							}
							player.points = gsu.userPoints;
						}
						if (EnemyGameField.gameField[x - 11][y] != 0
								&& EnemyGameField.gameField[x - 11][y] != 10
								&& EnemyGameField.gameField[x - 11][y] != 11
								&& EnemyGameField.gameField[x - 11][y] != 12) {
							if (EnemyGameField.gameField[x - 11][y] == 2) {
								gsu.userPoints = gsu.userPoints + 20;
								gsu.userShoot = 0;
								EnemyGameField.gameField[x - 11][y] = 22;
							}
							if (EnemyGameField.gameField[x - 11][y] == 3) {
								gsu.userPoints = gsu.userPoints + 16;
								gsu.userShoot = 0;
								EnemyGameField.gameField[x - 11][y] = 23;
							}
							if (EnemyGameField.gameField[x - 11][y] == 4) {
								gsu.userPoints = gsu.userPoints + 14;
								gsu.userShoot = 0;
								EnemyGameField.gameField[x - 11][y] = 24;
							}
							if (EnemyGameField.gameField[x - 11][y] == 5) {
								gsu.userPoints = gsu.userPoints + 12;
								gsu.userShoot = 0;
								EnemyGameField.gameField[x - 11][y] = 25;
							}

							gsu.userTalalat++;

							player.points = gsu.userPoints;

							invalidate();
						}

						if (gsu.userPoints >= 10 && gsu.userTizpont == false) {
							gsu.randomUserLoves++;
							gsu.userTizpont = true;
						}

						if (gsu.userPoints >= 50 && gsu.userOtvenpont == false) {
							gsu.randomUserLoves++;
							gsu.userOtvenpont = true;
						}

						AIShoot();
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

	void AIShoot() {
		int x = 0;
		int y = 0;
		boolean lo = true;

		playerTurn = false;

		synchronized (this) {
			try {
				this.wait(500);
			} catch (InterruptedException e) {
			}
		}

		while (lo) {
			x = rn.nextInt(10);
			y = rn.nextInt(10);
			if (userGameField.gameField[x][y] == 10
					|| userGameField.gameField[x][y] == 11
					|| userGameField.gameField[x][y] == 12
					|| userGameField.gameField[x][y] == 22
					|| userGameField.gameField[x][y] == 23
					|| userGameField.gameField[x][y] == 24
					|| userGameField.gameField[x][y] == 25) {
				lo = true;
			} else {
				lo = false;
			}
		}

		gsu.aiShoot++;
		gsu.aiTotalShoot++;

		if (userGameField.gameField[x][y] == 0) {
			int min = 10;
			int max = 13;
			int rnn = min + (rn.nextInt(max - min));
			userGameField.gameField[x][y] = rnn;
			userGameField.gameField[x][y] = rnn;
			if (gsu.aiShoot == 5) {
				gsu.aiPoints = gsu.aiPoints - 5;
				gsu.aiShoot = 0;
			} else {
				gsu.aiPoints--;
			}
		}

		if (userGameField.gameField[x][y] != 0
				&& userGameField.gameField[x][y] != 10
				&& userGameField.gameField[x][y] != 11
				&& userGameField.gameField[x][y] != 12) {

			if (userGameField.gameField[x][y] == 2) {
				gsu.aiPoints = gsu.aiPoints + 20;
				gsu.aiShoot = 0;
				userGameField.gameField[x][y] = 22;
			}
			if (userGameField.gameField[x][y] == 3) {
				gsu.aiPoints = gsu.aiPoints + 16;
				gsu.aiShoot = 0;
				userGameField.gameField[x][y] = 23;
			}
			if (userGameField.gameField[x][y] == 4) {
				gsu.aiPoints = gsu.aiPoints + 14;
				gsu.aiShoot = 0;
				userGameField.gameField[x][y] = 24;
			}
			if (userGameField.gameField[x][y] == 5) {
				gsu.aiPoints = gsu.aiPoints + 12;
				gsu.aiShoot = 0;
				userGameField.gameField[x][y] = 25;
			}
			if (gsu.aiPoints >= 10 && gsu.aiTizpont == false) {
				gsu.randomAILoves++;
				gsu.aiTizpont = true;
			}

			if (gsu.aiPoints >= 50 && gsu.aiOtvenpont == false) {
				gsu.randomAILoves++;
				gsu.aiOtvenpont = true;
			}
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

	void userShoot() {
		int x = 0;
		int y = 0;
		boolean lo = true;

		playerTurn = false;

		synchronized (this) {
			try {
				this.wait(250);
			} catch (InterruptedException e) {
			}
		}

		while (lo) {
			x = rn.nextInt(10);
			y = rn.nextInt(10);
			if (enemyGameField.gameField[x][y] == 10
					|| enemyGameField.gameField[x][y] == 11
					|| enemyGameField.gameField[x][y] == 12
					|| enemyGameField.gameField[x][y] == 22
					|| enemyGameField.gameField[x][y] == 23
					|| enemyGameField.gameField[x][y] == 24
					|| enemyGameField.gameField[x][y] == 25) {
				lo = true;
			} else {
				lo = false;
			}
		}

		gsu.userShoot++;
		gsu.userTotalShoot++;

		if (enemyGameField.gameField[x][y] == 0) {
			int min = 10;
			int max = 13;
			int rnn = min + (rn.nextInt(max - min));
			enemyGameField.gameField[x][y] = rnn;
			enemyGameField.gameField[x][y] = rnn;
			if (gsu.userShoot == 5) {
				gsu.userPoints = gsu.userPoints - 5;
				gsu.userShoot = 0;
			} else {
				gsu.userPoints--;
			}
		}

		if (enemyGameField.gameField[x][y] != 0
				&& enemyGameField.gameField[x][y] != 10
				&& enemyGameField.gameField[x][y] != 11
				&& enemyGameField.gameField[x][y] != 12) {

			if (enemyGameField.gameField[x][y] == 2) {
				gsu.userPoints = gsu.userPoints + 20;
				gsu.userShoot = 0;
				enemyGameField.gameField[x][y] = 22;
			}
			if (enemyGameField.gameField[x][y] == 3) {
				gsu.userPoints = gsu.userPoints + 16;
				gsu.userShoot = 0;
				enemyGameField.gameField[x][y] = 23;
			}
			if (enemyGameField.gameField[x][y] == 4) {
				gsu.userPoints = gsu.userPoints + 14;
				gsu.userShoot = 0;
				enemyGameField.gameField[x][y] = 24;
			}
			if (enemyGameField.gameField[x][y] == 5) {
				gsu.userPoints = gsu.userPoints + 12;
				gsu.userShoot = 0;
				enemyGameField.gameField[x][y] = 25;
			}

			gsu.userTalalat++;
		}

		playerTurn = true;
		invalidate();
	}

	public void drawRandomUserShoot(Canvas canvas) {
		if (gsu.randomUserLoves > 0 && SingleGameActivity.randomLoves == true) {
			for (int i = 0; i < 3; i++) {
				userShoot();
			}
			float X = (float) (getWidth() * 0.47f / 10.0);
			float Y = (float) (getHeight() * 0.8f / 10.0);
			float deltaX = X / 4;
			float deltaY = Y / 4;
			for (int i = 0; i < 10; i++) {
				for (int j = 0; j < 10; j++) {
					if (enemyGameField.gameField[i][j] == 22) {
						float sx = X * (i + 11) + deltaX;
						float sy = Y * j + deltaY;
						canvas.drawBitmap(shootShip2, sx, sy, null);
					}
					if (enemyGameField.gameField[i][j] == 23) {
						float sx = X * (i + 11) + deltaX;
						float sy = Y * j + deltaY;
						canvas.drawBitmap(shootShip3, sx, sy, null);
					}
					if (enemyGameField.gameField[i][j] == 24) {
						float sx = X * (i + 11) + deltaX;
						float sy = Y * j + deltaY;
						canvas.drawBitmap(shootShip4, sx, sy, null);
					}
					if (enemyGameField.gameField[i][j] == 25) {
						float sx = X * (i + 11) + deltaX;
						float sy = Y * j + deltaY;
						canvas.drawBitmap(shootShip5, sx, sy, null);
					}

					if (enemyGameField.gameField[i][j] == 10) {
						float sx = X * (i + 11) + deltaX;
						float sy = Y * j + deltaY;
						canvas.drawBitmap(splash1, sx, sy, null);
					}
					if (enemyGameField.gameField[i][j] == 11) {
						float sx = X * (i + 11) + deltaX;
						float sy = Y * j + deltaY;
						canvas.drawBitmap(splash2, sx, sy, null);
					}
					if (enemyGameField.gameField[i][j] == 12) {
						float sx = X * (i + 11) + deltaX;
						float sy = Y * j + deltaY;
						canvas.drawBitmap(splash3, sx, sy, null);
					}
				}
			}
			gsu.randomUserLoves--;
		}
		invalidate();

	}

	public void drawRandomAIShoot(Canvas canvas) {
		int rnn;
		rnn = rn.nextInt(7);
		if (gsu.randomAILoves > 0 && ((rnn == 1) || (rnn == 5))) {
			System.out.print("ELEGEG RANDOM");
			for (int i = 0; i < 3; i++) {
				AIShoot();
			}
			float X = (float) (getWidth() * 0.47f / 10.0);
			float Y = (float) (getHeight() * 0.8f / 10.0);
			float deltaX = X / 4;
			float deltaY = Y / 4;
			for (int i = 0; i < 10; i++) {
				for (int j = 0; j < 10; j++) {
					if (userGameField.gameField[i][j] == 22) {
						float sx = X * (i) + deltaX;
						float sy = Y * j + deltaY;
						canvas.drawBitmap(shootShip2, sx, sy, null);
					}
					if (userGameField.gameField[i][j] == 23) {
						float sx = X * (i) + deltaX;
						float sy = Y * j + deltaY;
						canvas.drawBitmap(shootShip3, sx, sy, null);
					}
					if (userGameField.gameField[i][j] == 24) {
						float sx = X * (i) + deltaX;
						float sy = Y * j + deltaY;
						canvas.drawBitmap(shootShip4, sx, sy, null);
					}
					if (userGameField.gameField[i][j] == 25) {
						float sx = X * (i) + deltaX;
						float sy = Y * j + deltaY;
						canvas.drawBitmap(shootShip5, sx, sy, null);
					}

					if (userGameField.gameField[i][j] == 10) {
						float sx = X * (i) + deltaX;
						float sy = Y * j + deltaY;
						canvas.drawBitmap(splash1, sx, sy, null);
					}
					if (userGameField.gameField[i][j] == 11) {
						float sx = X * (i) + deltaX;
						float sy = Y * j + deltaY;
						canvas.drawBitmap(splash2, sx, sy, null);
					}
					if (userGameField.gameField[i][j] == 12) {
						float sx = X * (i) + deltaX;
						float sy = Y * j + deltaY;
						canvas.drawBitmap(splash3, sx, sy, null);
					}
				}
			}
			gsu.randomAILoves--;
		}
		invalidate();
	}

}
