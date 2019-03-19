package invoices;

import android.app.Activity;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.drawable.BitmapDrawable;
import android.os.AsyncTask;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.*;

import com.brothersgas.R;

import org.json.JSONArray;
import org.json.JSONObject;

import butterknife.BindView;
import butterknife.ButterKnife;
import common.AppController;
import common.Common;
import common.NumberToWords;
import common.WebServiceAcess;
import consumption.ConsumptionPreview;
import model.Connection_Disconnection_Invoice_Preview_Model;
import payment.PaymentReceipt;
import utils.Utils;


public class Connection_Disconnection_Preview extends Activity implements View.OnClickListener {
    @BindView(R.id. invoice_number)
    android.widget.TextView invoice_number;
    @BindView(R.id. project_name)
    android.widget.TextView project_name;
    @BindView(R.id. teenant_name)
    android.widget.TextView teenant_name;
    @BindView(R.id. customer_name)
    android.widget.TextView customer_name;
    @BindView(R.id. customer_trn)
    android.widget.TextView customer_trn;
    @BindView(R.id. customer_address)
    android.widget.TextView customer_address;
    @BindView(R.id. suplier_name)
    android.widget.TextView suplier_name;
    @BindView(R.id.date_time)
    android.widget.TextView date_time;
    @BindView(R.id.name_id)
    android.widget.TextView name_id;


    @BindView(R.id. total_excluding_vat)
    android.widget.TextView total_excluding_vat;
    @BindView(R.id. vat_value)
    android.widget.TextView vat_value;
    @BindView(R.id. total_includingvat)
    android.widget.TextView total_includingvat;
    @BindView(R.id. total_inwords)
    android.widget.TextView total_inwords;
    @BindView(R.id. supplier_trn)
    android.widget.TextView supplier_trn;
    @BindView(R.id. registered_supplier_address)
    android.widget.TextView registered_supplier_address;
    @BindView(R.id. signature)
    ImageView signature;
    AppController controller;
    WebServiceAcess webServiceAcess;
    public static String invoice="";
   //public static String invoice="CDC-U109-19000053";
    NumberToWords numToWords;
    @BindView(R.id.progressBar)
    ProgressBar progress;

    @BindView(R.id.contentView)
    ScrollView contentView;
    @BindView(R.id.content)
    LinearLayout content;
    @BindView(R.id.back_button)
    Button back_button;
  public static String imagePath="";

