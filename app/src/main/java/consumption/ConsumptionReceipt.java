package consumption;

import android.app.Activity;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.drawable.BitmapDrawable;
import android.os.Build;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.annotation.RequiresApi;
import android.view.View;
import android.widget.AutoCompleteTextView;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.TextView;

import com.brothersgas.R;

import butterknife.BindView;
import butterknife.ButterKnife;
import common.AppController;
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
        setValue( );
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
                    Utils.showAlertNormal(ConsumptionReceipt.this, "Invoice send to registered email Id");
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
            }
        }
    }
}
