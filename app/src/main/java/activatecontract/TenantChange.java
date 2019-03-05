package activatecontract;

import android.app.Activity;
import android.app.DatePickerDialog;
import android.app.Dialog;
import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.ProgressBar;
import android.widget.RelativeLayout;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import com.brothersgas.R;

import org.json.JSONArray;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.Calendar;

import adapter.ContractListAdapter;
import butterknife.BindView;
import butterknife.ButterKnife;
import common.AppController;
import common.Common;
import common.WebServiceAcess;
import contracts.Search;
import interfaces.ListItemClickListner;
import model.ContractModel;
import utils.Utils;

public class TenantChange extends Activity implements View.OnClickListener {
    AppController controller;
    WebServiceAcess webServiceAcess;
    @BindView(R.id.c_number)
    EditText contract_number;
            @BindView(R.id.current_reading)
            EditText current_reading;
            @BindView(R.id.em_id)
            EditText em_id;
            @BindView(R.id.expiry_date)
            Button expiry_date;
            @BindView(R.id.address)
            EditText address;
            @BindView(R.id.emailId)
            EditText emailId;
            @BindView(R.id.contact_num)
            EditText contact_number;
    @BindView(R.id.progressBar2)
    ProgressBar progressBar;
    @BindView(R.id.footer)
    LinearLayout footer;
    @BindView(R.id.submit)
    Button submit;
    @BindView(R.id.back_button)
    Button back;
    private DatePicker datePicker;
    private Calendar calendar;

    private int year, month, day;
public static String contractNumber="";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.teenant_change);
        controller = (AppController) getApplicationContext();
        webServiceAcess = new WebServiceAcess();
        ButterKnife.bind(this);
        submit.setOnClickListener(this);
        back.setOnClickListener(this);
        expiry_date.setOnClickListener(this);
        contract_number.setText(contractNumber);
        calendar = Calendar.getInstance();
        year = calendar.get(Calendar.YEAR);

        month = calendar.get(Calendar.MONTH);
        day = calendar.get(Calendar.DAY_OF_MONTH);

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
       expiry_date.setText(new StringBuilder().append(year).append("")
                .append(monthValue).append("").append(dayValue));
    }
    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.back_button:
                finish();
                break;
            case R.id.submit:
if(isFieldsValidated())
{progressBar.setVisibility(View.VISIBLE);
footer.setVisibility(View.GONE);
    new Update().execute();
}

                break;
            case R.id.expiry_date:
                setDate(v);
                break;

        }
    }
    public boolean isFieldsValidated() {
        if ((current_reading.getText().length() > 0) && (em_id.getText().length() > 0) && (expiry_date.getText().length() > 0) && (address.getText().length() > 0) && (contact_number.getText().length() > 0) && (emailId.getText().length() > 0)) {
        return true;
        }else{
            if(current_reading.getText().length()==0)
            {
                Toast.makeText(TenantChange.this,"Please enter current reading",Toast.LENGTH_SHORT).show();
            }else if(em_id.getText().length()==0)
            {
                Toast.makeText(TenantChange.this,"Please enter emirates Id",Toast.LENGTH_SHORT).show();
            }else if(expiry_date.getText().length()==0)
            {
                Toast.makeText(TenantChange.this,"Please enter expiry date",Toast.LENGTH_SHORT).show();
            }else if(address.getText().length()==0)
            {
                Toast.makeText(TenantChange.this,"Please enter address",Toast.LENGTH_SHORT).show();
            }else if(contact_number.getText().length()==0)
            {
                Toast.makeText(TenantChange.this,"Please enter contact number",Toast.LENGTH_SHORT).show();
            }else if(emailId.getText().length()==0)
            {
                Toast.makeText(TenantChange.this,"Please enter email id",Toast.LENGTH_SHORT).show();
            }
        }
        return false;
    }


    /*-------------------------------------------------------------------block-------------------------------------------------------*/
    public class Update extends AsyncTask<String, Void, String> {
        @Override
        protected String doInBackground(String... strings) {

            String result = webServiceAcess.runRequest(Common.runAction, Common.Tennant_Change, new String[]{contractNumber, current_reading.getText().toString(),em_id.getText().toString(),expiry_date.getText().toString(),address.getText().toString(),contact_number.getText().toString(),emailId.getText().toString()});
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
                    JSONObject messageObject = Fld.getJSONObject(1);
                    String message = messageObject.isNull("content") ? "Message not available" : messageObject.getString("content");
                    if (message.contains("New")) {
                        Utils.showAlertNormal(TenantChange.this, message);
                        submit.setVisibility(View.GONE);
                    } else {
                        Utils.showAlertNormal(TenantChange.this, message);
                        submit.setVisibility(View.VISIBLE);
                    }
                } catch (Exception ex) {
                    ex.fillInStackTrace();
                }
            } else {
                Utils.showAlertNormal(TenantChange.this, "No message received from api");
            }
            progressBar.setVisibility(View.GONE);
            footer.setVisibility(View.VISIBLE);
        }
    }
}