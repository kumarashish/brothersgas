package model;

import org.json.JSONArray;
import org.json.JSONObject;

import common.Common;

public class InvoiceModel {
    public String Sales_Invoice_Number="";
    public  String Total_amount = "";
    public  String Total_amount_currency = "";
    public  String Total_paid_amount = "";
    public  String Total_paid_amount_currency = "";
    public  String Total_Outstanding_amount = "";
    public  String Total_outstanding_amount_currency ="";
    public  String Reference = "";
    public  String Date = "";
    public  String Amount = "";
    public  String CurrencyValue = "";
    public  String Customer = "";
    public  String Customer_Description = "";
    public  String Site = "";
    public  String Site_Description = "";
    public  String Paid_amount = "";
    public  String Paid_amount_currency = "";
    public  String Outstanding_amount = "";
    public  String Outstanding_amount_currency ="";
    public InvoiceModel(JSONArray jsonArray)
    {
        try{
            for(int i=0;i<jsonArray.length();i++)
            {
                JSONObject jsonObject=jsonArray.getJSONObject(i);
                if(jsonObject.getString("NAME").equalsIgnoreCase(Common.Sales_Invoice_Number))
                {
                    Sales_Invoice_Number=jsonObject.isNull("content")?"":jsonObject.getString("content");
                }
                else if(jsonObject.getString("NAME").equalsIgnoreCase(Common.Total_amount))
                {
                    Total_amount=jsonObject.isNull("content")?"":jsonObject.getString("content");
                }else if(jsonObject.getString("NAME").equalsIgnoreCase(Common.Total_amount_currency))
                {
                    Total_amount_currency=jsonObject.isNull("content")?"":jsonObject.getString("content");
                }
                else if(jsonObject.getString("NAME").equalsIgnoreCase(Common.Total_paid_amount))
                {
                    Total_paid_amount=jsonObject.isNull("content")?"":jsonObject.getString("content");
                }
                else if(jsonObject.getString("NAME").equalsIgnoreCase(Common.Total_paid_amount_currency))
                {
                    Total_paid_amount_currency=jsonObject.isNull("content")?"":jsonObject.getString("content");
                }
                else if(jsonObject.getString("NAME").equalsIgnoreCase(Common.Total_Outstanding_amount))
                {
                    Total_Outstanding_amount=jsonObject.isNull("content")?"":jsonObject.getString("content");
                }else if(jsonObject.getString("NAME").equalsIgnoreCase(Common.Total_outstanding_amount_currency))
                {
                    Total_outstanding_amount_currency=jsonObject.isNull("content")?"":jsonObject.getString("content");
                }
                else if(jsonObject.getString("NAME").equalsIgnoreCase(Common.Reference))
                {
                    Reference=jsonObject.isNull("content")?"":jsonObject.getString("content");
                }
                else if(jsonObject.getString("NAME").equalsIgnoreCase(Common.Date))
                {
                    Date=jsonObject.isNull("content")?"":jsonObject.getString("content");
                }
                else if(jsonObject.getString("NAME").equalsIgnoreCase(Common.Amount))
                {
                    Amount=jsonObject.isNull("content")?"":jsonObject.getString("content");
                }
                else if(jsonObject.getString("NAME").equalsIgnoreCase(Common.CurrencyValue))
                {
                    CurrencyValue=jsonObject.isNull("content")?"":jsonObject.getString("content");
                }
                else if(jsonObject.getString("NAME").equalsIgnoreCase(Common.Customer))
                {
                    Customer=jsonObject.isNull("content")?"":jsonObject.getString("content");
                }
                else if(jsonObject.getString("NAME").equalsIgnoreCase(Common.Customer_Description))
                {
                    Customer_Description=jsonObject.isNull("content")?"":jsonObject.getString("content");
                }
                else if(jsonObject.getString("NAME").equalsIgnoreCase(Common.Site))
                {
                    Site=jsonObject.isNull("content")?"":jsonObject.getString("content");
                }
                else if(jsonObject.getString("NAME").equalsIgnoreCase(Common.Site_Description))
                {
                    Site_Description=jsonObject.isNull("content")?"":jsonObject.getString("content");
                }
                else if(jsonObject.getString("NAME").equalsIgnoreCase(Common.Paid_amount))
                {
                    Paid_amount=jsonObject.isNull("content")?"":jsonObject.getString("content");
                }
                else if(jsonObject.getString("NAME").equalsIgnoreCase(Common.Paid_amount_currency))
                {
                    Paid_amount_currency=jsonObject.isNull("content")?"":jsonObject.getString("content");
                }
                else if(jsonObject.getString("NAME").equalsIgnoreCase(Common.Outstanding_amount))
                {
                    Outstanding_amount=jsonObject.isNull("content")?"":jsonObject.getString("content");
                }
                else if(jsonObject.getString("NAME").equalsIgnoreCase(Common.Outstanding_amount_currency))
                {
                    Outstanding_amount_currency=jsonObject.isNull("content")?"":jsonObject.getString("content");
                }
            }
        }catch (Exception ex)
        {
            ex.fillInStackTrace();
        }
    }

    public String getTotal_amount() {
        return Total_amount;
    }

    public String getAmount() {
        return Amount;
    }

    public String getCurrencyValue() {
        return CurrencyValue;
    }

    public String getDate() {
        return Date;
    }

    public String getTotal_amount_currency() {
        return Total_amount_currency;
    }

    public String getTotal_Outstanding_amount() {
        return Total_Outstanding_amount;
    }

    public String getCustomer() {
        return Customer;
    }

    public String getCustomer_Description() {
        return Customer_Description;
    }

    public String getReference() {
        return Reference;
    }

    public String getTotal_outstanding_amount_currency() {
        return Total_outstanding_amount_currency;
    }

    public String getTotal_paid_amount() {
        return Total_paid_amount;
    }

    public String getTotal_paid_amount_currency() {
        return Total_paid_amount_currency;
    }

    public String getOutstanding_amount() {
        return Outstanding_amount;
    }

    public String getPaid_amount() {
        return Paid_amount;
    }

    public String getOutstanding_amount_currency() {
        return Outstanding_amount_currency;
    }

    public String getPaid_amount_currency() {
        return Paid_amount_currency;
    }

    public String getSite() {
        return Site;
    }

    public String getSite_Description() {
        return Site_Description;
    }

    public String getSales_Invoice_Number() {
        return Sales_Invoice_Number;
    }
}


