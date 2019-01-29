package invoices;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.RadioButton;
import android.widget.RadioGroup;

import com.brothersgas.R;

import butterknife.BindView;
import butterknife.ButterKnife;
import common.AppController;
import common.WebServiceAcess;

public class Invoice_Details extends Activity implements View.OnClickListener{
    WebServiceAcess webServiceAcess;
    AppController controller;
    String contractId;
    @BindView(R.id.back_button)
    Button back;
    @BindView(R.id.generate)
    Button generate;




    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.invoice_details);
        ButterKnife.bind(this);
        back.setOnClickListener(this);
        generate.setOnClickListener(this);
    }

    @Override
    public void onClick(View v) {
        switch (v.getId())
        {
            case R.id.back_button:
                finish();
                break;
            case R.id.generate:
                startActivity(new Intent(Invoice_Details.this,GenerateInvoice.class));
                break;
        }

    }
}