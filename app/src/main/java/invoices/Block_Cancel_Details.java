package invoices;

import android.app.Activity;
import android.app.DatePickerDialog;
import android.app.Dialog;
import android.app.ProgressDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v7.app.AlertDialog;
import android.util.Log;
import android.view.View;
import android.view.Window;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.ProgressBar;
import android.widget.Spinner;
import android.widget.Switch;
import android.widget.TextView;
import android.widget.Toast;

import com.brothersgas.R;

import org.json.JSONArray;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.Calendar;

import activatecontract.ActivationContractDetails;
import butterknife.BindView;
import butterknife.ButterKnife;
import common.AppController;
import common.Common;
import common.WebServiceAcess;
import consumption.Consumption;
import model.BlockUnblockModel;
import model.ContractModel;
import model.ReasonsModel;
import utils.Utils;

public class Block_Cancel_Details  extends Activity implements View.OnClickListener {
    WebServiceAcess webServiceAcess;
    AppController controller;
    String contractId;
    public static ContractModel contractModel=null;
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
    @BindView(R.id.initial_meter_readingTv)
    TextView  intialMeterReadingHeading;
    ArrayList<String>reasons=new ArrayList<>();
    ArrayList<ReasonsModel>reasonsModelList=new ArrayList<>();
    private DatePicker datePicker;
    private Calendar calendar;
    Button re_activeDate=null;
    private int year, month, day;
    String currentMeterReading="";
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_contract_details);
        webServiceAcess = new WebServiceAcess();
        ButterKnife.bind(this);
        contractId = getIntent().getStringExtra("Data");
        heading.setText(contractId +"(Details)");
        intialMeterReadingHeading.setText("Previous Meter Reading");
        block.setText("Block");
        cancel.setText("Close");
        controller = (AppController) getApplicationContext();
        footer.setVisibility(View.VISIBLE);
        block.setOnClickListener(this);
        cancel.setOnClickListener(this);
        calendar = Calendar.getInstance();
        year = calendar.get(Calendar.YEAR);

        month = calendar.get(Calendar.MONTH);
        day = calendar.get(Calendar.DAY_OF_MONTH);

        back.setOnClickListener(this);
        if (Utils.isNetworkAvailable(Block_Cancel_Details.this)) {
            progressBar.setVisibility(View.VISIBLE);
            mainLayout.setVisibility(View.GONE);
            new GetData().execute();
        }
    }
    @SuppressWarnings("deprecation")
    public void setDate(View view) {
        showDialog(999);

    }

    @Override
    protected Dialog onCreateDialog(int id) {
        // TODO Auto-generated method stub
        if (id == 999) {
            return new DatePickerDialog(this,
                    myDateListener, year, month, day);
        }
        return null;
    }

    private DatePickerDialog.OnDateSetListener myDateListener = new
            DatePickerDialog.OnDateSetListener() {
                @Override
                public void onDateSet(DatePicker arg0,
                                      int arg1, int arg2, int arg3) {
                    // TODO Auto-generated method stub
                    // arg1 = year
                    // arg2 = month
                    // arg3 = day
                    showDate(arg1, arg2+1, arg3);
                }
            };
    private void showDate(int year, int month, int day) {
        String dayValue=Integer.toString(day);
        String monthValue=Integer.toString(month);
        if(month<10)
        {
            monthValue="0"+month;
        }
        if(day<10)
        {
            dayValue="0"+day;
        }
        re_activeDate.setText(new StringBuilder().append(dayValue).append("/")
                .append(monthValue).append("/").append(year));
    }
    /*-------------------------------------------------------------------getData-------------------------------------------------------*/
    public class GetReasons extends AsyncTask<String, Void, String> {
        ProgressDialog pd1;

        @Override
        protected void onPreExecute() {

            pd1=new ProgressDialog(Block_Cancel_Details.this);
            pd1.setMessage("Loading....");
            pd1.setCancelable(false);
            pd1.show();
            super.onPreExecute();

        }

        @Override
        protected String doInBackground(String... strings) {

            String result = webServiceAcess.runRequest(Common.runAction, Common.Reasons,new String[]{"1"});
            return result;
        }

        @Override
        protected void onPostExecute(String s) {
            Log.e("value", "onPostExecute: ", null);
            if (s.length() > 0) {
                try {
                    JSONObject jsonObject = new JSONObject(s);
                    JSONObject result = jsonObject.getJSONObject("RESULT");
                    JSONObject tab = result.getJSONObject("TAB");
                    JSONArray jsonArray = tab.getJSONArray("LIN");
                    for (int i = 0; i < jsonArray.length(); i++) {
                        JSONObject item = jsonArray.getJSONObject(i);
                        ReasonsModel model=new ReasonsModel(item.getJSONArray("FLD"));
                        reasonsModelList.add(model);
                        reasons.add(model.getReason());
                    }


                } catch (Exception ex) {
                    ex.fillInStackTrace();
                }
            } else {

                Utils.showAlert(Block_Cancel_Details.this, "Data not found");
            }
            pd1.cancel();

        }}
    public void showCancelAlert()
    {
        final Dialog dialog = new Dialog(Block_Cancel_Details.this);
        dialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
        dialog.setCancelable(true);
        dialog.setContentView(R.layout.reason_alert);
        final EditText currentmeterReading=(EditText)dialog.findViewById(R.id.current_reading) ;
        LinearLayout reasonView=(LinearLayout)dialog.findViewById(R.id.reasonView);
        LinearLayout dateView=(LinearLayout)dialog.findViewById(R.id.dateView);
        dateView.setVisibility(View.GONE);
        reasonView.setVisibility(View.GONE);

        final Spinner   reason=(Spinner)dialog.findViewById(R.id.reason);
        re_activeDate = (Button) dialog.findViewById(R.id.react_date);
        reason.setVisibility(View.GONE);
        re_activeDate.setVisibility(View.GONE);

        reason.setAdapter(new ArrayAdapter<String>(Block_Cancel_Details.this, android.R.layout.simple_spinner_item,reasons));
        Button submit = (Button) dialog.findViewById(R.id.submit);

        submit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if((currentmeterReading.getText().length()>0)) {

                    new Cancel().execute(new String[]{Common.CancelContract,"1",currentmeterReading.getText().toString()});
                    progressBar2.setVisibility(View.VISIBLE);
                    footer.setVisibility(View.GONE);
                    //new ActivateContract().execute(new String[]{Common.UpdateInitialReading,reason.getText().toString()});
                    dialog.dismiss();
                }else{

                        Toast.makeText(Block_Cancel_Details.this, "Please enter current meter reading", Toast.LENGTH_SHORT).show();

                }
            }
        });

        dialog.show();
    }

    public void showReasonAlert()
    {
        final Dialog dialog = new Dialog(Block_Cancel_Details.this);
        dialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
        dialog.setCancelable(true);
        dialog.setContentView(R.layout.reason_alert);
        final LinearLayout dateView=(LinearLayout)dialog.findViewById(R.id.dateView);
        final EditText currentmeterReading=(EditText)dialog.findViewById(R.id.current_reading) ;
        final Spinner   reason=(Spinner)dialog.findViewById(R.id.reason);
          re_activeDate = (Button) dialog.findViewById(R.id.react_date);

        reason.setAdapter(new ArrayAdapter<String>(Block_Cancel_Details.this, android.R.layout.simple_spinner_item,reasons));
        reason.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                if(reasonsModelList.get(position).getReactivation()!=0)
                {
                    dateView.setVisibility(View.GONE);
                }else{
                    dateView.setVisibility(View.VISIBLE);
                }
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });
        Button submit = (Button) dialog.findViewById(R.id.submit);
        re_activeDate.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                setDate(v);
            }
        });
        submit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String reactivationDate="";
                if(re_activeDate.getText().toString().length()>0)
                {
                    reactivationDate=Utils.getFormatted(re_activeDate.getText().toString());
                }
                if(isFiledsValidated(reason,re_activeDate,currentmeterReading)) {

                    new Block().execute(new String[]{Common.BlockUnBlock,"1",reason.getSelectedItem().toString(),reactivationDate,currentmeterReading.getText().toString()});
                    progressBar2.setVisibility(View.VISIBLE);
                    footer.setVisibility(View.GONE);
                    //new ActivateContract().execute(new String[]{Common.UpdateInitialReading,reason.getText().toString()});
                    dialog.dismiss();
                }else{

                }
            }
        });

        dialog.show();
    }

    public boolean isFiledsValidated(Spinner reason, Button re_activeDate, EditText currentmeterReading) {
        boolean status = false;
        if (reasonsModelList.get(reason.getSelectedItemPosition()).getReactivation()!= 0) {
            if (currentmeterReading.getText().length() > 0) {
                status = true;
            } else {
                Toast.makeText(Block_Cancel_Details.this, "Please enter Current meter reading", Toast.LENGTH_SHORT).show();
            }
        } else {
            if ((re_activeDate.getText().length() > 0) && (currentmeterReading.getText().length() > 0)) {
                status = true;
            } else {
                if (currentmeterReading.getText().length() == 0) {
                    Toast.makeText(Block_Cancel_Details.this, "Please enter Current meter reading", Toast.LENGTH_SHORT).show();
                } else {
                    Toast.makeText(Block_Cancel_Details.this, "Please select approx Re Activation date", Toast.LENGTH_SHORT).show();
                }
            }


        }
        return status;
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
                showCancelAlert();

                break;
        }
    }    /*-------------------------------------------------------------------block-------------------------------------------------------*/
    public class Block extends AsyncTask<String, Void, String> {
        String calledMethod;
        String currenttReading="";
        @Override
        protected String doInBackground(String... strings) {
            calledMethod=strings[0];
            currenttReading=strings[4];
            String result = webServiceAcess.runRequest(Common.runAction,strings[0], new String[]{contractId,strings[1],strings[2],strings[3],strings[4]});
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
                    BlockUnblockModel modell = new BlockUnblockModel(item.getJSONArray("FLD"));
                    if (modell.getStatus() == 2) {
                        if((modell.getConsumption_invoice().length() > 0)||(modell.getConsumption_invoice().length()>0)) {
                            model.setCustomerName(contractModel.getCustomername());
                            model.setContractNumber(contractModel.getContract_Meternumber());
                            model.setConsumptionInvoice(modell.getConsumption_invoice());
                            model.setCurrentMeterReading(currenttReading);
                            contractModel.setBlock_unblockflag(2);
                            Print_Email.model = model;
                            Print_Email.calledMethod="3";

                            Utils.showAlertNavigateToPrintEmail(Block_Cancel_Details.this, modell.getMessage(), Print_Email.class);
                        } else {
                            Utils.showAlertNormal(Block_Cancel_Details.this, modell.getMessage());

                        }

                        block.setVisibility(View.INVISIBLE);
                    } else {

                        Utils.showAlertNormal(Block_Cancel_Details.this, modell.getMessage());
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


    /*-------------------------------------------------------------------block-------------------------------------------------------*/
    public class Cancel extends AsyncTask<String, Void, String> {
        String calledMethod;
        String currenttReading="";
        @Override
        protected String doInBackground(String... strings) {
            calledMethod=strings[0];
            currenttReading=strings[2];
            String result = webServiceAcess.runRequest(Common.runAction,strings[0], new String[]{contractId,strings[2]});
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
                    BlockUnblockModel modell=new BlockUnblockModel(item.getJSONArray("FLD"));
                            if (modell.getConsumption_invoice().length()>0) {
                                model.setCustomerName(contractModel.getCustomername());
                                model.setConsumptionInvoice(modell.getConsumption_invoice());
                                model.setCurrentMeterReading(currenttReading);
                                contractModel.setClosemeterreadingvalue(2);
                                Print_Email.model=model;
                                Print_Email.calledMethod="3";
                                cancel.setVisibility(View.INVISIBLE);
                                Utils.showAlertNavigateToPrintEmail(Block_Cancel_Details.this,modell.getMessage(),Print_Email.class);
                            }else{
                                Utils.showAlertNormal(Block_Cancel_Details.this,modell.getMessage());
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
                        new GetReasons().execute();
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
        if((model.getPreviousReading().length()>0)&&(!model.getPreviousReading().equalsIgnoreCase("0")))
        {
            Initial_meter_reading.setText(model.getPreviousReading());
        }else {
            Initial_meter_reading.setText(model.getInitial_meter_reading() + " " + model.getUnits());
        }
        Deposit_Invoice.setText(model.getDeposit_Invoice());
        Connection_Disconnection_Invoice.setText(model.getConnection_Disconnection_Invoice());
        if (Integer.parseInt(model.getCloseFlag()) == 2) {
            cancel.setVisibility(View.INVISIBLE);
        }
        if (Integer.parseInt(model.getBlockFlag()) == 2) {
            block.setVisibility(View.INVISIBLE);
        }
    }
}