package com.brothersgas;

import android.app.Activity;
import android.app.AlertDialog;
import android.app.DatePickerDialog;
import android.app.Dialog;
import android.os.AsyncTask;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ProgressBar;
import android.widget.Toast;

import com.itextpdf.text.pdf.PdfPRow;

import org.json.JSONArray;
import org.json.JSONObject;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;

import butterknife.BindView;
import butterknife.ButterKnife;
import common.AppController;
import common.Common;
import common.TextView;
import common.WebServiceAcess;
import consumption.Consumption;
import model.EnquiryModel;
import utils.Utils;

/**
 * Created by ashish.kumar on 28-02-2019.
 */

public class Enquiry extends Activity implements View.OnClickListener {

    WebServiceAcess webServiceAcess;
    @BindView(R.id.mainLayout)
    LinearLayout mainLayout;
    @BindView(R.id.progressBar)
    ProgressBar progressBar;
    @BindView(R.id.s_date)
    Button startDate;
    @BindView(R.id.e_date)
    Button endDate;
    @BindView(R.id.search)
    Button search;
    @BindView(R.id.done)
    Button ok;
    AppController controller;
    @BindView(R.id.back_button)
    Button back_button;
    @BindView(R.id.generated_invoice_count)
    android.widget.TextView generatedInvoicecount;
    @BindView(R.id.number_cash_payment)
    android.widget.TextView number_cash_payment;
    @BindView(R.id.total_cash_amount_collected)
     android.widget.TextView total_cash_amount_collected;
    @BindView(R.id.chequepayment_count)
     android.widget.TextView chequepayment_count;
    @BindView(R.id.total_cheaqueamout)
     android.widget.TextView total_cheaqueamout;
    @BindView(R.id.total_cheaque_cash_amount)
     android.widget.TextView total_cheaque_cash_amount;
    @BindView(R.id.dep_conn_dconn)
     android.widget.TextView dep_conn_dconn;
    @BindView(R.id.total_dep_inv_amount)
      android.widget.TextView total_dep_inv_amount;
    @BindView(R.id.total_con_dconn_amount)
      android.widget.TextView total_con_dconn_amount;
    @BindView(R.id.teenant_change)
    android.widget.TextView teenant_Change;
    private DatePicker datePicker;
    private Calendar calendar;

    private int year, month, day;

    int selectedOption;
    int startDateSelcted=1,endDateSelected=2;
    boolean startDateSlected=false;
    boolean endDateSlected=false;
EnquiryModel model=null;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.enquiry);
        //getSupportActionBar().setTitle("");
        // Add titleTextView into ActionBar
        ButterKnife.bind(this);
        webServiceAcess = new WebServiceAcess();
        controller = (AppController) getApplicationContext();
        back_button.setOnClickListener(this);
        ok.setOnClickListener(this);
        startDate.setOnClickListener(this);
        endDate.setOnClickListener(this);
        search.setOnClickListener(this);
        calendar = Calendar.getInstance();
        year = calendar.get(Calendar.YEAR);
        month = calendar.get(Calendar.MONTH);
        day = calendar.get(Calendar.DAY_OF_MONTH);

    }
