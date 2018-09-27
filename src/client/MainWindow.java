/**
 * Name: David Setyanugraha, Student Id: 867585
 */


package client;

import java.awt.EventQueue;
import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.Insets;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.IOException;
import java.net.Socket;
import java.net.UnknownHostException;
import java.util.ArrayList;
import java.util.List;
import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JTextArea;
import javax.swing.JTextField;
import exception.ClientException;

public class MainWindow {

  private JFrame frame;
  private JTextArea masterTextArea;
  private JButton addButton;
  private JButton deleteButton;
  private JButton searchButton;
  private JLabel statusLabel;
  private static Socket socket;
  private static Client client;
  private static String onlineStatus;
  private static final String[] status = {"Disconnected", "Connected"};
  private static int numberAction;

  /**
   * Launch the application.
   */
  public static void main(String[] args) {

    try {
      initConnection(args);
      onlineStatus = status[1];
    } catch (IOException e) {
      onlineStatus = status[0];
    }

    EventQueue.invokeLater(new Runnable() {
      public void run() {
        try {
          MainWindow window = new MainWindow();
          window.frame.setVisible(true);
        } catch (Exception e) {
          e.printStackTrace();
        }
      }
    });
  }

  private static void initConnection(String[] args) throws UnknownHostException, IOException {
    String hostname = args[0];
    int port = Integer.parseInt(args[1]);

    socket = new Socket(hostname, port);
    client = new Client(socket);
  }

  /**
   * Create the application.
   */
  public MainWindow() {
    initialize();
  }

  /**
   * Initialize the contents of the frame.
   */
  private void initialize() {
    frame = new JFrame();
    frame.setBounds(100, 100, 800, 600);
    frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
    frame.setTitle("Dictionary");
    GridBagLayout gridBagLayout = new GridBagLayout();
    frame.getContentPane().setLayout(gridBagLayout);

    statusLabel = new JLabel();
    statusLabel.setText("Status: " + onlineStatus);
    GridBagConstraints gbc_textLable = new GridBagConstraints();
    gbc_textLable.insets = new Insets(0, 10, 5, 5);
    gbc_textLable.fill = GridBagConstraints.HORIZONTAL;
    gbc_textLable.gridx = 2;
    gbc_textLable.gridy = 0;
    frame.getContentPane().add(statusLabel, gbc_textLable);

    masterTextArea = new JTextArea();
    masterTextArea.setEditable(false);
    GridBagConstraints gbc_textArea = new GridBagConstraints();
    gbc_textArea.insets = new Insets(10, 10, 10, 10);
    gbc_textArea.fill = GridBagConstraints.BOTH;
    gbc_textArea.gridx = 2;
    gbc_textArea.gridy = 1;
    gbc_textArea.weightx = 5;
    gbc_textArea.gridheight = 20;
    frame.getContentPane().add(masterTextArea, gbc_textArea);
    masterTextArea.setRows(10);
    masterTextArea.setLineWrap(true);
    masterTextArea.setWrapStyleWord(true);

    search();
    add();
    delete();

    if (onlineStatus == status[0]) {
      trigger(status[0]);
    }
  }

  private void trigger(String newStatus) {
    numberAction++;
    onlineStatus = newStatus;

    if (onlineStatus == status[0]) {
      masterTextArea.setText("Please check your server connection! \n");
      addButton.setEnabled(false);
      deleteButton.setEnabled(false);
      searchButton.setEnabled(false);
    } else {
      addButton.setEnabled(true);
      deleteButton.setEnabled(true);
      searchButton.setEnabled(true);
    }

    statusLabel.setText("Status: " + onlineStatus);
  }

