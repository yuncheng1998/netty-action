package org.example.bio;

import java.io.IOException;
import java.net.InetSocketAddress;
import java.nio.ByteBuffer;
import java.nio.channels.ServerSocketChannel;
import java.nio.channels.SocketChannel;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;
import org.example.util.BufferUtils;

public class Server {

  public static void main(String[] args) throws IOException {
    ByteBuffer buffer = ByteBuffer.allocate(16);
    // 创建Socket服务器，绑定8080端口
    ServerSocketChannel ssc = ServerSocketChannel.open();
    // 设置为非阻塞模式，这种情况accept是非阻塞的，可能返回null
    ssc.configureBlocking(false);
    ssc.bind(new InetSocketAddress(8080));
    // 创建channel集合
    List<SocketChannel> channels = new ArrayList<>();
    while (true) {
      // accept方法建立连接，获得channel与客户端通信
      SocketChannel socketChannel = ssc.accept();
      // 非阻塞情况下，accept方法可能返回null，这种情况会线程空转
      if (Objects.isNull(socketChannel)) {
        continue;
      }
      socketChannel.configureBlocking(false);
      channels.add(socketChannel);
      // 处理
      for (SocketChannel channel : channels) {
        int read = channel.read(buffer);
        if (read == 0) {
          continue;
        }
        buffer.flip();
        BufferUtils.read(buffer);
        buffer.compact();
      }
    }

  }

}
