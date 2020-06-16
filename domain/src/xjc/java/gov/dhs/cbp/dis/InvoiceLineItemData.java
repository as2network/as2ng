
package gov.dhs.cbp.dis;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlType;


/**
 * Invoice Line Item Level Information
 * 
 * <p>Java class for InvoiceLineItemData complex type.
 * 
 * <p>The following schema fragment specifies the expected content contained within this class.
 * 
 * <pre>
 * &lt;complexType name="InvoiceLineItemData"&gt;
 *   &lt;complexContent&gt;
 *     &lt;restriction base="{http://www.w3.org/2001/XMLSchema}anyType"&gt;
 *       &lt;sequence&gt;
 *         &lt;element name="InvoiceLineNbr" type="{http://www.w3.org/2001/XMLSchema}string"/&gt;
 *         &lt;element name="CommodityData" type="{http://cbp.dhs.gov/DIS}CommodityData"/&gt;
 *       &lt;/sequence&gt;
 *     &lt;/restriction&gt;
 *   &lt;/complexContent&gt;
 * &lt;/complexType&gt;
 * </pre>
 * 
 * 
 */
@XmlAccessorType(XmlAccessType.FIELD)
@XmlType(name = "InvoiceLineItemData", propOrder = {
    "invoiceLineNbr",
    "commodityData"
})
public class InvoiceLineItemData {

    @XmlElement(name = "InvoiceLineNbr", required = true)
    protected String invoiceLineNbr;
    @XmlElement(name = "CommodityData", required = true)
    protected CommodityData commodityData;

    /**
     * Gets the value of the invoiceLineNbr property.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getInvoiceLineNbr() {
        return invoiceLineNbr;
    }

    /**
     * Sets the value of the invoiceLineNbr property.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setInvoiceLineNbr(String value) {
        this.invoiceLineNbr = value;
    }

    /**
     * Gets the value of the commodityData property.
     * 
     * @return
     *     possible object is
     *     {@link CommodityData }
     *     
     */
    public CommodityData getCommodityData() {
        return commodityData;
    }

    /**
     * Sets the value of the commodityData property.
     * 
     * @param value
     *     allowed object is
     *     {@link CommodityData }
     *     
     */
    public void setCommodityData(CommodityData value) {
        this.commodityData = value;
    }

}
