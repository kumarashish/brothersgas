package model;

import org.json.JSONArray;
import org.json.JSONObject;

import common.Common;

/**
 * Created by ashish.kumar on 29-01-2019.*/



public class ContractDetails {
 public String Site_value="";
 public  String Customer_value="";
 public String Contract_Date="";
 public  String Customer_Address="";
 public  String Product_Item="";
 public  String Deposit_Amount="";
 public  String Connection_charges="";
 public  String Disconnection_Charges="";
 public  String Pressure_Factor="";
 public  String Initial_meter_reading="";
 public String Deposit_Invoice="";
 public  String Connection_Disconnection_Invoice="";
 public String contractNumber="";
 public ContractDetails(JSONArray jsonArray)
 {
     try{
         for(int i=0;i<jsonArray.length();i++) {
             JSONObject jsonObject = jsonArray.getJSONObject(i);
             if(jsonObject.getString("NAME").equalsIgnoreCase(Common.Site_value))
             {
                 Site_value=jsonObject.isNull("content")?"":jsonObject.getString("content");
             }else  if(jsonObject.getString("NAME").equalsIgnoreCase(Common.Customer_value))
             {
                 Customer_value=jsonObject.isNull("content")?"":jsonObject.getString("content");
             }
             else if(jsonObject.getString("NAME").equalsIgnoreCase(Common.Contract_Date))
             {
                 Contract_Date=jsonObject.isNull("content")?"":jsonObject.getString("content");
             }else  if(jsonObject.getString("NAME").equalsIgnoreCase(Common.Customer_Address))
             {
                 Customer_Address=jsonObject.isNull("content")?"":jsonObject.getString("content");
             }
             else  if(jsonObject.getString("NAME").equalsIgnoreCase(Common.Product_Item))
             {
                 Product_Item=jsonObject.isNull("content")?"":jsonObject.getString("content");
             }
             else if(jsonObject.getString("NAME").equalsIgnoreCase(Common.Deposit_Amount))
             {
                 Deposit_Amount=jsonObject.isNull("content")?"":jsonObject.getString("content");
             }else  if(jsonObject.getString("NAME").equalsIgnoreCase(Common.Connection_charges))
             {
                 Connection_charges=jsonObject.isNull("content")?"":jsonObject.getString("content");
             }
             else if(jsonObject.getString("NAME").equalsIgnoreCase(Common.Disconnection_Charges))
             {
                 Disconnection_Charges=jsonObject.isNull("content")?"":jsonObject.getString("content");
             }else  if(jsonObject.getString("NAME").equalsIgnoreCase(Common.Pressure_Factor))
             {
                 Pressure_Factor=jsonObject.isNull("content")?"":jsonObject.getString("content");
             }
             else  if(jsonObject.getString("NAME").equalsIgnoreCase(Common.Initial_meter_reading))
             {
                 Initial_meter_reading=jsonObject.isNull("content")?"":jsonObject.getString("content");
             }else  if(jsonObject.getString("NAME").equalsIgnoreCase(Common.Deposit_Invoice))
             {
                 Deposit_Invoice=jsonObject.isNull("content")?"":jsonObject.getString("content");
             }else  if(jsonObject.getString("NAME").equalsIgnoreCase(Common.Connection_Disconnection_Invoice))
             {
                 Connection_Disconnection_Invoice=jsonObject.isNull("content")?"":jsonObject.getString("content");
             }
         }}catch (Exception ex)
     {
         ex.fillInStackTrace();
     }
 }

    public String getConnection_charges() {
        return Connection_charges;
    }

    public String getConnection_Disconnection_Invoice() {
        return Connection_Disconnection_Invoice;
    }

    public String getContract_Date() {
        return Contract_Date;
    }

    public String getCustomer_Address() {
        return Customer_Address;
    }

    public String getCustomer_value() {
        return Customer_value;
    }

    public String getDeposit_Amount() {
        return Deposit_Amount;
    }

    public String getDeposit_Invoice() {
        return Deposit_Invoice;
    }

    public String getDisconnection_Charges() {
        return Disconnection_Charges;
    }

    public String getInitial_meter_reading() {
        return Initial_meter_reading;
    }

    public String getPressure_Factor() {
        return Pressure_Factor;
    }

    public String getProduct_Item() {
        return Product_Item;
    }

    public String getSite_value() {
        return Site_value;
    }

    public void setContractNumber(String contractNumber) {
        this.contractNumber = contractNumber;
    }

    public String getContractNumber() {
        return contractNumber;
    }
}
