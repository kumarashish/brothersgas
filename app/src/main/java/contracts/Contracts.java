package contracts;

import android.app.Activity;
import android.os.AsyncTask;
import android.os.Bundle;
import android.util.Log;
import android.widget.Toast;

import com.brothersgas.Login;
import com.brothersgas.R;

import butterknife.ButterKnife;
import common.AppController;
import common.Common;
import common.WebServiceAcess;
import utils.Utils;

/**
 * Created by ashish.kumar on 24-01-2019.
 */

public class Contracts extends Activity {
    AppController controller;
    WebServiceAcess webServiceAcess;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_dashboard);
        controller = (AppController) getApplicationContext();
        webServiceAcess=new WebServiceAcess();
        ButterKnife.bind(this);
        if(Utils.isNetworkAvailable(Contracts.this)) {
            new GetData().execute();
        }
    }

    /*-------------------------------------------------------------------getData-------------------------------------------------------*/
    public class GetData extends AsyncTask<String,Void,String> {
        @Override
        protected String doInBackground(String... strings) {
            String result=webServiceAcess.queryRequest(Common.queryAction,Common.ContractList);
            return  result;
        }

        @Override
        protected void onPostExecute(String s) {
            Log.e("value", "onPostExecute: ", null);
            if(Utils.isUserLoggedIn(s))
            {
               // Toast.makeText(Contracts.this,"Logged in sucessfully.",Toast.LENGTH_SHORT).show();
            }else{
               // Toast.makeText(Contracts.this,"Invalid credentials! Please enter valid Username and Password",Toast.LENGTH_SHORT).show();
            }


        }
    }

}
