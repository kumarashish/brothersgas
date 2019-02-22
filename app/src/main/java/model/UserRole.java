package model;

import org.json.JSONArray;
import org.json.JSONObject;

import common.Common;

public class UserRole {
    public int AdminAcess;
    public int ContractsAcess;
    public int Connection_DisconnectionAcess;
    public int ConsumptionsAcess;
    public int BlockAcess;
    public int Status;
    public String Message;

    public UserRole(String value)
    {
        if(value.length()>0)
        {
            try {
                JSONObject jsonObjectt = new JSONObject(value);
                JSONObject jsonResult=jsonObjectt.getJSONObject("RESULT");
                JSONArray resultArray=jsonResult.getJSONArray("GRP");
                JSONObject grp=resultArray.getJSONObject(1);
                JSONArray field=grp.getJSONArray("FLD");
                for(int i=0;i<field.length();i++)
                {
                    JSONObject jsonObject=field.getJSONObject(i);
                    if(jsonObject.getString("NAME").equalsIgnoreCase(Common.AdminAcess))
                    {
                        AdminAcess=jsonObject.isNull("content")?1:jsonObject.getInt("content");
                    }else if(jsonObject.getString("NAME").equalsIgnoreCase(Common.ContractsAcess))
                    {
                        ContractsAcess=jsonObject.isNull("content")?1:jsonObject.getInt("content");
                    }
                    else if(jsonObject.getString("NAME").equalsIgnoreCase(Common.Connection_DisconnectionAcess))
                    {
                        Connection_DisconnectionAcess=jsonObject.isNull("content")?1:jsonObject.getInt("content");
                    }
                    else if(jsonObject.getString("NAME").equalsIgnoreCase(Common.ConsumptionsAcess))
                    {
                        ConsumptionsAcess=jsonObject.isNull("content")?1:jsonObject.getInt("content");
                    }else if(jsonObject.getString("NAME").equalsIgnoreCase(Common.BlockAcess))
                    {
                        BlockAcess=jsonObject.isNull("content")?1:jsonObject.getInt("content");
                    }
                    else if(jsonObject.getString("NAME").equalsIgnoreCase(Common.Status))
                    {
                       Status=jsonObject.isNull("content")?1:jsonObject.getInt("content");
                    }
                    else if(jsonObject.getString("NAME").equalsIgnoreCase(Common.Message))
                    {
                       Message=jsonObject.isNull("content")?"":jsonObject.getString("content");
                    }
                }

            }catch (Exception ex)
            {
                ex.fillInStackTrace();
            }

        }

    }

    public int getConnection_DisconnectionAcess() {
        return Connection_DisconnectionAcess;
    }

    public int getAdminAcess() {
        return AdminAcess;
    }

    public int getBlockAcess() {
        return BlockAcess;
    }

    public int getConsumptionsAcess() {
        return ConsumptionsAcess;
    }

    public int getContractsAcess() {
        return ContractsAcess;
    }

    public int getStatus() {
        return Status;
    }

    public String getMessage() {
        return Message;
    }
}
