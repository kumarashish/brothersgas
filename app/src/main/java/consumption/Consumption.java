package consumption;

import android.app.Activity;
import android.app.DatePickerDialog;
import android.app.Dialog;
import android.app.ProgressDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v7.app.AlertDialog;
import android.text.format.DateFormat;
import android.util.Log;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.AutoCompleteTextView;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.ProgressBar;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.ScrollView;
import android.widget.TextView;
import android.widget.Toast;

import com.brothersgas.R;
import com.whygraphics.multilineradiogroup.MultiLineRadioGroup;

import org.json.JSONArray;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;

import adapter.ContractListAdapter;
import adapter.CustomListAdapter;
import butterknife.BindView;
import butterknife.ButterKnife;
import common.AppController;
import common.Common;
import common.WebServiceAcess;
import contracts.ContractDetails;
import contracts.Search;
import interfaces.ListItemClickListner;
import invoices.Block_Cancel_Details;
import invoices.Connection_Disconnection_Invoice;
import invoices.Connection_Disconnection_Invoice_details;
import model.ContractModel;
import utils.Utils;

public class Consumption  extends Activity implements View.OnClickListener {
    AppController controller;
    WebServiceAcess webServiceAcess;
    @BindView(R.id.progressBar)
    ProgressBar progressBar;
    @BindView(R.id.back_button)
    Button back;

    @BindView(R.id.contractNumber)
    EditText contractNumber;
    @BindView(R.id.date)
    EditText date;
    @BindView(R.id.consumer)
    AutoCompleteTextView consumer;
    @BindView(R.id.address)
    EditText realEstateOwnerDescription;
    @BindView(R.id.real_Estate_owner)
    EditText realEstateOwner;
    @BindView(R.id.meter_prblm)
    CheckBox meterProblem;
    @BindView(R.id.meterId)
    EditText meterId;
    @BindView(R.id.previousDate)
    EditText previousDate;
    @BindView(R.id.currentDate)
    EditText currentDate;
    @BindView(R.id.submit)
    Button submit;
    public static model.ContractDetails model;
    private DatePicker datePicker;
    private Calendar calendar;
    private int year, month, day;
    @BindView(R.id.issueList)
    LinearLayout issueList;
    @BindView(R.id.contentView)
    ScrollView contentView;
    ArrayList<ContractModel> list = new ArrayList<>();
    ProgressDialog progressDialog;
@BindView(R.id.retype_currentReading)
EditText retype_currentReading;
@BindView(R.id.currentReading)
EditText currentReading;
@BindView(R.id.reason)
    RadioGroup reason;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.consumption);
        controller = (AppController) getApplicationContext();
        webServiceAcess = new WebServiceAcess();
        ButterKnife.bind(this);
        submit.setOnClickListener(this);
        back.setOnClickListener(this);
        currentDate.setOnClickListener(this);
        progressDialog=new ProgressDialog(this);
        progressDialog.setIndeterminate(false);
        progressDialog.setProgressStyle(ProgressDialog.STYLE_SPINNER);
        calendar = Calendar.getInstance();
        year = calendar.get(Calendar.YEAR);
        month = calendar.get(Calendar.MONTH);
        day = calendar.get(Calendar.DAY_OF_MONTH);
        meterProblem.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                if(isChecked) {
                    issueList.setVisibility(View.VISIBLE);
                } else {
                    issueList.setVisibility(View.GONE);
                }
            }
        });
        if (Utils.isNetworkAvailable(Consumption.this)) {
            progressBar.setVisibility(View.VISIBLE);
            contentView.setVisibility(View.GONE);
            new GetData().execute();
        }


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
                    showDate(arg1, arg2 + 1, arg3);
                }
            };

    private void showDate(int year, int month, int day) {
        String date = Integer.toString(year);
        if (month < 10) {
            date += "-" + "0" + month;
        } else {
            date += "-" + month;
        }

        if (day < 10) {
            date += "-" + "0" + day;
        } else {
            date += "-" + day;
        }
        currentDate.setText(date);
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.back_button:
                finish();
                break;
            case R.id.submit:
             if(contractNumber.getText().length()>0)
             {
                 if((currentReading.getText().length()>0)&&(retype_currentReading.getText().length()>0))
                 {
                     if(currentReading.getText().toString().trim().equals(retype_currentReading.getText().toString().trim()))
                     {
                       new GenerateInvoice().execute();
                     }else{
                         Utils.showAlertNormal(Consumption.this,"Curent Reading and Retype Reading must be same");
                     }
                 }else{
                     if(currentReading.getText().length()==0)
                     {
                         Utils.showAlertNormal(Consumption.this,"Please enter  Current Reading");
                     }else {
                         Utils.showAlertNormal(Consumption.this,"Please enter value in Retype Current Reading");
                     }
                 }
             }else{
                 Utils.showAlertNormal(Consumption.this,"Please select consumer");
             }
             break;
            case R.id.currentDate:
                showDialog(999);
                break;
        }

    }

