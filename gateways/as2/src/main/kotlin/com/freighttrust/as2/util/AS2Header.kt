package com.freighttrust.as2.util

enum class AS2Header(val key: String) {

  As2From("AS2-From"),
  As2To("AS2-To"),
  Version("AS2-Version"),
  ContentTransferEncoding("Content-Transfer-Encoding"),

  // The URL where the async MDN should be send to - limited to RFC 2822
  DispositionNotificationTo("Disposition-Notification-To"),
  From("From"),
  MessageId("Message-ID"),
  MimeVersion("Mime-Version"),

  // The URL where the async MDN should be send to
  ReceiptDeliveryOption("Receipt-Delivery-Option"),

  /**
   *Indicates whether or not the mdn should be signed and which mic algorithm to use
   * e.g.:
   * Disposition-notification-options:
  signed-receipt-protocol=optional,pkcs7-signature;
  signed-receipt-micalg=optional,sha1,md5
   */
  DispositionNotificationOptions("Disposition-Notification-Options"),


  RecipientAddress("Recipient-Address"),
  Server("Server"),
  Subject("Subject"),

  // Defined by RFC 6017
  EdiintFeatures("EDIINT-Features"),

  // disposition notification related
  ReceivedContentMIC("Received-Content-MIC"),
  Disposition("Disposition"),
  OriginalMessageID("Original-Message-ID"),
  OriginalRecipient("Original-Recipient"),
  FinalRecipient("Final-Recipient"),
  ReportingUA("Reporting-UA");

}
