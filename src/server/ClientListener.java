package server;
import java.io.*;
import java.net.Socket;
import java.util.Scanner;

public class ClientListener implements Runnable {

  private Socket socket;
  private ChatServer server;

  public ClientListener(Socket socket, ChatServer server) {
    this.socket = socket;
    this.server = server;
  }

  @Override
  public void run() {
    try (Scanner scanner = new Scanner(socket.getInputStream());
      PrintStream outStream = new PrintStream(socket.getOutputStream())) {
      String user = scanner.nextLine();
      System.out.println("Benutzername von " + socket.getInetAddress() + " ist " + user);
      server.register(user, outStream);

      while (scanner.hasNextLine()) {
        System.out.println("Warte auf die Nachricht von " + user);
        String message = scanner.nextLine();
        server.sendMessage(user + ": " + message);
      }
      server.removeUser(user);
    }
    catch (IOException e) {
      e.printStackTrace();
    }
    finally {
      System.out.println("Verbindung mit Client " + socket.getInetAddress() + " getrennt!");
    }

  }
}
