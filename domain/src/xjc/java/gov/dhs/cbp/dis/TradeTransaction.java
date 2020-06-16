
package gov.dhs.cbp.dis;

import java.util.ArrayList;
import java.util.List;
import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlType;


/**
 * <p>Java class for TradeTransaction complex type.
 * 
 * <p>The following schema fragment specifies the expected content contained within this class.
 * 
 * <pre>
 * &lt;complexType name="TradeTransaction"&gt;
 *   &lt;complexContent&gt;
 *     &lt;restriction base="{http://www.w3.org/2001/XMLSchema}anyType"&gt;
 *       &lt;sequence&gt;
 *         &lt;element name="TransactionCategory" type="{http://www.w3.org/2001/XMLSchema}string"/&gt;
 *         &lt;element name="Entry" maxOccurs="unbounded" minOccurs="0"&gt;
 *           &lt;complexType&gt;
 *             &lt;complexContent&gt;
 *               &lt;restriction base="{http://www.w3.org/2001/XMLSchema}anyType"&gt;
 *                 &lt;sequence&gt;
 *                   &lt;element name="EntryNumber" type="{http://www.w3.org/2001/XMLSchema}string"/&gt;
 *                   &lt;element name="Filer" type="{http://www.w3.org/2001/XMLSchema}string"/&gt;
 *                   &lt;element name="EntryLineNumber" type="{http://www.w3.org/2001/XMLSchema}string" minOccurs="0"/&gt;
 *                   &lt;element name="ReferenceNumber" type="{http://www.w3.org/2001/XMLSchema}string" minOccurs="0"/&gt;
 *                 &lt;/sequence&gt;
 *               &lt;/restriction&gt;
 *             &lt;/complexContent&gt;
 *           &lt;/complexType&gt;
 *         &lt;/element&gt;
 *         &lt;element name="EntrySummary" maxOccurs="unbounded" minOccurs="0"&gt;
 *           &lt;complexType&gt;
 *             &lt;complexContent&gt;
 *               &lt;restriction base="{http://www.w3.org/2001/XMLSchema}anyType"&gt;
 *                 &lt;sequence&gt;
 *                   &lt;element name="EntryNumber" type="{http://www.w3.org/2001/XMLSchema}string"/&gt;
 *                   &lt;element name="Filer" type="{http://www.w3.org/2001/XMLSchema}string"/&gt;
 *                   &lt;element name="EntryLineNumber" type="{http://www.w3.org/2001/XMLSchema}string" minOccurs="0"/&gt;
 *                   &lt;element name="ReferenceNumber" type="{http://www.w3.org/2001/XMLSchema}string" minOccurs="0"/&gt;
 *                 &lt;/sequence&gt;
 *               &lt;/restriction&gt;
 *             &lt;/complexContent&gt;
 *           &lt;/complexType&gt;
 *         &lt;/element&gt;
 *         &lt;element name="Bill" maxOccurs="unbounded" minOccurs="0"&gt;
 *           &lt;complexType&gt;
 *             &lt;complexContent&gt;
 *               &lt;restriction base="{http://www.w3.org/2001/XMLSchema}anyType"&gt;
 *                 &lt;sequence&gt;
 *                   &lt;element name="SCAC" type="{http://www.w3.org/2001/XMLSchema}string"/&gt;
 *                   &lt;element name="BillNumber" type="{http://www.w3.org/2001/XMLSchema}string"/&gt;
 *                   &lt;element name="HouseBillNumber" type="{http://www.w3.org/2001/XMLSchema}string" minOccurs="0"/&gt;
 *                   &lt;element name="ReferenceNumber" type="{http://www.w3.org/2001/XMLSchema}string" minOccurs="0"/&gt;
 *                 &lt;/sequence&gt;
 *               &lt;/restriction&gt;
 *             &lt;/complexContent&gt;
 *           &lt;/complexType&gt;
 *         &lt;/element&gt;
 *         &lt;element name="ISFNumber" maxOccurs="unbounded" minOccurs="0"&gt;
 *           &lt;complexType&gt;
 *             &lt;complexContent&gt;
 *               &lt;restriction base="{http://www.w3.org/2001/XMLSchema}anyType"&gt;
 *                 &lt;sequence&gt;
 *                   &lt;element name="Filer" type="{http://www.w3.org/2001/XMLSchema}string"/&gt;
 *                   &lt;element name="ISFNumber" type="{http://www.w3.org/2001/XMLSchema}string"/&gt;
 *                 &lt;/sequence&gt;
 *               &lt;/restriction&gt;
 *             &lt;/complexContent&gt;
 *           &lt;/complexType&gt;
 *         &lt;/element&gt;
 *         &lt;element name="ITN" maxOccurs="unbounded" minOccurs="0"&gt;
 *           &lt;complexType&gt;
 *             &lt;complexContent&gt;
 *               &lt;restriction base="{http://www.w3.org/2001/XMLSchema}anyType"&gt;
 *                 &lt;sequence&gt;
 *                   &lt;element name="ITN" type="{http://www.w3.org/2001/XMLSchema}string"/&gt;
 *                   &lt;element name="ReferenceNumber" type="{http://www.w3.org/2001/XMLSchema}string" minOccurs="0"/&gt;
 *                 &lt;/sequence&gt;
 *               &lt;/restriction&gt;
 *             &lt;/complexContent&gt;
 *           &lt;/complexType&gt;
 *         &lt;/element&gt;
 *         &lt;element name="XTN" maxOccurs="unbounded" minOccurs="0"&gt;
 *           &lt;complexType&gt;
 *             &lt;complexContent&gt;
 *               &lt;restriction base="{http://www.w3.org/2001/XMLSchema}anyType"&gt;
 *                 &lt;sequence&gt;
 *                   &lt;element name="XTN" type="{http://www.w3.org/2001/XMLSchema}string"/&gt;
 *                   &lt;element name="ReferenceNumber" type="{http://www.w3.org/2001/XMLSchema}string" minOccurs="0"/&gt;
 *                 &lt;/sequence&gt;
 *               &lt;/restriction&gt;
 *             &lt;/complexContent&gt;
 *           &lt;/complexType&gt;
 *         &lt;/element&gt;
 *         &lt;element name="FTZAdmissionNbr" type="{http://www.w3.org/2001/XMLSchema}string" minOccurs="0"/&gt;
 *         &lt;element name="ImporterOfRecordNbr" type="{http://www.w3.org/2001/XMLSchema}string" minOccurs="0"/&gt;
 *         &lt;element name="OtherTransaction" maxOccurs="unbounded" minOccurs="0"&gt;
 *           &lt;complexType&gt;
 *             &lt;complexContent&gt;
 *               &lt;restriction base="{http://www.w3.org/2001/XMLSchema}anyType"&gt;
 *                 &lt;sequence&gt;
 *                   &lt;element name="TransactionName" type="{http://www.w3.org/2001/XMLSchema}string"/&gt;
 *                   &lt;element name="TransactionNumber" type="{http://www.w3.org/2001/XMLSchema}string"/&gt;
 *                 &lt;/sequence&gt;
 *               &lt;/restriction&gt;
 *             &lt;/complexContent&gt;
 *           &lt;/complexType&gt;
 *         &lt;/element&gt;
 *       &lt;/sequence&gt;
 *     &lt;/restriction&gt;
 *   &lt;/complexContent&gt;
 * &lt;/complexType&gt;
 * </pre>
 * 
 * 
 */
