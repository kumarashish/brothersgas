package com.brothersgas;

import android.app.Activity;
import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.MotionEvent;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.AutoCompleteTextView;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.ProgressBar;
import android.widget.Spinner;

import org.json.JSONArray;
import org.json.JSONObject;

import java.util.ArrayList;

import activatecontract.ContractListForActivation;
import activatecontract.Dashboard2;
import adapter.ContractListAdapter;
import adapter.CustomListAdapter;
import adapter.ProjectSearchAdapter;
import butterknife.BindView;
import butterknife.ButterKnife;
import common.AppController;
import common.Common;
import common.WebServiceAcess;
import consumption.Consumption;
import invoices.Block_Cancel;
import invoices.Block_Cancel_Details;
import invoices.Connection_Disconnection_Invoice;
import invoices.Connection_Disconnection_Invoice_details;
import model.ContractModel;
import model.OwnerModel;
import model.ProjectModel;
import payment.InvoiceList;
import utils.Utils;


/**
 * Created by ashish.kumar on 03-04-2019.
 */

public class ProjectSearch  extends Activity implements View.OnClickListener{
    @BindView(R.id.progressBar)
    ProgressBar progressBar;
    @BindView(R.id.ownerList)
    AutoCompleteTextView projectList;

    @BindView(R.id.projectView)
    LinearLayout contractListView;
    @BindView(R.id.projectList)
 AutoCompleteTextView contractList;
    @BindView(R.id.submit)
    Button submit;
    WebServiceAcess webServiceAcess;
    AppController controller;


ArrayList< ContractModel>contractModelArrayList=new ArrayList<>();
    int requestedScreen=0;
    @BindView(R.id.back_button)
    Button back;
    OwnerModel model=null;
    ContractModel contractmodel=null;
    @Override
    protected void onCreate( Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_project_list);
        requestedScreen=getIntent().getIntExtra("RequestedScreen",0);
        controller=(AppController)getApplicationContext();
        ButterKnife.bind(this);
        back.setOnClickListener(this);
        webServiceAcess=new WebServiceAcess();
        projectList.setAdapter(new ProjectSearchAdapter(ProjectSearch.this,R.layout.project_search_row,controller.getOwnerList()));
        projectList.setThreshold(0);
        projectList.setOnItemClickListener(onItemClickListener);
        submit.setOnClickListener(this);
        projectList.setOnTouchListener(new View.OnTouchListener(){
            @Override
            public boolean onTouch(View v, MotionEvent event){
               projectList.showDropDown();
                return false;
            }
        });
        contractList.setOnTouchListener(new View.OnTouchListener(){
            @Override
            public boolean onTouch(View v, MotionEvent event){
                if(contractModelArrayList.size()>0) {
                    contractList.showDropDown();
                }
                return false;
            }
        });

