package com.tydic.sendEdm;
import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlType;


/**
 * <p>Java class for SendMail complex type.
 * 
 * <p>The following schema fragment specifies the expected content contained within this class.
 * 
 * <pre>
 * &lt;complexType name="SendMail">
 *   &lt;complexContent>
 *     &lt;restriction base="{http://www.w3.org/2001/XMLSchema}anyType">
 *       &lt;sequence>
 *         &lt;element name="xmlInfo" type="{http://www.w3.org/2001/XMLSchema}string" minOccurs="0"/>
 *       &lt;/sequence>
 *     &lt;/restriction>
 *   &lt;/complexContent>
 * &lt;/complexType>
 * </pre>
 * 
 * 
 */
@XmlAccessorType(XmlAccessType.FIELD)
@XmlType(name = "SendMail",namespace = "http://customMail.service.produceemail.jd.com.SendMail/", propOrder = {
    "xmlInfo"
})
public class SendMail {

    protected String xmlInfo;

    /**
     * Gets the value of the xmlInfo property.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getXmlInfo() {
        return xmlInfo;
    }

    /**
     * Sets the value of the xmlInfo property.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setXmlInfo(String value) {
        this.xmlInfo = value;
    }

}