@XmlAccessorType(XmlAccessType.FIELD)
@XmlType(name = "TradeTransaction", propOrder = {
    "transactionCategory",
    "entry",
    "entrySummary",
    "bill",
    "isfNumber",
    "itn",
    "xtn",
    "ftzAdmissionNbr",
    "importerOfRecordNbr",
    "otherTransaction"
})
public class TradeTransaction {

    @XmlElement(name = "TransactionCategory", required = true)
    protected String transactionCategory;
    @XmlElement(name = "Entry")
    protected List<TradeTransaction.Entry> entry;
    @XmlElement(name = "EntrySummary")
    protected List<TradeTransaction.EntrySummary> entrySummary;
    @XmlElement(name = "Bill")
    protected List<TradeTransaction.Bill> bill;
    @XmlElement(name = "ISFNumber")
    protected List<TradeTransaction.ISFNumber> isfNumber;
    @XmlElement(name = "ITN")
    protected List<TradeTransaction.ITN> itn;
    @XmlElement(name = "XTN")
    protected List<TradeTransaction.XTN> xtn;
    @XmlElement(name = "FTZAdmissionNbr")
    protected String ftzAdmissionNbr;
    @XmlElement(name = "ImporterOfRecordNbr")
    protected String importerOfRecordNbr;
    @XmlElement(name = "OtherTransaction")
    protected List<TradeTransaction.OtherTransaction> otherTransaction;

