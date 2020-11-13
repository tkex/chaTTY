package server;
import java.io.*;
import java.net.*;
import java.util.*;

public class ChatServer {

  public static void main(String[] args) {
    ChatServer server = new ChatServer();
    server.start();
  }

  private Map<String, PrintStream> outputStream = new TreeMap<>();

  public void removeUser(String user) {
    synchronized (outputStream) {
      outputStream.remove(user);
    }
    sendUsers();
  }

  public void sendMessage(String message) {
    String command = "message|" + message;
    sendToClients(command);
  }

  private void sendUsers() {
    Set<String> users = outputStream.keySet();
    StringBuffer command = new StringBuffer("users|");
    for (Iterator<String> it = users.iterator(); it.hasNext();) {
      command.append(it.next());
      if (it.hasNext()) {
        command.append(",");
      }
    }
    sendToClients(command.toString());
  }

  public void register(String user, PrintStream outStream) {
    synchronized (outputStream) {
      outputStream.put(user, outStream);
    }
    sendUsers();
  }

  private void sendToClients(String command) {
    synchronized (outputStream) {
      for (PrintStream outStream : outputStream.values()) {
        outStream.println(command);
      }
    }
  }

  private void start() {
    System.out.println("Server wurde gestartet!");
    try (ServerSocket serverSocket = new ServerSocket(5000)) {
      while (true) {
        System.out.println("Warte auf Verbindungen...");
        Socket socket = serverSocket.accept();
        System.out.println("Neue Verbindung mit dem Client " + socket.getInetAddress());
        ClientListener runnable = new ClientListener(socket, this);
        Thread thread = new Thread(runnable);
        thread.start();
      }
    }
    catch (IOException e) {
      e.printStackTrace();
    }
    System.out.println("Server wurde beendet!");
  }

}
