package consumption;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.AutoCompleteTextView;
import android.widget.Button;
import android.widget.ProgressBar;

import com.brothersgas.R;

import java.util.ArrayList;

import adapter.CustomListAdapter;
import butterknife.BindView;
import butterknife.ButterKnife;
import common.AppController;
import common.WebServiceAcess;
import contracts.ContractDetails;
import contracts.Search;
import interfaces.ListItemClickListner;
import invoices.Block_Cancel_Details;
import invoices.Connection_Disconnection_Invoice_details;
import model.ContractModel;

public class Consumption  extends Activity implements View.OnClickListener {
    AppController controller;
    WebServiceAcess webServiceAcess;
    @BindView(R.id.progressBar)
    ProgressBar progressBar;
    @BindView(R.id.back_button)
    Button back;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.consumption);
        controller = (AppController) getApplicationContext();
        webServiceAcess = new WebServiceAcess();
        ButterKnife.bind(this);

    }


    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.back_button:
                finish();
                break;
        }

    }
}