package main_Package;

import java.awt.Color;
import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.IOException;
import java.net.InetAddress;
import java.net.NetworkInterface;
import java.net.ServerSocket;
import java.net.Socket;
import java.net.SocketException;
import java.util.Enumeration;
import java.util.LinkedList;
import javax.swing.JButton;
import javax.swing.JTextField;
import javax.swing.JTextPane;
import javax.swing.text.BadLocationException;
import javax.swing.text.SimpleAttributeSet;
import javax.swing.text.StyleConstants;
import javax.swing.text.StyledDocument;

/**
 * The server class initializes all aspects of the chat server and listens for incoming connections on a specified port
 * @author mn3m
 */
public class Server {

      int port_number;        //the port that we will be accepting connections on
      ServerSocket ss;        //the server socket
      Socket cs;              //the socket that will connect with the server
      LinkedList<Socket> clients;         //a list of connected client sockets

      JTextField msg_field;
      JTextPane chat_pane;
      JButton snd_btn;
      
      /**
       * Initialize the server with the specified port_number
       * @param port_number   The Server's port number
       * @param msg_field     The Server's message text field
       * @param chat_pane     The Server's chat pane
       * @param snd_btn       The Server's send button
       */
      public Server(int port_number, JTextField msg_field, JTextPane chat_pane, JButton snd_btn) {
            try {
                  this.msg_field = msg_field;
                  this.chat_pane = chat_pane;
                  this.snd_btn = snd_btn;
                  this.port_number = port_number;
                  this.clients = new LinkedList<>();
                  ss = new ServerSocket(this.port_number);
                  print_server_address(chat_pane);
            } catch (IOException ex) {
                  System.err.println(ex.toString());
            }
      }
      
      /**
       * Accepts incoming connections on the port number specified by the constructor
       * Note: non-blocking call, works in the background forever
       * @see Server
       */
      public void accept_connections() {
            Runnable read_runnable = () -> {
                  try {
                        DataInputStream dis = new DataInputStream(cs.getInputStream());
                        while (true) {
                              String line = dis.readUTF();
                              if (!line.contains("online::id::")) {
                                    String[] line_id = line.split("::id::");        //split the message and the id sent from the client
                                    System.out.println(line_id[0]);
                                    appendToPane(chat_pane, "[" + cs.getInetAddress().toString() + "]: " + line_id[0] + "\n", Color.BLACK, false, false);
                                    send_message(line, true);
                              }
                        }
                  } catch (IOException ex) {
                        System.err.println(ex.toString());
                        appendToPane(chat_pane, "[" + cs.getInetAddress().toString() + "] Disconnected\n", Color.RED, true, false);
                        send_message("[" + cs.getInetAddress().toString() + "] Disconnected", true);
                        clients.remove(cs);
                        if (clients.size() < 1) {
                              appendToPane(chat_pane, "You are alone\n", Color.RED, true, true);
                        }
                  }
            };

            Runnable accept_connections_runnable = () -> {
                  while (true) {
                        try {
                              cs = ss.accept();
                              clients.add(cs);
                              msg_field.setEnabled(true);
                              snd_btn.setEnabled(true);
                              appendToPane(chat_pane, "[" + cs.getInetAddress().toString() + "] Connected\n", Color.RED, true, false);
                              send_message("[" + cs.getInetAddress().toString() + "] Connected", true);
                              new Thread(read_runnable).start();
                        } catch (IOException ex) {
                              System.err.println(ex.toString());
                              break;
                        }
                  }
            };

            new Thread(accept_connections_runnable).start();
      }
      
      /**
       * 
       * @param msg     the message that is to be sent to the connected clients or appended to the chat area
       * 
       * @param is_to_client  if true, the message is sent to the connected clients. else, the message will be appended to the chat area
       */
      public void send_message(String msg, boolean is_to_client) {
            String line = msg;
            DataOutputStream dos;

            for (int i = 0; i < clients.size(); i++) {
                  try {
                        dos = new DataOutputStream(clients.get(i).getOutputStream());
                        dos.writeUTF(line);
                  } catch (IOException ex) {
                        System.err.println("Server Error: " + ex.toString());
                        clients.remove(i);
                        send_message(msg, true);
                  }
            }

            if (!is_to_client) {
                  appendToPane(chat_pane, msg + "\n", Color.BLUE, true, false);
            }
            msg_field.setText(null);
      }
      
      /**
       * Disconnects all connected clients and closes the server
       */
      public void end_connections() {
            try {
                  for (Socket i : clients) {
                        i.close();
                  }
                  ss.close();
            } catch (IOException ex) {
                  System.err.println(ex.toString());
            }
      }
      
      /**
       * prints the IP address of the server
       * @param chat  the JTextPane where the IP should be printed
       */
      private void print_server_address(JTextPane chat) {
            String ip;
            Enumeration<NetworkInterface> n;
            try {
                  n = NetworkInterface.getNetworkInterfaces();
                  while (n.hasMoreElements()) {
                        NetworkInterface e = n.nextElement();
                        Enumeration<InetAddress> a = e.getInetAddresses();
                        while (a.hasMoreElements()) {
                              InetAddress addr = a.nextElement();
                              if (addr.isSiteLocalAddress()) {
                                    ip = "Server IP : " + addr.getHostAddress() + ":" + port_number;
                                    appendToPane(chat, ip + "\n", new Color(0x2372b2), true, false);
                              }
                        }
                  }
            } catch (SocketException e) {
                  System.err.println(e.getMessage());
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
