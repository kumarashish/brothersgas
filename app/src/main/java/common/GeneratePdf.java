package common;

import android.app.Activity;
import android.os.AsyncTask;
import android.os.Bundle;
import android.os.Environment;
import android.support.annotation.Nullable;
import android.util.Log;
import android.view.View;
import android.widget.*;

import com.brothersgas.R;
import com.itextpdf.text.BaseColor;
import com.itextpdf.text.Chunk;
import com.itextpdf.text.Document;
import com.itextpdf.text.DocumentException;
import com.itextpdf.text.Element;
import com.itextpdf.text.Font;
import com.itextpdf.text.PageSize;
import com.itextpdf.text.Paragraph;
import com.itextpdf.text.pdf.BaseFont;
import com.itextpdf.text.pdf.PdfWriter;
import com.itextpdf.text.pdf.draw.LineSeparator;

import org.json.JSONArray;
import org.json.JSONObject;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;

import butterknife.BindView;
import butterknife.ButterKnife;
import invoices.Block_Cancel_Details;
import invoices.Connection_Disconnection_Invoice;
import invoices.Print_Email;
import model.BlockUnblockModel;
import model.Connection_Disconnection_Invoice_Preview_Model;
import payment.InvoiceList;
import utils.Utils;


public class GeneratePdf extends Activity {
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
    String invoice="CDC-U109-19000053";
    NumberToWords numToWords;
    @BindView(R.id.progressBar)
    ProgressBar progress;
    @BindView(R.id.contentView)
    ScrollView contentView;
    @BindView(R.id.content)
    LinearLayout content;


    @Override
    protected void onCreate( Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.preview_screen);
        ButterKnife.bind(this);
        webServiceAcess = new WebServiceAcess();
        controller=(AppController)getApplicationContext();
        numToWords=new NumberToWords();
        if(Utils.isNetworkAvailable(GeneratePdf.this)) {
            progress.setVisibility(View.VISIBLE);
            contentView.setVisibility(View.GONE);
            new GetInvoiceDetails().execute();
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
//        signature
    }
}
