package client;
import java.io.*;
import java.awt.*;
import javax.swing.*;
import java.awt.event.*;
import java.net.Socket;

public class ChatFrame extends JFrame {

  public static void main(String[] args) {
    ChatFrame frame = new ChatFrame();
    frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
    frame.setSize(400, 300);
    frame.setVisible(true);
  }

  private JTextArea messagesArea;
  private JTextField newMessageField;

  private Socket socket;
  private PrintStream outStream;
  private UserModel userModel;

  public ChatFrame() {
    super("ChaTTY");
    addMenu();
    addComponents();
    addWindowListener(new WindowAdapter() {

      @Override
      public void windowClosing(WindowEvent e) {
        disconnect();
      }
    });
  }

  private void addComponents() {
    
    setLayout(new GridBagLayout());
    
    messagesArea = new JTextArea();
    messagesArea.setEditable(false);
    GridBagConstraints messagesConstraints = new GridBagConstraints();
    messagesConstraints.gridx = 0;
    messagesConstraints.gridy = 0;
    messagesConstraints.fill = GridBagConstraints.BOTH;
    messagesConstraints.weightx = 1.0;
    messagesConstraints.weighty = 1.0;
    messagesConstraints.insets = new Insets(5, 5, 0, 5);
    add(new JScrollPane(messagesArea), messagesConstraints);

    userModel = new UserModel();
    JList<String> peopleList = new JList<String>(userModel);
    GridBagConstraints peopleConstraints = new GridBagConstraints();
    peopleConstraints.gridx = 1;
    peopleConstraints.gridy = 0;
    peopleConstraints.fill = GridBagConstraints.BOTH;
    peopleConstraints.insets = new Insets(5, 0, 0, 5);
    JScrollPane peopleScroll = new JScrollPane(peopleList);
    peopleScroll.setPreferredSize(new Dimension(10, 10));
    add(peopleScroll, peopleConstraints);

    newMessageField = new JTextField();
    GridBagConstraints newMessageConstraints = new GridBagConstraints();
    newMessageConstraints.gridx = 0;
    newMessageConstraints.gridy = 1;
    newMessageConstraints.fill = GridBagConstraints.BOTH;
    newMessageConstraints.insets = new Insets(5, 5, 5, 5);
    add(newMessageField, newMessageConstraints);

    JButton sendButton = new JButton("Abschicken");
    sendButton.addActionListener(new ActionListener() {

      @Override
      public void actionPerformed(ActionEvent e) {
        sendMessage();
      }

    });
    GridBagConstraints sendConstraints = new GridBagConstraints();
    sendConstraints.gridx = 1;
    sendConstraints.gridy = 1;
    sendConstraints.fill = GridBagConstraints.HORIZONTAL;
    sendConstraints.insets = new Insets(5, 0, 5, 5);
    add(sendButton, sendConstraints);

    getRootPane().setDefaultButton(sendButton);
  }

  private void addMenu() {
    JMenuBar menuBar = new JMenuBar();
    setJMenuBar(menuBar);

    JMenu chatMenu = new JMenu("Chat");
    menuBar.add(chatMenu);

    JMenuItem connectItem = new JMenuItem("Verbinden");
    connectItem.addActionListener(new ActionListener() {

      @Override
      public void actionPerformed(ActionEvent e) {
        ConnectDialog dialog = new ConnectDialog(ChatFrame.this);
        dialog.setVisible(true);
        if (!dialog.isCanceled()) {
          disconnect();
          connect(dialog.getHost(), dialog.getUser());
        }
      }

    });

    connectItem.setAccelerator(KeyStroke.getKeyStroke(KeyEvent.VK_F1, 0));
    connectItem.setMnemonic('v');
    chatMenu.add(connectItem);

    JMenuItem disconnectItem = new JMenuItem("Trennen");
    disconnectItem.addActionListener(new ActionListener() {
      @Override
      public void actionPerformed(ActionEvent event) {
        disconnect();
      }

    });

    disconnectItem.setAccelerator(KeyStroke.getKeyStroke(KeyEvent.VK_F2, 0));
    chatMenu.add(disconnectItem);
    
    chatMenu.addSeparator();
    
    JMenuItem exitItem = new JMenuItem("Beenden");
    exitItem.addActionListener(new ActionListener() {
      
      @Override
      public void actionPerformed(ActionEvent e) {
        dispose();
      }
    });

    chatMenu.add(exitItem);
  }

  private void connect(final String host, String user) {
    try {
      socket = new Socket(host, 5000);
      showMessage("Verbunden mit " + socket.getInetAddress());
      ServerListener runnable = new ServerListener(this, socket);
      Thread listenThread = new Thread(runnable);
      listenThread.start();
      outStream = new PrintStream(socket.getOutputStream());
      outStream.println(user);
    }
    catch (IOException e) {
      showErrorMessage(e, "Fehler beim Verbinden");
    }
  }

  private void disconnect() {
    if (socket == null)
      return;
    try {
      outStream.close();
      socket.close();
    }
    catch (IOException e) {
      showErrorMessage(e, "Fehler beim Trennen der Verbindung");
    }
  }

  private void showErrorMessage(String title, Object message) {
    JOptionPane.showMessageDialog(this, message, title, JOptionPane.ERROR_MESSAGE);
  }

  public void showErrorMessage(Throwable throwable, String title) {
    StringWriter stringWriter = new StringWriter();
    PrintWriter printWriter = new PrintWriter(stringWriter);
    throwable.printStackTrace(printWriter);
    JTextArea textArea = new JTextArea(stringWriter.toString(), 10, 100);
    showErrorMessage(title, new JScrollPane(textArea));
  }

  public void showMessage(String message) {
    messagesArea.append(message + "\n");
  }

  private void sendMessage() {
    if (socket == null) {
      showErrorMessage("Verbindung fehlt", "Bitte mit dem Server verbinden!");
      return;
    }

    String message = newMessageField.getText();
    outStream.println(message);
    newMessageField.setText("");
  }

  public void setUsers(String[] users) {
    userModel.setUsers(users);
  }

}
