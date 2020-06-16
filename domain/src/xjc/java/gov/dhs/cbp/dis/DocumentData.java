
package gov.dhs.cbp.dis;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlType;


/**
 * Document data such as Document ID, Label, Name, Version etc describing the submitted document
 * 
 * <p>Java class for DocumentData complex type.
 * 
 * <p>The following schema fragment specifies the expected content contained within this class.
 * 
 * <pre>
 * &lt;complexType name="DocumentData"&gt;
 *   &lt;complexContent&gt;
 *     &lt;restriction base="{http://www.w3.org/2001/XMLSchema}anyType"&gt;
 *       &lt;sequence&gt;
 *         &lt;element name="DocumentHeader" type="{http://cbp.dhs.gov/DIS}DocumentHeader"/&gt;
 *         &lt;element name="GovtAgencyList" type="{http://cbp.dhs.gov/DIS}GovtAgencyList" minOccurs="0"/&gt;
 *         &lt;element name="Comment" type="{http://www.w3.org/2001/XMLSchema}string" minOccurs="0"/&gt;
 *         &lt;element name="OptionalData" type="{http://cbp.dhs.gov/DIS}OptionalData" minOccurs="0"/&gt;
 *         &lt;element name="VesselData" type="{http://cbp.dhs.gov/DIS}VesselDataType" minOccurs="0"/&gt;
 *         &lt;element name="ContactInfo" type="{http://cbp.dhs.gov/DIS}ContactInfoType" minOccurs="0"/&gt;
 *         &lt;element name="DocumentObject" type="{http://www.w3.org/2001/XMLSchema}string"/&gt;
 *       &lt;/sequence&gt;
 *     &lt;/restriction&gt;
 *   &lt;/complexContent&gt;
 * &lt;/complexType&gt;
 * </pre>
 * 
 * 
 */
@XmlAccessorType(XmlAccessType.FIELD)
@XmlType(name = "DocumentData", propOrder = {
    "documentHeader",
    "govtAgencyList",
    "comment",
    "optionalData",
    "vesselData",
    "contactInfo",
    "documentObject"
})
public class DocumentData {

    @XmlElement(name = "DocumentHeader", required = true)
    protected DocumentHeader documentHeader;
    @XmlElement(name = "GovtAgencyList")
    protected GovtAgencyList govtAgencyList;
    @XmlElement(name = "Comment")
    protected String comment;
    @XmlElement(name = "OptionalData")
    protected OptionalData optionalData;
    @XmlElement(name = "VesselData")
    protected VesselDataType vesselData;
    @XmlElement(name = "ContactInfo")
    protected ContactInfoType contactInfo;
    @XmlElement(name = "DocumentObject", required = true)
    protected String documentObject;

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
     * Gets the value of the govtAgencyList property.
     * 
     * @return
     *     possible object is
     *     {@link GovtAgencyList }
     *     
     */
    public GovtAgencyList getGovtAgencyList() {
        return govtAgencyList;
    }

    /**
     * Sets the value of the govtAgencyList property.
     * 
     * @param value
     *     allowed object is
     *     {@link GovtAgencyList }
     *     
     */
    public void setGovtAgencyList(GovtAgencyList value) {
        this.govtAgencyList = value;
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

    /**
     * Gets the value of the optionalData property.
     * 
     * @return
     *     possible object is
     *     {@link OptionalData }
     *     
     */
    public OptionalData getOptionalData() {
        return optionalData;
    }

    /**
     * Sets the value of the optionalData property.
     * 
     * @param value
     *     allowed object is
     *     {@link OptionalData }
     *     
     */
    public void setOptionalData(OptionalData value) {
        this.optionalData = value;
    }

    /**
     * Gets the value of the vesselData property.
     * 
     * @return
     *     possible object is
     *     {@link VesselDataType }
     *     
     */
    public VesselDataType getVesselData() {
        return vesselData;
    }

    /**
     * Sets the value of the vesselData property.
     * 
     * @param value
     *     allowed object is
     *     {@link VesselDataType }
     *     
     */
    public void setVesselData(VesselDataType value) {
        this.vesselData = value;
    }

    /**
     * Gets the value of the contactInfo property.
     * 
     * @return
     *     possible object is
     *     {@link ContactInfoType }
     *     
     */
    public ContactInfoType getContactInfo() {
        return contactInfo;
    }

    /**
     * Sets the value of the contactInfo property.
     * 
     * @param value
     *     allowed object is
     *     {@link ContactInfoType }
     *     
     */
    public void setContactInfo(ContactInfoType value) {
        this.contactInfo = value;
    }

    /**
     * Gets the value of the documentObject property.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getDocumentObject() {
        return documentObject;
    }

    /**
     * Sets the value of the documentObject property.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setDocumentObject(String value) {
        this.documentObject = value;
    }

}
