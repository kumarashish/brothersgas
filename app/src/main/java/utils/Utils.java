package utils;

import android.app.Activity;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.net.ConnectivityManager;
import android.support.v7.app.AlertDialog;

import org.json.JSONArray;
import org.json.JSONObject;

import consumption.ConsumptionList;
import invoices.Print_Email;

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




    public static boolean isNetworkAvailable(Context context) {
        return ((ConnectivityManager) context
                .getSystemService(Context.CONNECTIVITY_SERVICE))
                .getActiveNetworkInfo() != null;
    }

    public static void showAlert(final Activity act, String message)
    {
        AlertDialog.Builder builder1 = new AlertDialog.Builder( act);
        builder1.setMessage(message);
        builder1.setCancelable(true);
        builder1.setNeutralButton(
                "Ok",
                new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int id) {
                        dialog.cancel();
                        act.finish();
                    }
                });

        AlertDialog alert11 = builder1.create();
        alert11.show();
    }
    public static void showAlertNavigateToPrintEmail(final Activity act, String message)
    {
        AlertDialog.Builder builder1 = new AlertDialog.Builder( act);
        builder1.setMessage(message);
        builder1.setCancelable(true);
        builder1.setNeutralButton(
                "Ok",
                new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int id) {
                      act.startActivity(new Intent(act, Print_Email.class));
                        act.finish();
                    }
                });

        AlertDialog alert11 = builder1.create();
        alert11.show();
    }
    public static void showAlertNormal(final Activity act, String message)
    {
        AlertDialog.Builder builder1 = new AlertDialog.Builder( act);
        builder1.setMessage(message);
        builder1.setCancelable(true);
        builder1.setNeutralButton(
                "Ok",
                new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int id) {
                        dialog.cancel();

                    }
                });

        AlertDialog alert11 = builder1.create();
        alert11.show();
    }


}
