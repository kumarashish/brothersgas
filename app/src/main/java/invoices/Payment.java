package invoices;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.Toast;

import com.brothersgas.R;

import butterknife.BindView;
import butterknife.ButterKnife;
import common.AppController;
import common.WebServiceAcess;

/**
 * Created by ashish.kumar on 29-01-2019.
 */

public class Payment  extends Activity implements View.OnClickListener {
    WebServiceAcess webServiceAcess;
    AppController controller;
    String contractId;
    @BindView(R.id.back_button)
    Button back;
    @BindView(R.id.submit)
    Button submit;


    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.payment_popup);
        ButterKnife.bind(this);
        back.setOnClickListener(this);
        submit.setOnClickListener(this);

    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.back_button:
                finish();
                break;
            case R.id.submit:
                Toast.makeText(Payment.this, "Payment updated sucessfully.", Toast.LENGTH_SHORT).show();
                submit.setVisibility(View.GONE);
                break;

        }
    }
}
