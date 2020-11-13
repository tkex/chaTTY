package client;
import java.awt.*;
import java.awt.event.*;
import javax.swing.*;

public class ConnectDialog extends JDialog {

  private boolean canceled;
  private JTextField hostField;
  private JTextField userField;

  public ConnectDialog(Frame parent) {
    super(parent);
    setTitle("Verbinden");

    addContentPanel();
    addButtonPanel();

    addWindowListener(new WindowAdapter() {
      @Override
      public void windowClosing(WindowEvent e) {
        canceled = true;
      }
    });

    setResizable(false);
    setModal(true);
    pack();
  }

  private void addButtonPanel() {
    JPanel buttonPanel = new JPanel();
    buttonPanel.setLayout(new FlowLayout(FlowLayout.RIGHT));
    add(buttonPanel, BorderLayout.SOUTH);

    JButton connectButton = new JButton("Verbinden");
    connectButton.addActionListener(new ActionListener() {

      @Override
      public void actionPerformed(ActionEvent e) {
        dispose();
      }
    });
    buttonPanel.add(connectButton);
    getRootPane().setDefaultButton(connectButton);

    JButton cancelButton = new JButton("Abbrechen");
    cancelButton.addActionListener(new ActionListener() {

      @Override
      public void actionPerformed(ActionEvent e) {
        canceled = true;
        dispose();
      }

    });
    buttonPanel.add(cancelButton);
  }

  private void addContentPanel() {
    JPanel contentPanel = new JPanel();
    contentPanel.setLayout(new GridBagLayout());
    add(contentPanel);

    GridBagConstraints userLabelConstraints = new GridBagConstraints();
    userLabelConstraints.gridx = 0;
    userLabelConstraints.gridy = 0;
    userLabelConstraints.anchor = GridBagConstraints.WEST;
    userLabelConstraints.insets = new Insets(5, 5, 5, 5);
    contentPanel.add(new JLabel("Benutzername: "), userLabelConstraints);

    GridBagConstraints userFieldConstraints = new GridBagConstraints();
    userFieldConstraints.gridx = 1;
    userFieldConstraints.gridy = 0;
    userFieldConstraints.fill = GridBagConstraints.BOTH;
    userFieldConstraints.insets = new Insets(5, 5, 5, 5);
    userFieldConstraints.weightx = 1.0;
    userField = new JTextField();
    contentPanel.add(userField, userFieldConstraints);

    GridBagConstraints hostLabelConstraints = new GridBagConstraints();
    hostLabelConstraints.gridx = 0;
    hostLabelConstraints.gridy = 1;
    hostLabelConstraints.anchor = GridBagConstraints.WEST;
    hostLabelConstraints.insets = new Insets(0, 5, 5, 5);
    contentPanel.add(new JLabel("Server: "), hostLabelConstraints);

    GridBagConstraints hostFieldConstraints = new GridBagConstraints();
    hostFieldConstraints.gridx = 1;
    hostFieldConstraints.gridy = 1;
    hostFieldConstraints.insets = new Insets(0, 5, 5, 5);
    hostFieldConstraints.fill = GridBagConstraints.BOTH;
    hostField = new JTextField("localhost");
    contentPanel.add(hostField, hostFieldConstraints);
  }

  public String getUser() {
    return userField.getText();
  }

  public String getHost() {
    return hostField.getText();
  }

  public boolean isCanceled() {
    return canceled;
  }

}
