package client;
import java.io.IOException;
import java.net.Socket;
import java.util.*;

public class ServerListener implements Runnable {
  private ChatFrame chatFrame;
  private Socket socket;

  public ServerListener(ChatFrame chatFrame, Socket socket) {
    this.chatFrame = chatFrame;
    this.socket = socket;
  }

  public void run() {
    try (Scanner scanner = new Scanner(socket.getInputStream())) {
      try {
        while(true) {
          String line = scanner.nextLine();

          String[] splittedLine = line.split("\\|");
          String command = splittedLine[0];
          String details = splittedLine[1];

          System.out.println("Neue Daten empfangen: " + line + " (Kommando = " + command + ", Details = " + details + ")");

          switch (command) {
            case "message" -> chatFrame.showMessage(details);
            case "users" -> {
              String[] users = details.split(",");
              chatFrame.setUsers(users);
            }
            default -> System.err.println("Verarbeitungsfehler des Kommandos \"" + command + "\"");
          }
        }
      }
      catch (NoSuchElementException e) {
        chatFrame.showErrorMessage(e, "Kein Element (in Scanner) enthalten!");
      }
    }
    catch (IOException e) {
      chatFrame.showErrorMessage(e, "Irgendwas ist in der Server-Kommunikation schief gelaufen!");
    }
    finally {
      chatFrame.showMessage("Getrennt von " + socket.getInetAddress());
    }

  }
}