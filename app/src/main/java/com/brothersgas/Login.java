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

import com.backendless.Backendless;
import com.backendless.IDataStore;
import com.backendless.async.callback.AsyncCallback;
import com.backendless.exceptions.BackendlessFault;
import com.backendless.persistence.DataQueryBuilder;

import java.io.UTFDataFormatException;
import java.util.List;
import java.util.Map;

import javax.microedition.khronos.egl.EGLDisplay;

import butterknife.BindView;
import butterknife.ButterKnife;
import common.AppController;
import common.Common;
import common.WebServiceAcess;
import model.UserRole;
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
        ButterKnife.bind(this);
        config.setOnClickListener(this);
        webServiceAcess=new WebServiceAcess();
        controller=(AppController)getApplicationContext();
        btn_login.setOnClickListener(this);

    }



    @Override
    public void onClick(View v) {
        switch (v.getId())
        {

            case R.id.btnLogin:
                if((edt_username.getText().length()>0)&&(edt_password.getText().length()>0)) {
                    if(Utils.isNetworkAvailable(Login.this)) {
                            btn_login.setVisibility(View.GONE);
                            progressBar.setVisibility(View.VISIBLE);
                            getSettings();

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

    public void getSettings() {
        Thread T = new Thread(new Runnable() {
            @Override
            public void run() {
                 IDataStore<Map> contactStorage = Backendless.Data.of(Common.tableName);
                DataQueryBuilder queryBuilder = DataQueryBuilder.create();
                contactStorage.find(queryBuilder, new AsyncCallback<List<Map>>() {
                    @Override
                    public void handleResponse(List<Map> categoryList) {
                        if(categoryList.size()>0) {

                            for (int i = 0; i < categoryList.size(); i++) {
                                Map category = categoryList.get(i);
                                controller.getManager().setConfiguration(category.get("IPAddress").toString().trim(), category.get("Port").toString().trim(), category.get("Pool").toString().trim(), category.get("Username").toString().trim(), category.get("Password").toString().trim());
                                Configuration.setConfiguration(category.get("IPAddress").toString().trim(), category.get("Port").toString().trim(), category.get("Pool").toString().trim(), category.get("Username").toString().trim(), category.get("Password").toString().trim());
                            }

                            runOnUiThread(new Runnable() {
                                @Override
                                public void run() {
                                    new GetData().execute();
                                }
                            });
                            Log.i("MYAPP", "Retrieved " + categoryList.size() + " objects");
                        }else{
                            runOnUiThread(new Runnable() {
                                @Override
                                public void run() {
                                   Utils.showAlert(Login.this,"Settings Not Availbe on Server ,Please Update Settings.");
                                }
                            });
                        }
                    }

                    @Override
                    public void handleFault(BackendlessFault fault) {

                        Log.e("MYAPP", "Server reported an error " + fault);
                    }
                });
            }
        });
        T.start();
    }

    /*-------------------------------------------------------------------getData-------------------------------------------------------*/
    public class GetData extends AsyncTask<String,Void,String>{
        @Override
        protected String doInBackground(String... strings) {
          String result=webServiceAcess.runRequest(Common.runAction,Common.LoginMethod,new String[]{edt_username.getText().toString(),edt_password.getText().toString()});
            return  result;
        }

        @Override
        protected void onPostExecute(String s) {
            Log.e("value", "onPostExecute: ", null);
            UserRole model=new UserRole(s);
            if(model.getStatus()==2)
            {   controller.getManager().setUserLoggedIn(true);
                controller.setRole(s);
                controller.getManager().setLoggedInUserDetails(edt_username.getText().toString(),edt_password.getText().toString());
                startActivity(new Intent(Login.this,DashBoard.class));
                finish();
                Toast.makeText(Login.this,model.getMessage(),Toast.LENGTH_SHORT).show();
            }else{
                Toast.makeText(Login.this,model.getMessage(),Toast.LENGTH_SHORT).show();
            }

            btn_login.setVisibility(View.VISIBLE);
            progressBar.setVisibility(View.GONE);
        }
    }

}
