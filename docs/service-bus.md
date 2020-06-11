# Service Bus

## Messaging Topology

This section describes the queue and topic schemas in use for communication between processes via ActiveMQ.

# Inbound

- `queue://inbound.submission.as2` represents any submissions received via the AS2 gateway.
- `queue://inbound.dis.validation` represents all validation responses received from DIS.
- `queue://inbound.dis.review` represents all review responses received from DIS.       
    
# Internal

- `queue://internal.submitted` represents that have been successfully published into the DIS MQ. 

# Outbound

- `queue://outbound.dis.submission` represents all processed document submissions ready to be forwarded into the DIS MQ.
- `queue://outbound.as2.validation` represents all processed validation messages ready to be forward to the Trading Partner via their AS2 inbox.
- `queue://outbound.as2.review` represents all processed reviews messages ready to be forward to the Trading Partner via their AS2 inbox.     

