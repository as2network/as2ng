// automatically generated by the FlatBuffers compiler, do not modify

package com.freighttrust.customs.fb;

import java.nio.*;
import java.lang.*;
import java.util.*;
import com.google.flatbuffers.*;

@SuppressWarnings("unused")
public final class Test extends Table {
  public static Test getRootAsTest(ByteBuffer _bb) { return getRootAsTest(_bb, new Test()); }
  public static Test getRootAsTest(ByteBuffer _bb, Test obj) { _bb.order(ByteOrder.LITTLE_ENDIAN); return (obj.__assign(_bb.getInt(_bb.position()) + _bb.position(), _bb)); }
  public void __init(int _i, ByteBuffer _bb) { bb_pos = _i; bb = _bb; vtable_start = bb_pos - bb.getInt(bb_pos); vtable_size = bb.getShort(vtable_start); }
  public Test __assign(int _i, ByteBuffer _bb) { __init(_i, _bb); return this; }

  public long number() { int o = __offset(4); return o != 0 ? bb.getLong(o + bb_pos) : 0L; }

  public static int createTest(FlatBufferBuilder builder,
      long number) {
    builder.startObject(1);
    Test.addNumber(builder, number);
    return Test.endTest(builder);
  }

  public static void startTest(FlatBufferBuilder builder) { builder.startObject(1); }
  public static void addNumber(FlatBufferBuilder builder, long number) { builder.addLong(0, number, 0L); }
  public static int endTest(FlatBufferBuilder builder) {
    int o = builder.endObject();
    return o;
  }
}

