package payment;

import android.app.Activity;
import android.content.DialogInterface;
import android.os.Bundle;
import android.support.v7.app.AlertDialog;
import android.view.View;
import android.widget.Button;
import android.widget.ProgressBar;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.RelativeLayout;

import com.brothersgas.R;

import java.util.ArrayList;

import butterknife.BindView;
import butterknife.ButterKnife;
import common.AppController;
import common.WebServiceAcess;
import consumption.Consumption;
import model.ContractModel;

/**
 * Created by ashish.kumar on 07-02-2019.
 */

public class PaymentReceipt  extends Activity implements View.OnClickListener  {
    AppController controller;
    WebServiceAcess webServiceAcess;


    @BindView(R.id.progressBar)
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
                        finish();
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
                showAlert("Payment Updated Sucessfully.");
                break;
        }
    }
}
