package com.example.winsontju;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.hardware.Sensor;
import android.hardware.SensorManager;
import android.media.MediaPlayer;
import android.os.Build;
import android.os.Bundle;
import android.os.Handler;
import android.view.MotionEvent;
import android.view.View;
import android.view.WindowManager;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.EditText;
import android.widget.GridLayout;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.RequiresApi;
import androidx.appcompat.app.AppCompatActivity;

import java.util.Hashtable;
import java.util.Random;


public class GameActivity extends AppCompatActivity implements  View.OnClickListener {


    private int choose, move;
    private int numOfElements;
    private  MemoryButton [] allButtons;
    private int [] allButtonsGraphics;
    private MemoryButton selectButton1;
    private MemoryButton selectButton2;
    private boolean isBusy = false;
    final String LEVEL = "LEVEL";
    final String USER_NAME = "name";
    final String USER_AGE = "age";
    final String FOR_TIMER = "forTimer";
    private int sizeOfMat;
    private int numOfSeconds;
    private EditText name_txt;
    private int [] allButtonsGraphicLocation; //random all the location
    private EditText age_txt;
    private EditText time_text;
    private TextView questions;
    private String userName;
    private String userAge;
    private int[] timerSeconds = {30, 45, 60};
    private Timer timer;
    private SharedPreferences settings;
    private String current_question;
    private int current_answer;
    private String[] question_list;
    private Hashtable<String, Integer> answer_dictionary;
    private int score;
    TextView score_point;
    Database db;
    private SensorManager mSensorManager;
    private Sensor mAccelerometer;
    private ShakeDevice mShakeDetector;
    String musicStatus;
    private MediaPlayer mp;
    private SoundManager soundManager;

    @RequiresApi(api = Build.VERSION_CODES.LOLLIPOP)
    @Override
        protected void onCreate(Bundle savedInstanceState) {
        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN,
                WindowManager.LayoutParams.FLAG_FULLSCREEN);
        super.onCreate(savedInstanceState);
        setContentView(R.layout.game_activity);

        settings = getSharedPreferences("Settings", MODE_PRIVATE);
        //Preparing the settings for the game
        soundManager = new SoundManager(this);
        choose = soundManager.addSound(R.raw.choose_sound);
        move = soundManager.addSound(R.raw.move_sound);

        //Set the music to loop
        mp = MediaPlayer.create(getApplicationContext(), R.raw.game_music);
        mp.setLooping(true);
        musicStatus = settings.getString("Music","On");

        //To play the music or not
        switch (musicStatus){
            case "On":
                mp.start();
                break;
            case "Off":
                break;
        }


        Hashtable<String, Integer> easy = new Hashtable<>();
        // put() method
        easy.put("2 x 3", R.drawable.button_6);
        easy.put("1 x 2", R.drawable.button_2);
        easy.put("2 x 4", R.drawable.button_8);
        easy.put("2 x 2", R.drawable.button_4);
        easy.put("1 x 5", R.drawable.button_5);
        easy.put("8 / 2", R.drawable.button_4);
        easy.put("9 / 3", R.drawable.button_3);
        easy.put("4 / 2", R.drawable.button_2);
        easy.put("24 / 4", R.drawable.button_6);
        easy.put("24 / 3", R.drawable.button_8);
        easy.put("30 / 15", R.drawable.button_2);
        easy.put("25 / 5", R.drawable.button_5);
        easy.put("27 / 3", R.drawable.button_9);
        easy.put("1 + 2 + 3", R.drawable.button_6);
        easy.put("4 + 4", R.drawable.button_8);
        easy.put("5 + 2 + 2", R.drawable.button_9);
        easy.put("2 + 4", R.drawable.button_6);
        easy.put("3 + 1 + 1", R.drawable.button_5);
        easy.put("2 + 1 + 4", R.drawable.button_7);
        easy.put("7 - 3", R.drawable.button_4);
        easy.put("15 - 8", R.drawable.button_7);
        easy.put("17 - 3 - 5", R.drawable.button_9);
        easy.put("20 - 3 - 15", R.drawable.button_2);
        easy.put("15 - 4 - 4", R.drawable.button_7);
        easy.put("20 - 15", R.drawable.button_5);