    /**
     * Gets the value of the transactionCategory property.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getTransactionCategory() {
        return transactionCategory;
    }

    /**
     * Sets the value of the transactionCategory property.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setTransactionCategory(String value) {
        this.transactionCategory = value;
    }

    /**
     * Gets the value of the entry property.
     * 
     * <p>
     * This accessor method returns a reference to the live list,
     * not a snapshot. Therefore any modification you make to the
     * returned list will be present inside the JAXB object.
     * This is why there is not a <CODE>set</CODE> method for the entry property.
     * 
     * <p>
     * For example, to add a new item, do as follows:
     * <pre>
     *    getEntry().add(newItem);
     * </pre>
     * 
     * 
     * <p>
     * Objects of the following type(s) are allowed in the list
     * {@link TradeTransaction.Entry }
     * 
     * 
     */
    public List<TradeTransaction.Entry> getEntry() {
        if (entry == null) {
            entry = new ArrayList<TradeTransaction.Entry>();
        }
        return this.entry;
    }

    /**
     * Gets the value of the entrySummary property.
     * 
     * <p>
     * This accessor method returns a reference to the live list,
     * not a snapshot. Therefore any modification you make to the
     * returned list will be present inside the JAXB object.
     * This is why there is not a <CODE>set</CODE> method for the entrySummary property.
     * 
     * <p>
     * For example, to add a new item, do as follows:
     * <pre>
     *    getEntrySummary().add(newItem);
     * </pre>
     * 
     * 
     * <p>
     * Objects of the following type(s) are allowed in the list
     * {@link TradeTransaction.EntrySummary }
     * 
     * 
     */
    public List<TradeTransaction.EntrySummary> getEntrySummary() {
        if (entrySummary == null) {
            entrySummary = new ArrayList<TradeTransaction.EntrySummary>();
        }
        return this.entrySummary;
    }

    /**
     * Gets the value of the bill property.
     * 
     * <p>
     * This accessor method returns a reference to the live list,
     * not a snapshot. Therefore any modification you make to the
     * returned list will be present inside the JAXB object.
     * This is why there is not a <CODE>set</CODE> method for the bill property.
     * 
     * <p>
     * For example, to add a new item, do as follows:
     * <pre>
     *    getBill().add(newItem);
     * </pre>
     * 
     * 
     * <p>
     * Objects of the following type(s) are allowed in the list
     * {@link TradeTransaction.Bill }
     * 
     * 
     */
    public List<TradeTransaction.Bill> getBill() {
        if (bill == null) {
            bill = new ArrayList<TradeTransaction.Bill>();
        }
        return this.bill;
    }

    /**
     * Gets the value of the isfNumber property.
     * 
     * <p>
     * This accessor method returns a reference to the live list,
     * not a snapshot. Therefore any modification you make to the
     * returned list will be present inside the JAXB object.
     * This is why there is not a <CODE>set</CODE> method for the isfNumber property.
     * 
     * <p>
     * For example, to add a new item, do as follows:
     * <pre>
     *    getISFNumber().add(newItem);
     * </pre>
     * 
     * 
     * <p>
     * Objects of the following type(s) are allowed in the list
     * {@link TradeTransaction.ISFNumber }
     * 
     * 
     */
    public List<TradeTransaction.ISFNumber> getISFNumber() {
        if (isfNumber == null) {
            isfNumber = new ArrayList<TradeTransaction.ISFNumber>();
        }
        return this.isfNumber;
    }

