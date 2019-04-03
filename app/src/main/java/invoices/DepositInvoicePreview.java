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

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;

import butterknife.BindView;
import butterknife.ButterKnife;
import common.AppController;
import common.Common;
import common.NumberToWords;
import common.SettingsHelper;
import common.WebServiceAcess;
import consumption.ConsumptionPreview;
import model.Connection_Disconnection_Invoice_Preview_Model;
import model.Deposit_Invoice_Model;
import payment.PaymentReceipt;
import payment.PaymentReceiptPreview;
import utils.Utils;

public class DepositInvoicePreview extends Activity implements View.OnClickListener {
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



    @BindView(R.id.total)
    android.widget.TextView total;
    @BindView(R.id.total_inwords)
    android.widget.TextView total_inwords;
    @BindView(R.id.supplier_trn)
    android.widget.TextView supplier_trn;
    @BindView(R.id.registered_supplier_address)
    android.widget.TextView registered_supplier_address;
    @BindView(R.id.signature)
    ImageView signature;
    AppController controller;
    WebServiceAcess webServiceAcess;
    //public static String invoice="DMR-U109-19000202";
    public static String invoice ;
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
    public static String imagePath = "";//storage/sdcard0/Brothers_Gas/.1553619034324.jpg
     int sendAttempt=0;
    @BindView(R.id.footer)
    LinearLayout footer;
    @BindView(R.id.print_email)
    Button print_email;
    @BindView(R.id.payment)
    Button payment;
    Deposit_Invoice_Model model;
    AlertDialog printDialog;
    ProgressDialog dialog;
    private Connection connection;
    int xposition=0;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.deposit_invoice_preview);
        ButterKnife.bind(this);
        dialog=new ProgressDialog(DepositInvoicePreview.this);
        dialog.setMessage("Priniting....");
        webServiceAcess = new WebServiceAcess();
        controller = (AppController) getApplicationContext();
        numToWords = new NumberToWords();
        payment.setOnClickListener(this);
        print_email.setOnClickListener(this);
        back_button.setOnClickListener(this);
        if (Utils.isNetworkAvailable(DepositInvoicePreview.this)) {
            progress.setVisibility(View.VISIBLE);
            contentView.setVisibility(View.GONE);
            new GetInvoiceDetails().execute();
        }

    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.back_button:
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

            String result = webServiceAcess.runRequest(Common.runAction, Common.DepositInvoiceDetails, new String[]{invoice});
            return result;
        }

        @Override
        protected void onPostExecute(String s) {
            Log.e("value", "onPostExecute: ", null);
            if (s.length() > 0) {
                try {
                    model = new  Deposit_Invoice_Model(s);
                    if (model.getStatus() == 2) {
                        setValues(model);
                    }

                } catch (Exception ex) {
                    ex.fillInStackTrace();
                }
            } else {

            }

        }
    }

    public void setValues(Deposit_Invoice_Model model) {
        invoice_number.setText(model.getInvoice_NumberValue());
        project_name.setText(model.getProjectnameValue());
        teenant_name.setText(model.getTenantNameValue());
        customer_name.setText(model.getCustomerNameValue());
        customer_trn.setText(model.getCustomerTRNNumberValue());
        customer_address.setText(model.getCustomerAddressValue());
        suplier_name.setText(model.getSuppliername());
        supplier_trn.setText(model.getSupplierTRN());
        registered_supplier_address.setText(model.getRegisteredAddress());
        date_time.setText(Utils.getNewDate(model.getDateValue()) + "  " + model.getTime());
        name_id.setText(model.getUserNameValue() + "  &  " + model.getUserIDValue());

        for (int i = 0; i < model.getDetails_list().size(); i++) {
            View view = getLayoutInflater().inflate(R.layout.deposit_content_row, null);
            TextView item_name = (TextView) view.findViewById(R.id.item_name);
            TextView unit = (TextView) view.findViewById(R.id.unit);
            TextView quantity = (TextView) view.findViewById(R.id.quantity);
            TextView unit_price = (TextView) view.findViewById(R.id.unit_price);
            TextView total_price = (TextView) view.findViewById(R.id.total_price);

            item_name.setText(model.getDetails_list().get(i).getItemNameValue());
            unit.setText(model.getDetails_list().get(i).getUnitofMeasureValue());
            quantity.setText(model.getDetails_list().get(i).getQuantityValue());
            unit_price.setText(model.getDetails_list().get(i).getUnitPriceValue());
            total_price.setText(model.getDetails_list().get(i).getTotalpriceValue());

            content.addView(view);
        }


if(model.getTotalIncludingTaxValue()==null)
{
    String totalval=getTotal(model);
    total.setText(totalval);
    total_inwords.setText(getNumberToWords(totalval));

}else {
    total.setText(model.getTotalIncludingTaxValue());
    total_inwords.setText(getNumberToWords(model.getTotalIncludingTaxValue()));
}

       progress.setVisibility(View.GONE);
        contentView.setVisibility(View.VISIBLE);

         Bitmap bitmap = BitmapFactory.decodeFile(imagePath);
         signature.setImageBitmap(bitmap);

    }

    public String getTotal(Deposit_Invoice_Model model)
    {
        double total=0.0;
        for (int i = 0; i < model.getDetails_list().size(); i++) {
            total=total+Double.parseDouble(model.getDetails_list().get(i).getTotalpriceValue());
        }
            return Double.toString(total);
        }

    /*-------------------------------------------------------------------block-------------------------------------------------------*/
    public class EmailInvoice extends AsyncTask<String, Void, String> {

        @Override
        protected String doInBackground(String... strings) {



            String result = webServiceAcess.runRequest(Common.runAction,Common.Print_Email, new String[]{invoice,"1"});
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
                        Toast.makeText(DepositInvoicePreview.this,message,Toast.LENGTH_SHORT).show();

                    }else{
                        Utils.showAlertNormal(DepositInvoicePreview.this,message);
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

        edt.setText(SettingsHelper.getBluetoothAddress(DepositInvoicePreview.this));
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
                    Toast.makeText(DepositInvoicePreview.this,"Please enter mac address",Toast.LENGTH_SHORT).show();
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

    public void showAlert()
    {

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

                        Toast.makeText(DepositInvoicePreview.this, "Printer Ready", Toast.LENGTH_LONG).show();
                        try {
                            connection.open();
                            Bitmap signature = BitmapFactory.decodeFile(imagePath);
                            Bitmap icon = BitmapFactory.decodeResource(getResources(), R.drawable.brogas_logo);
                            Bitmap signatureBitmap = Bitmap.createScaledBitmap(signature, 200, 200, false);
                            Bitmap logo = Bitmap.createScaledBitmap(icon, 350, 200, false);
                            createfooterReceipt();
                            printer.printImage(new ZebraImageAndroid(signatureBitmap), 0, 0, signatureBitmap.getWidth(), signatureBitmap.getHeight(), false);
                            sendTestLabel();
                            printer.printImage(new ZebraImageAndroid(logo), 0, 0, logo.getWidth(), logo.getHeight(), false);
                            createLine();

                            //  printer.sendFileContents("^FT78,76^A0N,28,28^FH_^FDHello_0AWorld^FS");

                            connection.close();
                            Toast.makeText(DepositInvoicePreview.this, "Receipt Printed Sucessfully.", Toast.LENGTH_LONG).show();
                            dialog.cancel();

                        } catch (ConnectionException e) {
                            dialog.cancel();
                            Utils.showAlertNormal(DepositInvoicePreview.this,e.getMessage());
                        }

                    }
                });




            } else if (printerStatus.isHeadOpen) {
                Utils.showAlertNormal(DepositInvoicePreview.this,"Error: Head Open \nPlease Close Printer Head to Print");
                dialog.cancel();
            } else if (printerStatus.isPaused) {
                Utils.showAlertNormal(DepositInvoicePreview.this,"Error: Printer Paused");
                dialog.cancel();
            } else if (printerStatus.isPaperOut) {
                Utils.showAlertNormal(DepositInvoicePreview.this,"Error: Media Out \nPlease Load Media to Print");
                dialog.cancel();
            } else {
                Utils.showAlertNormal(DepositInvoicePreview.this,"Error: Please check the Connection of the Printer");
                dialog.cancel();
            }

            connection.close();
            getAndSaveSettings(macAddress);
            if(printDialog!=null)
            {
                printDialog.cancel();
            }
        } catch (ConnectionException e) {
            Utils.showAlertNormal(DepositInvoicePreview.this,e.getMessage());
        } catch (ZebraPrinterLanguageUnknownException e) {
            Utils.showAlertNormal(DepositInvoicePreview.this,e.getMessage());
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

                Toast.makeText(DepositInvoicePreview.this, displayPrinterLanguage + "\n" + "Language set to ZPL", Toast.LENGTH_LONG).show();

            }
        });

    }

    /**
     * This method saves the entered address of the printer.
     */

    private void getAndSaveSettings(String macAddress) {
        SettingsHelper.saveBluetoothAddress(DepositInvoicePreview.this, macAddress);

    }


    private void sendTestLabel() {
        try {
            byte[] configLabel = createZplReceipt().getBytes();
            connection.write(configLabel);

        } catch (ConnectionException e) {
        }
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
       xposition=0;
        String  tmpHeader=   "^XA" +

                "^PON^PW900^MNN^LL%d^LH0,0" + "\r\n" +

                // "^FO50,50" + "\r\n" + "^A0,N,50,50" + "\r\n" + "^FD Brothers Gas^FS" + "\r\n" +

                "^FO20,"+xposition+"" + "\r\n" + "^A0,N,35,35" + "\r\n" + "^FDDeposit Advice^FS" + "\r\n" +
                "^FO20,"+getXposition()+"" + "\r\n" + "^GB900,5,5,B,0^FS"+ "\r\n" +

                "^FO20,"+getXposition()+"" + "\r\n" + "^A0,N,25,25" + "\r\n" + "^FDInvoice No:^FS" + "\r\n" +

                "^FO225,"+xposition+"" + "\r\n" + "^A0,N,25,25" + "\r\n" + "^FD"+model.getInvoice_NumberValue()+"^FS" + "\r\n" +

                "^FO20,"+getXposition()+"" + "\r\n" + "^A0,N,25,25" + "\r\n" + "^FDProject Name:^FS" + "\r\n" +

                "^FO225,"+xposition+"" + "\r\n" + "^A0,N,25,25" + "\r\n" + "^FD"+model.getProjectnameValue()+"^FS" + "\r\n" +

                "^FO20,"+getXposition()+"" + "\r\n" + "^A0,N,25,25" + "\r\n" + "^FDTenant Name^FS" + "\r\n" +

                "^FO230,"+xposition+"" + "\r\n" + "^A0,N,25,25" + "\r\n" + "^FD"+model.getTenantNameValue()+"^FS" + "\r\n" +
                "^FO20,"+getXposition()+"" + "\r\n" + "^A0,N,25,25" + "\r\n" + "^FDCustomer Name^FS" + "\r\n" +
                "^FO230,"+xposition+"" + "\r\n" + "^A0,N,25,25" + "\r\n" + "^FD"+model.getCustomerNameValue()+"^FS" + "\r\n" +
                "^FO20,"+getXposition()+"" + "\r\n" + "^A0,N,25,25" + "\r\n" + "^FDCustomer TRN^FS" + "\r\n" +

                "^FO230,"+xposition+"" + "\r\n" + "^A0,N,25,25" + "\r\n" + "^FD"+model.getCustomerTRNNumberValue()+"^FS" + "\r\n" +
                "^FO20,"+getXposition()+"" + "\r\n" + "^A0,N,25,25" + "\r\n" + "^FDCust. Address^FS" + "\r\n" +

                "^FO230,"+xposition+"" + "\r\n" + "^A0,N,25,25" + "\r\n" + "^FD"+model.getCustomerAddressValue()+"^FS" + "\r\n" +
                "^FO20,"+getXposition()+"" + "\r\n" + "^A0,N,25,25" + "\r\n" + "^FDSupplier Name ^FS" + "\r\n" +
                getFormattedText(model.getSuppliername())+ "\r\n" +
                "^FO20,"+getXposition()+"" + "\r\n" + "^A0,N,25,25" + "\r\n" + "^FDSupplier TRN ^FS" + "\r\n" +

                "^FO230,"+xposition+"" + "\r\n" + "^A0,N,25,25" + "\r\n" + "^FD"+model.getSupplierTRN()+"^FS" + "\r\n" +

                "^FO20,"+getXposition()+"" + "\r\n" + "^A0,N,25,25" + "\r\n" + "^FDReg. Address ^FS" + "\r\n" +

                 getFormattedText(model.getRegisteredAddress())+ "\r\n" +
                "^FO20,"+getXposition()+"" + "\r\n" + "^A0,N,25,25" + "\r\n" + "^FDUser Name ^FS" + "\r\n" +

                "^FO230,"+xposition+"" + "\r\n" + "^A0,N,25,25" + "\r\n" + "^FD"+model.getUserNameValue()+"^FS" + "\r\n" +
                "^FO20,"+getXposition()+"" + "\r\n" + "^A0,N,25,25" + "\r\n" + "^FDUser Id ^FS" + "\r\n" +

                "^FO230,"+xposition+"" + "\r\n" + "^A0,N,25,25" + "\r\n" + "^FD"+model.getUserIDValue()+"^FS" + "\r\n" +

                "^FO20,"+getXposition()+"" + "\r\n" + "^A0,N,25,25" + "\r\n" + "^FDDate & Time ^FS" + "\r\n" +

                "^FO230,"+xposition+"" + "\r\n" + "^A0,N,25,25" + "\r\n" + "^FD"+Utils.getNewDate(model.getDateValue())+"  "+model.getTimeValue()+"^FS" + "\r\n" +
                "^FO20,"+getXposition()+"" + "\r\n" + "^GB900,5,5,B,0^FS"+ "\r\n" +
                "^FO20,"+getXposition()+"" + "\r\n" + "^A0,N,28,28" + "\r\n" + "^FDItem Name ^FS" + "\r\n" +
                "^FO320,"+xposition+"" + "\r\n" + "^A0,N,28,28" + "\r\n" + "^FDAmount ^FS" + "\r\n" ;

        int headerHeight =getXposition();



        String body = String.format("^LH0,%d", headerHeight);

        int heightOfOneLine = 40;

        float totalPrice = 0;

        Map<String, String> itemsToPrint = createListOfItems();

        int i = 0;
        for (String productName : itemsToPrint.keySet()) {
            String price = itemsToPrint.get(productName);
            String lineItem = "^FO20,%d" + "\r\n" + "^A0,N,24,24" + "\r\n" + "^FD%s^FS" + "\r\n" + "^FO320,%d" + "\r\n" + "^A0,N,24,24" + "\r\n" + "^FDAED %s^FS";
            int totalHeight = i++ * heightOfOneLine;
            body += String.format(lineItem, totalHeight, productName, totalHeight, price);

        }

        long totalBodyHeight = (itemsToPrint.size() + 1) * heightOfOneLine;
        // long totalBodyHeight =0;

        long footerStartPosition = headerHeight+totalBodyHeight;



        String totalval=getTotal(model);

        xposition=0;
        String footer = String.format("^LH0,%d" + "\r\n" +

                "^FO20,"+xposition+"" + "\r\n" + "^GB900,5,5,B,0^FS"+
                "^FO20,"+getXposition()+"" + "\r\n" + "^A0,N,30,30" + "\r\n" + "^FDTotal^FS" + "\r\n" +

                "^FO320,40" + "\r\n" + "^A0,N,30,30" + "\r\n" + "^FDAED "+totalval+"^FS" + "\r\n" +
                "^FO20,"+getXposition()+"" + "\r\n" + "^GB900,5,5,B,0^FS"+

                "^FO20,"+getXposition()+"" + "\r\n" + "^A0,N,25,25" + "\r\n" + "^FDAmount(Words)^FS" + "\r\n" +

                  getFormattedText(getNumberToWords(totalval))+"\r\n" +
                "^FO20,"+getXposition()+"" + "\r\n" + "^GB900,5,5,B,0^FS"+

                "^FO20,"+getXposition()+"" + "\r\n" + "^A0,N,25,25" + "\r\n" + "^FDThis is computer generated document does not require signature^FS" + "\r\n" +
                "^FO20,"+getXposition()+"" + "\r\n" + "^A0,N,25,25" + "\r\n" + "^FDTHIS IS BILL ONLY NOT A RECEIPT,PLEASE COLLECT RECEIPT FOR PAYMENTS^FS" + "\r\n" +
                "^FO20,"+getXposition()+"" + "\r\n" + "^A0,N,35,35" + "\r\n" + "^FD^FS" + "\r\n" +
                "^FO20,"+getXposition()+"" + "\r\n" + "^A0,N,35,35" + "\r\n" + "^FDCustomer Signature^FS" + "\r\n" +
                "^FO20,"+getXposition()+"" + "\r\n"  + "^XZ", footerStartPosition, totalPrice);

        long footerHeight = xposition+10;
        long labelLength = headerHeight + totalBodyHeight + footerHeight;



        String header = String.format(tmpHeader, labelLength,Utils.getNewDate(model.getDate()));

        String wholeZplLabel = String.format("%s%s%s", header, body, footer);

        return wholeZplLabel;
    }

    private Map<String, String> createListOfItems() {
        Map<String, String> retVal = new HashMap<String, String>();
        int j=1;

        for (int i = 0; i < model.getDetails_list().size();i++) {
         Deposit_Invoice_Model.BillDetails modell=model.getDetails_list().get(i);

            retVal.put(modell.getItemNameValue(), modell.getTotalpriceValue());
            j++;
        }
        return retVal;
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
    public String getFormattedText(String text) {
        String s = "";
        if (text.length() > 94) {
            s = "^FO230," + xposition + "" + "\r\n" + "^A0,N,25,25" + "\r\n" + "^FD" + text.substring(0, 47) + "^FS" + "\r\n" +
                    "^FO230," + getXposition() + "" + "\r\n" + "^A0,N,25,25" + "\r\n" + "^FD" + text.substring(47, 94) + "^FS" + "\r\n" +
                    "^FO230," + getXposition() + "" + "\r\n" + "^A0,N,25,25" + "\r\n" + "^FD" + text.substring(94, text.length()) + "^FS" + "\r\n";
        }
        else if (text.length() > 50) {
            s = "^FO230," + xposition + "" + "\r\n" + "^A0,N,25,25" + "\r\n" + "^FD" + text.substring(0, 47) + "^FS" + "\r\n" +
                    "^FO230," + getXposition() + "" + "\r\n" + "^A0,N,25,25" + "\r\n" + "^FD" + text.substring(47, text.length()) + "^FS" + "\r\n";
        } else {
            s = "^FO230," + xposition + "" + "\r\n" + "^A0,N,25,25" + "\r\n" + "^FD" + text + "^FS" + "\r\n";
        }
        return s;
    }
    public int getXposition()
    {
        xposition=xposition+40;
        return xposition;

    }
    private void createfooterReceipt() {
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
        String tmpHeader="";
        int headerHeight =0;
        tmpHeader=   "^XA" +
                "^PON^PW900^MNN^LL%d^LH0,0" + "\r\n" +
                "^FO10,10" + "\r\n" + "^A0,N,35,35" + "\r\n" + "^FD^FS" + "\r\n" +
                "^FO10,10" + "\r\n" + "^GB900,5,5,B,0^FS";
        headerHeight =10;
        String body = String.format("^LH0,%d", headerHeight);
        int heightOfOneLine = 40;
        float totalPrice = 0;
        //long totalBodyHeight = (itemsToPrint.size() + 1) * heightOfOneLine;
        long totalBodyHeight =0;

        long footerStartPosition = headerHeight;

        String footer = String.format("^LH0,%d" + "\r\n" +

                "^FO10,20" + "\r\n" + "^A0,N,30,30" + "\r\n" + "^FDRegistered Office^FS" + "\r\n" +
                "^FO10,60" + "\r\n" + "^A0,N,20,20" + "\r\n" + "^FDAmman Street,New Industrial Area,P.O.Box 2018,Ajman,UAE^FS" + "\r\n" +

                "^FO10,90" + "\r\n" + "^A0,N,20,20" + "\r\n" + "^FDT: +971(0)6 743 8307 F:+971 (0)6 743 7139^FS" + "\r\n" +
                "^FO10,120" + "\r\n" + "^A0,N,20,20" + "\r\n" + "^FDE: sales@brothersgas.ae Website:www.brothersgas.com^FS" + "\r\n" +
                "^FO10,150" + "\r\n" + "^GB900,5,5,B,0^FS"+ "\r\n" +
                "^FO10,160" + "\r\n"  + "^XZ", footerStartPosition, totalPrice);

        long footerHeight = 170;
        xposition=0;
        long labelLength = headerHeight + totalBodyHeight + footerHeight;

        Date date = new Date();
        SimpleDateFormat sdf = new SimpleDateFormat("dd/MM/yyyy");
        String dateString = sdf.format(date);

        String header = String.format(tmpHeader, labelLength,Utils.getNewDate(dateString ));

        String wholeZplLabel = String.format("%s%s%s", header, body, footer);
        try {
            connection.write(wholeZplLabel.getBytes());
        }catch (Exception ex)
        {
            ex.fillInStackTrace();
        }

    }

    private void createLine() {
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
        String tmpHeader="";
        int headerHeight =0;
        tmpHeader=   "^XA" +
                "^PON^PW900^MNN^LL%d^LH0,0" + "\r\n" +
                "^FO10,5" + "\r\n" + "^A0,N,35,35" + "\r\n" + "^FD^FS" + "\r\n" +
                "^FO10,5" + "\r\n" + "^GB900,5,5,B,0^FS";
        headerHeight =5;
        String body = String.format("^LH0,%d", headerHeight);

        float totalPrice = 0;

        long totalBodyHeight =0;

        long footerStartPosition = headerHeight;

        String footer = String.format("^LH0,%d" + "\r\n" +


                "^FO10,00" + "\r\n" + "^GB900,5,5,B,0^FS"+ "\r\n" +
                "^FO10,00" + "\r\n"  + "^XZ", footerStartPosition, totalPrice);

        long footerHeight = 00;
        xposition=0;
        long labelLength = headerHeight + totalBodyHeight + footerHeight;

        Date date = new Date();
        SimpleDateFormat sdf = new SimpleDateFormat("dd/MM/yyyy");
        String dateString = sdf.format(date);

        String header = String.format(tmpHeader, labelLength,Utils.getNewDate(dateString ));

        String wholeZplLabel = String.format("%s%s%s", header, body, footer);
        try {
            connection.write(wholeZplLabel.getBytes());
        }catch (Exception ex)
        {
            ex.fillInStackTrace();
        }

    }

}

