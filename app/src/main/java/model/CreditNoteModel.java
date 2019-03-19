package model;

import org.json.JSONArray;
import org.json.JSONObject;

/**
 * Created by ashish.kumar on 19-03-2019.
 */

public class CreditNoteModel {

    public static String InvoiceNumber="I_SINV";
    public static String CompanyName="O_CPY";
    public static String CompanyAddress="O_CPYADD";
    public static String CompanyTRT="O_CPYTRN";
    public static String CustomerCode="O_CUSTCODE";
    public static String CustomerName="O_CUSTNAME";
    public static String CustomerTRN="O_CUSTTRN";
    public static String CustomerAddress="O_CUSTADD";
    public static String Code="O_CODE";
    public static String Invoicenumber="O_INVNUM";
    public static String Reference="O_REFERENCE";
    public static String Date="O_DAT";
    public static String Time="O_TIM";
    public static String UserID="O_USRID";
    public static String UserName="O_USRNAM";
    public static String InvoiceDate="O_INVDAT";
    public static String InvoiceAmount="O_INVAMT";
   public static String Credit_Note_Amount=" O_CREDITAMT";
    public static String VatRate="O_VATRATE";
    public static String VatAmount="O_VATAMT";
    public static String TotalAmount="O_TOTAMT";
    public static String Toatal_Invoice_Amount="O_TOTINVAMT";
    public static String Toatal_Credit_Note_Amount="O_TOTCREDITAMT";
    public static String Total_Vat_rate="O_TOTVATRATE";
    public static String total_vat_Amount="O_TOTVATAMT";
    public static String Total_Amount="O_TOTAMT1";
    public static String Currency="O_CUR";
    public static String status="O_FLG";
    public static String message="O_MSG";


    //public String InvoiceNumberValue;
    public  String CompanyNameValue;
    public  String CompanyAddressValue;
    public  String CompanyTRTValue;
    public  String CustomerCodeValue;
    public  String CustomerNameValue;
    public  String CustomerTRNValue;
    public  String CustomerAddressValue;
    public String CodeValue;
    public  String InvoicenumberValue;
    public  String ReferenceValue;
    public  String DateValue;
    public  String TimeValue;
    public  String UserIDValue;
    public  String UserNameValue;
    public  String InvoiceDateValue;
    public String InvoiceAmountValue;
    public  String Credit_Note_AmountValue;
    public  String VatRateValue;
    public String VatAmountValue;
    public  String TotalAmountValue;
    public  String Toatal_Invoice_AmountValue;
    public  String Toatal_Credit_Note_AmountValue;
    public  String Total_Vat_rateValue;
    public  String total_vat_AmountValue;
    public  String Total_AmountValue;
    public  String CurrencyValue;
    public  int statusValue;
    public String messageValue;

