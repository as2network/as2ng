
package gov.dhs.cbp.dis;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlType;


/**
 * <p>Java class for DocumentLevelResult complex type.
 * 
 * <p>The following schema fragment specifies the expected content contained within this class.
 * 
 * <pre>
 * &lt;complexType name="DocumentLevelResult"&gt;
 *   &lt;complexContent&gt;
 *     &lt;restriction base="{http://www.w3.org/2001/XMLSchema}anyType"&gt;
 *       &lt;sequence&gt;
 *         &lt;element name="ProcessedDocumentHeader" type="{http://cbp.dhs.gov/DIS}DocumentHeader" minOccurs="0"/&gt;
 *         &lt;element name="PackageIdentifier" type="{http://cbp.dhs.gov/DIS}PackageIdentifierType" minOccurs="0"/&gt;
 *         &lt;element name="TradeTransaction" type="{http://cbp.dhs.gov/DIS}TradeTransaction" minOccurs="0"/&gt;
 *         &lt;element name="DocumentProcessingResult" type="{http://cbp.dhs.gov/DIS}ProcessingResult" minOccurs="0"/&gt;
 *       &lt;/sequence&gt;
 *     &lt;/restriction&gt;
 *   &lt;/complexContent&gt;
 * &lt;/complexType&gt;
 * </pre>
 * 
 * 
 */
@XmlAccessorType(XmlAccessType.FIELD)
@XmlType(name = "DocumentLevelResult", propOrder = {
    "processedDocumentHeader",
    "packageIdentifier",
    "tradeTransaction",
    "documentProcessingResult"
})
public class DocumentLevelResult {

    @XmlElement(name = "ProcessedDocumentHeader")
    protected DocumentHeader processedDocumentHeader;
    @XmlElement(name = "PackageIdentifier")
    protected PackageIdentifierType packageIdentifier;
    @XmlElement(name = "TradeTransaction")
    protected TradeTransaction tradeTransaction;
    @XmlElement(name = "DocumentProcessingResult")
    protected ProcessingResult documentProcessingResult;

    /**
     * Gets the value of the processedDocumentHeader property.
     * 
     * @return
     *     possible object is
     *     {@link DocumentHeader }
     *     
     */
    public DocumentHeader getProcessedDocumentHeader() {
        return processedDocumentHeader;
    }

    /**
     * Sets the value of the processedDocumentHeader property.
     * 
     * @param value
     *     allowed object is
     *     {@link DocumentHeader }
     *     
     */
    public void setProcessedDocumentHeader(DocumentHeader value) {
        this.processedDocumentHeader = value;
    }

    /**
     * Gets the value of the packageIdentifier property.
     * 
     * @return
     *     possible object is
     *     {@link PackageIdentifierType }
     *     
     */
    public PackageIdentifierType getPackageIdentifier() {
        return packageIdentifier;
    }

    /**
     * Sets the value of the packageIdentifier property.
     * 
     * @param value
     *     allowed object is
     *     {@link PackageIdentifierType }
     *     
     */
    public void setPackageIdentifier(PackageIdentifierType value) {
        this.packageIdentifier = value;
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
     * Gets the value of the documentProcessingResult property.
     * 
     * @return
     *     possible object is
     *     {@link ProcessingResult }
     *     
     */
    public ProcessingResult getDocumentProcessingResult() {
        return documentProcessingResult;
    }

    /**
     * Sets the value of the documentProcessingResult property.
     * 
     * @param value
     *     allowed object is
     *     {@link ProcessingResult }
     *     
     */
    public void setDocumentProcessingResult(ProcessingResult value) {
        this.documentProcessingResult = value;
    }

}
