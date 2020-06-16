
package gov.dhs.cbp.dis;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlSchemaType;
import javax.xml.bind.annotation.XmlType;
import javax.xml.datatype.XMLGregorianCalendar;


/**
 * One Message Header should be included on every message from a Trade Party to CBP. It contains logistical header Information about the message. 
 * 
 * <p>Java class for MessageHeader complex type.
 * 
 * <p>The following schema fragment specifies the expected content contained within this class.
 * 
 * <pre>
 * &lt;complexType name="MessageHeader"&gt;
 *   &lt;complexContent&gt;
 *     &lt;restriction base="{http://www.w3.org/2001/XMLSchema}anyType"&gt;
 *       &lt;sequence&gt;
 *         &lt;element name="MessageID" type="{http://www.w3.org/2001/XMLSchema}string"/&gt;
 *         &lt;element name="MessageType" type="{http://www.w3.org/2001/XMLSchema}string"/&gt;
 *         &lt;element name="SentDateTime" type="{http://www.w3.org/2001/XMLSchema}dateTime"/&gt;
 *         &lt;element name="TransmitterID" type="{http://www.w3.org/2001/XMLSchema}string"/&gt;
 *         &lt;element name="TransmitterSiteCode" type="{http://www.w3.org/2001/XMLSchema}string"/&gt;
 *         &lt;element name="PreparerID" type="{http://www.w3.org/2001/XMLSchema}string"/&gt;
 *         &lt;element name="PreparerSiteCode" type="{http://www.w3.org/2001/XMLSchema}string"/&gt;
 *         &lt;element name="Comment" type="{http://www.w3.org/2001/XMLSchema}string" minOccurs="0"/&gt;
 *       &lt;/sequence&gt;
 *     &lt;/restriction&gt;
 *   &lt;/complexContent&gt;
 * &lt;/complexType&gt;
 * </pre>
 * 
 * 
 */
@XmlAccessorType(XmlAccessType.FIELD)
@XmlType(name = "MessageHeader", propOrder = {
    "messageID",
    "messageType",
    "sentDateTime",
    "transmitterID",
    "transmitterSiteCode",
    "preparerID",
    "preparerSiteCode",
    "comment"
})
public class MessageHeader {

    @XmlElement(name = "MessageID", required = true)
    protected String messageID;
    @XmlElement(name = "MessageType", required = true)
    protected String messageType;
    @XmlElement(name = "SentDateTime", required = true)
    @XmlSchemaType(name = "dateTime")
    protected XMLGregorianCalendar sentDateTime;
    @XmlElement(name = "TransmitterID", required = true)
    protected String transmitterID;
    @XmlElement(name = "TransmitterSiteCode", required = true)
    protected String transmitterSiteCode;
    @XmlElement(name = "PreparerID", required = true)
    protected String preparerID;
    @XmlElement(name = "PreparerSiteCode", required = true)
    protected String preparerSiteCode;
    @XmlElement(name = "Comment")
    protected String comment;

    /**
     * Gets the value of the messageID property.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getMessageID() {
        return messageID;
    }

    /**
     * Sets the value of the messageID property.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setMessageID(String value) {
        this.messageID = value;
    }

    /**
     * Gets the value of the messageType property.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getMessageType() {
        return messageType;
    }

    /**
     * Sets the value of the messageType property.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setMessageType(String value) {
        this.messageType = value;
    }

    /**
     * Gets the value of the sentDateTime property.
     * 
     * @return
     *     possible object is
     *     {@link XMLGregorianCalendar }
     *     
     */
    public XMLGregorianCalendar getSentDateTime() {
        return sentDateTime;
    }

    /**
     * Sets the value of the sentDateTime property.
     * 
     * @param value
     *     allowed object is
     *     {@link XMLGregorianCalendar }
     *     
     */
    public void setSentDateTime(XMLGregorianCalendar value) {
        this.sentDateTime = value;
    }

    /**
     * Gets the value of the transmitterID property.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getTransmitterID() {
        return transmitterID;
    }

    /**
     * Sets the value of the transmitterID property.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setTransmitterID(String value) {
        this.transmitterID = value;
    }

    /**
     * Gets the value of the transmitterSiteCode property.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getTransmitterSiteCode() {
        return transmitterSiteCode;
    }

    /**
     * Sets the value of the transmitterSiteCode property.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setTransmitterSiteCode(String value) {
        this.transmitterSiteCode = value;
    }

    /**
     * Gets the value of the preparerID property.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getPreparerID() {
        return preparerID;
    }

    /**
     * Sets the value of the preparerID property.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setPreparerID(String value) {
        this.preparerID = value;
    }

    /**
     * Gets the value of the preparerSiteCode property.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getPreparerSiteCode() {
        return preparerSiteCode;
    }

    /**
     * Sets the value of the preparerSiteCode property.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setPreparerSiteCode(String value) {
        this.preparerSiteCode = value;
    }

    /**
     * Gets the value of the comment property.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getComment() {
        return comment;
    }

    /**
     * Sets the value of the comment property.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setComment(String value) {
        this.comment = value;
    }

}
