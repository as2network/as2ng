
package gov.dhs.cbp.dis;

import java.util.ArrayList;
import java.util.List;
import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlType;


/**
 * <p>Java class for ProcessingResult complex type.
 * 
 * <p>The following schema fragment specifies the expected content contained within this class.
 * 
 * <pre>
 * &lt;complexType name="ProcessingResult"&gt;
 *   &lt;complexContent&gt;
 *     &lt;restriction base="{http://www.w3.org/2001/XMLSchema}anyType"&gt;
 *       &lt;sequence&gt;
 *         &lt;element name="ProcessingEvent" type="{http://www.w3.org/2001/XMLSchema}string" minOccurs="0"/&gt;
 *         &lt;element name="ProcessingStatus" type="{http://www.w3.org/2001/XMLSchema}string" minOccurs="0"/&gt;
 *         &lt;element name="ProcessingStatusSummaryText" type="{http://www.w3.org/2001/XMLSchema}string" minOccurs="0"/&gt;
 *         &lt;element name="ProcessingLogText" type="{http://www.w3.org/2001/XMLSchema}string" maxOccurs="unbounded" minOccurs="0"/&gt;
 *       &lt;/sequence&gt;
 *     &lt;/restriction&gt;
 *   &lt;/complexContent&gt;
 * &lt;/complexType&gt;
 * </pre>
 * 
 * 
 */
@XmlAccessorType(XmlAccessType.FIELD)
@XmlType(name = "ProcessingResult", propOrder = {
    "processingEvent",
    "processingStatus",
    "processingStatusSummaryText",
    "processingLogText"
})
public class ProcessingResult {

    @XmlElement(name = "ProcessingEvent")
    protected String processingEvent;
    @XmlElement(name = "ProcessingStatus")
    protected String processingStatus;
    @XmlElement(name = "ProcessingStatusSummaryText")
    protected String processingStatusSummaryText;
    @XmlElement(name = "ProcessingLogText")
    protected List<String> processingLogText;

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
     * Gets the value of the processingStatus property.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getProcessingStatus() {
        return processingStatus;
    }

    /**
     * Sets the value of the processingStatus property.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setProcessingStatus(String value) {
        this.processingStatus = value;
    }

    /**
     * Gets the value of the processingStatusSummaryText property.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getProcessingStatusSummaryText() {
        return processingStatusSummaryText;
    }

    /**
     * Sets the value of the processingStatusSummaryText property.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setProcessingStatusSummaryText(String value) {
        this.processingStatusSummaryText = value;
    }

    /**
     * Gets the value of the processingLogText property.
     * 
     * <p>
     * This accessor method returns a reference to the live list,
     * not a snapshot. Therefore any modification you make to the
     * returned list will be present inside the JAXB object.
     * This is why there is not a <CODE>set</CODE> method for the processingLogText property.
     * 
     * <p>
     * For example, to add a new item, do as follows:
     * <pre>
     *    getProcessingLogText().add(newItem);
     * </pre>
     * 
     * 
     * <p>
     * Objects of the following type(s) are allowed in the list
     * {@link String }
     * 
     * 
     */
    public List<String> getProcessingLogText() {
        if (processingLogText == null) {
            processingLogText = new ArrayList<String>();
        }
        return this.processingLogText;
    }

}
