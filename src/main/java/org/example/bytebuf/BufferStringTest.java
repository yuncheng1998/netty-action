package org.example.bytebuf;

import java.nio.Buffer;
import java.nio.ByteBuffer;
import java.nio.charset.StandardCharsets;

public class BufferStringTest {

  private static void str2BufferFunc1(String str) {
    ByteBuffer buffer = ByteBuffer.allocate(16);
    buffer.put(str.getBytes(StandardCharsets.UTF_8));
  }

  private static void str2BufferFunc2(String str) {
    ByteBuffer buffer = StandardCharsets.UTF_8.encode(str);
  }

  private static void str2BufferFunc3(String str) {
    ByteBuffer buffer = ByteBuffer.wrap(str.getBytes(StandardCharsets.UTF_8));
  }

  private static void buffer2Str(ByteBuffer buffer) {
    String s = StandardCharsets.UTF_8.decode(buffer).toString();
  }

}
