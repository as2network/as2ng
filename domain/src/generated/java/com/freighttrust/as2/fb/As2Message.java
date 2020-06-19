// automatically generated by the FlatBuffers compiler, do not modify

package com.freighttrust.as2.fb;

import java.nio.*;
import java.lang.*;
import java.util.*;
import com.google.flatbuffers.*;

@SuppressWarnings("unused")
public final class As2Message extends Table {
  public static As2Message getRootAsAs2Message(ByteBuffer _bb) { return getRootAsAs2Message(_bb, new As2Message()); }
  public static As2Message getRootAsAs2Message(ByteBuffer _bb, As2Message obj) { _bb.order(ByteOrder.LITTLE_ENDIAN); return (obj.__assign(_bb.getInt(_bb.position()) + _bb.position(), _bb)); }
  public void __init(int _i, ByteBuffer _bb) { bb_pos = _i; bb = _bb; vtable_start = bb_pos - bb.getInt(bb_pos); vtable_size = bb.getShort(vtable_start); }
  public As2Message __assign(int _i, ByteBuffer _bb) { __init(_i, _bb); return this; }

  public String id() { int o = __offset(4); return o != 0 ? __string(o + bb_pos) : null; }
  public ByteBuffer idAsByteBuffer() { return __vector_as_bytebuffer(4, 1); }
  public ByteBuffer idInByteBuffer(ByteBuffer _bb) { return __vector_in_bytebuffer(_bb, 4, 1); }
  public String from() { int o = __offset(6); return o != 0 ? __string(o + bb_pos) : null; }
  public ByteBuffer fromAsByteBuffer() { return __vector_as_bytebuffer(6, 1); }
  public ByteBuffer fromInByteBuffer(ByteBuffer _bb) { return __vector_in_bytebuffer(_bb, 6, 1); }
  public String to() { int o = __offset(8); return o != 0 ? __string(o + bb_pos) : null; }
  public ByteBuffer toAsByteBuffer() { return __vector_as_bytebuffer(8, 1); }
  public ByteBuffer toInByteBuffer(ByteBuffer _bb) { return __vector_in_bytebuffer(_bb, 8, 1); }
  public String subject() { int o = __offset(10); return o != 0 ? __string(o + bb_pos) : null; }
  public ByteBuffer subjectAsByteBuffer() { return __vector_as_bytebuffer(10, 1); }
  public ByteBuffer subjectInByteBuffer(ByteBuffer _bb) { return __vector_in_bytebuffer(_bb, 10, 1); }
  public String protocol() { int o = __offset(12); return o != 0 ? __string(o + bb_pos) : null; }
  public ByteBuffer protocolAsByteBuffer() { return __vector_as_bytebuffer(12, 1); }
  public ByteBuffer protocolInByteBuffer(ByteBuffer _bb) { return __vector_in_bytebuffer(_bb, 12, 1); }
  public String contentType() { int o = __offset(14); return o != 0 ? __string(o + bb_pos) : null; }
  public ByteBuffer contentTypeAsByteBuffer() { return __vector_as_bytebuffer(14, 1); }
  public ByteBuffer contentTypeInByteBuffer(ByteBuffer _bb) { return __vector_in_bytebuffer(_bb, 14, 1); }
  public String contentDisposition() { int o = __offset(16); return o != 0 ? __string(o + bb_pos) : null; }
  public ByteBuffer contentDispositionAsByteBuffer() { return __vector_as_bytebuffer(16, 1); }
  public ByteBuffer contentDispositionInByteBuffer(ByteBuffer _bb) { return __vector_in_bytebuffer(_bb, 16, 1); }
  public KeyValue headers(int j) { return headers(new KeyValue(), j); }
  public KeyValue headers(KeyValue obj, int j) { int o = __offset(18); return o != 0 ? obj.__assign(__indirect(__vector(o) + j * 4), bb) : null; }
  public int headersLength() { int o = __offset(18); return o != 0 ? __vector_len(o) : 0; }
  public KeyValue attributes(int j) { return attributes(new KeyValue(), j); }
  public KeyValue attributes(KeyValue obj, int j) { int o = __offset(20); return o != 0 ? obj.__assign(__indirect(__vector(o) + j * 4), bb) : null; }
  public int attributesLength() { int o = __offset(20); return o != 0 ? __vector_len(o) : 0; }
  public byte data(int j) { int o = __offset(22); return o != 0 ? bb.get(__vector(o) + j * 1) : 0; }
  public int dataLength() { int o = __offset(22); return o != 0 ? __vector_len(o) : 0; }
  public ByteBuffer dataAsByteBuffer() { return __vector_as_bytebuffer(22, 1); }
  public ByteBuffer dataInByteBuffer(ByteBuffer _bb) { return __vector_in_bytebuffer(_bb, 22, 1); }

