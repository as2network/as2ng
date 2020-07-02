package com.freighttrust.as2.processor.receiver;

import com.freighttrust.as2.processor.receiver.net.AS2ModifiedReceiverHandler;
import com.helger.as2lib.processor.receiver.AbstractActiveNetModule;
import com.helger.as2lib.processor.receiver.net.INetModuleHandler;

import javax.annotation.Nonnull;

public class AS2ModifiedReceiverModule extends AbstractActiveNetModule {

  public AS2ModifiedReceiverModule() {
  }

  @Override
  @Nonnull
  public INetModuleHandler createHandler() {
    return new AS2ModifiedReceiverHandler(this);
  }
}
