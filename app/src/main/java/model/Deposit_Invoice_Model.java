package model;

import org.json.JSONArray;
import org.json.JSONObject;

import java.util.ArrayList;

public class Deposit_Invoice_Model {



        public static String Invoice_Number = "I_SINV";
        public static String Projectname = "O_PROJDESC";
        public static String TenantCode = "O_TENCODE";
        public static String TenantName = "O_TENNAME";
        public static String CustomerCode = "O_CUSTCODE";
        public static String CustomerName = "O_CUSTNAME";
        public static String CustomerTRNNumber = "O_CUSTTRN";
        public static String CustomerAddress = "O_CUSTADD";
        public static String SupplierTRN = "O_SUPTRN";
        public static String Suppliername = "O_SUPNAME";
        public static String Date = "O_DAT";
        public static String Time = "O_TIM";
        public static String UserName = "O_USRNAM";
        public static String UserID = "O_USRID";
        public static String RegisteredAddress = "O_REGADD";
        public static String Teliphone = "O_TEL";
        public static String Fax = "O_FAX";
        public static String Email = "O_EML";
        public static String Website = "O_WEB";
        public static String PreviousMeterreading = "O_PREVRED";
        public static String PresentMeterreading = "O_CURRRED";
        public static String UnitsConsumed = "O_UNCONS";
        public static String PressureFactor = "O_PF";
        public static String ActualUnitConsumed = "O_ACTUNCONS";
        public static String TotalExcludingTax = "O_TOTEVAT";
        public static String TotalIncludingTax = "O_TOTIVAT";
        public static String TotalVat = "O_TOTVAT";
        public static String message = "O_MSG";
        public static String status="O_FLG";


        public String Invoice_NumberValue;
        public  String ProjectnameValue;
        public  String TenantCodeValue;
        public String TenantNameValue;
        public  String CustomerCodeValue;
        public  String CustomerNameValue;
        public  String CustomerTRNNumberValue;
        public  String CustomerAddressValue;
        public  String SupplierTRNValue;
        public  String SuppliernameValue;
        public  String DateValue;
        public  String TimeValue;
        public  String UserNameValue;
        public  String UserIDValue;
        public  String RegisteredAddressValue;
        public String TeliphoneValue;
        public  String FaxValue;
        public  String EmailValue;
        public  String WebsiteValue;
        public  String PreviousMeterreadingValue;
        public  String PresentMeterreadingValue;
        public  String UnitsConsumedValue;
        public  String PressureFactorValue;
        public  String ActualUnitConsumedValue;
        public  String TotalExcludingTaxValue;
        public  String TotalIncludingTaxValue;
        public  String TotalVatValue;
        public String messageValue;
        public int statusValue;


        ArrayList<BillDetails> details_list=new ArrayList<>();

