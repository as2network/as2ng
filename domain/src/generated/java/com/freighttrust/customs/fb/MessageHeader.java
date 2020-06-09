// automatically generated by the FlatBuffers compiler, do not modify

package com.freighttrust.customs.fb;

import java.nio.*;
import java.lang.*;
import java.util.*;
import com.google.flatbuffers.*;

@SuppressWarnings("unused")
public final class MessageHeader extends Table {
  public static MessageHeader getRootAsMessageHeader(ByteBuffer _bb) { return getRootAsMessageHeader(_bb, new MessageHeader()); }
  public static MessageHeader getRootAsMessageHeader(ByteBuffer _bb, MessageHeader obj) { _bb.order(ByteOrder.LITTLE_ENDIAN); return (obj.__assign(_bb.getInt(_bb.position()) + _bb.position(), _bb)); }
  public void __init(int _i, ByteBuffer _bb) { bb_pos = _i; bb = _bb; vtable_start = bb_pos - bb.getInt(bb_pos); vtable_size = bb.getShort(vtable_start); }
  public MessageHeader __assign(int _i, ByteBuffer _bb) { __init(_i, _bb); return this; }

  public String messageId() { int o = __offset(4); return o != 0 ? __string(o + bb_pos) : null; }
  public ByteBuffer messageIdAsByteBuffer() { return __vector_as_bytebuffer(4, 1); }
  public ByteBuffer messageIdInByteBuffer(ByteBuffer _bb) { return __vector_in_bytebuffer(_bb, 4, 1); }
  public byte messageType() { int o = __offset(6); return o != 0 ? bb.get(o + bb_pos) : 0; }
  public long sentDateTime() { int o = __offset(8); return o != 0 ? bb.getLong(o + bb_pos) : 0L; }
  public String transmitterId() { int o = __offset(10); return o != 0 ? __string(o + bb_pos) : null; }
  public ByteBuffer transmitterIdAsByteBuffer() { return __vector_as_bytebuffer(10, 1); }
  public ByteBuffer transmitterIdInByteBuffer(ByteBuffer _bb) { return __vector_in_bytebuffer(_bb, 10, 1); }
  public short transmitterSiteCode() { int o = __offset(12); return o != 0 ? bb.getShort(o + bb_pos) : 0; }
  public String preparerId() { int o = __offset(14); return o != 0 ? __string(o + bb_pos) : null; }
  public ByteBuffer preparerIdAsByteBuffer() { return __vector_as_bytebuffer(14, 1); }
  public ByteBuffer preparerIdInByteBuffer(ByteBuffer _bb) { return __vector_in_bytebuffer(_bb, 14, 1); }
  public short preparerSiteCode() { int o = __offset(16); return o != 0 ? bb.getShort(o + bb_pos) : 0; }
  public String comment() { int o = __offset(18); return o != 0 ? __string(o + bb_pos) : null; }
  public ByteBuffer commentAsByteBuffer() { return __vector_as_bytebuffer(18, 1); }
  public ByteBuffer commentInByteBuffer(ByteBuffer _bb) { return __vector_in_bytebuffer(_bb, 18, 1); }

  public static int createMessageHeader(FlatBufferBuilder builder,
      int messageIdOffset,
      byte messageType,
      long sentDateTime,
      int transmitterIdOffset,
      short transmitterSiteCode,
      int preparerIdOffset,
      short preparerSiteCode,
      int commentOffset) {
    builder.startObject(8);
    MessageHeader.addSentDateTime(builder, sentDateTime);
    MessageHeader.addComment(builder, commentOffset);
    MessageHeader.addPreparerId(builder, preparerIdOffset);
    MessageHeader.addTransmitterId(builder, transmitterIdOffset);
    MessageHeader.addMessageId(builder, messageIdOffset);
    MessageHeader.addPreparerSiteCode(builder, preparerSiteCode);
    MessageHeader.addTransmitterSiteCode(builder, transmitterSiteCode);
    MessageHeader.addMessageType(builder, messageType);
    return MessageHeader.endMessageHeader(builder);
  }

  public static void startMessageHeader(FlatBufferBuilder builder) { builder.startObject(8); }
  public static void addMessageId(FlatBufferBuilder builder, int messageIdOffset) { builder.addOffset(0, messageIdOffset, 0); }
  public static void addMessageType(FlatBufferBuilder builder, byte messageType) { builder.addByte(1, messageType, 0); }
  public static void addSentDateTime(FlatBufferBuilder builder, long sentDateTime) { builder.addLong(2, sentDateTime, 0L); }
  public static void addTransmitterId(FlatBufferBuilder builder, int transmitterIdOffset) { builder.addOffset(3, transmitterIdOffset, 0); }
  public static void addTransmitterSiteCode(FlatBufferBuilder builder, short transmitterSiteCode) { builder.addShort(4, transmitterSiteCode, 0); }
  public static void addPreparerId(FlatBufferBuilder builder, int preparerIdOffset) { builder.addOffset(5, preparerIdOffset, 0); }
  public static void addPreparerSiteCode(FlatBufferBuilder builder, short preparerSiteCode) { builder.addShort(6, preparerSiteCode, 0); }
  public static void addComment(FlatBufferBuilder builder, int commentOffset) { builder.addOffset(7, commentOffset, 0); }
  public static int endMessageHeader(FlatBufferBuilder builder) {
    int o = builder.endObject();
    return o;
  }
}
