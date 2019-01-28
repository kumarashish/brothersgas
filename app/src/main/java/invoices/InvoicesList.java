package invoices;

import android.app.Activity;
import android.os.Bundle;

import com.brothersgas.R;

import common.AppController;
import common.WebServiceAcess;

/**
 * Created by ashish.kumar on 28-01-2019.
 */

public class InvoicesList extends Activity {
    WebServiceAcess webServiceAcess;
    AppController controller;
    String contractId;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_invoice_list);

    }}
