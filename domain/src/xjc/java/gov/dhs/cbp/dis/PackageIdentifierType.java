
package gov.dhs.cbp.dis;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlType;


/**
 * Can be used to provide CBMA IOR Number
 * 
 * <p>Java class for PackageIdentifierType complex type.
 * 
 * <p>The following schema fragment specifies the expected content contained within this class.
 * 
 * <pre>
 * &lt;complexType name="PackageIdentifierType"&gt;
 *   &lt;complexContent&gt;
 *     &lt;restriction base="{http://www.w3.org/2001/XMLSchema}anyType"&gt;
 *       &lt;sequence&gt;
 *         &lt;element name="PackageCategory" type="{http://www.w3.org/2001/XMLSchema}string" minOccurs="0"/&gt;
 *         &lt;element name="PackageName" type="{http://www.w3.org/2001/XMLSchema}anyType" minOccurs="0"/&gt;
 *         &lt;element name="ImporterOfRecordNbr" type="{http://www.w3.org/2001/XMLSchema}string"/&gt;
 *       &lt;/sequence&gt;
 *     &lt;/restriction&gt;
 *   &lt;/complexContent&gt;
 * &lt;/complexType&gt;
 * </pre>
 * 
 * 
 */
@XmlAccessorType(XmlAccessType.FIELD)
@XmlType(name = "PackageIdentifierType", propOrder = {
    "packageCategory",
    "packageName",
    "importerOfRecordNbr"
})
public class PackageIdentifierType {

    @XmlElement(name = "PackageCategory")
    protected String packageCategory;
    @XmlElement(name = "PackageName")
    protected Object packageName;
    @XmlElement(name = "ImporterOfRecordNbr", required = true)
    protected String importerOfRecordNbr;

    /**
     * Gets the value of the packageCategory property.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getPackageCategory() {
        return packageCategory;
    }

    /**
     * Sets the value of the packageCategory property.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setPackageCategory(String value) {
        this.packageCategory = value;
    }

    /**
     * Gets the value of the packageName property.
     * 
     * @return
     *     possible object is
     *     {@link Object }
     *     
     */
    public Object getPackageName() {
        return packageName;
    }

    /**
     * Sets the value of the packageName property.
     * 
     * @param value
     *     allowed object is
     *     {@link Object }
     *     
     */
    public void setPackageName(Object value) {
        this.packageName = value;
    }

    /**
     * Gets the value of the importerOfRecordNbr property.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getImporterOfRecordNbr() {
        return importerOfRecordNbr;
    }

    /**
     * Sets the value of the importerOfRecordNbr property.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setImporterOfRecordNbr(String value) {
        this.importerOfRecordNbr = value;
    }

}
