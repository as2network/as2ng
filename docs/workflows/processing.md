# Core Processing

This section describes the core message forwarding within the system. 

> For now we only support very basic message routing, with all messages originating from the `as2` gateway to be 
> forwarded to the `dis` gateway, and all messages received from the `dis` gateway to be forwarded to the relevant Trading 
> Partner in the `as2` gateway.

## Workflow 

**Note:**

- `<MESSAGE_TYPE>` represents the message body payload type e.g. `DocumentSubmissionPackage` or `MessageValidationResponse`.
- `<PORT_CODE>` represents the 4 digit transmitter port code in the message header.
- `<FILER_CODE>` represents the 3 character transmitter filer code in the message header. 

### AS2 to DIS

```mermaid
sequenceDiagram
    ActiveMQ ->>+ Processor: Offer message from queue://inbound.as2.>
    Note right of ActiveMQ: `>` indicates a recursive match which will pick up all message types
    Processor ->>+ Postgres: Persist message
    Postgres -->>- Processor: Ack write
    Note right of ActiveMQ: <MESSAGE_TYPE>, <PORT_CODE, <FILER_CODE> derived from message header   
    Processor ->>+ ActiveMQ: Publish message into queue://outbound.dis.<MESSAGE_TYPE>.<PORT_CODE>.<FILER_CODE>        
    ActiveMQ -->>- Processor: Ack message
    Processor -->>- ActiveMQ: Ack message from queue://inbound.as2.>                             
```

### DIS to AS2 

```mermaid
sequenceDiagram    
    ActiveMQ ->>+ Processor: Offer message from queue://inbound.dis.>
    Note right of ActiveMQ: `>` indicates a recursive match which will pick up all message types
    par Persist messag
        Processor ->>+ Postgres: Persist message
        Postgres -->>- Processor: Ack write
    and Lookup Metadata
        Note right of Processor: <FILER_CODE> used to determine Trading Partner
        Processor ->>+ Postgres: Lookup Trading Partner metadata
        Postgres -->>- Processor: Metadata
    end
    Note right of ActiveMQ: <MESSAGE_TYPE>, <PORT_CODE, <FILER_CODE> derived from message header
    Processor ->>+ ActiveMQ: Publish enriched message into queue://outbound.as2.<MESSAGE_TYPE>.<PORT_CODE>.<FILER_CODE>
    ActiveMQ -->>- Processor: Ack message
    Processor -->>- ActiveMQ: Ack message from queue://inbound.dis.>                                             
```
