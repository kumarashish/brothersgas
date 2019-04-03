package model;

import org.json.JSONArray;
import org.json.JSONObject;

import common.Common;

/**
 * Created by ashish.kumar on 03-04-2019.
 */

public class OwnerModel {
    String ownerName;
    String ownerCode;

    public OwnerModel(JSONArray jsonArray) {
        try {
            for (int i = 0; i < jsonArray.length(); i++) {
                JSONObject jsonObject = jsonArray.getJSONObject(i);
                if (jsonObject.getString("NAME").equalsIgnoreCase("O_RONAME")) {
                    ownerName = jsonObject.isNull("content") ? "" : jsonObject.getString("content");
                } else if (jsonObject.getString("NAME").equalsIgnoreCase("O_ROCODE")) {
                    ownerCode = jsonObject.isNull("content") ? "" : jsonObject.getString("content");
                }
            }
        } catch (Exception ex) {
            ex.fillInStackTrace();
        }
    }

    public String getProjectCode() {
        return ownerCode;
    }

    public String getProjectName() {
        return ownerName;
    }
}
