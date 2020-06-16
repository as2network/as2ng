
package gov.dhs.cbp.dis;

import java.util.ArrayList;
import java.util.List;
import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlType;


/**
 * <p>Java class for MessageValidationResponse complex type.
 * 
 * <p>The following schema fragment specifies the expected content contained within this class.
 * 
 * <pre>
 * &lt;complexType name="MessageValidationResponse"&gt;
 *   &lt;complexContent&gt;
 *     &lt;restriction base="{http://www.w3.org/2001/XMLSchema}anyType"&gt;
 *       &lt;sequence&gt;
 *         &lt;element name="MessageLevelResult" type="{http://cbp.dhs.gov/DIS}MessageLevelResult"/&gt;
 *         &lt;element name="DocumentLevelResult" type="{http://cbp.dhs.gov/DIS}DocumentLevelResult" maxOccurs="unbounded"/&gt;
 *       &lt;/sequence&gt;
 *     &lt;/restriction&gt;
 *   &lt;/complexContent&gt;
 * &lt;/complexType&gt;
 * </pre>
 * 
 * 
 */
@XmlAccessorType(XmlAccessType.FIELD)
@XmlType(name = "MessageValidationResponse", propOrder = {
    "messageLevelResult",
    "documentLevelResult"
})
public class MessageValidationResponse {

    @XmlElement(name = "MessageLevelResult", required = true)
    protected MessageLevelResult messageLevelResult;
    @XmlElement(name = "DocumentLevelResult", required = true)
    protected List<DocumentLevelResult> documentLevelResult;

    /**
     * Gets the value of the messageLevelResult property.
     * 
     * @return
     *     possible object is
     *     {@link MessageLevelResult }
     *     
     */
    public MessageLevelResult getMessageLevelResult() {
        return messageLevelResult;
    }

    /**
     * Sets the value of the messageLevelResult property.
     * 
     * @param value
     *     allowed object is
     *     {@link MessageLevelResult }
     *     
     */
    public void setMessageLevelResult(MessageLevelResult value) {
        this.messageLevelResult = value;
    }

    /**
     * Gets the value of the documentLevelResult property.
     * 
     * <p>
     * This accessor method returns a reference to the live list,
     * not a snapshot. Therefore any modification you make to the
     * returned list will be present inside the JAXB object.
     * This is why there is not a <CODE>set</CODE> method for the documentLevelResult property.
     * 
     * <p>
     * For example, to add a new item, do as follows:
     * <pre>
     *    getDocumentLevelResult().add(newItem);
     * </pre>
     * 
     * 
     * <p>
     * Objects of the following type(s) are allowed in the list
     * {@link DocumentLevelResult }
     * 
     * 
     */
    public List<DocumentLevelResult> getDocumentLevelResult() {
        if (documentLevelResult == null) {
            documentLevelResult = new ArrayList<DocumentLevelResult>();
        }
        return this.documentLevelResult;
    }

}