    /**
     * Gets the value of the itn property.
     * 
     * <p>
     * This accessor method returns a reference to the live list,
     * not a snapshot. Therefore any modification you make to the
     * returned list will be present inside the JAXB object.
     * This is why there is not a <CODE>set</CODE> method for the itn property.
     * 
     * <p>
     * For example, to add a new item, do as follows:
     * <pre>
     *    getITN().add(newItem);
     * </pre>
     * 
     * 
     * <p>
     * Objects of the following type(s) are allowed in the list
     * {@link TradeTransaction.ITN }
     * 
     * 
     */
    public List<TradeTransaction.ITN> getITN() {
        if (itn == null) {
            itn = new ArrayList<TradeTransaction.ITN>();
        }
        return this.itn;
    }

    /**
     * Gets the value of the xtn property.
     * 
     * <p>
     * This accessor method returns a reference to the live list,
     * not a snapshot. Therefore any modification you make to the
     * returned list will be present inside the JAXB object.
     * This is why there is not a <CODE>set</CODE> method for the xtn property.
     * 
     * <p>
     * For example, to add a new item, do as follows:
     * <pre>
     *    getXTN().add(newItem);
     * </pre>
     * 
     * 
     * <p>
     * Objects of the following type(s) are allowed in the list
     * {@link TradeTransaction.XTN }
     * 
     * 
     */
    public List<TradeTransaction.XTN> getXTN() {
        if (xtn == null) {
            xtn = new ArrayList<TradeTransaction.XTN>();
        }
        return this.xtn;
    }

    /**
     * Gets the value of the ftzAdmissionNbr property.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getFTZAdmissionNbr() {
        return ftzAdmissionNbr;
    }

    /**
     * Sets the value of the ftzAdmissionNbr property.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setFTZAdmissionNbr(String value) {
        this.ftzAdmissionNbr = value;
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

    /**
     * Gets the value of the otherTransaction property.
     * 
     * <p>
     * This accessor method returns a reference to the live list,
     * not a snapshot. Therefore any modification you make to the
     * returned list will be present inside the JAXB object.
     * This is why there is not a <CODE>set</CODE> method for the otherTransaction property.
     * 
     * <p>
     * For example, to add a new item, do as follows:
     * <pre>
     *    getOtherTransaction().add(newItem);
     * </pre>
     * 
     * 
     * <p>
     * Objects of the following type(s) are allowed in the list
     * {@link TradeTransaction.OtherTransaction }
     * 
     * 
     */
    public List<TradeTransaction.OtherTransaction> getOtherTransaction() {
        if (otherTransaction == null) {
            otherTransaction = new ArrayList<TradeTransaction.OtherTransaction>();
        }
        return this.otherTransaction;
    }


    /**
     * <p>Java class for anonymous complex type.
     * 
     * <p>The following schema fragment specifies the expected content contained within this class.
     * 
     * <pre>
     * &lt;complexType&gt;
     *   &lt;complexContent&gt;
     *     &lt;restriction base="{http://www.w3.org/2001/XMLSchema}anyType"&gt;
     *       &lt;sequence&gt;
     *         &lt;element name="SCAC" type="{http://www.w3.org/2001/XMLSchema}string"/&gt;
     *         &lt;element name="BillNumber" type="{http://www.w3.org/2001/XMLSchema}string"/&gt;
     *         &lt;element name="HouseBillNumber" type="{http://www.w3.org/2001/XMLSchema}string" minOccurs="0"/&gt;
     *         &lt;element name="ReferenceNumber" type="{http://www.w3.org/2001/XMLSchema}string" minOccurs="0"/&gt;
     *       &lt;/sequence&gt;
     *     &lt;/restriction&gt;
     *   &lt;/complexContent&gt;
     * &lt;/complexType&gt;
     * </pre>
     * 
     * 
     */
    @XmlAccessorType(XmlAccessType.FIELD)
    @XmlType(name = "", propOrder = {
        "scac",
        "billNumber",
        "houseBillNumber",
        "referenceNumber"
    })
    public static class Bill {

