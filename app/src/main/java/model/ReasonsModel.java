package model;

import org.json.JSONArray;
import org.json.JSONObject;

public class ReasonsModel {
    int reactivation;
    String reason;
   public ReasonsModel(JSONArray jsonArray)
    {
        try{
            for(int i=0;i<jsonArray.length();i++) {
                JSONObject jsonObject=jsonArray.getJSONObject(i);
                if (jsonObject.getString("NAME").equalsIgnoreCase("O_REACT"))
                {
                    reactivation=jsonObject.isNull("content")?1:jsonObject.getInt("content");
                }
                else if (jsonObject.getString("NAME").equalsIgnoreCase("O_REASON"))
                {
                    reason=jsonObject.isNull("content")?"":jsonObject.getString("content");
                }
            }
        }catch (Exception ex)
        {
            ex.fillInStackTrace();
        }
    }

    public int getReactivation() {
        return reactivation;
    }

    public String getReason() {
        return reason;
    }
}
