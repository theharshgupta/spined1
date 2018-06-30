package com.assistx.hg.sp;

import android.content.Intent;
import android.graphics.Typeface;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.view.Window;
import android.view.WindowManager;
import android.widget.TextView;

public class SplashScreen extends BaseActivity {
    TextView mTextView;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN,
                WindowManager.LayoutParams.FLAG_FULLSCREEN);

        setContentView(R.layout.splashscreenx);

        Typeface typeface = Typeface.createFromAsset(getAssets(), "AvantGardeMdITCTT.ttf");
        mTextView = findViewById(R.id.mainhead);
//        Calligrapher calligrapher = new Calligrapher(this);
//        calligrapher.setFont(this, "BreakersSSK-Bold.ttf", true);
        mTextView.setTypeface(typeface);
        Thread myThread = new Thread() {
            @Override
            public void run() {
                try {
                    sleep(2000);
                    Intent intent = new Intent(getApplicationContext(), MainActivity.class);
                    startActivity(intent);
                    finish();
                } catch (InterruptedException e) {
                    e.printStackTrace();

                }
            }
        };
        myThread.start();
    }
}
