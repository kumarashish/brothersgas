package utils;

import org.json.JSONArray;
import org.json.JSONObject;

/**
 * Created by ashish.kumar on 23-01-2019.
 */

public class Utils {

    public static boolean isUserLoggedIn(String value) {
        try {
            JSONObject jsonObject = new JSONObject(value);
            JSONObject result = jsonObject.getJSONObject("RESULT");
            JSONArray groups = result.getJSONArray("GRP");
            JSONObject statusJSon = groups.getJSONObject(1);
            JSONObject statusValueJSON = statusJSon.getJSONObject("FLD");
            if (statusValueJSON.getInt("content") == 2) {
                return true;
            }

        } catch (Exception ex) {
            ex.fillInStackTrace();
        }
        return false;
    }
}
