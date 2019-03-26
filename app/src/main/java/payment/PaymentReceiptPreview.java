package payment;

import android.app.Activity;
import android.app.AlertDialog;
import android.app.ProgressDialog;
import android.content.DialogInterface;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.drawable.Drawable;
import android.os.AsyncTask;
import android.os.Bundle;
import android.os.Environment;
import android.os.Looper;
import android.os.PersistableBundle;
import android.provider.MediaStore;
import android.support.annotation.Nullable;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ProgressBar;
import android.widget.ScrollView;
import android.widget.Toast;

import com.brothersgas.R;
import com.itextpdf.text.BaseColor;
import com.itextpdf.text.Chunk;
import com.itextpdf.text.Document;
import com.itextpdf.text.DocumentException;
import com.itextpdf.text.Element;
import com.itextpdf.text.Image;
import com.itextpdf.text.PageSize;
import com.itextpdf.text.Paragraph;
import com.itextpdf.text.pdf.PdfWriter;
import com.itextpdf.text.pdf.draw.LineSeparator;
import com.zebra.sdk.comm.BluetoothConnection;
import com.zebra.sdk.comm.Connection;
import com.zebra.sdk.comm.ConnectionException;
import com.zebra.sdk.comm.TcpConnection;
import com.zebra.sdk.graphics.internal.ZebraImageAndroid;
import com.zebra.sdk.printer.PrinterStatus;
import com.zebra.sdk.printer.SGD;
import com.zebra.sdk.printer.ZebraPrinter;
import com.zebra.sdk.printer.ZebraPrinterFactory;
import com.zebra.sdk.printer.ZebraPrinterLanguageUnknownException;
import com.zebra.sdk.printer.ZebraPrinterLinkOs;

import org.json.JSONArray;
import org.json.JSONObject;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;

import butterknife.BindView;
import butterknife.ButterKnife;
import common.AppController;
import common.Common;
import common.NumberToWords;
import common.ScreenshotUtils;
import common.SettingsHelper;
import common.WebServiceAcess;
import consumption.ConsumptionPreview;
import model.Connection_Disconnection_Invoice_Preview_Model;
import model.PaymentPreviewModel;
import utils.Utils;


/**
 * Created by ashish.kumar on 19-03-2019.
 */

