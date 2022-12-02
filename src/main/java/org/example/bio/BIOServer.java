package org.example.bio;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.net.ServerSocket;
import java.net.Socket;

public class BIOServer {

  public static final int PORT = 8080;

  public static final String END = "Done";

  public static void main(String[] args) throws IOException {
    ServerSocket serverSocket = new ServerSocket(PORT);
    Socket clientSocket = serverSocket.accept();
    BufferedReader reader = new BufferedReader(new InputStreamReader(clientSocket.getInputStream()));
    PrintWriter writer = new PrintWriter(clientSocket.getOutputStream(), true);
    String request;
    while ((request = reader.readLine()) != null) {
      if (END.equals(request)) {
        break;
      }
      System.out.println("接收请求: " + request);
      String response = "process --> " + request;
      writer.println(response);
    }
  }
}
