package com.example.zhangs.eightpuzzle;

import java.util.ArrayList;
import java.util.Random;

/**
 * Created by ZHANGS on 3/20/2017.
 */

public class NPuzzleGame {

    int SIZE=3;

    int [][]gamestate =new int[SIZE][SIZE];

    int br=0, bc=0;

    public void reset()
    {
        for(int row=0; row<SIZE; row++)
            for(int col=0; col<SIZE; col++)
            {
                gamestate[row][col]=row*SIZE+col;

            }
        br=0;
        bc=0;


    }

    public NPuzzleGame()
    {
        reset();
    }


    public void swap(int wr, int wc)
    {
        int tmp=gamestate[wr][wc];
        gamestate[wr][wc]=gamestate[br][bc];
        gamestate[br][bc]=tmp;

        br=wr;
        bc=wc;
    }

    public void randomize(int strength)
    {


        ArrayList<position> neighbours = new ArrayList<position>();

        // create random object
        Random randomno = new Random();

        int [][] offset= new int[4][2];
        // above
        offset[0][0]=-1;
        offset[0][1]=0;
        //right
        offset[1][0]=0;
        offset[1][1]=1;
        //below
        offset[2][0]=1;
        offset[2][1]=0;
        //left
        offset[3][0]=0;
        offset[3][1]=-1;


        for(int n=0; n<strength; n++) {
            // find all neighbours of the blank tile
            neighbours.clear();
            for(int i=0; i<4; i++)
            {
                int nr=br+offset[i][0];
                int nc=bc+offset[i][1];
                if (islegal(nr,nc))
                {
                    neighbours.add(new position(nr,nc));
                }
            }


            int whichone = randomno.nextInt(neighbours.size());
            swap(((position)(neighbours.get(whichone))).row, neighbours.get(whichone).col);
        }
    }

    boolean islegal(int r, int c)
    {
        return (r>=0&&r<SIZE&&c>=0&&c<SIZE);

    }

    // inner class
    class position
    {
        int row;
        int col;

        public position(int wr, int wc)
        {
            row=wr;
            col=wc;
        }
    }


}
