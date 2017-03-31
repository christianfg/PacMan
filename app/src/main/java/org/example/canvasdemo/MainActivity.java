package org.example.canvasdemo;

import android.app.Activity;
import android.content.Context;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import java.io.Console;
import java.util.ArrayList;
import java.util.Random;
import java.util.Timer;
import java.util.TimerTask;

public class MainActivity extends Activity {

    private Timer myTimer;
    private Timer roundTime;
    private Timer enemyTimer;

    private int roundCounter = 60;
    private int counter = 0;
    private int enemyCounter = 0;
    private boolean running = false;
    private boolean paused = false;
    private int direction = 0;
    static Context context;

	MyView myView;
    TextView timerView;
    TextView roundView;
    TextView pointView;

    //GOLD
    public static ArrayList<Goldcoin> coins = new ArrayList<Goldcoin>();

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_main);
        context = this;



        //RANDOM CORDS
        Random rn = new Random();
        int randomx = (rn.nextInt(1000));
        int randomy = (rn.nextInt(1000));

        //GOLD
        coins.add(new Goldcoin(randomx, randomy, false));


        timerView = (TextView) findViewById(R.id.timerView);
        roundView = (TextView) findViewById(R.id.roundView);
        pointView = (TextView) findViewById(R.id.pointView);
        myView = (MyView) findViewById(R.id.gameView);
        myView.setActivity(this);

        //NEW GAME BTN
        Button newgameButton = (Button) findViewById(R.id.newGame);
        newgameButton.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View v) {

                myView.newGame();

                Random rn = new Random();
                int randomx = (rn.nextInt(1000));
                int randomy = (rn.nextInt(1000));

                //GOLD
                coins.clear();
                coins.add(new Goldcoin(randomx, randomy, false));
            }
        });


		//CONTROLS
        Button upButton = (Button) findViewById(R.id.upButton);
        upButton.setOnClickListener(new OnClickListener() {

            @Override
            public void onClick(View v) {
                startGame();
                direction = 3;
            }
        });

        Button downButton = (Button) findViewById(R.id.downButton);
        downButton.setOnClickListener(new OnClickListener() {

            @Override
            public void onClick(View v) {
                startGame();
                direction = 4;
            }
        });

        Button leftButton = (Button) findViewById(R.id.leftButton);
        leftButton.setOnClickListener(new OnClickListener() {

            @Override
            public void onClick(View v) {
                startGame();
                direction = 2;
            }
        });

        Button rightButton = (Button) findViewById(R.id.rightButton);
        rightButton.setOnClickListener(new OnClickListener() {
			
			@Override
			public void onClick(View v) {
                startGame();
                direction = 1;
			}
		});

        Button pauseButton = (Button) findViewById(R.id.pauseGame);
        pauseButton.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View v) {
                if(running)
                {
                    running = false;
                    paused = true;
                }
                else
                {
                    running = true;
                    paused = false;
                }
            }
        });

        //RUNDE NEDTÆLNING
        roundTime = new Timer();
        roundTime.schedule(new TimerTask() {
            @Override
            public void run() {
                runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        if(!paused && running) {
                            if(roundCounter > -1)
                            {
                                roundView.setText("Time left: "+roundCounter);
                                roundCounter--;
                            }
                            else if(roundCounter <= 0)
                            {
                                gameOver();
                            }
                        }
                    }
                });
            }
        }, 1000, 1000);

        //PACMAN TIMER
        myTimer = new Timer();

        myTimer.schedule(new TimerTask() {
            @Override
            public void run() {
                TimerMethod();
            }

        }, 0, 80); //0 indicates we start now, 200
        //is the number of miliseconds between each call
	}

	public void resetCounters()
    {
        counter = 0;
        roundCounter = 60;
        roundView.setText("Time left: "+roundCounter);

        running = false;
    }

    public void startGame()
    {
        running = true;
    }

    public int getRoundCounter()
    {
        return roundCounter;
    }

    public void nextCoin()
    {
        Random rn = new Random();
        int randomx = (rn.nextInt(1000));
        int randomy = (rn.nextInt(1000));
        coins.add(new Goldcoin(randomx, randomy, false));
    }

    public void gameOver()
    {
        Toast toast = Toast.makeText(context,
                "GAME OVER!", Toast.LENGTH_LONG);
        toast.show();
        myView.newGame();
    }

    public void nextLevelToast()
    {
        Toast toast = Toast.makeText(context,
                "Congratulations! You made it to the next level. Try to collect even more coins on less time. Good luck", Toast.LENGTH_LONG);
        toast.show();
    }

    public void levelUpTime(int x)
    {
        roundCounter = roundCounter - x;
        roundView.setText("Time left: "+roundCounter);
    }

    public void gameCompleted()
    {
        Toast toast = Toast.makeText(context,
                "Amazing! You just beat the game. Good job!", Toast.LENGTH_LONG);
        toast.show();
    }

    @Override
    protected void onStop() {
        super.onStop();
        myTimer.cancel();
        enemyTimer.cancel();
        //just to make sure if the app is killed, that we stop the timer.

    }

    private void TimerMethod()
    {
        //This method is called directly by the timer
        //and runs in the same thread as the timer.

        //We call the method that will work with the UI
        //through the runOnUiThread method.
        this.runOnUiThread(Timer_Tick);
        this.runOnUiThread(Timer_Tick2);
    }

    private Runnable Timer_Tick = new Runnable() {
        public void run() {
            if (running)
            {
                counter++;
                //update the counter - notice this is NOT seconds in this example
                //you need TWO counters - one for the time and one for the pacman
                //timerView.setText("Timer value: "+counter);

                if (direction == 1)
                {
                    myView.moveRight(25);
                }
                else if (direction == 2)
                {
                    myView.moveLeft(25);
                }
                else if (direction == 3)
                {
                    myView.moveUp(25);
                }
                else if (direction == 4)
                {
                    myView.moveDown(25);
                }
            }
        }
    };


    //ENEMYTIMER
    private Runnable Timer_Tick2 = new Runnable() {
        public void run() {
            if (running)
            {
                enemyCounter++;
                myView.moveEnemy(25);
            }
            if (running && myView.getCurrentLevel() == 4)
            {
                enemyCounter++;
                myView.moveEnemy(30);
            }
        }
    };


    public TextView getPointView()
    {
        return pointView;
    }

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		// Inflate the menu; this adds items to the action bar if it is present.
		getMenuInflater().inflate(R.menu.main, menu);
		return true;
	}

	@Override
	public boolean onOptionsItemSelected(MenuItem item) {
		// Handle action bar item clicks here. The action bar will
		// automatically handle clicks on the Home/Up button, so long
		// as you specify a parent activity in AndroidManifest.xml.
		int id = item.getItemId();
		if (id == R.id.action_settings) {
			return true;
		}
		return super.onOptionsItemSelected(item);
	}
}
