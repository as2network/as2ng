package network.as2.server.util

object EdiFact {

  val sampleInvoice =
    "UNB+UNOA:1+01010000253001+O0013000093SCHA-Z59+991006:1902+PAYO0012101221'\n" +
      "UNH+1+INVOIC:D:97A:UN'\n" +
      "BGM+381+1060113800026+9'\n" +
      "DTM+137:199910060000:102'\n" +
      "NAD+BT+VAUXHALL MOTORS LTD::91'\n" +
      "RFF+VA:382324067'\n" +
      "NAD+SU+2002993::92'\n" +
      "RFF+VA:123844750'\n" +
      "CUX+2:EUR'\n" +
      "PAT+1'\n" +
      "DTM+140:19991031:102'\n" +
      "LIN+++090346642:IN'\n" +
      "QTY+12:54:PCE'\n" +
      "MOA+203:1960.29'\n" +
      "PRI+AAA:3630.1724::NTP:100:C62'\n" +
      "RFF+SI:165480'\n" +
      "DTM+11:199909280000:102'\n" +
      "RFF+ON:X18V00003'\n" +
      "RFF+TN:AB1'\n" +
      "TAX+7+VAT+++:::0'\n" +
      "NAD+ST+023::92'\n" +
      "UNS+S'\n" +
      "MOA+77:1960.29'\n" +
      "TAX+7+VAT'\n" +
      "UNT+24+1'\n" +
      "UNZ+1+PAYO0012101221'"

}

object EdiX12 {

  val samplePurchaseOrder =
    "ISA*00*          *00*          *ZZ*SENDERISA      *14*0073268795005  *020226*1534*U*00401*000000001*0*T*>~\n" +
      "GS*PO*SENDERGS*007326879*20020226*1534*1*X*004010~\n" +
      "ST*850*000000001~\n" +
      "BEG*00*SA*A99999-01**19970214~\n" +
      "REF*VR*54321~\n" +
      "ITD*01*3*1**15**16~\n" +
      "DTM*002*19971219~\n" +
      "DTM*002*19971219~\n" +
      "N1*BT*BUYSNACKS INC.*9*1223334444~\n" +
      "N3*P.O. BOX 0000~\n" +
      "N4*TEMPLE*TX*76503~\n" +
      "N1*ST*BUYSNACKS PORT*9*1223334445~\n" +
      "N3*1000 N. SAMPLE HIGHWAY~\n" +
      "N4*ATHENS*GA*30603~\n" +
      "PO1**16*CA*12.34**CB*000111111*UA*002840022222~\n" +
      "PID*F****CRUNCHY CHIPS LSS~\n" +
      "PO4*48*7.89*LB~\n" +
      "PO1**13*CA*12.34**CB*000555555*UA*002840033333~\n" +
      "PID*F****NACHO CHIPS LSS~\n" +
      "PO4*48*8.9*LB~\n" +
      "PO1**32*CA*12.34**CB*000666666*UA*002840044444~\n" +
      "PID*F****POTATO CHIPS~\n" +
      "PO4*72*6.78*LB~\n" +
      "PO1**51*CA*12.34**CB*000874917*UA*002840055555~\n" +
      "PID*F****CORN CHIPS~\n" +
      "PO4*48*8.9*LB~\n" +
      "PO1**9*CA*12.34**CB*000874958*UA*002840066666~\n" +
      "PID*F****BBQ CHIPS~\n" +
      "PO4*48*4.5*LB~\n" +
      "PO1**85*CA*12.34**CB*000874990*UA*002840077777~\n" +
      "PID*F****GREAT BIG CHIPS LSS~\n" +
      "PO4*48*4.56*LB~\n" +
      "PO1**1*CA*12.34**CB*000875088*UA*002840088888~\n" +
      "PID*F****MINI CHIPS LSS~\n" +
      "PO4*48*4.56*LB~\n" +
      "CTT*7~\n" +
      "SE*35*000000001~\n" +
      "GE*1*1~\n" +
      "IEA*1*000000001~\n"

  val sampleInvoice =
    "ISA*00*          *00*          *ZZ*SENDERISA      *ZZ*RECEIVERISA    *960807*1548*U*00401*000000020*0*T*>~\n" +
      "GS*IN*SENDERDEPT*007326879*19960807*1548*1*X*004010~\n" +
      "ST*810*000000001~\n" +
      "BIG*19971211*00001**A99999-01~\n" +
      "N1*ST*BUYSNACKS PORT*9*1223334445~\n" +
      "N3*1000 N. SAMPLE HIGHWAY~\n" +
      "N4*ATHENS*GA*30603~\n" +
      "N1*BT*BUYSNACKS*9*1223334444~\n" +
      "N3*P.O. BOX 0000~\n" +
      "N4*TEMPLE*TX*76503~\n" +
      "N1*RE*FOODSELLER*9*12345QQQQ~\n" +
      "N3*P.O. BOX 222222~\n" +
      "N4*DALLAS*TX*723224444~\n" +
      "ITD*01*3*1.000**15**16*****1/15 NET 30~\n" +
      "FOB*PP~\n" +
      "IT1**16*CA*12.34**UA*002840022222~\n" +
      "PID*F****CRUNCHY CHIPS LSS~\n" +
      "IT1**13*CA*12.34**UA*002840033333~\n" +
      "PID*F****NACHO CHIPS LSS~\n" +
      "IT1**32*CA*12.34**UA*002840044444~\n" +
      "PID*F****POTATO CHIPS~\n" +
      "IT1**51*CA*12.34**UA*002840055555~\n" +
      "PID*F****CORN CHIPS~\n" +
      "IT1**9*CA*12.34**UA*002840066666~\n" +
      "PID*F****BBQ CHIPS~\n" +
      "IT1**85*CA*12.34**UA*002840077777~\n" +
      "PID*F****GREAT BIG CHIPS LSS~\n" +
      "IT1**1*CA*12.34**UA*002840088888~\n" +
      "PID*F****MINI CHIPS LSS~\n" +
      "TDS*255438~\n" +
      "CAD*****FREEFORM~\n" +
      "ISS*207*CA~\n" +
      "CTT*7~\n" +
      "SE*32*000000001~\n" +
      "ST*810*000000002~\n" +
      "BIG*19971215*00001**A99999-04~\n" +
      "N1*ST*BUYER CHIPS*9*1223334445~\n" +
      "N3*1234 N. BUYER HIGHWAY~\n" +
      "N4*LOS ANGELES*CA*30603~\n" +
      "N1*BT*BUYER CHIPS*9*1223334444~\n" +
      "N3*P.O. BOX 123400~\n" +
      "N4*CARSON*CA*76503~\n" +
      "N1*RE*FOODSELLER*9*12345QQQQ~\n" +
      "N3*P.O. BOX 222222~\n" +
      "N4*DALLAS*TX*723224444~\n" +
      "ITD*01*3*1.000**15**16*****1/15 NET 30~\n" +
      "FOB*PP~\n" +
      "IT1**50*CA*12.34**UA*002840022222~\n" +
      "PID*F****CRUNCHY CHIPS LSS~\n" +
      "IT1**100*CA*12.34**UA*002840066666~\n" +
      "PID*F****BBQ CHIPS~\n" +
      "TDS*255438~\n" +
      "CAD*****FREEFORM~\n" +
      "ISS*207*CA~\n" +
      "CTT*2~\n" +
      "SE*22*000000002~\n" +
      "GE*2*1~\n" +
      "IEA*1*000000020~\n"

}
