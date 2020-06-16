
package gov.dhs.cbp.dis;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlSchemaType;
import javax.xml.bind.annotation.XmlType;
import javax.xml.datatype.XMLGregorianCalendar;


/**
 * Commodity Information. Provide what is available
 * 
 * <p>Java class for CommodityData complex type.
 * 
 * <p>The following schema fragment specifies the expected content contained within this class.
 * 
 * <pre>
 * &lt;complexType name="CommodityData"&gt;
 *   &lt;complexContent&gt;
 *     &lt;restriction base="{http://www.w3.org/2001/XMLSchema}anyType"&gt;
 *       &lt;sequence&gt;
 *         &lt;element name="EntryLineNumber" type="{http://www.w3.org/2001/XMLSchema}int" minOccurs="0"/&gt;
 *         &lt;element name="HTSNumber" type="{http://www.w3.org/2001/XMLSchema}string" minOccurs="0"/&gt;
 *         &lt;element name="CommodityDescription" type="{http://www.w3.org/2001/XMLSchema}string" minOccurs="0"/&gt;
 *         &lt;element name="CountryOfOrigin" type="{http://www.w3.org/2001/XMLSchema}string" minOccurs="0"/&gt;
 *         &lt;element name="ContainerNbr" type="{http://www.w3.org/2001/XMLSchema}string" minOccurs="0"/&gt;
 *         &lt;element name="ArrivalDate" type="{http://www.w3.org/2001/XMLSchema}dateTime" minOccurs="0"/&gt;
 *         &lt;element name="PortOfLading" type="{http://www.w3.org/2001/XMLSchema}string" minOccurs="0"/&gt;
 *         &lt;element name="PortOfUnlading" type="{http://www.w3.org/2001/XMLSchema}string" minOccurs="0"/&gt;
 *         &lt;element name="PortOfEntry" type="{http://www.w3.org/2001/XMLSchema}string" minOccurs="0"/&gt;
 *         &lt;element name="PPQSignature" type="{http://www.w3.org/2001/XMLSchema}string" minOccurs="0"/&gt;
 *         &lt;element name="SealNumbers" type="{http://www.w3.org/2001/XMLSchema}string" minOccurs="0"/&gt;
 *         &lt;element name="TradeParties" type="{http://cbp.dhs.gov/DIS}TradeParties" minOccurs="0"/&gt;
 *         &lt;element name="FilerReferenceNumber" type="{http://www.w3.org/2001/XMLSchema}string" minOccurs="0"/&gt;
 *         &lt;element name="VehicleAndEngineData" type="{http://cbp.dhs.gov/DIS}VehicleAndEngineData" minOccurs="0"/&gt;
 *       &lt;/sequence&gt;
 *     &lt;/restriction&gt;
 *   &lt;/complexContent&gt;
 * &lt;/complexType&gt;
 * </pre>
 * 
 * 
 */
@XmlAccessorType(XmlAccessType.FIELD)
@XmlType(name = "CommodityData", propOrder = {
    "entryLineNumber",
    "htsNumber",
    "commodityDescription",
    "countryOfOrigin",
    "containerNbr",
    "arrivalDate",
    "portOfLading",
    "portOfUnlading",
    "portOfEntry",
    "ppqSignature",
    "sealNumbers",
    "tradeParties",
    "filerReferenceNumber",
    "vehicleAndEngineData"
})
public class CommodityData {

    @XmlElement(name = "EntryLineNumber")
    protected Integer entryLineNumber;
    @XmlElement(name = "HTSNumber")
    protected String htsNumber;
    @XmlElement(name = "CommodityDescription")
    protected String commodityDescription;
    @XmlElement(name = "CountryOfOrigin")
    protected String countryOfOrigin;
    @XmlElement(name = "ContainerNbr")
    protected String containerNbr;
    @XmlElement(name = "ArrivalDate")
    @XmlSchemaType(name = "dateTime")
    protected XMLGregorianCalendar arrivalDate;
    @XmlElement(name = "PortOfLading")
    protected String portOfLading;
    @XmlElement(name = "PortOfUnlading")
    protected String portOfUnlading;
    @XmlElement(name = "PortOfEntry")
    protected String portOfEntry;
    @XmlElement(name = "PPQSignature")
    protected String ppqSignature;
    @XmlElement(name = "SealNumbers")
    protected String sealNumbers;
    @XmlElement(name = "TradeParties")
    protected TradeParties tradeParties;
    @XmlElement(name = "FilerReferenceNumber")
    protected String filerReferenceNumber;
    @XmlElement(name = "VehicleAndEngineData")
    protected VehicleAndEngineData vehicleAndEngineData;

