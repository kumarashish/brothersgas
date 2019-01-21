package common;


import org.json.JSONObject;
import org.ksoap2.HeaderProperty;
import org.ksoap2.SoapEnvelope;
import org.ksoap2.serialization.SoapObject;
import org.ksoap2.serialization.SoapSerializationEnvelope;
import org.ksoap2.transport.HttpTransportSE;

import java.net.SocketTimeoutException;
import java.util.ArrayList;
import java.util.List;

public class WebServiceAcess {

    //public boolean runRequest() {
        // System.out.println("CUSTOMER ID " + customerId);
//        String NAMESPACE = "http://www.adonix.com/WSS";
//        String METHOD_NAME = "run";
//        String SOAP_ACTION = "CAdxWebServiceXmlCC";
//        preferenceUtils = new PreferenceUtils(mContext);
//        username = preferenceUtils.getStringFromPreference(PreferenceUtils.A_USER_NAME, "");
//        password = preferenceUtils.getStringFromPreference(PreferenceUtils.A_PASSWORD, "");
//        ip = preferenceUtils.getStringFromPreference(PreferenceUtils.IP_ADDRESS, "");
//        port = preferenceUtils.getStringFromPreference(PreferenceUtils.PORT, "");
//        pool = preferenceUtils.getStringFromPreference(PreferenceUtils.ALIAS, "");
//        String URL = "http://" + ip + ":" + port + "/soap-generic/syracuse/collaboration/syracuse/CAdxWebServiceXmlCC";
//
//        SoapObject request = new SoapObject(NAMESPACE, METHOD_NAME);
//        request.addProperty("publicName", "X10CUSRLGN");
//        JSONObject jsonObject = new JSONObject();
//        try {
//
//            jsonObject.put("I_X10CUSRID", driverId);
//            jsonObject.put("I_XDATE", date);
//            jsonObject.put("I_XLOGINDAT", loginDate);
//            jsonObject.put("I_XLOGINTIM", loginTime);
//
//
//
//        } catch (Exception e) {
//            System.out.println("Exception " + e);
//        }
//        request.addProperty("inputXml", jsonObject.toString());
//
//        SoapObject callcontext = new SoapObject("", "callContext");
//        // Set all input params
//        callcontext.addProperty("codeLang", "ENG");
//        callcontext.addProperty("poolAlias", pool);
//        callcontext.addProperty("poolId", "");
//        callcontext.addProperty("codeUser", username);
//        callcontext.addProperty("password", password);
//        callcontext.addProperty("requestConfig", "adxwss.trace.on=off");
//
//        request.addSoapObject(callcontext);
//
//
//        SoapSerializationEnvelope envelope = new SoapSerializationEnvelope(SoapEnvelope.VER11);
//
//        envelope.setOutputSoapObject(request);
//
//        HttpTransportSE androidHttpTransport = new HttpTransportSE(URL);
//        androidHttpTransport.debug = true;
//
//        try {
//            List<HeaderProperty> headerList = new ArrayList<HeaderProperty>();
//            headerList.add(new HeaderProperty("Authorization", "Basic " + org.kobjects.base64.Base64.encode((username + ":" + password).getBytes())));
//
//
//            androidHttpTransport.call(SOAP_ACTION, envelope, headerList);
//
//
//            SoapObject response = (SoapObject) envelope.getResponse();
//            String resultXML = (String) response.getProperty("resultXml");
//            if (resultXML != null && resultXML.length() > 0) {
//                return parseXML(resultXML);
//            } else {
//                return false;
//            }
//        } catch (SocketTimeoutException e) {
//            e.printStackTrace();
//            return false;
//        } catch (Exception e) {
//            e.printStackTrace();
//            return false;
//        }
//
//    }


    }
