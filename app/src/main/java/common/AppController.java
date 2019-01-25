package common;

import android.app.Application;
import android.content.Context;
import android.content.SharedPreferences;
import android.content.pm.PackageManager;
import android.util.Log;

import utils.Configuration;

public class AppController extends Application {
    AppController controller;
    PrefManager manager;
    @Override
    public void onCreate() {
        super.onCreate();
        controller=this;
        manager= new PrefManager(getApplicationContext());
        Configuration.setConfiguration(manager.getIp(),manager.getPort(),manager.getAlias(),manager.getUserName(),manager.getPassword());

    }

    public AppController getController() {
        return controller;
    }

    public PrefManager getManager() {
        return manager;
    }

    public void logout() {
        manager.setUserLoggedIn(false);
    }

    public static String getSharedPrefs(Context c) {
        try {
            Context con = c.createPackageContext("com.sportsfight.com.sportsfight", Context.MODE_PRIVATE);
            SharedPreferences pref = con.getSharedPreferences("demopref", Context.MODE_PRIVATE);
            String your_data =
                    pref.getString("demostring", "No Value");
            return your_data;
        } catch (PackageManager.NameNotFoundException e) {
            Log.e("Not data shared", e.toString());
        }
        return "";
    }
}
