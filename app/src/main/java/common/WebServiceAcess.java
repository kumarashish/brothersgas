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
public WebServiceAcess(){}

    public String runRequest() {
         //System.out.println("CUSTOMER ID " + customerId);
        String NAMESPACE = "http://www.adonix.com/WSS";
        String WSDL="";
        String METHOD_NAME = "run";
        String SOAP_ACTION = "CAdxWebServiceXmlCC";
        String URL = "http://" + "47.91.105.187" + ":" + "8124" + "/soap-wsdl/syracuse/collaboration/syracuse/CAdxWebServiceXmlCC";
        SoapObject request = new SoapObject(NAMESPACE, METHOD_NAME);
        request.addProperty("publicName", "YMTRLOGIN");
        JSONObject jsonObject = new JSONObject();
        try {
            jsonObject.put("I_UID", "SRIKANTH");
            jsonObject.put("I_PWD", "sri123");
        } catch (Exception e) {
            System.out.println("Exception " + e);
        }
        request.addProperty("inputXml", jsonObject.toString());
        SoapObject callcontext = new SoapObject("", "callContext");
        // Set all input params
        callcontext.addProperty("codeLang", "ENG");
        callcontext.addProperty("poolAlias", "BROSGST");
        callcontext.addProperty("poolId", "");
        callcontext.addProperty("requestConfig", "");
        request.addSoapObject(callcontext);
        SoapSerializationEnvelope envelope = new SoapSerializationEnvelope(SoapEnvelope.VER11);
        envelope.setOutputSoapObject(request);
        HttpTransportSE androidHttpTransport = new HttpTransportSE(URL);
        androidHttpTransport.debug = true;
        try {
            List<HeaderProperty> headerList = new ArrayList<HeaderProperty>();
            headerList.add(new HeaderProperty("Authorization", "Basic " + org.kobjects.base64.Base64.encode(("admin" + ":" +"admin").getBytes())));
            androidHttpTransport.call(SOAP_ACTION, envelope,headerList);
            SoapObject response = (SoapObject) envelope.getResponse();
            String resultXML = (String) response.getProperty("resultXml");
            if (resultXML != null && resultXML.length() > 0) {
                return resultXML;
            } else {
                return "";
            }
        } catch (SocketTimeoutException e) {
            e.printStackTrace();
            return "";
        } catch (Exception e) {
            e.printStackTrace();
            return "";
        }

    }


    }
