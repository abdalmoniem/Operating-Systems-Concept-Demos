package main_Package;

import java.awt.Color;
import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.IOException;
import java.net.Socket;
import java.util.Random;
import javax.swing.JTextPane;
import javax.swing.text.BadLocationException;
import javax.swing.text.SimpleAttributeSet;
import javax.swing.text.StyleConstants;
import javax.swing.text.StyledDocument;

public final class Client {

      private final int id;
      private final Socket client;
      private String last_sent_msg;
      private JTextPane chat_pane;
      
      public Client(Socket client, JTextPane chat_pane) {
            this.id = new Random().nextInt(1000);
            this.client = client;
            this.chat_pane = chat_pane;
            this.send("online::id::" + this.id);
      }
      
      /**
       * Get client id
       * @return an integers representing the client id
       */
      public int get_id() {
            return this.id;
      }

      public Socket get_socket() {
            return this.client;
      }
      
      /**
       * starts a background thread that listens forever for incoming messages from a server
       */
      public void read() {
            Runnable read_runnable = () -> {
                  try {
                        DataInputStream dis = new DataInputStream(client.getInputStream());
                        while (true) {
                              String line = dis.readUTF();
                              System.out.println(line);
                              String[] line_id = null;
                              try {
                                    line_id = line.split("::id::");
                                    if (line.contains(last_sent_msg) && Integer.parseInt(line_id[1]) == this.get_id()) {    //check if the incomming message is an echo
                                          appendToPane(chat_pane, line_id[0] + "\n", Color.BLUE, true, false);
                                    } else if (line.toLowerCase().contains("connected") || line.toLowerCase().contains("disconnected")) {   //check if another client has connected
                                          appendToPane(chat_pane, line_id[0] + "\n", Color.RED, true, false);
                                    } else {    //checks if the incomming message is from another client or the server
                                          appendToPane(chat_pane, "[" + client.getInetAddress().toString() + "]: " + line_id[0] + "\n", Color.BLACK, false, false);
                                    }
                              } catch (Exception ex) {
                                    System.err.println("Error in Client ID #" + this.get_id() + ": " + ex.toString());
                              }
                        }
                  } catch (IOException ex) {
                        System.err.println("Error in Client ID #" + this.get_id() + ": " + ex.toString());
                  }
            };
            new Thread(read_runnable).start();
      }
      /**
       * Sends a message to the server
       * @param msg the message to be sent to the server
       */
      public void send(String msg) {
            if (!msg.toLowerCase().equals("::client::quit::" + this.get_id())) {    //if sent the server disconnects this client from the list of clients, else it sends the message along with the client id
                  String line = msg + "::id::" + this.get_id();
                  DataOutputStream dos;
                  try {
                        dos = new DataOutputStream(client.getOutputStream());
                        dos.writeUTF(line);
                        last_sent_msg = line;
                  } catch (IOException ex) {
                        System.err.println("Error in Client ID #" + this.get_id() + ": " + ex.toString());
                        appendToPane(chat_pane, "Lost connection to server...\n", Color.RED, true, false);
                  }
            } else {
                  try {
                        client.close();
                  } catch (IOException ex) {
                        System.err.println("Error in Client ID #" + this.get_id() + ": " + ex.toString());
                  }
            }
      }

      /**
       * 
       * @param pane    the JTextPane where you want to append text to
       * @param text     the text that you want to append to the JTextPane, Note: add an end line character in the end if you want new text to be appended in a new line
       * @param color   the color of the text
       * @param bold    if true the text will be appended in bold, else it will be appended PLAIN
       * @param italic  if true the text will be appended in italic, else it will be appended PLAIN or Bold as specified by the bold parameter
       */
      private void appendToPane(JTextPane pane, String text, Color color, boolean bold, boolean italic) {
            StyledDocument doc = pane.getStyledDocument();
            SimpleAttributeSet attr = new SimpleAttributeSet();
            try {
                  StyleConstants.setForeground(attr, color);
                  StyleConstants.setBold(attr, bold);
                  StyleConstants.setItalic(attr, italic);
                  doc.insertString(doc.getLength(), text, attr);
                  pane.setCaretPosition(pane.getDocument().getLength());
            } catch (BadLocationException ex) {
                  System.err.println(ex.toString());
            }
      }
}
