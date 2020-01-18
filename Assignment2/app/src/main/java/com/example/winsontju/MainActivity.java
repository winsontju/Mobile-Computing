package com.example.winsontju;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.view.WindowManager;
import android.widget.ImageButton;

import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.content.ContextCompat;

public class MainActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN,
                WindowManager.LayoutParams.FLAG_FULLSCREEN);
        setContentView(R.layout.activity_main);

        showActionBar();

    }

    public void playGame(View view){
        Intent intent = new Intent(this,GameActivity.class);
        startActivity(intent);
    }

    public void instructionButton(View view){
        Intent intent = new Intent(this,Instruction.class);
        startActivity(intent);
    }

    public void exitProgram(View view){
        finish();
    }

    public void showActionBar() {
        //Method used to create the custom action bar for leaderboards and setting
        ActionBar actionBar = getSupportActionBar();
        assert actionBar != null;
        actionBar.setDisplayShowHomeEnabled(false);
        actionBar.setDisplayShowTitleEnabled(false);
        actionBar.setDisplayShowCustomEnabled(true);
        actionBar.setElevation(0);
        actionBar.setBackgroundDrawable(ContextCompat.getDrawable(this,R.drawable.action_bar));

        //The null error can be ignored as the version used need some modification
        View actionBarLayout = getLayoutInflater().inflate(R.layout.topbar,null);
        actionBar.setCustomView(actionBarLayout);

        ImageButton leaderboardsButton = (ImageButton) actionBarLayout.findViewById(R.id.leaderboards_button);
        leaderboardsButton.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View view) {
                Intent intent = new Intent(MainActivity.this, LeaderboardsActivity.class);
                startActivity(intent);
            }
        });

        ImageButton settingButton = (ImageButton) actionBarLayout.findViewById(R.id.setting_button);
        settingButton.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View view) {
                Intent intent = new Intent(MainActivity.this, Settings.class);
                startActivity(intent);
            }
        });

    }
}
