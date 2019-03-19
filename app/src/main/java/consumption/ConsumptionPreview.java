package consumption;

import android.app.Activity;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.AsyncTask;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ProgressBar;
import android.widget.ScrollView;
import android.widget.TextView;

import com.brothersgas.R;

import org.json.JSONArray;
import org.json.JSONObject;

import butterknife.BindView;
import butterknife.ButterKnife;
import common.AppController;
import common.Common;
import common.NumberToWords;
import common.WebServiceAcess;
import model.Connection_Disconnection_Invoice_Preview_Model;
import model.ContractDetails;
import model.ContractModel;
import payment.PaymentReceipt;
import utils.Utils;

/**
 * Created by ashish.kumar on 19-03-2019.
 */

public class ConsumptionPreview extends Activity implements View.OnClickListener {
    @BindView(R.id.invoice_number)
    android.widget.TextView invoice_number;
    @BindView(R.id.project_name)
    android.widget.TextView project_name;
    @BindView(R.id.teenant_name)
    android.widget.TextView teenant_name;
    @BindView(R.id.customer_name)
    android.widget.TextView customer_name;
    @BindView(R.id.customer_trn)
    android.widget.TextView customer_trn;
    @BindView(R.id.customer_address)
    android.widget.TextView customer_address;
    @BindView(R.id.suplier_name)
    android.widget.TextView suplier_name;
    @BindView(R.id.date_time)
    android.widget.TextView date_time;
    @BindView(R.id.name_id)
    android.widget.TextView name_id;

    @BindView(R.id.previousUnits)
    android.widget.TextView previousUnits;
    @BindView(R.id.currentUnits)
    android.widget.TextView currentUnits;
    @BindView(R.id.consumedUnits)
    android.widget.TextView consumedUnits;
    @BindView(R.id.pressure_Factor)
    android.widget.TextView pressure_Factor;
    @BindView(R.id.actual_consumed_units)
    android.widget.TextView actual_consumed_units;
    @BindView(R.id.total_excluding_vat)
    android.widget.TextView total_excluding_vat;
    @BindView(R.id.vat_value)
    android.widget.TextView vat_value;
    @BindView(R.id.total_includingvat)
    android.widget.TextView total_includingvat;
    @BindView(R.id.total_inwords)
    android.widget.TextView total_inwords;
    @BindView(R.id.supplier_trn)
    android.widget.TextView supplier_trn;
    @BindView(R.id.registered_supplier_address)
    android.widget.TextView registered_supplier_address;
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
    WebServiceAcess webServiceAcess;
    @BindView(R.id. signature)
    ImageView signature;
    @BindView(R.id.print_email)
    Button print_email;
    @BindView(R.id.payment)
    Button payment;
    @BindView(R.id.footer)
    LinearLayout footer;
    @BindView(R.id.progressBar2)
    ProgressBar progressbar2;
    int sendAttempt=0;
    public static ContractModel model = null;
    public static ContractDetails detailsModel;
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.consumption_preview);
        ButterKnife.bind(this);
        webServiceAcess = new WebServiceAcess();
           back_button.setOnClickListener(this);
        numToWords = new NumberToWords();
        back_button.setOnClickListener(this);
        if (Utils.isNetworkAvailable(ConsumptionPreview.this)) {
            progress.setVisibility(View.VISIBLE);
            contentView.setVisibility(View.GONE);
            new GetInvoiceDetails().execute();
        }
        payment.setOnClickListener(this);
        print_email.setOnClickListener(this);
    }





    @Override
    public void onClick(View v) {
        switch (v.getId())
        {
            case R.id. back_button:
                finish();
                break;
            case R.id.print_email:
                progressbar2.setVisibility(View.VISIBLE);
                footer.setVisibility(View.GONE);
                new EmailInvoice().execute();
                break;
            case R.id. payment:
                PaymentReceipt.invoiceNumber=invoice;
                startActivity(new Intent(ConsumptionPreview.this, PaymentReceipt.class));
                finish();
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

        previousUnits.setText(model.getPreviousMeterreadingValue());
        currentUnits.setText(model.getPresentMeterreadingValue());
        consumedUnits.setText(model.getUnitsConsumed());
        pressure_Factor.setText(model.getPressureFactorValue());
        actual_consumed_units.setText(model.getActualUnitConsumedValue());
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
            String result = webServiceAcess.runRequest(Common.runAction,Common.Print_Email, new String[]{invoice,"3"});
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
                    { sendAttempt=sendAttempt+1;
                        if(sendAttempt==1)
                        {
                            print_email.setText("Resend");
                        }else {
                            print_email.setVisibility(View.GONE);
                        }
                        Utils.showAlertNormal(ConsumptionPreview.this,message);
                    }else{
                        Utils.showAlertNormal(ConsumptionPreview.this,message);
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