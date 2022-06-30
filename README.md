


              _____/\\\\\\\\\_________/\\\\\\\\\\\_______/\\\\\\\\\_____        
               ___/\\\\\\\\\\\\\_____/\\\/////////\\\___/\\\///////\\\___       
                __/\\\/////////\\\___\//\\\______\///___\///______\//\\\__      
                 _\/\\\_______\/\\\____\////\\\____________________/\\\/___     
                   _\/\\\\\\\\\\\\\\\_______\////\\\______________/\\\//_____    
                    _\/\\\/////////\\\__________\////\\\________/\\\//________   
                     _\/\\\_______\/\\\___/\\\______\//\\\_____/\\\/___________  
                      _\/\\\_______\/\\\__\///\\\\\\\\\\\/_____/\\\\\\\\\\\\\\\_ 
                       _\///________\///_____\///////////______\///////////////__




## AS2 Protocol Server

For getting started as a developer please consult the [Development Guide](/.github/DEVELOPMENT.md).

For information on how to run AS2NG please consult our [Usage Guide](/.github/USAGE.md)

For all other information please consult the [Wiki](https://github.com/as2network/as2ng/wiki).

### Example Workflow

```sequence
Shippment Information->Protocol Pipe: Necessary Information
Note right of Protocol: Protocol Parses raw EDI
Protocol Pipe-->Shippment Information: Validates
Protocol Pipe -->Protocol Service: Sends JSON Object
Protocol Service -->Network Service: Generates Contract from JSON Object 
Network Service -->Protocol Service: Contract Deployed
Protocol Service --> Protocol: Instrument Created at 0x....
Note right of Protocol: ABI.json and TDOC.json match and verify
Note left of Shippment Information: Shippment Information Polling (async)
Shippment Information->Protocol: Polling Job Status
Protocol->Shippment Information: Success
```

### Data Models

> TradeDoc Format

#### Main Document

![](https://raw.githubusercontent.com/freight-trust/tdocs/master/src/images/StandardDoc_StandardDocument.png)

####     Sections

![](https://raw.githubusercontent.com/freight-trust/tdocs/master/src/images/StandardDoc_Sections.png)




## License

Server Side Public License, v.1 

