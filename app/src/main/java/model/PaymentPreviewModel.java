package model;

import org.json.JSONArray;
import org.json.JSONObject;

/**
 * Created by ashish.kumar on 19-03-2019.
 */

public class PaymentPreviewModel {
    public static String PaymentNumber = "I_PAYNUM";
    public static String CustomerCode = "O_TENCODE";
    public static String CustomerName = "O_TENNAME";
    public static String CustomerAddress = "O_TENADD";
    public static String Amount = "O_AMT";
    public static String Currency = " O_CUR";
    public static String PaymentType = "O_PAYTYP";
    public static String Bank = "O_BANK";
    public static String ChequeNumber = "O_CHQNUM";
    public static String ChequeDate = "O_CHQDAT";
    public static String Date = "O_DAT";
    public static String Time = "O_TIM";
    public static String USERName = "O_USRNAM";
    public static String Userid = "O_USRID";
    public static String RegisterAddress = "O_REGADD";
    public static String InvoiceNumbrs = "O_INVNUM";
    public static String status = "O_FLG";
    public static String message = "O_MSG";



    public String PaymentNumberValue;
    public  String CustomerCodeValue;
    public String CustomerNameValue;
    public  String CustomerAddressValue;
    public  String AmountValue;
    public String CurrencyValue;
    public String PaymentTypeValue;
    public String BankValue;
    public  String ChequeNumberValue;
    public  String ChequeDateValue;
    public String DateValue;
    public  String TimeValue;
    public  String USERNameValue;
    public  String UseridValue;
    public  String RegisterAddressValue;
    public  String InvoiceNumbrsValue;
    public  int statusValue;
    public  String messageValue;

    public PaymentPreviewModel(String value)
    {
        try{
            JSONObject jsonObject=new JSONObject(value);
            JSONObject result=jsonObject.getJSONObject("RESULT");
            JSONArray group=result.getJSONArray("GRP");
            JSONObject tab=result.getJSONObject("TAB");
            JSONArray contentData= group.getJSONObject(0).getJSONArray("FLD");
            JSONArray statusData= group.getJSONObject(1).getJSONArray("FLD");

            for (int i = 0; i < statusData.length(); i++) {
                JSONObject jObject = statusData.getJSONObject(i);

                if (jObject.getString("NAME").equalsIgnoreCase(status)) {
                    statusValue = jObject.isNull("content") ? 1 : jObject.getInt("content");
                } else if (jObject.getString("NAME").equalsIgnoreCase(message)) {
                    messageValue = jObject.isNull("content") ? "" : jObject.getString("content");
                }
            }

            for(int i=0;i<contentData.length();i++)
            {
                JSONObject jObject = contentData.getJSONObject(i);
                if (jObject.getString("NAME").equalsIgnoreCase(PaymentNumber)) {
                    PaymentNumberValue= jObject.isNull("content") ?"" : jObject.getString("content");
                } else if (jObject.getString("NAME").equalsIgnoreCase(CustomerCode)) {
                    CustomerCodeValue = jObject.isNull("content") ? "" : jObject.getString("content");
                }else if (jObject.getString("NAME").equalsIgnoreCase(CustomerName)) {
                    CustomerNameValue= jObject.isNull("content") ?"" : jObject.getString("content");
            } else if (jObject.getString("NAME").equalsIgnoreCase(CustomerAddress)) {
                    CustomerAddressValue = jObject.isNull("content") ? "" : jObject.getString("content");
            }else if (jObject.getString("NAME").equalsIgnoreCase(Amount)) {
                    AmountValue= jObject.isNull("content") ?"" : jObject.getString("content");
                } else if (jObject.getString("NAME").equalsIgnoreCase(PaymentType)) {
                    PaymentTypeValue = jObject.isNull("content") ? "" : jObject.getString("content");
                }else if (jObject.getString("NAME").equalsIgnoreCase(Bank)) {
                    BankValue= jObject.isNull("content") ?"" : jObject.getString("content");
                } else if (jObject.getString("NAME").equalsIgnoreCase(ChequeNumber)) {
                    ChequeNumberValue = jObject.isNull("content") ? "" : jObject.getString("content");
                }else if (jObject.getString("NAME").equalsIgnoreCase(ChequeDate)) {
                    ChequeDateValue= jObject.isNull("content") ?"" : jObject.getString("content");
                } else if (jObject.getString("NAME").equalsIgnoreCase(Date)) {
                    DateValue = jObject.isNull("content") ? "" : jObject.getString("content");
                }else if (jObject.getString("NAME").equalsIgnoreCase(Time)) {
                    TimeValue= jObject.isNull("content") ?"" : jObject.getString("content");
                } else if (jObject.getString("NAME").equalsIgnoreCase(USERName)) {
                    USERNameValue = jObject.isNull("content") ? "" : jObject.getString("content");
                }else if (jObject.getString("NAME").equalsIgnoreCase(Userid)) {
                    UseridValue= jObject.isNull("content") ? "" : jObject.getString("content");
                }else if (jObject.getString("NAME").equalsIgnoreCase(RegisterAddress)) {
                    RegisterAddressValue= jObject.isNull("content") ?"" : jObject.getString("content");
                } else if (jObject.getString("NAME").equalsIgnoreCase(InvoiceNumbrs)) {
                    InvoiceNumbrsValue = jObject.isNull("content") ? "" : jObject.getString("content");
                }

            }
            InvoiceNumbrsValue=tab.getJSONObject("LIN").getJSONObject("FLD").getString("content");



        }catch (Exception ex)
        {
            ex.fillInStackTrace();
        }
    }

    public int getStatusValue() {
        return statusValue;
    }

    public String getAmountValue() {
        return AmountValue;
    }

    public String getBankValue() {
        return BankValue;
    }

    public String getCurrencyValue() {
        return CurrencyValue;
    }

    public String getDateValue() {
        return DateValue;
    }

    public String getMessageValue() {
        return messageValue;
    }

    public String getTimeValue() {
        return TimeValue;
    }

    public String getUseridValue() {
        return UseridValue;
    }

    public String getUSERNameValue() {
        return USERNameValue;
    }

    public String getCustomerAddressValue() {
        return CustomerAddressValue;
    }

    public String getCustomerNameValue() {
        return CustomerNameValue;
    }

    public String getCustomerCodeValue() {
        return CustomerCodeValue;
    }

    public String getChequeDateValue() {
        return ChequeDateValue;
    }

    public String getChequeNumberValue() {
        return ChequeNumberValue;
    }

    public String getInvoiceNumbrsValue() {
        return InvoiceNumbrsValue;
    }

    public String getPaymentNumberValue() {
        return PaymentNumberValue;
    }

    public String getPaymentTypeValue() {
        return PaymentTypeValue;
    }

    public String getRegisterAddressValue() {
        return RegisterAddressValue;
    }
}
