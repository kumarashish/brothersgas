package com.brothersgas;

import android.app.Activity;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.AutoCompleteTextView;
import android.widget.LinearLayout;
import android.widget.ProgressBar;
import android.widget.Spinner;

import org.json.JSONArray;
import org.json.JSONObject;

import java.util.ArrayList;

import butterknife.BindView;
import butterknife.ButterKnife;
import common.AppController;
import common.Common;
import common.WebServiceAcess;
import model.ContractModel;
import model.ProjectModel;
import utils.Utils;


/**
 * Created by ashish.kumar on 03-04-2019.
 */

public class ProjectSearch  extends Activity{
    @BindView(R.id.progressBar)
    ProgressBar progressBar;
    @BindView(R.id.ownerList)
    AutoCompleteTextView ownerList;

    @BindView(R.id.projectView)
    LinearLayout projectView;
    @BindView(R.id.projectList)
 AutoCompleteTextView projectList;
    WebServiceAcess webServiceAcess;
    AppController controller;
    ArrayList<ProjectModel>projectModelList=new ArrayList<>();
    ArrayList<String>projectNameList=new ArrayList<>();
    @Override
    protected void onCreate( Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_project_list);
        controller=(AppController)getApplicationContext();
        ButterKnife.bind(this);
        webServiceAcess=new WebServiceAcess();
        ArrayAdapter<String> adapter = new ArrayAdapter<String>
                (this,android.R.layout.select_dialog_item,controller.getOwnerNameList());
        ownerList.setThreshold(2);
        ownerList.setAdapter(adapter);
        ownerList.setOnItemClickListener(onItemClickListener);


    }
    private AdapterView.OnItemClickListener onItemClickListener =
            new AdapterView.OnItemClickListener() {
                @Override
                public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
                  String name=  (String) adapterView.getItemAtPosition(i);
                    if(Utils.isNetworkAvailable(ProjectSearch.this))
                    {   progressBar.setVisibility(View.VISIBLE);
                        new GetData().execute(controller.getOwnerList().get(controller.getOwnerNameList().indexOf(name)).getProjectCode());
                    }
                }
            };




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
                                    (ProjectSearch.this, android.R.layout.select_dialog_item,projectNameList);
                            projectList.setThreshold(2);
                            projectList.setAdapter(adapter);
                           // projectList.setOnItemClickListener(onItemClickListener);
                            projectView.setVisibility(View.VISIBLE);
                        }


                    } else {
                        Utils.showAlertNormal(ProjectSearch.this, "Some Errror occured while synchronizing,Please Retry.");
                    }


                } catch (Exception ex) {
                    ex.fillInStackTrace();
                }
            }
            progressBar.setVisibility(View.GONE);
        }
    }
}


