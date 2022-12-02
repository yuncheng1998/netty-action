package org.example.nio;

import com.sun.xml.internal.ws.policy.privateutil.PolicyUtils.IO;
import java.io.IOException;
import java.net.InetSocketAddress;
import java.nio.ByteBuffer;
import java.nio.channels.SelectableChannel;
import java.nio.channels.SelectionKey;
import java.nio.channels.Selector;
import java.nio.channels.ServerSocketChannel;
import java.nio.channels.SocketChannel;
import java.nio.charset.Charset;
import java.util.Iterator;
import org.example.util.BufferUtils;

public class NIOServer {

  private static void split(ByteBuffer source) {
    source.flip();
    for (int i = 0; i < source.limit(); i++) {
      if (source.get(i) == '\n') {
        int length = i + 1 - source.position();
        ByteBuffer target = ByteBuffer.allocate(length);
        for (int j = 0; j < length; j++) {
          target.put(source.get());
        }

        // 切换读模式，读取target中的内容
        target.flip();
        BufferUtils.read(target);
      }
    }
    source.compact();
  }

  public static void main(String[] args) throws IOException {


    ServerSocketChannel socketChannel = ServerSocketChannel.open();
    socketChannel.bind(new InetSocketAddress(8080));
    socketChannel.configureBlocking(false);

    // 创建selector，用于管理channel，注册channel到selector上，设定只关注accept事件
    Selector selector = Selector.open();
    socketChannel.register(selector, SelectionKey.OP_ACCEPT, null);

    // 四种事件
    // connect事件：客户端侧连接建立后触发
    // accept事件：有连接请求时触发 read事件：可读事件 write：可写事件


    while (true) {
      // 阻塞方法，在没有事件发生时会阻塞，避免线程浪费
      selector.select();
      // selector作为观察者收到了主题发出的通知，开始处理所有发生的事件
      Iterator<SelectionKey> iterator = selector.selectedKeys().iterator();
      while (iterator.hasNext()) {
        SelectionKey key = iterator.next();
        // 从集合中移除待处理的事件
        iterator.remove();
        // 区分事件类型
        if (key.isAcceptable()) {
          ServerSocketChannel channel = (ServerSocketChannel) key.channel();
          SocketChannel sc = channel.accept();
          sc.configureBlocking(false);
          // 注册到selector上，监听read事件
          ByteBuffer byteBuffer = ByteBuffer.allocate(16);
          // 传入byteBuffer作为附件
          SelectionKey sKey = sc.register(selector, SelectionKey.OP_READ | SelectionKey.OP_WRITE, byteBuffer);

          StringBuilder stringBuilder = new StringBuilder();
          for (int i = 0; i < 3000000; i++) {
            stringBuilder.append("a");
          }
          ByteBuffer content = Charset.defaultCharset().encode(stringBuilder.toString());
          sc.write(content);
          if (content.hasRemaining()) {
            sKey.attach(content);
          }


        } else if (key.isReadable()) {
          try {
            SocketChannel channel = (SocketChannel) key.channel();
            ByteBuffer byteBuffer = (ByteBuffer) key.attachment();
            int read = channel.read(byteBuffer);
            // 如果返回-1说明客户端正常断开
            if (read == -1) {
              key.cancel();
            } else {
              split(byteBuffer);
              if (byteBuffer.position() == byteBuffer.limit()) {
                key.attach(resize(byteBuffer));
              }
            }
          } catch (IOException e) {
            // 如果客户端异常断开，在获得channel时出现异常，在这里cancel()来处理掉事件
            key.cancel();
          }
        } else if (key.isWritable()) {
          ByteBuffer buffer = (ByteBuffer) key.attachment();
          SocketChannel channel = (SocketChannel) key.channel();
          channel.write(buffer);
          if (!buffer.hasRemaining()) {
            key.attach(null);
            key.interestOps(key.interestOps() ^ SelectionKey.OP_WRITE);
          }
        }
      }
    }





  }

  private static ByteBuffer resize(ByteBuffer byteBuffer) {
    ByteBuffer newBuffer = ByteBuffer.allocate(byteBuffer.capacity() * 2);
    byteBuffer.flip();
    newBuffer.put(byteBuffer);
    return newBuffer;
  }

}
