
package gov.dhs.cbp.dis;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlType;


/**
 * Toxic Substances Data
 * 
 * <p>Java class for ToxicSubstancesData complex type.
 * 
 * <p>The following schema fragment specifies the expected content contained within this class.
 * 
 * <pre>
 * &lt;complexType name="ToxicSubstancesData"&gt;
 *   &lt;complexContent&gt;
 *     &lt;restriction base="{http://www.w3.org/2001/XMLSchema}anyType"&gt;
 *       &lt;sequence&gt;
 *         &lt;element name="CASNbr" type="{http://www.w3.org/2001/XMLSchema}string" minOccurs="0"/&gt;
 *         &lt;element name="EPARegistrationNbr" type="{http://www.w3.org/2001/XMLSchema}string" minOccurs="0"/&gt;
 *         &lt;element name="EPAProducerEstNbr" type="{http://www.w3.org/2001/XMLSchema}string" minOccurs="0"/&gt;
 *       &lt;/sequence&gt;
 *     &lt;/restriction&gt;
 *   &lt;/complexContent&gt;
 * &lt;/complexType&gt;
 * </pre>
 * 
 * 
 */
@XmlAccessorType(XmlAccessType.FIELD)
@XmlType(name = "ToxicSubstancesData", propOrder = {
    "casNbr",
    "epaRegistrationNbr",
    "epaProducerEstNbr"
})
public class ToxicSubstancesData {

    @XmlElement(name = "CASNbr")
    protected String casNbr;
    @XmlElement(name = "EPARegistrationNbr")
    protected String epaRegistrationNbr;
    @XmlElement(name = "EPAProducerEstNbr")
    protected String epaProducerEstNbr;

    /**
     * Gets the value of the casNbr property.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getCASNbr() {
        return casNbr;
    }

    /**
     * Sets the value of the casNbr property.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setCASNbr(String value) {
        this.casNbr = value;
    }

    /**
     * Gets the value of the epaRegistrationNbr property.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getEPARegistrationNbr() {
        return epaRegistrationNbr;
    }

    /**
     * Sets the value of the epaRegistrationNbr property.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setEPARegistrationNbr(String value) {
        this.epaRegistrationNbr = value;
    }

    /**
     * Gets the value of the epaProducerEstNbr property.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getEPAProducerEstNbr() {
        return epaProducerEstNbr;
    }

    /**
     * Sets the value of the epaProducerEstNbr property.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setEPAProducerEstNbr(String value) {
        this.epaProducerEstNbr = value;
    }

}
