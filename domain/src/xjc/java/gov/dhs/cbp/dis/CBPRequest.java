
package gov.dhs.cbp.dis;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlSchemaType;
import javax.xml.bind.annotation.XmlType;
import javax.xml.datatype.XMLGregorianCalendar;


/**
 * <p>Java class for CBPRequest complex type.
 * 
 * <p>The following schema fragment specifies the expected content contained within this class.
 * 
 * <pre>
 * &lt;complexType name="CBPRequest"&gt;
 *   &lt;complexContent&gt;
 *     &lt;restriction base="{http://www.w3.org/2001/XMLSchema}anyType"&gt;
 *       &lt;sequence&gt;
 *         &lt;element name="CBPRequestID" type="{http://www.w3.org/2001/XMLSchema}string"/&gt;
 *         &lt;element name="CBPRequestType" type="{http://www.w3.org/2001/XMLSchema}string" minOccurs="0"/&gt;
 *         &lt;element name="CBPRequestDate" type="{http://www.w3.org/2001/XMLSchema}dateTime" minOccurs="0"/&gt;
 *       &lt;/sequence&gt;
 *     &lt;/restriction&gt;
 *   &lt;/complexContent&gt;
 * &lt;/complexType&gt;
 * </pre>
 * 
 * 
 */
@XmlAccessorType(XmlAccessType.FIELD)
@XmlType(name = "CBPRequest", propOrder = {
    "cbpRequestID",
    "cbpRequestType",
    "cbpRequestDate"
})
public class CBPRequest {

    @XmlElement(name = "CBPRequestID", required = true)
    protected String cbpRequestID;
    @XmlElement(name = "CBPRequestType")
    protected String cbpRequestType;
    @XmlElement(name = "CBPRequestDate")
    @XmlSchemaType(name = "dateTime")
    protected XMLGregorianCalendar cbpRequestDate;

    /**
     * Gets the value of the cbpRequestID property.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getCBPRequestID() {
        return cbpRequestID;
    }

    /**
     * Sets the value of the cbpRequestID property.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setCBPRequestID(String value) {
        this.cbpRequestID = value;
    }

    /**
     * Gets the value of the cbpRequestType property.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getCBPRequestType() {
        return cbpRequestType;
    }

    /**
     * Sets the value of the cbpRequestType property.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setCBPRequestType(String value) {
        this.cbpRequestType = value;
    }

    /**
     * Gets the value of the cbpRequestDate property.
     * 
     * @return
     *     possible object is
     *     {@link XMLGregorianCalendar }
     *     
     */
    public XMLGregorianCalendar getCBPRequestDate() {
        return cbpRequestDate;
    }

    /**
     * Sets the value of the cbpRequestDate property.
     * 
     * @param value
     *     allowed object is
     *     {@link XMLGregorianCalendar }
     *     
     */
    public void setCBPRequestDate(XMLGregorianCalendar value) {
        this.cbpRequestDate = value;
    }

}