        @XmlElement(name = "SCAC", required = true)
        protected String scac;
        @XmlElement(name = "BillNumber", required = true)
        protected String billNumber;
        @XmlElement(name = "HouseBillNumber")
        protected String houseBillNumber;
        @XmlElement(name = "ReferenceNumber")
        protected String referenceNumber;

        /**
         * Gets the value of the scac property.
         * 
         * @return
         *     possible object is
         *     {@link String }
         *     
         */
        public String getSCAC() {
            return scac;
        }

        /**
         * Sets the value of the scac property.
         * 
         * @param value
         *     allowed object is
         *     {@link String }
         *     
         */
        public void setSCAC(String value) {
            this.scac = value;
        }

        /**
         * Gets the value of the billNumber property.
         * 
         * @return
         *     possible object is
         *     {@link String }
         *     
         */
        public String getBillNumber() {
            return billNumber;
        }

        /**
         * Sets the value of the billNumber property.
         * 
         * @param value
         *     allowed object is
         *     {@link String }
         *     
         */
        public void setBillNumber(String value) {
            this.billNumber = value;
        }

        /**
         * Gets the value of the houseBillNumber property.
         * 
         * @return
         *     possible object is
         *     {@link String }
         *     
         */
        public String getHouseBillNumber() {
            return houseBillNumber;
        }

        /**
         * Sets the value of the houseBillNumber property.
         * 
         * @param value
         *     allowed object is
         *     {@link String }
         *     
         */
        public void setHouseBillNumber(String value) {
            this.houseBillNumber = value;
        }

        /**
         * Gets the value of the referenceNumber property.
         * 
         * @return
         *     possible object is
         *     {@link String }
         *     
         */
        public String getReferenceNumber() {
            return referenceNumber;
        }

        /**
         * Sets the value of the referenceNumber property.
         * 
         * @param value
         *     allowed object is
         *     {@link String }
         *     
         */
        public void setReferenceNumber(String value) {
            this.referenceNumber = value;
        }

    }


    /**
     * <p>Java class for anonymous complex type.
     * 
     * <p>The following schema fragment specifies the expected content contained within this class.
     * 
     * <pre>
     * &lt;complexType&gt;
     *   &lt;complexContent&gt;
     *     &lt;restriction base="{http://www.w3.org/2001/XMLSchema}anyType"&gt;
     *       &lt;sequence&gt;
     *         &lt;element name="EntryNumber" type="{http://www.w3.org/2001/XMLSchema}string"/&gt;
     *         &lt;element name="Filer" type="{http://www.w3.org/2001/XMLSchema}string"/&gt;
     *         &lt;element name="EntryLineNumber" type="{http://www.w3.org/2001/XMLSchema}string" minOccurs="0"/&gt;
     *         &lt;element name="ReferenceNumber" type="{http://www.w3.org/2001/XMLSchema}string" minOccurs="0"/&gt;
     *       &lt;/sequence&gt;
     *     &lt;/restriction&gt;
     *   &lt;/complexContent&gt;
     * &lt;/complexType&gt;
     * </pre>
     * 
     * 
     */
    @XmlAccessorType(XmlAccessType.FIELD)
    @XmlType(name = "", propOrder = {
        "entryNumber",
        "filer",
        "entryLineNumber",
        "referenceNumber"
    })
    public static class Entry {

        @XmlElement(name = "EntryNumber", required = true)
        protected String entryNumber;
        @XmlElement(name = "Filer", required = true)
        protected String filer;
        @XmlElement(name = "EntryLineNumber")
        protected String entryLineNumber;
        @XmlElement(name = "ReferenceNumber")
        protected String referenceNumber;

        /**
         * Gets the value of the entryNumber property.
         * 
         * @return
         *     possible object is
         *     {@link String }
         *     
         */
        public String getEntryNumber() {
            return entryNumber;
        }

        /**
         * Sets the value of the entryNumber property.
         * 
         * @param value
         *     allowed object is
         *     {@link String }
         *     
         */
        public void setEntryNumber(String value) {
            this.entryNumber = value;
        }

        /**
         * Gets the value of the filer property.
         * 
         * @return
         *     possible object is
         *     {@link String }
         *     
         */
        public String getFiler() {
            return filer;
        }

