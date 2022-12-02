package org.example.util;

import java.nio.ByteBuffer;

public class BufferUtils {

  public static void read(ByteBuffer buffer) {
    System.out.println("输出buffer数据,，容量大小 " + buffer.position());
    while (buffer.hasRemaining()) {
      System.out.print((char) buffer.get());
    }
  }

}
