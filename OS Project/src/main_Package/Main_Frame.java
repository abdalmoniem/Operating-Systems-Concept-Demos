package main_Package;

import java.awt.Color;
import java.awt.Toolkit;
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
import java.util.Random;
import javax.swing.AbstractButton;
import javax.swing.JOptionPane;
import javax.swing.JTextPane;
import javax.swing.table.DefaultTableModel;
import javax.swing.text.BadLocationException;
import javax.swing.text.SimpleAttributeSet;
import javax.swing.text.StyleConstants;
import javax.swing.text.StyledDocument;
import javax.swing.JFrame;
import gnu.io.CommPortIdentifier;
import gnu.io.PortInUseException;
import gnu.io.SerialPort;
import gnu.io.UnsupportedCommOperationException;
import java.awt.Font;
import java.awt.HeadlessException;
import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 *
 * @author mn3m
 */
public class Main_Frame extends JFrame {

      LinkedList<Process> ready_queue;
      LinkedList<Process> ready_queue_backup;
      DefaultTableModel proc_table_model;
      DefaultTableModel editing_table_model;
      boolean client_connected = false;
      boolean server_started = false;
      int PORT_NUM = 2000;
      ServerSocket ss = null;
      Socket cs = null;
      Client client = null;
      LinkedList<Socket> clients;
      String client_last_sent_msg = null;

      private BufferedReader input;
      private OutputStream output;
      private static final int TIME_OUT = 2000;
      private static final int DATA_RATE = 115200;
      private CommPortIdentifier portId = null;
      private final LinkedList<CommPortIdentifier> portsList;
      private SerialPort serialPort = null;
      boolean op_connected = false;
      boolean ip_connected = false;

      java.lang.Process graphing_process;

