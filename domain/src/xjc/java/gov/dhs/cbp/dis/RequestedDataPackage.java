
package gov.dhs.cbp.dis;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlType;


/**
 * <p>Java class for RequestedDataPackage complex type.
 * 
 * <p>The following schema fragment specifies the expected content contained within this class.
 * 
 * <pre>
 * &lt;complexType name="RequestedDataPackage"&gt;
 *   &lt;complexContent&gt;
 *     &lt;restriction base="{http://www.w3.org/2001/XMLSchema}anyType"&gt;
 *       &lt;sequence&gt;
 *         &lt;element name="DataRequestPackage" type="{http://cbp.dhs.gov/DIS}DataRequestPackage"/&gt;
 *         &lt;element name="RequestedData" type="{http://cbp.dhs.gov/DIS}RequestedData"/&gt;
 *       &lt;/sequence&gt;
 *     &lt;/restriction&gt;
 *   &lt;/complexContent&gt;
 * &lt;/complexType&gt;
 * </pre>
 * 
 * 
 */
@XmlAccessorType(XmlAccessType.FIELD)
@XmlType(name = "RequestedDataPackage", propOrder = {
    "dataRequestPackage",
    "requestedData"
})
public class RequestedDataPackage {

    @XmlElement(name = "DataRequestPackage", required = true)
    protected DataRequestPackage dataRequestPackage;
    @XmlElement(name = "RequestedData", required = true)
    protected RequestedData requestedData;

    /**
     * Gets the value of the dataRequestPackage property.
     * 
     * @return
     *     possible object is
     *     {@link DataRequestPackage }
     *     
     */
    public DataRequestPackage getDataRequestPackage() {
        return dataRequestPackage;
    }

    /**
     * Sets the value of the dataRequestPackage property.
     * 
     * @param value
     *     allowed object is
     *     {@link DataRequestPackage }
     *     
     */
    public void setDataRequestPackage(DataRequestPackage value) {
        this.dataRequestPackage = value;
    }

    /**
     * Gets the value of the requestedData property.
     * 
     * @return
     *     possible object is
     *     {@link RequestedData }
     *     
     */
    public RequestedData getRequestedData() {
        return requestedData;
    }

    /**
     * Sets the value of the requestedData property.
     * 
     * @param value
     *     allowed object is
     *     {@link RequestedData }
     *     
     */
    public void setRequestedData(RequestedData value) {
        this.requestedData = value;
    }

}
