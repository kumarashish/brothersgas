package model;

import org.json.JSONArray;
import org.json.JSONObject;

import common.Common;

/**
 * Created by ashish.kumar on 29-01-2019.*/



public class  ContractDetails {
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
    public  int block_unblockflag  ;
    public  int closemeterreadingvalue;
 public String contractNumber="";
    public String Currency="";
    public  String PreviousReading="";
    public  String PreviousDate="";
    public  String currentReading="";
    public  String sales_InvoiceNumber="";
    public  String sales_DeliveryNumber="";
    public String units ;
    public String blockFlag;
    public String closeFlag;
public String adminCharges;
public String consumptionInvoice="";
public String customerName="";
public String currentMeterReading="";

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
             else  if(jsonObject.getString("NAME").equalsIgnoreCase(Common.block_unblockflag))
             {
                 block_unblockflag=jsonObject.isNull("content")?0:jsonObject.getInt("content");
             }
             else  if(jsonObject.getString("NAME").equalsIgnoreCase(Common.closemeterreadingvalue))
             {
                 closemeterreadingvalue=jsonObject.isNull("content")?0:jsonObject.getInt("content");
             }

             else  if(jsonObject.getString("NAME").equalsIgnoreCase(Common.Currency))
             {
                Currency=jsonObject.isNull("content")?"":jsonObject.getString("content");
             }
             else  if(jsonObject.getString("NAME").equalsIgnoreCase(Common.PreviousDate))
             {
                PreviousDate=jsonObject.isNull("content")?"":jsonObject.getString("content");
             }
             else  if(jsonObject.getString("NAME").equalsIgnoreCase(Common.PreviousReading))
             {
                 PreviousReading=jsonObject.isNull("content")?"0":jsonObject.getString("content");
             }
             else  if(jsonObject.getString("NAME").equalsIgnoreCase(Common.units))
             {
                 units=jsonObject.isNull("content")?"":jsonObject.getString("content");
             } else  if(jsonObject.getString("NAME").equalsIgnoreCase(Common.blockFlag))
             {
                 blockFlag=jsonObject.isNull("content")?"1":jsonObject.getString("content");
             }else  if(jsonObject.getString("NAME").equalsIgnoreCase(Common.closeFlag))
             {
                 closeFlag=jsonObject.isNull("content")?"1":jsonObject.getString("content");
             }
         }}catch (Exception ex)
     {
         ex.fillInStackTrace();
     }
 }

    public String getBlockFlag() {
        return blockFlag;
    }

    public String getCloseFlag() {
        return closeFlag;
    }

    public String getUnits() {
        return "";
    }

    public void setConnection_Disconnection_Invoice(String connection_Disconnection_Invoice) {
        Connection_Disconnection_Invoice = connection_Disconnection_Invoice;
    }

    public String getPreviousDate() {
        return PreviousDate;
    }

    public String getPreviousReading() {
        return PreviousReading;
    }

    public void setDeposit_Invoice(String deposit_Invoice) {
        Deposit_Invoice = deposit_Invoice;
    }

    public String getCurrency() {
        return Currency;
    }

    public int getBlock_unblockflag() {
        return block_unblockflag;
    }

    public void setBlock_unblockflag(int block_unblockflag) {
        this.block_unblockflag = block_unblockflag;
    }

    public int getClosemeterreadingvalue() {
        return closemeterreadingvalue;
    }

    public void setClosemeterreadingvalue(int closemeterreadingvalue) {
        this.closemeterreadingvalue = closemeterreadingvalue;
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

    public void setSales_DeliveryNumber(String sales_DeliveryNumber) {
        this.sales_DeliveryNumber = sales_DeliveryNumber;
    }

    public void setSales_InvoiceNumber(String sales_InvoiceNumber) {
        this.sales_InvoiceNumber = sales_InvoiceNumber;
    }

    public void setCurrentReading(String currentReading) {
        this.currentReading = currentReading;
    }

    public String getCurrentReading() {
        return currentReading;
    }

    public String getSales_DeliveryNumber() {
        return sales_DeliveryNumber;
    }

    public String getSales_InvoiceNumber() {
        return sales_InvoiceNumber;
    }

    public String getContractNumber() {
        return contractNumber;
    }
    public void setAdminInvoiceCharges(String adminCharges) {
        this.adminCharges = adminCharges;
    }

    public String getAdminInvoiceCharges() {
        return adminCharges;
    }

    public void setCustomerName(String customerName) {
        this.customerName = customerName;
    }

    public String getCustomerName() {
        return customerName;
    }

    public void setConsumptionInvoice(String connectionInvoice) {
        this.consumptionInvoice= connectionInvoice;
    }

    public void setCurrentMeterReading(String currentMeterReading) {
        this.currentMeterReading = currentMeterReading;
    }

    public String getCurrentMeterReading() {
        return currentMeterReading;
    }

    public String getConsumptionInvoice() {
        return consumptionInvoice;
    }
}
