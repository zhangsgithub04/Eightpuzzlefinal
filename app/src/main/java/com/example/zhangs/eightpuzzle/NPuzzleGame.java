package com.example.zhangs.eightpuzzle;

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

}
