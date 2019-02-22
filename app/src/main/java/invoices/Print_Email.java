package invoices;

import android.app.Activity;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.drawable.BitmapDrawable;
import android.media.MediaScannerConnection;
import android.os.Build;
import android.os.Bundle;
import android.os.Environment;
import android.support.annotation.RequiresApi;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;

import com.brothersgas.R;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.Calendar;

import butterknife.BindView;
import butterknife.ButterKnife;
import common.Signature;
import common.TextView;
import common.WebServiceAcess;
import payment.PaymentReceipt;
import utils.Utils;

/**
 * Created by ashish.kumar on 22-02-2019.
 */

public class Print_Email extends Activity implements View.OnClickListener {
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
    WebServiceAcess webServiceAcess;
    Boolean isSignatureCaptured=false;
    String imagePath=null;

    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.print_email_pdf);
        webServiceAcess = new WebServiceAcess();
        ButterKnife.bind(this);
        back.setOnClickListener(this);
        signature.setOnClickListener(this);
        payment.setOnClickListener(this);
        print_email.setOnClickListener(this);
        setValue();
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
        Initial_meter_reading.setText(model.getInitial_meter_reading());
        Deposit_Invoice.setText(model.getDeposit_Invoice());
        Connection_Disconnection_Invoice.setText(model.getConnection_Disconnection_Invoice());
        footer.setVisibility(View.VISIBLE);
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
                    Utils.showAlertNormal(Print_Email.this,"Invoice send to registered email Id");
                }else{
                    Utils.showAlertNormal(Print_Email.this,"Please capture signature");
                }
                break;
            case R.id.payment:
                if(isSignatureCaptured)
                {
                    startActivity(new Intent(Print_Email.this, PaymentReceipt.class));
                  //  finish();
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
            }
        }
    }
}
