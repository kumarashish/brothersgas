package model;

import org.json.JSONArray;
import org.json.JSONObject;

public class BankModel {
    String bankName;
    String bankCode;

    public BankModel(JSONArray jsonArray)
    {
        try{
            for(int i=0;i<jsonArray.length();i++) {
                JSONObject jsonObject=jsonArray.getJSONObject(i);
                if (jsonObject.getString("NAME").equalsIgnoreCase("O_BNKNAM"))
                {
                    bankCode=jsonObject.isNull("content")?"":jsonObject.getString("content");
                }
                else if (jsonObject.getString("NAME").equalsIgnoreCase("O_BNKDES"))
                {
                    bankName=jsonObject.isNull("content")?"":jsonObject.getString("content");
                }
            }
        }catch (Exception ex)
        {
            ex.fillInStackTrace();
        }
    }

    public String getBankCode() {
        return bankCode;
    }

    public String getBankName() {
        return bankName;
    }
}



