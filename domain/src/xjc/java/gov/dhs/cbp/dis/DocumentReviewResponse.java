
package gov.dhs.cbp.dis;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlType;


/**
 * <p>Java class for DocumentReviewResponse complex type.
 * 
 * <p>The following schema fragment specifies the expected content contained within this class.
 * 
 * <pre>
 * &lt;complexType name="DocumentReviewResponse"&gt;
 *   &lt;complexContent&gt;
 *     &lt;restriction base="{http://www.w3.org/2001/XMLSchema}anyType"&gt;
 *       &lt;sequence&gt;
 *         &lt;element name="DocumentHeader" type="{http://cbp.dhs.gov/DIS}DocumentHeader"/&gt;
 *         &lt;element name="SubmittedToPortCode" type="{http://cbp.dhs.gov/DIS}SubmittedToPortCode" minOccurs="0"/&gt;
 *         &lt;element name="TradeTransaction" type="{http://cbp.dhs.gov/DIS}TradeTransaction"/&gt;
 *         &lt;element name="DocumentReviewResult" type="{http://cbp.dhs.gov/DIS}DocumentReviewResult"/&gt;
 *       &lt;/sequence&gt;
 *     &lt;/restriction&gt;
 *   &lt;/complexContent&gt;
 * &lt;/complexType&gt;
 * </pre>
 * 
 * 
 */
@XmlAccessorType(XmlAccessType.FIELD)
@XmlType(name = "DocumentReviewResponse", propOrder = {
    "documentHeader",
    "submittedToPortCode",
    "tradeTransaction",
    "documentReviewResult"
})
public class DocumentReviewResponse {

    @XmlElement(name = "DocumentHeader", required = true)
    protected DocumentHeader documentHeader;
    @XmlElement(name = "SubmittedToPortCode")
    protected String submittedToPortCode;
    @XmlElement(name = "TradeTransaction", required = true)
    protected TradeTransaction tradeTransaction;
    @XmlElement(name = "DocumentReviewResult", required = true)
    protected DocumentReviewResult documentReviewResult;

    /**
     * Gets the value of the documentHeader property.
     * 
     * @return
     *     possible object is
     *     {@link DocumentHeader }
     *     
     */
    public DocumentHeader getDocumentHeader() {
        return documentHeader;
    }

    /**
     * Sets the value of the documentHeader property.
     * 
     * @param value
     *     allowed object is
     *     {@link DocumentHeader }
     *     
     */
    public void setDocumentHeader(DocumentHeader value) {
        this.documentHeader = value;
    }

    /**
     * Gets the value of the submittedToPortCode property.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getSubmittedToPortCode() {
        return submittedToPortCode;
    }

    /**
     * Sets the value of the submittedToPortCode property.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setSubmittedToPortCode(String value) {
        this.submittedToPortCode = value;
    }

    /**
     * Gets the value of the tradeTransaction property.
     * 
     * @return
     *     possible object is
     *     {@link TradeTransaction }
     *     
     */
    public TradeTransaction getTradeTransaction() {
        return tradeTransaction;
    }

    /**
     * Sets the value of the tradeTransaction property.
     * 
     * @param value
     *     allowed object is
     *     {@link TradeTransaction }
     *     
     */
    public void setTradeTransaction(TradeTransaction value) {
        this.tradeTransaction = value;
    }

    /**
     * Gets the value of the documentReviewResult property.
     * 
     * @return
     *     possible object is
     *     {@link DocumentReviewResult }
     *     
     */
    public DocumentReviewResult getDocumentReviewResult() {
        return documentReviewResult;
    }

    /**
     * Sets the value of the documentReviewResult property.
     * 
     * @param value
     *     allowed object is
     *     {@link DocumentReviewResult }
     *     
     */
    public void setDocumentReviewResult(DocumentReviewResult value) {
        this.documentReviewResult = value;
    }

}
