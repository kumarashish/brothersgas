package invoices;

import android.app.Activity;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.AsyncTask;
import android.os.Bundle;
import android.os.PersistableBundle;
import android.support.annotation.Nullable;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ProgressBar;
import android.widget.ScrollView;
import android.widget.TextView;

import com.brothersgas.R;

import butterknife.BindView;
import butterknife.ButterKnife;
import common.Common;
import common.NumberToWords;
import common.WebServiceAcess;
import consumption.ConsumptionPreview;
import model.Connection_Disconnection_Invoice_Preview_Model;
import model.CreditNoteModel;
import utils.Utils;


/**
 * Created by ashish.kumar on 19-03-2019.
 */

public class Consumption_DeliveryNote_Preview extends Activity implements View.OnClickListener {
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


    @BindView(R.id.invoice_number2)
    android.widget.TextView invoice_number2;
    @BindView(R.id.companyname)
    android.widget.TextView companyname;
    @BindView(R.id.companyaddress)
    android.widget.TextView companyaddress;
    @BindView(R.id.companytrn)
    android.widget.TextView companytrn;
    @BindView(R.id.customer_name2)
    android.widget.TextView customer_name2;
    @BindView(R.id.customer_trn2)
    android.widget.TextView customer_trn2;
    @BindView(R.id.customer_address2)
    android.widget.TextView customer_address2;

    @BindView(R.id.date_time2)
    android.widget.TextView date_time2;
    @BindView(R.id.name_id2)
    android.widget.TextView name_id2;


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
    @BindView(R.id.credit_amount)
    android.widget.TextView credit_amount;
    @BindView(R.id.vatpercentage)
    android.widget.TextView vatpercentage;
    @BindView(R.id.vatamount)
    android.widget.TextView vatamount;
    @BindView(R.id.total_amount)
    android.widget.TextView  total_amount;
    @BindView(R.id.total_Amount_words)
    android.widget.TextView total_Amount_words;


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
    //String creditInvoiceNumber="CCM-U109-19000013";
    String creditInvoiceNumber="";
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
    @BindView(R.id.footer)
    LinearLayout footer;
    @BindView(R.id.print_email)
    Button print_email;
    @BindView(R.id.payment)
    Button payment;
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.creditnote_consumption_invoice);
        ButterKnife.bind(this);
        webServiceAcess = new WebServiceAcess();

        numToWords = new NumberToWords();
        back_button.setOnClickListener(this);
        if (Utils.isNetworkAvailable(Consumption_DeliveryNote_Preview.this)) {
            progress.setVisibility(View.VISIBLE);
            contentView.setVisibility(View.GONE);
            new GetInvoiceDetails().execute();
        }
    }



    public void GetCreditNoteData() {
        if (Utils.isNetworkAvailable(Consumption_DeliveryNote_Preview.this)) {
            progress.setVisibility(View.VISIBLE);
            contentView.setVisibility(View.GONE);
            new GetCreditNoteDetails().execute();
        }
    }

    @Override
    public void onClick(View v) {
        switch (v.getId())
        {
            case R.id. back_button:
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

    /*-------------------------------------------------------------------block-------------------------------------------------------*/
    public class GetCreditNoteDetails extends AsyncTask<String, Void, String> {

        @Override
        protected String doInBackground(String... strings) {

            String result = webServiceAcess.runRequest(Common.runAction,Common.CreditNoteDetails, new String[]{creditInvoiceNumber});
            return result;
        }

        @Override
        protected void onPostExecute(String s) {
            Log.e("value", "onPostExecute: ", null);
            if (s.length() > 0) {
                try {

                    CreditNoteModel model=new   CreditNoteModel(s);
                    if(model.getStatusValue()==2)
                    {
                        setCreditNoteValues(model);
                    }
                } catch (Exception ex) {
                    ex.fillInStackTrace();
                }
            } else {

            }

        }
    }
public void setCreditNoteValues(CreditNoteModel model)
{   invoice_number2.setText(model.getInvoicenumberValue());
    companyname.setText(model.getCompanyNameValue());
    companyaddress.setText(model.getCompanyAddressValue());
    companytrn.setText(model.getCompanyTRTValue());
    customer_name2.setText(model.getCustomerNameValue());
    customer_trn2.setText(model.getCustomerTRNValue());
    customer_address2.setText(model.getCustomerAddressValue());

    date_time2.setText(Utils.getNewDate(model.getDateValue()) +" : "+model.getTimeValue());
    name_id2.setText(model.getUserNameValue()+" & "+model.getUserIDValue());
    credit_amount.setText(model.getInvoiceAmountValue());
    vatpercentage.setText("VAT @ "+model.getVatRateValue()+"%");
    vatamount.setText(model.getTotal_vat_AmountValue());
    total_amount.setText(model.getToatal_Invoice_AmountValue());
    total_Amount_words.setText("AED "+ numToWords.convertNumberToWords((int)Math.round(Double.parseDouble(model.getToatal_Invoice_AmountValue())))+" Only /-");
    progress.setVisibility(View.GONE);
    contentView.setVisibility(View.VISIBLE);
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
        GetCreditNoteData();

    }

}
