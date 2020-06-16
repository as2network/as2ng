# Service Bus

This section describes the queue and topic schemas in use for communication between processes via ActiveMQ.

**Note:**

- `<MESSAGE_TYPE>` represents the message body payload type e.g. `DocumentSubmissionPackage` or `MessageValidationResponse`.
- `<PORT_CODE>` represents the 4 digit transmitter port code in the message header.
- `<FILER_CODE>` represents the 3 character transmitter filer code in the message header. 

# Inbound

The following queues represent messages capture from one gateway for delivery into another gateway. 

- `queue://inbound.as2.<MESSAGE_TYPE>.<PORT_CODE>.<FILER_CODE>`
- `queue://inbound.dis.<MESSAGE_TYPE>.<PORT_CODE>.<FILER_CODE>`

# Outbound

The following queues represent messages ready for delivery into a gateway.

- `queue://outbound.as2.<MESSAGE_TYPE>.<PORT_CODE>.<FILER_CODE>`
- `queue://outbound.dis.<MESSAGE_TYPE>.<PORT_CODE>.<FILER_CODE>`

# Delivered

The following queues represent messages that have been successfully delivered into a gateway.

- `queue://delivered.as2.<MESSAGE_TYPE>.<PORT_CODE>.<FILER_CODE>`
- `queue://delivered.dis.<MESSAGE_TYPE>.<PORT_CODE>.<FILER_CODE>`
    