    /**
     * Gets the value of the entryLineNumber property.
     * 
     * @return
     *     possible object is
     *     {@link Integer }
     *     
     */
    public Integer getEntryLineNumber() {
        return entryLineNumber;
    }

    /**
     * Sets the value of the entryLineNumber property.
     * 
     * @param value
     *     allowed object is
     *     {@link Integer }
     *     
     */
    public void setEntryLineNumber(Integer value) {
        this.entryLineNumber = value;
    }

    /**
     * Gets the value of the htsNumber property.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getHTSNumber() {
        return htsNumber;
    }

    /**
     * Sets the value of the htsNumber property.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setHTSNumber(String value) {
        this.htsNumber = value;
    }

    /**
     * Gets the value of the commodityDescription property.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getCommodityDescription() {
        return commodityDescription;
    }

    /**
     * Sets the value of the commodityDescription property.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setCommodityDescription(String value) {
        this.commodityDescription = value;
    }

    /**
     * Gets the value of the countryOfOrigin property.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getCountryOfOrigin() {
        return countryOfOrigin;
    }

    /**
     * Sets the value of the countryOfOrigin property.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setCountryOfOrigin(String value) {
        this.countryOfOrigin = value;
    }

    /**
     * Gets the value of the containerNbr property.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getContainerNbr() {
        return containerNbr;
    }

    /**
     * Sets the value of the containerNbr property.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setContainerNbr(String value) {
        this.containerNbr = value;
    }

    /**
     * Gets the value of the arrivalDate property.
     * 
     * @return
     *     possible object is
     *     {@link XMLGregorianCalendar }
     *     
     */
    public XMLGregorianCalendar getArrivalDate() {
        return arrivalDate;
    }

    /**
     * Sets the value of the arrivalDate property.
     * 
     * @param value
     *     allowed object is
     *     {@link XMLGregorianCalendar }
     *     
     */
    public void setArrivalDate(XMLGregorianCalendar value) {
        this.arrivalDate = value;
    }

    /**
     * Gets the value of the portOfLading property.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getPortOfLading() {
        return portOfLading;
    }

    /**
     * Sets the value of the portOfLading property.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setPortOfLading(String value) {
        this.portOfLading = value;
    }

    /**
     * Gets the value of the portOfUnlading property.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getPortOfUnlading() {
        return portOfUnlading;
    }

    /**
     * Sets the value of the portOfUnlading property.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setPortOfUnlading(String value) {
        this.portOfUnlading = value;
    }

    /**
     * Gets the value of the portOfEntry property.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getPortOfEntry() {
        return portOfEntry;
    }

    /**
     * Sets the value of the portOfEntry property.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setPortOfEntry(String value) {
        this.portOfEntry = value;
    }

    /**
     * Gets the value of the ppqSignature property.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getPPQSignature() {
        return ppqSignature;
    }

    /**
     * Sets the value of the ppqSignature property.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setPPQSignature(String value) {
        this.ppqSignature = value;
    }

    /**
     * Gets the value of the sealNumbers property.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getSealNumbers() {
        return sealNumbers;
    }

    /**
     * Sets the value of the sealNumbers property.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setSealNumbers(String value) {
        this.sealNumbers = value;
    }

    /**
     * Gets the value of the tradeParties property.
     * 
     * @return
     *     possible object is
     *     {@link TradeParties }
     *     
     */
    public TradeParties getTradeParties() {
        return tradeParties;
    }

    /**
     * Sets the value of the tradeParties property.
     * 
     * @param value
     *     allowed object is
     *     {@link TradeParties }
     *     
     */
    public void setTradeParties(TradeParties value) {
        this.tradeParties = value;
    }

    /**
     * Gets the value of the filerReferenceNumber property.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getFilerReferenceNumber() {
        return filerReferenceNumber;
    }

    /**
     * Sets the value of the filerReferenceNumber property.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setFilerReferenceNumber(String value) {
        this.filerReferenceNumber = value;
    }

    /**
     * Gets the value of the vehicleAndEngineData property.
     * 
     * @return
     *     possible object is
     *     {@link VehicleAndEngineData }
     *     
     */
    public VehicleAndEngineData getVehicleAndEngineData() {
        return vehicleAndEngineData;
    }

    /**
     * Sets the value of the vehicleAndEngineData property.
     * 
     * @param value
     *     allowed object is
     *     {@link VehicleAndEngineData }
     *     
     */
    public void setVehicleAndEngineData(VehicleAndEngineData value) {
        this.vehicleAndEngineData = value;
    }

}