        /**
         * Sets the value of the filer property.
         * 
         * @param value
         *     allowed object is
         *     {@link String }
         *     
         */
        public void setFiler(String value) {
            this.filer = value;
        }

        /**
         * Gets the value of the entryLineNumber property.
         * 
         * @return
         *     possible object is
         *     {@link String }
         *     
         */
        public String getEntryLineNumber() {
            return entryLineNumber;
        }

        /**
         * Sets the value of the entryLineNumber property.
         * 
         * @param value
         *     allowed object is
         *     {@link String }
         *     
         */
        public void setEntryLineNumber(String value) {
            this.entryLineNumber = value;
        }

        /**
         * Gets the value of the referenceNumber property.
         * 
         * @return
         *     possible object is
         *     {@link String }
         *     
         */
        public String getReferenceNumber() {
            return referenceNumber;
        }

        /**
         * Sets the value of the referenceNumber property.
         * 
         * @param value
         *     allowed object is
         *     {@link String }
         *     
         */
        public void setReferenceNumber(String value) {
            this.referenceNumber = value;
        }

    }


    /**
     * <p>Java class for anonymous complex type.
     * 
     * <p>The following schema fragment specifies the expected content contained within this class.
     * 
     * <pre>
     * &lt;complexType&gt;
     *   &lt;complexContent&gt;
     *     &lt;restriction base="{http://www.w3.org/2001/XMLSchema}anyType"&gt;
     *       &lt;sequence&gt;
     *         &lt;element name="EntryNumber" type="{http://www.w3.org/2001/XMLSchema}string"/&gt;
     *         &lt;element name="Filer" type="{http://www.w3.org/2001/XMLSchema}string"/&gt;
     *         &lt;element name="EntryLineNumber" type="{http://www.w3.org/2001/XMLSchema}string" minOccurs="0"/&gt;
     *         &lt;element name="ReferenceNumber" type="{http://www.w3.org/2001/XMLSchema}string" minOccurs="0"/&gt;
     *       &lt;/sequence&gt;
     *     &lt;/restriction&gt;
     *   &lt;/complexContent&gt;
     * &lt;/complexType&gt;
     * </pre>
     * 
     * 
     */
    @XmlAccessorType(XmlAccessType.FIELD)
    @XmlType(name = "", propOrder = {
        "entryNumber",
        "filer",
        "entryLineNumber",
        "referenceNumber"
    })
    public static class EntrySummary {

        @XmlElement(name = "EntryNumber", required = true)
        protected String entryNumber;
        @XmlElement(name = "Filer", required = true)
        protected String filer;
        @XmlElement(name = "EntryLineNumber")
        protected String entryLineNumber;
        @XmlElement(name = "ReferenceNumber")
        protected String referenceNumber;

        /**
         * Gets the value of the entryNumber property.
         * 
         * @return
         *     possible object is
         *     {@link String }
         *     
         */
        public String getEntryNumber() {
            return entryNumber;
        }

        /**
         * Sets the value of the entryNumber property.
         * 
         * @param value
         *     allowed object is
         *     {@link String }
         *     
         */
        public void setEntryNumber(String value) {
            this.entryNumber = value;
        }

        /**
         * Gets the value of the filer property.
         * 
         * @return
         *     possible object is
         *     {@link String }
         *     
         */
        public String getFiler() {
            return filer;
        }

        /**
         * Sets the value of the filer property.
         * 
         * @param value
         *     allowed object is
         *     {@link String }
         *     
         */
        public void setFiler(String value) {
            this.filer = value;
        }

        /**
         * Gets the value of the entryLineNumber property.
         * 
         * @return
         *     possible object is
         *     {@link String }
         *     
         */
        public String getEntryLineNumber() {
            return entryLineNumber;
        }

        /**
         * Sets the value of the entryLineNumber property.
         * 
         * @param value
         *     allowed object is
         *     {@link String }
         *     
         */
        public void setEntryLineNumber(String value) {
            this.entryLineNumber = value;
        }

        /**
         * Gets the value of the referenceNumber property.
         * 
         * @return
         *     possible object is
         *     {@link String }
         *     
         */
        public String getReferenceNumber() {
            return referenceNumber;
        }

