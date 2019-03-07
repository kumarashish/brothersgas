package payment;

import android.app.Activity;
import android.app.ProgressDialog;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.util.Log;
import android.view.View;
import android.widget.Button;
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
import common.WebServiceAcess;
import consumption.ConsumptionReceipt;
import invoices.Print_Email;
import model.PaymentReceiptModel;
import utils.Utils;

public class PaymentReceipt_Print_Email extends Activity implements View.OnClickListener {
    public static PaymentReceiptModel model;
    @BindView(R.id.back_button)
    Button back;
   @BindView(R.id.payment_number)
   TextView paymentNumber;
    @BindView(R.id.Site)
    TextView site;
    @BindView(R.id.customer)
    TextView customer;
    @BindView(R.id.Control_account_type)
            TextView Control_account_type;
    @BindView(R.id.Account)
    TextView Account;
    @BindView(R.id.Accounting_date)
            TextView Accounting_date;
    @BindView(R.id.Bank)
    TextView Bank;
    @BindView(R.id.Currency)
            TextView Currency;
    @BindView(R.id.BP_Amount)
    TextView BP_Amount;
    @BindView(R.id.Check_number)
            TextView Check_number;
    @BindView(R.id.Address)
    TextView Address;
    @BindView(R.id.Bank_Amount)
    TextView bankamount;
    @BindView(R.id.print_email)
    Button print_email;

    @BindView(R.id.chequeView)
    LinearLayout cheaqueView;
    @BindView(R.id.amount)
    TextView amount;
    public static boolean isPaymentTakenByCheaque=false;
    @BindView(R.id.progressBar2)
    ProgressBar progressBar;
    @BindView(R.id.footer)
    LinearLayout footer;

    WebServiceAcess webServiceAcess;

    @Override
    protected void onCreate( Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.payment_receipt_print_email);
        ButterKnife.bind(this);
        back.setOnClickListener(this);
        print_email.setOnClickListener(this);
        webServiceAcess = new WebServiceAcess();

        setValue();
    }
public void setValue()
{
   if(isPaymentTakenByCheaque==false)
   {
       cheaqueView.setVisibility(View.GONE);
       amount.setText("Cash Amount");
   }
    paymentNumber.setText(model.getPayment_Number());
    site.setText(model.getSiteDetails());
    customer.setText(model.getCustomerVal());
    Control_account_type.setText(model.getControl_account_type());
    Account.setText(model.getAccount());
    Accounting_date.setText(Utils.getDate(model.getAccounting_date()));
    Bank.setText(model.getBank());
    Currency.setText(model.getCurrencyal());
    BP_Amount.setText(model.getBP_Amount());
    Check_number.setText(model.getCheck_number());
    Address.setText(model.getAddress());
   bankamount.setText(model.getBank_Amount());
}

    @Override
    public void onClick(View v) {
        switch (v.getId())
        {
            case R.id.back_button:
                finish();
                break;
            case R.id.print_email:
                progressBar.setVisibility(View.VISIBLE);
                footer.setVisibility(View.GONE);
                new EmailInvoice().execute();
                break;
        }

    }

    @Override
    protected void onDestroy() {
        isPaymentTakenByCheaque=false;
        super.onDestroy();
    }
    /*-------------------------------------------------------------------block-------------------------------------------------------*/
    public class EmailInvoice extends AsyncTask<String, Void, String> {

        @Override
        protected String doInBackground(String... strings) {


            String result = webServiceAcess.runRequest(Common.runAction,Common.Print_Email, new String[]{model.getPayment_Number(),"5"});
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
                        Utils.showAlertNormal(PaymentReceipt_Print_Email.this,message);
                    }else{
                        Utils.showAlertNormal(PaymentReceipt_Print_Email.this,message);
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
//    /*-------------------------------------------------------------------upload signature-------------------------------------------------------*/
//    public class UploadSignature extends AsyncTask<String, Void, String> {
//        ProgressDialog pd1;
//        @Override
//        protected void onPreExecute() {
//            super.onPreExecute();
//            pd1=new ProgressDialog(PaymentReceipt_Print_Email.this);
//            pd1.setMessage("Uploading signature....");
//            pd1.setCancelable(false);
//            pd1.show();
//        }
//
//        @Override
//        protected String doInBackground(String... strings) {
//            String result = webServiceAcess.runRequest(Common.runAction,Common.UploadSignature, new String[]{model.getPayment_Number(),model.getCustomercode(),model.getCustomername(),Utils.getBase64(imagePath)});
//            return result;
//        }
//
//        @Override
//        protected void onPostExecute(String s) {
//            Log.e("value", "onPostExecute: ", null);
//            if (s.length() > 0) {
//                try {
//                    JSONObject jsonObject = new JSONObject(s);
//                    JSONObject result = jsonObject.getJSONObject("RESULT");
//                    JSONArray jsonArray = result.getJSONArray("GRP");
//                    JSONObject item = jsonArray.getJSONObject(1);
//                    JSONObject Fld = item.getJSONObject("FLD");
//                    String message =Fld.isNull("content")?"No Message From API": Fld.getString("content");
//                    Utils.showAlertNormal(PaymentReceipt_Print_Email.this,message);
//                } catch (Exception ex) {
//                    ex.fillInStackTrace();
//                }
//            }
//            pd1.cancel();
//
//        }
//    }
}
