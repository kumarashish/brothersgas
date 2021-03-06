package com.brothersgas;

import android.app.Activity;
import android.app.AlertDialog;
import android.app.Dialog;
import android.app.ProgressDialog;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.AsyncTask;
import android.os.Bundle;
import android.os.Looper;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.AutoCompleteTextView;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.PopupMenu;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

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

import activatecontract.ContractListForActivation;
import activatecontract.Dashboard2;
import activatecontract.NewContractList;
import butterknife.BindView;
import butterknife.ButterKnife;
import common.AppController;
import common.Common;
import common.SettingsHelper;
import common.WebServiceAcess;
import consumption.Consumption;
import consumption.ConsumptionList;
import contracts.ContractDetails;
import contracts.Contracts;
import invoices.Block_Cancel;
import invoices.Connection_Disconnection_Invoice;
import payment.InvoiceList;
import utils.Utils;

/**
 * Created by ashish.kumar on 23-01-2019.
 */

public class DashBoard extends Activity implements View.OnClickListener {

@BindView(R.id.menu)
ImageView menu;
@BindView(R.id.contract)
View contract;
    @BindView(R.id.consumption)
    View consumption;
    @BindView(R.id.payment)
    View payment;
    @BindView(R.id.con_discon_invoice)
    View connect_disconnection_invoice;
    @BindView(R.id.block_cancel)
    View block_cancel;
    @BindView(R.id.enquiry)
            View enquiry;