    @BindView(R.id.progressBar2)
    ProgressBar progressbar2;
    public static model.ContractDetails model;
    public static String  calledMethod="";
    int sendAttempt=0;
    @BindView(R.id.footer)
    LinearLayout footer;
    @BindView(R.id.print_email)
    Button print_email;
    @BindView(R.id.payment)
    Button payment;
    @Override
    protected void onCreate( Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.preview_screen);
        ButterKnife.bind(this);
        webServiceAcess = new WebServiceAcess();
        controller=(AppController)getApplicationContext();
        numToWords=new NumberToWords();
        back_button.setOnClickListener(this);
        payment.setOnClickListener(this);
        print_email.setOnClickListener(this);
        if(Utils.isNetworkAvailable(Connection_Disconnection_Preview.this)) {
            progress.setVisibility(View.VISIBLE);
            contentView.setVisibility(View.GONE);
            new GetInvoiceDetails().execute();
        }

    }

    @Override
    public void onClick(View v) {
        switch (v.getId())
        {
            case R.id. back_button:
                finish();
                break;
            case R.id.payment:
                PaymentReceipt.invoiceNumber=invoice;
                startActivity(new Intent(this, PaymentReceipt.class));
                finish();
                break;
            case R.id.print_email:
                progressbar2.setVisibility(View.VISIBLE);
                footer.setVisibility(View.GONE);
               new EmailInvoice().execute();
                break;
        }
    }

    /*-------------------------------------------------------------------block-------------------------------------------------------*/
    public class GetInvoiceDetails extends AsyncTask<String, Void, String> {

        @Override
        protected String doInBackground(String... strings) {

            String result = webServiceAcess.runRequest(Common.runAction,Common.Connection_DisconnectionInvoiceDetails, new String[]{invoice});
            return result;
        }

        @Override
        protected void onPostExecute(String s) {
            Log.e("value", "onPostExecute: ", null);
            if (s.length() > 0) {
                try {
                    Connection_Disconnection_Invoice_Preview_Model model=new  Connection_Disconnection_Invoice_Preview_Model(s);
                    if(model.getStatus()==2)
                    {
                        setValues(model);
                    }
                } catch (Exception ex) {
                    ex.fillInStackTrace();
                }
            } else {

            }

        }
    }

    public void setValues(Connection_Disconnection_Invoice_Preview_Model model) {
        invoice_number.setText(model.getInvoice_NumberValue());
        project_name.setText(model.getProjectnameValue());
        teenant_name.setText(model.getTenantNameValue());
        customer_name.setText(model.getCustomerNameValue());
        customer_trn.setText(model.getCustomerTRNNumberValue());
        customer_address.setText(model.getCustomerAddressValue());
        suplier_name.setText(model.getSuppliername());
        supplier_trn.setText(model.getSupplierTRN());
        registered_supplier_address.setText(model.getRegisteredAddress());
        date_time.setText(Utils.getNewDate(model.getDateValue()) +" : "+model.getTime());
        name_id.setText(model.getUserNameValue()+" & "+model.getUserIDValue());

        for (int i = 0; i < model.getDetails_list().size(); i++) {
            View view = getLayoutInflater().inflate(R.layout.content_row, null);
            TextView item_name = (TextView) view.findViewById(R.id.item_name);
            TextView unit = (TextView) view.findViewById(R.id.unit);
            TextView quantity = (TextView) view.findViewById(R.id.quantity);
            TextView unit_price = (TextView) view.findViewById(R.id.unit_price);
            TextView total_price = (TextView) view.findViewById(R.id.total_price);
            TextView total_vat = (TextView) view.findViewById(R.id.total_vat);
            TextView vat_percentage = (TextView) view.findViewById(R.id.vat_percentage);
            item_name.setText(model.getDetails_list().get(i).getItemNameValue());
            unit.setText(model.getDetails_list().get(i).getUnitofMeasureValue());
            quantity.setText(model.getDetails_list().get(i).getQuantityValue());
            unit_price.setText(model.getDetails_list().get(i).getUnitPriceValue());
            total_price.setText(model.getDetails_list().get(i).getTotalpriceValue());
            vat_percentage.setText("VAT @ " + model.getDetails_list().get(i).getVat_percentageValue() + "%");
            total_vat.setText(model.getDetails_list().get(i).getVatAmountValue());
            content.addView(view);
        }


        total_excluding_vat.setText(model.getTotalExcludingTaxValue());
        vat_value.setText(model.getTotalVatValue());
        total_includingvat.setText(model.getTotalIncludingTaxValue());
        progress.setVisibility(View.GONE);
        contentView.setVisibility(View.VISIBLE);
        total_inwords.setText("AED "+ numToWords.convertNumberToWords((int)Math.round(Double.parseDouble(model.getTotalIncludingTaxValue())))+" Only /-");

        Bitmap bitmap = BitmapFactory.decodeFile(imagePath);
        signature.setImageBitmap(bitmap);

    }
    /*-------------------------------------------------------------------block-------------------------------------------------------*/
    public class EmailInvoice extends AsyncTask<String, Void, String> {

        @Override
        protected String doInBackground(String... strings) {



            String result = webServiceAcess.runRequest(Common.runAction,Common.Print_Email, new String[]{invoice,"2"});
            return result;
        }

        @Override
        protected void onPostExecute(String s) {
            Log.e("value", "onPostExecute: ", null);
            if (s.length() > 0) {
                try {
                    JSONObject jsonObject = new JSONObject(s);
                    JSONObject result = jsonObject.getJSONObject("RESULT");
                    JSONArray jsonArray = result.getJSONArray("GRP");
                    JSONObject item = jsonArray.getJSONObject(1);
                    JSONArray Fld = item.getJSONArray("FLD");
                    JSONObject messageJsonObject=Fld.getJSONObject(1);
                    JSONObject status=Fld.getJSONObject(0);
                    String message =messageJsonObject.isNull("content")?"No Message From API": messageJsonObject.getString("content");
                    int statusValue=status.isNull("content")?1: status.getInt("content");
                    if(statusValue==2)
                    {
                        sendAttempt=sendAttempt+1;
                        if(sendAttempt==1)
                        {
                            print_email.setText("Resend");
                        }else {
                            print_email.setVisibility(View.GONE);
                        }
                        Utils.showAlertNormal(Connection_Disconnection_Preview.this,message);

                    }else{
                        Utils.showAlertNormal(Connection_Disconnection_Preview.this,message);
                    }


                } catch (Exception ex) {
                    ex.fillInStackTrace();
                }
                progressbar2.setVisibility(View.GONE);
                footer.setVisibility(View.VISIBLE);
            } else {
                progressbar2.setVisibility(View.GONE);
                footer.setVisibility(View.VISIBLE);
            }

        }
    }


}
