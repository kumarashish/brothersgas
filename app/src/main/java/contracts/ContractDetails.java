package contracts;

import android.app.Activity;
import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.os.PersistableBundle;
import android.support.annotation.Nullable;
import android.util.Log;
import android.view.View;
import android.widget.Toast;

import com.brothersgas.DashBoard;
import com.brothersgas.Login;
import com.brothersgas.R;

import common.AppController;
import common.Common;
import common.WebServiceAcess;
import utils.Utils;


/**
 * Created by ashish.kumar on 25-01-2019.
 */

public class ContractDetails extends Activity {
    WebServiceAcess webServiceAcess;
    AppController controller;
    String contractId;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_contracts);
        contractId=getIntent().getStringExtra("Data");
        controller=(AppController)getApplicationContext();
        webServiceAcess=new WebServiceAcess();
        if(Utils.isNetworkAvailable(ContractDetails.this))
        {
            new GetData().execute();
        }
    }

    /*-------------------------------------------------------------------getData-------------------------------------------------------*/
    public class GetData extends AsyncTask<String,Void,String> {
        @Override
        protected String doInBackground(String... strings) {
            String result=webServiceAcess.runRequest(Common.runAction,Common.ContractView,  new String[]{contractId});
            return  result;
        }

        @Override
        protected void onPostExecute(String s) {
            Log.e("value", "onPostExecute: ", null);

        }
    }
}
