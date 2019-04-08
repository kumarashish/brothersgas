package invoices;

import android.app.Activity;
import android.content.DialogInterface;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v7.app.AlertDialog;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.ProgressBar;
import android.widget.TextView;
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
import model.ContractModel;
import utils.Utils;

public class Connection_Disconnection_Invoice_details  extends Activity implements View.OnClickListener {
    WebServiceAcess webServiceAcess;
    AppController controller;
    String contractId;
    public static model.ContractDetails model = null;
    public static ContractModel contractModel=null;
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
    @BindView(R.id.initial_meter_readingTv)
    TextView initial_meter_readingTv;
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
    @BindView(R.id.heading)
    android.widget.TextView heading;
    @BindView(R.id.edit)
    Button edit;
    @BindView(R.id.submit)
    Button submit;
    @BindView(R.id.editView)
    LinearLayout editView;
    @BindView(R.id.in_meter_reading)
    EditText in_meter_reading;
    @BindView(R.id.in_meterReading_View)
    LinearLayout in_meterReading_View;
    public static boolean isCalledFromTenanatChange=false;
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_contract_details);
        webServiceAcess = new WebServiceAcess();
        ButterKnife.bind(this);
        contractId = contractModel.getContract_Meternumber() ;
        heading.setText(contractModel.getContract_Meternumber() +"(Details)");
        controller = (AppController) getApplicationContext();
        footer.setVisibility(View.VISIBLE);
        con_dconInvoice.setOnClickListener(this);
        dep_Invoice.setOnClickListener(this);
        initial_meter_readingTv.setText("Initial Meter Reading");
       if(isCalledFromTenanatChange==false) {
           edit.setVisibility(View.VISIBLE);
       }else{
           edit.setVisibility(View.GONE);
       }
        edit.setOnClickListener(this);
        submit.setOnClickListener(this);
        back.setOnClickListener(this);

        if (Utils.isNetworkAvailable(Connection_Disconnection_Invoice_details.this)) {
            progressBar.setVisibility(View.VISIBLE);
            mainLayout.setVisibility(View.GONE);
            new GetData().execute();
        }
        Initial_meter_reading.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {

            }

            @Override
            public void afterTextChanged(Editable s) {

                if(s.toString().contains("."))
                {   int index=s.toString().indexOf(".");
                    int count=Initial_meter_reading.getText().toString().substring( index,s.length()).length();
                    if(count>=5)
                    {    String []value=Initial_meter_reading.getText().toString().split("\\.");
                        String  text=value[1].substring(0,3);
                        Initial_meter_reading.setText(value[0]+"."+text);
                        Initial_meter_reading.setSelection(Initial_meter_reading.getText().length());

                    }


                }
            }
        });
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.back_button:
                finish();
                break;

            case R.id.con_dcon_invoice:
                progressBar2.setVisibility(View.VISIBLE);
                footer.setVisibility(View.GONE);
                    new GenerateInvoice().execute(new String[]{"2"});



                break;
            case R.id.edit:
                Initial_meter_reading.setFocusable(true);
                Initial_meter_reading.setEnabled(true);
                Initial_meter_reading.requestFocus();
                editView.setVisibility(View.VISIBLE);
                footer.setVisibility(View.GONE);
                if(Initial_meter_reading.getText().length()>0)
                {
                    Initial_meter_reading .setSelection(Initial_meter_reading.getText().length());
                }
                break;
            case R.id.dep_invoice:
                progressBar2.setVisibility(View.VISIBLE);
                footer.setVisibility(View.GONE);
                new GenerateInvoice().execute(new String[]{"1"});
                break;
            case R.id.submit:
                progressBar2.setVisibility(View.VISIBLE);
                editView.setVisibility(View.GONE);
               new Update().execute(new String[]{Common.UpdateInitialReading});;
                break;
        }
    }

    /*-------------------------------------------------------------------block-------------------------------------------------------*/
    public class Update extends AsyncTask<String, Void, String> {
        @Override
        protected String doInBackground(String... strings) {

            String result = webServiceAcess.runRequest(Common.runAction,strings[0], new String[]{contractId,Initial_meter_reading.getText().toString()});
            return result;
        }

        @Override
        protected void onPostExecute(String s) {
            Log.e("value", "onPostExecute: "+s, null);
            if (s.length() > 0) {
                try {
                    JSONObject jsonObject = new JSONObject(s);
                    JSONObject result = jsonObject.getJSONObject("RESULT");
                    JSONArray jsonArray = result.getJSONArray("GRP");
                    JSONObject item = jsonArray.getJSONObject(1);
                    JSONArray Fld = item.getJSONArray("FLD");
                    JSONObject statusObject=Fld.getJSONObject(0);
                    JSONObject messageObject=Fld.getJSONObject(1);
                    int status = statusObject.getInt("content");
                    String message =messageObject.isNull("content")?"Message not available": messageObject.getString("content");
                    if(status==2) {
                        Initial_meter_reading.setEnabled(false);
                        model.setInitial_meter_reading(Initial_meter_reading.getText().toString());
                        Utils.showAlertNormal(Connection_Disconnection_Invoice_details.this, message);
                        progressBar2.setVisibility(View.GONE);
                        editView.setVisibility(View.GONE);
                        handleEditButton();
                    }else {
                        Utils.showAlertNormal(Connection_Disconnection_Invoice_details.this, message);
                        progressBar2.setVisibility(View.GONE);
                        editView.setVisibility(View.VISIBLE);
                        footer.setVisibility(View.GONE);
                    }
                } catch (Exception ex) {
                    ex.fillInStackTrace();
                    progressBar2.setVisibility(View.GONE);
                    editView.setVisibility(View.VISIBLE);
                    footer.setVisibility(View.GONE);
                }
            }else{
                Utils.showAlertNormal(Connection_Disconnection_Invoice_details.this,Common.message);
                progressBar2.setVisibility(View.GONE);
                editView.setVisibility(View.VISIBLE);
                footer.setVisibility(View.GONE);
            }

        }
    }


    public void handleEditButton()
    {
        if((model.getDeposit_Invoice().length()>0)&&(model.getConnection_Disconnection_Invoice().length()>0))
        {
            footer.setVisibility(View.GONE);
            edit.setVisibility(View.GONE);
        }else   if((model.getDeposit_Invoice().length()==0)&&(model.getConnection_Disconnection_Invoice().length()==0)){
            footer.setVisibility(View.VISIBLE);
            dep_Invoice.setVisibility(View.VISIBLE);
            con_dconInvoice.setVisibility(View.VISIBLE);
            if(isCalledFromTenanatChange==false) {
                edit.setVisibility(View.VISIBLE);
            }else{
                edit.setVisibility(View.GONE);
            }

        }

        else {
            if (model.getDeposit_Invoice().length() > 0) {
                dep_Invoice.setVisibility(View.INVISIBLE);
                edit.setVisibility(View.GONE);

            } else if (model.getConnection_Disconnection_Invoice().length() > 0) {
                con_dconInvoice.setVisibility(View.INVISIBLE);
                edit.setVisibility(View.GONE);
            }
            footer.setVisibility(View.VISIBLE);
        }
    }
    /*-------------------------------------------------------------------block-------------------------------------------------------*/
    public class GenerateInvoice extends AsyncTask<String, Void, String> {
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
                    JSONArray Fld = item.getJSONArray("FLD");
                    JSONObject messageJsonObject=Fld.getJSONObject(1);
                    JSONObject depJsonObject=Fld.getJSONObject(0);
                    String message =messageJsonObject.isNull("content")?"No Message From API": messageJsonObject.getString("content");
                    String depInvoice=depJsonObject.isNull("content")?"": depJsonObject.getString("content");
                    if((message.contains("Invoice Already Exists")||message.contains("No Message From API")))

                    {
                        Utils.showAlertNormal(Connection_Disconnection_Invoice_details.this, message);
                    }else{
                        if (calledMethod.equalsIgnoreCase("1"))

                        //
                        {   model.setDeposit_Invoice(depInvoice);
                            model.setContractNumber(contractId);
                            model.setCustomerName(model.getCustomer_value());
                            Deposit_Invoice.setText(model.getDeposit_Invoice());
                            if(model.getDeposit_Invoice().length()>0) {
                                Print_Email.model = model;
                                Print_Email.calledMethod = calledMethod;
                                Utils.showAlertNavigateToPrintEmail(Connection_Disconnection_Invoice_details.this, message, Print_Email.class);
                            }else{
                                Utils.showAlertNormal(Connection_Disconnection_Invoice_details.this, message);
                            }



                        } else {

                                model.setConnection_Disconnection_Invoice(depInvoice);
                                model.setContractNumber(contractId);
                               model.setCustomerName(model.getCustomer_value());
                                Connection_Disconnection_Invoice.setText(model.getConnection_Disconnection_Invoice());
                            if(model.getConnection_Disconnection_Invoice().length()>0) {
                                Print_Email.model = model;
                                Print_Email.calledMethod = calledMethod;
                                Utils.showAlertNavigateToPrintEmail(Connection_Disconnection_Invoice_details.this, message, Print_Email.class);
                            }else{
                                Utils.showAlertNormal(Connection_Disconnection_Invoice_details.this, message);
                            }
                        }
                        //model.setCustomerName(contractModel.getCustomername());


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

    /*-------------------------------------------------------------------getData-------------------------------------------------------*/
    public class GetData extends AsyncTask<String, Void, String> {
        @Override
        protected String doInBackground(String... strings) {
            String result = webServiceAcess.runRequest(Common.runAction, Common.ContractView, new String[]{contractModel.getContract_Meternumber()});
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
        contract_date.setText(Utils.getDate(model.getContract_Date()));
        address.setText(model.getCustomer_Address());
        item.setText(model.getProduct_Item());
        depositamount.setText(model.getDeposit_Amount()+" "+model.getCurrency());
        Connection_charges.setText(model.getConnection_charges()+" "+model.getCurrency());
        Disconnection_Charges.setText(model.getDisconnection_Charges()+" "+model.getCurrency());
        Pressure_Factor.setText(model.getPressure_Factor());
        //in_meter_reading.setText(model.getInitial_meter_reading());
        in_meterReading_View.setVisibility(View.GONE);
        Initial_meter_reading.setText(model.getInitial_meter_reading());
        Deposit_Invoice.setText(model.getDeposit_Invoice());
        Connection_Disconnection_Invoice.setText(model.getConnection_Disconnection_Invoice());
        if((model.getDeposit_Invoice().length()>0)&&(model.getConnection_Disconnection_Invoice().length()>0))
        {
            footer.setVisibility(View.GONE);
            edit.setVisibility(View.GONE);
        }else   if((model.getDeposit_Invoice().length()==0)&&(model.getConnection_Disconnection_Invoice().length()==0)){
            footer.setVisibility(View.VISIBLE);
            dep_Invoice.setVisibility(View.VISIBLE);
            con_dconInvoice.setVisibility(View.VISIBLE);
            if(isCalledFromTenanatChange==false) {
                edit.setVisibility(View.VISIBLE);
            }else{
                edit.setVisibility(View.GONE);
            }
        }
        else {
          if (model.getDeposit_Invoice().length() > 0) {
                dep_Invoice.setVisibility(View.INVISIBLE);
              edit.setVisibility(View.GONE);

            } else if (model.getConnection_Disconnection_Invoice().length() > 0) {
                    con_dconInvoice.setVisibility(View.INVISIBLE);
              edit.setVisibility(View.GONE);
            }
            footer.setVisibility(View.VISIBLE);
        }
    }

    @Override
    protected void onDestroy() {
        isCalledFromTenanatChange=false;
        super.onDestroy();
    }
}