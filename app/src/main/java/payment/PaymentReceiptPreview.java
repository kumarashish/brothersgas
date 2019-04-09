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
import invoices.Consumption_DeliveryNote_Preview;
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
  //public static String invoice="RMRC-U1L1900191";
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
    int xposition=0;
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.payment_receipt_preview);
        ButterKnife.bind(this);
        dialog=new ProgressDialog(PaymentReceiptPreview.this);
        dialog.setMessage("Printing....");

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

                if(sendAttempt>1)
                {
                    showPrintAlertDialog();
                }else {
                    progressbar2.setVisibility(View.VISIBLE);
                    footer.setVisibility(View.GONE);
                    new EmailInvoice().execute();

                }

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

                Utils.showAlertNormal(PaymentReceiptPreview.this,Common.message);
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
        amount_in_words.setText( getNumberToWords(model.getAmountValue()) );
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
                            print_email.setText("Reprint");
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
                Utils.showAlertNormal(PaymentReceiptPreview.this,Common.message);
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

        edt.setText(SettingsHelper.getBluetoothAddress(PaymentReceiptPreview.this));
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
                            Bitmap logo = Bitmap.createScaledBitmap(icon, 300, 200, false);
                            createfooterReceipt();
                            sendTestLabel();
                            printer.printImage(new ZebraImageAndroid(logo), 0, 0, logo.getWidth(), logo.getHeight(), false);
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

                    "^FO10,00" + "\r\n" + "^A0,N,35,35" + "\r\n" + "^FDPayment Receipt^FS" + "\r\n" +
                    "^FO10,"+getXposition()+"" + "\r\n" + "^GB900,5,5,B,0^FS"+ "\r\n" +
                    "^FO10,"+getXposition()+"" + "\r\n" + "^A0,N,25,25" + "\r\n" + "^FDReceipt No:^FS" + "\r\n" +
                    "^FO260,"+xposition+"" + "\r\n" + "^A0,N,25,25" + "\r\n" + "^FD"+model.getPaymentNumberValue()+"^FS" + "\r\n" +
                    "^FO10,"+getXposition()+"" + "\r\n" + "^A0,N,25,25" + "\r\n" + "^FDDate:^FS" + "\r\n" +
                    "^FO260,"+xposition+"" + "\r\n" + "^A0,N,25,25" + "\r\n" + "^FD%s^FS" + "\r\n" +
                    "^FO10,"+getXposition()+"" + "\r\n" + "^A0,N,25,25" + "\r\n" + "^FDAddress^FS" + "\r\n" +
                    getFormattedText(model.getCustomerAddressValue())+ "\r\n" +
                    "^FO10,"+getXposition()+"" + "\r\n" + "^A0,N,25,25" + "\r\n" + "^FDMode^FS" + "\r\n" +
                    "^FO260,"+xposition+"" + "\r\n" + "^A0,N,25,25" + "\r\n" + "^FD"+model.getPaymentTypeValue()+"^FS" + "\r\n" +
                    "^FO10,"+getXposition()+"" + "\r\n" + "^A0,N,25,25" + "\r\n" + "^FDInvoice Number ^FS" + "\r\n" +
                    "^FO260,"+xposition+"" + "\r\n" + "^A0,N,25,25" + "\r\n" + "^FD"+model.getInvoiceNumbrsValue()+"^FS" + "\r\n" +
                    "^FO10,"+getXposition()+"" + "\r\n" + "^GB900,5,5,B,0^FS";
             headerHeight =getXposition();
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


            xposition=0;
         tmpHeader=   "^XA" +

                            "^PON^PW900^MNN^LL%d^LH0,0" + "\r\n" +

                            // "^FO50,50" + "\r\n" + "^A0,N,50,50" + "\r\n" + "^FD Brothers Gas^FS" + "\r\n" +

                 "^FO10,00" + "\r\n" + "^A0,N,35,35" + "\r\n" + "^FDPayment Receipt^FS" + "\r\n" +
                 "^FO10,"+getXposition()+"" + "\r\n" + "^GB900,5,5,B,0^FS"+ "\r\n" +
                 "^FO10,"+getXposition()+"" + "\r\n" + "^A0,N,25,25" + "\r\n" + "^FDReceipt No:^FS" + "\r\n" +
                 "^FO260,"+xposition+"" + "\r\n" + "^A0,N,25,25" + "\r\n" + "^FD"+model.getPaymentNumberValue()+"^FS" + "\r\n" +
                 "^FO10,"+getXposition()+"" + "\r\n" + "^A0,N,25,25" + "\r\n" + "^FDDate:^FS" + "\r\n" +
                 "^FO260,"+xposition+"" + "\r\n" + "^A0,N,25,25" + "\r\n" + "^FD%s^FS" + "\r\n" +
                 "^FO10,"+getXposition()+"" + "\r\n" + "^A0,N,25,25" + "\r\n" + "^FDAddress^FS" + "\r\n" +
                  getFormattedText(model.getCustomerAddressValue())+ "\r\n" +
                 "^FO10,"+getXposition()+"" + "\r\n" + "^A0,N,25,25" + "\r\n" + "^FDMode^FS" + "\r\n" +
                 "^FO260,"+xposition+"" + "\r\n" + "^A0,N,25,25" + "\r\n" + "^FD"+model.getPaymentTypeValue()+"^FS" + "\r\n" +

                            "^FO10,"+getXposition()+"" + "\r\n" + "^A0,N,25,25" + "\r\n" + "^FDCheque Number^FS" + "\r\n" +

                            "^FO260,"+xposition+"" + "\r\n" + "^A0,N,25,25" + "\r\n" + "^FD"+model.getChequeNumberValue()+"^FS" + "\r\n" +
                            "^FO10,"+getXposition()+"" + "\r\n" + "^A0,N,25,25" + "\r\n" + "^FDBank^FS" + "\r\n" +
                             "^FO260," + xposition + "" + "\r\n" + "^A0,N,25,25" + "\r\n" + "^FD" + model.getBankValue() + "^FS" + "\r\n" +
                            "^FO10,"+getXposition()+"" + "\r\n" + "^A0,N,25,25" + "\r\n" + "^FDInvoice Number ^FS" + "\r\n" +

                            "^FO260,"+xposition+"" + "\r\n" + "^A0,N,25,25" + "\r\n" + "^FD"+model.getInvoiceNumbrsValue()+"^FS" + "\r\n" +

                            "^FO10,"+getXposition()+"" + "\r\n" + "^GB500,5,5,B,0^FS";

            headerHeight =getXposition();
        }


        String body = String.format("^LH0,%d", headerHeight);

        int heightOfOneLine = 40;

        float totalPrice = 0;



        //long totalBodyHeight = (itemsToPrint.size() + 1) * heightOfOneLine;
        long totalBodyHeight =0;
       xposition=20;
        long footerStartPosition = headerHeight;

        String footer = String.format("^LH0,%d" + "\r\n" +

                "^FO10,"+xposition+"" + "\r\n" + "^A0,N,30,30" + "\r\n" + "^FDTotal^FS" + "\r\n" +

                "^FO260,"+xposition+"" + "\r\n" + "^A0,N,30,30" + "\r\n" + "^FDAED "+model.getAmountValue()+"^FS" + "\r\n" +
                "^FO10,"+getXposition()+"" + "\r\n" + "^GB900,5,5,B,0^FS"+

                "^FO10,"+getXposition()+"" + "\r\n" + "^A0,N,25,25" + "\r\n" + "^FDAmount(Words)^FS" + "\r\n" +
                getFormattedText(getNumberToWords(model.getAmountValue()))+ "\r\n" +
                "^FO10,"+getXposition()+"" + "\r\n" + "^GB900,5,5,B,0^FS"+
                "^FO10,"+getXposition()+"" + "\r\n" + "^A0,N,25,25" + "\r\n" + "^FDThis is computer generated document does not require signature^FS" + "\r\n" +
                "^FO10,"+getXposition()+"" + "\r\n" + "^A0,N,25,25" + "\r\n" + "^FD^FS" + "\r\n" +
                "^FO10,"+getXposition()+"" + "\r\n"  + "^XZ", footerStartPosition, totalPrice);

        long footerHeight = 240;
        xposition=0;
        long labelLength = headerHeight + totalBodyHeight + footerHeight;

        Date date = new Date();
        SimpleDateFormat sdf = new SimpleDateFormat("dd/MM/yyyy");
        String dateString = sdf.format(date);

        String header = String.format(tmpHeader, labelLength,Utils.getNewDate(model.getDateValue()));

       String wholeZplLabel = String.format("%s%s%s", header, body, footer);

        return wholeZplLabel;
    }

    public String getFormattedText(String text) {
        String s = "";
        if (text.length() > 50) {
            s = "^FO260," + xposition + "" + "\r\n" + "^A0,N,25,25" + "\r\n" + "^FD" + text.substring(0, 47) + "^FS" + "\r\n" +
                    "^FO260," + getXposition() + "" + "\r\n" + "^A0,N,25,25" + "\r\n" + "^FD" + text.substring(47, text.length()) + "^FS" + "\r\n";
        } else {
            s = "^FO260," + xposition + "" + "\r\n" + "^A0,N,25,25" + "\r\n" + "^FD" + text + "^FS" + "\r\n";
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

        String header = String.format(tmpHeader, labelLength,Utils.getNewDate(model.getDateValue()));

        String wholeZplLabel = String.format("%s%s%s", header, body, footer);
        try {
            connection.write(wholeZplLabel.getBytes());
        }catch (Exception ex)
        {
            ex.fillInStackTrace();
        }

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

