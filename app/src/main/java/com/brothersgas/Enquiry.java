package com.brothersgas;

import android.app.Activity;
import android.app.AlertDialog;
import android.app.DatePickerDialog;
import android.app.Dialog;
import android.app.ProgressDialog;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.AsyncTask;
import android.os.Bundle;
import android.os.Looper;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ProgressBar;
import android.widget.Toast;

import com.itextpdf.text.pdf.PdfPRow;
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

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;

import butterknife.BindView;
import butterknife.ButterKnife;
import common.AppController;
import common.Common;

import common.NumberToWords;
import common.SettingsHelper;
import common.WebServiceAcess;
import consumption.Consumption;
import invoices.Consumption_DeliveryNote_Preview;
import model.EnquiryModel;
import model.PaymentPreviewModel;


import payment.PaymentReceiptPreview;
import utils.Utils;

/**
 * Created by ashish.kumar on 28-02-2019.
 */

public class Enquiry extends Activity implements View.OnClickListener {

    WebServiceAcess webServiceAcess;
    @BindView(R.id.mainLayout)
    LinearLayout mainLayout;
    @BindView(R.id.progressBar)
    ProgressBar progressBar;
    @BindView(R.id.s_date)
    Button startDate;
    @BindView(R.id.e_date)
    Button endDate;
    @BindView(R.id.search)
    Button search;
    @BindView(R.id.done)
    Button ok;
    @BindView(R.id.print)
    Button print;
    AppController controller;
    @BindView(R.id.back_button)
    Button back_button;
    @BindView(R.id.generated_invoice_count)
    android.widget.TextView generatedInvoicecount;
    @BindView(R.id.number_cash_payment)
    android.widget.TextView number_cash_payment;
    @BindView(R.id.total_cash_amount_collected)
    android.widget.TextView total_cash_amount_collected;
    @BindView(R.id.chequepayment_count)
    android.widget.TextView chequepayment_count;
    @BindView(R.id.total_cheaqueamout)
    android.widget.TextView total_cheaqueamout;
    @BindView(R.id.total_cheaque_cash_amount)
    android.widget.TextView total_cheaque_cash_amount;
    @BindView(R.id.dep_conn_dconn)
    android.widget.TextView dep_conn_dconn;
    @BindView(R.id.total_dep_inv_amount)
    android.widget.TextView total_dep_inv_amount;
    @BindView(R.id.total_con_dconn_amount)
    android.widget.TextView total_con_dconn_amount;
    @BindView(R.id.teenant_change)
    android.widget.TextView teenant_Change;
    private DatePicker datePicker;
    private Calendar calendar;
    NumberToWords numToWords;

    private int year, month, day;

    int selectedOption;
    int startDateSelcted = 1, endDateSelected = 2;
    boolean startDateSlected = false;
    boolean endDateSlected = false;
    EnquiryModel model = null;
    private Connection connection;
    AlertDialog printDialog;
    ProgressDialog dialog;

