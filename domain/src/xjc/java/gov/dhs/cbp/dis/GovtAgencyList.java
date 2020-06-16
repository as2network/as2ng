
package gov.dhs.cbp.dis;

import java.util.ArrayList;
import java.util.List;
import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlType;


/**
 * <p>Java class for GovtAgencyList complex type.
 * 
 * <p>The following schema fragment specifies the expected content contained within this class.
 * 
 * <pre>
 * &lt;complexType name="GovtAgencyList"&gt;
 *   &lt;complexContent&gt;
 *     &lt;restriction base="{http://www.w3.org/2001/XMLSchema}anyType"&gt;
 *       &lt;sequence&gt;
 *         &lt;element name="GovtAgency" type="{http://www.w3.org/2001/XMLSchema}string" maxOccurs="50"/&gt;
 *       &lt;/sequence&gt;
 *     &lt;/restriction&gt;
 *   &lt;/complexContent&gt;
 * &lt;/complexType&gt;
 * </pre>
 * 
 * 
 */
@XmlAccessorType(XmlAccessType.FIELD)
@XmlType(name = "GovtAgencyList", propOrder = {
    "govtAgency"
})
public class GovtAgencyList {

    @XmlElement(name = "GovtAgency", required = true)
    protected List<String> govtAgency;

    /**
     * Gets the value of the govtAgency property.
     * 
     * <p>
     * This accessor method returns a reference to the live list,
     * not a snapshot. Therefore any modification you make to the
     * returned list will be present inside the JAXB object.
     * This is why there is not a <CODE>set</CODE> method for the govtAgency property.
     * 
     * <p>
     * For example, to add a new item, do as follows:
     * <pre>
     *    getGovtAgency().add(newItem);
     * </pre>
     * 
     * 
     * <p>
     * Objects of the following type(s) are allowed in the list
     * {@link String }
     * 
     * 
     */
    public List<String> getGovtAgency() {
        if (govtAgency == null) {
            govtAgency = new ArrayList<String>();
        }
        return this.govtAgency;
    }

}
