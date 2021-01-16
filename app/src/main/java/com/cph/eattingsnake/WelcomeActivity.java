package com.cph.eattingsnake;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.Window;
import android.widget.ImageButton;


public class WelcomeActivity extends Activity {

    ImageButton startB, noteB, exitB;
    Intent gameIntent, noteIntent;

    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if ((getIntent().getFlags() & Intent.FLAG_ACTIVITY_BROUGHT_TO_FRONT) != 0)
        {
            finish();
            return;
        }
        requestWindowFeature(Window.FEATURE_NO_TITLE);//去掉标题栏
        setContentView(R.layout.welcome);    //设置layout

        startB = findViewById(R.id.startbutton);//通过id获取button
        noteB = findViewById(R.id.notesbutton);
        exitB = findViewById(R.id.exitbutton);
        addListenerForButton();
        gameIntent = new Intent(WelcomeActivity.this, GameActivity.class);
        noteIntent = new Intent(WelcomeActivity.this, NoteActivity.class);
    }


    public void addListenerForButton() {
        startB.setOnClickListener(new OnClickListener() {
            public void onClick(View v) {
                startActivity(gameIntent);
            }
        });
        noteB.setOnClickListener(new OnClickListener() {
            public void onClick(View v) {
                startActivity(noteIntent);
            }
        });
        exitB.setOnClickListener(new OnClickListener() {
            public void onClick(View v) {
                finish();
            }
        });
    }
}
