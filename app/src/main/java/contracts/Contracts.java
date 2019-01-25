package contracts;

import android.app.Activity;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.ProgressBar;
import android.widget.Spinner;
import android.widget.Toast;

import com.brothersgas.Login;
import com.brothersgas.R;

import org.json.JSONArray;
import org.json.JSONObject;

import java.util.ArrayList;

import adapter.ContractListAdapter;
import butterknife.BindView;
import butterknife.ButterKnife;
import common.AppController;
import common.Common;
import common.WebServiceAcess;
import interfaces.ListItemClickListner;
import model.ContractModel;
import utils.Utils;

/**
 * Created by ashish.kumar on 24-01-2019.
 */

public class Contracts extends Activity implements View.OnClickListener , ListItemClickListner{
    AppController controller;
    WebServiceAcess webServiceAcess;
    ArrayList<ContractModel> list=new ArrayList<>();
    @BindView(R.id.listView)
    ListView listView;
    @BindView(R.id.progressBar)
    ProgressBar progressBar;
    @BindView(R.id.contentView)
    LinearLayout contentView;
    @BindView(R.id.invoice_options)
    LinearLayout invoice_options;
    @BindView(R.id.contract_type)
    Spinner contract_type;
    @BindView(R.id.notgenerated)
    Button not_generatedInvoice;
    @BindView(R.id.deposit_invoice)
    Button depositInvoice;
    @BindView(R.id.back_button)
    Button back;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_contracts);
        controller = (AppController) getApplicationContext();
        webServiceAcess=new WebServiceAcess();
        ButterKnife.bind(this);
        not_generatedInvoice.setOnClickListener(this);
        depositInvoice.setOnClickListener(this);
        back.setOnClickListener(this);
        if(Utils.isNetworkAvailable(Contracts.this)) {
            progressBar.setVisibility(View.VISIBLE);
            contentView.setVisibility(View.GONE);
            new GetData().execute();
        }
        contract_type.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                if(position==1)
                {
                    invoice_options.setVisibility(View.VISIBLE);
                }else {
                    invoice_options.setVisibility(View.GONE);
                }
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });
    }

    @Override
    public void onClick(View v) {
switch (v.getId())
{
    case R.id.back_button:
        finish();
        break;
    case R.id.deposit_invoice:
        depositInvoice.setBackgroundColor(getResources().getColor(R.color.colorPrimary));
         depositInvoice.setTextColor((getResources().getColor(R.color.white)));
        not_generatedInvoice.setBackgroundColor((getResources().getColor(R.color.white)));
        not_generatedInvoice.setTextColor((getResources().getColor(R.color.colorPrimary)));
        break;
    case R.id.notgenerated:
        not_generatedInvoice.setBackgroundColor((getResources().getColor(R.color.colorPrimary)));
        not_generatedInvoice.setTextColor((getResources().getColor(R.color.white)));
        depositInvoice.setBackgroundColor((getResources().getColor(R.color.white)));
        depositInvoice.setTextColor((getResources().getColor(R.color.colorPrimary)));
        break;
}
    }

    @Override
    public void onClick(final ContractModel model) {
      runOnUiThread(new Runnable() {
          @Override
          public void run() {
              Intent in=new Intent(Contracts.this,ContractDetails.class);
              in.putExtra("Data",model.getContract_Meternumber());
              startActivity(in);
          }
      });
    }

    @Override
    public void onCancelClick(ContractModel model) {

    }

    @Override
    public void onBlockClick(ContractModel model) {

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
                      listView.setAdapter(new ContractListAdapter(list,Contracts.this));
                      progressBar.setVisibility(View.GONE);
                      contentView.setVisibility(View.VISIBLE);
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
