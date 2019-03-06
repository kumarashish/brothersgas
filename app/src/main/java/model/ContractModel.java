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
    public String adminCharges="";


    public ContractModel(JSONArray jsonArray)
    {
        try{
            for(int i=0;i<jsonArray.length();i++)
            {
                JSONObject jsonObject=jsonArray.getJSONObject(i);
                if((jsonObject.getString("NAME").equalsIgnoreCase(Common.contract_Meternumber))||(jsonObject.getString("NAME").equalsIgnoreCase(Common.contract_Meternumber1)))
                {
                    contract_Meternumber=jsonObject.isNull("content")?"":jsonObject.getString("content");
                }else if((jsonObject.getString("NAME").equalsIgnoreCase(Common.customername))||(jsonObject.getString("NAME").equalsIgnoreCase(Common.customername1)))
                {
                    customername=jsonObject.isNull("content")?"":jsonObject.getString("content");
                }
                else if((jsonObject.getString("NAME").equalsIgnoreCase(Common.placeName))||(jsonObject.getString("NAME").equalsIgnoreCase(Common.placeName1)))
                {
                    placeName=jsonObject.isNull("content")?"":jsonObject.getString("content");
                }else if((jsonObject.getString("NAME").equalsIgnoreCase(Common.customercode))||(jsonObject.getString("NAME").equalsIgnoreCase(Common.customercode1)))
                {
                    customercode=jsonObject.isNull("content")?"":jsonObject.getString("content");
                }
                else if((jsonObject.getString("NAME").equalsIgnoreCase(Common.addresscode))||(jsonObject.getString("NAME").equalsIgnoreCase(Common.addresscode1)))
                {
                    addresscode=jsonObject.isNull("content")?"":jsonObject.getString("content");
                }else if((jsonObject.getString("NAME").equalsIgnoreCase(Common.contactcreationdate))||(jsonObject.getString("NAME").equalsIgnoreCase(Common.contactcreationdate1)))
                {
                    contactcreationdate =jsonObject.isNull("content")?"":jsonObject.getString("content");
                }
                else if((jsonObject.getString("NAME").equalsIgnoreCase(Common.block_unblockflag))||(jsonObject.getString("NAME").equalsIgnoreCase(Common.block_unblockflag1)))
                {
                    block_unblockflag =jsonObject.getInt("content");
                }
                else if((jsonObject.getString("NAME").equalsIgnoreCase(Common.closemeterreadingvalue))||(jsonObject.getString("NAME").equalsIgnoreCase(Common.closemeterreadingvalue1)))
                {
                   closemeterreadingvalue =jsonObject.getInt("content");
                }
                else if((jsonObject.getString("NAME").equalsIgnoreCase(Common.depositInvoice))||(jsonObject.getString("NAME").equalsIgnoreCase(Common.depositInvoice1)))
                {
                    depositInvoice =jsonObject.isNull("content")?"":jsonObject.getString("content");
                }
                else if((jsonObject.getString("NAME").equalsIgnoreCase(Common.connection_discconectionInvoice))||(jsonObject.getString("NAME").equalsIgnoreCase(Common.connection_discconectionInvoice1)))
                {
                    connection_discconectionInvoice=jsonObject.isNull("content")?"":jsonObject.getString("content");
                }else if((jsonObject.getString("NAME").equalsIgnoreCase(Common.project))||(jsonObject.getString("NAME").equalsIgnoreCase(Common.project1)))
                {
                     project=jsonObject.isNull("content")?"":jsonObject.getString("content");
                }else if((jsonObject.getString("NAME").equalsIgnoreCase(Common.projectDescription))||(jsonObject.getString("NAME").equalsIgnoreCase(Common.projectDescription1)))
                {
                    projectDesc=jsonObject.isNull("content")?"":jsonObject.getString("content");
                }
                else if((jsonObject.getString("NAME").equalsIgnoreCase(Common.owner))||(jsonObject.getString("NAME").equalsIgnoreCase(Common.owner1)))
                {
                    owner=jsonObject.isNull("content")?"":jsonObject.getString("content");
                }
                else if((jsonObject.getString("NAME").equalsIgnoreCase(Common.ownerDescription))||(jsonObject.getString("NAME").equalsIgnoreCase(Common.ownerDescription1)))
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

    public void setAdminInvoiceCharges(String adminCharges) {
        this.adminCharges = adminCharges;
    }

    public String getAdminInvoiceCharges() {
        return adminCharges;
    }
}
