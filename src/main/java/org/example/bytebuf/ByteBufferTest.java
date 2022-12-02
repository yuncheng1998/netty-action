package org.example.bytebuf;

import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.nio.ByteBuffer;
import java.nio.channels.FileChannel;

public class ByteBufferTest {

  /**
   *
   */
  public static void main(String[] args) {
    try (FileChannel fileChannel = new FileInputStream("data.txt").getChannel()) {
      ByteBuffer buffer = ByteBuffer.allocate(16);
      while (true) {
        // 从channel写入buffer中
        int read = fileChannel.read(buffer);
        // 如果没有写入，说明channel中的数据消费完
        if (read == -1) {
          break;
        }
        // 切换为读模式
        buffer.flip();
        while (buffer.hasRemaining()) {
          byte b = buffer.get();
          System.out.print((char) b);
        }
        // 切换回写模式
        buffer.compact();
      }
    } catch (IOException e) {
      e.printStackTrace();
    }
  }

}
