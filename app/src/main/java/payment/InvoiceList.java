package payment;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.ListView;
import android.widget.ProgressBar;
import android.widget.RelativeLayout;
import android.widget.Spinner;
import android.widget.TextView;

import com.brothersgas.R;

import java.util.ArrayList;

import butterknife.BindView;
import butterknife.ButterKnife;
import common.AppController;
import common.WebServiceAcess;
import interfaces.ListItemClickListner;
import model.ContractModel;

/**
 * Created by ashish.kumar on 07-02-2019.
 */

public class InvoiceList   extends Activity implements View.OnClickListener  {
    AppController controller;
    WebServiceAcess webServiceAcess;
    ArrayList<ContractModel> unblockedlist=new ArrayList<>();


    @BindView(R.id.progressBar)
    ProgressBar progressBar;
    @BindView(R.id.contentView)
    RelativeLayout contentView;


    @BindView(R.id.back_button)
    Button back;
    @BindView(R.id.new_invoice)
    Button newInvoice;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_invoice_list);
        controller = (AppController) getApplicationContext();
        webServiceAcess = new WebServiceAcess();
        ButterKnife.bind(this);
        newInvoice.setOnClickListener(this);
        back.setOnClickListener(this);
    }

    @Override
    public void onClick(View v) {
        switch (v.getId())
        {
            case R.id.back_button:
                finish();
                break;
            case R.id.new_invoice:
           startActivity(new Intent(InvoiceList.this,PaymentReceipt.class));
                break;
        }
    }
}