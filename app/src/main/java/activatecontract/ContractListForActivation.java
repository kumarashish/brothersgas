package activatecontract;

import android.app.Activity;
import android.content.Intent;
import android.graphics.Color;
import android.os.AsyncTask;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.ListView;
import android.widget.ProgressBar;
import android.widget.RelativeLayout;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import com.brothersgas.DashBoard;
import com.brothersgas.Print;
import com.brothersgas.R;

import org.json.JSONArray;
import org.json.JSONObject;
import org.w3c.dom.Text;

import java.util.ArrayList;

import adapter.ContractListAdapter;
import butterknife.BindView;
import butterknife.ButterKnife;
import common.AppController;
import common.Common;
import common.WebServiceAcess;
import contracts.ContractDetails;
import contracts.Contracts;
import contracts.Search;
import interfaces.ListItemClickListner;
import invoices.Block_Cancel_Details;
import model.BlockUnblockModel;
import model.ContractModel;
import utils.Utils;

/**
 * Created by ashish.kumar on 28-02-2019.
 */

public class ContractListForActivation extends Activity implements View.OnClickListener , ListItemClickListner {
    AppController controller;
    WebServiceAcess webServiceAcess;

    ArrayList<ContractModel> blockedlist=new ArrayList<>();

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
    @BindView(R.id.header)
    RelativeLayout header;
    @BindView(R.id.heading)
    TextView heading;
    ContractListAdapter adapter;
    String owner,project;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_contracts);
        controller = (AppController) getApplicationContext();
        webServiceAcess=new WebServiceAcess();
        ButterKnife.bind(this);
        owner=getIntent().getStringExtra("owner");
        project=getIntent().getStringExtra("project");
        search.setOnClickListener(this);
        header.setVisibility(View.GONE);
        heading.setText("Blocked Contracts");
        back.setOnClickListener(this);
        if(Utils.isNetworkAvailable(ContractListForActivation.this)) {
            progressBar.setVisibility(View.VISIBLE);
            contentView.setVisibility(View.GONE);
            new GetData().execute();
        }


    }

    @Override
    public void onClick(View v) {
        switch (v.getId())
        {
            case R.id.back_button:
                finish();
                break;
            case R.id.search:
                Search.contractList = blockedlist;
                Intent in=new Intent(ContractListForActivation.this,Search.class);
                in.putExtra("requestedScreen",5);
                startActivity(new Intent(in));
                break;

        }
    }

    @Override
    public void onClick(final ContractModel model) {
        runOnUiThread(new Runnable() {
            @Override
            public void run() {

                Intent in=new Intent(ContractListForActivation.this, ActivationContractDetails.class);
                ActivationContractDetails.contractModel=model;
                in.putExtra("Data",model.getContract_Meternumber());
                startActivityForResult(in,2);
            }
        });
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if((requestCode==2)&&(resultCode==RESULT_OK))
        {
                removeContract(ActivationContractDetails.contractModel.getContract_Meternumber());
                adapter.notifyDataSetChanged();
        }
    }
    public void removeContract(String contractNumber)
    {
        for(int i=0;i<blockedlist.size();i++)
        {
            if(blockedlist.get(i).getContract_Meternumber().equalsIgnoreCase(contractNumber))
            {
                blockedlist.remove(i);
                break;
            }
        }
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
            String result = webServiceAcess.runRequest(Common.runAction, Common.BlockList,new String[]{"1",owner,project});
            return result;
        }

        @Override
        protected void onPostExecute(String s) {
            Log.e("value", "onPostExecute: ", null);
            if (s.length() > 0) {
                try {
                    JSONObject jsonObject = new JSONObject(s);
                    JSONObject result = jsonObject.getJSONObject("RESULT");
                    JSONObject tab=result.getJSONObject("TAB");
                    Object obj=tab.get("LIN");
                    if(obj instanceof JSONArray) {
                        JSONArray jsonArray = tab.getJSONArray("LIN");
                        for (int i = 0; i < jsonArray.length(); i++) {
                            JSONObject item = jsonArray.getJSONObject(i);
                            ContractModel model = new ContractModel(item.getJSONArray("FLD"));
                            Log.d("contractId", model.getContract_Meternumber());

                            blockedlist.add(model);


                        }
                    }else{
                        JSONObject item =tab.getJSONObject("LIN");
                        ContractModel model = new ContractModel(item.getJSONArray("FLD"));
                        Log.d("contractId", model.getContract_Meternumber());

                        blockedlist.add(model);

                    }

                    if (  blockedlist.size() > 0) {
                        adapter=new ContractListAdapter( blockedlist, ContractListForActivation.this);
                        listView.setAdapter(adapter);
                        progressBar.setVisibility(View.GONE);
                        contentView.setVisibility(View.VISIBLE);
                    } else {
                        progressBar.setVisibility(View.GONE);
                        Utils.showAlert(ContractListForActivation.this, "No Blocked Contracts.");

                    }
                } catch (Exception ex) {
                    ex.fillInStackTrace();
                    progressBar.setVisibility(View.GONE);

                    Toast.makeText(ContractListForActivation.this, "Contract not available.", Toast.LENGTH_SHORT).show();

                }
            } else {
                progressBar.setVisibility(View.GONE);

                    Utils.showAlertNormal(ContractListForActivation.this,Common.message);


            }


        }
    }


}
