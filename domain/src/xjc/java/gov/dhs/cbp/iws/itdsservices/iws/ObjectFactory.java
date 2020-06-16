
package gov.dhs.cbp.iws.itdsservices.iws;

import javax.xml.bind.JAXBElement;
import javax.xml.bind.annotation.XmlElementDecl;
import javax.xml.bind.annotation.XmlRegistry;
import javax.xml.namespace.QName;


/**
 * This object contains factory methods for each 
 * Java content interface and Java element interface 
 * generated in the gov.dhs.cbp.iws.itdsservices.iws package. 
 * <p>An ObjectFactory allows you to programatically 
 * construct new instances of the Java representation 
 * for XML content. The Java representation of XML 
 * content can consist of schema derived interfaces 
 * and classes representing the binding of schema 
 * type definitions, element declarations and model 
 * groups.  Factory methods for each of these are 
 * provided in this class.
 * 
 */
@XmlRegistry
public class ObjectFactory {

    private final static QName _ResponseMessage_QNAME = new QName("http://iws.cbp.dhs.gov/ITDSServices/IWS", "ResponseMessage");

    /**
     * Create a new ObjectFactory that can be used to create new instances of schema derived classes for package: gov.dhs.cbp.iws.itdsservices.iws
     * 
     */
    public ObjectFactory() {
    }

    /**
     * Create an instance of {@link ResponseMessage }
     * 
     */
    public ResponseMessage createResponseMessage() {
        return new ResponseMessage();
    }

    /**
     * Create an instance of {@link ErrorDetailsList }
     * 
     */
    public ErrorDetailsList createErrorDetailsList() {
        return new ErrorDetailsList();
    }

    /**
     * Create an instance of {@link ErrorDetails }
     * 
     */
    public ErrorDetails createErrorDetails() {
        return new ErrorDetails();
    }

    /**
     * Create an instance of {@link JAXBElement }{@code <}{@link ResponseMessage }{@code >}
     * 
     * @param value
     *     Java instance representing xml element's value.
     * @return
     *     the new instance of {@link JAXBElement }{@code <}{@link ResponseMessage }{@code >}
     */
    @XmlElementDecl(namespace = "http://iws.cbp.dhs.gov/ITDSServices/IWS", name = "ResponseMessage")
    public JAXBElement<ResponseMessage> createResponseMessage(ResponseMessage value) {
        return new JAXBElement<ResponseMessage>(_ResponseMessage_QNAME, ResponseMessage.class, null, value);
    }

}
