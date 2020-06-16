
package gov.dhs.cbp.dis;

import java.util.ArrayList;
import java.util.List;
import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlType;


/**
 * <p>Java class for DocumentSubmissionPackage complex type.
 * 
 * <p>The following schema fragment specifies the expected content contained within this class.
 * 
 * <pre>
 * &lt;complexType name="DocumentSubmissionPackage"&gt;
 *   &lt;complexContent&gt;
 *     &lt;restriction base="{http://www.w3.org/2001/XMLSchema}anyType"&gt;
 *       &lt;sequence&gt;
 *         &lt;element name="PackageIdentifier" type="{http://cbp.dhs.gov/DIS}PackageIdentifierType" minOccurs="0"/&gt;
 *         &lt;element name="SubmittedToPortCode" type="{http://cbp.dhs.gov/DIS}SubmittedToPortCode" minOccurs="0"/&gt;
 *         &lt;element name="ActionCode" type="{http://www.w3.org/2001/XMLSchema}string"/&gt;
 *         &lt;element name="TradeTransaction" type="{http://cbp.dhs.gov/DIS}TradeTransaction" minOccurs="0"/&gt;
 *         &lt;element name="CBPRequest" type="{http://cbp.dhs.gov/DIS}CBPRequest" minOccurs="0"/&gt;
 *         &lt;element name="DocumentData" type="{http://cbp.dhs.gov/DIS}DocumentData" maxOccurs="unbounded"/&gt;
 *       &lt;/sequence&gt;
 *     &lt;/restriction&gt;
 *   &lt;/complexContent&gt;
 * &lt;/complexType&gt;
 * </pre>
 * 
 * 
 */
@XmlAccessorType(XmlAccessType.FIELD)
@XmlType(name = "DocumentSubmissionPackage", propOrder = {
    "packageIdentifier",
    "submittedToPortCode",
    "actionCode",
    "tradeTransaction",
    "cbpRequest",
    "documentData"
})
public class DocumentSubmissionPackage {

    @XmlElement(name = "PackageIdentifier")
    protected PackageIdentifierType packageIdentifier;
    @XmlElement(name = "SubmittedToPortCode")
    protected String submittedToPortCode;
    @XmlElement(name = "ActionCode", required = true)
    protected String actionCode;
    @XmlElement(name = "TradeTransaction")
    protected TradeTransaction tradeTransaction;
    @XmlElement(name = "CBPRequest")
    protected CBPRequest cbpRequest;
    @XmlElement(name = "DocumentData", required = true)
    protected List<DocumentData> documentData;

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
     * Gets the value of the actionCode property.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getActionCode() {
        return actionCode;
    }

    /**
     * Sets the value of the actionCode property.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setActionCode(String value) {
        this.actionCode = value;
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
     * Gets the value of the cbpRequest property.
     * 
     * @return
     *     possible object is
     *     {@link CBPRequest }
     *     
     */
    public CBPRequest getCBPRequest() {
        return cbpRequest;
    }

    /**
     * Sets the value of the cbpRequest property.
     * 
     * @param value
     *     allowed object is
     *     {@link CBPRequest }
     *     
     */
    public void setCBPRequest(CBPRequest value) {
        this.cbpRequest = value;
    }

    /**
     * Gets the value of the documentData property.
     * 
     * <p>
     * This accessor method returns a reference to the live list,
     * not a snapshot. Therefore any modification you make to the
     * returned list will be present inside the JAXB object.
     * This is why there is not a <CODE>set</CODE> method for the documentData property.
     * 
     * <p>
     * For example, to add a new item, do as follows:
     * <pre>
     *    getDocumentData().add(newItem);
     * </pre>
     * 
     * 
     * <p>
     * Objects of the following type(s) are allowed in the list
     * {@link DocumentData }
     * 
     * 
     */
    public List<DocumentData> getDocumentData() {
        if (documentData == null) {
            documentData = new ArrayList<DocumentData>();
        }
        return this.documentData;
    }

}
