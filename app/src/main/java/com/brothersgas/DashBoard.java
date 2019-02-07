package com.brothersgas;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.MenuItem;
import android.view.View;
import android.widget.ImageView;
import android.widget.PopupMenu;
import android.widget.Toast;

import butterknife.BindView;
import butterknife.ButterKnife;
import common.AppController;
import consumption.Consumption;
import consumption.ConsumptionList;
import contracts.Contracts;
import invoices.Block_Cancel;
import invoices.Connection_Disconnection_Invoice;
import payment.InvoiceList;

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
    AppController controller;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_dashboard);
        controller=(AppController)getApplicationContext();
        ButterKnife.bind(this);
        menu.setOnClickListener(this);
        contract.setOnClickListener(this);
        consumption.setOnClickListener(this);
        block_cancel.setOnClickListener(this);
        connect_disconnection_invoice.setOnClickListener(this);
        payment.setOnClickListener(this);
    }

    @Override
    public void onClick(View v) {
        switch (v.getId())
        {
            case R.id.menu:
                popupMenu();
                break;
            case R.id.contract:
                startActivity(new Intent(DashBoard.this, Contracts.class));
                break;
            case R.id.con_discon_invoice:
                startActivity(new Intent(DashBoard.this, Connection_Disconnection_Invoice.class));
                break;
            case R.id.block_cancel:
                startActivity(new Intent(DashBoard.this, Block_Cancel.class));
                break;
            case R.id.consumption:
                startActivity(new Intent(DashBoard.this, ConsumptionList.class));
                break;
            case R.id.payment:
                startActivity(new Intent(DashBoard.this, InvoiceList.class));
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

                    case R.id.settings:
                        //startActivity(new Intent(Dashboard.this,Settings.class));

                        break;
                }

                return true;
            }
        });
        popup.show();
    }
}