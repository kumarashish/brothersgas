package payment;

import android.app.Activity;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.view.View;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.brothersgas.R;

import butterknife.BindView;
import butterknife.ButterKnife;
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

    @Override
    protected void onCreate( Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.payment_receipt_print_email);
        ButterKnife.bind(this);
        back.setOnClickListener(this);
        print_email.setOnClickListener(this);
        setValue();
    }
public void setValue()
{
   if(isPaymentTakenByCheaque)
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
                Utils.showAlertNormal(PaymentReceipt_Print_Email.this,"Receipt send to registered email Id");
                break;
        }

    }

    @Override
    protected void onDestroy() {
        isPaymentTakenByCheaque=false;
        super.onDestroy();
    }
}