        public Deposit_Invoice_Model(String value)
        {
            try{
                JSONObject jsonobject=new JSONObject(value);
                JSONObject result=jsonobject.getJSONObject("RESULT");
                JSONObject tab=result.getJSONObject("TAB");
                JSONArray group=result.getJSONArray("GRP");
                JSONArray headerData=group.getJSONObject(0).getJSONArray("FLD");
                JSONArray statusData=group.getJSONObject(1).getJSONArray("FLD");
                JSONArray dataArray=tab.getJSONArray("LIN");

                for (int i = 0; i < statusData.length(); i++) {
                    JSONObject jObject = statusData.getJSONObject(i);

                    if (jObject.getString("NAME").equalsIgnoreCase(status)) {
                        statusValue = jObject.isNull("content") ? 1 : jObject.getInt("content");
                    } else if (jObject.getString("NAME").equalsIgnoreCase(message)) {
                        messageValue = jObject.isNull("content") ? "" : jObject.getString("content");
                    }
                }

                /*************************************************************************************************************/
                for (int i = 0; i < headerData.length(); i++) {
                    JSONObject jsonObject =headerData.getJSONObject(i);


                    if (jsonObject.getString("NAME").equalsIgnoreCase(Invoice_Number )) {
                        Invoice_NumberValue = jsonObject.isNull("content") ? "": jsonObject.getString("content");
                    } else if (jsonObject.getString("NAME").equalsIgnoreCase(Projectname)) {
                        ProjectnameValue = jsonObject.isNull("content") ? "" : jsonObject.getString("content");
                    }
                    else if (jsonObject.getString("NAME").equalsIgnoreCase(TenantCode)) {
                        TenantCodeValue = jsonObject.isNull("content") ? "" : jsonObject.getString("content");
                    }
                    else if (jsonObject.getString("NAME").equalsIgnoreCase(TenantName)) {
                        TenantNameValue = jsonObject.isNull("content") ? "" : jsonObject.getString("content");
                    }else if (jsonObject.getString("NAME").equalsIgnoreCase(CustomerCode)) {
                        CustomerCodeValue = jsonObject.isNull("content") ? "" : jsonObject.getString("content");
                    }
                    else if (jsonObject.getString("NAME").equalsIgnoreCase(CustomerName)) {
                        CustomerNameValue = jsonObject.isNull("content") ? "" : jsonObject.getString("content");
                    }
                    else if (jsonObject.getString("NAME").equalsIgnoreCase(CustomerTRNNumber)) {
                        CustomerTRNNumberValue = jsonObject.isNull("content") ? "" : jsonObject.getString("content");
                    }
                    else if (jsonObject.getString("NAME").equalsIgnoreCase(CustomerAddress)) {
                        CustomerAddressValue= jsonObject.isNull("content") ? "" : jsonObject.getString("content");
                    }
                    else if (jsonObject.getString("NAME").equalsIgnoreCase(SupplierTRN)) {
                        SupplierTRNValue= jsonObject.isNull("content") ? "" : jsonObject.getString("content");
                    }else if (jsonObject.getString("NAME").equalsIgnoreCase(Suppliername)) {
                        SuppliernameValue = jsonObject.isNull("content") ? "" : jsonObject.getString("content");
                    }else if (jsonObject.getString("NAME").equalsIgnoreCase(Date)) {
                        DateValue = jsonObject.isNull("content") ? "" : jsonObject.getString("content");
                    }else if (jsonObject.getString("NAME").equalsIgnoreCase(Time)) {
                        TimeValue = jsonObject.isNull("content") ? "" : jsonObject.getString("content");
                    }
                    else if (jsonObject.getString("NAME").equalsIgnoreCase(UserName)) {
                        UserNameValue = jsonObject.isNull("content") ? "" : jsonObject.getString("content");
                    }else if (jsonObject.getString("NAME").equalsIgnoreCase(UserID)) {
                        UserIDValue = jsonObject.isNull("content") ? "" : jsonObject.getString("content");
                    }
                    else if (jsonObject.getString("NAME").equalsIgnoreCase(RegisteredAddress)) {
                        RegisteredAddressValue = jsonObject.isNull("content") ? "" : jsonObject.getString("content");
                    }else if (jsonObject.getString("NAME").equalsIgnoreCase(TotalExcludingTax)) {
                        TotalExcludingTaxValue = jsonObject.isNull("content") ? "" : jsonObject.getString("content");
                    }else if (jsonObject.getString("NAME").equalsIgnoreCase(TotalIncludingTax)) {
                        TotalIncludingTaxValue = jsonObject.isNull("content") ? "" : jsonObject.getString("content");
                    }else if (jsonObject.getString("NAME").equalsIgnoreCase(TotalVat)) {
                        TotalVatValue = jsonObject.isNull("content") ? "" : jsonObject.getString("content");
                    }







                }
                /*************************************************************************************************************/
                for(int i=0;i<dataArray.length();i++)
                {
                    BillDetails model=new BillDetails(dataArray.getJSONObject(i).getJSONArray("FLD"));
                    details_list.add(model);
                }
            }catch (Exception ex)
            {
                ex.fillInStackTrace();
            }
        }

        public String getUserNameValue() {
            return UserNameValue;
        }

        public String getUserIDValue() {
            return UserIDValue;
        }

        public String getDateValue() {
            return DateValue;
        }

        public String getTimeValue() {
            return TimeValue;
        }

        public  String getEmail() {
            return EmailValue;
        }

        public String getCustomerName() {
            return CustomerNameValue;
        }

        public  String getTime() {
            return TimeValue;
        }

        public  String getDate() {
            return DateValue;
        }

        public  String getUserName() {
            return UserNameValue;
        }