  private void search() {
    JLabel searchLabel = new JLabel();
    searchLabel.setText("Search Meaning");
    GridBagConstraints gbc_textLable = new GridBagConstraints();
    gbc_textLable.insets = new Insets(0, 10, 5, 5);
    gbc_textLable.fill = GridBagConstraints.HORIZONTAL;
    gbc_textLable.gridx = 0;
    gbc_textLable.gridy = 1;
    frame.getContentPane().add(searchLabel, gbc_textLable);

    JTextField searchTextField = new JTextField();

    GridBagConstraints gbc_textField = new GridBagConstraints();
    gbc_textField.insets = new Insets(0, 10, 5, 5);
    gbc_textField.weightx = 0.5;
    gbc_textField.fill = GridBagConstraints.HORIZONTAL;
    gbc_textField.gridx = 0;
    gbc_textField.gridy = 2;
    frame.getContentPane().add(searchTextField, gbc_textField);
    searchTextField.setColumns(10);

    searchButton = new JButton("Search");
    GridBagConstraints gbc_btnNewButton = new GridBagConstraints();
    gbc_btnNewButton.insets = new Insets(0, 0, 5, 0);
    gbc_btnNewButton.gridx = 1;
    gbc_btnNewButton.gridy = 2;
    frame.getContentPane().add(searchButton, gbc_btnNewButton);

    searchButton.addActionListener(new ActionListener() {
      public void actionPerformed(ActionEvent arg0) {
        clearIfNumberActionHigh();
        String word = searchTextField.getText();
        System.out.println("Search clicked! Searching for " + word + "....");

        try {
          String response = client.send(Client.search, word);
          masterTextArea.append(">> " + response + "\n \n");
          trigger(status[1]);
        } catch (ClientException e) {
          masterTextArea.append(e.getMessage() + "\n \n");
          trigger(status[1]);
        } catch (IOException e) {
          trigger(status[0]);
        }
      }
    });

  }

