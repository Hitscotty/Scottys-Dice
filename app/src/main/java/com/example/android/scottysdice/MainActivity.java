package com.example.android.scottysdice;

import android.content.Intent;
import android.content.res.Resources;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;

import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import java.util.Random;

public class MainActivity extends AppCompatActivity {

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

    private static ImageView img;

    private static Button ROLL;
    private static Button HOLD;
    private static Button RESET;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);



        score     = (TextView) findViewById(R.id.score);
        turnScore = (TextView) findViewById(R.id.turnScore);
        img       = (ImageView) findViewById(R.id.dice1);
        ROLL      = (Button)    findViewById(R.id.roll_button);
        HOLD      = (Button)    findViewById(R.id.hold_button);
        RESET     = (Button)    findViewById(R.id.reset_button);

        pTotalScore       = 0;
        pTurnScore        = 0;
        cTotalScore       = 0;
        cTurnScore        = 0;

        random    = new Random();


    }

    public boolean onCreateOptionsMenu(Menu menu){
        MenuInflater inflater = getMenuInflater();
        inflater.inflate(R.menu.menu,menu);
        return true;
    }

    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.action_settings) {
            Log.d("press","worked");
            Intent launchNewIntent = new Intent(MainActivity.this,dice2Activity.class);
            startActivity(launchNewIntent);
            return true;
        }

        return true;
    }

    //                               Helper Functions
    //--------------------------------------------------------------------------------------------

    public void rollDie(View view){
        Resources res  = getResources();
        int diceRoll  = random.nextInt(6) + 1;

        //update image
        String diceImage =  "dice" + diceRoll;
        int resID = res.getIdentifier(diceImage , "drawable", getPackageName());
        img.setImageResource(resID);

        //Game Logic
        if(diceRoll != 1){
            pTurnScore += diceRoll;
            turnScore.setText("Your turn score: " + pTurnScore);

        } else {
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
