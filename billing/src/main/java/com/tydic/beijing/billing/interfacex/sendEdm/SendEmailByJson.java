package com.tydic.beijing.billing.interfacex.sendEdm;
import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlType;


/**
 * <p>Java class for sendEmailByJson complex type.
 * 
 * <p>The following schema fragment specifies the expected content contained within this class.
 * 
 * <pre>
 * &lt;complexType name="sendEmailByJson">
 *   &lt;complexContent>
 *     &lt;restriction base="{http://www.w3.org/2001/XMLSchema}anyType">
 *       &lt;sequence>
 *         &lt;element name="jsonInfo" type="{http://www.w3.org/2001/XMLSchema}string" minOccurs="0"/>
 *       &lt;/sequence>
 *     &lt;/restriction>
 *   &lt;/complexContent>
 * &lt;/complexType>
 * </pre>
 * 
 * 
 */
@XmlAccessorType(XmlAccessType.FIELD)
@XmlType(name = "sendEmailByJson",namespace = "http://customMail.service.produceemail.jd.com.sendEmailByJson/",  propOrder = {
    "jsonInfo"
})
public class SendEmailByJson {

    protected String jsonInfo;

    /**
     * Gets the value of the jsonInfo property.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getJsonInfo() {
        return jsonInfo;
    }

    /**
     * Sets the value of the jsonInfo property.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setJsonInfo(String value) {
        this.jsonInfo = value;
    }

}
