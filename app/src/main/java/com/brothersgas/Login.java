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
import common.AppController;
import common.Common;
import common.WebServiceAcess;
import utils.Configuration;
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
    AppController controller;
    ProgressBar dialogProgressBar;
    Button   saveBtn;
    Button   resetBtn;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.login);
        //getSupportActionBar().setTitle("");
        // Add titleTextView into ActionBar
        ButterKnife.bind(this);
        config.setOnClickListener(this);
        webServiceAcess=new WebServiceAcess();
        controller=(AppController)getApplicationContext();
        btn_login.setOnClickListener(this);
    }

    public void configPopUp() {

        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        LayoutInflater inflater = getLayoutInflater();
        View dialogLayout = inflater.inflate(R.layout.activity_config, null);
        dialogProgressBar=(ProgressBar)dialogLayout.findViewById(R.id.progressBar);
       final EditText conf_ip=(EditText)dialogLayout.findViewById(R.id.conf_ip);
        final  EditText conf_folder=(EditText)dialogLayout.findViewById(R.id.conf_folder);
        final  EditText conf_userid=(EditText)dialogLayout.findViewById(R.id.conf_userid);
        final  EditText conf_pass=(EditText)dialogLayout.findViewById(R.id.conf_pass);
         saveBtn=(Button) dialogLayout.findViewById(R.id.saveBtn);
        resetBtn=(Button) dialogLayout.findViewById(R.id.resetBtn);
        resetBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                conf_ip.setText("");
                conf_folder.setText("");
                conf_userid.setText("");
                conf_pass.setText("");
            }
        });
        saveBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if((conf_ip.getText().length()>0)&&(conf_folder.getText().length()>0)&&(conf_userid.getText().length()>0)&&(conf_pass.getText().length()>0))
                {
                    if(Utils.isNetworkAvailable(Login.this)) {
                        saveBtn.setVisibility(View.GONE);
                        resetBtn.setVisibility(View.GONE);
                        dialogProgressBar.setVisibility(View.VISIBLE);
                        new SaveData().execute(new String[]{conf_ip.getText().toString(),conf_folder.getText().toString(),conf_userid.getText().toString(),conf_pass.getText().toString()});


                    }else {
                        Toast.makeText(Login.this,"Internet Unavailable! Please connect to internet.",Toast.LENGTH_SHORT).show();
                    }

                }else{
                    if(conf_ip.getText().length()==0)
                    {
                        conf_ip.setError("Please enter Port");
                    }else if(conf_folder.getText().length()==0)
                    {
                        conf_folder.setError("Please enter alias name");
                    }
                    else if(conf_userid.getText().length()==0)
                    {
                        conf_ip.setError("Please enter user id");
                    }else if(conf_pass.getText().length()==0)
                    {
                        conf_folder.setError("Please enter password");
                    }
                }

            }
        });
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
                if((edt_username.getText().length()>0)&&(edt_password.getText().length()>0)) {
                    if(Utils.isNetworkAvailable(Login.this)) {
                        if(Configuration.Port.length()!=0) {
                            btn_login.setVisibility(View.GONE);
                            progressBar.setVisibility(View.VISIBLE);
                            new GetData().execute();
                        }else {
                            Toast.makeText(Login.this,"Please update configurations.",Toast.LENGTH_SHORT).show();
                        }
                    }else{
                        Toast.makeText(Login.this,"Internet Unavailable! Please connect to internet.",Toast.LENGTH_SHORT).show();
                    }
                }else{
                    if(edt_username.getText().length()==0) {
                        edt_username.setError("Please enter user name");
                    }else{
                        edt_password.setError("Please enter password");
                    }
                }
                break;

        }
    }
    /*-------------------------------------------------------------------saveCo-------------------------------------------------------*/
    public class SaveData extends AsyncTask<String,Void,String>{
        String port;
        String alias;
        String name;
        String password;
        @Override
        protected String doInBackground(String... strings) {
            port=strings[0];
            alias=strings[1];
            name=strings[2];
            password=strings[3];
            String result=webServiceAcess.saveSettingsRequest(Common.runAction,Common.ConfigMethod,port,alias,name,password);
            return  result;
        }

        @Override
        protected void onPostExecute(String s) {
            Log.e("value", "onPostExecute: ", null);
            if(s.equalsIgnoreCase("Success"))
            {     controller.getManager().setConfiguration(port,alias,name,password);
                  Configuration.setConfiguration(port,alias,name,password);
                Toast.makeText(Login.this,"Settings Saved sucessfully.",Toast.LENGTH_SHORT).show();
                dialog.cancel();
            }else{
                saveBtn.setVisibility(View.VISIBLE);
                resetBtn.setVisibility(View.VISIBLE);
                dialogProgressBar.setVisibility(View.GONE);
                Toast.makeText(Login.this,"Invalid Settings,Please enter correct configurations",Toast.LENGTH_SHORT).show();
            }


        }
    }
    /*-------------------------------------------------------------------getData-------------------------------------------------------*/
    public class GetData extends AsyncTask<String,Void,String>{
        @Override
        protected String doInBackground(String... strings) {
          String result=webServiceAcess.runRequest(Common.runAction,Common.LoginMethod,edt_username.getText().toString(),edt_password.getText().toString());
            return  result;
        }

        @Override
        protected void onPostExecute(String s) {
            Log.e("value", "onPostExecute: ", null);
            if(Utils.isUserLoggedIn(s))
            {   controller.getManager().setUserLoggedIn(true);
                controller.getManager().setLoggedInUserDetails(edt_username.getText().toString(),edt_password.getText().toString());
                startActivity(new Intent(Login.this,DashBoard.class));
                finish();
                Toast.makeText(Login.this,"Logged in sucessfully.",Toast.LENGTH_SHORT).show();
            }else{
                Toast.makeText(Login.this,"Invalid credentials! Please enter valid Username and Password",Toast.LENGTH_SHORT).show();
            }

            btn_login.setVisibility(View.VISIBLE);
            progressBar.setVisibility(View.GONE);
        }
    }

}
