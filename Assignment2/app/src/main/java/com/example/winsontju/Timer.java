package com.example.winsontju;

import android.os.CountDownTimer;
import android.widget.EditText;

public class Timer extends CountDownTimer {
    EditText timerText;
    GameActivity theActivity;

    public Timer(long numOfSeconds, long interval, EditText text, GameActivity theActivity){
        super(numOfSeconds,interval);
        this.timerText = text;
        this.theActivity = theActivity;
    }

    public void onTick(long millisUntilFinished){
        timerText.setText(millisUntilFinished/1000 + " sec");
    }

    public void onFinish(){
        timerText.setText(0 + " sec");
        theActivity.timeOutForGame();
    }
}
