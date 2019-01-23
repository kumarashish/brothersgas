package com.brothersgas;

import android.app.Activity;
import android.app.AlertDialog;
import android.os.Bundle;
import android.view.View;
import android.view.WindowManager;
import android.widget.ImageView;

import butterknife.BindView;
import butterknife.ButterKnife;
import common.WebServiceAcess;

/**
 * Created by ashish.kumar on 23-01-2019.
 */

public class DashBoard extends Activity implements View.OnClickListener {


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN,
                WindowManager.LayoutParams.FLAG_FULLSCREEN);
        setContentView(R.layout.activity_dashboard);
        //getSupportActionBar().setTitle("");
        // Add titleTextView into ActionBar
        ButterKnife.bind(this);
    }

    @Override
    public void onClick(View v) {

    }
}