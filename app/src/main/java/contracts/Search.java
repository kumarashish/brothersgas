package contracts;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.AutoCompleteTextView;
import android.widget.Button;
import android.widget.ListView;
import android.widget.ProgressBar;
import android.widget.RelativeLayout;
import android.widget.Spinner;

import com.brothersgas.R;

import java.util.ArrayList;

import activatecontract.ActivationContractDetails;
import adapter.CustomListAdapter;
import butterknife.BindView;
import butterknife.ButterKnife;
import common.AppController;
import common.WebServiceAcess;
import consumption.ConsumptionDetails;
import interfaces.ListItemClickListner;
import invoices.Block_Cancel;
import invoices.Block_Cancel_Details;
import invoices.Connection_Disconnection_Invoice;
import invoices.Connection_Disconnection_Invoice_details;
import model.ContractModel;

public class Search extends Activity implements View.OnClickListener , ListItemClickListner {
    AppController controller;
    WebServiceAcess webServiceAcess;

    @BindView(R.id.progressBar)
    ProgressBar progressBar;
@BindView(R.id.auto)
    AutoCompleteTextView autoCompleteTextView;

    @BindView(R.id.back_button)
    Button back;
    int requestedFor=1;
   public static ArrayList<ContractModel>contractList;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_search);
        requestedFor=getIntent().getIntExtra("requestedScreen",1);
        controller = (AppController) getApplicationContext();
        webServiceAcess = new WebServiceAcess();
        ButterKnife.bind(this);
        CustomListAdapter adapter = new CustomListAdapter(this, R.layout.contract_row, contractList);
        back.setOnClickListener(this);
        autoCompleteTextView.setAdapter(adapter);
        autoCompleteTextView.setOnItemClickListener(onItemClickListener);
    }
    private AdapterView.OnItemClickListener onItemClickListener =
            new AdapterView.OnItemClickListener(){
                @Override
                public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
                  ContractModel model=  (ContractModel) adapterView.getItemAtPosition(i);
                    Intent in=null;
                    switch (requestedFor)
                    {
                        case 1:
                            in=new Intent(Search.this,ContractDetails.class);
                            break;
                        case 2:
                            Connection_Disconnection_Invoice_details.contractModel=model;
                            in=new Intent(Search.this, Connection_Disconnection_Invoice_details.class);
                            break;
                        case 3:
                            Block_Cancel_Details.contractModel=model;
                            in=new Intent(Search.this, Block_Cancel_Details.class);
                            break;
                        case 4:
                            in=new Intent(Search.this, ConsumptionDetails.class);
                            break;
                        case 5:
                            ActivationContractDetails.contractModel=model;
                            in=new Intent(Search.this, ActivationContractDetails.class);
                            break;
                    }


                    in.putExtra("Data",model.getContract_Meternumber());
                    startActivity(in);
                    finish();
                }
            };




    @Override
    public void onClick(View v) {
        switch (v.getId())
        {
            case R.id.back_button:
                finish();
                break;
        }

    }

    @Override
    public void onClick(ContractModel model) {

    }

    @Override
    public void onCancelClick(ContractModel model) {

    }

    @Override
    public void onBlockClick(ContractModel model) {

    }
}