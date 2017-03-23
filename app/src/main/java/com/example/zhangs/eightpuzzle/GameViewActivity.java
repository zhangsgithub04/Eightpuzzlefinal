package com.example.zhangs.eightpuzzle;

import android.content.Context;
import android.graphics.drawable.Drawable;
import android.hardware.Sensor;
import android.hardware.SensorManager;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.GestureDetector;
import android.view.Gravity;
import android.view.MotionEvent;
import android.view.View;
import android.view.animation.TranslateAnimation;
import android.widget.Button;
import android.widget.Toast;

import static android.R.attr.duration;

public class GameViewActivity extends AppCompatActivity implements View.OnClickListener, GestureDetector.OnGestureListener {

    NPuzzleGame npg = new NPuzzleGame();
    Button[][] buttons = new Button[3][3];

    //private float xPos;
    //private float yPos;

    GestureDetector gestureDetector;


    // The following are used for the shake detection
    private SensorManager mSensorManager;
    private Sensor mAccelerometer;
    private ShakeDetector mShakeDetector;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);


        setContentView(R.layout.activity_game_view);

        //after the view has been set!
        Button b00=(Button)findViewById(R.id.b00);
/*
        b00.setBackgroundResource(R.drawable.pieces_8);

        //String uri="@drawable/pieces_"+"2";
        //int imageResource =getResources().getIdentifier(uri, null, getPackageName());

        String mDrawableName = "pieces_"+"4";
        int imageResource = getResources().getIdentifier(mDrawableName , "drawable", getPackageName());
        Drawable pic=getResources().getDrawable(imageResource);
        //b00.setBackground(pic);
*/

        for (int i = 0; i < npg.SIZE; i++) {
            for (int j = 0; j < npg.SIZE; j++) {

                String buttonID = "b" + i + j;
                int resID = getResources().getIdentifier(buttonID, "id", getPackageName());
                buttons[i][j] = ((Button) findViewById(resID));
                buttons[i][j].setOnClickListener(this);
            }
        }

        gestureDetector = new GestureDetector(this, this);


        // ShakeDetector initialization
        mSensorManager = (SensorManager) getSystemService(Context.SENSOR_SERVICE);
        mAccelerometer = mSensorManager
                .getDefaultSensor(Sensor.TYPE_ACCELEROMETER);
        mShakeDetector = new ShakeDetector();
        mShakeDetector.setOnShakeListener(new ShakeDetector.OnShakeListener() {

            @Override
            public void onShake(int count) {
				/*
				 * The following method, "handleShakeEvent(count):" is a stub //
				 * method you would use to setup whatever you want done once the
				 * device has been shook.
				 */
                switch (count)
                {

                    case 1:
                    npg.reset();
                    redraw();
                        break;
                    case 2:

                    //npg.randomize();
                    //redraw();
                    break;
                    default:
                        npg.reset();
                        redraw();
                        break;

                }
            }
        });

    }

    @Override
    public void onResume() {
        super.onResume();
        // Add the following line to register the Session Manager Listener onResume
        mSensorManager.registerListener(mShakeDetector, mAccelerometer,	SensorManager.SENSOR_DELAY_UI);
    }

    @Override
    public void onPause() {
        // Add the following line to unregister the Sensor Manager onPause
        mSensorManager.unregisterListener(mShakeDetector);
        super.onPause();
    }

    @Override
    public void onClick(View view) {
        int bid = view.getId();
        //Toast toast = Toast.makeText(getApplicationContext(), String.valueOf(bid), Toast.LENGTH_SHORT);
        //toast.setGravity(Gravity.CENTER_VERTICAL, 0, 0);
        //toast.show();

        int wr = -1;
        int wc = -1;

        switch (bid) {
            case R.id.b00:
                wr = 0;
                wc = 0;
                break;
            case R.id.b01:
                wr = 0;
                wc = 1;
                break;
            case R.id.b02:
                wr = 0;
                wc = 2;

                break;
            case R.id.b10:
                wr = 1;
                wc = 0;
                break;
            case R.id.b11:
                wr = 1;
                wc = 1;
                break;
            case R.id.b12:
                wr = 1;
                wc = 2;
                break;
            case R.id.b20:
                wr = 2;
                wc = 0;
                break;
            case R.id.b21:
                wr = 2;
                wc = 1;
                break;
            case R.id.b22:
                wr = 2;
                wc = 2;
                break;

        }

        if (wr > -1 && wc > -1) {
            if (isadjacent(wr, wc, npg.br, npg.bc)) {
                swap(wr, wc);
            }
        }
    }


    private void swap(int wr, int wc)
    {

        Button button1, button2;
        button1 =buttons[wr][wc];
        button2 =buttons[npg.br][npg.bc];
        //int suffix=wr*npg.SIZE+wc;
        int suffix = npg.gamestate[wr][wc];

        String mDrawableName = "pieces_" + suffix;
        int imageResource = getResources().getIdentifier(mDrawableName, "drawable", getPackageName());
        Drawable pic = getResources().getDrawable(imageResource);
        // Though decrepted, this works. If you, my students, find the right function, let me know.
        button2.setBackground(pic);
        button1.setBackground(null);
        npg.swap(wr,wc);
    }

    private boolean isadjacent(int r1, int c1, int r2, int c2)
    {
      if (((r1==r2)&&Math.abs(c1-c2)==1)||((c1==c2)&&Math.abs(r1-r2)==1))
            return true;
        else
            return false;

    }

    public void reSet(View view) {
        // Kabloey
        npg.reset();
        redraw();

    }

    private void redraw()
    {
        for(int row=0; row<npg.SIZE; row++)
            for(int col=0; col<npg.SIZE; col++) {
                if (row==0 &&col==0) buttons[row][col].setBackground(null);
                else {
                    int suffix = row * npg.SIZE + col;
                    String mDrawableName = "pieces_" + suffix;
                    int imageResource = getResources().getIdentifier(mDrawableName, "drawable", getPackageName());
                    Drawable pic = getResources().getDrawable(imageResource);
                    // Though decrepted, this works. If you, my students, find the right function, let me know.
                    buttons[row][col].setBackground(pic);
                }
            }

    }

    @Override
    public boolean onFling(MotionEvent motionEvent1, MotionEvent motionEvent2, float X, float Y) {

            if(motionEvent1.getY() - motionEvent2.getY() > 50){

                Toast.makeText(GameViewActivity.this , " Swipe Up " , Toast.LENGTH_LONG).show();
                goUp(null);

                return true;
            }

            if(motionEvent2.getY() - motionEvent1.getY() > 50){

                Toast.makeText(GameViewActivity.this , " Swipe Down " , Toast.LENGTH_LONG).show();
                goDown(null);
                return true;
            }

            if(motionEvent1.getX() - motionEvent2.getX() > 50){

                Toast.makeText(GameViewActivity.this , " Swipe Left " , Toast.LENGTH_LONG).show();
                goLeft(null);
                return true;
            }

            if(motionEvent2.getX() - motionEvent1.getX() > 50) {

                Toast.makeText(GameViewActivity.this, " Swipe Right ", Toast.LENGTH_LONG).show();
                goRight(null);

                return true;
            }
            else {

                return true ;
            }
        }

        @Override
        public void onLongPress(MotionEvent arg0) {

            // TODO Auto-generated method stub

        }

        @Override
        public boolean onScroll(MotionEvent arg0, MotionEvent arg1, float arg2, float arg3) {

            // TODO Auto-generated method stub

            return false;
        }

        @Override
        public void onShowPress(MotionEvent arg0) {

            // TODO Auto-generated method stub

        }

        @Override
        public boolean onSingleTapUp(MotionEvent arg0) {

            // TODO Auto-generated method stub

            return false;
        }

    @Override
        public boolean onTouchEvent(MotionEvent motionEvent) {

            // TODO Auto-generated method stub

            return gestureDetector.onTouchEvent(motionEvent);
        }


        @Override
        public boolean onDown(MotionEvent arg0) {

            // TODO Auto-generated method stub

            return false;
        }

        // assumming the reference tile is the blank tile
        public void goUp(View view) {

            if (npg.br-1>=0)
            {
                swap(npg.br-1,npg.bc);
            }

        }

        public void goLeft(View view) {


            if (npg.bc-1>=0)
            {
                swap(npg.br,npg.bc-1);
            }

        }

        public void goRight(View view) {
            if (npg.bc+1<npg.SIZE)
            {
                swap(npg.br,npg.bc+1);
            }
        }

        public void goDown(View view) {

            if (npg.br+1<npg.SIZE)
            {
                swap(npg.br+1,npg.bc);
            }


        }


    }
