package com.example.winsontju;

import android.content.Context;
import android.graphics.drawable.Drawable;

import androidx.annotation.RequiresApi;
import androidx.appcompat.widget.AppCompatButton;

import android.os.Build;
import android.widget.GridLayout;

public class MemoryButton extends AppCompatButton {

    protected int row;
    protected int col;
    protected int frontImageID;

    protected boolean isFlipped = false;
    protected boolean isMatched = false;

    protected Drawable front;
    protected Drawable back;

    @RequiresApi(api = Build.VERSION_CODES.LOLLIPOP)
    public MemoryButton(Context context, int row, int col, int frontImageID ){
        super(context);

        this.row = row;
        this.col = col;
        this.frontImageID = frontImageID;

        front = context.getDrawable(frontImageID);
        back = context.getDrawable(R.drawable.dolphin);

        setBackground(back);

        GridLayout.LayoutParams tempParams = new GridLayout.LayoutParams(GridLayout.spec(row), GridLayout.spec(col));

        tempParams.width = (int) getResources().getDisplayMetrics().density * 80;
        tempParams.height = (int) getResources().getDisplayMetrics().density * 80;

        setLayoutParams(tempParams);
    }

    public boolean isMatched() {
        return isMatched;
    }

    public void setMatched(boolean matched) {
        isMatched = matched;
    }

    public int getFrontImageID() {
        return frontImageID;
    }

    @RequiresApi(api = Build.VERSION_CODES.JELLY_BEAN)
    public void flip(){
            setBackground(back);
            isFlipped = false;
    }

    @RequiresApi(api = Build.VERSION_CODES.JELLY_BEAN)
    public void open(){
            setBackground(front);
            isFlipped = true;
    }

}
