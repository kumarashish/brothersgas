package common;

import android.content.Context;
import android.content.SharedPreferences;

import org.json.JSONObject;


/**
 * Created by Ashish.Kumar on 16-01-2018.
 */

public class PrefManager {
    SharedPreferences pref;
    SharedPreferences.Editor editor;
    Context _context;

    // shared pref mode
    int PRIVATE_MODE = 0;
    private static final String PREF_NAME = "BrothersGas";
    private static final String LoggedIn = "BrothersGasLoggedIn";
    private static final String loggedInUserName="BrothersGasLoggedInUserName";
    private static final String loggedInUserPassword="BrothersGasLoggedInUserPassword";
    private static final String rememberId = "BrothersGasRemId";
    private static final String rememberpassword = "BrothersGasRemPass";



    public PrefManager(Context context) {
        this._context = context;
        pref = _context.getSharedPreferences(PREF_NAME, PRIVATE_MODE);
        editor = pref.edit();
    }


    public SharedPreferences getPref() {
        return pref;
    }

    public void clearPreferences() {
        editor.clear().commit();
    }
    public void setUserLoggedIn(boolean isloggedIn) {
        editor.putBoolean(LoggedIn , isloggedIn);
        editor.commit();
    }

    public boolean isUserLoggedIn()
    {
       return pref.getBoolean(LoggedIn,false);
    }

    public void setConfiguration(String ip,String port, String alias, String username, String password)
    {
        editor.putString(Common.ip,ip);
        editor.putString(Common.PORT,port);
        editor.putString(Common.ALIAS,alias);
        editor.putString(Common.USERNAME,username);
        editor.putString(Common.PASSWORD,password);
        editor.apply();
    }

   public String getIp()
   {
      return pref.getString(Common.ip,"");
   }
   public String getAlias()
   {
       return   pref.getString(Common.ALIAS,"");
   }
    public String getUserName()
    {
        return pref.getString(Common.USERNAME,"");
    }
    public String getPassword()
    {
        return   pref.getString(Common.PASSWORD,"");
    }
    public String getPort()
    {
        return   pref.getString(Common.PORT,"");
    }


    public void setLoggedInUserDetails(String s, String s1) {
        editor.putString(loggedInUserName,s);
        editor.putString(loggedInUserPassword,s1);
        editor.apply();
    }

    public static String getLoggedInUserName() {
        return loggedInUserName;
    }

    public static String getLoggedInUserPassword() {
        return loggedInUserPassword;
    }
}