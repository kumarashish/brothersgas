package com.brothersgas;

import android.app.Activity;
import android.app.DatePickerDialog;
import android.app.Dialog;
import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.ListView;
import android.widget.ProgressBar;
import android.widget.Spinner;
import android.widget.Toast;

import org.json.JSONArray;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.Calendar;

import adapter.InvoiceListItemAdapter;
import butterknife.BindView;
import butterknife.ButterKnife;
import common.AppController;
import common.Common;
import common.NumberToWords;
import common.WebServiceAcess;
import consumption.ConsumptionPreview;
import interfaces.InvoiceItemsForPrint;
import invoices.Connection_Disconnection_Preview;
import invoices.Consumption_DeliveryNote_Preview;
import invoices.DepositInvoicePreview;
import model.InvoiceForPrintModel;
import payment.PaymentReceiptPreview;
import utils.Utils;


/**
 * Created by ashish.kumar on 04-04-2019.
 */

public class Print extends Activity implements View.OnClickListener, InvoiceItemsForPrint {
    private Calendar calendar;
    NumberToWords numToWords;

    private int year, month, day;
@BindView(R.id.invoice_type)
    Spinner invoice_type;
    @BindView(R.id.search)
    Button search;
    @BindView(R.id.date)
    Button date;
    int selectedType=1;
@BindView(R.id.progressBar)
    ProgressBar progress;
@BindView(R.id.listView)
    ListView listView;
WebServiceAcess webServiceAcess;
AppController controller;

ArrayList<InvoiceForPrintModel>listItems=new ArrayList<>();
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.print);
        ButterKnife.bind(this);
        controller=(AppController)getApplicationContext();
        date.setOnClickListener(this);
        search.setOnClickListener(this);
        calendar = Calendar.getInstance();
        webServiceAcess=new WebServiceAcess();
        year = calendar.get(Calendar.YEAR);
        month = calendar.get(Calendar.MONTH);
        day = calendar.get(Calendar.DAY_OF_MONTH);
        invoice_type.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                selectedType=position+1;
                Toast.makeText(Print.this,"Please click on search button for getting data",Toast.LENGTH_SHORT).show();
                listView.setVisibility(View.GONE);
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {
            }
        });
    }

    @Override
    public void onClick(View v) {
        switch (v.getId())
        {

            case R.id.search:
                if(date.getText().length()>0)
                {
                  if(Utils.isNetworkAvailable(Print.this))
                  { progress.setVisibility(View.VISIBLE);
                  listView.setVisibility(View.GONE);
                  switch (selectedType) {
                      case 1:
                      case 2:
                      case 3:
                      case 4:
                      new GetData().execute(new String[]{Common.InvoiceListForPrint, Integer.toString(selectedType)});
                      break;
                      case 5:
                          new GetData().execute(new String[]{Common.PaymentListForPrint,"1"});
                          break;
                  }
                  }else {
                      Utils.showAlertNormal(Print.this,"Internet not available...");
                  }
                }else{
                    Utils.showAlertNormal(Print.this,"Please select date");
                }
                break;
            case R.id.date:
                showDialog(999);
                break;
        }

    }


    @Override
    protected Dialog onCreateDialog(int id) {
        // TODO Auto-generated method stub

        DatePickerDialog dialog = new DatePickerDialog(this, myDateListener, year, month, day);

        dialog.getDatePicker().setMaxDate(Calendar.getInstance().getTimeInMillis());
        return dialog;

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
        String dayValue = Integer.toString(day);
        String monthValue = Integer.toString(month);
        if (month < 10) {
            monthValue = "0" + month;
        }
        if (day < 10) {
            dayValue = "0" + day;
        }

                if (Utils.getMinDate(new StringBuilder().append(dayValue).append("/")
                        .append(monthValue).append("/").append(year).toString()) <= Calendar.getInstance().getTimeInMillis()) {
                    date.setText(new StringBuilder().append(dayValue).append("/")
                            .append(monthValue).append("/").append(year));
                    listView.setVisibility(View.GONE);
                } else {
                    Utils.showAlertNormal(Print.this, "You cannot select future date");
                }



        }

    @Override
    public void onClick(final InvoiceForPrintModel model) {
        runOnUiThread(new Runnable() {
            @Override
            public void run() {
                Intent in=null;
                switch (selectedType)
                {
                    case 1:
                        in=new Intent(Print.this, DepositInvoicePreview.class);
                        DepositInvoicePreview.invoice=model.getInvoiceNumber();
                        startActivity(in);
                        break;
                    case 2:
                        in=new Intent(Print.this, Connection_Disconnection_Preview.class);
                        Connection_Disconnection_Preview.invoice=model.getInvoiceNumber();
                        startActivity(in);
                        break;
                    case 3:
                        in=new Intent(Print.this, ConsumptionPreview.class);
                        ConsumptionPreview.invoice=model.getInvoiceNumber();
                        startActivity(in);
                        break;
                    case 4:
                        in=new Intent(Print.this, Consumption_DeliveryNote_Preview.class);
                        Consumption_DeliveryNote_Preview.creditInvoiceNumber=model.getInvoiceNumber();
                        Consumption_DeliveryNote_Preview.invoice="";
                        startActivity(in);
                        break;
                    case 5:
                        in=new Intent(Print.this, PaymentReceiptPreview.class);
                        PaymentReceiptPreview.invoice=model.getInvoiceNumber();
                        startActivity(in);

                        break;
                }
            }
        });


    }

    /*-------------------------------------------------------------------getData-------------------------------------------------------*/
    public class GetData extends AsyncTask<String,Void,String> {

        @Override
        protected void onPreExecute() {

            super.onPreExecute();
        }

        @Override
        protected String doInBackground(String... strings) {
            String result=webServiceAcess.runRequest(Common.runAction,strings[0],  new String[]{controller.getManager().getUserName(),strings[1],Utils.getFormatted(date.getText().toString())});
            return  result;
        }

        @Override
        protected void onPostExecute(String s) {
            Log.e("value", "onPostExecute: ", null);
            if(s.length()>0)
            {listItems.clear();
                try{
                    JSONObject jsonObject=new JSONObject(s);
                    JSONObject result=jsonObject.getJSONObject("RESULT");
                    JSONObject tab=result.getJSONObject("TAB");
                    JSONArray jsonArray=result.getJSONArray("GRP");
                    JSONObject statusFlag=jsonArray.getJSONObject(1);
                    if(statusFlag.getJSONArray("FLD").getJSONObject(0).getInt("content")==2)
                    {
                        Object obj=tab.get("LIN");
                        if(obj instanceof JSONArray) {
                            JSONArray lin = (JSONArray)obj;
                            for(int i=0;i<lin.length();i++)
                            {
                                JSONObject objj=lin.getJSONObject(i);
                                listItems.add(new InvoiceForPrintModel(objj.getJSONArray("FLD")));
                            }
                        }else {
                            JSONObject lin=(JSONObject)obj;
                            listItems.add(new InvoiceForPrintModel(lin.getJSONArray("FLD")));
                        }


                    }
                    if(listItems.size()>0)
                    {
                        listView.setAdapter(new InvoiceListItemAdapter(listItems,Print.this));
                        listView.setVisibility(View.VISIBLE);
                    }


                }catch (Exception ex)
                {
                    ex.fillInStackTrace();
                    Utils.showAlertNormal(Print.this,"No Invoice found");
                }
            }else{
                Utils.showAlertNormal(Print.this,Common.message);
            }
            progress.setVisibility(View.GONE);
        }
    }

}
