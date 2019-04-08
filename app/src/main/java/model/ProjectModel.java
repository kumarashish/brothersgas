package model;

import org.json.JSONArray;
import org.json.JSONObject;

/**
 * Created by ashish.kumar on 03-04-2019.
 */

public class ProjectModel {
    String projectName;
    String projectCode;

    public ProjectModel(JSONArray jsonArray) {
        try {
            for (int i = 0; i < jsonArray.length(); i++) {
                JSONObject jsonObject = jsonArray.getJSONObject(i);
                if (jsonObject.getString("NAME").equalsIgnoreCase("O_PRJCOD")) {
                    projectCode = jsonObject.isNull("content") ? "" : jsonObject.getString("content");
                } else if (jsonObject.getString("NAME").equalsIgnoreCase("O_PRJNAME")) {
                    projectName = jsonObject.isNull("content") ? "" : jsonObject.getString("content");
                }else if (jsonObject.getString("NAME").equalsIgnoreCase("O_PRJCOD")) {
                    projectCode = jsonObject.isNull("content") ? "" : jsonObject.getString("content");
                }
                else if (jsonObject.getString("NAME").equalsIgnoreCase("O_PRJNAME")) {
                    projectName= jsonObject.isNull("content") ? "" : jsonObject.getString("content");
                }
            }
        } catch (Exception ex) {
            ex.fillInStackTrace();
        }
    }

    public String getProjectName() {
        return projectName;
    }

    public String getProjectCode() {
        return projectCode;
    }
}
