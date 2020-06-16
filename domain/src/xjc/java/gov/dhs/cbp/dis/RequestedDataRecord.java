
package gov.dhs.cbp.dis;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlSchemaType;
import javax.xml.bind.annotation.XmlType;
import javax.xml.datatype.XMLGregorianCalendar;


/**
 * <p>Java class for RequestedDataRecord complex type.
 * 
 * <p>The following schema fragment specifies the expected content contained within this class.
 * 
 * <pre>
 * &lt;complexType name="RequestedDataRecord"&gt;
 *   &lt;complexContent&gt;
 *     &lt;restriction base="{http://www.w3.org/2001/XMLSchema}anyType"&gt;
 *       &lt;sequence&gt;
 *         &lt;element name="DocumentID" type="{http://www.w3.org/2001/XMLSchema}string" minOccurs="0"/&gt;
 *         &lt;element name="DocumentLabel" type="{http://www.w3.org/2001/XMLSchema}string" minOccurs="0"/&gt;
 *         &lt;element name="DocumentName" type="{http://www.w3.org/2001/XMLSchema}string" minOccurs="0"/&gt;
 *         &lt;element name="DocumentType" type="{http://www.w3.org/2001/XMLSchema}string" minOccurs="0"/&gt;
 *         &lt;element name="DocumentTrackingID" type="{http://www.w3.org/2001/XMLSchema}string" minOccurs="0"/&gt;
 *         &lt;element name="DocumentSentDate" type="{http://www.w3.org/2001/XMLSchema}dateTime" minOccurs="0"/&gt;
 *         &lt;element name="DocumentValidationStatus" type="{http://www.w3.org/2001/XMLSchema}string" minOccurs="0"/&gt;
 *         &lt;element name="DocumentReviewStatus" type="{http://www.w3.org/2001/XMLSchema}string" minOccurs="0"/&gt;
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
@XmlType(name = "RequestedDataRecord", propOrder = {
    "documentID",
    "documentLabel",
    "documentName",
    "documentType",
    "documentTrackingID",
    "documentSentDate",
    "documentValidationStatus",
    "documentReviewStatus",
    "documentRejectReason"
})
public class RequestedDataRecord {

    @XmlElement(name = "DocumentID")
    protected String documentID;
    @XmlElement(name = "DocumentLabel")
    protected String documentLabel;
    @XmlElement(name = "DocumentName")
    protected String documentName;
    @XmlElement(name = "DocumentType")
    protected String documentType;
    @XmlElement(name = "DocumentTrackingID")
    protected String documentTrackingID;
    @XmlElement(name = "DocumentSentDate")
    @XmlSchemaType(name = "dateTime")
    protected XMLGregorianCalendar documentSentDate;
    @XmlElement(name = "DocumentValidationStatus")
    protected String documentValidationStatus;
    @XmlElement(name = "DocumentReviewStatus")
    protected String documentReviewStatus;
    @XmlElement(name = "DocumentRejectReason")
    protected String documentRejectReason;

    /**
     * Gets the value of the documentID property.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getDocumentID() {
        return documentID;
    }

    /**
     * Sets the value of the documentID property.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setDocumentID(String value) {
        this.documentID = value;
    }

    /**
     * Gets the value of the documentLabel property.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getDocumentLabel() {
        return documentLabel;
    }

    /**
     * Sets the value of the documentLabel property.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setDocumentLabel(String value) {
        this.documentLabel = value;
    }

    /**
     * Gets the value of the documentName property.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getDocumentName() {
        return documentName;
    }

    /**
     * Sets the value of the documentName property.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setDocumentName(String value) {
        this.documentName = value;
    }

    /**
     * Gets the value of the documentType property.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getDocumentType() {
        return documentType;
    }

    /**
     * Sets the value of the documentType property.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setDocumentType(String value) {
        this.documentType = value;
    }

    /**
     * Gets the value of the documentTrackingID property.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getDocumentTrackingID() {
        return documentTrackingID;
    }

    /**
     * Sets the value of the documentTrackingID property.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setDocumentTrackingID(String value) {
        this.documentTrackingID = value;
    }

    /**
     * Gets the value of the documentSentDate property.
     * 
     * @return
     *     possible object is
     *     {@link XMLGregorianCalendar }
     *     
     */
    public XMLGregorianCalendar getDocumentSentDate() {
        return documentSentDate;
    }

    /**
     * Sets the value of the documentSentDate property.
     * 
     * @param value
     *     allowed object is
     *     {@link XMLGregorianCalendar }
     *     
     */
    public void setDocumentSentDate(XMLGregorianCalendar value) {
        this.documentSentDate = value;
    }

    /**
     * Gets the value of the documentValidationStatus property.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getDocumentValidationStatus() {
        return documentValidationStatus;
    }

    /**
     * Sets the value of the documentValidationStatus property.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setDocumentValidationStatus(String value) {
        this.documentValidationStatus = value;
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
