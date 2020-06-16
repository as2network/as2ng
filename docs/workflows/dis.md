# DIS Gateway

The DIS Gateway is responsible for all communication to and from the DIS service via MQ.

## Basic Workflow

**Note:**

- `<MESSAGE_TYPE>` represents the message body payload type e.g. `DocumentSubmissionPackage` or `MessageValidationResponse`.
- `<PORT_CODE>` represents the 4 digit transmitter port code in the message header.
- `<FILER_CODE>` represents the 3 character transmitter filer code in the message header. 

### Outbound to DIS

```mermaid
sequenceDiagram
    ActiveMQ ->>+ DIS Gateway: Offer message from queue://outbound.dis.>
    Note over ActiveMQ,DIS Gateway: `>` indicates a recursive match all message types,port codes and filer codes
    DIS Gateway ->>+ DIS MQ: Publish message into queue://ATS.DIS.MSG.INBOUND
    DIS MQ -->>- DIS Gateway: Ack message    
    DIS Gateway ->>+ ActiveMQ: Publish message into queue://delivered.dis.documentSubmissionPackage.<PORT_CODE>.<FILER_CODE>
    ActiveMQ -->>- DIS Gateway: Ack message from queue://delivered.dis.documentSubmissionPackage.<PORT_CODE>.<FILER_CODE>
    DIS Gateway -->>- ActiveMQ: Ack message from queue://outbound.dis.documentSubmissionPackage.*                        
```

### Inbound from DIS

```mermaid
sequenceDiagram
    DIS MQ ->>+ DIS Gateway: Offer message from queue://ATS.DIS.<PORT_CODE>.<FILER_CODE>.Outbound
    Note over DIS MQ,DIS Gateway: Extract the message body payload type in camelCase for <MESSAGE_TYPE>   
    DIS Gateway ->>+ ActiveMQ: Publish message into queue://inbound.dis.<MESSAGE_TYPE>.<PORT_CODE>.<FILER_CODE>
    ActiveMQ -->>- DIS Gateway: Ack message   
    DIS Gateway -->> DIS MQ: Ack message from queue://ATS.DIS.<PORT_CODE><FILER_CODE>.Outbound    
    deactivate DIS Gateway        
```


