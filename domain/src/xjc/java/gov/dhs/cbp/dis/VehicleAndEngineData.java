
package gov.dhs.cbp.dis;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlSchemaType;
import javax.xml.bind.annotation.XmlType;
import javax.xml.datatype.XMLGregorianCalendar;


/**
 * VehicleAndEngineData
 * 
 * <p>Java class for VehicleAndEngineData complex type.
 * 
 * <p>The following schema fragment specifies the expected content contained within this class.
 * 
 * <pre>
 * &lt;complexType name="VehicleAndEngineData"&gt;
 *   &lt;complexContent&gt;
 *     &lt;restriction base="{http://www.w3.org/2001/XMLSchema}anyType"&gt;
 *       &lt;sequence&gt;
 *         &lt;element name="VIN" type="{http://www.w3.org/2001/XMLSchema}string" minOccurs="0"/&gt;
 *         &lt;element name="VehicleManufacturer" type="{http://www.w3.org/2001/XMLSchema}string" minOccurs="0"/&gt;
 *         &lt;element name="VehicleModel" type="{http://www.w3.org/2001/XMLSchema}string" minOccurs="0"/&gt;
 *         &lt;element name="VehicleSerialNumber" type="{http://www.w3.org/2001/XMLSchema}string" minOccurs="0"/&gt;
 *         &lt;element name="VehicleManufactureDate" type="{http://www.w3.org/2001/XMLSchema}dateTime" minOccurs="0"/&gt;
 *         &lt;element name="EngineManufacturer" type="{http://www.w3.org/2001/XMLSchema}string" minOccurs="0"/&gt;
 *         &lt;element name="EngineModel" type="{http://www.w3.org/2001/XMLSchema}string" minOccurs="0"/&gt;
 *         &lt;element name="EngineSerialNumber" type="{http://www.w3.org/2001/XMLSchema}string" minOccurs="0"/&gt;
 *         &lt;element name="EngineManufactureDate" type="{http://www.w3.org/2001/XMLSchema}dateTime" minOccurs="0"/&gt;
 *       &lt;/sequence&gt;
 *     &lt;/restriction&gt;
 *   &lt;/complexContent&gt;
 * &lt;/complexType&gt;
 * </pre>
 * 
 * 
 */
@XmlAccessorType(XmlAccessType.FIELD)
@XmlType(name = "VehicleAndEngineData", propOrder = {
    "vin",
    "vehicleManufacturer",
    "vehicleModel",
    "vehicleSerialNumber",
    "vehicleManufactureDate",
    "engineManufacturer",
    "engineModel",
    "engineSerialNumber",
    "engineManufactureDate"
})
public class VehicleAndEngineData {

    @XmlElement(name = "VIN")
    protected String vin;
    @XmlElement(name = "VehicleManufacturer")
    protected String vehicleManufacturer;
    @XmlElement(name = "VehicleModel")
    protected String vehicleModel;
    @XmlElement(name = "VehicleSerialNumber")
    protected String vehicleSerialNumber;
    @XmlElement(name = "VehicleManufactureDate")
    @XmlSchemaType(name = "dateTime")
    protected XMLGregorianCalendar vehicleManufactureDate;
    @XmlElement(name = "EngineManufacturer")
    protected String engineManufacturer;
    @XmlElement(name = "EngineModel")
    protected String engineModel;
    @XmlElement(name = "EngineSerialNumber")
    protected String engineSerialNumber;
    @XmlElement(name = "EngineManufactureDate")
    @XmlSchemaType(name = "dateTime")
    protected XMLGregorianCalendar engineManufactureDate;

    /**
     * Gets the value of the vin property.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getVIN() {
        return vin;
    }

    /**
     * Sets the value of the vin property.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setVIN(String value) {
        this.vin = value;
    }

    /**
     * Gets the value of the vehicleManufacturer property.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getVehicleManufacturer() {
        return vehicleManufacturer;
    }

    /**
     * Sets the value of the vehicleManufacturer property.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setVehicleManufacturer(String value) {
        this.vehicleManufacturer = value;
    }

    /**
     * Gets the value of the vehicleModel property.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getVehicleModel() {
        return vehicleModel;
    }

    /**
     * Sets the value of the vehicleModel property.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setVehicleModel(String value) {
        this.vehicleModel = value;
    }

    /**
     * Gets the value of the vehicleSerialNumber property.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getVehicleSerialNumber() {
        return vehicleSerialNumber;
    }

    /**
     * Sets the value of the vehicleSerialNumber property.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setVehicleSerialNumber(String value) {
        this.vehicleSerialNumber = value;
    }

    /**
     * Gets the value of the vehicleManufactureDate property.
     * 
     * @return
     *     possible object is
     *     {@link XMLGregorianCalendar }
     *     
     */
    public XMLGregorianCalendar getVehicleManufactureDate() {
        return vehicleManufactureDate;
    }

    /**
     * Sets the value of the vehicleManufactureDate property.
     * 
     * @param value
     *     allowed object is
     *     {@link XMLGregorianCalendar }
     *     
     */
    public void setVehicleManufactureDate(XMLGregorianCalendar value) {
        this.vehicleManufactureDate = value;
    }

    /**
     * Gets the value of the engineManufacturer property.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getEngineManufacturer() {
        return engineManufacturer;
    }

    /**
     * Sets the value of the engineManufacturer property.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setEngineManufacturer(String value) {
        this.engineManufacturer = value;
    }

    /**
     * Gets the value of the engineModel property.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getEngineModel() {
        return engineModel;
    }

    /**
     * Sets the value of the engineModel property.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setEngineModel(String value) {
        this.engineModel = value;
    }

    /**
     * Gets the value of the engineSerialNumber property.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getEngineSerialNumber() {
        return engineSerialNumber;
    }

    /**
     * Sets the value of the engineSerialNumber property.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setEngineSerialNumber(String value) {
        this.engineSerialNumber = value;
    }

    /**
     * Gets the value of the engineManufactureDate property.
     * 
     * @return
     *     possible object is
     *     {@link XMLGregorianCalendar }
     *     
     */
    public XMLGregorianCalendar getEngineManufactureDate() {
        return engineManufactureDate;
    }

    /**
     * Sets the value of the engineManufactureDate property.
     * 
     * @param value
     *     allowed object is
     *     {@link XMLGregorianCalendar }
     *     
     */
    public void setEngineManufactureDate(XMLGregorianCalendar value) {
        this.engineManufactureDate = value;
    }

}
