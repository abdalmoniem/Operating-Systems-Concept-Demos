package main_Package;

import java.awt.Color;
import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.IOException;
import java.net.Socket;
import java.util.Random;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.swing.JOptionPane;
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

    public int get_id() {
        return this.id;
    }
    
    public Socket get_socket() {
        return this.client;
    }

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
                        if (line.contains(last_sent_msg) && Integer.parseInt(line_id[1]) == this.get_id()) {
                            appendToPane(chat_pane, line_id[0] + "\n", Color.BLUE, true, false);
                        } else if (line.toLowerCase().contains("connected") || line.toLowerCase().contains("disconnected")){
                            appendToPane(chat_pane, line_id[0] + "\n", Color.RED, true, false);
                        } else {
                            appendToPane(chat_pane, "[" + client.getInetAddress().toString() + "]: " + line_id[0] + "\n", Color.BLACK, false, false);
                        }
                    } catch (Exception ex) {
                        System.err.println("Client ID #" + this.get_id() + ": " + ex.toString());
                    }
                }
            } catch (IOException ex) {
                System.err.println("Client ID #" + this.get_id() + ": " + ex.toString());
            }
        };
        new Thread(read_runnable).start();
    }

    public void send(String msg) {
        if (!msg.toLowerCase().equals("::client::quit::" + this.get_id())) {
            String line = msg + "::id::" + this.get_id();
            DataOutputStream dos;
            try {
                dos = new DataOutputStream(client.getOutputStream());
                dos.writeUTF(line);
                last_sent_msg = line;
            } catch (IOException ex) {
                System.err.println("Client ID #" + this.get_id() + ": " + ex.toString());
                appendToPane(chat_pane, "Lost connection to server...\n", Color.RED, true, false);
            }
        } else {
            try {
                client.close();
            } catch (IOException ex) {
                System.err.println("Client ID #" + this.get_id() + ": " + ex.toString());
            }
        }
    }

    private void appendToPane(JTextPane pane, String msg, Color color, boolean bold, boolean italic) {
        StyledDocument doc = pane.getStyledDocument();
        SimpleAttributeSet attr = new SimpleAttributeSet();
        try {
            StyleConstants.setForeground(attr, color);
            StyleConstants.setBold(attr, bold);
            StyleConstants.setItalic(attr, italic);
            doc.insertString(doc.getLength(), msg, attr);
            pane.setCaretPosition(pane.getDocument().getLength());
        } catch (BadLocationException ex) {
            System.err.println(ex.toString());
        }
    }
}
