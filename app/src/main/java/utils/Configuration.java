package utils;

public class Configuration {
    public static String Port;
    public static String IP;
    public static String ALIAS;
    public static String USERNAME;
    public static String PASSWORD;


    public static void setConfiguration(String ip,String port,String alias,String username,String password)
    {
        IP=ip;
        Port=port;
      ALIAS=alias;
      USERNAME=username;
      PASSWORD=password;
    }

    public static String getIP() {
        return IP;
    }

    public static String getALIAS() {
        return ALIAS;
    }

    public static String getPort() {
        return Port;
    }

    public static String getPASSWORD() {
        return PASSWORD;
    }

    public static String getUSERNAME() {
        return USERNAME;
    }
}