        Hashtable<String, Integer> medium = new Hashtable<>();
        medium.put("2 x 2 + 1", R.drawable.button_5);
        medium.put("2 x 3 + 1", R.drawable.button_7);
        medium.put("2 x 4 + 5", R.drawable.button_13);
        medium.put("2 x 3 + 8", R.drawable.button_14);
        medium.put("1 x 1 + 1", R.drawable.button_3);
        medium.put("2 x 2 + 2", R.drawable.button_6);
        medium.put("4 x 4 + 4", R.drawable.button_20);
        medium.put("50 / 25", R.drawable.button_2);

        score_point  = (TextView) findViewById(R.id.points);
        score = 0;
        db = new Database(this);

        Hashtable<String, Integer> difficult =  new Hashtable<>();
        difficult.put("2 x 2 + 1 + 1", R.drawable.button_6);
        difficult.put("2 x 3 + 1 + 2", R.drawable.button_9);
        difficult.put("2 x 4 + 5 + 3", R.drawable.button_16);
        difficult.put("2 x 3 + 8 + 4", R.drawable.button_18);
        difficult.put("1 x 1 + 1 + 2", R.drawable.button_5);
        difficult.put("2 x 2 + 2 + 3", R.drawable.button_9);
        difficult.put("4 x 4 + 4 + 2", R.drawable.button_22);
        difficult.put("50 / 25 + 3", R.drawable.button_5);


        bindUI();


        String currentDifficulty = settings.getString("Difficulty","Easy");
        GridLayout theGridLayout = (GridLayout)findViewById(R.id.game_layout);
        switch(currentDifficulty){
            case "Easy":
                question_list = easy.keySet().toArray(new String[easy.size()]);
                answer_dictionary = new Hashtable<String, Integer>();
                answer_dictionary = (Hashtable<String, Integer>)easy.clone();
                theGridLayout.setPadding(250,0,0,0);
                break;
            case "Normal":
                question_list = medium.keySet().toArray(new String[medium.size()]);
                answer_dictionary = new Hashtable<String, Integer>();
                answer_dictionary = (Hashtable<String, Integer>)medium.clone();
                theGridLayout.setPadding(150,0,0,0);
                break;
            case "Hard":
                question_list = difficult.keySet().toArray(new String[difficult.size()]);
                answer_dictionary = new Hashtable<String, Integer>();
                answer_dictionary = (Hashtable<String, Integer>)difficult.clone();
                break;
        }

