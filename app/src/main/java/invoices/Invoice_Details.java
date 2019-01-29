package invoices;

import android.app.Activity;
import android.os.Bundle;

import com.brothersgas.R;

import common.AppController;
import common.WebServiceAcess;

public class Invoice_Details extends Activity {
    WebServiceAcess webServiceAcess;
    AppController controller;
    String contractId;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_invoice_list);

    }}