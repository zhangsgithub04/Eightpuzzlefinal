package com.example.zhangs.eightpuzzle;

import android.content.Context;
import android.graphics.drawable.Drawable;
import android.hardware.Sensor;
import android.hardware.SensorEvent;
import android.hardware.SensorEventListener;
import android.hardware.SensorManager;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.GestureDetector;
import android.view.Gravity;
import android.view.MotionEvent;
import android.view.View;
import android.view.animation.TranslateAnimation;
import android.widget.Button;
import android.widget.Toast;

import static android.R.attr.duration;

public class GameViewActivity extends AppCompatActivity implements View.OnClickListener, GestureDetector.OnGestureListener, SensorEventListener {

    NPuzzleGame npg = new NPuzzleGame();
    Button[][] buttons = new Button[3][3];

    //private float xPos;
    //private float yPos;

    GestureDetector gestureDetector;


    // The following are used for the shake detection
    private SensorManager mSensorManager;
    private ShakeDetector mShakeDetector;
    private Sensor mAccelerometer;
    private Sensor magnetometer;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);


        setContentView(R.layout.activity_game_view);
        //after the view has been set!
        /*
        Button b00=(Button)findViewById(R.id.b00);

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

        magnetometer = mSensorManager.getDefaultSensor(Sensor.TYPE_MAGNETIC_FIELD);

        initListeners();

        mAccelerometer = mSensorManager
                .getDefaultSensor(Sensor.TYPE_ACCELEROMETER);
        mShakeDetector = new ShakeDetector();
        mShakeDetector.setOnShakeListener(new ShakeDetector.OnShakeListener() {



            @Override
            public void onShake(int count) {
				/* whatever you want done once the
				 * device has been shook.
				 */

                switch (count)
                {

                    case 1:
                        npg.reset();
                        redraw();
                        break;

                    case 2:
                        npg.randomize(20);
                        redraw();
                        break;

                    default:
                        npg.reset();
                        redraw();
                        break;

                }
            }
        });

    }


    public void initListeners()
    {
        mSensorManager.registerListener(this, mAccelerometer, SensorManager.SENSOR_DELAY_FASTEST);
        mSensorManager.registerListener(this, magnetometer, SensorManager.SENSOR_DELAY_FASTEST);
    }

    @Override
    public void onDestroy()
    {
        mSensorManager.unregisterListener(this);
        super.onDestroy();
    }

    @Override
    public void onBackPressed()
    {
        mSensorManager.unregisterListener(this);
        super.onBackPressed();
    }



    float[] inclineGravity = new float[3];
    float[] mGravity;
    float[] mGeomagnetic;
    float orientation[] = new float[3];
    float pitch;
    float roll;

    @Override
    public void onSensorChanged(SensorEvent event) {
        //If type is accelerometer only assign values to global property mGravity
        if (event.sensor.getType() == Sensor.TYPE_ACCELEROMETER)
        {
            mGravity = event.values;
        }
        else if (event.sensor.getType() == Sensor.TYPE_MAGNETIC_FIELD)
        {
            mGeomagnetic = event.values;

            if (isTiltDownward())
            {
                goUp(null);
                Log.d("test", "downwards");
            }
            else if (isTiltUpward())
            {
                goDown(null);
                Log.d("test", "upwards");
            }
            else if (isTiltLeftward())
            {
                goRight(null);
                Log.d("test", "downwards");
            }

            else if (isTiltRightward())
            {
                goLeft(null);
                Log.d("test", "downwards");
            }
        }

    }

    @Override
    public void onAccuracyChanged(Sensor sensor, int accuracy) {
        // TODO Auto-generated method stub

    }

    public boolean isTiltUpward()
    {
        if (mGravity != null && mGeomagnetic != null)
        {
            float R[] = new float[9];
            float I[] = new float[9];

            boolean success = SensorManager.getRotationMatrix(R, I, mGravity, mGeomagnetic);

            if (success)
            {
                float orientation[] = new float[3];
                SensorManager.getOrientation(R, orientation);

                /*
                * If the roll is positive, you're in reverse landscape (landscape right), and if the roll is negative you're in landscape (landscape left)
                *
                * Similarly, you can use the pitch to differentiate between portrait and reverse portrait.
                * If the pitch is positive, you're in reverse portrait, and if the pitch is negative you're in portrait.
                *
                * orientation -> azimut, pitch and roll
                *
                *
                */

                pitch = orientation[1];
                roll = orientation[2];

                inclineGravity = mGravity.clone();

                double norm_Of_g = Math.sqrt(inclineGravity[0] * inclineGravity[0] + inclineGravity[1] * inclineGravity[1] + inclineGravity[2] * inclineGravity[2]);

                // Normalize the accelerometer vector
                inclineGravity[0] = (float) (inclineGravity[0] / norm_Of_g);
                inclineGravity[1] = (float) (inclineGravity[1] / norm_Of_g);
                inclineGravity[2] = (float) (inclineGravity[2] / norm_Of_g);

                //Checks if device is flat on ground or not
                int inclination = (int) Math.round(Math.toDegrees(Math.acos(inclineGravity[1])));

                /*
                * Float obj1 = new Float("10.2");
                * Float obj2 = new Float("10.20");
                * int retval = obj1.compareTo(obj2);
                *
                * if(retval > 0) {
                * System.out.println("obj1 is greater than obj2");
                * }
                * else if(retval < 0) {
                * System.out.println("obj1 is less than obj2");
                * }
                * else {
                * System.out.println("obj1 is equal to obj2");
                * }
                */
                Float objPitch = new Float(pitch);
                Float objZero = new Float(0.0);
                Float objZeroPointTwo = new Float(0.2);
                Float objZeroPointTwoNegative = new Float(-0.2);

                int objPitchZeroResult = objPitch.compareTo(objZero);
                int objPitchZeroPointTwoResult = objZeroPointTwo.compareTo(objPitch);
                int objPitchZeroPointTwoNegativeResult = objPitch.compareTo(objZeroPointTwoNegative);

                //if (roll < 0 && ((objPitchZeroResult > 0 && objPitchZeroPointTwoResult > 0)
                //        || (objPitchZeroResult < 0 && objPitchZeroPointTwoNegativeResult > 0)) && (inclination > 30 && inclination < 40))

                if (inclination > 30 && inclination < 35)
                {
                    return true;
                }
                else
                {
                    return false;
                }
            }
        }

        return false;
    }

    public boolean isTiltDownward()
    {
        if (mGravity != null && mGeomagnetic != null)
        {
            float R[] = new float[9];
            float I[] = new float[9];

            boolean success = SensorManager.getRotationMatrix(R, I, mGravity, mGeomagnetic);

            if (success)
            {
                float orientation[] = new float[3];
                SensorManager.getOrientation(R, orientation);

                pitch = orientation[1];
                roll = orientation[2];

                inclineGravity = mGravity.clone();

                double norm_Of_g = Math.sqrt(inclineGravity[0] * inclineGravity[0] + inclineGravity[1] * inclineGravity[1] + inclineGravity[2] * inclineGravity[2]);

                // Normalize the accelerometer vector
                inclineGravity[0] = (float) (inclineGravity[0] / norm_Of_g);
                inclineGravity[1] = (float) (inclineGravity[1] / norm_Of_g);
                inclineGravity[2] = (float) (inclineGravity[2] / norm_Of_g);

                //Checks if device is flat on groud or not
                int inclination = (int) Math.round(Math.toDegrees(Math.acos(inclineGravity[1])));

                Float objPitch = new Float(pitch);
                Float objZero = new Float(0.0);
                Float objZeroPointTwo = new Float(0.2);
                Float objZeroPointTwoNegative = new Float(-0.2);

                int objPitchZeroResult = objPitch.compareTo(objZero);
                int objPitchZeroPointTwoResult = objZeroPointTwo.compareTo(objPitch);
                int objPitchZeroPointTwoNegativeResult = objPitch.compareTo(objZeroPointTwoNegative);

                //if (roll < 0 && ((objPitchZeroResult > 0 && objPitchZeroPointTwoResult > 0)
                //        || (objPitchZeroResult < 0 && objPitchZeroPointTwoNegativeResult > 0)) && (inclination > 140 && inclination < 160))
                 if   (inclination > 145 && inclination < 160)
                {
                    return true;
                }
                else
                {
                    return false;
                }
            }
        }

        return false;
    }

    public boolean isTiltLeftward()
    {
        if (mGravity != null && mGeomagnetic != null)
        {
            float R[] = new float[9];
            float I[] = new float[9];

            boolean success = SensorManager.getRotationMatrix(R, I, mGravity, mGeomagnetic);

            if (success)
            {
                float orientation[] = new float[3];
                SensorManager.getOrientation(R, orientation);

                pitch = orientation[1];
                roll = orientation[2];

                inclineGravity = mGravity.clone();

                double norm_Of_g = Math.sqrt(inclineGravity[0] * inclineGravity[0] + inclineGravity[1] * inclineGravity[1] + inclineGravity[2] * inclineGravity[2]);

                // Normalize the accelerometer vector
                inclineGravity[0] = (float) (inclineGravity[0] / norm_Of_g);
                inclineGravity[1] = (float) (inclineGravity[1] / norm_Of_g);
                inclineGravity[2] = (float) (inclineGravity[2] / norm_Of_g);

                //Checks if device is flat on groud or not
                int inclination = (int) Math.round(Math.toDegrees(Math.acos(inclineGravity[2])));

                Float objPitch = new Float(pitch);
                Float objZero = new Float(0.0);
                Float objZeroPointTwo = new Float(0.2);
                Float objZeroPointTwoNegative = new Float(-0.2);

                int objPitchZeroResult = objPitch.compareTo(objZero);
                int objPitchZeroPointTwoResult = objZeroPointTwo.compareTo(objPitch);
                int objPitchZeroPointTwoNegativeResult = objPitch.compareTo(objZeroPointTwoNegative);

                //if (roll < 0 && ((objPitchZeroResult > 0 && objPitchZeroPointTwoResult > 0)
                //        || (objPitchZeroResult < 0 && objPitchZeroPointTwoNegativeResult > 0)) && (inclination > 140 && inclination < 160))
                if   (inclination > 145 && inclination < 160)
                {
                    return true;
                }
                else
                {
                    return false;
                }
            }
        }

        return false;
    }

    public boolean isTiltRightward()
    {
        if (mGravity != null && mGeomagnetic != null)
        {
            float R[] = new float[9];
            float I[] = new float[9];

            boolean success = SensorManager.getRotationMatrix(R, I, mGravity, mGeomagnetic);

            if (success)
            {
                float orientation[] = new float[3];
                SensorManager.getOrientation(R, orientation);

                pitch = orientation[1];
                roll = orientation[2];

                inclineGravity = mGravity.clone();

                double norm_Of_g = Math.sqrt(inclineGravity[0] * inclineGravity[0] + inclineGravity[1] * inclineGravity[1] + inclineGravity[2] * inclineGravity[2]);

                // Normalize the accelerometer vector
                inclineGravity[0] = (float) (inclineGravity[0] / norm_Of_g);
                inclineGravity[1] = (float) (inclineGravity[1] / norm_Of_g);
                inclineGravity[2] = (float) (inclineGravity[2] / norm_Of_g);

                //Checks if device is flat on groud or not
                int inclination = (int) Math.round(Math.toDegrees(Math.acos(inclineGravity[2])));

                Float objPitch = new Float(pitch);
                Float objZero = new Float(0.0);
                Float objZeroPointTwo = new Float(0.2);
                Float objZeroPointTwoNegative = new Float(-0.2);

                int objPitchZeroResult = objPitch.compareTo(objZero);
                int objPitchZeroPointTwoResult = objZeroPointTwo.compareTo(objPitch);
                int objPitchZeroPointTwoNegativeResult = objPitch.compareTo(objZeroPointTwoNegative);

                //if (roll < 0 && ((objPitchZeroResult > 0 && objPitchZeroPointTwoResult > 0)
                //        || (objPitchZeroResult < 0 && objPitchZeroPointTwoNegativeResult > 0)) && (inclination > 140 && inclination < 160))
                if   (inclination > 30 && inclination < 40)
                {
                    return true;
                }
                else
                {
                    return false;
                }
            }
        }

        return false;
    }


    @Override
    public void onResume() {
        initListeners();
        super.onResume();
        // Add the following line to register the Session Manager Listener onResume
        mSensorManager.registerListener(mShakeDetector, mAccelerometer,	SensorManager.SENSOR_DELAY_UI);
    }


    @Override
    public void onPause() {
        // Add the following line to unregister the Sensor Manager onPause
        mSensorManager.unregisterListener(this);
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
        redraw();
    }

    private boolean isadjacent(int r1, int c1, int r2, int c2)
    {
      if (((r1==r2)&&Math.abs(c1-c2)==1)||((c1==c2)&&Math.abs(r1-r2)==1))
            return true;
        else
            return false;

    }

    public void reSet(View view) {

        npg.reset();
        redraw();

    }


    public void randomize(View view) {


        npg.randomize(10);
        redraw();

    }

    private void redraw()
    {
        for(int row=0; row<npg.SIZE; row++)
            for(int col=0; col<npg.SIZE; col++) {
                if (npg.gamestate[row][col]==0) buttons[row][col].setBackground(null);
                else {
                    //int suffix = row * npg.SIZE + col;
                    int suffix = npg.gamestate[row][col];
                    String mDrawableName = "pieces_" + suffix;
                    int imageResource = getResources().getIdentifier(mDrawableName, "drawable", getPackageName());
                    Drawable pic = getResources().getDrawable(imageResource);
                    // Though decrepted, this works. If you, my students, find the right function, let me know.
                    buttons[row][col].setBackground(pic);
                }
                buttons[row][col].setText(String.valueOf(npg.gamestate[row][col]));
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
