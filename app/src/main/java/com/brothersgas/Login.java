package com.brothersgas;

import android.app.ActionBar;
import android.app.Activity;
import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.AsyncTask;
import android.provider.MediaStore;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.text.InputFilter;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.TextView;
import android.widget.Toast;

import java.io.UTFDataFormatException;

import javax.microedition.khronos.egl.EGLDisplay;

import butterknife.BindView;
import butterknife.ButterKnife;
import common.WebServiceAcess;
import utils.Utils;

public class Login extends Activity implements View.OnClickListener {
    AlertDialog dialog;
    @BindView(R.id.ivConfiguration)
    ImageView config;
    WebServiceAcess  webServiceAcess;
    @BindView(R.id.etUserName)
    EditText edt_username;
    @BindView(R.id.etPassword)
    EditText edt_password;

    @BindView(R.id.btnLogin)
    Button btn_login;
    @BindView(R.id.progressBar)
    ProgressBar progressBar;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN,
                WindowManager.LayoutParams.FLAG_FULLSCREEN);
        setContentView(R.layout.login);
        //getSupportActionBar().setTitle("");
        // Add titleTextView into ActionBar
        ButterKnife.bind(this);
        config.setOnClickListener(this);
        webServiceAcess=new WebServiceAcess();
        btn_login.setOnClickListener(this);
    }

    public void configPopUp() {
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        LayoutInflater inflater = getLayoutInflater();
        View dialogLayout = inflater.inflate(R.layout.activity_config, null);
        builder.setView(dialogLayout);
        dialog = builder.create();
        dialog.show();
    }

    @Override
    public void onClick(View v) {
        switch (v.getId())
        {
            case R.id.ivConfiguration:
                configPopUp();
                break;
            case R.id.btnLogin:
                btn_login.setVisibility(View.GONE);
                progressBar.setVisibility(View.VISIBLE);
                new GetData().execute();
                break;

        }
    }
    public class GetData extends AsyncTask<String,Void,String>{
        @Override
        protected String doInBackground(String... strings) {
          String result=webServiceAcess.runRequest();
            return  result;
        }

        @Override
        protected void onPostExecute(String s) {
            Log.e("value", "onPostExecute: ", null);
            if(Utils.isUserLoggedIn(s))
            {
                startActivity(new Intent(Login.this,DashBoard.class));
                finish();
                Toast.makeText(Login.this,"Logged in sucessfully.",Toast.LENGTH_SHORT).show();
            }else{
                Toast.makeText(Login.this,"Invalid credentials!Please enter valid userId and Password",Toast.LENGTH_SHORT).show();
            }

            btn_login.setVisibility(View.VISIBLE);
            progressBar.setVisibility(View.GONE);
        }
    }

}
