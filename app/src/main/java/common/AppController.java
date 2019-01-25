package common;

import android.app.Application;

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
}
