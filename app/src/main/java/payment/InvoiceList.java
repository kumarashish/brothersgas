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
import model.ContractModel;
import model.InvoiceModel;
import utils.Utils;

/**
 * Created by ashish.kumar on 07-02-2019.
 */

public class InvoiceList   extends Activity implements View.OnClickListener , InvoiceListItemClickListner {
    AppController controller;
    WebServiceAcess webServiceAcess;
    ArrayList<ContractModel> unblockedlist=new ArrayList<>();
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
    ContractModel model;
    @BindView(R.id.tot_amount)
    TextView totalAmount;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_invoice_list);
        controller = (AppController) getApplicationContext();
        webServiceAcess = new WebServiceAcess();
        ButterKnife.bind(this);
        back.setOnClickListener(this);
        if(Utils.isNetworkAvailable(InvoiceList.this))
        {   header.setVisibility(View.GONE);
            progressBar.setVisibility(View.VISIBLE);
            new GetData().execute();
        }

    }

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
    public void onClick(InvoiceModel model) {
        PaymentReceipt.invoiceNumber=model.getSales_Invoice_Number();
        PaymentReceipt.amount=model.getOutstanding_amount()+" "+model.getOutstanding_amount_currency();
        startActivity(new Intent(InvoiceList.this,PaymentReceipt.class));


    }

    /*-------------------------------------------------------------------getData-------------------------------------------------------*/
    public class GetData extends AsyncTask<String,Void,String> {
        @Override
        protected String doInBackground(String... strings) {
            String result = webServiceAcess.queryRequest(Common.queryAction, Common.ContractList);
            return result;
        }

        @Override
        protected void onPostExecute(String s) {
            Log.e("value", "onPostExecute: ", null);
            if (s.length() > 0) {
                try {
                    JSONObject jsonObject = new JSONObject(s);
                    JSONObject result = jsonObject.getJSONObject("RESULT");
                    JSONArray jsonArray = result.getJSONArray("LIN");
                    for (int i = 0; i < jsonArray.length(); i++) {
                        JSONObject item = jsonArray.getJSONObject(i);
                        ContractModel model = new ContractModel(item.getJSONArray("FLD"));

//                        if ((model.getBlock_unblockflag() != 2)) {
//
//                            if ((model.getDepositInvoice().length() == 0) || (model.getConnection_discconectionInvoice().length() == 0)) {
//
//                            }
//                        }
                        unblockedlist.add(model);
                    }
                    if (unblockedlist.size() > 0) {
                        CustomListAdapter adapter = new CustomListAdapter(InvoiceList.this, R.layout.contract_row, unblockedlist);
                        search.setAdapter(adapter);
                        search.setOnItemClickListener(onItemClickListener);
                        progressBar.setVisibility(View.GONE);
                        header.setVisibility(View.VISIBLE);

                    } else {
                        progressBar.setVisibility(View.GONE);
                        Utils.showAlert(InvoiceList.this, "No data Found");

                    }
                } catch (Exception ex) {
                    ex.fillInStackTrace();
                }
            } else {
                progressBar.setVisibility(View.GONE);
                Toast.makeText(InvoiceList.this, "Data not found", Toast.LENGTH_SHORT).show();
            }


        }
    }

        /*-------------------------------------------------------------------getData-------------------------------------------------------*/
    public class FetchInvoices extends AsyncTask<String,Void,String> {
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
                    JSONObject lin=tab.getJSONObject("LIN");
                    JSONArray jsonArray =  lin.getJSONArray("FLD");

                        InvoiceModel model=new InvoiceModel(jsonArray);
                        if(Double.parseDouble(model.getOutstanding_amount())>0) {
                            pendingInvoice.add(model);
                        }


                    if (pendingInvoice.size() > 0) {
                        hideKeyboard(InvoiceList.this);
                        String[] outAmout=getTotalOutStandingAmount();
                        totalAmount.setText(outAmout[0]+" "+outAmout[1]);
                        listView.setAdapter(new Invoice_ListAdapter(pendingInvoice, InvoiceList.this));
                        footer.setVisibility(View.VISIBLE);

                    }

                } catch (Exception ex) {
                    ex.fillInStackTrace();
                }

            } else {


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
            currency=pendingInvoice.get(i).getOutstanding_amount_currency();
        }
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