package model;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;

import common.Common;

/**
 * Created by ashish.kumar on 06-03-2019.
 */

public class EnquiryModel {
    public String Username;
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


    ArrayList<PaymentModel> cashList=new ArrayList<>();
    ArrayList<PaymentModel> cheaqueList=new ArrayList<>();
    public EnquiryModel(JSONArray jsonArray)
    {
        try{
            for(int i=0;i<jsonArray.length();i++) {
                JSONObject jsonObject = jsonArray.getJSONObject(i);
                if (jsonObject.getString("NAME").equalsIgnoreCase(Common.Number_of_invoices)) {
                    Number_of_invoices = jsonObject.isNull("content") ? "" : jsonObject.getString("content");
                }else if (jsonObject.getString("NAME").equalsIgnoreCase("O_YUSRNAM")) {
                    Username = jsonObject.isNull("content") ? "" : jsonObject.getString("content");
                }
                else if (jsonObject.getString("NAME").equalsIgnoreCase(Common.Number_of_Cash_Payments)) {
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

    public String getUsername() {
        return Username;
    }

    public String getTotal_Invoices_amount() {
        return Total_Invoices_amount;
    }

    public void setCashCheaquePayments(JSONArray jsonArray)
    {  cashList.clear();
        cheaqueList.clear();
     for(int i=0;i<jsonArray.length();i++)
     {

         try {
             JSONObject jsonObject=jsonArray.getJSONObject(i);
             PaymentModel model=new PaymentModel(jsonObject.getJSONArray("FLD"));
             if(model.getPaymentType().equalsIgnoreCase("RMRC"))
             {
                 cashList.add(model);
             }else{
                 cheaqueList.add(model);
             }
         } catch (JSONException e) {
             e.printStackTrace();
         }
     }
    }

    public ArrayList<PaymentModel> getCashList() {
        return cashList;
    }

    public ArrayList<PaymentModel> getCheaqueList() {
        return cheaqueList;
    }

    public class PaymentModel{
        String paymentNumber="";
        String customer="";
        String amout="";
        String paymentType;

        public PaymentModel(JSONArray paymentList)
        {
            try{

                for(int i=0;i<paymentList.length();i++) {
                    JSONObject jsonObject = paymentList.getJSONObject(i);
                    if (jsonObject.getString("NAME").equalsIgnoreCase("O_PAYNUM")) {
                       paymentNumber = jsonObject.isNull("content") ? "" : jsonObject.getString("content");
                    }else if (jsonObject.getString("NAME").equalsIgnoreCase("O_CUST")) {
                      customer= jsonObject.isNull("content") ? "" : jsonObject.getString("content");
                    }else if (jsonObject.getString("NAME").equalsIgnoreCase("O_AMT")) {
                        amout= jsonObject.isNull("content") ? "" : jsonObject.getString("content");
                    }else if (jsonObject.getString("NAME").equalsIgnoreCase("O_PAYTYP")) {
                        paymentType= jsonObject.isNull("content") ? "" : jsonObject.getString("content");
                    }

                }

        }catch (Exception ex)
            {
            ex.fillInStackTrace();}


        }

        public String getPaymentType() {
            return paymentType;
        }

        public String getCustomer() {
            return customer;
        }

        public String getAmout() {
            return amout;
        }

        public String getPaymentNumber() {
            return paymentNumber;
        }
    }
}
