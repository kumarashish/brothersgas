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
import contracts.Contracts;

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
            case R.id.consumption:
                Toast.makeText(DashBoard.this,"under development",Toast.LENGTH_SHORT).show();
                break;
            case R.id.payment:
                Toast.makeText(DashBoard.this,"under development",Toast.LENGTH_SHORT).show();
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