        public  String getCustomerAddress() {
            return CustomerAddressValue;
        }

        public  String getCustomerCode() {
            return CustomerCodeValue;
        }

        public  String getCustomerTRNNumber() {
            return CustomerTRNNumberValue;
        }

        public  String getFax() {
            return FaxValue;
        }



        public  String getProjectname() {
            return ProjectnameValue;
        }

        public String getPreviousMeterreading() {
            return PreviousMeterreadingValue;
        }

        public  String getPresentMeterreading() {
            return PresentMeterreadingValue;
        }

        public  String getPressureFactor() {
            return PressureFactorValue;
        }

        public  String getRegisteredAddress() {
            return RegisteredAddressValue;
        }

        public  String getSuppliername() {
            return SuppliernameValue;
        }

        public  String getSupplierTRN() {
            return SupplierTRNValue ;
        }

        public  String getTeliphone() {
            return TeliphoneValue;
        }



        public int getStatus() {
            return statusValue;
        }

        public  String getMessage() {
            return messageValue;
        }

        public String getCustomerCodeValue() {
            return CustomerCodeValue;
        }

        public String getInvoice_NumberValue() {
            return Invoice_NumberValue;
        }


        public String getCustomerNameValue() {
            return CustomerNameValue;
        }

        public String getCustomerTRNNumberValue() {
            return CustomerTRNNumberValue;
        }

        public String getProjectnameValue() {
            return ProjectnameValue;
        }

        public String getCustomerAddressValue() {
            return CustomerAddressValue;
        }

        public String getTenantCodeValue() {
            return TenantCodeValue;
        }

        public String getTenantNameValue() {
            return TenantNameValue;
        }



        public String getTotalExcludingTaxValue() {
            return TotalExcludingTaxValue;
        }

        public String getActualUnitConsumedValue() {
            return ActualUnitConsumedValue;
        }

        public String getTotalIncludingTaxValue() {
            return TotalIncludingTaxValue;
        }

        public String getTotalVatValue() {
            return TotalVatValue;
        }

        public ArrayList<BillDetails> getDetails_list()
        {
            return details_list;
        }

        public class BillDetails{

            public String ItemName = "O_PROD";
            public  String UnitofMeasure = "O_UNITS";
            public  String Quantity = "O_QTY";
            public  String UnitPrice = "O_UNITPRI";
            public  String Totalprice = "O_TOTPRI";



            public  String ItemNameValue;
            public  String UnitofMeasureValue;
            public  String QuantityValue;
            public  String UnitPriceValue;
            public  String TotalpriceValue;

            public BillDetails(JSONArray jsonArray) {
                try {

                    for (int i = 0; i < jsonArray.length(); i++) {
                        JSONObject jsonObject = jsonArray.getJSONObject(i);
                        if (jsonObject.getString("NAME").equalsIgnoreCase(ItemName)) {
                            ItemNameValue = jsonObject.isNull("content") ? "" : jsonObject.getString("content");
                        } else if (jsonObject.getString("NAME").equalsIgnoreCase(UnitofMeasure)) {
                            UnitofMeasureValue = jsonObject.isNull("content") ? "" : jsonObject.getString("content");
                        } else if (jsonObject.getString("NAME").equalsIgnoreCase(Quantity)) {
                            QuantityValue = jsonObject.isNull("content") ? "" : jsonObject.getString("content");
                        } else if (jsonObject.getString("NAME").equalsIgnoreCase(UnitPrice)) {
                            UnitPriceValue = jsonObject.isNull("content") ? "" : jsonObject.getString("content");
                        } else if (jsonObject.getString("NAME").equalsIgnoreCase(Totalprice)) {
                            TotalpriceValue = jsonObject.isNull("content") ? "" : jsonObject.getString("content");
                        }
                    }
                } catch (Exception ex) {
                    ex.fillInStackTrace();
                }
            }

            public String getQuantityValue() {
                return QuantityValue;
            }

            public String getItemNameValue() {
                return ItemNameValue;
            }

            public String getUnitofMeasureValue() {
                return UnitofMeasureValue;
            }

            public String getUnitPriceValue() {
                return UnitPriceValue;
            }

            public String getTotalpriceValue() {
                return TotalpriceValue;
            }


        }


    }


