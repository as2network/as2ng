
package gov.dhs.cbp.dis;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlSchemaType;
import javax.xml.bind.annotation.XmlType;
import javax.xml.datatype.XMLGregorianCalendar;


/**
 * Elements that are used to identify/locate a unique document submission.
 * 
 * <p>Java class for DocumentHeader complex type.
 * 
 * <p>The following schema fragment specifies the expected content contained within this class.
 * 
 * <pre>
 * &lt;complexType name="DocumentHeader"&gt;
 *   &lt;complexContent&gt;
 *     &lt;restriction base="{http://www.w3.org/2001/XMLSchema}anyType"&gt;
 *       &lt;sequence&gt;
 *         &lt;element name="DocumentID" type="{http://www.w3.org/2001/XMLSchema}string"/&gt;
 *         &lt;element name="DocumentLabel" type="{http://www.w3.org/2001/XMLSchema}string"/&gt;
 *         &lt;element name="CompleteFileName" type="{http://www.w3.org/2001/XMLSchema}string"/&gt;
 *         &lt;element name="FileExtensionType" type="{http://www.w3.org/2001/XMLSchema}string"/&gt;
 *         &lt;element name="DocumentDescription" type="{http://www.w3.org/2001/XMLSchema}string" minOccurs="0"/&gt;
 *         &lt;element name="DocumentSentDate" type="{http://www.w3.org/2001/XMLSchema}dateTime" minOccurs="0"/&gt;
 *         &lt;element name="DocPreviouslySubmitted" type="{http://www.w3.org/2001/XMLSchema}string" minOccurs="0"/&gt;
 *       &lt;/sequence&gt;
 *     &lt;/restriction&gt;
 *   &lt;/complexContent&gt;
 * &lt;/complexType&gt;
 * </pre>
 * 
 * 
 */
@XmlAccessorType(XmlAccessType.FIELD)
@XmlType(name = "DocumentHeader", propOrder = {
    "documentID",
    "documentLabel",
    "completeFileName",
    "fileExtensionType",
    "documentDescription",
    "documentSentDate",
    "docPreviouslySubmitted"
})
public class DocumentHeader {

    @XmlElement(name = "DocumentID", required = true)
    protected String documentID;
    @XmlElement(name = "DocumentLabel", required = true)
    protected String documentLabel;
    @XmlElement(name = "CompleteFileName", required = true)
    protected String completeFileName;
    @XmlElement(name = "FileExtensionType", required = true)
    protected String fileExtensionType;
    @XmlElement(name = "DocumentDescription")
    protected String documentDescription;
    @XmlElement(name = "DocumentSentDate")
    @XmlSchemaType(name = "dateTime")
    protected XMLGregorianCalendar documentSentDate;
    @XmlElement(name = "DocPreviouslySubmitted")
    protected String docPreviouslySubmitted;

    /**
     * Gets the value of the documentID property.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getDocumentID() {
        return documentID;
    }

    /**
     * Sets the value of the documentID property.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setDocumentID(String value) {
        this.documentID = value;
    }

    /**
     * Gets the value of the documentLabel property.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getDocumentLabel() {
        return documentLabel;
    }

    /**
     * Sets the value of the documentLabel property.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setDocumentLabel(String value) {
        this.documentLabel = value;
    }

    /**
     * Gets the value of the completeFileName property.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getCompleteFileName() {
        return completeFileName;
    }

    /**
     * Sets the value of the completeFileName property.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setCompleteFileName(String value) {
        this.completeFileName = value;
    }

    /**
     * Gets the value of the fileExtensionType property.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getFileExtensionType() {
        return fileExtensionType;
    }

    /**
     * Sets the value of the fileExtensionType property.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setFileExtensionType(String value) {
        this.fileExtensionType = value;
    }

    /**
     * Gets the value of the documentDescription property.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getDocumentDescription() {
        return documentDescription;
    }

    /**
     * Sets the value of the documentDescription property.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setDocumentDescription(String value) {
        this.documentDescription = value;
    }

    /**
     * Gets the value of the documentSentDate property.
     * 
     * @return
     *     possible object is
     *     {@link XMLGregorianCalendar }
     *     
     */
    public XMLGregorianCalendar getDocumentSentDate() {
        return documentSentDate;
    }

    /**
     * Sets the value of the documentSentDate property.
     * 
     * @param value
     *     allowed object is
     *     {@link XMLGregorianCalendar }
     *     
     */
    public void setDocumentSentDate(XMLGregorianCalendar value) {
        this.documentSentDate = value;
    }

    /**
     * Gets the value of the docPreviouslySubmitted property.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getDocPreviouslySubmitted() {
        return docPreviouslySubmitted;
    }

    /**
     * Sets the value of the docPreviouslySubmitted property.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setDocPreviouslySubmitted(String value) {
        this.docPreviouslySubmitted = value;
    }

}
