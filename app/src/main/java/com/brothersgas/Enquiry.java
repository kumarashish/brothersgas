package com.brothersgas;

import android.app.Activity;
import android.app.AlertDialog;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.ProgressBar;

import butterknife.BindView;
import butterknife.ButterKnife;
import common.AppController;
import common.WebServiceAcess;

/**
 * Created by ashish.kumar on 28-02-2019.
 */

public class Enquiry extends Activity implements View.OnClickListener {

    WebServiceAcess webServiceAcess;


    @BindView(R.id.done)
    Button ok;

    AppController controller;

    @BindView(R.id.back_button)
    Button back_button;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.enquiry);
        //getSupportActionBar().setTitle("");
        // Add titleTextView into ActionBar
        ButterKnife.bind(this);

        webServiceAcess = new WebServiceAcess();
        controller = (AppController) getApplicationContext();
        back_button.setOnClickListener(this);
        ok.setOnClickListener(this);
    }

    @Override
    public void onClick(View v) {
        switch (v.getId())
        {
            case R.id.done:
            case R.id.back_button:
                finish();
                break;
        }

    }
}

