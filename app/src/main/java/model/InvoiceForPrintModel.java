package model;

import org.json.JSONArray;
import org.json.JSONObject;

import common.Common;

/**
 * Created by ashish.kumar on 04-04-2019.
 */

public class InvoiceForPrintModel {
    String invoiceNumber;
    String invoiceDate;
    String site;

    String customer;
    String amount;
    String currency;


  public   InvoiceForPrintModel(JSONArray jsonArray)
    {
        try{
            for(int i=0;i<jsonArray.length();i++)
            {
                JSONObject jsonObject=jsonArray.getJSONObject(i);
                if(jsonObject.getString("NAME").equalsIgnoreCase("O_YNUM"))
                {
                    invoiceNumber=jsonObject.isNull("content")?"":jsonObject.getString("content");
                }
                else if(jsonObject.getString("NAME").equalsIgnoreCase("O_YDAT"))
                {
                    invoiceDate=jsonObject.isNull("content")?"":jsonObject.getString("content");
                }
                else if(jsonObject.getString("NAME").equalsIgnoreCase("O_YSITE"))
                {
                    site=jsonObject.isNull("content")?"":jsonObject.getString("content");
                }
                else if(jsonObject.getString("NAME").equalsIgnoreCase("O_YBPNAM"))
                {
                    customer=jsonObject.isNull("content")?"":jsonObject.getString("content");
                }
                else if(jsonObject.getString("NAME").equalsIgnoreCase("O_YAMT"))
                {
                    amount=jsonObject.isNull("content")?"":jsonObject.getString("content");
                }
                else if(jsonObject.getString("NAME").equalsIgnoreCase("O_YCUR"))
                {
                    currency=jsonObject.isNull("content")?"":jsonObject.getString("content");
                }


            }
        }catch (Exception ex)

        {
            ex.fillInStackTrace();
        }
    }

    public String getCurrency() {
        return currency;
    }

    public String getAmount() {
        return amount;
    }

    public String getCustomer() {
        return customer;
    }

    public String getInvoiceDate() {
        return invoiceDate;
    }

    public String getInvoiceNumber() {
        return invoiceNumber;
    }

    public String getSite() {
        return site;
    }
}