public long getMinDate(String datee) {
    SimpleDateFormat sdf = new SimpleDateFormat("dd/MM/yyyy");

    try {
        //formatting the dateString to convert it into a Date
        Date date = sdf.parse(datee);
        System.out.println("Given Time in milliseconds : " + date.getTime());

        Calendar calendar = Calendar.getInstance();
        //Setting the Calendar date and time to the given date and time
        calendar.setTime(date);
        System.out.println("Given Time in milliseconds : " + calendar.getTimeInMillis());
return calendar.getTimeInMillis();
    } catch (ParseException e) {
        e.printStackTrace();
    }
    return 0;
}

    @Override
    protected Dialog onCreateDialog(int id) {
        // TODO Auto-generated method stub
        if (id == 999) {
            DatePickerDialog dialog=  new DatePickerDialog(this, myDateListener, year, month, day);
            if(endDate.getText().length()>0) {
                dialog.getDatePicker().setMaxDate(getMinDate(endDate.getText().toString()));
            }else{
                dialog.getDatePicker().setMaxDate(Calendar.getInstance().getTimeInMillis());
            }
            return dialog;
        }
        else if (id == 991) {
            DatePickerDialog dialog=  new DatePickerDialog(this, myDateListener2, year, month, day);
            if(startDate.getText().length()>0) {
                dialog.getDatePicker().setMinDate(getMinDate(startDate.getText().toString()));
                dialog.getDatePicker().setMaxDate(Calendar.getInstance().getTimeInMillis());
            }
            return dialog;

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

    private DatePickerDialog.OnDateSetListener myDateListener2 = new
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
        switch (selectedOption)
        {
            case 1:
                startDate.setText(new StringBuilder().append(dayValue).append("/")
                        .append(monthValue).append("/").append(year));
                startDateSlected=true;
                break;
            case 2:
                endDate.setText(new StringBuilder().append(dayValue).append("/").append(monthValue).append("/").append(year));
                endDateSlected=true;
                break;
        }

    }
    @Override
    public void onClick(View v) {
        switch (v.getId())
        {
            case R.id.done:
            case R.id.back_button:
                finish();
                break;
            case R.id.s_date:
                selectedOption=startDateSelcted;
                showDialog(999);;

                break;
            case R.id.e_date:
                selectedOption=endDateSelected;
                showDialog(991);;
                break;
            case R.id.search:
                if((startDateSlected==true)&&(endDateSlected==true))
                {
                    new GetEnquiryData().execute();
                }else {
                    if(startDateSlected==false)
                    {
                        Toast.makeText(Enquiry.this,"Please Select start date",Toast.LENGTH_SHORT).show();
                    }else{
                        Toast.makeText(Enquiry.this,"Please Select end date",Toast.LENGTH_SHORT).show();
                    }
                }

                break;
        }

    }
    /*-------------------------------------------------------------------getContractDetailsData-------------------------------------------------------*/
    public class GetEnquiryData extends AsyncTask<String,Void,String> {
        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            progressBar.setVisibility(View.VISIBLE);
        }

        @Override
        protected String doInBackground(String... strings) {
            String result=webServiceAcess.runRequest(Common.runAction,Common.Enqiry,  new String[]{ controller.getManager().getLoggedInUserName(), Utils.getFormatted(startDate.getText().toString()),Utils.getFormatted(endDate.getText().toString())});
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
                    JSONObject groupJSON=result.getJSONObject("GRP");
                    model=new EnquiryModel(groupJSON.getJSONArray("FLD"));
                    setValue();
                }catch (Exception ex)
                {
                    ex.fillInStackTrace();
                }
                progressBar.setVisibility(View.GONE);
            }else{
                progressBar.setVisibility(View.GONE);

                Toast.makeText(Enquiry.this,"Data not found",Toast.LENGTH_SHORT).show();
            }
        }

        public void setValue() {
            if (model != null) {

                generatedInvoicecount.setText(model.getNumber_of_invoices());
                number_cash_payment.setText(model.getNumber_of_Cash_Payments());
                total_cash_amount_collected.setText(model.getTotal_Cash_payments_amount());
                chequepayment_count.setText(model.getNumber_of_Cheque_Payments());
                total_cheaqueamout.setText(model.getTotal_Cheque_payments_amount());
                total_cheaque_cash_amount.setText(model.getTotal_Cheque_and_cheque_payments_amount());
                dep_conn_dconn.setText(model.getNumber_of_disconnection_invoices());
                teenant_Change.setText(model.getNumber_of_Connections());
                total_dep_inv_amount.setText(model.getTotal_Deposite_Invoices_amount());
                total_con_dconn_amount.setText(model.getTotal_Disconnection_Invoices_amount());
            }
            mainLayout.setVisibility(View.VISIBLE);
        }
    }
}

