package com.freighttrust.as2.receiver.net;

import javax.annotation.Nonnull;

import com.helger.as2lib.processor.receiver.AbstractActiveNetModule;
import com.helger.as2lib.processor.receiver.net.AS2MDNReceiverHandler;

public class FixedAS2MDNReceiverModule extends AbstractActiveNetModule
{
  public FixedAS2MDNReceiverModule()
  {}

  @Override
  @Nonnull
  public FixedAS2MDNReceiverHandler createHandler ()
  {
    return new FixedAS2MDNReceiverHandler (this);
  }
}
