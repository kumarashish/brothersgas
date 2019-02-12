package consumption;

import android.app.Activity;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v7.app.AlertDialog;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.ProgressBar;
import android.widget.Toast;

import com.brothersgas.R;

import org.json.JSONArray;
import org.json.JSONObject;

import butterknife.BindView;
import butterknife.ButterKnife;
import common.AppController;
import common.Common;
import common.WebServiceAcess;
import invoices.Block_Cancel_Details;
import utils.Utils;

/**
 * Created by ashish.kumar on 07-02-2019.
 */

public class ConsumptionDetails  extends Activity implements View.OnClickListener {
    WebServiceAcess webServiceAcess;
    AppController controller;
    String contractId;
    model.ContractDetails model = null;
    @BindView(R.id.progressBar)
    ProgressBar progressBar;
    @BindView(R.id.progressBar2)
    ProgressBar progressBar2;
    @BindView(R.id.mainLayout)
    LinearLayout mainLayout;
    @BindView(R.id.site)
    android.widget.TextView site;
    @BindView(R.id.customer_id)
    android.widget.TextView customer_id;
    @BindView(R.id.contract_date)
    android.widget.TextView contract_date;
    @BindView(R.id.address)
    android.widget.TextView address;
    @BindView(R.id.item)
    android.widget.TextView item;
    @BindView(R.id.depositamount)
    android.widget.TextView depositamount;
    @BindView(R.id.Connection_charges)
    android.widget.TextView Connection_charges;
    @BindView(R.id.Disconnection_Charges)
    android.widget.TextView Disconnection_Charges;
    @BindView(R.id.Pressure_Factor)
    android.widget.TextView Pressure_Factor;
    @BindView(R.id.Initial_meter_reading)
    android.widget.TextView Initial_meter_reading;
    @BindView(R.id.Deposit_Invoice)
    android.widget.TextView Deposit_Invoice;
    @BindView(R.id.Connection_Disconnection_Invoice)
    android.widget.TextView Connection_Disconnection_Invoice;
    @BindView(R.id.back_button)
    Button back;
    @BindView(R.id.footer)
    LinearLayout footer;
    @BindView(R.id.dep_invoice)
    Button block;
    @BindView(R.id.con_dcon_invoice)
    Button cancel;


    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_contract_details);
        webServiceAcess = new WebServiceAcess();
        ButterKnife.bind(this);
        contractId = getIntent().getStringExtra("Data");
        block.setText("");
        block.setVisibility(View.INVISIBLE);
        cancel.setText("Generate");
        controller = (AppController) getApplicationContext();
        footer.setVisibility(View.VISIBLE);
        block.setOnClickListener(this);
        cancel.setOnClickListener(this);

        back.setOnClickListener(this);
        if (Utils.isNetworkAvailable(ConsumptionDetails.this)) {
            progressBar.setVisibility(View.VISIBLE);
            mainLayout.setVisibility(View.GONE);
            new GetData().execute();
        }
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.back_button:
                finish();
                break;

            case R.id.con_dcon_invoice:
                Intent in = new Intent(ConsumptionDetails.this, Consumption.class);
                Consumption.model = model;
                startActivity(in);
                finish();
                break;
        }
    }

    /*-------------------------------------------------------------------getData-------------------------------------------------------*/
    public class GetData extends AsyncTask<String, Void, String> {
        @Override
        protected String doInBackground(String... strings) {
            String result = webServiceAcess.runRequest(Common.runAction, Common.ContractView, new String[]{contractId});
            return result;
        }

        @Override
        protected void onPostExecute(String s) {
            Log.e("value", "onPostExecute: ", null);
            if (s.length() > 0) {
                try {
                    JSONObject jsonObject = new JSONObject(s);
                    JSONObject result = jsonObject.getJSONObject("RESULT");
                    JSONArray jsonArray = result.getJSONArray("GRP");
                    JSONObject item = jsonArray.getJSONObject(1);
                    model = new model.ContractDetails(item.getJSONArray("FLD"));

                    if (model != null) {
                        model.setContractNumber(contractId);
                        setValue();
                        progressBar.setVisibility(View.GONE);
                        mainLayout.setVisibility(View.VISIBLE);
                    }
                } catch (Exception ex) {
                    ex.fillInStackTrace();
                }
            } else {
                progressBar.setVisibility(View.GONE);
                mainLayout.setVisibility(View.VISIBLE);
                Toast.makeText(ConsumptionDetails.this, "Data not found", Toast.LENGTH_SHORT).show();
            }
        }
    }

    public void setValue() {
        site.setText(model.getSite_value());
        customer_id.setText(model.getCustomer_value());
        contract_date.setText(model.getContract_Date());
        address.setText(model.getCustomer_Address());
        item.setText(model.getProduct_Item());
        depositamount.setText(model.getDeposit_Amount() +" "+model.getCurrency());
        Connection_charges.setText(model.getConnection_charges()+" "+model.getCurrency());
        Disconnection_Charges.setText(model.getDisconnection_Charges()+" "+model.getCurrency());
        Pressure_Factor.setText(model.getPressure_Factor());
        Initial_meter_reading.setText(model.getInitial_meter_reading());
        Deposit_Invoice.setText(model.getDeposit_Invoice());
        Connection_Disconnection_Invoice.setText(model.getConnection_Disconnection_Invoice());

    }
}