//    public void setData() {
//        if (model != null) {
//            contractNumber.setText(model.getContractNumber());
//            consumer.setText(model.getCustomer_value());
//            address.setText(model.getCustomer_Address());
//            date.setText(model.getContract_Date());
//            meterId.setText(model.getContractNumber());
//
//        }
//        Date d = new Date();
//        CharSequence s = DateFormat.format("yyyy-MM-dd", d.getTime());
//        currentDate.setText(s);
//
//    }


    /*-------------------------------------------------------------------getData-------------------------------------------------------*/
    public class GetData extends AsyncTask<String, Void, String> {
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

                            if ((model.getDepositInvoice().length() == 0) || (model.getConnection_discconectionInvoice().length() == 0)) {
                                list.add(model);
                            }
                        }
                    }
                    if (list.size() > 0) {
                        CustomListAdapter adapter = new CustomListAdapter(Consumption.this, R.layout.contract_row, list);
                        consumer.setAdapter(adapter);
                        consumer.setOnItemClickListener(onItemClickListener);
                        progressBar.setVisibility(View.GONE);
                        contentView.setVisibility(View.VISIBLE);

                    } else {
                        progressBar.setVisibility(View.GONE);
                        Utils.showAlert(Consumption.this, "No data Found");

                    }
                } catch (Exception ex) {
                    ex.fillInStackTrace();
                }
            } else {
                progressBar.setVisibility(View.GONE);
                Utils.showAlert(Consumption.this, "Data not found");
            }


        }
    }

    /*-------------------------------------------------------------------getContractDetailsData-------------------------------------------------------*/
    public class GetContractDetails extends AsyncTask<String,Void,String> {
        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            progressDialog.show();
        }

        @Override
        protected String doInBackground(String... strings) {
            String result=webServiceAcess.runRequest(Common.runAction,Common.ContractView,  new String[]{  contractNumber.getText().toString().trim()});
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
                    JSONArray jsonArray=result.getJSONArray("GRP");
                    JSONObject item=jsonArray.getJSONObject(1);
                    model=new model.ContractDetails(item.getJSONArray("FLD"));
                    if(model!=null)
                    {
                        progressDialog.cancel();

                    }
                }catch (Exception ex)
                {
                    ex.fillInStackTrace();
                }
            }else{
                progressDialog.cancel();

                Toast.makeText(Consumption.this,"Data not found",Toast.LENGTH_SHORT).show();
            }
        }
    }

    public void setValue( ContractModel model)
    {
                 consumer.setText(model.getCustomername());
                 contractNumber.setText(model.getContract_Meternumber());
                  date.setText(model.getContactcreationdate());
                  realEstateOwner.setText(model.getOwner());
                  realEstateOwnerDescription.setText(model.getOwnerDesc());
        //new GetContractDetails().execute();
    }

    private AdapterView.OnItemClickListener onItemClickListener =
            new AdapterView.OnItemClickListener() {
                @Override
                public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {

                    Object item = adapterView.getItemAtPosition(i);
                    if (item instanceof ContractModel) {
                        ContractModel model = (ContractModel) item;
                                   setValue(model);
                    }

                }
            };


    /*-------------------------------------------------------------------getData-------------------------------------------------------*/
    public class GenerateInvoice extends AsyncTask<String, Void, String> {
        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            progressDialog.show();
        }

        @Override
        protected String doInBackground(String... strings) {
            String meterProblemValue="0";
            String reasonValue="";
            if(meterProblem.isChecked()) {
                meterProblemValue="2";
                RadioButton radioButton=(RadioButton)findViewById(reason.getCheckedRadioButtonId());
                reasonValue=radioButton.getText().toString();
            }
            String result = webServiceAcess.runRequest(Common.runAction, Common.GenerateDeliveryNote, new String[]{contractNumber.getText().toString(),currentReading.getText().toString(),retype_currentReading.getText().toString(),meterProblemValue,reasonValue});
            return result;
        }

        @Override
        protected void onPostExecute(String s) {
            Log.e("value", "onPostExecute: ", null);
            if (s.length() > 0) {
                try {
                    JSONObject jsonObject = new JSONObject(s);
                    JSONObject result = jsonObject.getJSONObject("RESULT");
                    JSONObject jsonObject1 = result.getJSONObject("GRP");
                    JSONArray fld = jsonObject1.getJSONArray("FLD");
                   JSONObject messageJSON=fld.getJSONObject(7);
                    JSONObject deliveryNoteJSON=fld.getJSONObject(5);
                    JSONObject invoiceNumberJSON=fld.getJSONObject(6);
                    String deliveryNote=deliveryNoteJSON.isNull("content")?"":deliveryNoteJSON.getString("content");
                    String invoiceNumber=invoiceNumberJSON.isNull("content")?"":invoiceNumberJSON.getString("content");
                    String message=messageJSON.isNull("content")?"":messageJSON.getString("content");

                    if(invoiceNumber.length()>0)
                    {
                        Utils.showAlertNavigateToPrintEmail(Consumption.this,message);

                    }else{
                        Utils.showAlertNormal(Consumption.this,message);

                    }
                } catch (Exception ex) {
                    ex.fillInStackTrace();
                }
            }
            progressDialog.cancel();
        }
    }
}