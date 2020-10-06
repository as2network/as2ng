// automatically generated by the FlatBuffers compiler, do not modify

package com.freighttrust.as2.fb;

import com.google.flatbuffers.FlatBufferBuilder;
import com.google.flatbuffers.Table;

import java.nio.ByteBuffer;
import java.nio.ByteOrder;

@SuppressWarnings("unused")
public final class As2Mdn extends Table {
  public static As2Mdn getRootAsAs2Mdn(ByteBuffer _bb) {
    return getRootAsAs2Mdn(_bb, new As2Mdn());
  }

  public static As2Mdn getRootAsAs2Mdn(ByteBuffer _bb, As2Mdn obj) {
    _bb.order(ByteOrder.LITTLE_ENDIAN);
    return (obj.__assign(_bb.getInt(_bb.position()) + _bb.position(), _bb));
  }

  public void __init(int _i, ByteBuffer _bb) {
    bb_pos = _i;
    bb = _bb;
    vtable_start = bb_pos - bb.getInt(bb_pos);
    vtable_size = bb.getShort(vtable_start);
  }

  public As2Mdn __assign(int _i, ByteBuffer _bb) {
    __init(_i, _bb);
    return this;
  }

  public String id() {
    int o = __offset(4);
    return o != 0 ? __string(o + bb_pos) : null;
  }

  public ByteBuffer idAsByteBuffer() {
    return __vector_as_bytebuffer(4, 1);
  }

  public ByteBuffer idInByteBuffer(ByteBuffer _bb) {
    return __vector_in_bytebuffer(_bb, 4, 1);
  }

  public String messageId() {
    int o = __offset(6);
    return o != 0 ? __string(o + bb_pos) : null;
  }

  public ByteBuffer messageIdAsByteBuffer() {
    return __vector_as_bytebuffer(6, 1);
  }

  public ByteBuffer messageIdInByteBuffer(ByteBuffer _bb) {
    return __vector_in_bytebuffer(_bb, 6, 1);
  }

  public String text() {
    int o = __offset(8);
    return o != 0 ? __string(o + bb_pos) : null;
  }

  public ByteBuffer textAsByteBuffer() {
    return __vector_as_bytebuffer(8, 1);
  }

  public ByteBuffer textInByteBuffer(ByteBuffer _bb) {
    return __vector_in_bytebuffer(_bb, 8, 1);
  }

  public KeyValue headers(int j) {
    return headers(new KeyValue(), j);
  }

  public KeyValue headers(KeyValue obj, int j) {
    int o = __offset(10);
    return o != 0 ? obj.__assign(__indirect(__vector(o) + j * 4), bb) : null;
  }

  public int headersLength() {
    int o = __offset(10);
    return o != 0 ? __vector_len(o) : 0;
  }

  public KeyValue attributes(int j) {
    return attributes(new KeyValue(), j);
  }

  public KeyValue attributes(KeyValue obj, int j) {
    int o = __offset(12);
    return o != 0 ? obj.__assign(__indirect(__vector(o) + j * 4), bb) : null;
  }

  public int attributesLength() {
    int o = __offset(12);
    return o != 0 ? __vector_len(o) : 0;
  }

  public byte data(int j) {
    int o = __offset(14);
    return o != 0 ? bb.get(__vector(o) + j * 1) : 0;
  }

  public int dataLength() {
    int o = __offset(14);
    return o != 0 ? __vector_len(o) : 0;
  }

  public ByteBuffer dataAsByteBuffer() {
    return __vector_as_bytebuffer(14, 1);
  }

  public ByteBuffer dataInByteBuffer(ByteBuffer _bb) {
    return __vector_in_bytebuffer(_bb, 14, 1);
  }

  public static int createAs2Mdn(FlatBufferBuilder builder,
                                 int idOffset,
                                 int messageIdOffset,
                                 int textOffset,
                                 int headersOffset,
                                 int attributesOffset,
                                 int dataOffset) {
    builder.startObject(6);
    As2Mdn.addData(builder, dataOffset);
    As2Mdn.addAttributes(builder, attributesOffset);
    As2Mdn.addHeaders(builder, headersOffset);
    As2Mdn.addText(builder, textOffset);
    As2Mdn.addMessageId(builder, messageIdOffset);
    As2Mdn.addId(builder, idOffset);
    return As2Mdn.endAs2Mdn(builder);
  }

  public static void startAs2Mdn(FlatBufferBuilder builder) {
    builder.startObject(6);
  }

  public static void addId(FlatBufferBuilder builder, int idOffset) {
    builder.addOffset(0, idOffset, 0);
  }

  public static void addMessageId(FlatBufferBuilder builder, int messageIdOffset) {
    builder.addOffset(1, messageIdOffset, 0);
  }

  public static void addText(FlatBufferBuilder builder, int textOffset) {
    builder.addOffset(2, textOffset, 0);
  }

  public static void addHeaders(FlatBufferBuilder builder, int headersOffset) {
    builder.addOffset(3, headersOffset, 0);
  }

  public static int createHeadersVector(FlatBufferBuilder builder, int[] data) {
    builder.startVector(4, data.length, 4);
    for (int i = data.length - 1; i >= 0; i--) builder.addOffset(data[i]);
    return builder.endVector();
  }

  public static void startHeadersVector(FlatBufferBuilder builder, int numElems) {
    builder.startVector(4, numElems, 4);
  }

  public static void addAttributes(FlatBufferBuilder builder, int attributesOffset) {
    builder.addOffset(4, attributesOffset, 0);
  }

  public static int createAttributesVector(FlatBufferBuilder builder, int[] data) {
    builder.startVector(4, data.length, 4);
    for (int i = data.length - 1; i >= 0; i--) builder.addOffset(data[i]);
    return builder.endVector();
  }

  public static void startAttributesVector(FlatBufferBuilder builder, int numElems) {
    builder.startVector(4, numElems, 4);
  }

  public static void addData(FlatBufferBuilder builder, int dataOffset) {
    builder.addOffset(5, dataOffset, 0);
  }

  public static int createDataVector(FlatBufferBuilder builder, byte[] data) {
    builder.startVector(1, data.length, 1);
    for (int i = data.length - 1; i >= 0; i--) builder.addByte(data[i]);
    return builder.endVector();
  }

  public static void startDataVector(FlatBufferBuilder builder, int numElems) {
    builder.startVector(1, numElems, 1);
  }

  public static int endAs2Mdn(FlatBufferBuilder builder) {
    int o = builder.endObject();
    return o;
  }
}

