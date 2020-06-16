
package gov.dhs.cbp.dis;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlType;


/**
 * <p>Java class for ContactInfoType complex type.
 * 
 * <p>The following schema fragment specifies the expected content contained within this class.
 * 
 * <pre>
 * &lt;complexType name="ContactInfoType"&gt;
 *   &lt;complexContent&gt;
 *     &lt;restriction base="{http://www.w3.org/2001/XMLSchema}anyType"&gt;
 *       &lt;sequence&gt;
 *         &lt;element name="POC_Info" type="{http://www.w3.org/2001/XMLSchema}string" minOccurs="0"/&gt;
 *         &lt;element name="POC_PhoneNumber" type="{http://www.w3.org/2001/XMLSchema}string" minOccurs="0"/&gt;
 *         &lt;element name="ReturnEmailAddress" type="{http://www.w3.org/2001/XMLSchema}string" minOccurs="0"/&gt;
 *       &lt;/sequence&gt;
 *     &lt;/restriction&gt;
 *   &lt;/complexContent&gt;
 * &lt;/complexType&gt;
 * </pre>
 * 
 * 
 */
@XmlAccessorType(XmlAccessType.FIELD)
@XmlType(name = "ContactInfoType", propOrder = {
    "pocInfo",
    "pocPhoneNumber",
    "returnEmailAddress"
})
public class ContactInfoType {

    @XmlElement(name = "POC_Info")
    protected String pocInfo;
    @XmlElement(name = "POC_PhoneNumber")
    protected String pocPhoneNumber;
    @XmlElement(name = "ReturnEmailAddress")
    protected String returnEmailAddress;

    /**
     * Gets the value of the pocInfo property.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getPOCInfo() {
        return pocInfo;
    }

    /**
     * Sets the value of the pocInfo property.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setPOCInfo(String value) {
        this.pocInfo = value;
    }

    /**
     * Gets the value of the pocPhoneNumber property.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getPOCPhoneNumber() {
        return pocPhoneNumber;
    }

    /**
     * Sets the value of the pocPhoneNumber property.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setPOCPhoneNumber(String value) {
        this.pocPhoneNumber = value;
    }

    /**
     * Gets the value of the returnEmailAddress property.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getReturnEmailAddress() {
        return returnEmailAddress;
    }

    /**
     * Sets the value of the returnEmailAddress property.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setReturnEmailAddress(String value) {
        this.returnEmailAddress = value;
    }

}
