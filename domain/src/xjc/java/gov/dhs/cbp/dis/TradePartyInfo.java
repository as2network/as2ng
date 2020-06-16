
package gov.dhs.cbp.dis;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlType;


/**
 * <p>Java class for TradePartyInfo complex type.
 * 
 * <p>The following schema fragment specifies the expected content contained within this class.
 * 
 * <pre>
 * &lt;complexType name="TradePartyInfo"&gt;
 *   &lt;complexContent&gt;
 *     &lt;restriction base="{http://www.w3.org/2001/XMLSchema}anyType"&gt;
 *       &lt;sequence&gt;
 *         &lt;element name="TradePartyID" type="{http://www.w3.org/2001/XMLSchema}string"/&gt;
 *         &lt;element name="TradePartyType" type="{http://www.w3.org/2001/XMLSchema}string"/&gt;
 *         &lt;element name="TradePartyName" type="{http://www.w3.org/2001/XMLSchema}string" minOccurs="0"/&gt;
 *         &lt;element name="TradePartyAddress" type="{http://www.w3.org/2001/XMLSchema}string" minOccurs="0"/&gt;
 *       &lt;/sequence&gt;
 *     &lt;/restriction&gt;
 *   &lt;/complexContent&gt;
 * &lt;/complexType&gt;
 * </pre>
 * 
 * 
 */
@XmlAccessorType(XmlAccessType.FIELD)
@XmlType(name = "TradePartyInfo", propOrder = {
    "tradePartyID",
    "tradePartyType",
    "tradePartyName",
    "tradePartyAddress"
})
public class TradePartyInfo {

    @XmlElement(name = "TradePartyID", required = true)
    protected String tradePartyID;
    @XmlElement(name = "TradePartyType", required = true)
    protected String tradePartyType;
    @XmlElement(name = "TradePartyName")
    protected String tradePartyName;
    @XmlElement(name = "TradePartyAddress")
    protected String tradePartyAddress;

    /**
     * Gets the value of the tradePartyID property.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getTradePartyID() {
        return tradePartyID;
    }

    /**
     * Sets the value of the tradePartyID property.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setTradePartyID(String value) {
        this.tradePartyID = value;
    }

    /**
     * Gets the value of the tradePartyType property.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getTradePartyType() {
        return tradePartyType;
    }

    /**
     * Sets the value of the tradePartyType property.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setTradePartyType(String value) {
        this.tradePartyType = value;
    }

    /**
     * Gets the value of the tradePartyName property.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getTradePartyName() {
        return tradePartyName;
    }

    /**
     * Sets the value of the tradePartyName property.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setTradePartyName(String value) {
        this.tradePartyName = value;
    }

    /**
     * Gets the value of the tradePartyAddress property.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getTradePartyAddress() {
        return tradePartyAddress;
    }

    /**
     * Sets the value of the tradePartyAddress property.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setTradePartyAddress(String value) {
        this.tradePartyAddress = value;
    }

}
