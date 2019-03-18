package activatecontract;

import android.Manifest;
import android.app.Activity;
import android.app.ProgressDialog;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.drawable.BitmapDrawable;
import android.os.AsyncTask;
import android.os.Build;
import android.os.Bundle;
import android.support.annotation.RequiresApi;
import android.support.v4.app.ActivityCompat;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.ProgressBar;
import android.widget.TextView;

import com.brothersgas.R;

import org.json.JSONArray;
import org.json.JSONObject;

import butterknife.BindView;
import butterknife.ButterKnife;
import common.Common;
import common.Signature;
import common.WebServiceAcess;
import payment.PaymentReceipt;
import utils.Utils;

public class Print_Email  extends Activity implements View.OnClickListener {
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
    @BindView(R.id.print_email)
    Button print_email;
    @BindView(R.id.payment)
    Button payment;
    @BindView(R.id.heading)
    android.widget.TextView heading;
    @BindView(R.id.signature)
    android.widget.TextView signature;
    public static model.ContractDetails model;
    public static String  calledMethod;
    WebServiceAcess webServiceAcess;
    Boolean isSignatureCaptured=false;
    String imagePath=null;
    @BindView(R.id.progressBar2)
    ProgressBar progressBar;
    @BindView(R.id.admin_invoice_view)
    LinearLayout admin_invoice_view;
    @BindView(R.id.admin_Invoice)
    TextView admin_Invoice;
    @BindView(R.id.current_meter_reading_header)
    LinearLayout current_meter_reading_header;
    @BindView(R.id.current_meter_reading)
    android.widget.TextView currentMeterReading;
int sendAttempt=0;
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.print_email_pdf);
        webServiceAcess = new WebServiceAcess();
        ButterKnife.bind(this);
        back.setOnClickListener(this);
        signature.setOnClickListener(this);
        payment.setOnClickListener(this);
        print_email.setOnClickListener(this);
        admin_invoice_view.setVisibility(View.VISIBLE);
        setValue();
        if(checkPermissionForReadExtertalStorage()==false)
        {
            try {
                requestPermissionForReadExtertalStorage();
            } catch (Exception e) {
                e.printStackTrace();
            }
        }


    }
    public void requestPermissionForReadExtertalStorage() throws Exception {
        try {
            ActivityCompat.requestPermissions(Print_Email.this, new String[]{Manifest.permission.READ_EXTERNAL_STORAGE},
                    22);
        } catch (Exception e) {
            e.printStackTrace();
            throw e;
        }
    }
    public boolean checkPermissionForReadExtertalStorage() {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
            int result = checkSelfPermission(Manifest.permission.READ_EXTERNAL_STORAGE);
            return result == PackageManager.PERMISSION_GRANTED;
        }
        return false;
    }
    public void setValue() {
        site.setText(model.getSite_value());
        customer_id.setText(model.getCustomer_value());
        contract_date.setText(model.getContract_Date());
        address.setText(model.getCustomer_Address());
        item.setText(model.getProduct_Item());
        depositamount.setText(model.getDeposit_Amount() + " " + model.getCurrency());
        Connection_charges.setText(model.getConnection_charges() + " " + model.getCurrency());
        Disconnection_Charges.setText(model.getDisconnection_Charges() + " " + model.getCurrency());
        Pressure_Factor.setText(model.getPressure_Factor());
        if((model.getPreviousReading().length()>0)&&(!model.getPreviousReading().equalsIgnoreCase("0")))
        {
            Initial_meter_reading.setText(model.getPreviousReading());
        }else {
            Initial_meter_reading.setText(model.getInitial_meter_reading() + " " + model.getUnits());
        }

        Deposit_Invoice.setText(model.getDeposit_Invoice());
        Connection_Disconnection_Invoice.setText(model.getConnection_Disconnection_Invoice());
        admin_Invoice.setText(model.getConsumptionInvoice());
        footer.setVisibility(View.VISIBLE);
        if(model.getCurrentMeterReading().length()>0)
        {
            current_meter_reading_header.setVisibility(View.VISIBLE);
            currentMeterReading.setText(model.getCurrentMeterReading());
        }
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.back_button:
                finish();
                break;
            case R.id.print_email:
                if(isSignatureCaptured)
                {
                    progressBar.setVisibility(View.VISIBLE);
                    footer.setVisibility(View.GONE);
                    new EmailInvoice().execute();

                }else{
                    Utils.showAlertNormal(Print_Email.this,"Please capture signature");
                }
                break;
            case R.id.payment:
                if(isSignatureCaptured)
                { PaymentReceipt.invoiceNumber=model.getConsumptionInvoice();
                    startActivity(new Intent(Print_Email.this, PaymentReceipt.class));
                    finish();
                }else{
                    Utils.showAlertNormal(Print_Email.this,"Please capture signature");
                }

                break;
            case R.id.signature:
                startActivityForResult(new Intent(Print_Email.this, Signature.class), 2);
                break;
        }
    }


    @RequiresApi(api = Build.VERSION_CODES.JELLY_BEAN)
    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == 2) {
            if (resultCode == RESULT_OK) {
                imagePath= data.getStringExtra("filepath");
                Bitmap bitmap = BitmapFactory.decodeFile(imagePath);
                isSignatureCaptured=true;
                signature.setBackground( new BitmapDrawable(getResources(),bitmap));
                signature.setText("");
                new UploadSignature().execute();
            }
        }
    }
    /*-------------------------------------------------------------------block-------------------------------------------------------*/
    public class EmailInvoice extends AsyncTask<String, Void, String> {

        @Override
        protected String doInBackground(String... strings) {
            String val="1";

            String inVoiceNumber=model.getAdminInvoiceCharges();

            String result = webServiceAcess.runRequest(Common.runAction,Common.Print_Email, new String[]{inVoiceNumber,val});
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
                    JSONObject status=Fld.getJSONObject(0);
                    String message =messageJsonObject.isNull("content")?"No Message From API": messageJsonObject.getString("content");
                    int statusValue=status.isNull("content")?1: status.getInt("content");
                    if(statusValue==2)
                    {
                        Utils.showAlertNormal(Print_Email.this,message);
                    }else{
                        Utils.showAlertNormal(Print_Email.this,message);
                    }

                    progressBar.setVisibility(View.GONE);
                    footer.setVisibility(View.VISIBLE);
                } catch (Exception ex) {
                    ex.fillInStackTrace();
                }
            } else {
            }
        }
    }
    /*-------------------------------------------------------------------upload signature-------------------------------------------------------*/
    public class UploadSignature extends AsyncTask<String, Void, String> {
        ProgressDialog pd1;
        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            pd1=new ProgressDialog(Print_Email.this);
            pd1.setMessage("Uploading signature....");
            pd1.setCancelable(false);
            pd1.show();
        }
        @Override
        protected String doInBackground(String... strings) {
            String result = webServiceAcess.runRequest(Common.runAction,Common.UploadSignature,  new String[]{model.getConsumptionInvoice(),model.getCustomer_value(),model.getCustomerName(),Utils.getBase64(imagePath)});
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
                    Utils.showAlertNormal(Print_Email.this,message);
                } catch (Exception ex) {
                    ex.fillInStackTrace();
                }
            }
            pd1.cancel();

        }
    }
}