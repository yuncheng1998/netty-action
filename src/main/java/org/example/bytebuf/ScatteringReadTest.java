package org.example.bytebuf;

import java.io.FileInputStream;
import java.io.IOException;
import java.nio.ByteBuffer;
import java.nio.channels.FileChannel;
import java.nio.charset.StandardCharsets;

public class ScatteringReadTest {

  /**
   * 分散读取
   */
  public static void main(String[] args) {
    try (FileChannel fileChannel = new FileInputStream("data.txt").getChannel()) {
      ByteBuffer b1 = ByteBuffer.allocate(10);
      ByteBuffer b2 = ByteBuffer.allocate(10);
      ByteBuffer b3 = ByteBuffer.allocate(10);
      fileChannel.read(new ByteBuffer[]{b1, b2, b3});
      b1.flip(); // 注意一定要切换为读模式
      b2.flip();
      b3.flip();
      System.out.println(StandardCharsets.UTF_8.decode(b1));
      System.out.println(StandardCharsets.UTF_8.decode(b2));
      System.out.println(StandardCharsets.UTF_8.decode(b3));
    } catch (IOException e) {
      e.printStackTrace();
    }
  }
}