  private void add() {
    JLabel addLabel = new JLabel();
    addLabel.setText("Add Word");
    GridBagConstraints gbc_textLable = new GridBagConstraints();
    gbc_textLable.insets = new Insets(0, 10, 5, 5);
    gbc_textLable.fill = GridBagConstraints.HORIZONTAL;
    gbc_textLable.gridx = 0;
    gbc_textLable.gridy = 3;
    frame.getContentPane().add(addLabel, gbc_textLable);

    JTextField searchTextField = new JTextField();
    GridBagConstraints gbc_textField = new GridBagConstraints();
    gbc_textField.insets = new Insets(0, 10, 5, 5);
    gbc_textField.fill = GridBagConstraints.HORIZONTAL;
    gbc_textField.gridx = 0;
    gbc_textField.gridy = 4;
    frame.getContentPane().add(searchTextField, gbc_textField);
    searchTextField.setColumns(10);

    ArrayList<JTextField> listMeanings = new ArrayList<JTextField>();

    // meaning 1
    JTextField meaningTextField1 = new JTextField();
    GridBagConstraints gbc_meaning1TextField = new GridBagConstraints();
    gbc_meaning1TextField.insets = new Insets(0, 10, 5, 5);
    gbc_meaning1TextField.fill = GridBagConstraints.HORIZONTAL;
    gbc_meaning1TextField.gridx = 0;
    gbc_meaning1TextField.gridy = 5;
    frame.getContentPane().add(meaningTextField1, gbc_meaning1TextField);
    meaningTextField1.setColumns(10);
    listMeanings.add(meaningTextField1);

    // meaning 2
    JTextField meaningTextField2 = new JTextField();
    GridBagConstraints gbc_meaning2TextField = new GridBagConstraints();
    gbc_meaning2TextField.insets = new Insets(0, 10, 5, 5);
    gbc_meaning2TextField.fill = GridBagConstraints.HORIZONTAL;
    gbc_meaning2TextField.gridx = 0;
    gbc_meaning2TextField.gridy = 6;
    frame.getContentPane().add(meaningTextField2, gbc_meaning2TextField);
    meaningTextField2.setColumns(10);
    listMeanings.add(meaningTextField2);

    // meaning 3
    JTextField meaningTextField3 = new JTextField();
    GridBagConstraints gbc_meaning3TextField = new GridBagConstraints();
    gbc_meaning3TextField.insets = new Insets(0, 10, 5, 5);
    gbc_meaning3TextField.fill = GridBagConstraints.HORIZONTAL;
    gbc_meaning3TextField.gridx = 0;
    gbc_meaning3TextField.gridy = 7;
    frame.getContentPane().add(meaningTextField3, gbc_meaning3TextField);
    meaningTextField3.setColumns(10);
    listMeanings.add(meaningTextField3);

    // meaning 4
    JTextField meaningTextField4 = new JTextField();
    GridBagConstraints gbc_meaning4TextField = new GridBagConstraints();
    gbc_meaning4TextField.insets = new Insets(0, 10, 5, 5);
    gbc_meaning4TextField.fill = GridBagConstraints.HORIZONTAL;
    gbc_meaning4TextField.gridx = 0;
    gbc_meaning4TextField.gridy = 8;
    frame.getContentPane().add(meaningTextField4, gbc_meaning4TextField);
    meaningTextField4.setColumns(10);
    listMeanings.add(meaningTextField4);

    // meaning 5
    JTextField meaningTextField5 = new JTextField();
    GridBagConstraints gbc_meaning5TextField = new GridBagConstraints();
    gbc_meaning5TextField.insets = new Insets(0, 10, 5, 5);
    gbc_meaning5TextField.fill = GridBagConstraints.HORIZONTAL;
    gbc_meaning5TextField.gridx = 0;
    gbc_meaning5TextField.gridy = 9;
    frame.getContentPane().add(meaningTextField5, gbc_meaning5TextField);
    meaningTextField5.setColumns(10);
    listMeanings.add(meaningTextField5);

    // meaning 6
    JTextField meaningTextField6 = new JTextField();
    GridBagConstraints gbc_meaning6TextField = new GridBagConstraints();
    gbc_meaning6TextField.insets = new Insets(0, 10, 5, 5);
    gbc_meaning6TextField.fill = GridBagConstraints.HORIZONTAL;
    gbc_meaning6TextField.gridx = 0;
    gbc_meaning6TextField.gridy = 10;
    frame.getContentPane().add(meaningTextField6, gbc_meaning6TextField);
    meaningTextField6.setColumns(10);
    listMeanings.add(meaningTextField6);

    JLabel number1 = new JLabel();
    number1.setText("(meaning #1)");
    GridBagConstraints gbc_number1textLable = new GridBagConstraints();
    gbc_number1textLable.insets = new Insets(0, 10, 5, 5);
    gbc_number1textLable.fill = GridBagConstraints.HORIZONTAL;
    gbc_number1textLable.gridx = 1;
    gbc_number1textLable.gridy = 5;
    frame.getContentPane().add(number1, gbc_number1textLable);

    JLabel number2 = new JLabel();
    number2.setText("(meaning #2)");
    GridBagConstraints gbc_number2textLable = new GridBagConstraints();
    gbc_number2textLable.insets = new Insets(0, 10, 5, 5);
    gbc_number2textLable.fill = GridBagConstraints.HORIZONTAL;
    gbc_number2textLable.gridx = 1;
    gbc_number2textLable.gridy = 6;
    frame.getContentPane().add(number2, gbc_number2textLable);

    JLabel number3 = new JLabel();
    number3.setText("(meaning #3)");
    GridBagConstraints gbc_number3textLable = new GridBagConstraints();
    gbc_number3textLable.insets = new Insets(0, 10, 5, 5);
    gbc_number3textLable.fill = GridBagConstraints.HORIZONTAL;
    gbc_number3textLable.gridx = 1;
    gbc_number3textLable.gridy = 7;
    frame.getContentPane().add(number3, gbc_number3textLable);

    JLabel number4 = new JLabel();
    number4.setText("(meaning #4)");
    GridBagConstraints gbc_number4textLable = new GridBagConstraints();
    gbc_number4textLable.insets = new Insets(0, 10, 5, 5);
    gbc_number4textLable.fill = GridBagConstraints.HORIZONTAL;
    gbc_number4textLable.gridx = 1;
    gbc_number4textLable.gridy = 8;
    frame.getContentPane().add(number4, gbc_number4textLable);

    JLabel number5 = new JLabel();
    number5.setText("(meaning #5)");
    GridBagConstraints gbc_number5textLable = new GridBagConstraints();
    gbc_number5textLable.insets = new Insets(0, 10, 5, 5);
    gbc_number5textLable.fill = GridBagConstraints.HORIZONTAL;
    gbc_number5textLable.gridx = 1;
    gbc_number5textLable.gridy = 9;
    frame.getContentPane().add(number5, gbc_number5textLable);

    JLabel number6 = new JLabel();
    number6.setText("(meaning #6)");
    GridBagConstraints gbc_number6textLable = new GridBagConstraints();
    gbc_number6textLable.insets = new Insets(0, 10, 5, 5);
    gbc_number6textLable.fill = GridBagConstraints.HORIZONTAL;
    gbc_number6textLable.gridx = 1;
    gbc_number6textLable.gridy = 10;
    frame.getContentPane().add(number6, gbc_number6textLable);

    addButton = new JButton("Add");
    GridBagConstraints gbc_btnNewButton = new GridBagConstraints();
    gbc_btnNewButton.insets = new Insets(0, 0, 5, 0);
    gbc_btnNewButton.gridx = 1;
    gbc_btnNewButton.gridy = 4;
    frame.getContentPane().add(addButton, gbc_btnNewButton);

    addButton.addActionListener(new ActionListener() {
      public void actionPerformed(ActionEvent arg0) {
        clearIfNumberActionHigh();
        String word = searchTextField.getText();

        List<String> meanings = new ArrayList<String>();

        for (JTextField jTextField : listMeanings) {
          if (!jTextField.getText().isEmpty() && jTextField.getText() != null) {
            meanings.add(jTextField.getText());
          }
        }

        System.out.println("Add clicked! Adding " + word + " : " + meanings.toString());

        try {
          String response = client.send(Client.add, word, meanings);
          masterTextArea.append(">> " + response + "\n \n");
          trigger(status[1]);
        } catch (ClientException e) {
          masterTextArea.append(e.getMessage() + "\n \n");
          trigger(status[1]);
        } catch (IOException e) {
          trigger(status[0]);
        }
      }
    });
  }