  public static int createAs2Message(FlatBufferBuilder builder,
      int idOffset,
      int fromOffset,
      int toOffset,
      int subjectOffset,
      int protocolOffset,
      int contentTypeOffset,
      int contentDispositionOffset,
      int headersOffset,
      int attributesOffset,
      int dataOffset) {
    builder.startObject(10);
    As2Message.addData(builder, dataOffset);
    As2Message.addAttributes(builder, attributesOffset);
    As2Message.addHeaders(builder, headersOffset);
    As2Message.addContentDisposition(builder, contentDispositionOffset);
    As2Message.addContentType(builder, contentTypeOffset);
    As2Message.addProtocol(builder, protocolOffset);
    As2Message.addSubject(builder, subjectOffset);
    As2Message.addTo(builder, toOffset);
    As2Message.addFrom(builder, fromOffset);
    As2Message.addId(builder, idOffset);
    return As2Message.endAs2Message(builder);
  }

  public static void startAs2Message(FlatBufferBuilder builder) { builder.startObject(10); }
  public static void addId(FlatBufferBuilder builder, int idOffset) { builder.addOffset(0, idOffset, 0); }
  public static void addFrom(FlatBufferBuilder builder, int fromOffset) { builder.addOffset(1, fromOffset, 0); }
  public static void addTo(FlatBufferBuilder builder, int toOffset) { builder.addOffset(2, toOffset, 0); }
  public static void addSubject(FlatBufferBuilder builder, int subjectOffset) { builder.addOffset(3, subjectOffset, 0); }
  public static void addProtocol(FlatBufferBuilder builder, int protocolOffset) { builder.addOffset(4, protocolOffset, 0); }
  public static void addContentType(FlatBufferBuilder builder, int contentTypeOffset) { builder.addOffset(5, contentTypeOffset, 0); }
  public static void addContentDisposition(FlatBufferBuilder builder, int contentDispositionOffset) { builder.addOffset(6, contentDispositionOffset, 0); }
  public static void addHeaders(FlatBufferBuilder builder, int headersOffset) { builder.addOffset(7, headersOffset, 0); }
  public static int createHeadersVector(FlatBufferBuilder builder, int[] data) { builder.startVector(4, data.length, 4); for (int i = data.length - 1; i >= 0; i--) builder.addOffset(data[i]); return builder.endVector(); }
  public static void startHeadersVector(FlatBufferBuilder builder, int numElems) { builder.startVector(4, numElems, 4); }
  public static void addAttributes(FlatBufferBuilder builder, int attributesOffset) { builder.addOffset(8, attributesOffset, 0); }
  public static int createAttributesVector(FlatBufferBuilder builder, int[] data) { builder.startVector(4, data.length, 4); for (int i = data.length - 1; i >= 0; i--) builder.addOffset(data[i]); return builder.endVector(); }
  public static void startAttributesVector(FlatBufferBuilder builder, int numElems) { builder.startVector(4, numElems, 4); }
  public static void addData(FlatBufferBuilder builder, int dataOffset) { builder.addOffset(9, dataOffset, 0); }
  public static int createDataVector(FlatBufferBuilder builder, byte[] data) { builder.startVector(1, data.length, 1); for (int i = data.length - 1; i >= 0; i--) builder.addByte(data[i]); return builder.endVector(); }
  public static void startDataVector(FlatBufferBuilder builder, int numElems) { builder.startVector(1, numElems, 1); }
  public static int endAs2Message(FlatBufferBuilder builder) {
    int o = builder.endObject();
    return o;
  }
}

