package invoices;

import android.app.Activity;
import android.app.Dialog;
import android.content.DialogInterface;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v7.app.AlertDialog;
import android.util.Log;
import android.view.View;
import android.view.Window;
import android.widget.Button;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.ProgressBar;
import android.widget.Switch;
import android.widget.Toast;

import com.brothersgas.R;

import org.json.JSONArray;
import org.json.JSONObject;

import activatecontract.ActivationContractDetails;
import butterknife.BindView;
import butterknife.ButterKnife;
import common.AppController;
import common.Common;
import common.WebServiceAcess;
import utils.Utils;

public class Block_Cancel_Details  extends Activity implements View.OnClickListener {
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
    EditText Initial_meter_reading;
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
    @BindView(R.id.heading)
    android.widget.TextView heading;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_contract_details);
        webServiceAcess = new WebServiceAcess();
        ButterKnife.bind(this);
        contractId = getIntent().getStringExtra("Data");
        heading.setText(contractId +"(Details)");
        block.setText("Block");
        cancel.setText("Close");
        controller = (AppController) getApplicationContext();
        footer.setVisibility(View.VISIBLE);
        block.setOnClickListener(this);
        cancel.setOnClickListener(this);

        back.setOnClickListener(this);
        if (Utils.isNetworkAvailable(Block_Cancel_Details.this)) {
            progressBar.setVisibility(View.VISIBLE);
            mainLayout.setVisibility(View.GONE);
            new GetData().execute();
        }
    }
    public void showReasonAlert()
    {
        final Dialog dialog = new Dialog(Block_Cancel_Details.this);
        dialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
        dialog.setCancelable(true);
        dialog.setContentView(R.layout.reason_alert);

        final EditText reason = (EditText) dialog.findViewById(R.id.reason);


        Button submit = (Button) dialog.findViewById(R.id.submit);
        submit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(reason.getText().length()>0) {

                    new Block().execute(new String[]{Common.BlockUnBlock,"1"});
                    progressBar2.setVisibility(View.VISIBLE);
                    footer.setVisibility(View.GONE);
                    //new ActivateContract().execute(new String[]{Common.UpdateInitialReading,reason.getText().toString()});
                    dialog.dismiss();
                }else{
                    Toast.makeText(Block_Cancel_Details.this,"Please enter reason",Toast.LENGTH_SHORT).show();
                }
            }
        });

        dialog.show();
    }
    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.back_button:
                finish();
                break;
            case R.id.dep_invoice:

                showReasonAlert();
                break;
            case R.id.con_dcon_invoice:
                progressBar2.setVisibility(View.VISIBLE);
                footer.setVisibility(View.GONE);
                new Block().execute(new String[]{Common.CancelContract,""});
                break;
        }
    }    /*-------------------------------------------------------------------block-------------------------------------------------------*/
    public class Block extends AsyncTask<String, Void, String> {
        String calledMethod;
        @Override
        protected String doInBackground(String... strings) {
            calledMethod=strings[0];
            String result = webServiceAcess.runRequest(Common.runAction,strings[0], new String[]{contractId,strings[1]});
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
                    if(calledMethod.equalsIgnoreCase(Common.BlockUnBlock)) {
                        int status = Fld.getInt("content");
                        if (status == 2) {
                            showAlert("Contract Blocked Sucessfully");
                        } else {

                            Toast.makeText(Block_Cancel_Details.this, "Failed", Toast.LENGTH_SHORT).show();
                        }
                    }else{
                            String message =Fld.isNull("content")?"Contract canceled and Delivery note has been generated": Fld.getString("content");

                            showAlert(message);



                        }


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
    AlertDialog.Builder builder1 = new AlertDialog.Builder(Block_Cancel_Details.this);
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
                Toast.makeText(Block_Cancel_Details.this, "Data not found", Toast.LENGTH_SHORT).show();
            }
        }
    }

    public void setValue() {
        site.setText(model.getSite_value());
        customer_id.setText(model.getCustomer_value());
        contract_date.setText(Utils.getDate(model.getContract_Date()));
        address.setText(model.getCustomer_Address());
        item.setText(model.getProduct_Item());
        depositamount.setText(model.getDeposit_Amount()+" "+model.getCurrency());
        Connection_charges.setText(model.getConnection_charges()+" "+model.getCurrency());
        Disconnection_Charges.setText(model.getDisconnection_Charges()+" "+model.getCurrency());
        Pressure_Factor.setText(model.getPressure_Factor());
        Initial_meter_reading.setText(model.getInitial_meter_reading() +" "+model.getUnits());
        Deposit_Invoice.setText(model.getDeposit_Invoice());
        if (model.getBlock_unblockflag() == 2) {
            block.setVisibility(View.INVISIBLE);
        }
        if (model.getClosemeterreadingvalue() == 2) {
            cancel.setVisibility(View.INVISIBLE);
        }
    }
}