package model;

import org.json.JSONArray;
import org.json.JSONObject;

import common.Common;

public class ContractModel {
    public  String contract_Meternumber=  "";
    public  String  customername = "";
    public  String customercode ="";
    public  String placeName="";
    public String addresscode="";
    public String contactcreationdate ="";
    public  int block_unblockflag  ;
    public  int closemeterreadingvalue;
    public   String depositInvoice="";
    public  String connection_discconectionInvoice="";
    public   String project="";
    public   String projectDesc="";
    public  String owner="";
    public  String ownerDesc="";


    public ContractModel(JSONArray jsonArray)
    {
        try{
            for(int i=0;i<jsonArray.length();i++)
            {
                JSONObject jsonObject=jsonArray.getJSONObject(i);
                if(jsonObject.getString("NAME").equalsIgnoreCase(Common.contract_Meternumber))
                {
                    contract_Meternumber=jsonObject.isNull("content")?"":jsonObject.getString("content");
                }else if(jsonObject.getString("NAME").equalsIgnoreCase(Common.customername))
                {
                    customername=jsonObject.isNull("content")?"":jsonObject.getString("content");
                }
                else if(jsonObject.getString("NAME").equalsIgnoreCase(Common.placeName))
                {
                    placeName=jsonObject.isNull("content")?"":jsonObject.getString("content");
                }else if(jsonObject.getString("NAME").equalsIgnoreCase(Common.customercode))
                {
                    customercode=jsonObject.isNull("content")?"":jsonObject.getString("content");
                }
                else if(jsonObject.getString("NAME").equalsIgnoreCase(Common.addresscode))
                {
                    addresscode=jsonObject.isNull("content")?"":jsonObject.getString("content");
                }else if(jsonObject.getString("NAME").equalsIgnoreCase(Common.contactcreationdate))
                {
                    contactcreationdate =jsonObject.isNull("content")?"":jsonObject.getString("content");
                }
                else if(jsonObject.getString("NAME").equalsIgnoreCase(Common.block_unblockflag))
                {
                    block_unblockflag =jsonObject.getInt("content");
                }
                else if(jsonObject.getString("NAME").equalsIgnoreCase(Common.closemeterreadingvalue))
                {
                   closemeterreadingvalue =jsonObject.getInt("content");
                }
                else if(jsonObject.getString("NAME").equalsIgnoreCase(Common.depositInvoice))
                {
                    depositInvoice =jsonObject.isNull("content")?"":jsonObject.getString("content");
                }
                else if(jsonObject.getString("NAME").equalsIgnoreCase(Common.connection_discconectionInvoice))
                {
                    connection_discconectionInvoice=jsonObject.isNull("content")?"":jsonObject.getString("content");
                }else if(jsonObject.getString("NAME").equalsIgnoreCase(Common.project))
                {
                     project=jsonObject.isNull("content")?"":jsonObject.getString("content");
                }else if(jsonObject.getString("NAME").equalsIgnoreCase(Common.projectDescription))
                {
                    projectDesc=jsonObject.isNull("content")?"":jsonObject.getString("content");
                }
                else if(jsonObject.getString("NAME").equalsIgnoreCase(Common.owner))
                {
                    owner=jsonObject.isNull("content")?"":jsonObject.getString("content");
                }
                else if(jsonObject.getString("NAME").equalsIgnoreCase(Common.ownerDescription))
                {
                    ownerDesc=jsonObject.isNull("content")?"":jsonObject.getString("content");
                }
            }
        }catch (Exception ex)
        {
            ex.fillInStackTrace();
        }
    }

    public String getOwner() {
        return owner;
    }

    public String getOwnerDesc() {
        return ownerDesc;
    }

    public String getProject() {
        return project;
    }

    public  String getConnection_discconectionInvoice() {
        return connection_discconectionInvoice;
    }

    public  String getDepositInvoice() {
        return depositInvoice;
    }

    public  int getBlock_unblockflag() {
        return block_unblockflag;
    }

    public void setBlock_unblockflag(int block_unblockflag) {
        this.block_unblockflag = block_unblockflag;
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

    public String getProjectDesc() {
        return projectDesc;
    }
}
