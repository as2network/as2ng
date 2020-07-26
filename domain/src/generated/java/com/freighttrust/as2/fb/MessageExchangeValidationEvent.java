// automatically generated by the FlatBuffers compiler, do not modify

package com.freighttrust.as2.fb;

import com.google.flatbuffers.FlatBufferBuilder;
import com.google.flatbuffers.Table;

import java.nio.ByteBuffer;
import java.nio.ByteOrder;

@SuppressWarnings("unused")
public final class MessageExchangeValidationEvent extends Table {
  public static MessageExchangeValidationEvent getRootAsMessageExchangeValidationEvent(ByteBuffer _bb) { return getRootAsMessageExchangeValidationEvent(_bb, new MessageExchangeValidationEvent()); }
  public static MessageExchangeValidationEvent getRootAsMessageExchangeValidationEvent(ByteBuffer _bb, MessageExchangeValidationEvent obj) { _bb.order(ByteOrder.LITTLE_ENDIAN); return (obj.__assign(_bb.getInt(_bb.position()) + _bb.position(), _bb)); }
  public void __init(int _i, ByteBuffer _bb) { bb_pos = _i; bb = _bb; vtable_start = bb_pos - bb.getInt(bb_pos); vtable_size = bb.getShort(vtable_start); }
  public MessageExchangeValidationEvent __assign(int _i, ByteBuffer _bb) { __init(_i, _bb); return this; }

  public long senderId() { int o = __offset(4); return o != 0 ? bb.getLong(o + bb_pos) : 0L; }
  public long recipientId() { int o = __offset(6); return o != 0 ? bb.getLong(o + bb_pos) : 0L; }
  public String messageId() { int o = __offset(8); return o != 0 ? __string(o + bb_pos) : null; }
  public ByteBuffer messageIdAsByteBuffer() { return __vector_as_bytebuffer(8, 1); }
  public ByteBuffer messageIdInByteBuffer(ByteBuffer _bb) { return __vector_in_bytebuffer(_bb, 8, 1); }
  public String subject() { int o = __offset(10); return o != 0 ? __string(o + bb_pos) : null; }
  public ByteBuffer subjectAsByteBuffer() { return __vector_as_bytebuffer(10, 1); }
  public ByteBuffer subjectInByteBuffer(ByteBuffer _bb) { return __vector_in_bytebuffer(_bb, 10, 1); }

  public static int createMessageExchangeValidationEvent(FlatBufferBuilder builder,
      long senderId,
      long recipientId,
      int messageIdOffset,
      int subjectOffset) {
    builder.startObject(4);
    MessageExchangeValidationEvent.addRecipientId(builder, recipientId);
    MessageExchangeValidationEvent.addSenderId(builder, senderId);
    MessageExchangeValidationEvent.addSubject(builder, subjectOffset);
    MessageExchangeValidationEvent.addMessageId(builder, messageIdOffset);
    return MessageExchangeValidationEvent.endMessageExchangeValidationEvent(builder);
  }

  public static void startMessageExchangeValidationEvent(FlatBufferBuilder builder) { builder.startObject(4); }
  public static void addSenderId(FlatBufferBuilder builder, long senderId) { builder.addLong(0, senderId, 0L); }
  public static void addRecipientId(FlatBufferBuilder builder, long recipientId) { builder.addLong(1, recipientId, 0L); }
  public static void addMessageId(FlatBufferBuilder builder, int messageIdOffset) { builder.addOffset(2, messageIdOffset, 0); }
  public static void addSubject(FlatBufferBuilder builder, int subjectOffset) { builder.addOffset(3, subjectOffset, 0); }
  public static int endMessageExchangeValidationEvent(FlatBufferBuilder builder) {
    int o = builder.endObject();
    return o;
  }
}

