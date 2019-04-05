package common;

import android.app.Application;
import android.os.Environment;
import android.os.StrictMode;

import org.json.JSONArray;
import org.json.JSONObject;
import org.json.JSONTokener;

import java.util.ArrayList;

import model.OwnerModel;
import model.UserRole;
import utils.Configuration;
import utils.Utils;

public class AppController extends Application {
    AppController controller;
    PrefManager manager;
    UserRole userRole=null;
    ArrayList<OwnerModel>ownerList=new ArrayList<>();
    @Override
    public void onCreate() {
        super.onCreate();
        controller=this;
        manager= new PrefManager(getApplicationContext());
        Configuration.setConfiguration(manager.getIp(),manager.getPort(),manager.getAlias(),manager.getUserName(),manager.getPassword());
        String role=manager.getUserRole();
        if(role.length()>0) {
            userRole = new UserRole(role);
        }
        Utils.makeFolder(String.valueOf(Environment.getExternalStorageDirectory()), "/BrothersGas");
        Common.sdCardPath = Environment.getExternalStorageDirectory() + "/BrothersGas";
        StrictMode.VmPolicy.Builder builder = new StrictMode.VmPolicy.Builder();
        StrictMode.setVmPolicy(builder.build());
        if ((controller.getManager().isUserLoggedIn()) && (manager.getSyncData().length() > 0)) {
            try {
                Object json = new JSONTokener(manager.getSyncData()).nextValue();
                if (json instanceof JSONArray) {
                    JSONArray jsonArray = new JSONArray(manager.getSyncData());
                    for (int i = 0; i < jsonArray.length(); i++) {
                        OwnerModel model = new OwnerModel(jsonArray.getJSONObject(i).getJSONArray("FLD"));
                        ownerList.add(model);
                    }
                } else {
                    OwnerModel model = new OwnerModel(((JSONObject) json).getJSONArray("FLD"));
                    ownerList.add(model);
                }
            } catch (Exception ex) {
                ex.fillInStackTrace();
            }
        }

    }

    public ArrayList<OwnerModel> getOwnerList() {
        return ownerList;
    }

    public void setOwnerList(String data, String syncDate) {
        ownerList.clear();
        manager.saveSyncData(data, syncDate);
        try {
            Object json = new JSONTokener(manager.getSyncData()).nextValue();
            if (json instanceof JSONArray) {
                JSONArray jsonArray = new JSONArray(manager.getSyncData());
                for (int i = 0; i < jsonArray.length(); i++) {
                    OwnerModel model = new OwnerModel(jsonArray.getJSONObject(i).getJSONArray("FLD"));
                    ownerList.add(model);
                }
            } else {
                OwnerModel model = new OwnerModel(((JSONObject) json).getJSONArray("FLD"));
                ownerList.add(model);
            }
        } catch (Exception ex) {
            ex.fillInStackTrace();
        }
    }

   public ArrayList<String > getOwnerNameList()
   {
       ArrayList<String > names=new ArrayList<>();
       for (int i = 0; i <ownerList.size(); i++) {

           names.add(ownerList.get(i).getProjectCode()+" - "+ownerList.get(i).getProjectName());
       }
       return names;
   }
    public AppController getController() {
        return controller;
    }

    public PrefManager getManager() {
        return manager;
    }
    public void setRole(String role)
    {
        userRole=new UserRole(role);
        manager.setUserRole(role);
    }

    public UserRole getUserRole() {
        return userRole;
    }

    public void logout() {
        userRole=null;
        manager.setUserRole("");
        manager.setUserLoggedIn(false);
    }
}