      javax.swing.UIManager.LookAndFeelInfo info = javax.swing.UIManager.getInstalledLookAndFeels()[3];

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
                                    ip = "Server IP : " + addr.getHostAddress() + ":" + PORT_NUM;
                                    appendToPane(chat, ip + "\n", new Color(0x2372b2), true, false);
                              }
                        }
                  }
            } catch (SocketException e) {
                  System.err.println(e.getMessage());
            }
      }

      private void send_message(String msg, boolean echo) {
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

            if (!echo) {
                  appendToPane(server_chat_pane, msg + "\n", Color.BLUE, true, false);
            }
            server_msg_field.setText(null);
      }

      private String get_selected_button_text() {
            String button_text = null;
            for (Enumeration<AbstractButton> buttons = scheduler_button_group.getElements(); buttons.hasMoreElements();) {
                  AbstractButton button = buttons.nextElement();
                  if (button.isSelected()) {
                        button_text = button.getText();
                  }
            }
            return button_text;
      }

      private void clear_table(DefaultTableModel tm) {
            int table_length = tm.getRowCount();
            for (int i = 0; i < table_length; i++) {
                  tm.removeRow(0);
            }
      }

      private void populate_scheduler_table(LinkedList<Process> queue) {
            queue.forEach(p -> {
                  Object[] processes_data = {p.PID, p.Arrival_Time, p.Burst_Time, p.Priority};
                  proc_table_model.addRow(processes_data);
            });
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

      public synchronized void close() {
            if (serialPort != null) {
                  serialPort.removeEventListener();
                  serialPort.close();
            }
      }

      public Main_Frame() {
            initComponents();
            ready_queue = new LinkedList<>();
            clients = new LinkedList<>();
            portsList = new LinkedList<>();
            proc_table_model = (DefaultTableModel) processes_table.getModel();
            editing_table_model = (DefaultTableModel) editing_table.getModel();
      }

    // <editor-fold defaultstate="collapsed" desc="Generated Code">//GEN-BEGIN:initComponents
    private void initComponents() {

        scheduling_frame = new javax.swing.JFrame();
        scheduling_table_scroll_pane = new javax.swing.JScrollPane();
        processes_table = new javax.swing.JTable();
        fifo_chk_btn = new javax.swing.JRadioButton();
        npsjf_chk_btn = new javax.swing.JRadioButton();
        rr_chk_btn = new javax.swing.JRadioButton();
        sort_btn = new javax.swing.JButton();
        edit_scheduler_table_row = new javax.swing.JButton();
        scheduler_button_group = new javax.swing.ButtonGroup();
        editing_table_frame = new javax.swing.JFrame();
        editing_table_frame_scroll_pane = new javax.swing.JScrollPane();
        editing_table = new javax.swing.JTable();
        ok_button = new javax.swing.JButton();
        new_row_btn = new javax.swing.JButton();
        delete_row_btn = new javax.swing.JButton();
        server_frame = new javax.swing.JFrame();
        server_chat_panel = new javax.swing.JScrollPane();
        server_chat_pane = new javax.swing.JTextPane();
        server_snd_btn = new javax.swing.JButton();
        server_msg_field = new javax.swing.JTextField();
        client_frame = new javax.swing.JFrame();
        client_chat_panel = new javax.swing.JScrollPane();
        client_chat_pane = new javax.swing.JTextPane();
        client_msg_field = new javax.swing.JTextField();
        client_snd_btn = new javax.swing.JButton();
        about_frame = new javax.swing.JFrame();
        name2 = new javax.swing.JLabel();
        name3 = new javax.swing.JLabel();
        name1 = new javax.swing.JLabel();
        name4 = new javax.swing.JLabel();
        name5 = new javax.swing.JLabel();
        logo_label = new javax.swing.JLabel();
        development_team_title_label = new javax.swing.JLabel();
        output_frame = new javax.swing.JFrame();
        output_search_btn = new javax.swing.JButton();
        output_portsListComoBox = new javax.swing.JComboBox<>();
        output_cnct_discnctBtn = new javax.swing.JButton();
        statusLabel = new javax.swing.JLabel();
        brightness_slider = new javax.swing.JSlider();
        brightness_label = new javax.swing.JLabel();
        min_label = new javax.swing.JLabel();
        max_label = new javax.swing.JLabel();
        slow_input_frame = new javax.swing.JFrame();
        slow_input_search_btn = new javax.swing.JButton();
        slow_input_portsListComoBox = new javax.swing.JComboBox<>();
        slow_input_cnct_discnctBtn = new javax.swing.JButton();
        fast_input_frame = new javax.swing.JFrame();
        fast_input_search_btn = new javax.swing.JButton();
        fast_input_portsListComoBox = new javax.swing.JComboBox<>();
        fast_input_cnct_discnctBtn = new javax.swing.JButton();
        mem_btn = new javax.swing.JButton();
        net_btn = new javax.swing.JButton();
        proc_btn = new javax.swing.JButton();
        disk_btn = new javax.swing.JButton();
        main_menu_bar = new javax.swing.JMenuBar();
        file_menu = new javax.swing.JMenu();
        open_sym_item = new javax.swing.JMenuItem();
        save_sym_item = new javax.swing.JMenuItem();
        exit_item = new javax.swing.JMenuItem();
        proc_menu_item = new javax.swing.JMenu();
        sched_item = new javax.swing.JMenuItem();
        sync_item = new javax.swing.JMenuItem();
        deadlock_item = new javax.swing.JMenu();
        detect_item = new javax.swing.JMenuItem();
        prevent_item = new javax.swing.JMenuItem();
        avoid_item = new javax.swing.JMenuItem();
        mem_menu = new javax.swing.JMenu();
        file_sys_menu = new javax.swing.JMenu();
        io_menu = new javax.swing.JMenu();
        slow_input_item = new javax.swing.JMenuItem();
        fast_input_item = new javax.swing.JMenuItem();
        output_item = new javax.swing.JMenuItem();
        net_menu = new javax.swing.JMenu();
        chat_menu = new javax.swing.JMenu();
        server_item = new javax.swing.JMenuItem();
        client_item = new javax.swing.JMenuItem();
        examples_menu = new javax.swing.JMenu();
        ex_proc_menu = new javax.swing.JMenu();
        ex_sched_item = new javax.swing.JMenuItem();
        ex_sync_item = new javax.swing.JMenuItem();
        ex_deadlock_item = new javax.swing.JMenu();
        ex_detect_item = new javax.swing.JMenuItem();
        ex_prevent_item = new javax.swing.JMenuItem();
        ex_avoid_item = new javax.swing.JMenuItem();
        ex_mem_menu = new javax.swing.JMenu();
        ex_file_sys_menu = new javax.swing.JMenu();
        ex_io_menu = new javax.swing.JMenu();
        ex_net_menu = new javax.swing.JMenu();
        ex_chat_menu = new javax.swing.JMenu();
        ex_server_item = new javax.swing.JMenuItem();
        ex_client_item = new javax.swing.JMenuItem();
        help_menu = new javax.swing.JMenu();
        doc_item = new javax.swing.JMenuItem();
        about_item = new javax.swing.JMenuItem();

        processes_table.setModel(new javax.swing.table.DefaultTableModel(
            new Object [][] {

            },
            new String [] {
                "PID", "Arrival Time", "Burst Time", "Priority"
            }
        ) {
            Class[] types = new Class [] {
                java.lang.Integer.class, java.lang.Integer.class, java.lang.Integer.class, java.lang.Integer.class
            };
            boolean[] canEdit = new boolean [] {
                false, false, false, false
            };

            public Class getColumnClass(int columnIndex) {
                return types [columnIndex];
            }

            public boolean isCellEditable(int rowIndex, int columnIndex) {
                return canEdit [columnIndex];
            }
        });
        scheduling_table_scroll_pane.setViewportView(processes_table);

        scheduler_button_group.add(fifo_chk_btn);
        fifo_chk_btn.setSelected(true);
        fifo_chk_btn.setText("FIFO");

        scheduler_button_group.add(npsjf_chk_btn);
        npsjf_chk_btn.setText("SJF");

        scheduler_button_group.add(rr_chk_btn);
        rr_chk_btn.setText("RR");

        sort_btn.setText("Schedule");
        sort_btn.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                sort_btnActionPerformed(evt);
            }
        });

        edit_scheduler_table_row.setText("Edit Table");
        edit_scheduler_table_row.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                edit_scheduler_table_rowActionPerformed(evt);
            }
        });

        javax.swing.GroupLayout scheduling_frameLayout = new javax.swing.GroupLayout(scheduling_frame.getContentPane());
        scheduling_frame.getContentPane().setLayout(scheduling_frameLayout);
        scheduling_frameLayout.setHorizontalGroup(
            scheduling_frameLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addComponent(scheduling_table_scroll_pane, javax.swing.GroupLayout.DEFAULT_SIZE, 470, Short.MAX_VALUE)
            .addGroup(scheduling_frameLayout.createSequentialGroup()
                .addContainerGap()
                .addGroup(scheduling_frameLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(sort_btn, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                    .addGroup(scheduling_frameLayout.createSequentialGroup()
                        .addComponent(fifo_chk_btn)
                        .addGap(18, 18, 18)
                        .addComponent(npsjf_chk_btn)
                        .addGap(18, 18, 18)
                        .addComponent(rr_chk_btn)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                        .addComponent(edit_scheduler_table_row)))
                .addContainerGap())
        );
        scheduling_frameLayout.setVerticalGroup(
            scheduling_frameLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(scheduling_frameLayout.createSequentialGroup()
                .addComponent(scheduling_table_scroll_pane, javax.swing.GroupLayout.DEFAULT_SIZE, 305, Short.MAX_VALUE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addGroup(scheduling_frameLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(edit_scheduler_table_row)
                    .addComponent(fifo_chk_btn)
                    .addComponent(npsjf_chk_btn)
                    .addComponent(rr_chk_btn))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(sort_btn, javax.swing.GroupLayout.PREFERRED_SIZE, 49, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addContainerGap())
        );

        editing_table_frame.addWindowListener(new java.awt.event.WindowAdapter() {
            public void windowClosing(java.awt.event.WindowEvent evt) {
                editing_table_frameWindowClosing(evt);
            }
        });

        editing_table.setModel(new javax.swing.table.DefaultTableModel(
            new Object [][] {

            },
            new String [] {
                "PID", "Arrival Time", "Burst Time", "Priority"
            }
        ));
        editing_table_frame_scroll_pane.setViewportView(editing_table);

        ok_button.setText("OK");
        ok_button.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                ok_buttonActionPerformed(evt);
            }
        });

        new_row_btn.setText("New Row");
        new_row_btn.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                new_row_btnActionPerformed(evt);
            }
        });

        delete_row_btn.setText("Delete Row");
        delete_row_btn.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                delete_row_btnActionPerformed(evt);
            }
        });

        javax.swing.GroupLayout editing_table_frameLayout = new javax.swing.GroupLayout(editing_table_frame.getContentPane());
        editing_table_frame.getContentPane().setLayout(editing_table_frameLayout);
        editing_table_frameLayout.setHorizontalGroup(
            editing_table_frameLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addComponent(editing_table_frame_scroll_pane, javax.swing.GroupLayout.DEFAULT_SIZE, 400, Short.MAX_VALUE)
            .addGroup(editing_table_frameLayout.createSequentialGroup()
                .addContainerGap()
                .addGroup(editing_table_frameLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING, false)
                    .addComponent(delete_row_btn, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                    .addComponent(new_row_btn, javax.swing.GroupLayout.DEFAULT_SIZE, 101, Short.MAX_VALUE))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(ok_button, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                .addContainerGap())
        );
        editing_table_frameLayout.setVerticalGroup(
            editing_table_frameLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(editing_table_frameLayout.createSequentialGroup()
                .addComponent(editing_table_frame_scroll_pane, javax.swing.GroupLayout.DEFAULT_SIZE, 231, Short.MAX_VALUE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addGroup(editing_table_frameLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING, false)
                    .addGroup(editing_table_frameLayout.createSequentialGroup()
                        .addComponent(new_row_btn)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(delete_row_btn))
                    .addComponent(ok_button, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
                .addContainerGap())
        );

        server_frame.setTitle("Server");
        server_frame.addWindowListener(new java.awt.event.WindowAdapter() {
            public void windowClosing(java.awt.event.WindowEvent evt) {
                server_frameWindowClosing(evt);
            }
        });

        server_chat_pane.setEditable(false);
        server_chat_pane.setFont(new java.awt.Font("Consolas", 0, 12)); // NOI18N
        server_chat_pane.setCursor(new java.awt.Cursor(java.awt.Cursor.TEXT_CURSOR));
        server_chat_panel.setViewportView(server_chat_pane);

        server_snd_btn.setText("Send");
        server_snd_btn.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                server_snd_btnActionPerformed(evt);
            }
        });

        server_msg_field.addKeyListener(new java.awt.event.KeyAdapter() {
            public void keyPressed(java.awt.event.KeyEvent evt) {
                server_msg_fieldKeyPressed(evt);
            }
        });

        javax.swing.GroupLayout server_frameLayout = new javax.swing.GroupLayout(server_frame.getContentPane());
        server_frame.getContentPane().setLayout(server_frameLayout);
        server_frameLayout.setHorizontalGroup(
            server_frameLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addComponent(server_chat_panel)
            .addGroup(server_frameLayout.createSequentialGroup()
                .addComponent(server_msg_field, javax.swing.GroupLayout.DEFAULT_SIZE, 424, Short.MAX_VALUE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(server_snd_btn, javax.swing.GroupLayout.PREFERRED_SIZE, 70, javax.swing.GroupLayout.PREFERRED_SIZE))
        );
        server_frameLayout.setVerticalGroup(
            server_frameLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(server_frameLayout.createSequentialGroup()
                .addComponent(server_chat_panel, javax.swing.GroupLayout.DEFAULT_SIZE, 370, Short.MAX_VALUE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addGroup(server_frameLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(server_snd_btn)
                    .addComponent(server_msg_field, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addContainerGap())
        );

        client_frame.addWindowListener(new java.awt.event.WindowAdapter() {
            public void windowClosing(java.awt.event.WindowEvent evt) {
                client_frameWindowClosing(evt);
            }
        });

        client_chat_pane.setEditable(false);
        client_chat_pane.setFont(new java.awt.Font("Consolas", 0, 12)); // NOI18N
        client_chat_pane.setCursor(new java.awt.Cursor(java.awt.Cursor.TEXT_CURSOR));
        client_chat_panel.setViewportView(client_chat_pane);

        client_msg_field.addKeyListener(new java.awt.event.KeyAdapter() {
            public void keyPressed(java.awt.event.KeyEvent evt) {
                client_msg_fieldKeyPressed(evt);
            }
        });

        client_snd_btn.setText("Send");
        client_snd_btn.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                client_snd_btnActionPerformed(evt);
            }
        });

        javax.swing.GroupLayout client_frameLayout = new javax.swing.GroupLayout(client_frame.getContentPane());
        client_frame.getContentPane().setLayout(client_frameLayout);
        client_frameLayout.setHorizontalGroup(
            client_frameLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addComponent(client_chat_panel)
            .addGroup(client_frameLayout.createSequentialGroup()
                .addComponent(client_msg_field, javax.swing.GroupLayout.DEFAULT_SIZE, 327, Short.MAX_VALUE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(client_snd_btn)
                .addContainerGap())
        );
        client_frameLayout.setVerticalGroup(
            client_frameLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(client_frameLayout.createSequentialGroup()
                .addComponent(client_chat_panel, javax.swing.GroupLayout.DEFAULT_SIZE, 260, Short.MAX_VALUE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addGroup(client_frameLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(client_msg_field, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(client_snd_btn))
                .addContainerGap())
        );

        about_frame.setTitle("About");
        about_frame.setResizable(false);

        name2.setFont(new java.awt.Font("Segoe Print", 1, 14)); // NOI18N
        name2.setHorizontalAlignment(javax.swing.SwingConstants.CENTER);
        name2.setText("Khalid Taha Ahmed");

        name3.setFont(new java.awt.Font("Segoe Print", 1, 14)); // NOI18N
        name3.setHorizontalAlignment(javax.swing.SwingConstants.CENTER);
        name3.setText("Eslam Khalid Tawfik");

        name1.setFont(new java.awt.Font("Segoe Print", 1, 14)); // NOI18N
        name1.setHorizontalAlignment(javax.swing.SwingConstants.CENTER);
        name1.setText("Ahmed Samir Demerdash");

        name4.setFont(new java.awt.Font("Segoe Print", 1, 14)); // NOI18N
        name4.setHorizontalAlignment(javax.swing.SwingConstants.CENTER);
        name4.setText("Rana Tarek Qunswa");

        name5.setFont(new java.awt.Font("Segoe Print", 1, 14)); // NOI18N
        name5.setHorizontalAlignment(javax.swing.SwingConstants.CENTER);
        name5.setText("AbdAlMoniem Osama AlHifnawy");

        logo_label.setHorizontalAlignment(javax.swing.SwingConstants.CENTER);
        logo_label.setIcon(new javax.swing.ImageIcon(getClass().getResource("/main_Package/Pics/logo.png"))); // NOI18N

        development_team_title_label.setFont(new java.awt.Font("Tahoma", 1, 24)); // NOI18N
        development_team_title_label.setHorizontalAlignment(javax.swing.SwingConstants.CENTER);
        development_team_title_label.setText("Development Team:");

        javax.swing.GroupLayout about_frameLayout = new javax.swing.GroupLayout(about_frame.getContentPane());
        about_frame.getContentPane().setLayout(about_frameLayout);
        about_frameLayout.setHorizontalGroup(
            about_frameLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addComponent(name2, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
            .addComponent(name3, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
            .addComponent(name4, javax.swing.GroupLayout.Alignment.TRAILING, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
            .addComponent(name5, javax.swing.GroupLayout.Alignment.TRAILING, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
            .addComponent(name1, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
            .addComponent(development_team_title_label, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
            .addComponent(logo_label, javax.swing.GroupLayout.Alignment.TRAILING, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
        );
        about_frameLayout.setVerticalGroup(
            about_frameLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, about_frameLayout.createSequentialGroup()
                .addGap(0, 0, Short.MAX_VALUE)
                .addComponent(logo_label, javax.swing.GroupLayout.PREFERRED_SIZE, 440, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(development_team_title_label)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(name1)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(name2)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(name3, javax.swing.GroupLayout.PREFERRED_SIZE, 13, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(name4)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(name5))
        );

        output_frame.setTitle("Output");
        output_frame.setResizable(false);
        output_frame.addWindowListener(new java.awt.event.WindowAdapter() {
            public void windowClosing(java.awt.event.WindowEvent evt) {
                output_frameWindowClosing(evt);
            }
        });

        output_search_btn.setText("Search COM Ports");
        output_search_btn.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                output_search_btnActionPerformed(evt);
            }
        });

        output_cnct_discnctBtn.setText("Connect to Port");
        output_cnct_discnctBtn.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                output_cnct_discnctBtnActionPerformed(evt);
            }
        });

        statusLabel.setFont(new java.awt.Font("Consolas", 0, 14)); // NOI18N
        statusLabel.setText("Status: Disconnected");

        brightness_slider.setFont(new java.awt.Font("Tahoma", 1, 24)); // NOI18N
        brightness_slider.setMajorTickSpacing(5);
        brightness_slider.setMaximum(255);
        brightness_slider.setPaintTicks(true);
        brightness_slider.setValue(0);
        brightness_slider.setCursor(new java.awt.Cursor(java.awt.Cursor.HAND_CURSOR));
        brightness_slider.addChangeListener(new javax.swing.event.ChangeListener() {
            public void stateChanged(javax.swing.event.ChangeEvent evt) {
                brightness_sliderStateChanged(evt);
            }
        });

        brightness_label.setFont(new java.awt.Font("Consolas", 1, 14)); // NOI18N
        brightness_label.setText("Brightness:");

        min_label.setText("min");

        max_label.setText("max");

        javax.swing.GroupLayout output_frameLayout = new javax.swing.GroupLayout(output_frame.getContentPane());
        output_frame.getContentPane().setLayout(output_frameLayout);
        output_frameLayout.setHorizontalGroup(
            output_frameLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(output_frameLayout.createSequentialGroup()
                .addContainerGap()
                .addGroup(output_frameLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(output_frameLayout.createSequentialGroup()
                        .addComponent(output_search_btn, javax.swing.GroupLayout.PREFERRED_SIZE, 300, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                        .addGroup(output_frameLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addComponent(output_portsListComoBox, 0, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                            .addComponent(output_cnct_discnctBtn, javax.swing.GroupLayout.DEFAULT_SIZE, 190, Short.MAX_VALUE)))
                    .addComponent(brightness_slider, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                    .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, output_frameLayout.createSequentialGroup()
                        .addGap(0, 0, Short.MAX_VALUE)
                        .addComponent(statusLabel))
                    .addGroup(output_frameLayout.createSequentialGroup()
                        .addComponent(brightness_label)
                        .addGap(0, 0, Short.MAX_VALUE))
                    .addGroup(output_frameLayout.createSequentialGroup()
                        .addComponent(min_label)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                        .addComponent(max_label)))
                .addContainerGap())
        );
        output_frameLayout.setVerticalGroup(
            output_frameLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(output_frameLayout.createSequentialGroup()
                .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                .addGroup(output_frameLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(output_frameLayout.createSequentialGroup()
                        .addComponent(output_portsListComoBox, javax.swing.GroupLayout.PREFERRED_SIZE, 34, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(output_cnct_discnctBtn, javax.swing.GroupLayout.PREFERRED_SIZE, 35, javax.swing.GroupLayout.PREFERRED_SIZE))
                    .addComponent(output_search_btn, javax.swing.GroupLayout.PREFERRED_SIZE, 75, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(brightness_label)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addGroup(output_frameLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(min_label)
                    .addComponent(max_label))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(brightness_slider, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(statusLabel)
                .addContainerGap())
        );

        slow_input_frame.setTitle("Slow Input");
        slow_input_frame.setResizable(false);

        slow_input_search_btn.setText("Search COM Ports");
        slow_input_search_btn.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                slow_input_search_btnActionPerformed(evt);
            }
        });

        slow_input_cnct_discnctBtn.setText("Connect to Port");
        slow_input_cnct_discnctBtn.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                slow_input_cnct_discnctBtnActionPerformed(evt);
            }
        });

        javax.swing.GroupLayout slow_input_frameLayout = new javax.swing.GroupLayout(slow_input_frame.getContentPane());
        slow_input_frame.getContentPane().setLayout(slow_input_frameLayout);
        slow_input_frameLayout.setHorizontalGroup(
            slow_input_frameLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(slow_input_frameLayout.createSequentialGroup()
                .addContainerGap()
                .addComponent(slow_input_search_btn, javax.swing.GroupLayout.PREFERRED_SIZE, 300, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                .addGroup(slow_input_frameLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(slow_input_portsListComoBox, 0, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                    .addComponent(slow_input_cnct_discnctBtn, javax.swing.GroupLayout.DEFAULT_SIZE, 190, Short.MAX_VALUE))
                .addContainerGap())
        );
        slow_input_frameLayout.setVerticalGroup(
            slow_input_frameLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(slow_input_frameLayout.createSequentialGroup()
                .addContainerGap()
                .addGroup(slow_input_frameLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(slow_input_frameLayout.createSequentialGroup()
                        .addComponent(slow_input_portsListComoBox, javax.swing.GroupLayout.PREFERRED_SIZE, 34, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(slow_input_cnct_discnctBtn, javax.swing.GroupLayout.PREFERRED_SIZE, 35, javax.swing.GroupLayout.PREFERRED_SIZE))
                    .addComponent(slow_input_search_btn, javax.swing.GroupLayout.PREFERRED_SIZE, 75, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
        );

        fast_input_frame.setTitle("Fast Input");
        fast_input_frame.setResizable(false);

        fast_input_search_btn.setText("Search COM Ports");
        fast_input_search_btn.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                fast_input_search_btnActionPerformed(evt);
            }
        });

        fast_input_cnct_discnctBtn.setText("Connect to Port");
        fast_input_cnct_discnctBtn.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                fast_input_cnct_discnctBtnActionPerformed(evt);
            }
        });

        javax.swing.GroupLayout fast_input_frameLayout = new javax.swing.GroupLayout(fast_input_frame.getContentPane());
        fast_input_frame.getContentPane().setLayout(fast_input_frameLayout);
        fast_input_frameLayout.setHorizontalGroup(
            fast_input_frameLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(fast_input_frameLayout.createSequentialGroup()
                .addContainerGap()
                .addComponent(fast_input_search_btn, javax.swing.GroupLayout.PREFERRED_SIZE, 300, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                .addGroup(fast_input_frameLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(fast_input_portsListComoBox, 0, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                    .addComponent(fast_input_cnct_discnctBtn, javax.swing.GroupLayout.DEFAULT_SIZE, 190, Short.MAX_VALUE))
                .addContainerGap())
        );
        fast_input_frameLayout.setVerticalGroup(
            fast_input_frameLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(fast_input_frameLayout.createSequentialGroup()
                .addContainerGap()
                .addGroup(fast_input_frameLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(fast_input_frameLayout.createSequentialGroup()
                        .addComponent(fast_input_portsListComoBox, javax.swing.GroupLayout.PREFERRED_SIZE, 34, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(fast_input_cnct_discnctBtn, javax.swing.GroupLayout.PREFERRED_SIZE, 35, javax.swing.GroupLayout.PREFERRED_SIZE))
                    .addComponent(fast_input_search_btn, javax.swing.GroupLayout.PREFERRED_SIZE, 75, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
        );

        setDefaultCloseOperation(javax.swing.WindowConstants.EXIT_ON_CLOSE);
        setResizable(false);

        mem_btn.setText("Memory");

        net_btn.setText("Network");
        net_btn.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                net_btnActionPerformed(evt);
            }
        });

        proc_btn.setText("Processes");

        disk_btn.setText("Disk");

        file_menu.setText("File");

        open_sym_item.setAccelerator(javax.swing.KeyStroke.getKeyStroke(java.awt.event.KeyEvent.VK_O, java.awt.event.InputEvent.CTRL_MASK));
        open_sym_item.setText("Open Simulation");
        file_menu.add(open_sym_item);

        save_sym_item.setAccelerator(javax.swing.KeyStroke.getKeyStroke(java.awt.event.KeyEvent.VK_S, java.awt.event.InputEvent.CTRL_MASK));
        save_sym_item.setText("Save Simulation");
        file_menu.add(save_sym_item);

        exit_item.setText("Exit");
        exit_item.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                exit_itemActionPerformed(evt);
            }
        });
        file_menu.add(exit_item);

        main_menu_bar.add(file_menu);

        proc_menu_item.setText("Processes");

        sched_item.setText("Scheduling");
        sched_item.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                sched_itemActionPerformed(evt);
            }
        });
        proc_menu_item.add(sched_item);

        sync_item.setText("Synchronization");
        proc_menu_item.add(sync_item);

        deadlock_item.setText("Deadlock");

        detect_item.setText("Detection");
        detect_item.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                detect_itemActionPerformed(evt);
            }
        });
        deadlock_item.add(detect_item);

        prevent_item.setText("Prevention");
        deadlock_item.add(prevent_item);

        avoid_item.setText("Avoidance");
        deadlock_item.add(avoid_item);

        proc_menu_item.add(deadlock_item);

        main_menu_bar.add(proc_menu_item);

        mem_menu.setText("Memory");
        main_menu_bar.add(mem_menu);

        file_sys_menu.setText("File System");
        main_menu_bar.add(file_sys_menu);

        io_menu.setText("I/O");

        slow_input_item.setText("Slow Input");
        slow_input_item.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                slow_input_itemActionPerformed(evt);
            }
        });
        io_menu.add(slow_input_item);

        fast_input_item.setText("Fast Input");
        fast_input_item.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                fast_input_itemActionPerformed(evt);
            }
        });
        io_menu.add(fast_input_item);

        output_item.setText("Output");
        output_item.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                output_itemActionPerformed(evt);
            }
        });
        io_menu.add(output_item);

        main_menu_bar.add(io_menu);

        net_menu.setText("Network");

        chat_menu.setText("Simple Chat");

        server_item.setText("Server");
        server_item.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                server_itemActionPerformed(evt);
            }
        });
        chat_menu.add(server_item);

        client_item.setText("Client");
        client_item.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                client_itemActionPerformed(evt);
            }
        });
        chat_menu.add(client_item);

        net_menu.add(chat_menu);

        main_menu_bar.add(net_menu);

        examples_menu.setText("Examples");

        ex_proc_menu.setText("Processes");

        ex_sched_item.setText("Scheduling");
        ex_proc_menu.add(ex_sched_item);

        ex_sync_item.setText("Synchronization");
        ex_proc_menu.add(ex_sync_item);

        ex_deadlock_item.setText("Deadlock");

        ex_detect_item.setText("Detection");
        ex_deadlock_item.add(ex_detect_item);

        ex_prevent_item.setText("Prevention");
        ex_deadlock_item.add(ex_prevent_item);

        ex_avoid_item.setText("Avoidance");
        ex_deadlock_item.add(ex_avoid_item);

        ex_proc_menu.add(ex_deadlock_item);

        examples_menu.add(ex_proc_menu);

        ex_mem_menu.setText("Memory");
        examples_menu.add(ex_mem_menu);

        ex_file_sys_menu.setText("File System");
        examples_menu.add(ex_file_sys_menu);

        ex_io_menu.setText("I/O");
        examples_menu.add(ex_io_menu);

        ex_net_menu.setText("Network");

        ex_chat_menu.setText("Simple Chat");

        ex_server_item.setText("Server");
        ex_chat_menu.add(ex_server_item);

        ex_client_item.setText("Client");
        ex_chat_menu.add(ex_client_item);

        ex_net_menu.add(ex_chat_menu);

        examples_menu.add(ex_net_menu);

        main_menu_bar.add(examples_menu);

        help_menu.setText("Help");

        doc_item.setAccelerator(javax.swing.KeyStroke.getKeyStroke(java.awt.event.KeyEvent.VK_H, java.awt.event.InputEvent.CTRL_MASK));
        doc_item.setText("Documentation");
        help_menu.add(doc_item);

        about_item.setAccelerator(javax.swing.KeyStroke.getKeyStroke(java.awt.event.KeyEvent.VK_A, java.awt.event.InputEvent.CTRL_MASK));
        about_item.setText("About");
        about_item.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                about_itemActionPerformed(evt);
            }
        });
        help_menu.add(about_item);

        main_menu_bar.add(help_menu);

        setJMenuBar(main_menu_bar);

        javax.swing.GroupLayout layout = new javax.swing.GroupLayout(getContentPane());
        getContentPane().setLayout(layout);
        layout.setHorizontalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, layout.createSequentialGroup()
                .addContainerGap(163, Short.MAX_VALUE)
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(proc_btn, javax.swing.GroupLayout.Alignment.TRAILING, javax.swing.GroupLayout.PREFERRED_SIZE, 150, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(disk_btn, javax.swing.GroupLayout.Alignment.TRAILING, javax.swing.GroupLayout.PREFERRED_SIZE, 150, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(net_btn, javax.swing.GroupLayout.PREFERRED_SIZE, 150, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(mem_btn, javax.swing.GroupLayout.PREFERRED_SIZE, 150, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addGap(181, 181, 181))
        );
        layout.setVerticalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(layout.createSequentialGroup()
                .addGap(41, 41, 41)
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(mem_btn, javax.swing.GroupLayout.PREFERRED_SIZE, 150, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(proc_btn, javax.swing.GroupLayout.PREFERRED_SIZE, 150, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(net_btn, javax.swing.GroupLayout.PREFERRED_SIZE, 150, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(disk_btn, javax.swing.GroupLayout.PREFERRED_SIZE, 150, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addContainerGap(27, Short.MAX_VALUE))
        );

        pack();
    }// </editor-fold>//GEN-END:initComponents

    private void sort_btnActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_sort_btnActionPerformed
          try {
                clear_table(proc_table_model);
                String buttonText = get_selected_button_text();
                LinkedList<Process> temp = new LinkedList<>();
                if (buttonText.toLowerCase().contains("fifo")) {
                      temp.addAll(new Scheduler(ready_queue).sort(Scheduler.FIFO));
                } else if (buttonText.toLowerCase().contains("sjf")) {
                      temp.addAll(new Scheduler(ready_queue).sort(Scheduler.SJF));
                } else if (buttonText.toLowerCase().contains("rr")) {
                      temp.addAll(new Scheduler(ready_queue).sort(Scheduler.RR, 40));
                }
                populate_scheduler_table(temp);
          } catch (Exception ex) {
                System.err.println(ex.toString());
          }
    }//GEN-LAST:event_sort_btnActionPerformed

    private void edit_scheduler_table_rowActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_edit_scheduler_table_rowActionPerformed
          scheduling_frame.setVisible(false);
          clear_table(editing_table_model);
          for (int i = 0; i < proc_table_model.getRowCount(); i++) {
                Object[] data = new Object[4];
                for (int j = 0; j < proc_table_model.getColumnCount(); j++) {
                      data[j] = proc_table_model.getValueAt(i, j);
                }
                editing_table_model.addRow(data);
          }
          editing_table_frame.setSize(500, 350);
          editing_table_frame.setVisible(true);
    }//GEN-LAST:event_edit_scheduler_table_rowActionPerformed

    private void new_row_btnActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_new_row_btnActionPerformed
          editing_table_model.addRow(new Object[4]);
    }//GEN-LAST:event_new_row_btnActionPerformed

    private void ok_buttonActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_ok_buttonActionPerformed
          ready_queue.clear();
          clear_table(proc_table_model);
          for (int i = 0; i < editing_table_model.getRowCount(); i++) {
                Object[] data = new Object[4];
                for (int j = 0; j < editing_table_model.getColumnCount(); j++) {
                      data[j] = editing_table_model.getValueAt(i, j);
                }
                proc_table_model.addRow(data);
          }

          for (int i = 0; i < editing_table_model.getRowCount(); i++) {
                Object[] data = new Object[4];
                for (int j = 0; j < editing_table_model.getColumnCount(); j++) {
                      data[j] = editing_table_model.getValueAt(i, j);
                }
                Process temp = new Process(Integer.parseInt(data[0].toString()),
                        Integer.parseInt(data[1].toString()),
                        Integer.parseInt(data[2].toString()),
                        Integer.parseInt(data[3].toString()));
                ready_queue.add(temp);
          }
          clear_table(editing_table_model);
          editing_table_frame.setVisible(false);
          scheduling_frame.setVisible(true);
    }//GEN-LAST:event_ok_buttonActionPerformed

    private void editing_table_frameWindowClosing(java.awt.event.WindowEvent evt) {//GEN-FIRST:event_editing_table_frameWindowClosing
          editing_table_frame.setVisible(false);
          scheduling_frame.setVisible(true);
    }//GEN-LAST:event_editing_table_frameWindowClosing

    private void delete_row_btnActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_delete_row_btnActionPerformed
          editing_table_model.removeRow(editing_table.getSelectedRow());
    }//GEN-LAST:event_delete_row_btnActionPerformed

    private void sched_itemActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_sched_itemActionPerformed
          ready_queue.clear();
          clear_table(proc_table_model);
          clear_table(editing_table_model);
          for (int i = 0; i < 15; i++) {
                ready_queue.add(new Process(i,
                        new Random().nextInt(100),
                        new Random().nextInt(30),
                        new Random().nextInt(6)));
          }

          populate_scheduler_table(ready_queue);
          scheduling_frame.setSize(500, 350);
          scheduling_frame.setVisible(true);
    }//GEN-LAST:event_sched_itemActionPerformed

    private void server_itemActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_server_itemActionPerformed
          if (!server_started) {
                try {
                      server_frame.setSize(500, 400);
                      server_frame.setVisible(true);
                      server_msg_field.setEnabled(false);
                      server_snd_btn.setEnabled(false);
                      server_chat_pane.setText(null);
                      server_started = true;

                      ss = new ServerSocket(PORT_NUM);
                      print_server_address(server_chat_pane);

                      Runnable read_runnable = () -> {
                            try {
                                  DataInputStream dis = new DataInputStream(cs.getInputStream());
                                  while (true) {
                                        String line = dis.readUTF();
                                        if (!line.contains("online::id::")) {
                                              String[] line_id = line.split("::id::");
                                              System.out.println(line_id[0]);
                                              appendToPane(server_chat_pane, "[" + cs.getInetAddress().toString() + "]: " + line_id[0] + "\n", Color.BLACK, false, false);
                                              send_message(line, true);
                                        }
                                  }
                            } catch (IOException ex) {
                                  System.err.println(ex.toString());
                                  appendToPane(server_chat_pane, "[" + cs.getInetAddress().toString() + "] Disconnected\n", Color.RED, true, false);
                                  send_message("[" + cs.getInetAddress().toString() + "] Disconnected", true);
                            }
                      };

                      Runnable connect_runnable = () -> {
                            while (true) {
                                  try {
                                        cs = ss.accept();
                                        clients.add(cs);
                                        server_msg_field.setEnabled(true);
                                        server_snd_btn.setEnabled(true);
                                        appendToPane(server_chat_pane, "[" + cs.getInetAddress().toString() + "] Connected\n", Color.RED, true, false);
                                        send_message("[" + cs.getInetAddress().toString() + "] Connected", true);
                                        new Thread(read_runnable).start();
                                  } catch (IOException ex) {
                                        System.err.println(ex.toString());
                                        break;
                                  }
                            }
                      };

                      new Thread(connect_runnable).start();
                } catch (IOException ex) {
                      System.err.println(ex.toString());
                }
          } else {
                JOptionPane.showMessageDialog(this, "Server is already started", "Error", JOptionPane.ERROR_MESSAGE);
          }
    }//GEN-LAST:event_server_itemActionPerformed

    private void server_msg_fieldKeyPressed(java.awt.event.KeyEvent evt) {//GEN-FIRST:event_server_msg_fieldKeyPressed
          if (evt.getKeyCode() == 10) {   //enter key is pressed
                String line = server_msg_field.getText();
                if (line.length() != 0) {
                      send_message(line, false);
                }
          }
    }//GEN-LAST:event_server_msg_fieldKeyPressed

    private void client_itemActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_client_itemActionPerformed
          try {
                if (!client_connected) {
                      String user_input = JOptionPane.showInputDialog(this, "Enter server ip address and port number separated by \":\": ", "Server IP:Port number", JOptionPane.PLAIN_MESSAGE);
                      if (user_input != null) {
                            client_chat_pane.setText(null);
                            client_connected = true;
                            String[] ip_port = user_input.split(":");
                            client = new Client(new Socket(ip_port[0], Integer.parseInt(ip_port[1])), client_chat_pane);
                            client.read();
                            client_frame.setSize(500, 400);
                            client_frame.setVisible(true);
                            client_frame.setTitle(client.get_socket().getInetAddress().toString() + "::" + client.get_id());
                      }
                } else {
                      JOptionPane.showMessageDialog(this, "Client is already connected", "Error", JOptionPane.ERROR_MESSAGE);
                }
          } catch (IOException ex) {
                JOptionPane.showMessageDialog(this, ex.getMessage(), "Error", JOptionPane.ERROR_MESSAGE);
                System.err.println(ex.toString());
                client_connected = false;
          } catch (ArrayIndexOutOfBoundsException ex) {
                JOptionPane.showMessageDialog(this, "Invalid IP and Port address", "Error", JOptionPane.ERROR_MESSAGE);
                System.err.println(ex.toString());
                client_connected = false;
          } catch (HeadlessException | NumberFormatException ex) {
                System.err.println(ex.toString());
                client_connected = false;
          }
    }//GEN-LAST:event_client_itemActionPerformed

    private void client_msg_fieldKeyPressed(java.awt.event.KeyEvent evt) {//GEN-FIRST:event_client_msg_fieldKeyPressed
          if (evt.getKeyCode() == 10) {   //enter key is pressed
                String line = client_msg_field.getText();
                if (line.length() != 0) {
                      client.send(line);
                      client_msg_field.setText(null);
                }
          }
    }//GEN-LAST:event_client_msg_fieldKeyPressed

    private void exit_itemActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_exit_itemActionPerformed
          System.exit(0);
    }//GEN-LAST:event_exit_itemActionPerformed

    private void server_frameWindowClosing(java.awt.event.WindowEvent evt) {//GEN-FIRST:event_server_frameWindowClosing
          try {
                for (Socket i : clients) {
                      i.close();
                }
                ss.close();
                server_started = false;
          } catch (IOException ex) {
                System.err.println(ex.toString());
          }
    }//GEN-LAST:event_server_frameWindowClosing

    private void client_frameWindowClosing(java.awt.event.WindowEvent evt) {//GEN-FIRST:event_client_frameWindowClosing
          client.send("::client::quit::" + client.get_id());
          client_connected = false;
    }//GEN-LAST:event_client_frameWindowClosing

    private void detect_itemActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_detect_itemActionPerformed
          LinkedList<Process> ready = new LinkedList<>();
          for (int i = 0; i < 5; i++) {
                ready.add(new Process());
          }

          ready.forEach(i -> {
                System.out.printf("PID: %d\tNeed A: %d\tNeed B: %d\tNeed C: %d\n",
                        i.PID, i.Need.get_A(), i.Need.get_B(), i.Need.get_C());
          });

          System.out.println("\nChecking for Deadlock...");
          new Deadlock(ready, new Resource(10, 33, 19)).detect();
    }//GEN-LAST:event_detect_itemActionPerformed

    private void net_btnActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_net_btnActionPerformed
          int choice = JOptionPane.showOptionDialog(this,
                  "Run Client or Server application ?",
                  "Choose a Program",
                  JOptionPane.YES_NO_CANCEL_OPTION,
                  JOptionPane.QUESTION_MESSAGE,
                  null,
                  new String[]{"Server", "Client", "Cancel"},
                  "Server");
          if (choice == JOptionPane.YES_OPTION) {
                server_itemActionPerformed(evt);
          } else if (choice == JOptionPane.NO_OPTION) {
                client_itemActionPerformed(evt);
          }
    }//GEN-LAST:event_net_btnActionPerformed

    private void about_itemActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_about_itemActionPerformed
          int screen_width = Toolkit.getDefaultToolkit().getScreenSize().width;
          int screen_height = Toolkit.getDefaultToolkit().getScreenSize().height;

          about_frame.setLocation(screen_width / 3, screen_height / 8);
          about_frame.setSize(280, 650);
          about_frame.setVisible(true);
    }//GEN-LAST:event_about_itemActionPerformed

    private void output_itemActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_output_itemActionPerformed
          output_frame.setSize(520, 220);
          output_frame.setVisible(true);
          brightness_slider.setEnabled(false);
    }//GEN-LAST:event_output_itemActionPerformed

    private void output_search_btnActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_output_search_btnActionPerformed
          output_portsListComoBox.removeAllItems();
          Enumeration portEnum = CommPortIdentifier.getPortIdentifiers();
          portsList.clear();

          while (portEnum.hasMoreElements()) {
                CommPortIdentifier currPortId = (CommPortIdentifier) portEnum.nextElement();
                output_portsListComoBox.addItem(currPortId.getName());
                portsList.add(currPortId);
          }
    }//GEN-LAST:event_output_search_btnActionPerformed

    private void output_cnct_discnctBtnActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_output_cnct_discnctBtnActionPerformed
          if (!op_connected) {
                try {
                      portId = portsList.get(output_portsListComoBox.getSelectedIndex());
                      serialPort = (SerialPort) portId.open(this.getClass().getName(), TIME_OUT);
                      output_cnct_discnctBtn.setText("Disconnect from Port");
                      statusLabel.setText("Status: Connected");
                      brightness_slider.setEnabled(true);
                      op_connected = !op_connected;
                      serialPort.setSerialPortParams(DATA_RATE,
                              SerialPort.DATABITS_8,
                              SerialPort.STOPBITS_1,
                              SerialPort.PARITY_NONE);
                      output = serialPort.getOutputStream();
                } catch (NullPointerException e) {
                      System.err.println(e.toString());
                } catch (UnsupportedCommOperationException | PortInUseException | IOException ex) {
                      System.err.println(ex.toString());
                }
          } else {
                close();
                output_cnct_discnctBtn.setText("Connect to Port");
                statusLabel.setText("Status: Disonnected");
                brightness_slider.setEnabled(false);
                op_connected = !op_connected;
          }
    }//GEN-LAST:event_output_cnct_discnctBtnActionPerformed

    private void brightness_sliderStateChanged(javax.swing.event.ChangeEvent evt) {//GEN-FIRST:event_brightness_sliderStateChanged
          try {
                if (op_connected) {
                      if (!brightness_slider.getValueIsAdjusting()) {
                            String value = String.valueOf(brightness_slider.getValue());
                            System.out.println(value);
                            output.write(value.getBytes());
                            output.flush();
                      }
                }
          } catch (IOException ex) {
                System.err.println(ex.toString());
          }
    }//GEN-LAST:event_brightness_sliderStateChanged

    private void output_frameWindowClosing(java.awt.event.WindowEvent evt) {//GEN-FIRST:event_output_frameWindowClosing
          if (op_connected) {
                close();
          }
    }//GEN-LAST:event_output_frameWindowClosing

    private void slow_input_itemActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_slow_input_itemActionPerformed
          slow_input_frame.setSize(520, 130);
          slow_input_frame.setVisible(true);
    }//GEN-LAST:event_slow_input_itemActionPerformed

    private void slow_input_search_btnActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_slow_input_search_btnActionPerformed
          slow_input_portsListComoBox.removeAllItems();
          Enumeration portEnum = CommPortIdentifier.getPortIdentifiers();
          portsList.clear();

          while (portEnum.hasMoreElements()) {
                CommPortIdentifier currPortId = (CommPortIdentifier) portEnum.nextElement();
                slow_input_portsListComoBox.addItem(currPortId.getName());
                portsList.add(currPortId);
          }
    }//GEN-LAST:event_slow_input_search_btnActionPerformed

      @SuppressWarnings("empty-statement")
    private void slow_input_cnct_discnctBtnActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_slow_input_cnct_discnctBtnActionPerformed
          if (!ip_connected) {
                String com_num = slow_input_portsListComoBox.getSelectedItem().toString();
                ip_connected = !ip_connected;
                slow_input_cnct_discnctBtn.setEnabled(false);
                slow_input_cnct_discnctBtn.setText("Connected");
                new Thread(() -> {
                      try {
                            String[] cmd = new String[]{};
                            if (info.getName().toLowerCase().equals("windows")) {
                                  cmd = new String[]{"cmd", "/c", "python scripts_and_helpers\\graphing\\slow\\plotter.py " + com_num};
                            } else {
                                  cmd = new String[]{"/bin/sh", "-c", "python scripts_and_helpers/graphing/slow/plotter.py " + com_num};
                            }

                            graphing_process = new ProcessBuilder(cmd).start();
                            while (graphing_process.isAlive());
                            slow_input_cnct_discnctBtn.setEnabled(true);
                            slow_input_cnct_discnctBtn.setText("Connect to Port");
                            ip_connected = !ip_connected;
                      } catch (IOException ex) {
                            System.err.println(ex.toString());
                      }
                }).start();
          } else {
                slow_input_cnct_discnctBtn.setEnabled(true);
                slow_input_cnct_discnctBtn.setText("Connect to Port");
                ip_connected = !ip_connected;
          }
    }//GEN-LAST:event_slow_input_cnct_discnctBtnActionPerformed

    private void server_snd_btnActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_server_snd_btnActionPerformed
          server_msg_fieldKeyPressed(new java.awt.event.KeyEvent(this, 0, 0, 0, 10));
    }//GEN-LAST:event_server_snd_btnActionPerformed

    private void client_snd_btnActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_client_snd_btnActionPerformed
          client_msg_fieldKeyPressed(new java.awt.event.KeyEvent(this, 0, 0, 0, 10));
    }//GEN-LAST:event_client_snd_btnActionPerformed

    private void fast_input_itemActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_fast_input_itemActionPerformed
          fast_input_frame.setSize(520, 130);
          fast_input_frame.setVisible(true);
          fast_input_portsListComoBox.removeAllItems();
          fast_input_cnct_discnctBtn.setText("Connect to COM PORT");
          fast_input_cnct_discnctBtn.setEnabled(true);
    }//GEN-LAST:event_fast_input_itemActionPerformed

    private void fast_input_search_btnActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_fast_input_search_btnActionPerformed
          fast_input_portsListComoBox.removeAllItems();
          Enumeration portEnum = CommPortIdentifier.getPortIdentifiers();
          portsList.clear();

          while (portEnum.hasMoreElements()) {
                CommPortIdentifier currPortId = (CommPortIdentifier) portEnum.nextElement();
                fast_input_portsListComoBox.addItem(currPortId.getName());
                portsList.add(currPortId);
          }
    }//GEN-LAST:event_fast_input_search_btnActionPerformed

    private void fast_input_cnct_discnctBtnActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_fast_input_cnct_discnctBtnActionPerformed
          if (!ip_connected) {
                String com_num = fast_input_portsListComoBox.getSelectedItem().toString();
                ip_connected = !ip_connected;
                fast_input_cnct_discnctBtn.setEnabled(false);
                fast_input_cnct_discnctBtn.setText("Connected");
                new Thread(() -> {
                      try {
                            String[] cmd = new String[]{};
                            if (info.getName().toLowerCase().equals("windows")) {
                                  cmd = new String[]{"cmd", "/c", "graphing.bat " + com_num};
                            } else {
                                  cmd = new String[]{"/bin/sh", "-c", "scripts_and_helpers/graphing/fast/linux/p_oscilloscope " + com_num};
                            }
                            graphing_process = new ProcessBuilder(cmd).start();
                            ip_connected = !ip_connected;
                      } catch (IOException ex) {
                            System.err.println(ex.toString());
                      }
                }).start();
          } else {
                fast_input_cnct_discnctBtn.setEnabled(true);
                fast_input_cnct_discnctBtn.setText("Connect to Port");
                ip_connected = !ip_connected;
          }
    }//GEN-LAST:event_fast_input_cnct_discnctBtnActionPerformed

      public static void main(String args[]) {
            try {
                  javax.swing.UIManager.LookAndFeelInfo info = javax.swing.UIManager.getInstalledLookAndFeels()[3];
                  javax.swing.UIManager.setLookAndFeel(info.getClassName());
            } catch (ClassNotFoundException | InstantiationException | IllegalAccessException | javax.swing.UnsupportedLookAndFeelException ex) {
                  System.err.println(ex);
            }
        //</editor-fold>

        //</editor-fold>

            /* Create and display the form */
            java.awt.EventQueue.invokeLater(() -> {
                  Main_Frame main_frame = new Main_Frame();

                  int screen_width = Toolkit.getDefaultToolkit().getScreenSize().width;
                  int screen_height = Toolkit.getDefaultToolkit().getScreenSize().height;

                  main_frame.setLocation(screen_width / 4, screen_height / 5);
                  main_frame.setVisible(true);
            });
      }

    // Variables declaration - do not modify//GEN-BEGIN:variables
    private javax.swing.JFrame about_frame;
    private javax.swing.JMenuItem about_item;
    private javax.swing.JMenuItem avoid_item;
    private javax.swing.JLabel brightness_label;
    private javax.swing.JSlider brightness_slider;
    private javax.swing.JMenu chat_menu;
    private javax.swing.JTextPane client_chat_pane;
    private javax.swing.JScrollPane client_chat_panel;
    private javax.swing.JFrame client_frame;
    private javax.swing.JMenuItem client_item;
    private javax.swing.JTextField client_msg_field;
    private javax.swing.JButton client_snd_btn;
    private javax.swing.JMenu deadlock_item;
    private javax.swing.JButton delete_row_btn;
    private javax.swing.JMenuItem detect_item;
    private javax.swing.JLabel development_team_title_label;
    private javax.swing.JButton disk_btn;
    private javax.swing.JMenuItem doc_item;
    private javax.swing.JButton edit_scheduler_table_row;
    private javax.swing.JTable editing_table;
    private javax.swing.JFrame editing_table_frame;
    private javax.swing.JScrollPane editing_table_frame_scroll_pane;
    private javax.swing.JMenuItem ex_avoid_item;
    private javax.swing.JMenu ex_chat_menu;
    private javax.swing.JMenuItem ex_client_item;
    private javax.swing.JMenu ex_deadlock_item;
    private javax.swing.JMenuItem ex_detect_item;
    private javax.swing.JMenu ex_file_sys_menu;
    private javax.swing.JMenu ex_io_menu;
    private javax.swing.JMenu ex_mem_menu;
    private javax.swing.JMenu ex_net_menu;
    private javax.swing.JMenuItem ex_prevent_item;
    private javax.swing.JMenu ex_proc_menu;
    private javax.swing.JMenuItem ex_sched_item;
    private javax.swing.JMenuItem ex_server_item;
    private javax.swing.JMenuItem ex_sync_item;
    private javax.swing.JMenu examples_menu;
    private javax.swing.JMenuItem exit_item;
    private javax.swing.JButton fast_input_cnct_discnctBtn;
    private javax.swing.JFrame fast_input_frame;
    private javax.swing.JMenuItem fast_input_item;
    private javax.swing.JComboBox<String> fast_input_portsListComoBox;
    private javax.swing.JButton fast_input_search_btn;
    private javax.swing.JRadioButton fifo_chk_btn;
    private javax.swing.JMenu file_menu;
    private javax.swing.JMenu file_sys_menu;
    private javax.swing.JMenu help_menu;
    private javax.swing.JMenu io_menu;
    private javax.swing.JLabel logo_label;
    private javax.swing.JMenuBar main_menu_bar;
    private javax.swing.JLabel max_label;
    private javax.swing.JButton mem_btn;
    private javax.swing.JMenu mem_menu;
    private javax.swing.JLabel min_label;
    private javax.swing.JLabel name1;
    private javax.swing.JLabel name2;
    private javax.swing.JLabel name3;
    private javax.swing.JLabel name4;
    private javax.swing.JLabel name5;
    private javax.swing.JButton net_btn;
    private javax.swing.JMenu net_menu;
    private javax.swing.JButton new_row_btn;
    private javax.swing.JRadioButton npsjf_chk_btn;
    private javax.swing.JButton ok_button;
    private javax.swing.JMenuItem open_sym_item;
    private javax.swing.JButton output_cnct_discnctBtn;
    private javax.swing.JFrame output_frame;
    private javax.swing.JMenuItem output_item;
    private javax.swing.JComboBox<String> output_portsListComoBox;
    private javax.swing.JButton output_search_btn;
    private javax.swing.JMenuItem prevent_item;
    private javax.swing.JButton proc_btn;
    private javax.swing.JMenu proc_menu_item;
    private javax.swing.JTable processes_table;
    private javax.swing.JRadioButton rr_chk_btn;
    private javax.swing.JMenuItem save_sym_item;
    private javax.swing.JMenuItem sched_item;
    private javax.swing.ButtonGroup scheduler_button_group;
    private javax.swing.JFrame scheduling_frame;
    private javax.swing.JScrollPane scheduling_table_scroll_pane;
    private javax.swing.JTextPane server_chat_pane;
    private javax.swing.JScrollPane server_chat_panel;
    private javax.swing.JFrame server_frame;
    private javax.swing.JMenuItem server_item;
    private javax.swing.JTextField server_msg_field;
    private javax.swing.JButton server_snd_btn;
    private javax.swing.JButton slow_input_cnct_discnctBtn;
    private javax.swing.JFrame slow_input_frame;
    private javax.swing.JMenuItem slow_input_item;
    private javax.swing.JComboBox<String> slow_input_portsListComoBox;
    private javax.swing.JButton slow_input_search_btn;
    private javax.swing.JButton sort_btn;
    private javax.swing.JLabel statusLabel;
    private javax.swing.JMenuItem sync_item;
    // End of variables declaration//GEN-END:variables
}