public class PaymentReceiptPreview extends Activity implements View.OnClickListener{
    @BindView(R.id.back_button)
    Button back_button;
    AppController controller;
    WebServiceAcess webServiceAcess;
// public static String invoice="RMRC-U1L1900113";
public static String invoice="";
    NumberToWords numToWords;
    @BindView(R.id.progressBar)
    ProgressBar progress;
    @BindView(R.id.contentView)
    ScrollView contentView;
    @BindView(R.id.mainLayout)
    LinearLayout mainLayout;
    @BindView(R.id. received_from)
    android.widget.TextView received_from;
    @BindView(R.id. receiptno)
    android.widget.TextView receiptno;
    @BindView(R.id.date)
    android.widget.TextView date;
    @BindView(R.id.time)
    android.widget.TextView time;
    @BindView(R.id.address)
    android.widget.TextView address;
    @BindView(R.id.userId)
    android.widget.TextView userId;
    @BindView(R.id.username)
    android.widget.TextView username;
    @BindView(R.id.amount)
    android.widget.TextView  amount;
    @BindView(R.id.amount_in_words)
    android.widget.TextView amount_in_words;
    @BindView(R.id.payment_method)
    android.widget.TextView payment_method;
    @BindView(R.id. chequeView)
    LinearLayout chequeView;
    @BindView(R.id.chequenumber)
    android.widget.TextView chequenumber;
    @BindView(R.id. chequedate)
    android.widget.TextView chequedate;
    @BindView(R.id.bank)
    android.widget.TextView bank;
    @BindView(R.id.invoice_numbers)
    android.widget.TextView invoice_numbers;
    @BindView(R.id. signature)
    ImageView signature;
    public static String imagePath="";
    @BindView(R.id.footer)
    LinearLayout footer;
    @BindView(R.id.progressBar2)
    ProgressBar progressbar2;
    @BindView(R.id.print_email)
    Button print_email;
    int sendAttempt=0;
    private Connection connection;
    PaymentPreviewModel model=null;
    AlertDialog printDialog;
ProgressDialog dialog;
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.payment_receipt_preview);
        ButterKnife.bind(this);
        dialog=new ProgressDialog(PaymentReceiptPreview.this);
        dialog.setMessage("Priniting....");

        webServiceAcess = new WebServiceAcess();
        controller=(AppController)getApplicationContext();
        numToWords=new NumberToWords();
        back_button.setOnClickListener(this);
        print_email.setOnClickListener(this);
        if(Utils.isNetworkAvailable(PaymentReceiptPreview.this)) {
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

            String result = webServiceAcess.runRequest(Common.runAction,Common.PaymentInoviceDetails, new String[]{invoice});
            return result;
        }

        @Override
        protected void onPostExecute(String s) {
            Log.e("value", "onPostExecute: ", null);
            if (s.length() > 0) {
                try {
                    model=new   PaymentPreviewModel(s);
                    if(model.getStatusValue()==2)
                    {
                        setValues(model);
                    }else{
                        Utils.showAlertNormal(PaymentReceiptPreview.this,model.getMessageValue());
                        progress.setVisibility(View.GONE);
                        footer.setVisibility(View.GONE);

                    }
                } catch (Exception ex) {
                    ex.fillInStackTrace();
                    Utils.showAlertNormal(PaymentReceiptPreview.this,"format changed");
                    progress.setVisibility(View.GONE);
                    footer.setVisibility(View.GONE);

                }


            } else {
                Utils.showAlertNormal(PaymentReceiptPreview.this,"Data not available.");
                progress.setVisibility(View.GONE);
                footer.setVisibility(View.GONE);
            }

        }
    }

    public void setValues(PaymentPreviewModel model)
    {

        received_from.setText(model.getCustomerNameValue());
        receiptno.setText(model.getPaymentNumberValue());
        date.setText(Utils.getNewDate(model.getDateValue()));
        time.setText(model.getTimeValue());
        address.setText(model.getCustomerAddressValue());
        userId.setText(model.getUseridValue());
        username.setText(model.getUSERNameValue());
        amount.setText("AED "+model.getAmountValue());
        amount_in_words.setText("AED " + numToWords.convertNumberToWords((int) Math.round(Double.parseDouble(model.getAmountValue()))) + " Only /-");
        payment_method.setText("By " + model.getPaymentTypeValue());
        if (model.getPaymentTypeValue().equalsIgnoreCase("cheque")) {
            chequeView.setVisibility(View.VISIBLE);
            chequenumber.setText(model.getChequeNumberValue());
            chequedate.setText(Utils.getNewDate(model.getChequeDateValue()));
            bank.setText(model.getBankValue());
        } else {
            chequeView.setVisibility(View.GONE);
        }

        invoice_numbers.setText(model.getInvoiceNumbrsValue());
//        Bitmap bitmap = BitmapFactory.decodeFile(imagePath);
//        signature.setImageBitmap(bitmap);
        progress.setVisibility(View.GONE);
        contentView.setVisibility(View.VISIBLE);



    }
    /*-------------------------------------------------------------------block-------------------------------------------------------*/
    public class EmailInvoice extends AsyncTask<String, Void, String> {

        @Override
        protected String doInBackground(String... strings) {


            String result = webServiceAcess.runRequest(Common.runAction,Common.Print_Email, new String[]{receiptno.getText().toString(),"5"});
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
                        Toast.makeText(PaymentReceiptPreview.this,message,Toast.LENGTH_SHORT).show();
                        showPrintAlertDialog();

                    }else{
                        Utils.showAlertNormal(PaymentReceiptPreview.this,message);
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

    public void showPrintAlertDialog() {
        AlertDialog.Builder dialogBuilder = new AlertDialog.Builder(this);
        LayoutInflater inflater = this.getLayoutInflater();
        final View dialogView = inflater.inflate(R.layout.printer_popup, null);
        dialogBuilder.setView(dialogView);

        final EditText edt = (EditText) dialogView.findViewById(R.id.macInput);
        edt.setText(SettingsHelper.getBluetoothAddress(PaymentReceiptPreview.this));
        Button submit=(Button)dialogView.findViewById(R.id.print);
        submit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(edt.getText().toString().length()>0) {
                    dialog.show();;
                    performTest(edt.getText().toString());
                }else{
                    Toast.makeText(PaymentReceiptPreview.this,"Please enter mac address",Toast.LENGTH_SHORT).show();
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

                        Toast.makeText(PaymentReceiptPreview.this, "Printer Ready", Toast.LENGTH_LONG).show();
                        try {
                            connection.open();


                            Bitmap icon = BitmapFactory.decodeResource(getResources(),
                                    R.drawable.brogas_logo);
                            //Bitmap signatureBitmap = Bitmap.createScaledBitmap(signatureArea.getBitmap(), 300, 200, false);
                            Bitmap logo = Bitmap.createScaledBitmap(icon, 300, 200, false);

                            // printer.printImage(new ZebraImageAndroid(signatureBitmap), 0, 0, signatureBitmap.getWidth(), signatureBitmap.getHeight(), false);
                            sendTestLabel();
                           printer.printImage(new ZebraImageAndroid(logo), 0, 0, logo.getWidth(), logo.getHeight(), false);


                            //  printer.sendFileContents("^FT78,76^A0N,28,28^FH_^FDHello_0AWorld^FS");

                            connection.close();
                            Toast.makeText(PaymentReceiptPreview.this, "Receipt Printed Sucessfully.", Toast.LENGTH_LONG).show();
                            dialog.cancel();

                        } catch (ConnectionException e) {
                            dialog.cancel();
                            Utils.showAlertNormal(PaymentReceiptPreview.this,e.getMessage());
                        }

                    }
                });




            } else if (printerStatus.isHeadOpen) {
                Utils.showAlertNormal(PaymentReceiptPreview.this,"Error: Head Open \nPlease Close Printer Head to Print");
                dialog.cancel();
            } else if (printerStatus.isPaused) {
                Utils.showAlertNormal(PaymentReceiptPreview.this,"Error: Printer Paused");
                dialog.cancel();
            } else if (printerStatus.isPaperOut) {
                Utils.showAlertNormal(PaymentReceiptPreview.this,"Error: Media Out \nPlease Load Media to Print");
                dialog.cancel();
            } else {
                Utils.showAlertNormal(PaymentReceiptPreview.this,"Error: Please check the Connection of the Printer");
                dialog.cancel();
            }

            connection.close();
            getAndSaveSettings(macAddress);
            if(printDialog!=null)
            {
                printDialog.cancel();
            }
        } catch (ConnectionException e) {
            Utils.showAlertNormal(PaymentReceiptPreview.this,e.getMessage());
        } catch (ZebraPrinterLanguageUnknownException e) {
            Utils.showAlertNormal(PaymentReceiptPreview.this,e.getMessage());
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

                Toast.makeText(PaymentReceiptPreview.this, displayPrinterLanguage + "\n" + "Language set to ZPL", Toast.LENGTH_LONG).show();

            }
        });

    }

    /**
     * This method saves the entered address of the printer.
     */

    private void getAndSaveSettings(String macAddress) {
        SettingsHelper.saveBluetoothAddress(PaymentReceiptPreview.this, macAddress);

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
        String tmpHeader="";
        int headerHeight =0;
        if(model.getPaymentTypeValue().equalsIgnoreCase("Cash"))
        {

            tmpHeader=   "^XA" +

                    "^PON^PW900^MNN^LL%d^LH0,0" + "\r\n" +

                    // "^FO50,50" + "\r\n" + "^A0,N,50,50" + "\r\n" + "^FD Brothers Gas^FS" + "\r\n" +

                    "^FO50,50" + "\r\n" + "^A0,N,35,35" + "\r\n" + "^FDPayment Receipt^FS" + "\r\n" +

                    "^FO50,100" + "\r\n" + "^A0,N,25,25" + "\r\n" + "^FDReceipt No:^FS" + "\r\n" +

                    "^FO225,100" + "\r\n" + "^A0,N,25,25" + "\r\n" + "^FD"+model.getPaymentNumberValue()+"^FS" + "\r\n" +

                    "^FO50,130" + "\r\n" + "^A0,N,25,25" + "\r\n" + "^FDDate:^FS" + "\r\n" +

                    "^FO225,130" + "\r\n" + "^A0,N,25,25" + "\r\n" + "^FD%s^FS" + "\r\n" +

                    "^FO50,170" + "\r\n" + "^A0,N,25,25" + "\r\n" + "^FDAddress^FS" + "\r\n" +

                    "^FO230,170" + "\r\n" + "^A0,N,25,25" + "\r\n" + "^FD"+model.getCustomerAddressValue()+"^FS" + "\r\n" +

                    "^FO50,200" + "\r\n" + "^A0,N,25,25" + "\r\n" + "^FDMode^FS" + "\r\n" +

                    "^FO230,200" + "\r\n" + "^A0,N,25,25" + "\r\n" + "^FD"+model.getPaymentTypeValue()+"^FS" + "\r\n" +

                    "^FO50,230" + "\r\n" + "^A0,N,25,25" + "\r\n" + "^FDInvoice Number ^FS" + "\r\n" +

                    "^FO230,230" + "\r\n" + "^A0,N,25,25" + "\r\n" + "^FD"+model.getInvoiceNumbrsValue()+"^FS" + "\r\n" +


                    "^FO50,250" + "\r\n" + "^GB350,5,5,B,0^FS";
             headerHeight =260;
        }else{

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

            tmpHeader=   "^XA" +

                            "^PON^PW900^MNN^LL%d^LH0,0" + "\r\n" +

                            // "^FO50,50" + "\r\n" + "^A0,N,50,50" + "\r\n" + "^FD Brothers Gas^FS" + "\r\n" +

                            "^FO50,50" + "\r\n" + "^A0,N,35,35" + "\r\n" + "^FDPayment Receipt^FS" + "\r\n" +

                            "^FO50,100" + "\r\n" + "^A0,N,25,25" + "\r\n" + "^FDReceipt No:^FS" + "\r\n" +

                            "^FO225,100" + "\r\n" + "^A0,N,25,25" + "\r\n" + "^FD"+model.getPaymentNumberValue()+"^FS" + "\r\n" +

                            "^FO50,130" + "\r\n" + "^A0,N,25,25" + "\r\n" + "^FDDate:^FS" + "\r\n" +

                            "^FO225,130" + "\r\n" + "^A0,N,25,25" + "\r\n" + "^FD%s^FS" + "\r\n" +

                            "^FO50,170" + "\r\n" + "^A0,N,25,25" + "\r\n" + "^FDAddress^FS" + "\r\n" +

                            "^FO230,170" + "\r\n" + "^A0,N,25,25" + "\r\n" + "^FD"+model.getCustomerAddressValue()+"^FS" + "\r\n" +

                            "^FO50,200" + "\r\n" + "^A0,N,25,25" + "\r\n" + "^FDMode^FS" + "\r\n" +

                            "^FO230,200" + "\r\n" + "^A0,N,25,25" + "\r\n" + "^FD"+model.getPaymentTypeValue()+"^FS" + "\r\n" +
                            "^FO50,230" + "\r\n" + "^A0,N,25,25" + "\r\n" + "^FDCheque Number^FS" + "\r\n" +

                            "^FO230,230" + "\r\n" + "^A0,N,25,25" + "\r\n" + "^FD"+model.getChequeNumberValue()+"^FS" + "\r\n" +
                            "^FO50,260" + "\r\n" + "^A0,N,25,25" + "\r\n" + "^FDBank^FS" + "\r\n" +

                            "^FO230,260" + "\r\n" + "^A0,N,25,25" + "\r\n" + "^FD"+model.getBankValue()+"^FS" + "\r\n" +
                            "^FO50,290" + "\r\n" + "^A0,N,25,25" + "\r\n" + "^FDInvoice Number ^FS" + "\r\n" +

                            "^FO230,290" + "\r\n" + "^A0,N,25,25" + "\r\n" + "^FD"+model.getInvoiceNumbrsValue()+"^FS" + "\r\n" +


                            "^FO50,310" + "\r\n" + "^GB350,5,5,B,0^FS";

            headerHeight =320;
        }


        String body = String.format("^LH0,%d", headerHeight);

        int heightOfOneLine = 40;

        float totalPrice = 0;

        // Map<String, String> itemsToPrint = createListOfItems();

        int i = 0;
//        for (String productName : itemsToPrint.keySet()) {
//            String price = itemsToPrint.get(productName);
//
//            String lineItem = "^FO50,%d" + "\r\n" + "^A0,N,28,28" + "\r\n" + "^FD%s^FS" + "\r\n" + "^FO280,%d" + "\r\n" + "^A0,N,28,28" + "\r\n" + "^FD$%s^FS";
//            totalPrice += Float.parseFloat(price);
//            int totalHeight = i++ * heightOfOneLine;
//            body += String.format(lineItem, totalHeight, productName, totalHeight, price);
//
//        }

        // long totalBodyHeight = (itemsToPrint.size() + 1) * heightOfOneLine;
        long totalBodyHeight =0;

        long footerStartPosition = headerHeight;

        String footer = String.format("^LH0,%d" + "\r\n" +

                "^FO50,15" + "\r\n" + "^A0,N,30,30" + "\r\n" + "^FDTotal^FS" + "\r\n" +

                "^FO230,15" + "\r\n" + "^A0,N,30,30" + "\r\n" + "^FDAED "+model.getAmountValue()+"^FS" + "\r\n" +
                "^FO50,40" + "\r\n" + "^GB350,5,5,B,0^FS"+

                "^FO50,70" + "\r\n" + "^A0,N,25,25" + "\r\n" + "^FDAmount(Words)^FS" + "\r\n" +

                "^FO230,70" + "\r\n" + "^A0,N,25,25" + "\r\n" + "^FDAED "+numToWords.convertNumberToWords((int) Math.round(Double.parseDouble(model.getAmountValue())))+"/-^FS" + "\r\n" +
                "^FO50,100" + "\r\n" + "^GB280,5,5,B,0^FS"+

                "^FO50,120" + "\r\n" + "^A0,N,25,25" + "\r\n" + "^FDThanks for choosing Brothers Gas!^FS" + "\r\n" +

                "^FO50,150" + "\r\n"  + "^XZ", footerStartPosition, totalPrice);

        long footerHeight = 200;
        long labelLength = headerHeight + totalBodyHeight + footerHeight;

        Date date = new Date();
        SimpleDateFormat sdf = new SimpleDateFormat("dd/MM/yyyy");
        String dateString = sdf.format(date);

        String header = String.format(tmpHeader, labelLength,Utils.getNewDate(model.getDateValue()));

        String wholeZplLabel = String.format("%s%s%s", header, body, footer);

        return wholeZplLabel;
    }



}

