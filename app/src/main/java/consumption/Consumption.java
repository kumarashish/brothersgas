package consumption;

import android.app.Activity;
import android.app.DatePickerDialog;
import android.app.Dialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AlertDialog;
import android.text.format.DateFormat;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.AutoCompleteTextView;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.ProgressBar;
import android.widget.RadioButton;

import com.brothersgas.R;
import com.whygraphics.multilineradiogroup.MultiLineRadioGroup;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;

import adapter.CustomListAdapter;
import butterknife.BindView;
import butterknife.ButterKnife;
import common.AppController;
import common.WebServiceAcess;
import contracts.ContractDetails;
import contracts.Search;
import interfaces.ListItemClickListner;
import invoices.Block_Cancel_Details;
import invoices.Connection_Disconnection_Invoice_details;
import model.ContractModel;

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
    EditText consumer;
    @BindView(R.id.address)
    EditText address;

    @BindView(R.id.meter_Status)
    MultiLineRadioGroup meterstatus;

    @BindView(R.id.normal)
    RadioButton normal;

    @BindView(R.id.meter_prblm)
    RadioButton meterProblem;

    @BindView(R.id.meterId)
    EditText meterId;
    @BindView(R.id.previousDate)
    EditText previousDate;
    @BindView(R.id.currentDate)
    EditText currentDate;
    @BindView(R.id.submit)
    Button submit;
    public static  model.ContractDetails model;
    private DatePicker datePicker;
    private Calendar calendar;
    private int year, month, day;
    @BindView(R.id.issueList)
    LinearLayout issueList;
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
        setData();
        meterstatus.check(normal.getId());
        calendar = Calendar.getInstance();
        year = calendar.get(Calendar.YEAR);
        month = calendar.get(Calendar.MONTH);
        day = calendar.get(Calendar.DAY_OF_MONTH);
        meterstatus.setOnCheckedChangeListener(new MultiLineRadioGroup.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(ViewGroup viewGroup, RadioButton radioButton) {
                if(radioButton.getId()==meterProblem.getId())
                {
                    issueList.setVisibility(View.VISIBLE);
                }else{
                    issueList.setVisibility(View.GONE);
                }
            }
        });


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
        String date=Integer.toString(year);
        if(month<10)
        {
            date+="-"+"0"+month;
        }else{
            date+="-"+month;
        }

        if(day<10)
        {
            date+="-"+"0"+day;
        }else{
            date+="-"+day;
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
                showAlert("Invoice and Delivery Note has been generated");
            case R.id.currentDate:
                showDialog(999);
                break;
        }

    }

    public void setData()
    {if(model!=null) {
        contractNumber.setText(model.getContractNumber());
        consumer.setText(model.getCustomer_value());
        address.setText(model.getCustomer_Address());
        date.setText(model.getContract_Date());
        meterId.setText(model.getContractNumber());
    }
        Date d = new Date();
        CharSequence s  = DateFormat.format("yyyy-MM-dd", d.getTime());
        currentDate.setText(s);

    }
    public void showAlert(String message)
    {
        AlertDialog.Builder builder1 = new AlertDialog.Builder(Consumption.this);
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
}