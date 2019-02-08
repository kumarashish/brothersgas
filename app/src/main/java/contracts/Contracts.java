package contracts;

import android.app.Activity;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Color;
import android.os.AsyncTask;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.ProgressBar;
import android.widget.RelativeLayout;
import android.widget.Spinner;
import android.widget.TextView;
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
import invoices.Connection_Disconnection_Invoice;
import model.ContractModel;
import utils.Utils;

/**
 * Created by ashish.kumar on 24-01-2019.
 */

public class Contracts extends Activity implements View.OnClickListener , ListItemClickListner{
    AppController controller;
    WebServiceAcess webServiceAcess;
    ArrayList<ContractModel> list=new ArrayList<>();
    ArrayList<ContractModel> blockedlist=new ArrayList<>();
    ArrayList<ContractModel> unblockedlist=new ArrayList<>();
    ArrayList<ContractModel> cancelledlist=new ArrayList<>();
    @BindView(R.id.listView)
    ListView listView;
    @BindView(R.id.progressBar)
    ProgressBar progressBar;
    @BindView(R.id.contentView)
    RelativeLayout contentView;

    @BindView(R.id.contract_type)
    Spinner contract_type;
    @BindView(R.id.back_button)
    Button back;
    @BindView(R.id.search)
    Button search;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_contracts);
        controller = (AppController) getApplicationContext();
        webServiceAcess=new WebServiceAcess();
        ButterKnife.bind(this);
        search.setOnClickListener(this);

        back.setOnClickListener(this);
        if(Utils.isNetworkAvailable(Contracts.this)) {
            progressBar.setVisibility(View.VISIBLE);
            contentView.setVisibility(View.GONE);
            new GetData().execute();
        }

        contract_type.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {

                switch (position)
                {
                    case 0:
                        if(list.size()>0) {
                            listView.setAdapter(new ContractListAdapter(unblockedlist, Contracts.this));
                        }
                        break;
                    case 1:
                    if(list.size()>0) {
                        listView.setAdapter(new ContractListAdapter(blockedlist, Contracts.this));
                    }
                        break;
                    case 2:
                        if(list.size()>0) {
                            listView.setAdapter(new ContractListAdapter(cancelledlist, Contracts.this));
                        }
                        break;
                    case 3:
                        if(list.size()>0) {
                            listView.setAdapter(new ContractListAdapter(list, Contracts.this));
                        }
                        break;

                }
                ((TextView) view).setTextColor(Color.WHITE); //Change selected text color
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
    case R.id.search:

        switch (contract_type.getSelectedItemPosition()) {
            case 0:
                Search.contractList = unblockedlist;

                break;
            case 1:
                Search.contractList = blockedlist;
                break;
            case 2:
                Search.contractList = cancelledlist;
                break;
            case 3:
                Search.contractList = list;
                break;

        }
        Intent in=new Intent(Contracts.this,Search.class);
        in.putExtra("requestedScreen",1);
        startActivity(new Intent(in));
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
                      ContractModel model=new ContractModel(item.getJSONArray("FLD"));
                      Log.d("contractId",model.getContract_Meternumber());
                      list.add(model);
                      Log.d("contractId adding",list.get(i).getContract_Meternumber());
                      if(i!=0) {
                          Log.d("contractIdafter adding", list.get(i-1).getContract_Meternumber());
                      }
                      if(model.getBlock_unblockflag()==2)
                      {
                          blockedlist.add(model);
                      }else if(model.getClosemeterreadingvalue()==2)
                      {
                          cancelledlist.add(model);
                      }else {
                          unblockedlist.add(model);
                      }



                  }

                  if(list.size()>0)
                  {for(int i=0;i<list.size();i++)
                  {
                      Log.d("contractId from list",list.get(i).getContract_Meternumber());
                  }
                      listView.setAdapter(new ContractListAdapter(list,Contracts.this));
                      progressBar.setVisibility(View.GONE);
                      contentView.setVisibility(View.VISIBLE);
                  }
              }catch (Exception ex)
              {
                  ex.fillInStackTrace();
              }
            }else{
                progressBar.setVisibility(View.GONE);

               Toast.makeText(Contracts.this,"Data not found",Toast.LENGTH_SHORT).show();
            }


        }
    }

}
