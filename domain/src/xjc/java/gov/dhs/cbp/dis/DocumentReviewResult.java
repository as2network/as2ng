
package gov.dhs.cbp.dis;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlType;


/**
 * <p>Java class for DocumentReviewResult complex type.
 * 
 * <p>The following schema fragment specifies the expected content contained within this class.
 * 
 * <pre>
 * &lt;complexType name="DocumentReviewResult"&gt;
 *   &lt;complexContent&gt;
 *     &lt;restriction base="{http://www.w3.org/2001/XMLSchema}anyType"&gt;
 *       &lt;sequence&gt;
 *         &lt;element name="ProcessingEvent" type="{http://www.w3.org/2001/XMLSchema}string" minOccurs="0"/&gt;
 *         &lt;element name="DocumentReviewStatus" type="{http://www.w3.org/2001/XMLSchema}string" minOccurs="0"/&gt;
 *         &lt;element name="DocumentReviewComment" type="{http://www.w3.org/2001/XMLSchema}string" minOccurs="0"/&gt;
 *         &lt;element name="DocumentRejectReason" type="{http://www.w3.org/2001/XMLSchema}string" minOccurs="0"/&gt;
 *       &lt;/sequence&gt;
 *     &lt;/restriction&gt;
 *   &lt;/complexContent&gt;
 * &lt;/complexType&gt;
 * </pre>
 * 
 * 
 */
@XmlAccessorType(XmlAccessType.FIELD)
@XmlType(name = "DocumentReviewResult", propOrder = {
    "processingEvent",
    "documentReviewStatus",
    "documentReviewComment",
    "documentRejectReason"
})
public class DocumentReviewResult {

    @XmlElement(name = "ProcessingEvent")
    protected String processingEvent;
    @XmlElement(name = "DocumentReviewStatus")
    protected String documentReviewStatus;
    @XmlElement(name = "DocumentReviewComment")
    protected String documentReviewComment;
    @XmlElement(name = "DocumentRejectReason")
    protected String documentRejectReason;

    /**
     * Gets the value of the processingEvent property.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getProcessingEvent() {
        return processingEvent;
    }

    /**
     * Sets the value of the processingEvent property.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setProcessingEvent(String value) {
        this.processingEvent = value;
    }

    /**
     * Gets the value of the documentReviewStatus property.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getDocumentReviewStatus() {
        return documentReviewStatus;
    }

    /**
     * Sets the value of the documentReviewStatus property.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setDocumentReviewStatus(String value) {
        this.documentReviewStatus = value;
    }

    /**
     * Gets the value of the documentReviewComment property.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getDocumentReviewComment() {
        return documentReviewComment;
    }

    /**
     * Sets the value of the documentReviewComment property.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setDocumentReviewComment(String value) {
        this.documentReviewComment = value;
    }

    /**
     * Gets the value of the documentRejectReason property.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getDocumentRejectReason() {
        return documentRejectReason;
    }

    /**
     * Sets the value of the documentRejectReason property.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setDocumentRejectReason(String value) {
        this.documentRejectReason = value;
    }

}