        if(controller.getOwnerNameList().size()==0)
        {
            Utils.showAlert(ProjectSearch.this,"Owner List missing,Please go back and Sync Data..");
        }
        projectList.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {

            }
            @Override
            public void afterTextChanged(Editable s) {

            }
        });
    }
      private AdapterView.OnItemClickListener onItemClickListener =
            new AdapterView.OnItemClickListener() {
                @Override
                public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {


                    model=  (OwnerModel)adapterView.getItemAtPosition(i);
                    projectList.setText(model.getProjectName()+" - "+model.getProjectCode());
                    if(Utils.isNetworkAvailable(ProjectSearch.this))
                    {   progressBar.setVisibility(View.VISIBLE);
                        handleRequest();
                    }else{
                        Utils.showAlertNormal(ProjectSearch.this,"Internet Unavailable..");
                    }

                }
            };

       private AdapterView.OnItemClickListener onContractClickListner=
            new AdapterView.OnItemClickListener() {
                @Override
                public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
                    contractmodel=  (ContractModel) adapterView.getItemAtPosition(i);

                     contractList.setText(contractmodel.getCustomername());

                }
            };

    @Override
    public void onClick(View v) {
        switch (v.getId())
        {
            case R.id.back_button:
                finish();
                break;
            case R.id.submit:
                if((requestedScreen==1)||(requestedScreen==4))
                {
                    if ((model != null)){
                        navigatetoClass();
                    }else{
                        Utils.showAlertNormal(ProjectSearch.this, "Please select Project");
                    }
                }else {
                    if ((model != null) && (contractModelArrayList.size() > 0) && (contractmodel != null)) {
                        navigatetoClass();
                    } else {
                        if (model == null) {
                            Utils.showAlertNormal(ProjectSearch.this, "Please select Project");
                        } else if (contractModelArrayList.size() == 0) {
                            Utils.showAlertNormal(ProjectSearch.this, "No Contract available for selected Project");
                        } else {
                            Utils.showAlertNormal(ProjectSearch.this, "Please select Contract");
                        }
                    }
                }
                break;
        }

    }


    /*-------------------------------------------------------------------getData-------------------------------------------------------*/
    public class GetData extends AsyncTask<String, Void, String> {
        @Override
        protected void onPreExecute() {
            super.onPreExecute();

        }

        @Override
        protected String doInBackground(String... strings) {

            String result = webServiceAcess.runRequest(Common.runAction, strings[1],new String[]{strings[0],model.getOwnerCode(),model.getProjectCode()});
            return result;
        }

        @Override
        protected void onPostExecute(String s) {
            contractModelArrayList.clear();
            Log.e("value ", "onPostExecute: ", null);
            if (s.length() > 0) {
                    try {
                        JSONObject jsonObject = new JSONObject(s);
                        JSONObject result = jsonObject.getJSONObject("RESULT");
                        JSONObject tab=result.getJSONObject("TAB");
                        Object object = tab.get("LIN");
                        if(object instanceof JSONArray) {
                            JSONArray jsonArray = tab.getJSONArray("LIN");
                            for (int i = 0; i < jsonArray.length(); i++) {
                                JSONObject item = jsonArray.getJSONObject(i);
                                ContractModel model = new ContractModel(item.getJSONArray("FLD"));
                                contractModelArrayList.add(model);
                            }
                        }else{
                            JSONObject item = (JSONObject)object;
                            ContractModel model = new ContractModel(item.getJSONArray("FLD"));

                            contractModelArrayList.add(model);

                        }
                        if (contractModelArrayList.size() > 0) {
                            CustomListAdapter adapter = new CustomListAdapter(ProjectSearch.this, R.layout.contract_row, contractModelArrayList);
                            contractList.setAdapter(adapter);
                            contractList.setThreshold(0);
                            contractListView.setVisibility(View.VISIBLE);
                            contractList.setOnItemClickListener( onContractClickListner);
                            if((contractmodel!=null)&&(isSelectedModelPresent()==true))
                            {
                                contractmodel=getUpdatedContractModel ();
                                contractList.setText(contractmodel.getCustomername());
                            }
                        }else{
                            progressBar.setVisibility(View.GONE);
                            Utils.showAlert(ProjectSearch.this,"No record available");
                        }
                    } catch (Exception ex) {
                        ex.fillInStackTrace();
                        Utils.showAlertNormal(ProjectSearch.this,"No record available");
                    }
            }else{
                Utils.showAlertNormal(ProjectSearch.this,Common.message);
            }
            submit.setVisibility(View.VISIBLE);
            progressBar.setVisibility(View.GONE);
        }
    }

    public ContractModel getUpdatedContractModel() {
        for (int i = 0; i < contractModelArrayList.size(); i++) {
            if (contractModelArrayList.get(i).getContract_Meternumber().equalsIgnoreCase(contractmodel.getContract_Meternumber())) {
                return contractModelArrayList.get(i);
            }
        }
        return contractmodel;
    }

    public boolean isSelectedModelPresent()
    {
        for (int i = 0; i < contractModelArrayList.size(); i++) {
            if (contractModelArrayList.get(i).getContract_Meternumber().equalsIgnoreCase(contractmodel.getContract_Meternumber())) {
                return true;
            }
        }
        return false;
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if ((requestCode == 2) && (resultCode == RESULT_OK)) {
            if ((requestedScreen != 1) || (requestedScreen != 4)) {
                contractListView.setVisibility(View.GONE);
                progressBar.setVisibility(View.VISIBLE);
                contractList.setText("");
                submit.setVisibility(View.GONE);
                handleRequest();
            }

        }
    }

    public void handleRequest() {
        Intent in=null;
        switch (requestedScreen) {
            case 1:
                progressBar.setVisibility(View.GONE);
                navigatetoClass();
                break;
            case 2:
                new GetData().execute(new String[]{"2",Common.BlockList});
                break;
            case 3:

                new GetData().execute(new String[]{"3",Common.BlockList});
                break;
            case 4:
                progressBar.setVisibility(View.GONE);
                navigatetoClass();

                break;
            case 5:
                new GetData().execute(new String[]{"1", Common.PaymentList});
                break;


        }
    }


    public void navigatetoClass() {
        Intent in=null;
        switch (requestedScreen) {
            case 1:
                in=new Intent(ProjectSearch.this,Dashboard2.class);
                in.putExtra("owner",model.getOwnerCode());
                in.putExtra("project",model.getProjectCode());
                startActivityForResult(in,2);
                break;
            case 2:
                in=new Intent(ProjectSearch.this, Connection_Disconnection_Invoice_details.class);
                Connection_Disconnection_Invoice_details.contractModel=contractmodel;
                startActivityForResult(in,2);
                break;
            case 3:

                in=new Intent(ProjectSearch.this, Block_Cancel_Details.class);
                Block_Cancel_Details.contractModel=contractmodel;
                startActivityForResult(in,2);

                break;
            case 4:
                in=new Intent(ProjectSearch.this,Consumption.class);
                in.putExtra("owner",model.getOwnerCode());
                in.putExtra("project",model.getProjectCode());
                startActivityForResult(in,2);

                break;
            case 5:
                in=new Intent(ProjectSearch.this,InvoiceList.class);
                InvoiceList.model=contractmodel;
                startActivityForResult(in,2);
                // new GetData().execute(new String[]{});
                break;


        }
    }



}


