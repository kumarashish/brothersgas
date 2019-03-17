package common;


import android.util.Log;

import org.json.JSONObject;
import org.ksoap2.HeaderProperty;
import org.ksoap2.SoapEnvelope;
import org.ksoap2.serialization.SoapObject;
import org.ksoap2.serialization.SoapSerializationEnvelope;
import org.ksoap2.transport.HttpTransportSE;

import java.net.SocketTimeoutException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import org.json.JSONException;
import org.json.JSONObject;
import org.json.XML;

import utils.Configuration;

public class WebServiceAcess {


   public static int login=1;

   public WebServiceAcess(){}


    /********************************************************************************************************/
    public JSONObject getRequestJson(String publicName,String [] value )
    {   JSONObject jsonObject = new JSONObject();
    try {
        switch (publicName) {
            case Common.LoginMethod:
            jsonObject.put("I_UID", value[0]);
            jsonObject.put("I_PWD", value[1]);
            break;
            case Common.ContractView:
                jsonObject.put("I_CONTNO", value[0]);
                break;
            case Common.CancelContract:
                jsonObject.put("I_CONTNO", value[0]);
                jsonObject.put("I_YCURRDNG", value[1]);
                break;
            case Common.BlockUnBlock:
                jsonObject.put("I_CONTNO", value[0]);
                jsonObject.put("I_FLG", value[1]);
                jsonObject.put("I_REASON", value[2]);
                jsonObject.put("I_REACTIVEDT", value[3]);
                jsonObject.put("I_YCURRDNG", value[4]);


                break;
            case Common.DepositInvoice:
                jsonObject.put("I_CONTNO", value[0]);
                jsonObject.put("I_FLG", value[1]);

                break;
            case Common.UpdateInitialReading:
                jsonObject.put("I_CONTNO", value[0]);
                jsonObject.put("I_INITRE", value[1]);
                break;
            case Common.GenerateDeliveryNote:


                jsonObject.put("I_CONTNO", value[0]);
                jsonObject.put("I_CURRDNG",value[1]);
                jsonObject.put("I_RETYPE", value[2]);
                jsonObject.put("I_METPROB",value[3]);
                jsonObject.put("I_REASON", value[4]);
                jsonObject.put("I_TYPE", "1");
                jsonObject.put("I_MPROBAMT", value[5]);


                break;
            case Common.Tennant_Change:
                jsonObject.put("I_CONTNO", value[0]);
                jsonObject.put("I_CURRED", value[1]);
                jsonObject.put("I_EMIID", value[2]);
                jsonObject.put("I_EMIDATE", value[3]);
                jsonObject.put("I_ADDLIG", value[4]);
                jsonObject.put("I_CUSCONT", value[5]);
                jsonObject.put("I_IDNUM", "");
                jsonObject.put("I_EMIMG", value[7]);
                jsonObject.put("I_EMIMG1", value[8]);
                jsonObject.put("I_EMAIL", value[6]);





                break;
            case Common.InvoiceSearch:
                jsonObject.put("I_YBPC", value[0]);
                break;
            case Common.CreatePayment:
                jsonObject.put("I_YSINVNO", value[0]);
                jsonObject.put("I_YCHKNO", value[1]);
                jsonObject.put("I_YOAMT",value[2]);
                jsonObject.put("I_YPAYTYP",value[3]);
                jsonObject.put("I_YBANK",value[4]);
                jsonObject.put("I_YCHQIMG",value[5]);
                jsonObject.put("I_YCHKDATE",value[6]);





                break;



            case Common.PayAll:
                jsonObject.put("I_YBPR", value[0]);
                jsonObject.put("I_YCHKNO",value[1]);
                jsonObject.put("I_YOAMT",value[2]);

                jsonObject.put("I_YPAYTYP", value[3]);
                jsonObject.put("I_YBANK",value[4]);
                jsonObject.put("I_YCHQIMG",value[5]);
                jsonObject.put("I_YCHKDATE",value[6]);

                break;

            case Common.Print_Email:
                jsonObject.put("I_NUM", value[0]);
                jsonObject.put("I_TYP", value[1]);

                break;
            case Common.ContractDetailsForConsumption:
            case Common.Reasons:
            case Common.BlockList:
            case Common.BankList:
                jsonObject.put("I_FLAG", value[0]);
                break;
            case Common.Enqiry:
                jsonObject.put("I_YUSER", value[0]);
                jsonObject.put("I_YSTARTDATE", value[1]);
                jsonObject.put("I_ENDDATE", value[2]);
                break;
            case Common.AmountForInvoice:
                jsonObject.put("I_INVNUM", value[0]);

                break;
            case Common.CheaqueDateValidity:
                jsonObject.put("I_YCHQDAT", value[0]);
                break;


            case Common.UploadSignature:
                jsonObject.put("I_CONTR", value[0]);
                jsonObject.put("I_CUST", value[1]);
                jsonObject.put("I_CUSNAM", value[2]);
                jsonObject.put("I_SIGN", value[3]);



                break;





        }
    } catch (Exception e) {
        System.out.println("Exception " + e);
    }
    return jsonObject;
    }

/********************************************************************************************************/
    public String runRequest(String  METHOD_NAME,String publicName,String []values) {

        String NAMESPACE = "http://www.adonix.com/WSS";
//        String METHOD_NAME = "run";
        String SOAP_ACTION = "CAdxWebServiceXmlCC";
//       // String URL = "http://" + "47.91.105.187" + ":" + "8124" + "/soap-generic/syracuse/collaboration/syracuse/CAdxWebServiceXmlCC";
        String URL = "http://"+ Configuration.getIP() +  ":" + Configuration.Port + "/soap-generic/syracuse/collaboration/syracuse/CAdxWebServiceXmlCC";
        SoapObject request = new SoapObject(NAMESPACE, METHOD_NAME);
      //  request.addProperty("publicName", "YMTRLOGIN");
        request.addProperty("publicName", publicName);
        request.addProperty("inputXml", getRequestJson(publicName,values).toString());
        SoapObject callcontext = new SoapObject("", "callContext");
        // Set all input params
        callcontext.addProperty("codeLang", "ENG");
        callcontext.addProperty("poolAlias", Configuration.ALIAS );
        callcontext.addProperty("poolId", "");
        callcontext.addProperty("requestConfig", "");
        request.addSoapObject(callcontext);
        SoapSerializationEnvelope envelope = new SoapSerializationEnvelope(SoapEnvelope.VER11);
        envelope.setOutputSoapObject(request);
        HttpTransportSE androidHttpTransport = new HttpTransportSE(URL,60000);
        androidHttpTransport.debug = true;
        try {
            List<HeaderProperty> headerList = new ArrayList<HeaderProperty>();
            headerList.add(new HeaderProperty("Authorization", "Basic " + org.kobjects.base64.Base64.encode((Configuration.getUSERNAME()+ ":" +Configuration.getPASSWORD()).getBytes())));
            androidHttpTransport.call(SOAP_ACTION, envelope,headerList);
            SoapObject response = (SoapObject) envelope.getResponse();
            String resultXML = (String) response.getProperty("resultXml");
            //String errorXml=(String) response.getProperty("message");
            if (resultXML != null && resultXML.length() > 0) {
                JSONObject jsonObj = null;
                try {
                    jsonObj = XML.toJSONObject(resultXML);
                } catch (JSONException e) {
                    Log.e("JSON exception", e.getMessage());
                    e.printStackTrace();
                }
                return jsonObj.toString();
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

    public String queryRequest(String  METHOD_NAME,String publicName) {
        String NAMESPACE = "http://www.adonix.com/WSS";
//       String METHOD_NAME = "run";
        String SOAP_ACTION = "CAdxWebServiceXmlCC";
      // String URL = "http://" + "47.91.105.187" + ":" + "8124" + "/soap-generic/syracuse/collaboration/syracuse/CAdxWebServiceXmlCC";
        String URL = "http://" + Configuration.getIP() + ":" + Configuration.Port + "/soap-generic/syracuse/collaboration/syracuse/CAdxWebServiceXmlCC";
        SoapObject request = new SoapObject(NAMESPACE, METHOD_NAME);
        //  request.addProperty("publicName", "YMTRLOGIN");
        request.addProperty("publicName", publicName);
        request.addProperty("listSize", 9999);
        SoapObject callcontext = new SoapObject("", "callContext");
        // Set all input params
        callcontext.addProperty("codeLang", "ENG");
        callcontext.addProperty("poolAlias", Configuration.ALIAS );
        callcontext.addProperty("poolId", "");
        callcontext.addProperty("requestConfig", "");
        request.addSoapObject(callcontext);
        SoapSerializationEnvelope envelope = new SoapSerializationEnvelope(SoapEnvelope.VER11);
        envelope.setOutputSoapObject(request);
        HttpTransportSE androidHttpTransport = new HttpTransportSE(URL);
        androidHttpTransport.debug = true;
        try {
            List<HeaderProperty> headerList = new ArrayList<HeaderProperty>();
            headerList.add(new HeaderProperty("Authorization", "Basic " + org.kobjects.base64.Base64.encode((Configuration.getUSERNAME()+ ":" +Configuration.getPASSWORD()).getBytes())));
            androidHttpTransport.call(SOAP_ACTION, envelope,headerList);
            SoapObject response = (SoapObject) envelope.getResponse();
            String resultXML = (String) response.getProperty("resultXml");
            if (resultXML != null && resultXML.length() > 0) {
                JSONObject jsonObj = null;
                try {
                    jsonObj = XML.toJSONObject(resultXML);
                } catch (JSONException e) {
                    Log.e("JSON exception", e.getMessage());
                    e.printStackTrace();
                }
                return jsonObj.toString();
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
    public String saveSettingsRequest(String methodName,String publicName,String ip,String port,String alias,String Id,String password) {

        String NAMESPACE = "http://www.adonix.com/WSS";
        // String METHOD_NAME = "run";
        String SOAP_ACTION = "CAdxWebServiceXmlCC";
        // String URL = "http://" + "47.91.105.187" + ":" + "8124" + "/soap-generic/syracuse/collaboration/syracuse/CAdxWebServiceXmlCC";
        String URL = "http://" + ip + ":" + port + "/soap-generic/syracuse/collaboration/syracuse/"+SOAP_ACTION+"";
        SoapObject request = new SoapObject(NAMESPACE, methodName);
        // request.addProperty("publicName", "YMTRLOGIN");
        request.addProperty("publicName", publicName);

        JSONObject jsonObject = new JSONObject();
        try {
            jsonObject.put("I_UNAME", Id);
            jsonObject.put("I_PWD", password);
        } catch (Exception e) {
            System.out.println("Exception " + e);
        }
        request.addProperty("inputXml", jsonObject.toString());

        SoapObject callcontext = new SoapObject("", "callContext");
        // Set all input params
        callcontext.addProperty("codeLang", "ENG");
        ///callcontext.addProperty("poolAlias", "BROSGST");
        callcontext.addProperty("poolAlias", alias);
        callcontext.addProperty("poolId", "");
        callcontext.addProperty("requestConfig", "");
        request.addSoapObject(callcontext);
        SoapSerializationEnvelope envelope = new SoapSerializationEnvelope(SoapEnvelope.VER11);
        envelope.setOutputSoapObject(request);
        HttpTransportSE androidHttpTransport = new HttpTransportSE(URL);
        androidHttpTransport.debug = true;
        try {
            List<HeaderProperty> headerList = new ArrayList<HeaderProperty>();
            headerList.add(new HeaderProperty("Authorization", "Basic " + org.kobjects.base64.Base64.encode((Id+ ":" +password).getBytes())));
            androidHttpTransport.call(SOAP_ACTION, envelope,headerList);
            SoapObject response = (SoapObject) envelope.getResponse();
            String resultXML = response.getProperty(0).toString();;
           if(resultXML.contains("Success"))
           {
               return "Success";
           }else
            {
                return "Failed to update";
            }
        } catch (SocketTimeoutException e) {
            e.printStackTrace();
            return "";
        } catch (Exception e) {
            e.printStackTrace();
            return "";
        }

    }
public String getCurrentDate()
{
    SimpleDateFormat timeStampFormat = new SimpleDateFormat("yyyyMMdd");
    Date myDate = new Date();
    String date = timeStampFormat.format(myDate);
    return date;
}
    }
