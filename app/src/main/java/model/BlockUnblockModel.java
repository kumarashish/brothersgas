package model;

import org.json.JSONArray;
import org.json.JSONObject;

import common.Common;

public class BlockUnblockModel {
    String message;
    int status;
    String adminCharges;
   public  BlockUnblockModel(JSONArray jsonArray)
   {  try {
       for (int i = 0; i < jsonArray.length(); i++) {
           JSONObject jsonObject = jsonArray.getJSONObject(i);

           if (jsonObject.getString("NAME").equalsIgnoreCase("O_FLG")) {
               status = jsonObject.isNull("content") ? 1 : jsonObject.getInt("content");
           } else if (jsonObject.getString("NAME").equalsIgnoreCase("O_MSG")) {
               message = jsonObject.isNull("content") ? "" : jsonObject.getString("content");
           }else if(jsonObject.getString("NAME").equalsIgnoreCase("O_ADMCHRINV"))
           {
               adminCharges= jsonObject.isNull("content") ? "" : jsonObject.getString("content");
           }
       }
   }catch (Exception ex)
   { ex.fillInStackTrace();
   }
   }

    public String getAdminCharges() {
        return adminCharges;
    }

    public String getMessage() {
        return message;
    }

    public int getStatus() {
        return status;
    }
}
