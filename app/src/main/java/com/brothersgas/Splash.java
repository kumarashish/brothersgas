package com.brothersgas;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.view.WindowManager;

/**
 * Created by ashish.kumar on 21-01-2019.
 */

public class Splash extends Activity {

    private static int SPLASH_TIME_OUT = 3000;
    final int permissionReadExternalStorage=1;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN,
                WindowManager.LayoutParams.FLAG_FULLSCREEN);
        setContentView(R.layout.activity_splash);
        runThread();
    }


    public void runThread()
    {
        new Handler().postDelayed(new Runnable() {

            /*
             * Showing splash screen with a timer. This will be useful when you
             * want to show case your app logo / company
             */

            @Override
            public void run() {
                // This method will be executed once the timer is over
                // Start your app main activity
                launchHomeScreen();
            }
        }, SPLASH_TIME_OUT);
    }

    public void launchHomeScreen() {


                Intent i = new Intent(Splash.this, Login.class);
                startActivity(i);
                finish();
    }


    }