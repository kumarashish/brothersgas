package invoices;

import android.app.Activity;
import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.ListView;
import android.widget.ProgressBar;
import android.widget.RelativeLayout;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

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
import contracts.Search;
import interfaces.ListItemClickListner;
import model.ContractModel;
import utils.Utils;

public class Block_Cancel  extends Activity implements View.OnClickListener , ListItemClickListner {
    AppController controller;
    WebServiceAcess webServiceAcess;


    ArrayList<ContractModel> unblockedlist=new ArrayList<>();

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
    @BindView(R.id.heading)
    TextView heading;
    @BindView(R.id.header)
    RelativeLayout header;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_contracts);
        controller = (AppController) getApplicationContext();
        webServiceAcess=new WebServiceAcess();
        ButterKnife.bind(this);
        search.setOnClickListener(this);
        header.setVisibility(View.GONE);
        heading.setText("Block & Cancel");
        back.setOnClickListener(this);
        if(Utils.isNetworkAvailable(Block_Cancel.this)) {
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
                Search.contractList = unblockedlist;
                Intent in=new Intent(Block_Cancel.this,Search.class);
                in.putExtra("requestedScreen",3);
                startActivity(in);
                break;

        }
    }

    @Override
    public void onClick(final ContractModel model) {
        runOnUiThread(new Runnable() {
            @Override
            public void run() {
                Intent in=new Intent(Block_Cancel.this,Block_Cancel_Details.class);
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
            String result = webServiceAcess.queryRequest(Common.queryAction, Common.ContractList);
            return result;
        }

        @Override
        protected void onPostExecute(String s) {
            Log.e("value", "onPostExecute: ", null);
            if (s.length() > 0) {
                try {
                    JSONObject jsonObject = new JSONObject(s);
                    JSONObject result = jsonObject.getJSONObject("RESULT");
                    JSONArray jsonArray = result.getJSONArray("LIN");
                    for (int i = 0; i < jsonArray.length(); i++) {
                        JSONObject item = jsonArray.getJSONObject(i);
                        ContractModel model = new ContractModel(item.getJSONArray("FLD"));

                        if ((model.getBlock_unblockflag() != 2)) {
                            if((model.getDepositInvoice().length()!=0)&&(model.getConnection_discconectionInvoice().length()!=0))
                            {
                                unblockedlist.add(model);
                            }

                        }
                    }
                    if (unblockedlist.size() > 0) {
                        listView.setAdapter(new ContractListAdapter(unblockedlist, Block_Cancel.this));
                        progressBar.setVisibility(View.GONE);
                        contentView.setVisibility(View.VISIBLE);
                    }else{
                        progressBar.setVisibility(View.GONE);
                        Utils.showAlert(Block_Cancel.this,"No data Found");

                    }
                } catch (Exception ex) {
                    ex.fillInStackTrace();
                }
            } else {
                progressBar.setVisibility(View.GONE);

                Toast.makeText(Block_Cancel.this, "Data not found", Toast.LENGTH_SHORT).show();
            }


        }
    }}