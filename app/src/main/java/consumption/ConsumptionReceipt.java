package consumption;

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
import android.support.annotation.Nullable;
import android.support.annotation.RequiresApi;
import android.support.v4.app.ActivityCompat;
import android.util.Log;
import android.view.View;
import android.widget.AutoCompleteTextView;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.ProgressBar;
import android.widget.TextView;

import com.brothersgas.R;

import org.json.JSONArray;
import org.json.JSONObject;

import butterknife.BindView;
import butterknife.ButterKnife;
import common.AppController;
import common.Common;
import common.Signature;
import common.WebServiceAcess;
import invoices.Print_Email;
import model.ContractDetails;
import model.ContractModel;
import payment.PaymentReceipt;
import utils.Utils;

public class ConsumptionReceipt extends Activity implements View.OnClickListener {
    @BindView(R.id.back_button)
    Button back;

    @BindView(R.id.contractNumber)
    EditText contractNumber;
    @BindView(R.id.date)
    EditText date;
    @BindView(R.id.consumer)
    TextView consumer;
    @BindView(R.id.address)
    EditText realEstateOwnerDescription;
    @BindView(R.id.real_Estate_owner)
    EditText realEstateOwner;

    @BindView(R.id.previousDate)
    EditText previousDate;

    @BindView(R.id.currentReading)
    EditText currentReading;
    public static ContractModel model = null;
    public static ContractDetails detailsModel;
    @BindView(R.id.signature)
    android.widget.TextView signature;
    @BindView(R.id.print_email)
    Button print_email;
    @BindView(R.id.payment)
    Button payment;
    @BindView(R.id.previousReading)
    EditText previousReading;
    @BindView(R.id.sales_InvoiceNumber)
    EditText sales_InvoiceNumber;
    @BindView(R.id.sales_DeliveryNumber)
    EditText sales_DeliveryNumbe;
@BindView(R.id.footer)
    LinearLayout footer;
@BindView(R.id.progressBar2)
    ProgressBar progressbar2;
WebServiceAcess webServiceAcess;

    boolean isSignatureCaptured = false;
    String imagePath = "";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.consumption_receipt);
        ButterKnife.bind(this);
        back.setOnClickListener(this);
        signature.setOnClickListener(this);
        payment.setOnClickListener(this);
        print_email.setOnClickListener(this);
        webServiceAcess=new WebServiceAcess();
        setValue( );
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
            ActivityCompat.requestPermissions(ConsumptionReceipt.this, new String[]{Manifest.permission.READ_EXTERNAL_STORAGE},
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

    public void setValue( )
    {
        consumer.setText(model.getCustomername());
        contractNumber.setText(model.getContract_Meternumber());
        previousDate.setText(detailsModel.getPreviousDate());
        previousReading.setText(detailsModel.getPreviousReading());
        currentReading.setText(detailsModel.getCurrentReading());
        sales_DeliveryNumbe.setText(detailsModel.getSales_DeliveryNumber());
        sales_InvoiceNumber.setText(detailsModel.getSales_InvoiceNumber());
        date.setText(model.getContactcreationdate());
        realEstateOwner.setText(model.getOwner());
        realEstateOwnerDescription.setText(model.getOwnerDesc());

    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.back_button:
                finish();
                break;
            case R.id.print_email:
                if (isSignatureCaptured) {
                   progressbar2.setVisibility(View.VISIBLE);
                   footer.setVisibility(View.GONE);
                   new EmailInvoice().execute();
                } else {
                    Utils.showAlertNormal(ConsumptionReceipt.this, "Please capture signature");
                }
                break;
            case R.id.payment:
                if (isSignatureCaptured) {
                    PaymentReceipt.invoiceNumber=detailsModel.getSales_InvoiceNumber();
                    startActivity(new Intent(ConsumptionReceipt.this, PaymentReceipt.class));

                } else {
                    Utils.showAlertNormal(ConsumptionReceipt.this, "Please capture signature");
                }

                break;
            case R.id.signature:
                startActivityForResult(new Intent(ConsumptionReceipt.this, Signature.class), 2);
                break;
        }
    }

    @RequiresApi(api = Build.VERSION_CODES.JELLY_BEAN)
    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == 2) {
            if (resultCode == RESULT_OK) {
                imagePath = data.getStringExtra("filepath");
                Bitmap bitmap = BitmapFactory.decodeFile(imagePath);
                isSignatureCaptured = true;
                signature.setBackground(new BitmapDrawable(getResources(), bitmap));
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
            String inVoiceNumber=detailsModel.getSales_InvoiceNumber();
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
                    { print_email.setVisibility(View.GONE);
                        Utils.showAlertNormal(ConsumptionReceipt.this,message);
                    }else{
                        Utils.showAlertNormal(ConsumptionReceipt.this,message);
                    }


                } catch (Exception ex) {
                    ex.fillInStackTrace();
                }
                progressbar2.setVisibility(View.GONE);
                footer.setVisibility(View.VISIBLE);
            } else {
                progressbar2.setVisibility(View.GONE);
                footer.setVisibility(View.VISIBLE);
            }

        }
    }
    /*-------------------------------------------------------------------upload signature-------------------------------------------------------*/
    public class UploadSignature extends AsyncTask<String, Void, String> {
        ProgressDialog pd1;
        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            pd1=new ProgressDialog(ConsumptionReceipt.this);
            pd1.setMessage("Uploading signature....");
            pd1.setCancelable(false);
            pd1.show();
        }

        @Override
        protected String doInBackground(String... strings) {
            String result = webServiceAcess.runRequest(Common.runAction,Common.UploadSignature, new String[]{detailsModel.getSales_InvoiceNumber(),model.getCustomercode(),model.getCustomername(),Utils.getBase64(imagePath)});
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
                    Utils.showAlertNormal(ConsumptionReceipt.this,message);
                } catch (Exception ex) {
                    ex.fillInStackTrace();
                }
            }
            pd1.cancel();

        }
    }
}
