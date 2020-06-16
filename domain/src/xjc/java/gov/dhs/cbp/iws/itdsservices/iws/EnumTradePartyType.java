
package gov.dhs.cbp.iws.itdsservices.iws;

import javax.xml.bind.annotation.XmlEnum;
import javax.xml.bind.annotation.XmlType;


/**
 * <p>Java class for enumTradePartyType.
 * 
 * <p>The following schema fragment specifies the expected content contained within this class.
 * <p>
 * <pre>
 * &lt;simpleType name="enumTradePartyType"&gt;
 *   &lt;restriction base="{http://www.w3.org/2001/XMLSchema}string"&gt;
 *     &lt;enumeration value="MANUFACTURER"/&gt;
 *     &lt;enumeration value="EXPORTER"/&gt;
 *     &lt;enumeration value="IMPORTER"/&gt;
 *     &lt;enumeration value="SHIPPER"/&gt;
 *     &lt;enumeration value="CARRIER"/&gt;
 *     &lt;enumeration value="BROKER"/&gt;
 *     &lt;enumeration value="FILER"/&gt;
 *     &lt;enumeration value="CONSIGNEE"/&gt;
 *     &lt;enumeration value="AGENT"/&gt;
 *     &lt;enumeration value="BUYER"/&gt;
 *     &lt;enumeration value="SELLER"/&gt;
 *     &lt;enumeration value="FACILITATOR"/&gt;
 *     &lt;enumeration value="OTHER"/&gt;
 *     &lt;enumeration value="UNKNOWN"/&gt;
 *   &lt;/restriction&gt;
 * &lt;/simpleType&gt;
 * </pre>
 * 
 */
@XmlType(name = "enumTradePartyType")
@XmlEnum
public enum EnumTradePartyType {

    MANUFACTURER,
    EXPORTER,
    IMPORTER,
    SHIPPER,
    CARRIER,
    BROKER,
    FILER,
    CONSIGNEE,
    AGENT,
    BUYER,
    SELLER,
    FACILITATOR,
    OTHER,
    UNKNOWN;

    public String value() {
        return name();
    }

    public static EnumTradePartyType fromValue(String v) {
        return valueOf(v);
    }

}
