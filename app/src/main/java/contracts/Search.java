package contracts;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.AutoCompleteTextView;
import android.widget.Button;
import android.widget.ListView;
import android.widget.ProgressBar;
import android.widget.RelativeLayout;
import android.widget.Spinner;

import com.brothersgas.R;

import java.util.ArrayList;

import adapter.CustomListAdapter;
import butterknife.BindView;
import butterknife.ButterKnife;
import common.AppController;
import common.WebServiceAcess;
import interfaces.ListItemClickListner;
import model.ContractModel;

public class Search extends Activity implements View.OnClickListener , ListItemClickListner {
    AppController controller;
    WebServiceAcess webServiceAcess;

    @BindView(R.id.progressBar)
    ProgressBar progressBar;
@BindView(R.id.auto)
    AutoCompleteTextView autoCompleteTextView;

    @BindView(R.id.back_button)
    Button back;
   public static ArrayList<ContractModel>contractList;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_search);
        controller = (AppController) getApplicationContext();
        webServiceAcess = new WebServiceAcess();
        ButterKnife.bind(this);
        CustomListAdapter adapter = new CustomListAdapter(this, R.layout.contract_row, contractList);
        back.setOnClickListener(this);
        autoCompleteTextView.setAdapter(adapter);
        autoCompleteTextView.setOnItemClickListener(onItemClickListener);
    }
    private AdapterView.OnItemClickListener onItemClickListener =
            new AdapterView.OnItemClickListener(){
                @Override
                public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
                  ContractModel model=  (ContractModel) adapterView.getItemAtPosition(i);
                    Intent in=new Intent(Search.this,ContractDetails.class);
                    in.putExtra("Data",model.getContract_Meternumber());
                    startActivity(in);
                    finish();
                }
            };




    @Override
    public void onClick(View v) {
        switch (v.getId())
        {
            case R.id.back_button:
                finish();
                break;
        }

    }

    @Override
    public void onClick(ContractModel model) {

    }

    @Override
    public void onCancelClick(ContractModel model) {

    }

    @Override
    public void onBlockClick(ContractModel model) {

    }
}