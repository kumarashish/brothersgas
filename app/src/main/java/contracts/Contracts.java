package contracts;

import android.app.Activity;
import android.os.AsyncTask;
import android.os.Bundle;
import android.util.Log;
import android.widget.ListView;
import android.widget.Toast;

import com.brothersgas.Login;
import com.brothersgas.R;

import org.json.JSONArray;
import org.json.JSONObject;

import java.util.ArrayList;

import butterknife.BindView;
import butterknife.ButterKnife;
import common.AppController;
import common.Common;
import common.WebServiceAcess;
import model.ContractModel;
import utils.Utils;

/**
 * Created by ashish.kumar on 24-01-2019.
 */

public class Contracts extends Activity {
    AppController controller;
    WebServiceAcess webServiceAcess;
    ArrayList<ContractModel> list=new ArrayList<>();
    @BindView(R.id.listView)
    ListView listView;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_contracts);
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
            if(s.length()>0)
            {
              try{
                  JSONObject jsonObject=new JSONObject(s);
                  JSONObject result=jsonObject.getJSONObject("RESULT");
                  JSONArray jsonArray=result.getJSONArray("LIN");
                  for(int i=0;i<jsonArray.length();i++)
                  {
                      JSONObject item=jsonArray.getJSONObject(i);
                      list.add(new ContractModel(item.getJSONArray("FLD")));



                  }

                  if(list.size()>0)
                  {
                      //listView.setAdapter(new S);
                  }
              }catch (Exception ex)
              {
                  ex.fillInStackTrace();
              }
            }else{
               // Toast.makeText(Contracts.this,"Invalid credentials! Please enter valid Username and Password",Toast.LENGTH_SHORT).show();
            }


        }
    }

}
