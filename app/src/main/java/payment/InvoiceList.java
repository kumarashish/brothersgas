package payment;

import android.app.Activity;
import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.util.Log;
import android.view.KeyEvent;
import android.view.View;
import android.view.inputmethod.InputMethodManager;
import android.widget.AdapterView;
import android.widget.AutoCompleteTextView;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.ProgressBar;
import android.widget.RelativeLayout;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import com.brothersgas.R;

import org.json.JSONArray;
import org.json.JSONObject;

import java.util.ArrayList;

import adapter.ContractListAdapter;
import adapter.CustomListAdapter;
import adapter.Invoice_ListAdapter;
import butterknife.BindView;
import butterknife.ButterKnife;
import common.AppController;
import common.Common;
import common.WebServiceAcess;
import consumption.Consumption;
import interfaces.InvoiceListItemClickListner;
import interfaces.ListItemClickListner;
import invoices.Block_Cancel;
import invoices.Connection_Disconnection_Invoice;
import invoices.DepositInvoicePreview;
import model.ContractModel;
import model.InvoiceModel;
import utils.Utils;

/**
 * Created by ashish.kumar on 07-02-2019.
 */

public class InvoiceList   extends Activity implements View.OnClickListener , InvoiceListItemClickListner {
    AppController controller;
    WebServiceAcess webServiceAcess;
    //ArrayList<ContractModel> unblockedlist=new ArrayList<>();
    ArrayList<InvoiceModel> pendingInvoice=new ArrayList<>();
    @BindView(R.id.progressBar)
    ProgressBar progressBar;


@BindView(R.id.search)
AutoCompleteTextView search;
    @BindView(R.id.back_button)
    Button back;
    @BindView(R.id.footer)
    RelativeLayout footer;
    @BindView(R.id.listView)
    ListView listView;

    @BindView(R.id.header)
    RelativeLayout header;
    public static ContractModel model;
    @BindView(R.id.tot_amount)
    TextView totalAmount;
@BindView(R.id.pay_now)
Button payNow;
    String owner;
    String project;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_invoice_list);
        controller = (AppController) getApplicationContext();
        owner=getIntent().getStringExtra("owner");
        project=getIntent().getStringExtra("project");
        webServiceAcess = new WebServiceAcess();
        ButterKnife.bind(this);
        back.setOnClickListener(this);
        payNow.setOnClickListener(this);
        if(Utils.isNetworkAvailable(InvoiceList.this))
        {   header.setVisibility(View.GONE);
            progressBar.setVisibility(View.VISIBLE);
            new FetchInvoices().execute(new String[]{model.getCustomercode()});
        }

    }

    @Override
    public void onClick(View v) {
        switch (v.getId())
        {
            case R.id.back_button:
                finish();
                break;
            case R.id.pay_now:
                PayAll.customerNumberValue=model.getCustomercode();
                String []value=getTotalOutStandingAmount();
                PayAll.amount=value[0];
                PayAll.unit=value[1];
                startActivityForResult(new Intent(InvoiceList.this,PayAll.class),2);
                break;

        }
    }

    @Override
    public void onClick(InvoiceModel model) {
        PaymentReceipt.invoiceNumber=model.getSales_Invoice_Number();
        PaymentReceipt.amount=model.getOutstanding_amount();
        PaymentReceipt.unit=model.getOutstanding_amount_currency();
        startActivityForResult(new Intent(InvoiceList.this,PaymentReceipt.class),2);


    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if((requestCode==2)) {
            progressBar.setVisibility(View.VISIBLE);
            footer.setVisibility(View.GONE);
            new FetchInvoices().execute(new String[]{model.getCustomercode()});
        }

    }

    /*-------------------------------------------------------------------getData-------------------------------------------------------*/
