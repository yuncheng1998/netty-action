package org.example.channel;

import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.nio.channels.FileChannel;

public class FileChannelTest {

  public static void main(String[] args) {
    try (FileChannel from = new FileInputStream("data.txt").getChannel();
        FileChannel to = new FileOutputStream("to.txt").getChannel()) {
      long size = from.size();
      long left = size;
      while (left > 0) {
        left = left - from.transferTo(size - left, left, to);
      }
    } catch (IOException e) {
      e.printStackTrace();
    }
  }

}
