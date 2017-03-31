package org.example.canvasdemo;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.util.AttributeSet;
import android.view.View;
import android.widget.TextView;


import java.util.ArrayList;
import java.util.Random;

import static java.lang.Math.sqrt;
import static org.example.canvasdemo.MainActivity.coins;
import static org.example.canvasdemo.R.string.points;

public class MyView extends View{

	TextView pointView;
	MainActivity activity;

	public void setActivity(MainActivity activity)
	{
		this.activity = activity;
	}


	//PACMAN
	Bitmap pacman = BitmapFactory.decodeResource(getResources(), R.drawable.pacman);
    //The coordinates for our dear pacman: (0,0) is the top-left corner
	int pacx = 50;
    int pacy = 400;
    int h,w; //used for storing our height and width


	//LEVELS
	int lvlOneCounter = 6;
	int lvlTwoCounter = 12;
	int lvlThreeCounter = 24;
	int currentLevel = 2;


	//GOLD
	Bitmap goldcoin = BitmapFactory.decodeResource(getResources(), R.drawable.gold);
	int coinCounter = 0;

	//ENEMY
	Bitmap enemy = BitmapFactory.decodeResource(getResources(), R.drawable.ghost);
	Random rn = new Random();

	int enemyx = rn.nextInt(1000);
	int enemyy = rn.nextInt(1000);
	int ghostDirection = 1;

	Enemy e1 = new Enemy(enemyx, enemyy, false, 20);

	public void moveEnemy(int x)
	{
		if(e1.directionCounter < 0)
		{
			ghostDirection = rn.nextInt(4) + 1;
			e1.directionCounter = rn.nextInt(40);
		}
		else
		{
			e1.directionCoundown();
		}

		if (w==0) return;

		if (ghostDirection == 1) // RIGHT
		{
			enemyx = enemyx + x;
			invalidate();
		}
		else if (ghostDirection == 2) // LEFT
		{
			enemyx = enemyx - x;
			invalidate();
		}
		else if (ghostDirection == 3) //UP
		{
			enemyy = enemyy - x;
			invalidate();
		}
		else if (ghostDirection == 4) //DOWN
		{
			enemyy = enemyy + x;
			invalidate();
		}

		if (enemyx+x+enemy.getWidth() > w)
		{
			ghostDirection = 2;
		}
		if (enemyx < 0)
		{
			ghostDirection = 1;
		}
		if (enemyy+x+enemy.getHeight() > h)
		{
			ghostDirection = 3;
		}
		if (enemyy < 0)
		{
			ghostDirection = 4;
		}
	}
    
    public void moveUp(int x)
    {
    	if (pacy > 0)
    		pacy=pacy-x;
    	invalidate();
    }

	public void moveDown(int x)
	{
		if (pacy+x+pacman.getHeight()<h)
			pacy=pacy+x;
		invalidate();
	}

	public void moveLeft(int x)
	{
		if (pacx > 0)
			pacx=pacx-x;
		invalidate();
	}

	public void moveRight(int x)
	{
		if (pacx+x+pacman.getWidth()<w)
			pacx=pacx+x;
		invalidate();
	}

	//NEW GAME
	public void newGame()
	{
		activity.resetCounters();
		pacx = 50;
		enemyx = rn.nextInt(1000);
		enemyy = rn.nextInt(1000);
		invalidate();
		coinCounter = 0;
		TextView pointView = activity.getPointView();
		pointView.setText("Coins: " +coinCounter);
		currentLevel = 2;
	}

	public int getCurrentLevel()
	{
		return currentLevel;
	}


	/* The next 3 constructors are needed for the Android view system,
	when we have a custom view.
	 */
	public MyView(Context context) {
		super(context);
	}
	
	public MyView(Context context, AttributeSet attrs) {
		super(context,attrs);
	}
	
	
	public MyView(Context context, AttributeSet attrs, int defStyleAttr)
	{
		super(context,attrs,defStyleAttr);
	}

	//In the onDraw we put all our code that should be
	//drawn whenever we update the screen.

	@Override
	protected void onDraw(Canvas canvas) {
		//Here we get the height and weight
		h = canvas.getHeight();
		w = canvas.getWidth();
		//Making a new paint object
		Paint paint = new Paint();
		//setting the color
		paint.setColor(Color.RED);
		canvas.drawColor(Color.WHITE); //clear entire canvas to white color
		paint.setColor(Color.GREEN);

		//setting the color using the format: Transparency, Red, Green, Blue
		paint.setColor(0xff000099);

		boolean allTaken = true;
		for (Goldcoin g : coins) {
			int centerxpacman = pacx+pacman.getWidth()/2;
			int centerypacman = pacy+pacman.getHeight()/2;
			int centerxgold = g.getGoldx()+goldcoin.getWidth()/2;
			int centerygold = g.getGoldy()+goldcoin.getHeight()/2;
			double distance = Math.sqrt(Math.pow(centerxpacman-centerxgold,2)+Math.pow(centerypacman-centerygold, 2));

			if (g.taken == false) {
				canvas.drawBitmap(goldcoin, g.getGoldx(), g.getGoldy(), paint);
				allTaken = false;
			}
			if((!g.taken) && (distance < 55)) {
				g.taken = true;
				activity.nextCoin();
				coinCounter++;
				TextView text = activity.getPointView();
				text.setText("Coins: " +coinCounter);
			}

		}
		if (allTaken)
		{
			newGame();
		}


		if(currentLevel == 2 && coinCounter >= lvlOneCounter && activity.getRoundCounter() > 0)
		{
			newGame();
			activity.nextLevelToast();
			currentLevel = 3;
			activity.levelUpTime(10);
		}

		if(currentLevel == 3 && coinCounter >= lvlTwoCounter && activity.getRoundCounter() > 0)
		{
			newGame();
			activity.nextLevelToast();
			currentLevel = 4;
			activity.levelUpTime(20);
		}

		if(currentLevel == 4 && coinCounter >= lvlThreeCounter && activity.getRoundCounter() > 0)
		{
			newGame();
			activity.gameCompleted();
			currentLevel = 2;
		}

		int centerxpacman = pacx+pacman.getWidth()/2;
		int centerypacman = pacy+pacman.getHeight()/2;
		int centerxenemy = enemyx+enemy.getWidth()/2;
		int centeryenemy = enemyy+enemy.getHeight()/2;
		double enemyDistance = Math.sqrt(Math.pow(centerxpacman-centerxenemy,2)+Math.pow(centerypacman-centeryenemy, 2));
		if (enemyDistance < 100)
		{
			activity.gameOver();
		}


		canvas.drawBitmap(pacman, pacx, pacy, paint);
		canvas.drawBitmap(enemy, enemyx, enemyy, paint);

		super.onDraw(canvas);
	}
}
