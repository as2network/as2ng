
package gov.dhs.cbp.dis;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlType;


/**
 * <p>Java class for VesselDataType complex type.
 * 
 * <p>The following schema fragment specifies the expected content contained within this class.
 * 
 * <pre>
 * &lt;complexType name="VesselDataType"&gt;
 *   &lt;complexContent&gt;
 *     &lt;restriction base="{http://www.w3.org/2001/XMLSchema}anyType"&gt;
 *       &lt;sequence&gt;
 *         &lt;element name="CarrierSCAC" type="{http://www.w3.org/2001/XMLSchema}string" minOccurs="0"/&gt;
 *         &lt;element name="License_PermitNumber" type="{http://www.w3.org/2001/XMLSchema}string" minOccurs="0"/&gt;
 *         &lt;element name="CertificateNumber" type="{http://www.w3.org/2001/XMLSchema}string" minOccurs="0"/&gt;
 *         &lt;element name="IssueDate" type="{http://www.w3.org/2001/XMLSchema}string" minOccurs="0"/&gt;
 *         &lt;element name="ExpirationDate" type="{http://www.w3.org/2001/XMLSchema}string" minOccurs="0"/&gt;
 *         &lt;element name="GrossTonnage" type="{http://www.w3.org/2001/XMLSchema}string" minOccurs="0"/&gt;
 *         &lt;element name="NetTonnage" type="{http://www.w3.org/2001/XMLSchema}string" minOccurs="0"/&gt;
 *         &lt;element name="VesselVoyageNumber" type="{http://www.w3.org/2001/XMLSchema}string" minOccurs="0"/&gt;
 *         &lt;element name="VesselVoyageSegmentNbr" type="{http://www.w3.org/2001/XMLSchema}string" minOccurs="0"/&gt;
 *         &lt;element name="VesselInspectionID" type="{http://www.w3.org/2001/XMLSchema}string" minOccurs="0"/&gt;
 *       &lt;/sequence&gt;
 *     &lt;/restriction&gt;
 *   &lt;/complexContent&gt;
 * &lt;/complexType&gt;
 * </pre>
 * 
 * 
 */
@XmlAccessorType(XmlAccessType.FIELD)
@XmlType(name = "VesselDataType", propOrder = {
    "carrierSCAC",
    "licensePermitNumber",
    "certificateNumber",
    "issueDate",
    "expirationDate",
    "grossTonnage",
    "netTonnage",
    "vesselVoyageNumber",
    "vesselVoyageSegmentNbr",
    "vesselInspectionID"
})
public class VesselDataType {

    @XmlElement(name = "CarrierSCAC")
    protected String carrierSCAC;
    @XmlElement(name = "License_PermitNumber")
    protected String licensePermitNumber;
    @XmlElement(name = "CertificateNumber")
    protected String certificateNumber;
    @XmlElement(name = "IssueDate")
    protected String issueDate;
    @XmlElement(name = "ExpirationDate")
    protected String expirationDate;
    @XmlElement(name = "GrossTonnage")
    protected String grossTonnage;
    @XmlElement(name = "NetTonnage")
    protected String netTonnage;
    @XmlElement(name = "VesselVoyageNumber")
    protected String vesselVoyageNumber;
    @XmlElement(name = "VesselVoyageSegmentNbr")
    protected String vesselVoyageSegmentNbr;
    @XmlElement(name = "VesselInspectionID")
    protected String vesselInspectionID;

    /**
     * Gets the value of the carrierSCAC property.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getCarrierSCAC() {
        return carrierSCAC;
    }

    /**
     * Sets the value of the carrierSCAC property.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setCarrierSCAC(String value) {
        this.carrierSCAC = value;
    }

    /**
     * Gets the value of the licensePermitNumber property.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getLicensePermitNumber() {
        return licensePermitNumber;
    }

    /**
     * Sets the value of the licensePermitNumber property.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setLicensePermitNumber(String value) {
        this.licensePermitNumber = value;
    }

    /**
     * Gets the value of the certificateNumber property.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getCertificateNumber() {
        return certificateNumber;
    }

    /**
     * Sets the value of the certificateNumber property.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setCertificateNumber(String value) {
        this.certificateNumber = value;
    }

    /**
     * Gets the value of the issueDate property.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getIssueDate() {
        return issueDate;
    }

    /**
     * Sets the value of the issueDate property.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setIssueDate(String value) {
        this.issueDate = value;
    }

    /**
     * Gets the value of the expirationDate property.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getExpirationDate() {
        return expirationDate;
    }

    /**
     * Sets the value of the expirationDate property.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setExpirationDate(String value) {
        this.expirationDate = value;
    }

    /**
     * Gets the value of the grossTonnage property.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getGrossTonnage() {
        return grossTonnage;
    }

    /**
     * Sets the value of the grossTonnage property.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setGrossTonnage(String value) {
        this.grossTonnage = value;
    }

    /**
     * Gets the value of the netTonnage property.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getNetTonnage() {
        return netTonnage;
    }

    /**
     * Sets the value of the netTonnage property.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setNetTonnage(String value) {
        this.netTonnage = value;
    }

    /**
     * Gets the value of the vesselVoyageNumber property.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getVesselVoyageNumber() {
        return vesselVoyageNumber;
    }

    /**
     * Sets the value of the vesselVoyageNumber property.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setVesselVoyageNumber(String value) {
        this.vesselVoyageNumber = value;
    }

    /**
     * Gets the value of the vesselVoyageSegmentNbr property.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getVesselVoyageSegmentNbr() {
        return vesselVoyageSegmentNbr;
    }

    /**
     * Sets the value of the vesselVoyageSegmentNbr property.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setVesselVoyageSegmentNbr(String value) {
        this.vesselVoyageSegmentNbr = value;
    }

    /**
     * Gets the value of the vesselInspectionID property.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getVesselInspectionID() {
        return vesselInspectionID;
    }

    /**
     * Sets the value of the vesselInspectionID property.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setVesselInspectionID(String value) {
        this.vesselInspectionID = value;
    }

}
