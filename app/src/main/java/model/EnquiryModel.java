package model;

import org.json.JSONArray;
import org.json.JSONObject;

import common.Common;

/**
 * Created by ashish.kumar on 06-03-2019.
 */

public class EnquiryModel {
     public  String Number_of_invoices;
     public String Number_of_Cash_Payments;
    public  String Number_of_Cheque_Payments;
     public  String Number_of_Connections;
     public  String Number_of_deposite_invoices;
    public  String Number_of_disconnection_invoices;
    public  String Number_of_Consumable_invoices;
     public  String Total_Invoices_amount;
     public  String Total_Cash_payments_amount;
     public  String Total_Cheque_payments_amount;
     public  String Total_Cheque_and_cheque_payments_amount;
     public  String Total_Deposite_Invoices_amount;
    public  String Total_Disconnection_Invoices_amount;
    public String Total_Consumable_Invoices_amount;
    public EnquiryModel(JSONArray jsonArray)
    {
        try{
            for(int i=0;i<jsonArray.length();i++) {
                JSONObject jsonObject = jsonArray.getJSONObject(i);
                if (jsonObject.getString("NAME").equalsIgnoreCase(Common.Number_of_invoices)) {
                    Number_of_invoices = jsonObject.isNull("content") ? "" : jsonObject.getString("content");
                }else if (jsonObject.getString("NAME").equalsIgnoreCase(Common.Number_of_Cash_Payments)) {
                    Number_of_Cash_Payments = jsonObject.isNull("content") ? "" : jsonObject.getString("content");
                }else if (jsonObject.getString("NAME").equalsIgnoreCase(Common.Number_of_Cheque_Payments)) {
                    Number_of_Cheque_Payments = jsonObject.isNull("content") ? "" : jsonObject.getString("content");
                }else if (jsonObject.getString("NAME").equalsIgnoreCase(Common.Number_of_Connections)) {
                    Number_of_Connections = jsonObject.isNull("content") ? "" : jsonObject.getString("content");
                }else if (jsonObject.getString("NAME").equalsIgnoreCase(Common.Number_of_deposite_invoices)) {
                    Number_of_deposite_invoices = jsonObject.isNull("content") ? "" : jsonObject.getString("content");
                }else if (jsonObject.getString("NAME").equalsIgnoreCase(Common.Number_of_disconnection_invoices)) {
                    Number_of_disconnection_invoices = jsonObject.isNull("content") ? "" : jsonObject.getString("content");
                }else if (jsonObject.getString("NAME").equalsIgnoreCase(Common.Number_of_Consumable_invoices)) {
                    Number_of_Consumable_invoices = jsonObject.isNull("content") ? "" : jsonObject.getString("content");
                }else if (jsonObject.getString("NAME").equalsIgnoreCase(Common.Total_Invoices_amount)) {
                    Total_Invoices_amount = jsonObject.isNull("content") ? "" : jsonObject.getString("content");
                }else if (jsonObject.getString("NAME").equalsIgnoreCase(Common.Total_Cash_payments_amount)) {
                    Total_Cash_payments_amount = jsonObject.isNull("content") ? "" : jsonObject.getString("content");
                }else if (jsonObject.getString("NAME").equalsIgnoreCase(Common.Total_Cheque_payments_amount)) {
                    Total_Cheque_payments_amount = jsonObject.isNull("content") ? "" : jsonObject.getString("content");
                }else if (jsonObject.getString("NAME").equalsIgnoreCase(Common.Total_Cheque_and_cheque_payments_amount)) {
                    Total_Cheque_and_cheque_payments_amount = jsonObject.isNull("content") ? "" : jsonObject.getString("content");
                }else if (jsonObject.getString("NAME").equalsIgnoreCase(Common.Total_Deposite_Invoices_amount)) {
                    Total_Deposite_Invoices_amount = jsonObject.isNull("content") ? "" : jsonObject.getString("content");
                }else if (jsonObject.getString("NAME").equalsIgnoreCase(Common.Total_Disconnection_Invoices_amount)) {
                    Total_Disconnection_Invoices_amount = jsonObject.isNull("content") ? "" : jsonObject.getString("content");
                }else if (jsonObject.getString("NAME").equalsIgnoreCase(Common.Total_Consumable_Invoices_amount)) {
                    Total_Consumable_Invoices_amount = jsonObject.isNull("content") ? "" : jsonObject.getString("content");
                }
            }}catch (Exception ex)
        {
        ex.fillInStackTrace();
        }
    }

    public String getNumber_of_Cash_Payments() {
        return Number_of_Cash_Payments;
    }

    public String getNumber_of_Cheque_Payments() {
        return Number_of_Cheque_Payments;
    }

    public String getNumber_of_Connections() {
        return Number_of_Connections;
    }

    public String getNumber_of_Consumable_invoices() {
        return Number_of_Consumable_invoices;
    }

    public String getNumber_of_deposite_invoices() {
        return Number_of_deposite_invoices;
    }

    public String getNumber_of_disconnection_invoices() {
        return Number_of_disconnection_invoices;
    }

    public String getNumber_of_invoices() {
        return Number_of_invoices;
    }

    public String getTotal_Cash_payments_amount() {
        return Total_Cash_payments_amount;
    }

    public String getTotal_Cheque_and_cheque_payments_amount() {
        return Total_Cheque_and_cheque_payments_amount;
    }

    public String getTotal_Cheque_payments_amount() {
        return Total_Cheque_payments_amount;
    }

    public String getTotal_Consumable_Invoices_amount() {
        return Total_Consumable_Invoices_amount;
    }

    public String getTotal_Deposite_Invoices_amount() {
        return Total_Deposite_Invoices_amount;
    }

    public String getTotal_Disconnection_Invoices_amount() {
        return Total_Disconnection_Invoices_amount;
    }

    public String getTotal_Invoices_amount() {
        return Total_Invoices_amount;
    }
}
