package invoices;

import android.app.Activity;
import android.app.AlertDialog;
import android.app.ProgressDialog;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.AsyncTask;
import android.os.Bundle;
import android.os.Looper;
import android.os.PersistableBundle;
import android.support.annotation.Nullable;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ProgressBar;
import android.widget.ScrollView;
import android.widget.TextView;
import android.widget.Toast;

import com.brothersgas.R;
import com.zebra.sdk.comm.BluetoothConnection;
import com.zebra.sdk.comm.Connection;
import com.zebra.sdk.comm.ConnectionException;
import com.zebra.sdk.graphics.internal.ZebraImageAndroid;
import com.zebra.sdk.printer.PrinterStatus;
import com.zebra.sdk.printer.SGD;
import com.zebra.sdk.printer.ZebraPrinter;
import com.zebra.sdk.printer.ZebraPrinterFactory;
import com.zebra.sdk.printer.ZebraPrinterLanguageUnknownException;
import com.zebra.sdk.printer.ZebraPrinterLinkOs;

import org.json.JSONArray;
import org.json.JSONObject;

import java.util.HashMap;
import java.util.Map;

import butterknife.BindView;
import butterknife.ButterKnife;
import common.Common;
import common.NumberToWords;
import common.SettingsHelper;
import common.WebServiceAcess;

import model.Connection_Disconnection_Invoice_Preview_Model;
import model.CreditNoteModel;
import payment.PaymentReceipt;
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
   // public static String creditInvoiceNumber="CCM-U109-19000013";
   public static String creditInvoiceNumber="";
    NumberToWords numToWords;
    @BindView(R.id.progressBar)
    ProgressBar progress;
    @BindView(R.id.progressBar2)
    ProgressBar progress2;
    @BindView(R.id.contentView)
    ScrollView contentView;
    @BindView(R.id.content)
    LinearLayout content;
    @BindView(R.id.back_button)
    Button back_button;
    public static String imagePath="/storage/sdcard0/Brothers_Gas/.1553619034324.jpg";
    WebServiceAcess webServiceAcess;
    @BindView(R.id. signature)
    ImageView signature;
    @BindView(R.id.footer)
    LinearLayout footer;
    @BindView(R.id.print_email)
    Button print_email;
    @BindView(R.id.payment)
    Button payment;
    int sendAttempt=0;
    @BindView(R.id.heading)
    TextView heading;
    @BindView(R.id.consumptionInvoice)
    LinearLayout consumptionInvoice;
    AlertDialog printDialog;
    ProgressDialog dialog;
    private Connection connection;
    Connection_Disconnection_Invoice_Preview_Model model;
    CreditNoteModel crModel;
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.creditnote_consumption_invoice);
        ButterKnife.bind(this);
        webServiceAcess = new WebServiceAcess();
        dialog=new ProgressDialog(Consumption_DeliveryNote_Preview.this);
        dialog.setMessage("Priniting....");
        numToWords = new NumberToWords();
        back_button.setOnClickListener(this);
        if (Utils.isNetworkAvailable(Consumption_DeliveryNote_Preview.this)) {

            if (invoice.length() > 0) {
                progress.setVisibility(View.VISIBLE);
                contentView.setVisibility(View.GONE);
                new GetInvoiceDetails().execute();

            } else {
                consumptionInvoice.setVisibility(View.GONE);
                heading.setText("");
                payment.setVisibility(View.GONE);
                GetCreditNoteData();
            }
        }
        print_email.setOnClickListener(this);
        payment.setOnClickListener(this);
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
            case R.id.print_email:

                if(sendAttempt>1)
                {
                    showPrintAlertDialog();
                }else {
                    progress2.setVisibility(View.VISIBLE);
                    footer.setVisibility(View.GONE);
                    new EmailInvoice().execute();

                }
                break;
            case R.id.payment:
                PaymentReceipt.invoiceNumber =invoice;
                startActivity(new Intent(this, PaymentReceipt.class));
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
                  model=new  Connection_Disconnection_Invoice_Preview_Model(s);
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

                   crModel=new   CreditNoteModel(s);
                    if(crModel.getStatusValue()==2)
                    {
                        setCreditNoteValues(crModel);
                    }
                } catch (Exception ex) {
                    ex.fillInStackTrace();
                }
            } else {

            }

        }
    }
