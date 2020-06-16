
package gov.dhs.cbp.iws.itdsservices.iws;

import javax.xml.bind.annotation.XmlEnum;
import javax.xml.bind.annotation.XmlEnumValue;
import javax.xml.bind.annotation.XmlType;


/**
 * <p>Java class for enumMessageType.
 * 
 * <p>The following schema fragment specifies the expected content contained within this class.
 * <p>
 * <pre>
 * &lt;simpleType name="enumMessageType"&gt;
 *   &lt;restriction base="{http://www.w3.org/2001/XMLSchema}string"&gt;
 *     &lt;enumeration value="IWSMessage"/&gt;
 *     &lt;enumeration value="Other"/&gt;
 *   &lt;/restriction&gt;
 * &lt;/simpleType&gt;
 * </pre>
 * 
 */
@XmlType(name = "enumMessageType")
@XmlEnum
public enum EnumMessageType {

    @XmlEnumValue("IWSMessage")
    IWS_MESSAGE("IWSMessage"),
    @XmlEnumValue("Other")
    OTHER("Other");
    private final String value;

    EnumMessageType(String v) {
        value = v;
    }

    public String value() {
        return value;
    }

    public static EnumMessageType fromValue(String v) {
        for (EnumMessageType c: EnumMessageType.values()) {
            if (c.value.equals(v)) {
                return c;
            }
        }
        throw new IllegalArgumentException(v);
    }

}