    public CreditNoteModel(String value)
    {
        try{
            JSONObject jsonObject=new JSONObject(value);
            JSONObject result=jsonObject.getJSONObject("RESULT");
            JSONArray group=result.getJSONArray("GRP");
            JSONArray statusData=group.getJSONObject(2).getJSONArray("FLD");
            JSONArray conetentData=group.getJSONObject(1).getJSONArray("FLD");
            for (int i = 0; i < statusData.length(); i++) {
                JSONObject jObject = statusData.getJSONObject(i);
                if (jObject.getString("NAME").equalsIgnoreCase(status)) {
                    statusValue = jObject.isNull("content") ? 1 : jObject.getInt("content");
                } else if (jObject.getString("NAME").equalsIgnoreCase(message)) {
                    messageValue = jObject.isNull("content") ? "" : jObject.getString("content");
                }
            }


            for (int i = 0; i < conetentData.length(); i++) {
                JSONObject jObject = conetentData.getJSONObject(i);
                if (jObject.getString("NAME").equalsIgnoreCase(CompanyName)) {
                    CompanyNameValue = jObject.isNull("content") ? "": jObject.getString("content");
                } else if (jObject.getString("NAME").equalsIgnoreCase(CompanyAddress)) {
                    CompanyAddressValue = jObject.isNull("content") ? "" : jObject.getString("content");
                }else if (jObject.getString("NAME").equalsIgnoreCase(CompanyTRT)) {
                    CompanyTRTValue = jObject.isNull("content") ? "": jObject.getString("content");
                } else if (jObject.getString("NAME").equalsIgnoreCase(CustomerCode)) {
                    CustomerCodeValue = jObject.isNull("content") ? "" : jObject.getString("content");
                }else if (jObject.getString("NAME").equalsIgnoreCase(CustomerName)) {
                    CustomerNameValue = jObject.isNull("content") ? "": jObject.getString("content");
                } else if (jObject.getString("NAME").equalsIgnoreCase(CustomerAddress)) {
                    CustomerAddressValue = jObject.isNull("content") ? "" : jObject.getString("content");
                } else if (jObject.getString("NAME").equalsIgnoreCase(CustomerTRN)) {
                    CustomerTRNValue = jObject.isNull("content") ? "" : jObject.getString("content");
                }
                else if (jObject.getString("NAME").equalsIgnoreCase(Code)) {
                    CodeValue = jObject.isNull("content") ? "": jObject.getString("content");
                } else if (jObject.getString("NAME").equalsIgnoreCase(Invoicenumber)) {
                    InvoicenumberValue = jObject.isNull("content") ? "" : jObject.getString("content");
                }else if (jObject.getString("NAME").equalsIgnoreCase(Reference)) {
                    ReferenceValue = jObject.isNull("content") ? "" : jObject.getString("content");
                }else if (jObject.getString("NAME").equalsIgnoreCase(Date)) {
                    DateValue= jObject.isNull("content") ? "": jObject.getString("content");
                } else if (jObject.getString("NAME").equalsIgnoreCase(Time)) {
                    TimeValue = jObject.isNull("content") ? "" : jObject.getString("content");
                }else if (jObject.getString("NAME").equalsIgnoreCase(UserID)) {
                    UserIDValue = jObject.isNull("content") ? "": jObject.getString("content");
                } else if (jObject.getString("NAME").equalsIgnoreCase(UserName)) {
                    UserNameValue = jObject.isNull("content") ? "" : jObject.getString("content");
                }else if (jObject.getString("NAME").equalsIgnoreCase(InvoiceDate)) {
                    InvoiceDateValue = jObject.isNull("content") ? "": jObject.getString("content");
                } else if (jObject.getString("NAME").equalsIgnoreCase(InvoiceAmount)) {
                    InvoiceAmountValue = jObject.isNull("content") ? "" : jObject.getString("content");
                } else if (jObject.getString("NAME").equalsIgnoreCase(Credit_Note_Amount)) {
                    Credit_Note_AmountValue = jObject.isNull("content") ? "" : jObject.getString("content");
                }else if (jObject.getString("NAME").equalsIgnoreCase(VatRate)) {
                    VatRateValue = jObject.isNull("content") ? "": jObject.getString("content");
                } else if (jObject.getString("NAME").equalsIgnoreCase(VatAmount)) {
                    VatAmountValue = jObject.isNull("content") ? "" : jObject.getString("content");
                }else if (jObject.getString("NAME").equalsIgnoreCase(TotalAmount)) {
                    TotalAmountValue = jObject.isNull("content") ? "": jObject.getString("content");
                } else if (jObject.getString("NAME").equalsIgnoreCase(Toatal_Invoice_Amount)) {
                    Toatal_Invoice_AmountValue = jObject.isNull("content") ? "" : jObject.getString("content");
                }else if (jObject.getString("NAME").equalsIgnoreCase(Toatal_Credit_Note_Amount)) {
                    Toatal_Credit_Note_AmountValue= jObject.isNull("content") ? "": jObject.getString("content");
                } else if (jObject.getString("NAME").equalsIgnoreCase(Total_Vat_rate)) {
                    Total_Vat_rateValue = jObject.isNull("content") ? "" : jObject.getString("content");
                }else if (jObject.getString("NAME").equalsIgnoreCase(total_vat_Amount)) {
                    total_vat_AmountValue = jObject.isNull("content") ? "": jObject.getString("content");
                } else if (jObject.getString("NAME").equalsIgnoreCase( Total_Amount)) {
                    Total_AmountValue = jObject.isNull("content") ? "" : jObject.getString("content");
                }

            }
        }catch (Exception ex)
        {
            ex.fillInStackTrace();
        }
    }

    public String getVatAmountValue() {
        return VatAmountValue;
    }

    public String getDateValue() {
        return DateValue;
    }

    public String getCodeValue() {
        return CodeValue;
    }

    public String getInvoicenumberValue() {
        return InvoicenumberValue;
    }

    public String getReferenceValue() {
        return ReferenceValue;
    }

    public String getCustomerCodeValue() {
        return CustomerCodeValue;
    }

    public String getCustomerNameValue() {
        return CustomerNameValue;
    }

    public String getCurrencyValue() {
        return CurrencyValue;
    }

    public String getCustomerAddressValue() {
        return CustomerAddressValue;
    }

    public int getStatusValue() {
        return statusValue;
    }

    public String getCompanyAddressValue() {
        return CompanyAddressValue;
    }

    public String getMessageValue() {
        return messageValue;
    }

    public String getCompanyNameValue() {
        return CompanyNameValue;
    }

    public String getCompanyTRTValue() {
        return CompanyTRTValue;
    }

    public String getCustomerTRNValue() {
        return CustomerTRNValue;
    }

    public String getInvoiceAmountValue() {
        return InvoiceAmountValue;
    }

    public String getInvoiceDateValue() {
        return InvoiceDateValue;
    }


    public String getTotalAmountValue() {
        return TotalAmountValue;
    }

    public String getUserIDValue() {
        return UserIDValue;
    }

    public String getUserNameValue() {
        return UserNameValue;
    }

    public String getTotal_AmountValue() {
        return Total_AmountValue;
    }

    public String getCredit_Note_AmountValue() {
        return Credit_Note_AmountValue;
    }

    public String getToatal_Invoice_AmountValue() {
        return Toatal_Invoice_AmountValue;
    }

    public String getTotal_vat_AmountValue() {
        return total_vat_AmountValue;
    }

    public String getTotal_Vat_rateValue() {
        return Total_Vat_rateValue;
    }

    public String getToatal_Credit_Note_AmountValue() {
        return Toatal_Credit_Note_AmountValue;
    }

    public String getVatRateValue() {
        return VatRateValue;
    }

    public String getTimeValue() {
        return TimeValue;
    }
}
