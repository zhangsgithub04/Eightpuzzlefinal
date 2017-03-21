package com.example.zhangs.eightpuzzle;

import android.content.Context;
import android.graphics.drawable.Drawable;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.Gravity;
import android.view.View;
import android.view.animation.TranslateAnimation;
import android.widget.Button;
import android.widget.Toast;

import static android.R.attr.duration;

public class GameViewActivity extends AppCompatActivity implements View.OnClickListener
    {

    NPuzzleGame npg=new NPuzzleGame();
    Button [][]buttons=new Button[3][3];

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);


        setContentView(R.layout.activity_game_view);

        //after the view has been set!
        Button b00=(Button)findViewById(R.id.b00);
       // b00.setBackgroundResource(R.drawable.pieces_8);

        //String uri="@drawable/pieces_"+"2";
        //int imageResource =getResources().getIdentifier(uri, null, getPackageName());

        String mDrawableName = "pieces_"+"4";
        int imageResource = getResources().getIdentifier(mDrawableName , "drawable", getPackageName());
        Drawable pic=getResources().getDrawable(imageResource);
        //b00.setBackground(pic);


        for(int i=0; i<npg.SIZE; i++) {
            for(int j=0; j<npg.SIZE; j++) {

                String buttonID = "b" + i + j;
                int resID = getResources().getIdentifier(buttonID, "id", getPackageName());
                buttons[i][j] = ((Button) findViewById(resID));
                buttons[i][j].setOnClickListener(this);
            }
        }

    }

        @Override
        public void onClick(View view)
        {
            int bid=view.getId();

                Toast toast = Toast.makeText(getApplicationContext(), String.valueOf(bid), Toast.LENGTH_SHORT);
                toast.setGravity(Gravity.CENTER_VERTICAL, 0, 0);
                toast.show();

                Button button1, button2;
                int wr=-1;
                int wc=-1;

            switch (bid) {
                case R.id.b00:
                    wr=0;
                    wc=0;
                    break;
                case R.id.b01:
                    wr=0;
                    wc=1;
                    break;
                case R.id.b02:
                    wr=0;
                    wc=2;

                    break;
                case R.id.b10:
                    wr=1;
                    wc=0;
                    break;
                case R.id.b11:
                    wr=1;
                    wc=1;
                    break;
                case R.id.b12:
                    wr=1;
                    wc=2;
                    break;
                case R.id.b20:
                    wr=2;
                    wc=0;
                    break;
                case R.id.b21:
                    wr=2;
                    wc=1;
                    break;
                case R.id.b22:
                    wr=2;
                    wc=2;
                    break;

            }

            if (wr>-1 ||wc>-1) {
                if (isadjacent(wr, wc, npg.br, npg.bc)) {
                    button1 = buttons[wr][wc];
                    button2 = buttons[npg.br][npg.bc];
                    int suffix=wr*npg.SIZE+wc;
                    String mDrawableName = "pieces_"+suffix;
                    int imageResource = getResources().getIdentifier(mDrawableName , "drawable", getPackageName());
                    Drawable pic=getResources().getDrawable(imageResource);

                    button2.setBackground(pic);
                    button1.setBackground(null);

                    npg.swap(wr, wc);

                }
            }
        }


    private boolean isadjacent(int r1, int c1, int r2, int c2)
    {
      if (((r1==r2)&&Math.abs(c1-c2)==1)||((c1==c2)&&Math.abs(r1-r2)==1))
            return true;
        else
            return false;

    }

    /*
    public void testmove(View v) {
        // default method for handling onClick Events..
        switch (v.getId()) {

            case R.id.b20:

               // do your code
                Button from=(Button)findViewById(R.id.b20);
                Button to=(Button)findViewById(R.id.b21);

                int start_x; //=from.getLeft();
                int start_y; //=from.getTop();

                int end_x;//=to.getLeft();
                int end_y;//=to.getTop();

                int[] slocation = new int[2];
                //from.getLocationInWindow(slocation);
                from.getLocationOnScreen(slocation);
                start_x=slocation[0];
                start_y=slocation[1];

                int[] elocation = new int[2];
                //to.getLocationInWindow(elocation);
                to.getLocationOnScreen(elocation);
                end_x=elocation[0];
                end_y=elocation[1];

                TranslateAnimation animation = new TranslateAnimation(start_x, start_y, end_x, end_y);
                animation.setDuration(1000); // duartion in ms
                animation.setFillAfter(false);
                from.startAnimation(animation);


                Context context = getApplicationContext();
                CharSequence text = "From: "+start_x+", "+start_y+ " to " +end_x+", " +end_y;
                int duration = Toast.LENGTH_SHORT;

                Toast toast = Toast.makeText(context, text, duration);
                toast.setGravity(Gravity.CENTER_VERTICAL, 0, 0);

                toast.show();

                break;

            case R.id.b21:
                // do your code
                break;



            default:
                break;
        }



    }
    */
}
