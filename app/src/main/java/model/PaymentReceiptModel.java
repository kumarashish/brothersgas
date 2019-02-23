package model;

import org.json.JSONArray;
import org.json.JSONObject;

import common.Common;

public class PaymentReceiptModel {
     public String Payment_Number = "";
     public String SiteDetails = "";
     public String CustomerVal = "";
     public String Control_account_type = "";
     public String Account = "";
     public String Accounting_date = "";
     public String Bank = "";
     public String Currencyal = "";
     public String BP_Amount = "";
     public String Check_number = "";
     public String Address = "";
     public String Bank_Amount = "";

    public PaymentReceiptModel(JSONArray jsonArray) {
        try {
            for(int i=0;i<jsonArray.length();i++) {
                JSONObject jsonObject = jsonArray.getJSONObject(i);
                if (jsonObject.getString("NAME").equalsIgnoreCase(Common.Payment_Number)) {
                    Payment_Number = jsonObject.isNull("content") ? "" : jsonObject.getString("content");
                } else if (jsonObject.getString("NAME").equalsIgnoreCase(Common.SiteDetails)) {
                    SiteDetails = jsonObject.isNull("content") ? "" : jsonObject.getString("content");
                }else if (jsonObject.getString("NAME").equalsIgnoreCase(Common.CustomerVal)) {
                    CustomerVal = jsonObject.isNull("content") ? "" : jsonObject.getString("content");
                }else if (jsonObject.getString("NAME").equalsIgnoreCase(Common.Control_account_type)) {
                    Control_account_type = jsonObject.isNull("content") ? "" : jsonObject.getString("content");
                }else if (jsonObject.getString("NAME").equalsIgnoreCase(Common.Account)) {
                    Account = jsonObject.isNull("content") ? "" : jsonObject.getString("content");
                }else if (jsonObject.getString("NAME").equalsIgnoreCase(Common.Accounting_date)) {
                    Accounting_date = jsonObject.isNull("content") ? "" : jsonObject.getString("content");
                }else if (jsonObject.getString("NAME").equalsIgnoreCase(Common.Bank)) {
                    Bank = jsonObject.isNull("content") ? "" : jsonObject.getString("content");
                }else if (jsonObject.getString("NAME").equalsIgnoreCase(Common.Currencyal)) {
                    Currencyal = jsonObject.isNull("content") ? "" : jsonObject.getString("content");
                }else if (jsonObject.getString("NAME").equalsIgnoreCase(Common.BP_Amount)) {
                    BP_Amount = jsonObject.isNull("content") ? "" : jsonObject.getString("content");
                }else if (jsonObject.getString("NAME").equalsIgnoreCase(Common.Check_number)) {
                    Check_number = jsonObject.isNull("content") ? "" : jsonObject.getString("content");
                }else if (jsonObject.getString("NAME").equalsIgnoreCase(Common.Address)) {
                    Address = jsonObject.isNull("content") ? "" : jsonObject.getString("content");
                }else if (jsonObject.getString("NAME").equalsIgnoreCase(Common.Bank_Amount)) {
                    Bank_Amount = jsonObject.isNull("content") ? "" : jsonObject.getString("content");
                }
            }
        } catch (Exception ex) {
            ex.fillInStackTrace();
        }
    }

    public String getAccount() {
        return Account;
    }

    public String getAccounting_date() {
        return Accounting_date;
    }

    public String getControl_account_type() {
        return Control_account_type;
    }

    public String getCustomerVal() {
        return CustomerVal;
    }

    public String getPayment_Number() {
        return Payment_Number;
    }

    public String getBank() {
        return Bank;
    }

    public String getSiteDetails() {
        return SiteDetails;
    }

    public String getAddress() {
        return Address;
    }

    public String getBank_Amount() {
        return Bank_Amount;
    }

    public String getBP_Amount() {
        return BP_Amount;
    }

    public String getCheck_number() {
        return Check_number;
    }

    public String getCurrencyal() {
        return Currencyal;
    }
}
