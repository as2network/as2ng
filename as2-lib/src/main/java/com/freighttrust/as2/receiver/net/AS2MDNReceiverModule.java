package com.freighttrust.as2.receiver.net;

import com.helger.as2lib.processor.receiver.AbstractActiveNetModule;

import javax.annotation.Nonnull;

public class AS2MDNReceiverModule extends AbstractActiveNetModule {
  public AS2MDNReceiverModule() {
  }

  @Override
  @Nonnull
  public AS2MDNReceiverHandler createHandler() {
    return new AS2MDNReceiverHandler(this);
  }
}
