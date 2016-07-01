package com.example.android.scottysdice;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;

public class GameOverActivity extends AppCompatActivity {

    Button gameOver;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_game_over);

        gameOver = (Button) findViewById(R.id.startOverButton);

        Intent finish = getIntent();
        Intent start  = getIntent();
    }

    public void startOver(View view){
        Intent main = new Intent(getApplicationContext(), MainActivity.class);
        startActivity(main);

    }
}