        /**
         * Sets the value of the referenceNumber property.
         * 
         * @param value
         *     allowed object is
         *     {@link String }
         *     
         */
        public void setReferenceNumber(String value) {
            this.referenceNumber = value;
        }

    }


    /**
     * <p>Java class for anonymous complex type.
     * 
     * <p>The following schema fragment specifies the expected content contained within this class.
     * 
     * <pre>
     * &lt;complexType&gt;
     *   &lt;complexContent&gt;
     *     &lt;restriction base="{http://www.w3.org/2001/XMLSchema}anyType"&gt;
     *       &lt;sequence&gt;
     *         &lt;element name="Filer" type="{http://www.w3.org/2001/XMLSchema}string"/&gt;
     *         &lt;element name="ISFNumber" type="{http://www.w3.org/2001/XMLSchema}string"/&gt;
     *       &lt;/sequence&gt;
     *     &lt;/restriction&gt;
     *   &lt;/complexContent&gt;
     * &lt;/complexType&gt;
     * </pre>
     * 
     * 
     */
    @XmlAccessorType(XmlAccessType.FIELD)
    @XmlType(name = "", propOrder = {
        "filer",
        "isfNumber"
    })
    public static class ISFNumber {

        @XmlElement(name = "Filer", required = true)
        protected String filer;
        @XmlElement(name = "ISFNumber", required = true)
        protected String isfNumber;

        /**
         * Gets the value of the filer property.
         * 
         * @return
         *     possible object is
         *     {@link String }
         *     
         */
        public String getFiler() {
            return filer;
        }

        /**
         * Sets the value of the filer property.
         * 
         * @param value
         *     allowed object is
         *     {@link String }
         *     
         */
        public void setFiler(String value) {
            this.filer = value;
        }

        /**
         * Gets the value of the isfNumber property.
         * 
         * @return
         *     possible object is
         *     {@link String }
         *     
         */
        public String getISFNumber() {
            return isfNumber;
        }

        /**
         * Sets the value of the isfNumber property.
         * 
         * @param value
         *     allowed object is
         *     {@link String }
         *     
         */
        public void setISFNumber(String value) {
            this.isfNumber = value;
        }

    }


    /**
     * <p>Java class for anonymous complex type.
     * 
     * <p>The following schema fragment specifies the expected content contained within this class.
     * 
     * <pre>
     * &lt;complexType&gt;
     *   &lt;complexContent&gt;
     *     &lt;restriction base="{http://www.w3.org/2001/XMLSchema}anyType"&gt;
     *       &lt;sequence&gt;
     *         &lt;element name="ITN" type="{http://www.w3.org/2001/XMLSchema}string"/&gt;
     *         &lt;element name="ReferenceNumber" type="{http://www.w3.org/2001/XMLSchema}string" minOccurs="0"/&gt;
     *       &lt;/sequence&gt;
     *     &lt;/restriction&gt;
     *   &lt;/complexContent&gt;
     * &lt;/complexType&gt;
     * </pre>
     * 
     * 
     */
    @XmlAccessorType(XmlAccessType.FIELD)
    @XmlType(name = "", propOrder = {
        "itn",
        "referenceNumber"
    })
    public static class ITN {

        @XmlElement(name = "ITN", required = true)
        protected String itn;
        @XmlElement(name = "ReferenceNumber")
        protected String referenceNumber;

        /**
         * Gets the value of the itn property.
         * 
         * @return
         *     possible object is
         *     {@link String }
         *     
         */
        public String getITN() {
            return itn;
        }

        /**
         * Sets the value of the itn property.
         * 
         * @param value
         *     allowed object is
         *     {@link String }
         *     
         */
        public void setITN(String value) {
            this.itn = value;
        }

        /**
         * Gets the value of the referenceNumber property.
         * 
         * @return
         *     possible object is
         *     {@link String }
         *     
         */
        public String getReferenceNumber() {
            return referenceNumber;
        }

        /**
         * Sets the value of the referenceNumber property.
         * 
         * @param value
         *     allowed object is
         *     {@link String }
         *     
         */
        public void setReferenceNumber(String value) {
            this.referenceNumber = value;
        }

    }


