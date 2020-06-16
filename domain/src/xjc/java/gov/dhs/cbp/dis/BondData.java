
package gov.dhs.cbp.dis;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlType;


/**
 * Bond Specific Information
 * 
 * <p>Java class for BondData complex type.
 * 
 * <p>The following schema fragment specifies the expected content contained within this class.
 * 
 * <pre>
 * &lt;complexType name="BondData"&gt;
 *   &lt;complexContent&gt;
 *     &lt;restriction base="{http://www.w3.org/2001/XMLSchema}anyType"&gt;
 *       &lt;sequence&gt;
 *         &lt;element name="BondName" type="{http://www.w3.org/2001/XMLSchema}string" minOccurs="0"/&gt;
 *         &lt;element name="BondNumber" type="{http://www.w3.org/2001/XMLSchema}string" minOccurs="0"/&gt;
 *         &lt;element name="BondType" type="{http://www.w3.org/2001/XMLSchema}string" minOccurs="0"/&gt;
 *         &lt;element name="SuretyCode" type="{http://www.w3.org/2001/XMLSchema}string" minOccurs="0"/&gt;
 *         &lt;element name="AgentIDNbr" type="{http://www.w3.org/2001/XMLSchema}string" minOccurs="0"/&gt;
 *         &lt;element name="Filer" type="{http://www.w3.org/2001/XMLSchema}string" minOccurs="0"/&gt;
 *         &lt;element name="BondAmount" type="{http://www.w3.org/2001/XMLSchema}string" minOccurs="0"/&gt;
 *       &lt;/sequence&gt;
 *     &lt;/restriction&gt;
 *   &lt;/complexContent&gt;
 * &lt;/complexType&gt;
 * </pre>
 * 
 * 
 */
@XmlAccessorType(XmlAccessType.FIELD)
@XmlType(name = "BondData", propOrder = {
    "bondName",
    "bondNumber",
    "bondType",
    "suretyCode",
    "agentIDNbr",
    "filer",
    "bondAmount"
})
public class BondData {

    @XmlElement(name = "BondName")
    protected String bondName;
    @XmlElement(name = "BondNumber")
    protected String bondNumber;
    @XmlElement(name = "BondType")
    protected String bondType;
    @XmlElement(name = "SuretyCode")
    protected String suretyCode;
    @XmlElement(name = "AgentIDNbr")
    protected String agentIDNbr;
    @XmlElement(name = "Filer")
    protected String filer;
    @XmlElement(name = "BondAmount")
    protected String bondAmount;

    /**
     * Gets the value of the bondName property.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getBondName() {
        return bondName;
    }

    /**
     * Sets the value of the bondName property.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setBondName(String value) {
        this.bondName = value;
    }

    /**
     * Gets the value of the bondNumber property.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getBondNumber() {
        return bondNumber;
    }

    /**
     * Sets the value of the bondNumber property.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setBondNumber(String value) {
        this.bondNumber = value;
    }

    /**
     * Gets the value of the bondType property.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getBondType() {
        return bondType;
    }

    /**
     * Sets the value of the bondType property.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setBondType(String value) {
        this.bondType = value;
    }

    /**
     * Gets the value of the suretyCode property.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getSuretyCode() {
        return suretyCode;
    }

    /**
     * Sets the value of the suretyCode property.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setSuretyCode(String value) {
        this.suretyCode = value;
    }

    /**
     * Gets the value of the agentIDNbr property.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getAgentIDNbr() {
        return agentIDNbr;
    }

    /**
     * Sets the value of the agentIDNbr property.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setAgentIDNbr(String value) {
        this.agentIDNbr = value;
    }

    /**
     * Gets the value of the filer property.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getFiler() {
        return filer;
    }

    /**
     * Sets the value of the filer property.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setFiler(String value) {
        this.filer = value;
    }

    /**
     * Gets the value of the bondAmount property.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getBondAmount() {
        return bondAmount;
    }

    /**
     * Sets the value of the bondAmount property.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setBondAmount(String value) {
        this.bondAmount = value;
    }

}
