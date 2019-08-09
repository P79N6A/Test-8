
/*
 * 
 */

package com.tydic.beijing.billing.interfacex.sendEdm.impl;


import java.net.MalformedURLException;
import java.net.URL;
import javax.xml.namespace.QName;
import javax.xml.ws.WebEndpoint;
import javax.xml.ws.WebServiceClient;
import javax.xml.ws.WebServiceFeature;
import javax.xml.ws.Service;

/**
 * This class was generated by Apache CXF 2.2.4
 * Mon Jan 07 09:36:35 CST 2013
 * Generated source version: 2.2.4
 * 
 */


@WebServiceClient(name = "CustomMailImplService", 
                  wsdlLocation = "http://custommail.producemail.360buy.com/services/services/customMailInterface?wsdl",
                  targetNamespace = "http://impl.customMail.service.produceemail.jd.com/") 
public class CustomMailImplService extends Service {

    public final static URL WSDL_LOCATION;
    public final static QName SERVICE = new QName("http://impl.customMail.service.produceemail.jd.com/", "CustomMailImplService");
    public final static QName CustomMailImplPort = new QName("http://impl.customMail.service.produceemail.jd.com/", "CustomMailImplPort");
    static {
        URL url = null;
        try {
            url = new URL("http://custommail.producemail.360buy.com/services/services/customMailInterface?wsdl");
        } catch (MalformedURLException e) {
            System.err.println("Can not initialize the default wsdl from http://custommail.producemail.360buy.com/services/services/customMailInterface?wsdl");
            // e.printStackTrace();
        }
        WSDL_LOCATION = url;
    }

    public CustomMailImplService(URL wsdlLocation) {
        super(wsdlLocation, SERVICE);
    }

    public CustomMailImplService(URL wsdlLocation, QName serviceName) {
        super(wsdlLocation, serviceName);
    }

    public CustomMailImplService() {
        super(WSDL_LOCATION, SERVICE);
    }

    /**
     * 
     * @return
     *     returns CustomMailInterface
     */
    @WebEndpoint(name = "CustomMailImplPort")
    public com.tydic.beijing.billing.interfacex.sendEdm.CustomMailInterface getCustomMailImplPort() {
        return super.getPort(CustomMailImplPort, com.tydic.beijing.billing.interfacex.sendEdm.CustomMailInterface.class);
    }

    /**
     * 
     * @param features
     *     A list of {@link javax.xml.ws.WebServiceFeature} to configure on the proxy.  Supported features not in the <code>features</code> parameter will have their default values.
     * @return
     *     returns CustomMailInterface
     */
    @WebEndpoint(name = "CustomMailImplPort")
    public com.tydic.beijing.billing.interfacex.sendEdm.CustomMailInterface getCustomMailImplPort(WebServiceFeature... features) {
        return super.getPort(CustomMailImplPort, com.tydic.beijing.billing.interfacex.sendEdm.CustomMailInterface.class, features);
    }

}
