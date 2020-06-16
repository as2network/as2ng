
package gov.dhs.cbp.dis;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlType;


/**
 * <p>Java class for OptionalData complex type.
 * 
 * <p>The following schema fragment specifies the expected content contained within this class.
 * 
 * <pre>
 * &lt;complexType name="OptionalData"&gt;
 *   &lt;complexContent&gt;
 *     &lt;restriction base="{http://www.w3.org/2001/XMLSchema}anyType"&gt;
 *       &lt;sequence&gt;
 *         &lt;element name="InvoiceData" type="{http://cbp.dhs.gov/DIS}InvoiceData" minOccurs="0"/&gt;
 *         &lt;element name="BondData" type="{http://cbp.dhs.gov/DIS}BondData" minOccurs="0"/&gt;
 *         &lt;element name="PackingListData" type="{http://cbp.dhs.gov/DIS}PackingListData" minOccurs="0"/&gt;
 *         &lt;element name="CertificateData" type="{http://cbp.dhs.gov/DIS}CertificateData" minOccurs="0"/&gt;
 *         &lt;element name="PermitData" type="{http://cbp.dhs.gov/DIS}PermitData" minOccurs="0"/&gt;
 *         &lt;element name="ToxicSubstancesData" type="{http://cbp.dhs.gov/DIS}ToxicSubstancesData" minOccurs="0"/&gt;
 *         &lt;element name="CommodityList" type="{http://cbp.dhs.gov/DIS}CommodityList" minOccurs="0"/&gt;
 *         &lt;element name="AdditionalData" type="{http://cbp.dhs.gov/DIS}AdditionalData" minOccurs="0"/&gt;
 *       &lt;/sequence&gt;
 *     &lt;/restriction&gt;
 *   &lt;/complexContent&gt;
 * &lt;/complexType&gt;
 * </pre>
 * 
 * 
 */
@XmlAccessorType(XmlAccessType.FIELD)
@XmlType(name = "OptionalData", propOrder = {
    "invoiceData",
    "bondData",
    "packingListData",
    "certificateData",
    "permitData",
    "toxicSubstancesData",
    "commodityList",
    "additionalData"
})
public class OptionalData {

    @XmlElement(name = "InvoiceData")
    protected InvoiceData invoiceData;
    @XmlElement(name = "BondData")
    protected BondData bondData;
    @XmlElement(name = "PackingListData")
    protected PackingListData packingListData;
    @XmlElement(name = "CertificateData")
    protected CertificateData certificateData;
    @XmlElement(name = "PermitData")
    protected PermitData permitData;
    @XmlElement(name = "ToxicSubstancesData")
    protected ToxicSubstancesData toxicSubstancesData;
    @XmlElement(name = "CommodityList")
    protected CommodityList commodityList;
    @XmlElement(name = "AdditionalData")
    protected AdditionalData additionalData;

    /**
     * Gets the value of the invoiceData property.
     * 
     * @return
     *     possible object is
     *     {@link InvoiceData }
     *     
     */
    public InvoiceData getInvoiceData() {
        return invoiceData;
    }

    /**
     * Sets the value of the invoiceData property.
     * 
     * @param value
     *     allowed object is
     *     {@link InvoiceData }
     *     
     */
    public void setInvoiceData(InvoiceData value) {
        this.invoiceData = value;
    }

    /**
     * Gets the value of the bondData property.
     * 
     * @return
     *     possible object is
     *     {@link BondData }
     *     
     */
    public BondData getBondData() {
        return bondData;
    }

    /**
     * Sets the value of the bondData property.
     * 
     * @param value
     *     allowed object is
     *     {@link BondData }
     *     
     */
    public void setBondData(BondData value) {
        this.bondData = value;
    }

    /**
     * Gets the value of the packingListData property.
     * 
     * @return
     *     possible object is
     *     {@link PackingListData }
     *     
     */
    public PackingListData getPackingListData() {
        return packingListData;
    }

    /**
     * Sets the value of the packingListData property.
     * 
     * @param value
     *     allowed object is
     *     {@link PackingListData }
     *     
     */
    public void setPackingListData(PackingListData value) {
        this.packingListData = value;
    }

    /**
     * Gets the value of the certificateData property.
     * 
     * @return
     *     possible object is
     *     {@link CertificateData }
     *     
     */
    public CertificateData getCertificateData() {
        return certificateData;
    }

    /**
     * Sets the value of the certificateData property.
     * 
     * @param value
     *     allowed object is
     *     {@link CertificateData }
     *     
     */
    public void setCertificateData(CertificateData value) {
        this.certificateData = value;
    }

    /**
     * Gets the value of the permitData property.
     * 
     * @return
     *     possible object is
     *     {@link PermitData }
     *     
     */
    public PermitData getPermitData() {
        return permitData;
    }

    /**
     * Sets the value of the permitData property.
     * 
     * @param value
     *     allowed object is
     *     {@link PermitData }
     *     
     */
    public void setPermitData(PermitData value) {
        this.permitData = value;
    }

    /**
     * Gets the value of the toxicSubstancesData property.
     * 
     * @return
     *     possible object is
     *     {@link ToxicSubstancesData }
     *     
     */
    public ToxicSubstancesData getToxicSubstancesData() {
        return toxicSubstancesData;
    }

    /**
     * Sets the value of the toxicSubstancesData property.
     * 
     * @param value
     *     allowed object is
     *     {@link ToxicSubstancesData }
     *     
     */
    public void setToxicSubstancesData(ToxicSubstancesData value) {
        this.toxicSubstancesData = value;
    }

    /**
     * Gets the value of the commodityList property.
     * 
     * @return
     *     possible object is
     *     {@link CommodityList }
     *     
     */
    public CommodityList getCommodityList() {
        return commodityList;
    }

    /**
     * Sets the value of the commodityList property.
     * 
     * @param value
     *     allowed object is
     *     {@link CommodityList }
     *     
     */
    public void setCommodityList(CommodityList value) {
        this.commodityList = value;
    }

    /**
     * Gets the value of the additionalData property.
     * 
     * @return
     *     possible object is
     *     {@link AdditionalData }
     *     
     */
    public AdditionalData getAdditionalData() {
        return additionalData;
    }

    /**
     * Sets the value of the additionalData property.
     * 
     * @param value
     *     allowed object is
     *     {@link AdditionalData }
     *     
     */
    public void setAdditionalData(AdditionalData value) {
        this.additionalData = value;
    }

}
