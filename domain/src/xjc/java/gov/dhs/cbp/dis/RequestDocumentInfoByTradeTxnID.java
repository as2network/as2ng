
package gov.dhs.cbp.dis;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlType;


/**
 * Used to Request Document Information by a specified Trade Transaction ID
 * 
 * <p>Java class for RequestDocumentInfoByTradeTxnID complex type.
 * 
 * <p>The following schema fragment specifies the expected content contained within this class.
 * 
 * <pre>
 * &lt;complexType name="RequestDocumentInfoByTradeTxnID"&gt;
 *   &lt;complexContent&gt;
 *     &lt;restriction base="{http://www.w3.org/2001/XMLSchema}anyType"&gt;
 *       &lt;sequence&gt;
 *         &lt;element name="RequestID" type="{http://www.w3.org/2001/XMLSchema}string"/&gt;
 *         &lt;element name="TradeTransactionID" type="{http://cbp.dhs.gov/DIS}TradeTransaction"/&gt;
 *       &lt;/sequence&gt;
 *     &lt;/restriction&gt;
 *   &lt;/complexContent&gt;
 * &lt;/complexType&gt;
 * </pre>
 * 
 * 
 */
@XmlAccessorType(XmlAccessType.FIELD)
@XmlType(name = "RequestDocumentInfoByTradeTxnID", propOrder = {
    "requestID",
    "tradeTransactionID"
})
public class RequestDocumentInfoByTradeTxnID {

    @XmlElement(name = "RequestID", required = true)
    protected String requestID;
    @XmlElement(name = "TradeTransactionID", required = true)
    protected TradeTransaction tradeTransactionID;

    /**
     * Gets the value of the requestID property.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getRequestID() {
        return requestID;
    }

    /**
     * Sets the value of the requestID property.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setRequestID(String value) {
        this.requestID = value;
    }

    /**
     * Gets the value of the tradeTransactionID property.
     * 
     * @return
     *     possible object is
     *     {@link TradeTransaction }
     *     
     */
    public TradeTransaction getTradeTransactionID() {
        return tradeTransactionID;
    }

    /**
     * Sets the value of the tradeTransactionID property.
     * 
     * @param value
     *     allowed object is
     *     {@link TradeTransaction }
     *     
     */
    public void setTradeTransactionID(TradeTransaction value) {
        this.tradeTransactionID = value;
    }

}
