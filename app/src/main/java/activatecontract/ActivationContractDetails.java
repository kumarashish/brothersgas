package activatecontract;

import android.app.Activity;
import android.app.Dialog;
import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.view.Window;
import android.widget.Button;
import android.widget.EditText;
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
import invoices.Block_Cancel_Details;
import model.BlockUnblockModel;
import model.ContractModel;
import utils.Utils;

/**
 * Created by ashish.kumar on 28-02-2019.
 */

public class ActivationContractDetails  extends Activity implements View.OnClickListener {
    WebServiceAcess webServiceAcess;
    AppController controller;
    String contractId;
    model.ContractDetails model = null;
    @BindView(R.id.progressBar)
    ProgressBar progressBar;
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
    EditText Initial_meter_reading;
    @BindView(R.id.Deposit_Invoice)
    android.widget.TextView Deposit_Invoice;
    @BindView(R.id.Connection_Disconnection_Invoice)
    android.widget.TextView Connection_Disconnection_Invoice;
    @BindView(R.id.back_button)
    Button back;
    @BindView(R.id.footer)
    LinearLayout footer;
    @BindView(R.id.edit)
    Button edit_button;


    @BindView(R.id.progressBar2)
    ProgressBar progressBar2;
    @BindView(R.id.heading)
    android.widget.TextView heading;
    @BindView(R.id.dep_invoice)
    Button activateTenant;
    @BindView(R.id.con_dcon_invoice)
    Button changeTenant;
    public static ContractModel contractModel=null;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_contract_details);
        controller = (AppController) getApplicationContext();
        webServiceAcess = new WebServiceAcess();
        ButterKnife.bind(this);
        contractId = getIntent().getStringExtra("Data");
        heading.setText(contractId + "(Details)");
        back.setOnClickListener(this);
        edit_button.setOnClickListener(this);
        edit_button.setVisibility(View.GONE);

        activateTenant.setText("Activate");
        changeTenant.setText("Tenant Change");
        footer.setVisibility(View.VISIBLE);
        activateTenant.setOnClickListener(this);
        changeTenant.setOnClickListener(this);
        edit_button.setVisibility(View.GONE);

        if (Utils.isNetworkAvailable(ActivationContractDetails.this)) {
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
            case R.id.edit:
                Initial_meter_reading.setFocusable(true);
                Initial_meter_reading.setEnabled(true);
                Initial_meter_reading.requestFocus();

                footer.setVisibility(View.VISIBLE);
                if (Initial_meter_reading.getText().length() > 0) {
                    Initial_meter_reading.setSelection(Initial_meter_reading.getText().length());
                }
                break;
            case R.id.submit:
                if (Initial_meter_reading.getText().length() > 0) {
                    footer.setVisibility(View.GONE);
                    progressBar2.setVisibility(View.VISIBLE);
                    new Update().execute(new String[]{Common.UpdateInitialReading});

                }
            case R.id.dep_invoice:
                showReasonAlert();
                break;
            case R.id.con_dcon_invoice:
                TenantChange.contractNumber=contractModel.getContract_Meternumber();
                startActivityForResult(new Intent(ActivationContractDetails.this,TenantChange.class),2);
                break;

        }
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
    }

    /*-------------------------------------------------------------------block-------------------------------------------------------*/
    public class Update extends AsyncTask<String, Void, String> {
        @Override
        protected String doInBackground(String... strings) {

            String result = webServiceAcess.runRequest(Common.runAction, strings[0], new String[]{contractId, Initial_meter_reading.getText().toString()});
            return result;
        }

        @Override
        protected void onPostExecute(String s) {
            Log.e("value", "onPostExecute: " + s, null);
            if (s.length() > 0) {
                try {
                    JSONObject jsonObject = new JSONObject(s);
                    JSONObject result = jsonObject.getJSONObject("RESULT");
                    JSONArray jsonArray = result.getJSONArray("GRP");
                    JSONObject item = jsonArray.getJSONObject(1);
                    JSONArray Fld = item.getJSONArray("FLD");
                    JSONObject statusObject = Fld.getJSONObject(0);
                    JSONObject messageObject = Fld.getJSONObject(1);
                    int status = statusObject.getInt("content");
                    String message = messageObject.isNull("content") ? "Message not available" : messageObject.getString("content");
                    if (status == 2) {
                        Utils.showAlert(ActivationContractDetails.this, message);
                    } else {
                        Utils.showAlertNormal(ActivationContractDetails.this, message);
                    }
                } catch (Exception ex) {
                    ex.fillInStackTrace();
                }
            } else {
                Utils.showAlertNormal(ActivationContractDetails.this, "No message received from api");
            }
            progressBar2.setVisibility(View.GONE);
            footer.setVisibility(View.VISIBLE);
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
                Toast.makeText(ActivationContractDetails.this, "Data not found", Toast.LENGTH_SHORT).show();
            }
        }
    }
    /*-------------------------------------------------------------------getData-------------------------------------------------------*/
    public class ActivateContract extends AsyncTask<String, Void, String> {
        @Override
        protected String doInBackground(String... strings) {
            String result = webServiceAcess.runRequest(Common.runAction, Common.BlockUnBlock, new String[]{contractId,"2",strings[1]});
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
                    BlockUnblockModel model=new BlockUnblockModel(item.getJSONArray("FLD"));
                    if (model.getStatus() == 2) {
                        Utils.showAlertForReturnIntent(ActivationContractDetails.this,model.getMessage() );
                    } else {

                        Toast.makeText(ActivationContractDetails.this, "Failed", Toast.LENGTH_SHORT).show();

                    }
                    progressBar2.setVisibility(View.GONE);
                    footer.setVisibility(View.VISIBLE);
                } catch (Exception ex) {
                    ex.fillInStackTrace();
                    progressBar2.setVisibility(View.GONE);
                    footer.setVisibility(View.VISIBLE);
                }
            } else {
                progressBar2.setVisibility(View.GONE);
                footer.setVisibility(View.VISIBLE);
                Toast.makeText(ActivationContractDetails.this, "Data not found", Toast.LENGTH_SHORT).show();
            }
        }
    }

    public void showReasonAlert()
    {
        final Dialog dialog = new Dialog(ActivationContractDetails.this);
        dialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
        dialog.setCancelable(true);
        dialog.setContentView(R.layout.reason_alert);

        final EditText reason = (EditText) dialog.findViewById(R.id.reason);


        Button submit = (Button) dialog.findViewById(R.id.submit);
        submit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(reason.getText().length()>0) {
                    footer.setVisibility(View.GONE);
                    progressBar2.setVisibility(View.VISIBLE);
                    new ActivateContract().execute(new String[]{Common.UpdateInitialReading,reason.getText().toString()});
                    dialog.dismiss();
                }else{
                    Toast.makeText(ActivationContractDetails.this,"Please enter reason",Toast.LENGTH_SHORT).show();
                }
            }
        });

        dialog.show();
    }
    public void setValue() {
        site.setText(model.getSite_value());
        customer_id.setText(model.getCustomer_value());
        contract_date.setText(Utils.getDate(model.getContract_Date()));
        address.setText(model.getCustomer_Address());
        item.setText(model.getProduct_Item());
        depositamount.setText(model.getDeposit_Amount() + " " + model.getCurrency());
        Connection_charges.setText(model.getConnection_charges() + " " + model.getCurrency());
        Disconnection_Charges.setText(model.getDisconnection_Charges() + " " + model.getCurrency());
        Pressure_Factor.setText(model.getPressure_Factor());
        Initial_meter_reading.setText(model.getInitial_meter_reading()+ " " + model.getUnits());
        Deposit_Invoice.setText(model.getDeposit_Invoice() );
        Connection_Disconnection_Invoice.setText(model.getConnection_Disconnection_Invoice());

    }
}