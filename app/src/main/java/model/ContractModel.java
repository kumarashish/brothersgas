package model;

import org.json.JSONArray;
import org.json.JSONObject;

import common.Common;

public class ContractModel {
    public static String contract_Meternumber=  "";
    public static String  customername = "";
    public static String customercode ="";
    public static String placeName="";
    public static String addresscode="";
    public static String contactcreationdate ="";
    public static int block_unblockflag  ;
    public static int closemeterreadingvalue;


    public ContractModel(JSONArray jsonArray)
    {
        try{
            for(int i=0;i<jsonArray.length();i++)
            {
                JSONObject jsonObject=jsonArray.getJSONObject(i);
                if(jsonObject.getString("NAME").equalsIgnoreCase(Common.contract_Meternumber))
                {
                    contract_Meternumber=jsonObject.getString("content");
                }else if(jsonObject.getString("NAME").equalsIgnoreCase(Common.customername))
                {
                    customername=jsonObject.getString("content");
                }
                else if(jsonObject.getString("NAME").equalsIgnoreCase(Common.placeName))
                {
                    placeName=jsonObject.getString("content");
                }else if(jsonObject.getString("NAME").equalsIgnoreCase(Common.customercode))
                {
                    customercode=jsonObject.getString("content");
                }
                else if(jsonObject.getString("NAME").equalsIgnoreCase(Common.addresscode))
                {
                    addresscode=jsonObject.getString("content");
                }else if(jsonObject.getString("NAME").equalsIgnoreCase(Common.contactcreationdate))
                {
                    contactcreationdate =jsonObject.getString("content");
                }
                else if(jsonObject.getString("NAME").equalsIgnoreCase(Common.block_unblockflag))
                {
                    block_unblockflag =jsonObject.getInt("content");
                }
                else if(jsonObject.getString("NAME").equalsIgnoreCase(Common.closemeterreadingvalue))
                {
                   closemeterreadingvalue =jsonObject.getInt("content");
                }
            }
        }catch (Exception ex)
        {
            ex.fillInStackTrace();
        }
    }

    public  int getBlock_unblockflag() {
        return block_unblockflag;
    }

    public  int getClosemeterreadingvalue() {
        return closemeterreadingvalue;
    }

    public  String getAddresscode() {
        return addresscode;
    }

    public  String getContactcreationdate() {
        return contactcreationdate;
    }

    public  String getContract_Meternumber() {
        return contract_Meternumber;
    }

    public  String getCustomercode() {
        return customercode;
    }

    public  String getCustomername() {
        return customername;
    }

    public  String getPlaceName() {
        return placeName;
    }
}
