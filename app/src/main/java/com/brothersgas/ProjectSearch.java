package com.brothersgas;

import android.app.Activity;
import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
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
import butterknife.BindView;
import butterknife.ButterKnife;
import common.AppController;
import common.Common;
import common.WebServiceAcess;
import consumption.Consumption;
import invoices.Block_Cancel;
import invoices.Connection_Disconnection_Invoice;
import model.ContractModel;
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
    AutoCompleteTextView ownerList;

    @BindView(R.id.projectView)
    LinearLayout projectView;
    @BindView(R.id.projectList)
 AutoCompleteTextView projectList;
    @BindView(R.id.submit)
    Button submit;
    WebServiceAcess webServiceAcess;
    AppController controller;
    ArrayList<ProjectModel>projectModelList=new ArrayList<>();
    ArrayList<String>projectNameList=new ArrayList<>();

    int requestedScreen=0;
    String ownerName="";
    String projectName="";
    @BindView(R.id.back_button)
    Button back;

    @Override
    protected void onCreate( Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_project_list);
        requestedScreen=getIntent().getIntExtra("RequestedScreen",0);
        controller=(AppController)getApplicationContext();
        ButterKnife.bind(this);
        back.setOnClickListener(this);
        webServiceAcess=new WebServiceAcess();
        ArrayAdapter<String> adapter = new ArrayAdapter<String>
                (this,android.R.layout.select_dialog_item,controller.getOwnerNameList());
        ownerList.setThreshold(2);
        ownerList.setAdapter(adapter);
        ownerList.setOnItemClickListener(onItemClickListener);
        submit.setOnClickListener(this);
        if(controller.getOwnerNameList().size()==0)
        {
            Utils.showAlert(ProjectSearch.this,"Owner List missing,Please go back and Sync Data..");
        }
        ownerList.addTextChangedListener(new TextWatcher() {
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
                  ownerName=  (String)adapterView.getItemAtPosition(i);
                  String[]value=ownerName.split(" - ");
                    if(Utils.isNetworkAvailable(ProjectSearch.this))
                    {   progressBar.setVisibility(View.VISIBLE);
                        new GetData().execute(value[0]);
                    }else{
                        Utils.showAlertNormal(ProjectSearch.this,"Internet Unavailable..");
                    }
                }
            };

    private AdapterView.OnItemClickListener onProjectClickListener =
            new AdapterView.OnItemClickListener() {
                @Override
                public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
                    projectName=  (String)adapterView.getItemAtPosition(i);


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
                if((ownerName.length()>0)&&(projectName.length()>0)&&(projectNameList.size()>0))
                {
                    navigatetoClass();
                }else{
                    if(ownerName.length()==0)
                    {
                        Utils.showAlertNormal(ProjectSearch.this,"Please select owner");
                    }else if (projectName.length()==0){
                        Utils.showAlertNormal(ProjectSearch.this,"Please select project");
                    }else{
                        Utils.showAlertNormal(ProjectSearch.this,"No Project Found,Please select other owner");
                    }
                }
                break;
        }

    }


    /*-------------------------------------------------------------------getData-------------------------------------------------------*/
    public class GetData extends AsyncTask<String, Void, String> {
        String calledMethod = "";

        @Override
        protected void onPreExecute() {

            super.onPreExecute();
        }

        @Override
        protected String doInBackground(String... strings) {
            String result = webServiceAcess.runRequest(Common.runAction, Common.ProjectList, new String[]{controller.getManager().getLoggedInUserName(), strings[0]});
            return result;
        }

        @Override
        protected void onPostExecute(String s) {
            Log.e("value", "onPostExecute: ", null);
            if (s.length() > 0) {
                try {
                    projectNameList.clear();
                    projectModelList.clear();
                    JSONObject jsonObject = new JSONObject(s);
                    JSONObject result = jsonObject.getJSONObject("RESULT");
                    JSONObject tab = result.getJSONObject("TAB");
                    JSONArray jsonArray = result.getJSONArray("GRP");
                    JSONObject statusFlag = jsonArray.getJSONObject(1);
                    if (statusFlag.getJSONArray("FLD").getJSONObject(0).getInt("content") == 2) {

                        Object lin = tab.get("LIN");
                        if (lin instanceof JSONArray) {
                            JSONArray data = (JSONArray) lin;
                            for (int i = 0; i < data.length(); i++) {
                                JSONObject jsonObject1 = data.getJSONObject(i);
                                ProjectModel model = new ProjectModel(jsonObject1.getJSONArray("FLD"));
                                projectModelList.add(model);
                                projectNameList.add(model.getProjectCode() + " - " + model.getProjectName());
                            }
                        } else {
                            JSONObject object = (JSONObject) lin;
                            ProjectModel model = new ProjectModel(object.getJSONArray("FLD"));
                            projectModelList.add(model);
                            projectNameList.add(model.getProjectCode() + " - " + model.getProjectName());

                        }
                        if (projectModelList.size() > 0) {
                            ArrayAdapter<String> adapter = new ArrayAdapter<String>
                                    (ProjectSearch.this, android.R.layout.select_dialog_item, projectNameList);
                            projectList.setThreshold(2);
                            projectList.setAdapter(adapter);
                            projectList.setOnItemClickListener(onProjectClickListener);
                            projectView.setVisibility(View.VISIBLE);
                        }


                    } else {
                        Utils.showAlertNormal(ProjectSearch.this, "Project not available.");
                    }


                } catch (Exception ex) {
                    ex.fillInStackTrace();
                    Utils.showAlertNormal(ProjectSearch.this, "Project not available.");
                }
            }else{
                Utils.showAlertNormal(ProjectSearch.this,Common.message);
            }
            progressBar.setVisibility(View.GONE);
        }
    }

    public void navigatetoClass() {
        switch (requestedScreen) {
            case 1:
                Intent in=new Intent(ProjectSearch.this, Dashboard2.class);
                in.putExtra("owner",ownerName.split(" - ")[0]);
                in.putExtra("project",projectName.split(" - ")[0]);
                startActivity(in);
                finish();
                break;
            case 2:
                 in=new Intent(ProjectSearch.this,Connection_Disconnection_Invoice.class);
                in.putExtra("owner",ownerName.split(" - ")[0]);
                in.putExtra("project",projectName.split(" - ")[0]);
                startActivity(in);

                finish();
                break;
            case 3:
               in=new Intent(ProjectSearch.this, Block_Cancel.class);
                in.putExtra("owner",ownerName.split(" - ")[0]);
                in.putExtra("project",projectName.split(" - ")[0]);
                startActivity(in);

                finish();
                break;
            case 4:
                in=new Intent(ProjectSearch.this,Consumption.class);
                in.putExtra("owner",ownerName.split(" - ")[0]);
                in.putExtra("project",projectName.split(" - ")[0]);
                startActivity(in);

                finish();
                break;
            case 5:
                 in=new Intent(ProjectSearch.this,InvoiceList.class);
                in.putExtra("owner",ownerName.split(" - ")[0]);
                in.putExtra("project",projectName.split(" - ")[0]);
                startActivity(in);

                finish();
                break;


        }
    }
}


