package com.example.winsontju;

import android.app.Activity;
import android.os.Bundle;
import android.view.WindowManager;
import android.widget.ImageView;


public class Instruction extends Activity {
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN,
                WindowManager.LayoutParams.FLAG_FULLSCREEN);
        setContentView(R.layout.instruction_activity);

    }


}