  private void clearIfNumberActionHigh() {
    if (numberAction > 1) {
      masterTextArea.setText("");
      numberAction = 0;
    }
  }

  private void delete() {
    JLabel deleteLabel = new JLabel();
    deleteLabel.setText("Delete Word");
    GridBagConstraints gbc_textLable = new GridBagConstraints();
    gbc_textLable.insets = new Insets(0, 10, 5, 5);
    gbc_textLable.fill = GridBagConstraints.HORIZONTAL;
    gbc_textLable.gridx = 0;
    gbc_textLable.gridy = 11;
    frame.getContentPane().add(deleteLabel, gbc_textLable);

    JTextField deleteTextField = new JTextField();
    GridBagConstraints gbc_textField = new GridBagConstraints();
    gbc_textField.insets = new Insets(0, 10, 5, 5);
    gbc_textField.weightx = 0.5;
    gbc_textField.fill = GridBagConstraints.HORIZONTAL;
    gbc_textField.gridx = 0;
    gbc_textField.gridy = 12;
    frame.getContentPane().add(deleteTextField, gbc_textField);
    deleteTextField.setColumns(10);

    deleteButton = new JButton("Delete");
    GridBagConstraints gbc_btnNewButton = new GridBagConstraints();
    gbc_btnNewButton.insets = new Insets(0, 0, 5, 0);
    gbc_btnNewButton.gridx = 1;
    gbc_btnNewButton.gridy = 12;
    frame.getContentPane().add(deleteButton, gbc_btnNewButton);

    deleteButton.addActionListener(new ActionListener() {
      public void actionPerformed(ActionEvent arg0) {
        clearIfNumberActionHigh();
        String word = deleteTextField.getText();
        System.out.println("Delete clicked! Deleting for " + word);

        try {
          String response = client.send(Client.delete, word);
          masterTextArea.append(">> " + response + "\n \n");
          trigger(status[1]);
        } catch (ClientException e) {
          masterTextArea.append(e.getMessage() + "\n \n");
          trigger(status[1]);
        } catch (IOException e) {
          trigger(status[0]);
        }
      }
    });
  }

}
