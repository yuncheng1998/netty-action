package org.example.nio;

import java.io.IOException;
import java.net.InetSocketAddress;
import java.net.SocketAddress;
import java.nio.ByteBuffer;
import java.nio.channels.SocketChannel;
import java.nio.charset.Charset;

public class NIOClient {

  public static void main(String[] args) throws IOException {
    SocketChannel channel = SocketChannel.open();
    channel.connect(new InetSocketAddress("localhost", 8080));
    receiveData(channel);
  }

  private static void sendData(SocketChannel channel) throws IOException {
    channel.write(Charset.defaultCharset().encode("012345678abcdefghijk\n"));
    channel.write(Charset.defaultCharset().encode("XXXX012345678abcdefghijk\n"));
  }

  private static void receiveData(SocketChannel channel) throws IOException {
    int sum = 0;
    while (true) {
      ByteBuffer buffer = ByteBuffer.allocate(1024 * 1024);
      int read = channel.read(buffer);
      sum += read;
      System.out.println("一次读取的字节数：" + read + "，总读取的字节数：" + sum);
      buffer.clear();
    }

  }

}