        switch(currentDifficulty){
            case "Easy":
                sizeOfMat = 3;
                break;
            case "Normal":
                sizeOfMat = 4;
                break;
            case "Hard":
                sizeOfMat = 5;
                break;
        }
        int numCol =sizeOfMat;
        int numRow = sizeOfMat;
        this.numOfElements = numCol * numRow;
        this.allButtons = new MemoryButton[numOfElements];
        this.allButtonsGraphics = new int [numOfElements];
        if(numRow == 3){
            putAllButtonsGraphicForEasy();
        }else if(numRow == 4){
            putAllButtonsGraphicForMedium();
        }else{
            putAllButtonsGraphicForHard();
        }
        this.allButtonsGraphicLocation = new int [numOfElements];
        shuffleButtonGraphics();
        initeMemoryButtons(numRow,numCol,theGridLayout);
       // final Animation animation = AnimationUtils.loadAnimation(this, R.anim.fade);
        for (MemoryButton butt : this.allButtons) {
            //butt.startAnimation(animation);
            butt.open();
        }
        Handler handler = new Handler();
        handler.postDelayed(new Runnable() {
            public void run() {
                for (MemoryButton butt : GameActivity.this.allButtons) {
                    butt.flip();
                }
            }
        }, 3000);
        initForTimer(numOfSeconds + 4, time_text);
        get_question_answer();
    }

    @RequiresApi(api = Build.VERSION_CODES.JELLY_BEAN)
    private void get_question_answer(){
        current_question = question_list[new Random().nextInt(question_list.length)];
        questions.setText(current_question);
        current_answer = answer_dictionary.get(current_question);
        System.out.println(current_answer);
        Handler handler = new Handler();
        handler.postDelayed(new Runnable() {
            public void run() {
                for (MemoryButton butt : GameActivity.this.allButtons) {
                    butt.flip();
                }
            }
        }, 3000);

    }

    private void initForTimer(long numOfSeconds, EditText timerText) {
        timer = new Timer(numOfSeconds * 1000, 1000, timerText, this);
        timer.start();
    }



        @RequiresApi(api = Build.VERSION_CODES.LOLLIPOP)
        public void initeMemoryButtons(int numRow, int numCol, GridLayout theGridLayout){
            for (int row = 0; row < numRow ; row++){
                for(int col = 0 ; col <numCol ; col++){
                    MemoryButton tempButton = new MemoryButton(this,row,col,allButtonsGraphics[allButtonsGraphicLocation[row *numCol +col]]);
                    tempButton.setId(View.generateViewId());
                    tempButton.setOnClickListener(this);
                    allButtons[row * numCol +col] = tempButton;
                    theGridLayout.addView(tempButton);
                }
            }
        }

        protected void shuffleButtonGraphics(){
            Random rand = new Random();

            for (int i = 0; i < numOfElements ; i++ ){
                this.allButtonsGraphicLocation[i] = i % (numOfElements);
            }
            for (int i = 0; i < numOfElements ; i++ ){//swap location
                int temp = this.allButtonsGraphicLocation[i];
                if(numOfElements == 9){
                    int swapIndex = rand.nextInt(9);
                    allButtonsGraphicLocation[i] = allButtonsGraphicLocation[swapIndex];
                    allButtonsGraphicLocation[swapIndex] = temp;
                }else if(numOfElements == 16){
                    int swapIndex = rand.nextInt(16);
                    allButtonsGraphicLocation[i] = allButtonsGraphicLocation[swapIndex];
                    allButtonsGraphicLocation[swapIndex] = temp;
                }else{
                    int swapIndex = rand.nextInt(25);
                    allButtonsGraphicLocation[i] = allButtonsGraphicLocation[swapIndex];
                    allButtonsGraphicLocation[swapIndex] = temp;
                }

            }
        }

        public void onBackPressed(){
            timer.cancel();
            helperForMenu();
        }

    private void bindUI() {
        questions  = (TextView) findViewById(R.id.questions);
        time_text = (EditText) findViewById(R.id.time_game);
       // userName = getIntent().getStringExtra(USER_NAME).toString();
       // name_txt.setText("Name: " + userName);
       // userAge = getIntent().getStringExtra(USER_AGE).toString();
       // age_txt.setText("Age: " + userAge);
        int level = getIntent().getIntExtra(FOR_TIMER, 0);
        numOfSeconds = timerSeconds[level];
    }

    public void putAllButtonsGraphicForEasy(){
        this.allButtonsGraphics[0] = R.drawable.button_1;
        this.allButtonsGraphics[1] = R.drawable.button_2;
        this.allButtonsGraphics[2] = R.drawable.button_3;
        this.allButtonsGraphics[3] = R.drawable.button_4;
        this.allButtonsGraphics[4] = R.drawable.button_5;
        this.allButtonsGraphics[5] = R.drawable.button_6;
        this.allButtonsGraphics[6] = R.drawable.button_7;
        this.allButtonsGraphics[7] = R.drawable.button_8;
        this.allButtonsGraphics[8] = R.drawable.button_9;
    }

    public void putAllButtonsGraphicForMedium(){
        this.allButtonsGraphics[0] = R.drawable.button_1;
        this.allButtonsGraphics[1] = R.drawable.button_2;
        this.allButtonsGraphics[2] = R.drawable.button_3;
        this.allButtonsGraphics[3] = R.drawable.button_4;
        this.allButtonsGraphics[4] = R.drawable.button_5;
        this.allButtonsGraphics[5] = R.drawable.button_6;
        this.allButtonsGraphics[6] = R.drawable.button_7;
        this.allButtonsGraphics[7] = R.drawable.button_8;
        this.allButtonsGraphics[8] = R.drawable.button_9;
        this.allButtonsGraphics[9] = R.drawable.button_10;
        this.allButtonsGraphics[10] = R.drawable.button_11;
        this.allButtonsGraphics[11] = R.drawable.button_12;
        this.allButtonsGraphics[12] = R.drawable.button_13;
        this.allButtonsGraphics[13] = R.drawable.button_14;
        this.allButtonsGraphics[14] = R.drawable.button_15;
        this.allButtonsGraphics[15] = R.drawable.button_16;
    }
    public void putAllButtonsGraphicForHard(){
        this.allButtonsGraphics[0] = R.drawable.button_1;
        this.allButtonsGraphics[1] = R.drawable.button_2;
        this.allButtonsGraphics[2] = R.drawable.button_3;
        this.allButtonsGraphics[3] = R.drawable.button_4;
        this.allButtonsGraphics[4] = R.drawable.button_5;
        this.allButtonsGraphics[5] = R.drawable.button_6;
        this.allButtonsGraphics[6] = R.drawable.button_7;
        this.allButtonsGraphics[7] = R.drawable.button_8;
        this.allButtonsGraphics[8] = R.drawable.button_9;
        this.allButtonsGraphics[9] = R.drawable.button_10;
        this.allButtonsGraphics[10] = R.drawable.button_11;
        this.allButtonsGraphics[11] = R.drawable.button_12;
        this.allButtonsGraphics[12] = R.drawable.button_13;
        this.allButtonsGraphics[13] = R.drawable.button_14;
        this.allButtonsGraphics[14] = R.drawable.button_15;
        this.allButtonsGraphics[15] = R.drawable.button_16;
        this.allButtonsGraphics[16] = R.drawable.button_17;
        this.allButtonsGraphics[17] = R.drawable.button_18;
        this.allButtonsGraphics[18] = R.drawable.button_19;
        this.allButtonsGraphics[19] = R.drawable.button_20;
        this.allButtonsGraphics[20] = R.drawable.button_21;
        this.allButtonsGraphics[21] = R.drawable.button_22;
        this.allButtonsGraphics[22] = R.drawable.button_23;
        this.allButtonsGraphics[23] = R.drawable.button_24;
        this.allButtonsGraphics[24] = R.drawable.button_25;

    }

        private boolean checkIfDone() {
            for (int i = 0; i < numOfElements; i++) {
                if (allButtons[i].isEnabled()) {
                    return false;
                }
            }
            return true;
        }

        private void backToMenu() {
            Handler tempHandler = new Handler();
            tempHandler.postDelayed(new Runnable() {
                @Override
                public void run() {
                    helperForMenu();
                }
            }, 3000);

        }

        public void timeOutForGame() {
            Toast.makeText(GameActivity.this, "Time's up", Toast.LENGTH_LONG).show();
            helperForMenu();

            db.addScore(score);

        }

        private void helperForMenu(){
            Intent intent = new Intent(GameActivity.this,MainActivity.class);
        //    intent.putExtra(USER_NAME,userName);
        //    intent.putExtra(USER_AGE,userAge);
            startActivity(intent);
        }

    @RequiresApi(api = Build.VERSION_CODES.JELLY_BEAN)
    @Override
    public void onClick(View view) {

        for (MemoryButton butt : this.allButtons) {
            soundManager.play(choose);
            butt.open();

        }
        final MemoryButton button = (MemoryButton) view;
        final Handler handler = new Handler();
        handler.postDelayed(new Runnable() {
            @Override
            public void run() {
                button.open();
                isBusy = false;
            }
        },500);


        for (MemoryButton butt : this.allButtons) {
            soundManager.play(choose);
            butt.open();
        }


        if(button.getFrontImageID() == current_answer){
            Toast.makeText(GameActivity.this, "Correct", Toast.LENGTH_LONG).show();
            score = score + 5;
        }
        else {
            score -= 2;
            if (score < 0){
                score = 0;
            }
        }
        score_point.setText("Score: "+score);

        get_question_answer();
    }
    public void shakeDevice(){
        //For the shake sensor
        mSensorManager = (SensorManager) getSystemService(Context.SENSOR_SERVICE);
        mAccelerometer = mSensorManager
                .getDefaultSensor(Sensor.TYPE_ACCELEROMETER);
        mShakeDetector = new ShakeDevice();
        mShakeDetector.setOnShakeListener(new ShakeDevice.OnShakeListener() {
            @RequiresApi(api = Build.VERSION_CODES.JELLY_BEAN)
            @Override
            public void onShake(int count) {
                initForTimer(numOfSeconds + 4, time_text);
                get_question_answer();
            }
        });
    }

}
