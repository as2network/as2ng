
package gov.dhs.cbp.dis;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlType;


/**
 * <p>Java class for MessageLevelResult complex type.
 * 
 * <p>The following schema fragment specifies the expected content contained within this class.
 * 
 * <pre>
 * &lt;complexType name="MessageLevelResult"&gt;
 *   &lt;complexContent&gt;
 *     &lt;restriction base="{http://www.w3.org/2001/XMLSchema}anyType"&gt;
 *       &lt;sequence&gt;
 *         &lt;element name="ProcessedMessageHeader" type="{http://cbp.dhs.gov/DIS}MessageHeader"/&gt;
 *         &lt;element name="MessageProcessingResult" type="{http://cbp.dhs.gov/DIS}ProcessingResult"/&gt;
 *       &lt;/sequence&gt;
 *     &lt;/restriction&gt;
 *   &lt;/complexContent&gt;
 * &lt;/complexType&gt;
 * </pre>
 * 
 * 
 */
@XmlAccessorType(XmlAccessType.FIELD)
@XmlType(name = "MessageLevelResult", propOrder = {
    "processedMessageHeader",
    "messageProcessingResult"
})
public class MessageLevelResult {

    @XmlElement(name = "ProcessedMessageHeader", required = true)
    protected MessageHeader processedMessageHeader;
    @XmlElement(name = "MessageProcessingResult", required = true)
    protected ProcessingResult messageProcessingResult;

    /**
     * Gets the value of the processedMessageHeader property.
     * 
     * @return
     *     possible object is
     *     {@link MessageHeader }
     *     
     */
    public MessageHeader getProcessedMessageHeader() {
        return processedMessageHeader;
    }

    /**
     * Sets the value of the processedMessageHeader property.
     * 
     * @param value
     *     allowed object is
     *     {@link MessageHeader }
     *     
     */
    public void setProcessedMessageHeader(MessageHeader value) {
        this.processedMessageHeader = value;
    }

    /**
     * Gets the value of the messageProcessingResult property.
     * 
     * @return
     *     possible object is
     *     {@link ProcessingResult }
     *     
     */
    public ProcessingResult getMessageProcessingResult() {
        return messageProcessingResult;
    }

    /**
     * Sets the value of the messageProcessingResult property.
     * 
     * @param value
     *     allowed object is
     *     {@link ProcessingResult }
     *     
     */
    public void setMessageProcessingResult(ProcessingResult value) {
        this.messageProcessingResult = value;
    }

}
