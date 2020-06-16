
package gov.dhs.cbp.iws.itdsservices.iws;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlType;


/**
 * A message response is received synchronously upon a making web service call
 * 
 * <p>Java class for ResponseMessage complex type.
 * 
 * <p>The following schema fragment specifies the expected content contained within this class.
 * 
 * <pre>
 * &lt;complexType name="ResponseMessage"&gt;
 *   &lt;complexContent&gt;
 *     &lt;restriction base="{http://www.w3.org/2001/XMLSchema}anyType"&gt;
 *       &lt;sequence&gt;
 *         &lt;element name="ReturnCode" type="{http://www.w3.org/2001/XMLSchema}string"/&gt;
 *         &lt;element name="ReturnRemark" type="{http://www.w3.org/2001/XMLSchema}string" minOccurs="0"/&gt;
 *         &lt;element name="ErrorDetailsList" type="{http://iws.cbp.dhs.gov/ITDSServices/IWS}ErrorDetailsList" minOccurs="0"/&gt;
 *       &lt;/sequence&gt;
 *     &lt;/restriction&gt;
 *   &lt;/complexContent&gt;
 * &lt;/complexType&gt;
 * </pre>
 * 
 * 
 */
@XmlAccessorType(XmlAccessType.FIELD)
@XmlType(name = "ResponseMessage", propOrder = {
    "returnCode",
    "returnRemark",
    "errorDetailsList"
})
public class ResponseMessage {

    @XmlElement(name = "ReturnCode", required = true)
    protected String returnCode;
    @XmlElement(name = "ReturnRemark")
    protected String returnRemark;
    @XmlElement(name = "ErrorDetailsList")
    protected ErrorDetailsList errorDetailsList;

    /**
     * Gets the value of the returnCode property.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getReturnCode() {
        return returnCode;
    }

    /**
     * Sets the value of the returnCode property.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setReturnCode(String value) {
        this.returnCode = value;
    }

    /**
     * Gets the value of the returnRemark property.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getReturnRemark() {
        return returnRemark;
    }

    /**
     * Sets the value of the returnRemark property.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setReturnRemark(String value) {
        this.returnRemark = value;
    }

    /**
     * Gets the value of the errorDetailsList property.
     * 
     * @return
     *     possible object is
     *     {@link ErrorDetailsList }
     *     
     */
    public ErrorDetailsList getErrorDetailsList() {
        return errorDetailsList;
    }

    /**
     * Sets the value of the errorDetailsList property.
     * 
     * @param value
     *     allowed object is
     *     {@link ErrorDetailsList }
     *     
     */
    public void setErrorDetailsList(ErrorDetailsList value) {
        this.errorDetailsList = value;
    }

}
