package com.tydic.sendEdm;

import java.net.URL;
import java.util.ArrayList;
import java.util.Date;
import java.util.GregorianCalendar;
import java.util.Set;

import javax.xml.datatype.DatatypeConfigurationException;
import javax.xml.datatype.DatatypeFactory;
import javax.xml.datatype.XMLGregorianCalendar;
import javax.xml.namespace.QName;
import javax.xml.soap.SOAPElement;
import javax.xml.soap.SOAPException;
import javax.xml.soap.SOAPHeader;
import javax.xml.soap.SOAPMessage;
import javax.xml.ws.BindingProvider;
import javax.xml.ws.handler.Handler;
import javax.xml.ws.handler.MessageContext;
import javax.xml.ws.handler.soap.SOAPHandler;
import javax.xml.ws.handler.soap.SOAPMessageContext;

import org.apache.log4j.Logger;

import com.tydic.sendEdm.impl.CustomMailImplService;

/**
 * @author dongxuanyi
 * 
 */

public class Service {
    private final static Logger logger = Logger.getLogger(Service.class);
    private static String Url;
    private static String TOKEN;

    protected static String getTOKEN() {
        return TOKEN;
    }

    public static void setTOKEN(String TOKEN) {
        Service.TOKEN = TOKEN;
    }

    public static String getUrl() {
        return Url;
    }

    public static void setUrl(String url) {
        Url = url;
    }

    public static XMLGregorianCalendar convertToXmlGregorianCalendar(Date date) {
        XMLGregorianCalendar xgc = null;
        try {
            GregorianCalendar gc = new GregorianCalendar();
            gc.setTime(date);
            xgc = DatatypeFactory.newInstance().newXMLGregorianCalendar(gc);
        } catch (DatatypeConfigurationException e) {
            logger.error(e.getMessage(), e);
        }
        return xgc;
    }

    public int sendEmailByJson(String jsonString) {
        int result = 0;

        if (!checkToken()) {
            result = Result.TOKEN错误.getValue();
            return result;
        }

        try {
            CustomMailImplService sendEmailByJson;
            if (Url == null || Url.isEmpty()) {
                sendEmailByJson = new CustomMailImplService();
            } else {
                sendEmailByJson = new CustomMailImplService(new URL(Url));
            }
            ArrayList<Handler> handlers = new ArrayList<Handler>();
            handlers.add(new LoginHandler());
            CustomMailInterface sender = sendEmailByJson.getCustomMailImplPort();
            ((BindingProvider) sender).getBinding().setHandlerChain(handlers);
            result = sender.sendEmailByJson(jsonString);
            Result resultDetail = Result.getRusult(result);
            if (!resultDetail.equals(Result.成功)) {
                logger.error(String.format("sendEmailByJson err,err code:%d,info:%s", result, resultDetail.toString()));
            }
        } catch (javax.xml.ws.soap.SOAPFaultException ex) {
            if ("Token wrong! ".equals(ex.getMessage())) {
                logger.error("sendEmailByJson check token err", ex);
                result = Result.TOKEN错误.getValue();
            } else {
                throw ex;
            }
        } catch (javax.xml.ws.WebServiceException ex) {
            if (ex.getMessage().length() > 25 && "Failed to access the WSDL".equals(ex.getMessage().substring(0, 25))) {
                logger.error("sendEmailByJson connect err", ex);
                result = Result.连接错误.getValue();
            } else {
                throw ex;
            }
        } catch (Exception ex) {
            logger.error("sendEmailByJson err", ex);
            result = Result.未知失败.getValue();
        }
        return result;

    }

    public int sendEmailByXML(String xmlInfo) {
        int result = 0;

        if (!checkToken()) {
            result = Result.TOKEN错误.getValue();
            return result;
        }

        try {
            CustomMailImplService sendEmailByXML;
            if (Url == null || Url.isEmpty()) {
                sendEmailByXML = new CustomMailImplService();
            } else {
                sendEmailByXML = new CustomMailImplService(new URL(Url));
            }
            ArrayList<Handler> handlers = new ArrayList<Handler>();
            handlers.add(new LoginHandler());
            CustomMailInterface sender = sendEmailByXML.getCustomMailImplPort();
            ((BindingProvider) sender).getBinding().setHandlerChain(handlers);
            result = sender.sendEmailByXML(xmlInfo);
            Result resultDetail = Result.getRusult(result);
            if (!resultDetail.equals(Result.成功)) {
                logger.error(String.format("sendEmailByXML err,err code:%d,info:%s", result, resultDetail.toString()));
            }
        } catch (javax.xml.ws.soap.SOAPFaultException ex) {
            if ("Token wrong! ".equals(ex.getMessage())) {
                logger.error("sendEmailByXML check token err", ex);
                result = Result.TOKEN错误.getValue();
            } else {
                throw ex;
            }
        } catch (javax.xml.ws.WebServiceException ex) {
            if (ex.getMessage().length() > 25 && "Failed to access the WSDL".equals(ex.getMessage().substring(0, 25))) {
                logger.error("sendEmailByXML connect err", ex);
                result = Result.连接错误.getValue();
            } else {
                throw ex;
            }
        } catch (Exception ex) {
            logger.error("sendEmailByXML err", ex);
            result = Result.未知失败.getValue();
        }
        return result;
    }

    static class LoginHandler implements SOAPHandler<SOAPMessageContext> {

        @Override
        public boolean handleMessage(SOAPMessageContext smc) {

            Boolean outboundProperty = (Boolean) smc.get(MessageContext.MESSAGE_OUTBOUND_PROPERTY);

            if (outboundProperty.booleanValue()) {
                SOAPMessage message = smc.getMessage();
                try {
                    SOAPHeader header = message.getSOAPHeader();
                    if (header == null) {
                        message.getSOAPPart().getEnvelope().addHeader();
                        header = message.getSOAPHeader();
                    }
                    // QName qName = new QName("AuthenticationHeader");
                    SOAPElement element = header.addChildElement("AuthenticationHeader", "", "http://360buy.com/");
                    element.addChildElement("Token", "", "http://360buy.com/").addTextNode(getTOKEN());
                } catch (SOAPException e) {
                    logger.error(e.getMessage(), e);
                }
            }
            return true;
        }

//        private String getToken() {
//            String result = "";
//            if (!"".equals(TOKEN)) {
//                MessageDigest md;
//                try {
//                    md = MessageDigest.getInstance("MD5");
//                    md.update((TOKEN).getBytes());
//                    byte b[] = md.digest();
//                    result = new String(new sun.misc.BASE64Encoder().encode(b));
//                } catch (NoSuchAlgorithmException e) {
//                    logger.error(e.getMessage(), e);
//                }
//            }else {
//                logger.error("------send test sms------ ");
//            }
//            return result;
//        }

        @Override
        public boolean handleFault(SOAPMessageContext context) {
            // TODO Auto-generated method stub
            return false;
        }

        @Override
        public void close(MessageContext context) {
            // TODO Auto-generated method stub

        }

        @Override
        public Set<QName> getHeaders() {
            // TODO Auto-generated method stub
            return null;
        }
    }

    private boolean checkToken() {
        boolean result = false;

        if (TOKEN != null && TOKEN.length() > 0 && TOKEN.trim().length() > 0) {
            result = true;
        }

        return result;
    }

}