    int attemptCount=0;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.enquiry);
        //getSupportActionBar().setTitle("");
        // Add titleTextView into ActionBar
        ButterKnife.bind(this);
        dialog = new ProgressDialog(Enquiry.this);
        dialog.setMessage("Priniting....");
        webServiceAcess = new WebServiceAcess();
        controller = (AppController) getApplicationContext();
        back_button.setOnClickListener(this);
        ok.setOnClickListener(this);
        startDate.setOnClickListener(this);
        endDate.setOnClickListener(this);
        search.setOnClickListener(this);
        print.setOnClickListener(this);
        calendar = Calendar.getInstance();
        year = calendar.get(Calendar.YEAR);
        month = calendar.get(Calendar.MONTH);
        day = calendar.get(Calendar.DAY_OF_MONTH);
        numToWords = new NumberToWords();

    }

    public long getMinDate(String datee) {
        SimpleDateFormat sdf = new SimpleDateFormat("dd/MM/yyyy");

        try {
            //formatting the dateString to convert it into a Date
            Date date = sdf.parse(datee);
            System.out.println("Given Time in milliseconds : " + date.getTime());

            Calendar calendar = Calendar.getInstance();
            //Setting the Calendar date and time to the given date and time
            calendar.setTime(date);
            System.out.println("Given Time in milliseconds : " + calendar.getTimeInMillis());
            return calendar.getTimeInMillis();
        } catch (ParseException e) {
            e.printStackTrace();
        }
        return 0;
    }

    @Override
    protected Dialog onCreateDialog(int id) {
        // TODO Auto-generated method stub
        if (id == 999) {
            DatePickerDialog dialog = new DatePickerDialog(this, myDateListener, year, month, day);

            dialog.getDatePicker().setMaxDate(Calendar.getInstance().getTimeInMillis());
            return dialog;
        } else if (id == 991) {
            DatePickerDialog dialog = new DatePickerDialog(this, myDateListener2, year, month, day);

            //dialog.getDatePicker().setMinDate(getMinDate(startDate.getText().toString()));
            dialog.getDatePicker().setMaxDate(Calendar.getInstance().getTimeInMillis());

            return dialog;

        }
        return null;
    }

    private DatePickerDialog.OnDateSetListener myDateListener = new
            DatePickerDialog.OnDateSetListener() {
                @Override
                public void onDateSet(DatePicker arg0,
                                      int arg1, int arg2, int arg3) {
                    // TODO Auto-generated method stub
                    // arg1 = year
                    // arg2 = month
                    // arg3 = day
                    showDate(arg1, arg2 + 1, arg3);
                }
            };

    private DatePickerDialog.OnDateSetListener myDateListener2 = new
            DatePickerDialog.OnDateSetListener() {
                @Override
                public void onDateSet(DatePicker arg0,
                                      int arg1, int arg2, int arg3) {
                    // TODO Auto-generated method stub
                    // arg1 = year
                    // arg2 = month
                    // arg3 = day
                    showDate(arg1, arg2 + 1, arg3);
                }
            };

    private void showDate(int year, int month, int day) {
        String dayValue = Integer.toString(day);
        String monthValue = Integer.toString(month);
        if (month < 10) {
            monthValue = "0" + month;
        }
        if (day < 10) {
            dayValue = "0" + day;
        }
        switch (selectedOption) {
            case 1:
                if (getMinDate(new StringBuilder().append(dayValue).append("/")
                        .append(monthValue).append("/").append(year).toString()) <= Calendar.getInstance().getTimeInMillis()) {
                    startDate.setText(new StringBuilder().append(dayValue).append("/")
                            .append(monthValue).append("/").append(year));
                    startDateSlected = true;
                } else {
                    Utils.showAlertNormal(Enquiry.this, "You cannot select future date");
                }

                break;
            case 2:
                if (getMinDate(new StringBuilder().append(dayValue).append("/")
                        .append(monthValue).append("/").append(year).toString()) <= Calendar.getInstance().getTimeInMillis()) {
                    if (getMinDate(new StringBuilder().append(dayValue).append("/")
                            .append(monthValue).append("/").append(year).toString()) >= getMinDate(startDate.getText().toString())) {
                        endDate.setText(new StringBuilder().append(dayValue).append("/").append(monthValue).append("/").append(year));
                        endDateSlected = true;
                    } else {
                        Utils.showAlertNormal(Enquiry.this, "End date should be greater than or equal to start date");
                    }
                } else {
                    Utils.showAlertNormal(Enquiry.this, "You cannot select future date");
                }

                break;
        }

    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.done:
            case R.id.back_button:
                finish();
                break;
            case R.id.print:
                showPrintAlertDialog();
                break;
            case R.id.s_date:
                clearValue();
                selectedOption = startDateSelcted;
                showDialog(999);
                clearValue();
                break;
            case R.id.e_date:
                if (startDate.getText().length() > 0) {
                    selectedOption = endDateSelected;
                    showDialog(991);
                    clearValue();
                } else {
                    Utils.showAlertNormal(Enquiry.this, "Please select start date first");
                }
                break;
            case R.id.search:
                if ((startDateSlected == true) && (endDateSlected == true)) {
                    if (Utils.getMinDate(startDate.getText().toString()) <= Utils.getMinDate(endDate.getText().toString())) {
                        new GetEnquiryData().execute();
                    } else {
                        Utils.showAlertNormal(Enquiry.this, "End date must be greater than start date");
                    }
                } else {
                    if (startDateSlected == false) {
                        Toast.makeText(Enquiry.this, "Please Select start date", Toast.LENGTH_SHORT).show();
                    } else {
                        Toast.makeText(Enquiry.this, "Please Select end date", Toast.LENGTH_SHORT).show();
                    }
                }

                break;
        }

    }

    /*-------------------------------------------------------------------getContractDetailsData-------------------------------------------------------*/
    public class GetEnquiryData extends AsyncTask<String, Void, String> {
        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            progressBar.setVisibility(View.VISIBLE);
        }

        @Override
        protected String doInBackground(String... strings) {
            String result = webServiceAcess.runRequest(Common.runAction, Common.Enqiry, new String[]{controller.getManager().getLoggedInUserName(), Utils.getFormatted(startDate.getText().toString()), Utils.getFormatted(endDate.getText().toString())});
            return result;
        }

        @Override
        protected void onPostExecute(String s) {
            Log.e("value", "onPostExecute: ", null);
            if (s.length() > 0) {
                try {
                    JSONObject jsonObject = new JSONObject(s);
                    JSONObject result = jsonObject.getJSONObject("RESULT");
                    JSONObject groupJSON = result.getJSONObject("GRP");
                    model = new EnquiryModel(groupJSON.getJSONArray("FLD"));
                    setValue();
                } catch (Exception ex) {
                    ex.fillInStackTrace();
                }
                progressBar.setVisibility(View.GONE);
            } else {
                progressBar.setVisibility(View.GONE);

                Toast.makeText(Enquiry.this, "Data not found", Toast.LENGTH_SHORT).show();
            }
        }


    }

    public void setValue() {
        if (model != null) {

            generatedInvoicecount.setText(model.getNumber_of_invoices());
            number_cash_payment.setText(model.getNumber_of_Cash_Payments());
            total_cash_amount_collected.setText("AED "+model.getTotal_Cash_payments_amount());
            chequepayment_count.setText(model.getNumber_of_Cheque_Payments());
            total_cheaqueamout.setText("AED "+model.getTotal_Cheque_payments_amount());
            total_cheaque_cash_amount.setText("AED "+model.getTotal_Cheque_and_cheque_payments_amount());
            dep_conn_dconn.setText(model.getNumber_of_disconnection_invoices());
            teenant_Change.setText(model.getNumber_of_Connections());
            total_dep_inv_amount.setText("AED "+model.getTotal_Deposite_Invoices_amount());
            total_con_dconn_amount.setText("AED "+model.getTotal_Disconnection_Invoices_amount());
        }
        mainLayout.setVisibility(View.VISIBLE);
    }

    public void clearValue() {


        generatedInvoicecount.setText("");
        number_cash_payment.setText("");
        total_cash_amount_collected.setText("");
        chequepayment_count.setText("");
        total_cheaqueamout.setText("");
        total_cheaque_cash_amount.setText("");
        dep_conn_dconn.setText("");
        teenant_Change.setText("");
        total_dep_inv_amount.setText("");
        total_con_dconn_amount.setText("");

        mainLayout.setVisibility(View.GONE);
    }


    /**
     * This method saves the entered address of the printer.
     */


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

        edt.setText(SettingsHelper.getBluetoothAddress(Enquiry.this));
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
                if (edt.getText().toString().length() > 0) {
                    attemptCount=attemptCount+1;
                    print.setText("Reprint");
                    dialog.show();
                    performTest(edt.getText().toString());
                } else {
                    Toast.makeText(Enquiry.this, "Please enter mac address", Toast.LENGTH_SHORT).show();
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

    public void showAlert() {

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

                      //  Toast.makeText(Enquiry.this, "Printer Ready", Toast.LENGTH_LONG).show();
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
                            Toast.makeText(Enquiry.this, "Receipt Printed Sucessfully.", Toast.LENGTH_LONG).show();
                            dialog.cancel();

                        } catch (ConnectionException e) {
                            dialog.cancel();
                            Utils.showAlertNormal(Enquiry.this, e.getMessage());
                        }

                    }
                });


            } else if (printerStatus.isHeadOpen) {
                Utils.showAlertNormal(Enquiry.this, "Error: Head Open \nPlease Close Printer Head to Print");
                dialog.cancel();
            } else if (printerStatus.isPaused) {
                Utils.showAlertNormal(Enquiry.this, "Error: Printer Paused");
                dialog.cancel();
            } else if (printerStatus.isPaperOut) {
                Utils.showAlertNormal(Enquiry.this, "Error: Media Out \nPlease Load Media to Print");
                dialog.cancel();
            } else {
                Utils.showAlertNormal(Enquiry.this, "Error: Please check the Connection of the Printer");
                dialog.cancel();
            }

            connection.close();
            getAndSaveSettings(macAddress);
            if (printDialog != null) {
                printDialog.cancel();
            }
        } catch (ConnectionException e) {
            Utils.showAlertNormal(Enquiry.this, e.getMessage());
        } catch (ZebraPrinterLanguageUnknownException e) {
            Utils.showAlertNormal(Enquiry.this, e.getMessage());
        } finally {

        }

    }

    /* * This method implements the best practices to check the language of the printer and set the language of the printer to ZPL.
     *
     * @throws ConnectionException
     */
    private void getPrinterStatus() throws ConnectionException {


        final String printerLanguage = SGD.GET("device.languages", connection); //This command is used to get the language of the printer.

        final String displayPrinterLanguage = "Printer Language is " + printerLanguage;

        SGD.SET("device.languages", "zpl", connection); //This command set the language of the printer to ZPL

        runOnUiThread(new Runnable() {
            @Override
            public void run() {

                Toast.makeText(Enquiry.this, displayPrinterLanguage + "\n" + "Language set to ZPL", Toast.LENGTH_LONG).show();

            }
        });

    }

    /**
     * This method saves the entered address of the printer.
     */

    private void getAndSaveSettings(String macAddress) {
        SettingsHelper.saveBluetoothAddress(Enquiry.this, macAddress);

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
        String tmpHeader = "";
        int headerHeight = 0;

        tmpHeader = "^XA" +

                "^PON^PW900^MNN^LL%d^LH0,0" + "\r\n" +

                // "^FO50,50" + "\r\n" + "^A0,N,50,50" + "\r\n" + "^FD Brothers Gas^FS" + "\r\n" +

                "^FO20,00" + "\r\n" + "^A0,N,35,35" + "\r\n" + "^FDEnquiry("+startDate.getText().toString()+" - "+endDate.getText().toString()+")^FS" + "\r\n" +
                "^FO20,60" + "\r\n" + "^GB500,5,5,B,0^FS" + "\r\n" +

                "^FO20,100" + "\r\n" + "^A0,N,25,25" + "\r\n" + "^FDGenerated Invoices :^FS" + "\r\n" +

                "^FO380,100" + "\r\n" + "^A0,N,25,25" + "\r\n" + "^FD" + model.getNumber_of_invoices() + "^FS" + "\r\n" +

                "^FO20,140" + "\r\n" + "^A0,N,25,25" + "\r\n" + "^FDCash Payment :^FS" + "\r\n" +

                "^FO380,140" + "\r\n" + "^A0,N,25,25" + "\r\n" + "^FD" + model.getNumber_of_Cash_Payments() + "^FS" + "\r\n" +

                "^FO20,180" + "\r\n" + "^A0,N,25,25" + "\r\n" + "^FDCash Amount Collected : ^FS" + "\r\n" +

                "^FO380,180" + "\r\n" + "^A0,N,25,25" + "\r\n" + "^FDAED " + model.getTotal_Cash_payments_amount() + "^FS" + "\r\n" +

                "^FO20,220" + "\r\n" + "^A0,N,25,25" + "\r\n" + "^FDCheque Payments : ^FS" + "\r\n" +

                "^FO380,220" + "\r\n" + "^A0,N,25,25" + "\r\n" + "^FD" + model.getNumber_of_Cheque_Payments() + "^FS" + "\r\n" +
                "^FO20,260" + "\r\n" + "^A0,N,25,25" + "\r\n" + "^FDTotal Cheque Amount : ^FS" + "\r\n" +

                "^FO380,260" + "\r\n" + "^A0,N,25,25" + "\r\n" + "^FDAED " +model.getTotal_Cheque_payments_amount() + "^FS" + "\r\n" +


                "^FO20,300" + "\r\n" + "^A0,N,25,25" + "\r\n" + "^FDTotal (Cash+Cheque) Amount : ^FS" + "\r\n" +

                "^FO380,300" + "\r\n" + "^A0,N,25,25" + "\r\n" + "^FDAED " + model.getTotal_Cheque_and_cheque_payments_amount() + "^FS" + "\r\n" +
                "^FO20,340" + "\r\n" + "^GB500,5,5,B,0^FS" + "\r\n" +

                "^FO200,380" + "\r\n" + "^A0,N,25,25" + "\r\n" + "^FDNumber of New Connections ^FS" + "\r\n" +

                "^FO380,380" + "\r\n" + "^A0,N,25,25" + "\r\n" + "^FD^FS" + "\r\n" +

                "^FO20,420" + "\r\n" + "^A0,N,25,25" + "\r\n" + "^FDTenant Change : ^FS" + "\r\n" +

                "^FO380,420" + "\r\n" + "^A0,N,25,25" + "\r\n" + "^FD" + model.getNumber_of_Connections() + "^FS" + "\r\n" +


                "^FO20,460" + "\r\n" + "^A0,N,25,25" + "\r\n" + "^FDDeposit_Conn_DisConn. : ^FS" + "\r\n" +

                "^FO380,460" + "\r\n" + "^A0,N,25,25" + "\r\n" + "^FD" + model.getNumber_of_disconnection_invoices() + "^FS" + "\r\n" +

                "^FO20,500" + "\r\n" + "^GB500,5,5,B,0^FS" + "\r\n" +

                "^FO20,540" + "\r\n" + "^A0,N,25,25" + "\r\n" + "^FDTotal Dep.Invoice Amount : ^FS" + "\r\n" +

                "^FO380,540" + "\r\n" + "^A0,N,25,25" + "\r\n" + "^FDAED " + model.getTotal_Deposite_Invoices_amount() + "^FS" + "\r\n" +
                "^FO20,580" + "\r\n" + "^A0,N,25,25" + "\r\n" + "^FDConn_DisConn.Invoice Amount : ^FS" + "\r\n" +

                "^FO380,580" + "\r\n" + "^A0,N,25,25" + "\r\n" + "^FDAED " + model.getTotal_Disconnection_Invoices_amount() + "^FS" + "\r\n" +


                "^FO20,620" + "\r\n" + "^GB500,5,5,B,0^FS";

        headerHeight = 630;

        String body = String.format("^LH0,%d", headerHeight);


        float totalPrice = 0;


        long totalBodyHeight = 0;

        long footerStartPosition = headerHeight;

        String footer = String.format("^LH0,%d" + "\r\n" +

                "^FO20,20" + "\r\n" + "^A0,N,30,30" + "\r\n" + "^FDThanks for choosing Brothers Gas!^FS" + "\r\n" +

                "^FO20,60" + "\r\n" + "^XZ", footerStartPosition, totalPrice);

        long footerHeight = 250;
        long labelLength = headerHeight + totalBodyHeight + footerHeight;

        Date date = new Date();
        SimpleDateFormat sdf = new SimpleDateFormat("dd/MM/yyyy");
        String dateString = sdf.format(date);

        String header = String.format(tmpHeader, labelLength, dateString);

        String wholeZplLabel = String.format("%s%s%s", header, body, footer);

        return wholeZplLabel;
    }



}

