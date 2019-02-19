package common;

import android.app.Application;

import model.UserRole;
import utils.Configuration;

public class AppController extends Application {
    AppController controller;
    PrefManager manager;
    UserRole userRole=null;
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