    /**
     * <p>Java class for anonymous complex type.
     * 
     * <p>The following schema fragment specifies the expected content contained within this class.
     * 
     * <pre>
     * &lt;complexType&gt;
     *   &lt;complexContent&gt;
     *     &lt;restriction base="{http://www.w3.org/2001/XMLSchema}anyType"&gt;
     *       &lt;sequence&gt;
     *         &lt;element name="TransactionName" type="{http://www.w3.org/2001/XMLSchema}string"/&gt;
     *         &lt;element name="TransactionNumber" type="{http://www.w3.org/2001/XMLSchema}string"/&gt;
     *       &lt;/sequence&gt;
     *     &lt;/restriction&gt;
     *   &lt;/complexContent&gt;
     * &lt;/complexType&gt;
     * </pre>
     * 
     * 
     */
    @XmlAccessorType(XmlAccessType.FIELD)
    @XmlType(name = "", propOrder = {
        "transactionName",
        "transactionNumber"
    })
    public static class OtherTransaction {

        @XmlElement(name = "TransactionName", required = true)
        protected String transactionName;
        @XmlElement(name = "TransactionNumber", required = true)
        protected String transactionNumber;

        /**
         * Gets the value of the transactionName property.
         * 
         * @return
         *     possible object is
         *     {@link String }
         *     
         */
        public String getTransactionName() {
            return transactionName;
        }

        /**
         * Sets the value of the transactionName property.
         * 
         * @param value
         *     allowed object is
         *     {@link String }
         *     
         */
        public void setTransactionName(String value) {
            this.transactionName = value;
        }

        /**
         * Gets the value of the transactionNumber property.
         * 
         * @return
         *     possible object is
         *     {@link String }
         *     
         */
        public String getTransactionNumber() {
            return transactionNumber;
        }

        /**
         * Sets the value of the transactionNumber property.
         * 
         * @param value
         *     allowed object is
         *     {@link String }
         *     
         */
        public void setTransactionNumber(String value) {
            this.transactionNumber = value;
        }

    }


    /**
     * <p>Java class for anonymous complex type.
     * 
     * <p>The following schema fragment specifies the expected content contained within this class.
     * 
     * <pre>
     * &lt;complexType&gt;
     *   &lt;complexContent&gt;
     *     &lt;restriction base="{http://www.w3.org/2001/XMLSchema}anyType"&gt;
     *       &lt;sequence&gt;
     *         &lt;element name="XTN" type="{http://www.w3.org/2001/XMLSchema}string"/&gt;
     *         &lt;element name="ReferenceNumber" type="{http://www.w3.org/2001/XMLSchema}string" minOccurs="0"/&gt;
     *       &lt;/sequence&gt;
     *     &lt;/restriction&gt;
     *   &lt;/complexContent&gt;
     * &lt;/complexType&gt;
     * </pre>
     * 
     * 
     */
    @XmlAccessorType(XmlAccessType.FIELD)
    @XmlType(name = "", propOrder = {
        "xtn",
        "referenceNumber"
    })
    public static class XTN {

        @XmlElement(name = "XTN", required = true)
        protected String xtn;
        @XmlElement(name = "ReferenceNumber")
        protected String referenceNumber;

        /**
         * Gets the value of the xtn property.
         * 
         * @return
         *     possible object is
         *     {@link String }
         *     
         */
        public String getXTN() {
            return xtn;
        }

        /**
         * Sets the value of the xtn property.
         * 
         * @param value
         *     allowed object is
         *     {@link String }
         *     
         */
        public void setXTN(String value) {
            this.xtn = value;
        }

        /**
         * Gets the value of the referenceNumber property.
         * 
         * @return
         *     possible object is
         *     {@link String }
         *     
         */
        public String getReferenceNumber() {
            return referenceNumber;
        }

        /**
         * Sets the value of the referenceNumber property.
         * 
         * @param value
         *     allowed object is
         *     {@link String }
         *     
         */
        public void setReferenceNumber(String value) {
            this.referenceNumber = value;
        }

    }

}
