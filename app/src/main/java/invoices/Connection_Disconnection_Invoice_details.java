package invoices;

import android.app.Activity;
import android.content.DialogInterface;
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
import contracts.ContractDetails;
import utils.Utils;

public class Connection_Disconnection_Invoice_details  extends Activity implements View.OnClickListener {
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
    Button dep_Invoice;
    @BindView(R.id.con_dcon_invoice)
    Button con_dconInvoice;
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_contract_details);
        webServiceAcess = new WebServiceAcess();
        ButterKnife.bind(this);
        contractId = getIntent().getStringExtra("Data");
        controller = (AppController) getApplicationContext();
        footer.setVisibility(View.VISIBLE);
        con_dconInvoice.setOnClickListener(this);
        dep_Invoice.setOnClickListener(this);

        back.setOnClickListener(this);
        if (Utils.isNetworkAvailable(Connection_Disconnection_Invoice_details.this)) {
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
            case R.id.dep_invoice:
                progressBar2.setVisibility(View.VISIBLE);
                footer.setVisibility(View.GONE);
                new Block().execute(new String[]{"1"});
                break;
            case R.id.con_dcon_invoice:
                progressBar2.setVisibility(View.VISIBLE);
                footer.setVisibility(View.GONE);
                new Block().execute(new String[]{"2"});
                break;
        }
    }    /*-------------------------------------------------------------------block-------------------------------------------------------*/
    public class Block extends AsyncTask<String, Void, String> {
        String calledMethod;
        @Override
        protected String doInBackground(String... strings) {
            calledMethod=strings[0];
            String result = webServiceAcess.runRequest(Common.runAction,Common.DepositInvoice, new String[]{contractId,strings[0]});
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
                    JSONObject Fld = item.getJSONObject("FLD");
                    String message =Fld.isNull("content")?"No Message From API": Fld.getString("content");
                    showAlert(message);
                } catch (Exception ex) {
                    ex.fillInStackTrace();
                }
            } else {
            }
            progressBar2.setVisibility(View.GONE);
            footer.setVisibility(View.VISIBLE);
        }
    }
    public void showAlert(String message)
    {
        AlertDialog.Builder builder1 = new AlertDialog.Builder(Connection_Disconnection_Invoice_details.this);
        builder1.setMessage(message);
        builder1.setCancelable(true);
        builder1.setNeutralButton(
                "Ok",
                new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int id) {
                        dialog.cancel();
                        finish();
                    }
                });

        AlertDialog alert11 = builder1.create();
        alert11.show();
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
                Toast.makeText(Connection_Disconnection_Invoice_details.this, "Data not found", Toast.LENGTH_SHORT).show();
            }
        }
    }

    public void setValue() {
        site.setText(model.getSite_value());
        customer_id.setText(model.getCustomer_value());
        contract_date.setText(model.getContract_Date());
        address.setText(model.getCustomer_Address());
        item.setText(model.getProduct_Item());
        depositamount.setText(model.getDeposit_Amount());
        Connection_charges.setText(model.getConnection_charges());
        Disconnection_Charges.setText(model.getDisconnection_Charges());
        Pressure_Factor.setText(model.getPressure_Factor());
        Initial_meter_reading.setText(model.getInitial_meter_reading());
        Deposit_Invoice.setText(model.getDeposit_Invoice());
        Connection_Disconnection_Invoice.setText(model.getConnection_Disconnection_Invoice());
        if(model.getDeposit_Invoice().length()>0)
        {
            dep_Invoice.setVisibility(View.GONE);
        }
        if(model.getConnection_Disconnection_Invoice().length()>0)
        {
            con_dconInvoice.setVisibility(View.GONE);
        }

    }
}