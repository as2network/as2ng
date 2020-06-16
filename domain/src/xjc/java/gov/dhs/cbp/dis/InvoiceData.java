
package gov.dhs.cbp.dis;

import java.util.ArrayList;
import java.util.List;
import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlType;


/**
 * Invoice Specific Information
 * 
 * <p>Java class for InvoiceData complex type.
 * 
 * <p>The following schema fragment specifies the expected content contained within this class.
 * 
 * <pre>
 * &lt;complexType name="InvoiceData"&gt;
 *   &lt;complexContent&gt;
 *     &lt;restriction base="{http://www.w3.org/2001/XMLSchema}anyType"&gt;
 *       &lt;sequence&gt;
 *         &lt;element name="InvoiceNbr" type="{http://www.w3.org/2001/XMLSchema}string"/&gt;
 *         &lt;element name="InvoiceType" type="{http://www.w3.org/2001/XMLSchema}string"/&gt;
 *         &lt;element name="InvoiceLineItemData" type="{http://cbp.dhs.gov/DIS}InvoiceLineItemData" maxOccurs="unbounded" minOccurs="0"/&gt;
 *       &lt;/sequence&gt;
 *     &lt;/restriction&gt;
 *   &lt;/complexContent&gt;
 * &lt;/complexType&gt;
 * </pre>
 * 
 * 
 */
@XmlAccessorType(XmlAccessType.FIELD)
@XmlType(name = "InvoiceData", propOrder = {
    "invoiceNbr",
    "invoiceType",
    "invoiceLineItemData"
})
public class InvoiceData {

    @XmlElement(name = "InvoiceNbr", required = true)
    protected String invoiceNbr;
    @XmlElement(name = "InvoiceType", required = true)
    protected String invoiceType;
    @XmlElement(name = "InvoiceLineItemData")
    protected List<InvoiceLineItemData> invoiceLineItemData;

    /**
     * Gets the value of the invoiceNbr property.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getInvoiceNbr() {
        return invoiceNbr;
    }

    /**
     * Sets the value of the invoiceNbr property.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setInvoiceNbr(String value) {
        this.invoiceNbr = value;
    }

    /**
     * Gets the value of the invoiceType property.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getInvoiceType() {
        return invoiceType;
    }

    /**
     * Sets the value of the invoiceType property.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setInvoiceType(String value) {
        this.invoiceType = value;
    }

    /**
     * Gets the value of the invoiceLineItemData property.
     * 
     * <p>
     * This accessor method returns a reference to the live list,
     * not a snapshot. Therefore any modification you make to the
     * returned list will be present inside the JAXB object.
     * This is why there is not a <CODE>set</CODE> method for the invoiceLineItemData property.
     * 
     * <p>
     * For example, to add a new item, do as follows:
     * <pre>
     *    getInvoiceLineItemData().add(newItem);
     * </pre>
     * 
     * 
     * <p>
     * Objects of the following type(s) are allowed in the list
     * {@link InvoiceLineItemData }
     * 
     * 
     */
    public List<InvoiceLineItemData> getInvoiceLineItemData() {
        if (invoiceLineItemData == null) {
            invoiceLineItemData = new ArrayList<InvoiceLineItemData>();
        }
        return this.invoiceLineItemData;
    }

}
