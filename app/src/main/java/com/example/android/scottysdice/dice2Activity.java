package com.example.android.scottysdice;

import android.content.Intent;
import android.content.res.Resources;
import android.graphics.drawable.Drawable;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import com.example.android.scottysdice.R;

import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import java.util.Random;

public class dice2Activity extends AppCompatActivity {

    private static final String EXTRA_MESSAGE = "com.example.android.scottysdice.MESSAGE";

    private static int pTotalScore       = 0;
    private static int pTurnScore        = 0;
    private static int cTotalScore       = 0;
    private static int cTurnScore        = 0;
    private static boolean computersTurn = false;

    private static Random random;

    //views
    private static TextView score;
    private static TextView turnScore;

    private static ImageView dice1;
    private static ImageView dice2;

    private static Button ROLL;
    private static Button HOLD;
    private static Button RESET;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_dice2);



        score     = (TextView) findViewById(R.id.score);
        turnScore = (TextView) findViewById(R.id.turnScore);
        dice1       = (ImageView) findViewById(R.id.dice1);
        dice2       = (ImageView) findViewById(R.id.dice2);

        ROLL      = (Button)    findViewById(R.id.roll_button);
        HOLD      = (Button)    findViewById(R.id.hold_button);
        RESET     = (Button)    findViewById(R.id.reset_button);

        pTotalScore       = 0;
        pTurnScore        = 0;
        cTotalScore       = 0;
        cTurnScore        = 0;

        random    = new Random();

        Intent two = getIntent();


    }

    //                               Helper Functions
    //--------------------------------------------------------------------------------------------

    public void rollDie(View view){
        Resources res  = getResources();
        int diceRoll1  = random.nextInt(6) + 1;
        int diceRoll2  = random.nextInt(6) + 1;

        //update image
        String dice1Image = "dice" + diceRoll1;
        String dice2Image = "dice" + diceRoll2;

        int resID1 = res.getIdentifier(dice1Image , "drawable", getPackageName());
        dice1.setImageResource(resID1);

        int resID2 = res.getIdentifier(dice2Image , "drawable", getPackageName());
        dice2.setImageResource(resID2);

        //Game Logic
        if(diceRoll1 == diceRoll2){
            pTurnScore  += diceRoll1 + diceRoll2;
            pTotalScore += pTurnScore;
            updateTotalScore();
            updateTurnScore();
        }
        if(diceRoll1 != 1 && diceRoll2 !=1){
            pTurnScore += diceRoll1 + diceRoll2;
            updateTurnScore();
        }

        if(diceRoll1 == 1 || diceRoll2 == 1){
            pTurnScore   = 0;
            computerTurn();
        }
        if(diceRoll1 == 1 && diceRoll2 == 1){
            pTotalScore = 0;
            computerTurn();
        }
        else {
            pTurnScore   = 0;
            computerTurn();
        }

    }

    public void reset(View view){

        pTotalScore = 0;
        pTurnScore  = 0;
        cTotalScore = 0;
        cTurnScore  = 0;

        updateTotalScore();
        updateTurnScore();

        computersTurn = false;

    }

    public void hold(View view){
        //used to switch turns
        if(!computersTurn) {
            computersTurn = true;
        } else {
            computersTurn = false;
        }

        updateTotalScore();
        updateTurnScore();

        if(pTotalScore > 99) gameOver();
        pTurnScore = 0;

        computerTurn();
    }

    public void computerTurn(){

        int computerRoll         = random.nextInt(6) + 1;

        disableButtons(computersTurn);

        while(computerRoll!=1){
            cTurnScore += computerRoll;
            updateTurnScore();

            //computer holds
            if(cTurnScore > 10){
                cTotalScore += cTurnScore;
                cTurnScore = 0;
                updateTotalScore();
                break;
            }
            computerRoll  = random.nextInt(6) + 1;

        }

        //update label to indicate player turn
        computersTurn = false;

        updateTurnScore();

        disableButtons(true);

        //update Total score of computer
        if(cTotalScore > 99) gameOver();
    }

    /** Must set global variables before use */
    public void updateTotalScore(){
        if(!computersTurn){
            cTotalScore += cTurnScore;

        } else {
            pTotalScore += pTurnScore;
        }

        score.setText("Your score: " + pTotalScore + "- Computer score: " + cTotalScore);
    }

    /** Must set global variables before use */
    public void updateTurnScore(){
        if(computersTurn){
            turnScore.setText("Computers Turn: " + cTurnScore);
        } else {
            turnScore.setText("your Turn: " + pTurnScore);

        }
    }

    /** will disable/enable buttons based on boolean parameter ***/
    public void disableButtons(boolean enabled){
        ROLL.setEnabled(enabled);
        HOLD.setEnabled(enabled);
        RESET.setEnabled(enabled);
    }

    public void gameOver(){

        Intent finish         = new Intent(this, GameOverActivity.class);
        TextView gameOverText = (TextView) findViewById(R.id.gameOverText);
        String gameScore      = "Your score: " + pTotalScore + "- Computer score: " + cTotalScore;
        finish.putExtra(EXTRA_MESSAGE, gameScore);
        startActivity(finish);

    }

}