    AppController controller;
    Dialog    printDialog,syncDialog;
    private Connection connection;
    ProgressDialog dialog;
    WebServiceAcess webServiceAcess;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_dashboard);
        controller=(AppController)getApplicationContext();
        webServiceAcess=new WebServiceAcess();
        ButterKnife.bind(this);
        menu.setOnClickListener(this);
        contract.setOnClickListener(this);
        consumption.setOnClickListener(this);
        block_cancel.setOnClickListener(this);
        enquiry.setOnClickListener(this);

        connect_disconnection_invoice.setOnClickListener(this);
        payment.setOnClickListener(this);
        dialog = new ProgressDialog(DashBoard.this);
        if(controller.getManager().getLastSyncDate().length()==0)
        {
            showSyncAlertDialog();
        }

    }

    @Override
    public void onClick(View v) {
        switch (v.getId())
        {
            case R.id.menu:
                popupMenu();
                break;
            case R.id.contract:
                if((controller.getUserRole().getContractsAcess()==2)||(controller.getUserRole().getAdminAcess()==2)) {
                    if(controller.getManager().getLastSyncDate().length()==0)
                    {
                        showSyncAlertDialog();
                    }else {
                        Intent in = new Intent(DashBoard.this, ProjectSearch.class);
                        in.putExtra("RequestedScreen", 1);
                        startActivity(in);
                    }
                    //startActivity(new Intent(DashBoard.this, Dashboard2.class));
                }else{
                    Utils.showAlertNormal(DashBoard.this,"You are not authorized to use this feature");
                }
                break;
            case R.id.con_discon_invoice:
                if((controller.getUserRole().getConnection_DisconnectionAcess()==2)||(controller.getUserRole().getAdminAcess()==2)) {
                    //startActivity(new Intent(DashBoard.this, Connection_Disconnection_Invoice.class));
                    if(controller.getManager().getLastSyncDate().length()==0)
                    {
                        showSyncAlertDialog();
                    }else {
                        Intent in = new Intent(DashBoard.this, ProjectSearch.class);
                        in.putExtra("RequestedScreen", 2);
                        startActivity(in);
                    }
                }else{
                    Utils.showAlertNormal(DashBoard.this,"You are not authorized to use this feature");
                }

                break;
            case R.id.block_cancel:
                if((controller.getUserRole().getBlockAcess()==2)||(controller.getUserRole().getAdminAcess()==2)) {
                    if(controller.getManager().getLastSyncDate().length()==0)
                    {
                        showSyncAlertDialog();
                    }else {
                        // startActivity(new Intent(DashBoard.this, Block_Cancel.class));
                        Intent in = new Intent(DashBoard.this, ProjectSearch.class);
                        in.putExtra("RequestedScreen", 3);
                        startActivity(in);
                    }
                }else{
                    Utils.showAlertNormal(DashBoard.this,"You are not authorized to use this feature");
                }

                break;
            case R.id.consumption:
                if((controller.getUserRole().getConsumptionsAcess()==2)||(controller.getUserRole().getAdminAcess()==2)) {
                   // startActivity(new Intent(DashBoard.this, Consumption.class));
                    if(controller.getManager().getLastSyncDate().length()==0)
                    {
                        showSyncAlertDialog();
                    }else {
                        Intent in = new Intent(DashBoard.this, ProjectSearch.class);
                        in.putExtra("RequestedScreen", 4);
                        startActivity(in);
                    }
                }else{
                    Utils.showAlertNormal(DashBoard.this,"You are not authorized to use this feature");
                }

                break;
            case R.id.payment:
                if(controller.getManager().getLastSyncDate().length()==0)
                {
                    showSyncAlertDialog();
                }else {
                    Intent in = new Intent(DashBoard.this, ProjectSearch.class);
                    in.putExtra("RequestedScreen", 5);
                    startActivity(in);
                }
                  //  startActivity(new Intent(DashBoard.this, InvoiceList.class));

                break;
            case R.id.enquiry:

                startActivity(new Intent(DashBoard.this, Enquiry.class));

                break;
        }

    }


    public void popupMenu()
    {
        PopupMenu popup = new PopupMenu(DashBoard.this,menu);
        //Inflating the Popup using xml file
        popup.getMenuInflater().inflate(R.menu.popup_menu, popup.getMenu());

        //registering popup with OnMenuItemClickListener
        popup.setOnMenuItemClickListener(new PopupMenu.OnMenuItemClickListener() {
            public boolean onMenuItemClick(MenuItem item) {
                switch (item.getItemId())
                {
                    case R.id.logout:
                        controller.logout();
                        startActivity(new Intent(DashBoard.this,Login.class));
                        Toast.makeText(DashBoard.this, "Logged out Sucessfully", Toast.LENGTH_SHORT).show();
                        finish();
                        break;
                    case R.id.sync:
                        showSyncAlertDialog();

                        break;
                    case R.id.print:
                        startActivity(new Intent(DashBoard.this,Print.class));
                        break;
                    case R.id.settings:
                        showPrintAlertDialog();

                        break;

                }

                return true;
            }
        });
        popup.show();
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

        edt.setText(SettingsHelper.getBluetoothAddress(DashBoard.this));
        macView.setVisibility(View.VISIBLE);
        textView.setVisibility(View.GONE);
        cancel.setVisibility(View.INVISIBLE);
        submit.setText("Save");
        submit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (edt.getText().toString().length() > 0) {
                    dialog.setMessage("Checking printer status....");
                    dialog.show();
                    performTest(edt.getText().toString());
                } else {
                    Toast.makeText(DashBoard.this, "Please enter mac address", Toast.LENGTH_SHORT).show();
                }

            }
        });


        printDialog = dialogBuilder.create();
        printDialog.show();
    }

    public void showSyncAlertDialog() {
        AlertDialog.Builder dialogBuilder = new AlertDialog.Builder(this);
        LayoutInflater inflater = this.getLayoutInflater();
        final View dialogView = inflater.inflate(R.layout.sync_popup, null);
        dialogBuilder.setView(dialogView);
        Button submit=(Button)dialogView.findViewById(R.id.update);
        final TextView textView=(TextView)dialogView.findViewById(R.id.lastsync);
        final Button cancel=(Button) dialogView.findViewById(R.id.cancel);
        cancel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                syncDialog.cancel();
            }
        });
        if(controller.getManager().getLastSyncDate().length()==0)
        {
            textView.setText("You havent synchronized data,Please synchronize know for using app");
        }else {
            textView.setText("Last Sync on : " + controller.getManager().getLastSyncDate());
        }
        submit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
            if(Utils.isNetworkAvailable(DashBoard.this))
            {
                new GetData().execute(new String[]{Common.OwnerList});
                syncDialog.cancel();
            }
            }
        });


       syncDialog = dialogBuilder.create();
        syncDialog.setCancelable(false);
        syncDialog.show();
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

                        Toast.makeText(DashBoard.this, "Printer Ready", Toast.LENGTH_LONG).show();
                        try {
                            connection.open();
                            Bitmap icon = BitmapFactory.decodeResource(getResources(), R.drawable.brogas_logo);
                            Bitmap logo = Bitmap.createScaledBitmap(icon, 300, 200, false);
                            printer.printImage(new ZebraImageAndroid(logo), 0, 0, logo.getWidth(), logo.getHeight(), false);
                            connection.close();
                            Toast.makeText(DashBoard.this, "Printer Setup Done.", Toast.LENGTH_LONG).show();
                            dialog.cancel();

                        } catch (ConnectionException e) {
                            dialog.cancel();
                            Utils.showAlertNormal(DashBoard.this, e.getMessage());
                        }

                    }
                });


            } else if (printerStatus.isHeadOpen) {
                Utils.showAlertNormal(DashBoard.this, "Error: Head Open \nPlease Close Printer Head to Print");
                dialog.cancel();
            } else if (printerStatus.isPaused) {
                Utils.showAlertNormal(DashBoard.this, "Error: Printer Paused");
                dialog.cancel();
            } else if (printerStatus.isPaperOut) {
                Utils.showAlertNormal(DashBoard.this, "Error: Media Out \nPlease Load Media to Print");
                dialog.cancel();
            } else {
                Utils.showAlertNormal(DashBoard.this, "Error: Please check the Connection of the Printer");
                dialog.cancel();
            }

            connection.close();
            getAndSaveSettings(macAddress);
            Toast.makeText(DashBoard.this,"Settings updated Sucessfully",Toast.LENGTH_SHORT).show();
            if (printDialog != null) {
                printDialog.cancel();
            }
        } catch (ConnectionException e) {
            Utils.showAlertNormal(DashBoard.this, e.getMessage());
            dialog.cancel();
        } catch (ZebraPrinterLanguageUnknownException e) {
            Utils.showAlertNormal(DashBoard.this, e.getMessage());
            dialog.cancel();
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

                Toast.makeText(DashBoard.this, displayPrinterLanguage + "\n" + "Language set to ZPL", Toast.LENGTH_LONG).show();

            }
        });

    }

    /**
     * This method saves the entered address of the printer.
     */

    private void getAndSaveSettings(String macAddress) {
        SettingsHelper.saveBluetoothAddress(DashBoard.this, macAddress);

    }
    /*-------------------------------------------------------------------getData-------------------------------------------------------*/
    public class GetData extends AsyncTask<String,Void,String> {
        String calledMethod="";
        @Override
        protected void onPreExecute() {
            dialog.setMessage("Please wait synchronizing data....");
            dialog.show();
            super.onPreExecute();
        }

        @Override
        protected String doInBackground(String... strings) {
            String result=webServiceAcess.runRequest(Common.runAction,strings[0],  new String[]{controller.getManager().getLoggedInUserName()});
            return  result;
        }

        @Override
        protected void onPostExecute(String s) {
            Log.e("value", "onPostExecute: ", null);
            if(s.length()>0)
            {
                try{
                    JSONObject jsonObject=new JSONObject(s);
                    JSONObject result=jsonObject.getJSONObject("RESULT");
                    JSONObject tab=result.getJSONObject("TAB");
                    JSONArray jsonArray=result.getJSONArray("GRP");
                    JSONObject statusFlag=jsonArray.getJSONObject(1);
                    if(statusFlag.getJSONArray("FLD").getJSONObject(0).getInt("content")==2)
                    {

                        Object linobject=tab.get("LIN");
                        if(linobject instanceof JSONObject)
                        {
                            JSONObject jsonObject1=(JSONObject)linobject;
                            controller.setOwnerList(jsonObject1.toString(),Utils.getCurrentDate());
                        }else {
                            JSONArray lin = tab.getJSONArray("LIN");
                            controller.setOwnerList(lin.toString(),Utils.getCurrentDate());
                        }

                        Utils.showAlertNormal(DashBoard.this,"Synchronized Sucessfully.");

                    }else{
                        Utils.showAlertNormal(DashBoard.this,"Some Errror occured while synchronizing,Please Retry.");
                    }



                }catch (Exception ex)
                {
                    ex.fillInStackTrace();
                    Utils.showAlertNormal(DashBoard.this,"Some Errror occured while synchronizing,Please Retry.");
                }
            }else{
                Utils.showAlertNormal(DashBoard.this,Common.message);
            }
            dialog.cancel();
        }
    }



}