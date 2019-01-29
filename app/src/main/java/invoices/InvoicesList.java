package invoices;

import android.app.Activity;
import android.content.DialogInterface;
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

/**
 * Created by ashish.kumar on 28-01-2019.
 */

public class InvoicesList extends Activity implements View.OnClickListener{
    WebServiceAcess webServiceAcess;
    AppController controller;
    String contractId;
    @BindView(R.id.back_button)
    Button back;
    @BindView(R.id.radio_gp)
    RadioGroup group;
    @BindView(R.id.c_name)
    RadioButton cname;
    @BindView(R.id.c_number)
    RadioButton cnumber;
    @BindView(R.id.edittext)
    EditText searchEdt;
    @BindView(R.id.next)
    View next1;
    @BindView(R.id.next2)
    View next2;
    @BindView(R.id.next3)
    View next3;
    @BindView(R.id.next4)
    View next4;
    @BindView(R.id.next5)
    View next5;




    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_invoice_list);
        ButterKnife.bind(this);
        back.setOnClickListener(this);
        next1.setOnClickListener(this);
        next2.setOnClickListener(this);
        next3.setOnClickListener(this);
        next4.setOnClickListener(this);
        next5.setOnClickListener(this);
        group.check(cname.getId());
        group.setOnCheckedChangeListener(new RadioGroup.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(RadioGroup group, int checkedId) {
             if(checkedId==cname.getId())
             {
                 searchEdt.setHint("Serach by Customer name");
             }else {
                 searchEdt.setHint("Serach by Consumer number");
             }
            }
        });
    }

    @Override
    public void onClick(View v) {
        switch (v.getId())
        {
            case R.id.back_button:
                finish();
                break;
            case R.id.next:
            case R.id.next2:
            case R.id.next3:
                case R.id.next4:
                    case R.id.next5:
                        startActivity(new Intent(InvoicesList.this,Invoice_Details.class));
                        break;


        }
    }
}
