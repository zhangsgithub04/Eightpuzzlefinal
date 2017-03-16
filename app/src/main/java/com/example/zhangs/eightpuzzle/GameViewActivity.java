package com.example.zhangs.eightpuzzle;

import android.content.Context;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.Gravity;
import android.view.View;
import android.view.animation.TranslateAnimation;
import android.widget.Button;
import android.widget.Toast;

public class GameViewActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setContentView(R.layout.activity_game_view);


    }

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
}
