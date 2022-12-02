package org.example.bio;

import java.io.IOException;
import java.io.OutputStream;
import java.io.PrintStream;
import java.net.Socket;
import java.util.Scanner;

public class BIOClient {

  public static void main(String[] args) throws IOException {

    Socket socket = new Socket("127.0.0.1",8080);
    OutputStream outputStream = socket.getOutputStream();
    PrintStream printStream = new PrintStream(outputStream);
    Scanner sc = new Scanner(System.in);
    while (true){
      System.out.print("这里说：");
      printStream.println(sc.nextLine());
      printStream.flush();
    }
  }



}