//    public class GetData extends AsyncTask<String,Void,String> {
//        @Override
//        protected String doInBackground(String... strings) {
//           String result = webServiceAcess.runRequest(Common.runAction, Common.PaymentList,new String[]{"1",owner,project});
//           // String result = webServiceAcess.runRequest(Common.runAction, Common.PaymentList,new String[]{"1","UO00082","000014"});
//            return result;
//        }
//
//        @Override
//        protected void onPostExecute(String s) {
//            Log.e("value", "onPostExecute: ", null);
//            if (s.length() > 0) {
//                try {
//                    JSONObject jsonObject = new JSONObject(s);
//                    JSONObject result = jsonObject.getJSONObject("RESULT");
//                    JSONObject jsonObject1=result.getJSONObject("TAB");
//                    JSONArray jsonArray = jsonObject1.getJSONArray("LIN");
//                    for (int i = 0; i < jsonArray.length(); i++) {
//                        JSONObject item = jsonArray.getJSONObject(i);
//                        ContractModel model = new ContractModel(item.getJSONArray("FLD"));
//                        unblockedlist.add(model);
//                    }
//                    if (unblockedlist.size() > 0) {
//                        CustomListAdapter adapter = new CustomListAdapter(InvoiceList.this, R.layout.contract_row, unblockedlist);
//                        search.setAdapter(adapter);
//                        search.setOnItemClickListener(onItemClickListener);
//                        progressBar.setVisibility(View.GONE);
//                        header.setVisibility(View.VISIBLE);
//
//                    } else {
//                        progressBar.setVisibility(View.GONE);
//                        Utils.showAlert(InvoiceList.this, "No data Found");
//
//                    }
//                } catch (Exception ex) {
//                    progressBar.setVisibility(View.GONE);
//                    Utils.showAlert(InvoiceList.this, "Error occured during data parsing");
//                    ex.fillInStackTrace();
//                }
//            } else {
//                Utils.showAlertNormal(InvoiceList.this,Common.message);
//                progressBar.setVisibility(View.GONE);
//
//            }
//
//
//        }
//    }

        /*-------------------------------------------------------------------getData-------------------------------------------------------*/
    public class FetchInvoices extends AsyncTask<String,Void,String> {

            @Override
            protected void onPreExecute() {
                totalAmount.setText("");
                pendingInvoice.clear();
                super.onPreExecute();
            }

            @Override
        protected String doInBackground(String... strings) {

            String result = webServiceAcess.runRequest(Common.runAction, Common.InvoiceSearch,new String[]{strings[0]});
            return result;
        }

        @Override
        protected void onPostExecute(String s) {
            Log.e("value", "onPostExecute: ", null);
            if (s.length() > 0) {
                try {
                    JSONObject jsonObject = new JSONObject(s);
                    JSONObject result = jsonObject.getJSONObject("RESULT");
                    JSONObject tab=result.getJSONObject("TAB");
                   Object obj=tab.get("LIN");

                    if (obj instanceof JSONArray) {
                        JSONArray lin =(JSONArray) obj;
                        for (int i = 0; i < lin.length(); i++) {
                            JSONObject jsonObject1 = lin.getJSONObject(i);
                            JSONArray jsonArray = jsonObject1.getJSONArray("FLD");
                            InvoiceModel model = new InvoiceModel(jsonArray);
                            if(Double.parseDouble(model.getOutstanding_amount())>0) {
                                pendingInvoice.add(model);
                            }
                        }

                    }
                    else if (obj instanceof JSONObject){
                        JSONObject  lin =(JSONObject) obj;
                        JSONArray jsonArray = lin.getJSONArray("FLD");
                        InvoiceModel model = new InvoiceModel(jsonArray);
                        if(Double.parseDouble(model.getOutstanding_amount())>0) {
                            pendingInvoice.add(model);
                        }

                    }

                    if (pendingInvoice.size() > 0) {
                        hideKeyboard(InvoiceList.this);
                        String[] outAmout=getTotalOutStandingAmount();
                        totalAmount.setText(outAmout[0]+" "+outAmout[1]);
                        listView.setAdapter(new Invoice_ListAdapter(pendingInvoice, InvoiceList.this));
                        footer.setVisibility(View.VISIBLE);

                    }else{
                        Utils.showAlertNormal(InvoiceList.this,"No Invoice Found for payment");
                    }

                } catch (Exception ex) {
                    ex.fillInStackTrace();
                    Utils.showAlertNormal(InvoiceList.this, "No Invoice Found for payment");
                }

            } else {
                Utils.showAlertNormal(InvoiceList.this,Common.message);

            }

            progressBar.setVisibility(View.GONE);
        }
    }

    public String[] getTotalOutStandingAmount()
    {Double val=0.0;
    String currency="";
        for(int i=0;i<pendingInvoice.size();i++)
        {
            val+= Double.parseDouble(pendingInvoice.get(i).getOutstanding_amount());

        }
        currency=pendingInvoice.get(0).getOutstanding_amount_currency();
        return new String[]{val.toString(),currency};
    }

    private AdapterView.OnItemClickListener onItemClickListener =
            new AdapterView.OnItemClickListener() {
                @Override
                public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {

                    Object item = adapterView.getItemAtPosition(i);
                    if (item instanceof ContractModel) {
                        model=(ContractModel) item;
                        search.setText(model.getCustomername());
                        progressBar.setVisibility(View.VISIBLE);
                        new FetchInvoices().execute(new String[]{model.getCustomercode()});

                    }

                }
            };
    public static void hideKeyboard(Activity activity) {
        InputMethodManager imm = (InputMethodManager) activity.getSystemService(Activity.INPUT_METHOD_SERVICE);
        //Find the currently focused view, so we can grab the correct window token from it.
        View view = activity.getCurrentFocus();
        //If no view currently has focus, create a new one, just so we can grab a window token from it
        if (view == null) {
            view = new View(activity);
        }
        imm.hideSoftInputFromWindow(view.getWindowToken(), 0);
    }

}