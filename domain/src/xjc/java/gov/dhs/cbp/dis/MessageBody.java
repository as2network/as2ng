
package gov.dhs.cbp.dis;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlType;


/**
 * The Message Body should be included on every message from a Trade Party to CBP. It contains a single 'Payload'. The choices of Payloads are below 
 * 
 * <p>Java class for MessageBody complex type.
 * 
 * <p>The following schema fragment specifies the expected content contained within this class.
 * 
 * <pre>
 * &lt;complexType name="MessageBody"&gt;
 *   &lt;complexContent&gt;
 *     &lt;restriction base="{http://www.w3.org/2001/XMLSchema}anyType"&gt;
 *       &lt;choice&gt;
 *         &lt;element name="DocumentSubmissionPackage" type="{http://cbp.dhs.gov/DIS}DocumentSubmissionPackage"/&gt;
 *         &lt;element name="DocumentWithdrawal" type="{http://cbp.dhs.gov/DIS}DocumentWithdrawal"/&gt;
 *         &lt;element name="DataRequestPackage" type="{http://cbp.dhs.gov/DIS}DataRequestPackage"/&gt;
 *         &lt;element name="RequestedDataPackage" type="{http://cbp.dhs.gov/DIS}RequestedDataPackage"/&gt;
 *         &lt;element name="MessageValidationResponse" type="{http://cbp.dhs.gov/DIS}MessageValidationResponse"/&gt;
 *         &lt;element name="DocumentReviewResponse" type="{http://cbp.dhs.gov/DIS}DocumentReviewResponse"/&gt;
 *       &lt;/choice&gt;
 *     &lt;/restriction&gt;
 *   &lt;/complexContent&gt;
 * &lt;/complexType&gt;
 * </pre>
 * 
 * 
 */
@XmlAccessorType(XmlAccessType.FIELD)
@XmlType(name = "MessageBody", propOrder = {
    "documentSubmissionPackage",
    "documentWithdrawal",
    "dataRequestPackage",
    "requestedDataPackage",
    "messageValidationResponse",
    "documentReviewResponse"
})
public class MessageBody {

    @XmlElement(name = "DocumentSubmissionPackage")
    protected DocumentSubmissionPackage documentSubmissionPackage;
    @XmlElement(name = "DocumentWithdrawal")
    protected DocumentWithdrawal documentWithdrawal;
    @XmlElement(name = "DataRequestPackage")
    protected DataRequestPackage dataRequestPackage;
    @XmlElement(name = "RequestedDataPackage")
    protected RequestedDataPackage requestedDataPackage;
    @XmlElement(name = "MessageValidationResponse")
    protected MessageValidationResponse messageValidationResponse;
    @XmlElement(name = "DocumentReviewResponse")
    protected DocumentReviewResponse documentReviewResponse;

    /**
     * Gets the value of the documentSubmissionPackage property.
     * 
     * @return
     *     possible object is
     *     {@link DocumentSubmissionPackage }
     *     
     */
    public DocumentSubmissionPackage getDocumentSubmissionPackage() {
        return documentSubmissionPackage;
    }

    /**
     * Sets the value of the documentSubmissionPackage property.
     * 
     * @param value
     *     allowed object is
     *     {@link DocumentSubmissionPackage }
     *     
     */
    public void setDocumentSubmissionPackage(DocumentSubmissionPackage value) {
        this.documentSubmissionPackage = value;
    }

    /**
     * Gets the value of the documentWithdrawal property.
     * 
     * @return
     *     possible object is
     *     {@link DocumentWithdrawal }
     *     
     */
    public DocumentWithdrawal getDocumentWithdrawal() {
        return documentWithdrawal;
    }

    /**
     * Sets the value of the documentWithdrawal property.
     * 
     * @param value
     *     allowed object is
     *     {@link DocumentWithdrawal }
     *     
     */
    public void setDocumentWithdrawal(DocumentWithdrawal value) {
        this.documentWithdrawal = value;
    }

    /**
     * Gets the value of the dataRequestPackage property.
     * 
     * @return
     *     possible object is
     *     {@link DataRequestPackage }
     *     
     */
    public DataRequestPackage getDataRequestPackage() {
        return dataRequestPackage;
    }

    /**
     * Sets the value of the dataRequestPackage property.
     * 
     * @param value
     *     allowed object is
     *     {@link DataRequestPackage }
     *     
     */
    public void setDataRequestPackage(DataRequestPackage value) {
        this.dataRequestPackage = value;
    }

    /**
     * Gets the value of the requestedDataPackage property.
     * 
     * @return
     *     possible object is
     *     {@link RequestedDataPackage }
     *     
     */
    public RequestedDataPackage getRequestedDataPackage() {
        return requestedDataPackage;
    }

    /**
     * Sets the value of the requestedDataPackage property.
     * 
     * @param value
     *     allowed object is
     *     {@link RequestedDataPackage }
     *     
     */
    public void setRequestedDataPackage(RequestedDataPackage value) {
        this.requestedDataPackage = value;
    }

    /**
     * Gets the value of the messageValidationResponse property.
     * 
     * @return
     *     possible object is
     *     {@link MessageValidationResponse }
     *     
     */
    public MessageValidationResponse getMessageValidationResponse() {
        return messageValidationResponse;
    }

    /**
     * Sets the value of the messageValidationResponse property.
     * 
     * @param value
     *     allowed object is
     *     {@link MessageValidationResponse }
     *     
     */
    public void setMessageValidationResponse(MessageValidationResponse value) {
        this.messageValidationResponse = value;
    }

    /**
     * Gets the value of the documentReviewResponse property.
     * 
     * @return
     *     possible object is
     *     {@link DocumentReviewResponse }
     *     
     */
    public DocumentReviewResponse getDocumentReviewResponse() {
        return documentReviewResponse;
    }

    /**
     * Sets the value of the documentReviewResponse property.
     * 
     * @param value
     *     allowed object is
     *     {@link DocumentReviewResponse }
     *     
     */
    public void setDocumentReviewResponse(DocumentReviewResponse value) {
        this.documentReviewResponse = value;
    }

}
