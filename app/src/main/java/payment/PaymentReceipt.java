package payment;

import android.app.Activity;
import android.content.DialogInterface;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v7.app.AlertDialog;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.ProgressBar;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.RelativeLayout;
import android.widget.Toast;

import com.brothersgas.R;

import org.json.JSONArray;
import org.json.JSONObject;

import java.util.ArrayList;

import butterknife.BindView;
import butterknife.ButterKnife;
import common.AppController;
import common.Common;
import common.TextView;
import common.WebServiceAcess;
import consumption.Consumption;
import invoices.Block_Cancel_Details;
import model.ContractModel;
import model.PaymentReceiptModel;
import utils.Utils;

/**
 * Created by ashish.kumar on 07-02-2019.
 */

public class PaymentReceipt  extends Activity implements View.OnClickListener  {
    AppController controller;
    WebServiceAcess webServiceAcess;


    @BindView(R.id.progressBar2)
    ProgressBar progressBar;


    @BindView(R.id.back_button)
    Button back;
    @BindView(R.id.submit)
    Button submit;
    @BindView(R.id.mode)
    RadioGroup paymentMode;
    @BindView(R.id.cash)
    RadioButton cash;
    @BindView(R.id.cheque)
    RadioButton cheque;
    @BindView(R.id.cheaqueDateView)
    LinearLayout cheaqueDateView;
    @BindView(R.id.cheaqueNumberView)
    LinearLayout cheaqueNumberView;
    @BindView(R.id.bankView)
    LinearLayout bankView;
    @BindView(R.id.amount_value)
    EditText amountValue;
    @BindView(R.id.amount_unit)
    EditText amountUnit;
    @BindView(R.id.invoice_number)
    android.widget.TextView invoice_numberValue;
    @BindView(R.id.cheaquenumber)
            EditText cheaqueNumber;
    @BindView(R.id.date)
    EditText chequeDate;
            @BindView(R.id.bank)
            EditText bank;


    public static String invoiceNumber="";
    public static String amount="";
    public static String unit="";
    boolean paymentModeCheque=false;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.payment_screen);
        controller = (AppController) getApplicationContext();
        webServiceAcess = new WebServiceAcess();
        ButterKnife.bind(this);
        submit.setOnClickListener(this);
        back.setOnClickListener(this);
        paymentMode.check(cheque.getId());
        invoice_numberValue.setText(invoiceNumber);
        amountValue.setText(amount);
        amountUnit.setText(unit);
        paymentMode.setOnCheckedChangeListener(new RadioGroup.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(RadioGroup group, int checkedId) {
                if(checkedId==cheque.getId())
                {
                    cheaqueNumberView.setVisibility(View.VISIBLE);
                    cheaqueDateView.setVisibility(View.VISIBLE);
                    bankView.setVisibility(View.VISIBLE);
                    paymentModeCheque=true;
                }else {
                    cheaqueNumberView.setVisibility(View.GONE);
                    cheaqueDateView.setVisibility(View.GONE);
                    bankView.setVisibility(View.GONE);
                    paymentModeCheque=false;
                }
            }
        });
    }
    public void showAlert(String message)
    {
        AlertDialog.Builder builder1 = new AlertDialog.Builder(PaymentReceipt.this);
        builder1.setMessage(message);
        builder1.setCancelable(true);
        builder1.setNeutralButton(
                "Ok",
                new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int id) {
                        dialog.cancel();

                    }
                });

        AlertDialog alert11 = builder1.create();
        alert11.show();
    }
    @Override
    public void onClick(View v) {
        switch (v.getId())
        {
            case R.id.back_button:
                finish();
                break;
            case R.id.submit:
                if(paymentMode.getCheckedRadioButtonId()==cheque.getId())
                {
                    if((cheaqueNumber.getText().length()>0)&&(cheaqueNumber.getText().length()>0)&&(cheaqueNumber.getText().length()>0))
                    {   progressBar.setVisibility(View.VISIBLE);
                        submit.setVisibility(View.GONE);
                        new CreatePayment().execute();
                    }else{
                        if(cheaqueNumber.getText().length()==0)
                        {
                            showAlert("Please enter cheque number");
                        }
                        else if(chequeDate.getText().length()==0)
                        {
                            showAlert("Please enter cheque date");
                        }
                        else if(bank.getText().length()==0)
                        {
                            showAlert("Please enter bank details");
                        }
                    }
                }else{
                    progressBar.setVisibility(View.VISIBLE);
                    submit.setVisibility(View.GONE);
                    new CreatePayment().execute();

                }
                break;
        }
    }

    /*-------------------------------------------------------------------block-------------------------------------------------------*/
    public class CreatePayment extends AsyncTask<String, Void, String> {

        @Override
        protected String doInBackground(String... strings) {
            String cheaueNumberString="";
            String mode="1";
            if(paymentMode.getCheckedRadioButtonId()==cheque.getId())
            {
                cheaueNumberString=cheaqueNumber.getText().toString();
                mode="2";
            }
            String result = webServiceAcess.runRequest(Common.runAction,Common.CreatePayment, new String[]{invoiceNumber,cheaueNumberString,mode,amountValue.getText().toString()});
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
                    JSONArray fld=item.getJSONArray("FLD");
                    PaymentReceiptModel model=new PaymentReceiptModel(fld);
                    PaymentReceipt_Print_Email.model=model;
                    PaymentReceipt_Print_Email.isPaymentTakenByCheaque=paymentModeCheque;
                    Utils.showAlertNavigateToPrintEmail(PaymentReceipt.this,"Paymet created Sucessfully.Payment Number "+model.getPayment_Number(),PaymentReceipt_Print_Email.class);

                } catch (Exception ex) {
                    ex.fillInStackTrace();
                }
            } else {
                submit.setVisibility(View.VISIBLE);

            }
            progressBar.setVisibility(View.GONE);

        }
    }
}



