# Architecture

The overall architecture is message driven, with a centralised message bus and a queue/topic topology which allows for:

- accepting messages from a variety of gateways into a single unified processing pipeline
- opportunities for addressing scaling / resiliency problems on a per workflow basis
- a series of simple single-responsibility processes
- ease of introspection and auditing across workflows 

## Key Technologies

- [ActiveMQ](http://activemq.apache.org/) as a core messaging bus
- [PostgreSQL](https://www.postgresql.org) for persistence
- [Kotlin](https://kotlinlang.org)/JVM for gateways & processing 

