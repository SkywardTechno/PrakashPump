package skyward.pp.webservice;

import org.ksoap2.SoapEnvelope;
import org.ksoap2.serialization.SoapObject;
import org.ksoap2.serialization.SoapSerializationEnvelope;
import org.ksoap2.transport.HttpTransportSE;

public class SOAPManager {
    public static final String NAMESPACE = "http://tempuri.org/";

   // public static String MAIN_REQUEST_URL = "http://192.168.1.25:5090/Webservice/service.asmx"; //local
    public static String MAIN_REQUEST_URL = "http://apex.skywardcrm.com/prakashpumpWebservice/Webservice/Service.asmx"; //live

    public static SoapSerializationEnvelope getSoapSerializationEnvelope(
            SoapObject request) {
        SoapSerializationEnvelope envelope = new SoapSerializationEnvelope(
                SoapEnvelope.VER11);
        envelope.dotNet = true;
        envelope.implicitTypes = true;
        envelope.setAddAdornments(false);
        envelope.setOutputSoapObject(request);
        return envelope;
    }

    public static HttpTransportSE getHttpTransportSE() {
        HttpTransportSE ht = new HttpTransportSE(MAIN_REQUEST_URL);
        ht.debug = true;
        ht.setXmlVersionTag("<!--?xml version=\"1.0\" encoding= \"UTF-8\" ?-->");
        return ht;
    }

}

