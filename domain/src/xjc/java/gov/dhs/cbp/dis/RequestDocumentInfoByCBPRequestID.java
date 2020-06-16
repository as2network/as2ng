
package gov.dhs.cbp.dis;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlType;


/**
 * Used to Request Document Information by a specified CBP Request ID
 * 
 * <p>Java class for RequestDocumentInfoByCBPRequestID complex type.
 * 
 * <p>The following schema fragment specifies the expected content contained within this class.
 * 
 * <pre>
 * &lt;complexType name="RequestDocumentInfoByCBPRequestID"&gt;
 *   &lt;complexContent&gt;
 *     &lt;restriction base="{http://www.w3.org/2001/XMLSchema}anyType"&gt;
 *       &lt;sequence&gt;
 *         &lt;element name="CBPRequestID" type="{http://www.w3.org/2001/XMLSchema}string"/&gt;
 *       &lt;/sequence&gt;
 *     &lt;/restriction&gt;
 *   &lt;/complexContent&gt;
 * &lt;/complexType&gt;
 * </pre>
 * 
 * 
 */
@XmlAccessorType(XmlAccessType.FIELD)
@XmlType(name = "RequestDocumentInfoByCBPRequestID", propOrder = {
    "cbpRequestID"
})
public class RequestDocumentInfoByCBPRequestID {

    @XmlElement(name = "CBPRequestID", required = true)
    protected String cbpRequestID;

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

}
