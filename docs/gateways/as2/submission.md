# AS2 - Document Submission

```mermaid
sequenceDiagram
    TradingPartner ->> AS2 Gateway: Upload DIS MessageEnvelope within XML file
    AS2 Gateway ->> AS2 Gateway: Parse & validate
    AS2 Gateway ->> ActiveMQ: Publish message into queue://inbound.as2    
    AS2 Gateway ->> TradingPartner: Ack receipt of file          
```





