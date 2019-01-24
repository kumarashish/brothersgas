package com.brothersgas;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.view.WindowManager;

import common.AppController;

/**
 * Created by ashish.kumar on 21-01-2019.
 */

public class Splash extends Activity {

    private static int SPLASH_TIME_OUT = 3000;
    final int permissionReadExternalStorage=1;
    AppController controller;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN,
                WindowManager.LayoutParams.FLAG_FULLSCREEN);
        setContentView(R.layout.activity_splash);
        controller=(AppController)getApplicationContext();
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
              if(controller.getManager().isUserLoggedIn())
              {Intent i = new Intent(Splash.this,DashBoard.class);
                  startActivity(i);


              }else {


                  Intent i = new Intent(Splash.this, Login.class);
                  startActivity(i);

              }
                finish();
    }


    }