public void setCreditNoteValues(CreditNoteModel model)
{   invoice_number2.setText(creditInvoiceNumber);
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
    total_Amount_words.setText(getNumberToWords(model.getToatal_Invoice_AmountValue()));
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
        total_inwords.setText(getNumberToWords(model.getTotalIncludingTaxValue()));

        Bitmap bitmap = BitmapFactory.decodeFile(imagePath);
        signature.setImageBitmap(bitmap);
        GetCreditNoteData();

    }
    /*-------------------------------------------------------------------block-------------------------------------------------------*/
    public class EmailInvoice extends AsyncTask<String, Void, String> {

        @Override
        protected String doInBackground(String... strings) {



            String result = webServiceAcess.runRequest(Common.runAction,Common.Print_EmailInvoice_CreditNote, new String[]{invoice,creditInvoiceNumber});
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
                            print_email.setText("Reprint");
                        }
                        showPrintAlertDialog();
                        Toast.makeText(Consumption_DeliveryNote_Preview.this,message,Toast.LENGTH_SHORT).show();

                    }else{
                        Utils.showAlertNormal(Consumption_DeliveryNote_Preview.this,message);
                    }


                } catch (Exception ex) {
                    ex.fillInStackTrace();
                }
                progress2.setVisibility(View.GONE);
                footer.setVisibility(View.VISIBLE);
            } else {
                progress2.setVisibility(View.GONE);
                footer.setVisibility(View.VISIBLE);
            }

        }
    }

    @Override
    protected void onDestroy() {
        creditInvoiceNumber="";
        invoice="";
        super.onDestroy();
    }


    public void showPrintAlertDialog() {
        AlertDialog.Builder dialogBuilder = new AlertDialog.Builder(this);
        LayoutInflater inflater = this.getLayoutInflater();
        final View dialogView = inflater.inflate(R.layout.printer_popup, null);
        dialogBuilder.setView(dialogView);

        final EditText edt = (EditText) dialogView.findViewById(R.id.macInput);
        Button submit=(Button)dialogView.findViewById(R.id.print);
        final LinearLayout macView=(LinearLayout)dialogView.findViewById(R.id.macView);
        final LinearLayout textView=(LinearLayout)dialogView.findViewById(R.id.textView);
        final Button cancel=(Button) dialogView.findViewById(R.id.cancel);
        cancel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                printDialog.cancel();
            }
        });

        edt.setText(SettingsHelper.getBluetoothAddress(Consumption_DeliveryNote_Preview.this));
        if(edt.getText().length()>0)
        {
            macView.setVisibility(View.GONE);
            textView.setVisibility(View.VISIBLE);
            cancel.setVisibility(View.VISIBLE);
            submit.setText("Yes");

        }else{
            macView.setVisibility(View.VISIBLE);
            textView.setVisibility(View.GONE);
            submit.setText("Submit");
            cancel.setVisibility(View.INVISIBLE);
        }
        submit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(edt.getText().toString().length()>0) {
                    dialog.show();;
                    performTest(edt.getText().toString());
                }else{
                    Toast.makeText(Consumption_DeliveryNote_Preview.this,"Please enter mac address",Toast.LENGTH_SHORT).show();
                }

            }
        });


        printDialog = dialogBuilder.create();
        printDialog.show();
    }
    public void performTest(final String macaddress) {
        new Thread(new Runnable() {
            public void run() {
                Looper.prepare();
                doPerformTest(macaddress);
                Looper.loop();
                Looper.myLooper().quit();
            }
        }).start();

    }



    /**
     * This method is used to create a new alert dialog to sign and print and implements best practices to check the status of the printer.
     */
    public void doPerformTest(String macAddress) {

        connection = new BluetoothConnection(macAddress);

        try {
            connection.open();
            final ZebraPrinter printer = ZebraPrinterFactory.getInstance(connection);
            ZebraPrinterLinkOs linkOsPrinter = ZebraPrinterFactory.createLinkOsPrinter(printer);
            PrinterStatus printerStatus = (linkOsPrinter != null) ? linkOsPrinter.getCurrentStatus() : printer.getCurrentStatus();
            getPrinterStatus();
            if (printerStatus.isReadyToPrint) {
                runOnUiThread(new Runnable() {
                    @Override
                    public void run() {

                        Toast.makeText(Consumption_DeliveryNote_Preview.this, "Printer Ready", Toast.LENGTH_LONG).show();
                        try {
                            connection.open();
                            Bitmap signature = BitmapFactory.decodeFile(imagePath);
                            Bitmap icon = BitmapFactory.decodeResource(getResources(), R.drawable.brogas_logo);
                            Bitmap signatureBitmap = Bitmap.createScaledBitmap(signature, 200, 200, false);
                            Bitmap logo = Bitmap.createScaledBitmap(icon, 350, 200, false);
                            if(invoice.length()>0) {
                                printer.printImage(new ZebraImageAndroid(signatureBitmap), 0, 0, signatureBitmap.getWidth(), signatureBitmap.getHeight(), false);
                                sendTestLabel2();
                                printer.printImage(new ZebraImageAndroid(logo), 0, 0, logo.getWidth(), logo.getHeight(), false);
                                printer.printImage(new ZebraImageAndroid(signatureBitmap), 0, 0, signatureBitmap.getWidth(), signatureBitmap.getHeight(), false);
                                sendTestLabel();
                                printer.printImage(new ZebraImageAndroid(logo), 0, 0, logo.getWidth(), logo.getHeight(), false);
                            }else{
                                printer.printImage(new ZebraImageAndroid(signatureBitmap), 0, 0, signatureBitmap.getWidth(), signatureBitmap.getHeight(), false);
                                sendTestLabel2();
                                printer.printImage(new ZebraImageAndroid(logo), 0, 0, logo.getWidth(), logo.getHeight(), false);


                            }

                            //  printer.sendFileContents("^FT78,76^A0N,28,28^FH_^FDHello_0AWorld^FS");

                            connection.close();
                            Toast.makeText(Consumption_DeliveryNote_Preview.this, "Receipt Printed Sucessfully.", Toast.LENGTH_LONG).show();
                            dialog.cancel();

                        } catch (ConnectionException e) {
                            dialog.cancel();
                            Utils.showAlertNormal(Consumption_DeliveryNote_Preview.this,e.getMessage());
                        }

                    }
                });




            } else if (printerStatus.isHeadOpen) {
                Utils.showAlertNormal(Consumption_DeliveryNote_Preview.this,"Error: Head Open \nPlease Close Printer Head to Print");
                dialog.cancel();
            } else if (printerStatus.isPaused) {
                Utils.showAlertNormal(Consumption_DeliveryNote_Preview.this,"Error: Printer Paused");
                dialog.cancel();
            } else if (printerStatus.isPaperOut) {
                Utils.showAlertNormal(Consumption_DeliveryNote_Preview.this,"Error: Media Out \nPlease Load Media to Print");
                dialog.cancel();
            } else {
                Utils.showAlertNormal(Consumption_DeliveryNote_Preview.this,"Error: Please check the Connection of the Printer");
                dialog.cancel();
            }

            connection.close();
            getAndSaveSettings(macAddress);
            if(printDialog!=null)
            {
                printDialog.cancel();
            }
        } catch (ConnectionException e) {
            Utils.showAlertNormal(Consumption_DeliveryNote_Preview.this,e.getMessage());
        } catch (ZebraPrinterLanguageUnknownException e) {
            Utils.showAlertNormal(Consumption_DeliveryNote_Preview.this,e.getMessage());
        } finally {

        }

    }

    /* * This method implements the best practices to check the language of the printer and set the language of the printer to ZPL.
     *
     * @throws ConnectionException
     */
    private void getPrinterStatus() throws ConnectionException{


        final String printerLanguage = SGD.GET("device.languages", connection); //This command is used to get the language of the printer.

        final String displayPrinterLanguage = "Printer Language is " + printerLanguage;

        SGD.SET("device.languages", "zpl", connection); //This command set the language of the printer to ZPL

        runOnUiThread(new Runnable() {
            @Override
            public void run() {

                Toast.makeText(Consumption_DeliveryNote_Preview.this, displayPrinterLanguage + "\n" + "Language set to ZPL", Toast.LENGTH_LONG).show();

            }
        });

    }

    /**
     * This method saves the entered address of the printer.
     */

    private void getAndSaveSettings(String macAddress) {
        SettingsHelper.saveBluetoothAddress(Consumption_DeliveryNote_Preview.this, macAddress);

    }


    private void sendTestLabel() {
        try {
            byte[] configLabel = createZplReceipt().getBytes();
            connection.write(configLabel);

        } catch (ConnectionException e) {
        }
    }
    private void sendTestLabel2() {
        try {
            byte[] configLabel = createZplCreditNote().getBytes();
            connection.write(configLabel);

        } catch (ConnectionException e) {
        }
    }

    private String createZplCreditNote() {


        String  tmpHeader=   "^XA" +

                "^PON^PW900^MNN^LL%d^LH0,0" + "\r\n" +

                // "^FO50,50" + "\r\n" + "^A0,N,50,50" + "\r\n" + "^FD Brothers Gas^FS" + "\r\n" +

                "^FO20,00" + "\r\n" + "^A0,N,35,35" + "\r\n" + "^FDCredit Note^FS" + "\r\n" +
                "^FO20,60" + "\r\n" + "^GB500,5,5,B,0^FS"+ "\r\n" +

                "^FO20,95" + "\r\n" + "^A0,N,25,25" + "\r\n" + "^FDInvoice No:^FS" + "\r\n" +

                "^FO225,95" + "\r\n" + "^A0,N,25,25" + "\r\n" + "^FD"+creditInvoiceNumber+"^FS" + "\r\n" +

                "^FO20,135" + "\r\n" + "^A0,N,25,25" + "\r\n" + "^FDCompany Name:^FS" + "\r\n" +

                "^FO225,135" + "\r\n" + "^A0,N,25,25" + "\r\n" + "^FD"+crModel.getCompanyNameValue()+"^FS" + "\r\n" +

                "^FO20,180" + "\r\n" + "^A0,N,25,25" + "\r\n" + "^FDCompany TRN^FS" + "\r\n" +

                "^FO230,180" + "\r\n" + "^A0,N,25,25" + "\r\n" + "^FD"+crModel.getCompanyTRTValue()+"^FS" + "\r\n" +

                "^FO20,220" + "\r\n" + "^A0,N,25,25" + "\r\n" + "^FDCompany Address^FS" + "\r\n" +

                "^FO230,220" + "\r\n" + "^A0,N,25,25" + "\r\n" + "^FD"+crModel.getCompanyAddressValue()+"^FS" + "\r\n" +
                "^FO20,260" + "\r\n" + "^A0,N,25,25" + "\r\n" + "^FDCustomer Name^FS" + "\r\n" +

                "^FO230,260" + "\r\n" + "^A0,N,25,25" + "\r\n" + "^FD"+crModel.getCustomerNameValue()+"^FS" + "\r\n" +
                "^FO20,300" + "\r\n" + "^A0,N,25,25" + "\r\n" + "^FDCust. Address^FS" + "\r\n" +

                "^FO230,300" + "\r\n" + "^A0,N,25,25" + "\r\n" + "^FD"+crModel.getCustomerAddressValue()+"^FS" + "\r\n" +
                "^FO20,340" + "\r\n" + "^A0,N,25,25" + "\r\n" + "^FDCustomer TRN ^FS" + "\r\n" +

                "^FO20,380" + "\r\n" + "^A0,N,25,25" + "\r\n" + "^FDUser Name ^FS" + "\r\n" +

                "^FO230,380" + "\r\n" + "^A0,N,25,25" + "\r\n" + "^FD"+crModel.getUserNameValue()+"^FS" + "\r\n" +
                "^FO20,420" + "\r\n" + "^A0,N,25,25" + "\r\n" + "^FDUser Id ^FS" + "\r\n" +

                "^FO230,420" + "\r\n" + "^A0,N,25,25" + "\r\n" + "^FD"+crModel.getUserIDValue()+"^FS" + "\r\n" +

                "^FO20,460" + "\r\n" + "^A0,N,25,25" + "\r\n" + "^FDDate & Time ^FS" + "\r\n" +

                "^FO230,460" + "\r\n" + "^A0,N,25,25" + "\r\n" + "^FD"+Utils.getNewDate(crModel.getDateValue())+" & "+crModel.getTimeValue()+"^FS" + "\r\n" +

                "^FO20,490" + "\r\n" + "^GB500,5,5,B,0^FS";

        int headerHeight =500;



        String body = String.format("^LH0,%d", headerHeight);

        float totalPrice = 0;



        long footerStartPosition = headerHeight;





        String footer = String.format("^LH0,%d" + "\r\n" +


                "^FO20,15" + "\r\n" + "^A0,N,30,30" + "\r\n" + "^FDCredit Note Amount^FS" + "\r\n" +

                "^FO320,15" + "\r\n" + "^A0,N,30,30" + "\r\n" + "^FDAED "+crModel.getInvoiceAmountValue()+"^FS" + "\r\n" +
                "^FO20,55" + "\r\n" + "^A0,N,30,30" + "\r\n" + "^FDTotal VAT^FS" + "\r\n" +

                "^FO320,55" + "\r\n" + "^A0,N,30,30" + "\r\n" + "^FDAED "+crModel.getVatAmountValue()+"^FS" + "\r\n" +


                "^FO20,95" + "\r\n" + "^A0,N,30,30" + "\r\n" + "^FDTotal^FS" + "\r\n" +

                "^FO320,95" + "\r\n" + "^A0,N,30,30" + "\r\n" + "^FDAED "+ crModel.getToatal_Credit_Note_AmountValue()+"^FS" + "\r\n" +
                "^FO20,135" + "\r\n" + "^GB500,5,5,B,0^FS"+

                "^FO20,175" + "\r\n" + "^A0,N,25,25" + "\r\n" + "^FDAmount(Words)^FS" + "\r\n" +

                "^FO320,175" + "\r\n" + "^A0,N,25,25" + "\r\n" + "^FD "+getNumberToWords(crModel.getToatal_Credit_Note_AmountValue())+"^FS" + "\r\n" +
                "^FO20,215" + "\r\n" + "^GB500,5,5,B,0^FS"+

                "^FO20,255" + "\r\n" + "^A0,N,25,25" + "\r\n" + "^FDThanks for choosing Brothers Gas!^FS" + "\r\n" +
                "^FO20,295" + "\r\n" + "^A0,N,35,35" + "\r\n" + "^FDSignature!^FS" + "\r\n" +

                "^FO20,335" + "\r\n"  + "^XZ", footerStartPosition, totalPrice);

        long footerHeight = 350;
        long labelLength = headerHeight +footerHeight;



        String header = String.format(tmpHeader, labelLength,Utils.getNewDate(crModel.getDateValue()));

        String wholeZplLabel = String.format("%s%s%s", header, body, footer);

        return wholeZplLabel;
    }

    private String createZplReceipt() {
        /*
         This routine is provided to you as an example of how to create a variable length label with user specified data.
         The basic flow of the example is as follows
            Header of the label with some variable data
            Body of the label
                Loops thru user content and creates small line items of printed material
            Footer of the label

         As you can see, there are some variables that the user provides in the header, body and footer, and this routine uses that to build up a proper ZPL string for printing.
         Using this same concept, you can create one label for your receipt header, one for the body and one for the footer. The body receipt will be duplicated as many items as there are in your variable data

         */




        /*
         Some basics of ZPL. Find more information here : http://www.zebra.com/content/dam/zebra/manuals/en-us/printer/zplii-pm-vol2-en.pdf

         ^XA indicates the beginning of a label
         ^PW sets the width of the label (in dots)
         ^MNN sets the printer in continuous mode (variable length receipts only make sense with variably sized labels)
         ^LL sets the length of the label (we calculate this value at the end of the routine)
         ^LH sets the reference axis for printing.
            You will notice we change this positioning of the 'Y' axis (length) as we build up the label. Once the positioning is changed, all new fields drawn on the label are rendered as if '0' is the new home position
         ^FO sets the origin of the field relative to Label Home ^LH
         ^A sets font information
         ^FD is a field description
         ^GB is graphic boxes (or lines)
         ^B sets barcode information
         ^XZ indicates the end of a label
         */

        String  tmpHeader=   "^XA" +

                "^PON^PW900^MNN^LL%d^LH0,0" + "\r\n" +

                // "^FO50,50" + "\r\n" + "^A0,N,50,50" + "\r\n" + "^FD Brothers Gas^FS" + "\r\n" +

                "^FO20,00" + "\r\n" + "^A0,N,35,35" + "\r\n" + "^FDConsumption Invoice^FS" + "\r\n" +
                "^FO20,60" + "\r\n" + "^GB500,5,5,B,0^FS"+ "\r\n" +

                "^FO20,95" + "\r\n" + "^A0,N,25,25" + "\r\n" + "^FDInvoice No:^FS" + "\r\n" +

                "^FO225,95" + "\r\n" + "^A0,N,25,25" + "\r\n" + "^FD"+model.getInvoice_NumberValue()+"^FS" + "\r\n" +

                "^FO20,135" + "\r\n" + "^A0,N,25,25" + "\r\n" + "^FDProject Name:^FS" + "\r\n" +

                "^FO225,135" + "\r\n" + "^A0,N,25,25" + "\r\n" + "^FD"+model.getProjectnameValue()+"^FS" + "\r\n" +

                "^FO20,180" + "\r\n" + "^A0,N,25,25" + "\r\n" + "^FDTeenant Name^FS" + "\r\n" +

                "^FO230,180" + "\r\n" + "^A0,N,25,25" + "\r\n" + "^FD"+model.getTenantNameValue()+"^FS" + "\r\n" +

                "^FO20,220" + "\r\n" + "^A0,N,25,25" + "\r\n" + "^FDCustomer Name^FS" + "\r\n" +

                "^FO230,220" + "\r\n" + "^A0,N,25,25" + "\r\n" + "^FD"+model.getCustomerNameValue()+"^FS" + "\r\n" +
                "^FO20,260" + "\r\n" + "^A0,N,25,25" + "\r\n" + "^FDCustomer TRN^FS" + "\r\n" +

                "^FO230,260" + "\r\n" + "^A0,N,25,25" + "\r\n" + "^FD"+model.getCustomerTRNNumberValue()+"^FS" + "\r\n" +
                "^FO20,300" + "\r\n" + "^A0,N,25,25" + "\r\n" + "^FDCust. Address^FS" + "\r\n" +

                "^FO230,300" + "\r\n" + "^A0,N,25,25" + "\r\n" + "^FD"+model.getCustomerAddressValue()+"^FS" + "\r\n" +
                "^FO20,340" + "\r\n" + "^A0,N,25,25" + "\r\n" + "^FDSupplier Name ^FS" + "\r\n" +

                "^FO230,340" + "\r\n" + "^A0,N,25,25" + "\r\n" + "^FD"+model.getSuppliername()+"^FS" + "\r\n" +
                "^FO20,380" + "\r\n" + "^A0,N,25,25" + "\r\n" + "^FDSupplier TRN ^FS" + "\r\n" +

                "^FO230,380" + "\r\n" + "^A0,N,25,25" + "\r\n" + "^FD"+model.getSupplierTRN()+"^FS" + "\r\n" +

                "^FO20,420" + "\r\n" + "^A0,N,25,25" + "\r\n" + "^FDReg. Address ^FS" + "\r\n" +

                "^FO230,420" + "\r\n" + "^A0,N,25,25" + "\r\n" + "^FD"+model.getRegisteredAddress()+"^FS" + "\r\n" +
                "^FO20,460" + "\r\n" + "^A0,N,25,25" + "\r\n" + "^FDUser Name ^FS" + "\r\n" +

                "^FO230,460" + "\r\n" + "^A0,N,25,25" + "\r\n" + "^FD"+model.getUserNameValue()+"^FS" + "\r\n" +
                "^FO20,500" + "\r\n" + "^A0,N,25,25" + "\r\n" + "^FDUser Id ^FS" + "\r\n" +

                "^FO230,500" + "\r\n" + "^A0,N,25,25" + "\r\n" + "^FD"+model.getUserIDValue()+"^FS" + "\r\n" +

                "^FO20,540" + "\r\n" + "^A0,N,25,25" + "\r\n" + "^FDDate & Time ^FS" + "\r\n" +

                "^FO230,540" + "\r\n" + "^A0,N,25,25" + "\r\n" + "^FD"+Utils.getNewDate(model.getDateValue())+" & "+model.getTimeValue()+"^FS" + "\r\n" +
                "^FO20,570" + "\r\n" + "^GB500,5,5,B,0^FS"+ "\r\n" +

                "^FO20,600" + "\r\n" + "^A0,N,25,25" + "\r\n" + "^FDPrevious Reading ^FS" + "\r\n" +

                "^FO260,600" + "\r\n" + "^A0,N,25,25" + "\r\n" + "^FD"+model.getPreviousMeterreadingValue()+"^FS" + "\r\n" +

                "^FO20,640" + "\r\n" + "^A0,N,25,25" + "\r\n" + "^FDCurrent Reading ^FS" + "\r\n" +

                "^FO260,640" + "\r\n" + "^A0,N,25,25" + "\r\n" + "^FD"+model.getPresentMeterreadingValue()+"^FS" + "\r\n" +

                "^FO20,680" + "\r\n" + "^A0,N,25,25" + "\r\n" + "^FDPresent Reading ^FS" + "\r\n" +

                "^FO260,680" + "\r\n" + "^A0,N,25,25" + "\r\n" + "^FD"+model.getPresentMeterreadingValue()+"^FS" + "\r\n" +
                "^FO20,720" + "\r\n" + "^A0,N,25,25" + "\r\n" + "^FDUnits Consumed ^FS" + "\r\n" +

                "^FO260,720" + "\r\n" + "^A0,N,25,25" + "\r\n" + "^FD"+model.getUnitsConsumed()+"^FS" + "\r\n" +

                "^FO20,760" + "\r\n" + "^A0,N,25,25" + "\r\n" + "^FDPressure Factor ^FS" + "\r\n" +

                "^FO260,760" + "\r\n" + "^A0,N,25,25" + "\r\n" + "^FD"+model.getPressureFactorValue()+"^FS" + "\r\n" +
                "^FO20,800" + "\r\n" + "^A0,N,25,25" + "\r\n" + "^FDActual Units ^FS" + "\r\n" +

                "^FO260,800" + "\r\n" + "^A0,N,25,25" + "\r\n" + "^FD"+model.getActualUnitConsumedValue()+"^FS" + "\r\n" +

                "^FO20,840" + "\r\n" + "^GB500,5,5,B,0^FS";

        int headerHeight =850;



        String body = String.format("^LH0,%d", headerHeight);

        int heightOfOneLine = 40;

        float totalPrice = 0;

        Map<String, String> itemsToPrint = createListOfItems();

        int i = 0;
        for (String productName : itemsToPrint.keySet()) {
            String price = itemsToPrint.get(productName);
            productName=getFormattedName(productName );

            String lineItem = "^FO20,%d" + "\r\n" + "^A0,N,28,28" + "\r\n" + "^FD%s^FS" + "\r\n" + "^FO320,%d" + "\r\n" + "^A0,N,28,28" + "\r\n" + "^FDAED %s^FS";

            int totalHeight = i++ * heightOfOneLine;
            body += String.format(lineItem, totalHeight, productName, totalHeight, price);

        }

        long totalBodyHeight = (itemsToPrint.size() + 1) * heightOfOneLine;
        // long totalBodyHeight =0;

        long footerStartPosition = headerHeight+totalBodyHeight;





        String footer = String.format("^LH0,%d" + "\r\n" +

                "^FO20,00" + "\r\n" + "^GB500,5,5,B,0^FS"+
                "^FO20,15" + "\r\n" + "^A0,N,30,30" + "\r\n" + "^FDTotal(Exc.VAT)^FS" + "\r\n" +

                "^FO320,15" + "\r\n" + "^A0,N,30,30" + "\r\n" + "^FDAED "+model.getTotalExcludingTaxValue()+"^FS" + "\r\n" +
                "^FO20,55" + "\r\n" + "^A0,N,30,30" + "\r\n" + "^FDTotal VAT^FS" + "\r\n" +

                "^FO320,55" + "\r\n" + "^A0,N,30,30" + "\r\n" + "^FDAED "+model.getTotalVatValue()+"^FS" + "\r\n" +


                "^FO20,95" + "\r\n" + "^A0,N,30,30" + "\r\n" + "^FDTotal(Inc.VAT)^FS" + "\r\n" +

                "^FO320,95" + "\r\n" + "^A0,N,30,30" + "\r\n" + "^FDAED "+model.getTotalIncludingTaxValue()+"^FS" + "\r\n" +
                "^FO20,135" + "\r\n" + "^GB500,5,5,B,0^FS"+

                "^FO20,175" + "\r\n" + "^A0,N,25,25" + "\r\n" + "^FDAmount(Words)^FS" + "\r\n" +

                "^FO320,175" + "\r\n" + "^A0,N,25,25" + "\r\n" + "^FD "+getNumberToWords(model.getTotalIncludingTaxValue())+"^FS" + "\r\n" +
                "^FO20,215" + "\r\n" + "^GB500,5,5,B,0^FS"+

                "^FO20,255" + "\r\n" + "^A0,N,25,25" + "\r\n" + "^FDThanks for choosing Brothers Gas!^FS" + "\r\n" +
                "^FO20,295" + "\r\n" + "^A0,N,35,35" + "\r\n" + "^FDSignature!^FS" + "\r\n" +

                "^FO20,335" + "\r\n"  + "^XZ", footerStartPosition, totalPrice);

        long footerHeight = 350;
        long labelLength = headerHeight + totalBodyHeight + footerHeight;



        String header = String.format(tmpHeader, labelLength,Utils.getNewDate(model.getDate()));

        String wholeZplLabel = String.format("%s%s%s", header, body, footer);

        return wholeZplLabel;
    }




    private Map<String, String> createListOfItems() {
        Map<String, String> retVal = new HashMap<String, String>();
        int j=1;

        for (int i = 0; i <model .getDetails_list().size();i++) {
            Connection_Disconnection_Invoice_Preview_Model.BillDetails modell=model.getDetails_list().get(i);
            retVal.put("Vat"+j+" @"+modell.getVat_percentageValue()+"%", modell.getVatAmountValue());
            retVal.put(modell.getItemNameValue(), modell.getTotalpriceValue());
            j++;
        }
        return retVal;
    }

    public String getFormattedName(String value)
    {if(value.contains("@")) {
        String [] val=value.split("@");
        switch (val[0].trim()) {
            case "Vat1":
            case "Vat2":
            case "Vat3":
            case "Vat4":
            case "Vat5":
                return "Vat @ "+val[1];
        }
    }
        return value;
    }
    public String getNumberToWords(String val) {
        String words = "";
        if (val.contains(".")) {
            String[] value = val.split("\\.");
            if (Integer.parseInt(value[1]) > 0) {
                String string1 = numToWords.convertNumberToWords(Integer.parseInt(value[0]));
                if (value[1].length() == 1) {
                    value[1] = value[1] + "0";
                }
                String string2 = numToWords.convertNumberToWords(Integer.parseInt(value[1]));
                words = "AED " + string1 + " and " + string2 + " Fils Only /-";

            } else {
                words = "AED " + numToWords.convertNumberToWords((int) Math.round(Double.parseDouble(val))) + " Only /-";
            }
        }else {
            words = "AED " + numToWords.convertNumberToWords((int) Math.round(Double.parseDouble(val))) + " Only /-";
        }
        return words;
    }

}
