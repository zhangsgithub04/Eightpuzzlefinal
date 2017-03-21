package com.example.zhangs.eightpuzzle;

/**
 * Created by ZHANGS on 3/20/2017.
 */

public class NPuzzleGame {
    int [][]gamestate =new int[3][3];

    int br=0, bc=0;

    int SIZE=3;

    public NPuzzleGame()
    {
        for(int row=0; row<SIZE; row++)
            for(int col=0; col<SIZE; col++)
            {
                gamestate[row][col]=row*3+col;

            }
        br=0;
        bc=0;

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
