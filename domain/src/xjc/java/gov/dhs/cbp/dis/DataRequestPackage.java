
package gov.dhs.cbp.dis;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlType;


/**
 * To be used when Trade asks CBP for Data
 * 
 * <p>Java class for DataRequestPackage complex type.
 * 
 * <p>The following schema fragment specifies the expected content contained within this class.
 * 
 * <pre>
 * &lt;complexType name="DataRequestPackage"&gt;
 *   &lt;complexContent&gt;
 *     &lt;restriction base="{http://www.w3.org/2001/XMLSchema}anyType"&gt;
 *       &lt;choice&gt;
 *         &lt;element name="RequestDocInfoByDocumentID" type="{http://cbp.dhs.gov/DIS}RequestDocumentInfoByDocumentID" minOccurs="0"/&gt;
 *         &lt;element name="RequestDocInfoByDateRange" type="{http://cbp.dhs.gov/DIS}RequestDocumentInfoByDateRange" minOccurs="0"/&gt;
 *         &lt;element name="RequestDocInfoByCBPRequest" type="{http://cbp.dhs.gov/DIS}RequestDocumentInfoByCBPRequestID" minOccurs="0"/&gt;
 *         &lt;element name="RequestDocInfoByTradeTxn" type="{http://cbp.dhs.gov/DIS}RequestDocumentInfoByTradeTxnID" minOccurs="0"/&gt;
 *       &lt;/choice&gt;
 *     &lt;/restriction&gt;
 *   &lt;/complexContent&gt;
 * &lt;/complexType&gt;
 * </pre>
 * 
 * 
 */
@XmlAccessorType(XmlAccessType.FIELD)
@XmlType(name = "DataRequestPackage", propOrder = {
    "requestDocInfoByDocumentID",
    "requestDocInfoByDateRange",
    "requestDocInfoByCBPRequest",
    "requestDocInfoByTradeTxn"
})
public class DataRequestPackage {

    @XmlElement(name = "RequestDocInfoByDocumentID")
    protected RequestDocumentInfoByDocumentID requestDocInfoByDocumentID;
    @XmlElement(name = "RequestDocInfoByDateRange")
    protected RequestDocumentInfoByDateRange requestDocInfoByDateRange;
    @XmlElement(name = "RequestDocInfoByCBPRequest")
    protected RequestDocumentInfoByCBPRequestID requestDocInfoByCBPRequest;
    @XmlElement(name = "RequestDocInfoByTradeTxn")
    protected RequestDocumentInfoByTradeTxnID requestDocInfoByTradeTxn;

    /**
     * Gets the value of the requestDocInfoByDocumentID property.
     * 
     * @return
     *     possible object is
     *     {@link RequestDocumentInfoByDocumentID }
     *     
     */
    public RequestDocumentInfoByDocumentID getRequestDocInfoByDocumentID() {
        return requestDocInfoByDocumentID;
    }

    /**
     * Sets the value of the requestDocInfoByDocumentID property.
     * 
     * @param value
     *     allowed object is
     *     {@link RequestDocumentInfoByDocumentID }
     *     
     */
    public void setRequestDocInfoByDocumentID(RequestDocumentInfoByDocumentID value) {
        this.requestDocInfoByDocumentID = value;
    }

    /**
     * Gets the value of the requestDocInfoByDateRange property.
     * 
     * @return
     *     possible object is
     *     {@link RequestDocumentInfoByDateRange }
     *     
     */
    public RequestDocumentInfoByDateRange getRequestDocInfoByDateRange() {
        return requestDocInfoByDateRange;
    }

    /**
     * Sets the value of the requestDocInfoByDateRange property.
     * 
     * @param value
     *     allowed object is
     *     {@link RequestDocumentInfoByDateRange }
     *     
     */
    public void setRequestDocInfoByDateRange(RequestDocumentInfoByDateRange value) {
        this.requestDocInfoByDateRange = value;
    }

    /**
     * Gets the value of the requestDocInfoByCBPRequest property.
     * 
     * @return
     *     possible object is
     *     {@link RequestDocumentInfoByCBPRequestID }
     *     
     */
    public RequestDocumentInfoByCBPRequestID getRequestDocInfoByCBPRequest() {
        return requestDocInfoByCBPRequest;
    }

    /**
     * Sets the value of the requestDocInfoByCBPRequest property.
     * 
     * @param value
     *     allowed object is
     *     {@link RequestDocumentInfoByCBPRequestID }
     *     
     */
    public void setRequestDocInfoByCBPRequest(RequestDocumentInfoByCBPRequestID value) {
        this.requestDocInfoByCBPRequest = value;
    }

    /**
     * Gets the value of the requestDocInfoByTradeTxn property.
     * 
     * @return
     *     possible object is
     *     {@link RequestDocumentInfoByTradeTxnID }
     *     
     */
    public RequestDocumentInfoByTradeTxnID getRequestDocInfoByTradeTxn() {
        return requestDocInfoByTradeTxn;
    }

    /**
     * Sets the value of the requestDocInfoByTradeTxn property.
     * 
     * @param value
     *     allowed object is
     *     {@link RequestDocumentInfoByTradeTxnID }
     *     
     */
    public void setRequestDocInfoByTradeTxn(RequestDocumentInfoByTradeTxnID value) {
        this.requestDocInfoByTradeTxn = value;
    }

}
