/**


import java.awt.*;
import java.awt.event.*;
import java.io.*;
import java.net.*;
import java.util.*;
import javax.swing.*;
import javax.swing.border.*;

public class EnhancedChatClient extends JFrame {
    private static final String TITLE = "Java Swing Chat";
    private static final int WIDTH = 800;
    private static final int HEIGHT = 600;

    private JTextArea chatArea;
    private JTextField messageField;
    private JButton sendButton;
    private JList<String> userList;
    private DefaultListModel<String> userListModel;
    private JPanel statusPanel;
    private JLabel statusLabel;

    private Socket socket;
    private PrintWriter out;
    private BufferedReader in;
    private String username;
    private String serverAddress;
    private int serverPort = 9001;

    public EnhancedChatClient() {
        // Set up the frame
        super(TITLE);
        setSize(WIDTH, HEIGHT);
        setMinimumSize(new Dimension(500, 400));
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setLayout(new BorderLayout(5, 5));

        // Initialize components
        initializeComponents();

        // Add window closing event
        addWindowListener(new WindowAdapter() {
            @Override
            public void windowClosing(WindowEvent e) {
                disconnect();
            }
        });

        // Center on screen
        setLocationRelativeTo(null);
    }

    private void initializeComponents() {
        // Chat area - center panel
        chatArea = new JTextArea();
        chatArea.setEditable(false);
        chatArea.setLineWrap(true);
        chatArea.setWrapStyleWord(true);
        chatArea.setFont(new Font("SansSerif", Font.PLAIN, 14));
        chatArea.setMargin(new Insets(10, 10, 10, 10));
        JScrollPane chatScrollPane = new JScrollPane(chatArea);
        chatScrollPane.setVerticalScrollBarPolicy(JScrollPane.VERTICAL_SCROLLBAR_ALWAYS);

        // Message input panel - south
        JPanel inputPanel = new JPanel(new BorderLayout(5, 5));
        inputPanel.setBorder(BorderFactory.createEmptyBorder(5, 5, 5, 5));

        messageField = new JTextField();
        messageField.setFont(new Font("SansSerif", Font.PLAIN, 14));
        messageField.setEnabled(false);
        messageField.addActionListener(e -> sendMessage());

        sendButton = new JButton("Send");
        sendButton.setEnabled(false);
        sendButton.addActionListener(e -> sendMessage());

        inputPanel.add(messageField, BorderLayout.CENTER);
        inputPanel.add(sendButton, BorderLayout.EAST);

        // User list - east
        userListModel = new DefaultListModel<>();
        userList = new JList<>(userListModel);
        userList.setFont(new Font("SansSerif", Font.PLAIN, 14));
        userList.setBorder(BorderFactory.createEmptyBorder(5, 5, 5, 5));
        JScrollPane userScrollPane = new JScrollPane(userList);
        userScrollPane.setPreferredSize(new Dimension(150, HEIGHT));
        userScrollPane.setBorder(BorderFactory.createCompoundBorder(
                BorderFactory.createEmptyBorder(5, 0, 5, 5),
                BorderFactory.createTitledBorder("Online Users")
        ));

        // Status panel - north
        statusPanel = new JPanel(new BorderLayout());
        statusPanel.setBorder(BorderFactory.createEmptyBorder(5, 5, 5, 5));
        statusLabel = new JLabel("Not connected", JLabel.CENTER);
        statusLabel.setFont(new Font("SansSerif", Font.BOLD, 14));
        statusPanel.add(statusLabel, BorderLayout.CENTER);

        // Add components to frame
        add(statusPanel, BorderLayout.NORTH);
        add(chatScrollPane, BorderLayout.CENTER);
        add(inputPanel, BorderLayout.SOUTH);
        add(userScrollPane, BorderLayout.EAST);

        // Menu Bar
        JMenuBar menuBar = new JMenuBar();

        JMenu connectionMenu = new JMenu("Connection");
        JMenuItem connectItem = new JMenuItem("Connect...");
        connectItem.addActionListener(e -> showConnectionDialog());
        JMenuItem disconnectItem = new JMenuItem("Disconnect");
        disconnectItem.addActionListener(e -> disconnect());

        connectionMenu.add(connectItem);
        connectionMenu.add(disconnectItem);

        JMenu helpMenu = new JMenu("Help");
        JMenuItem aboutItem = new JMenuItem("About");
        aboutItem.addActionListener(e -> JOptionPane.showMessageDialog(this,
                "Java Swing Chat Client\nA simple chat application",
                "About", JOptionPane.INFORMATION_MESSAGE));
        helpMenu.add(aboutItem);

        menuBar.add(connectionMenu);
        menuBar.add(helpMenu);
        setJMenuBar(menuBar);
    }

    private void showConnectionDialog() {
        JPanel panel = new JPanel(new GridLayout(3, 2, 5, 5));

        JTextField serverField = new JTextField("localhost");
        JTextField portField = new JTextField("9001");
        JTextField usernameField = new JTextField();

        panel.add(new JLabel("Server:"));
        panel.add(serverField);
        panel.add(new JLabel("Port:"));
        panel.add(portField);
        panel.add(new JLabel("Username:"));
        panel.add(usernameField);

        int result = JOptionPane.showConfirmDialog(this, panel, "Connect to Server",
                JOptionPane.OK_CANCEL_OPTION, JOptionPane.PLAIN_MESSAGE);

        if (result == JOptionPane.OK_OPTION) {
            try {
                serverAddress = serverField.getText().trim();
                serverPort = Integer.parseInt(portField.getText().trim());
                username = usernameField.getText().trim();

                if (username.isEmpty()) {
                    JOptionPane.showMessageDialog(this,
                            "Username cannot be empty", "Error",
                            JOptionPane.ERROR_MESSAGE);
                    return;
                }

                connectToServer();
            } catch (NumberFormatException e) {
                JOptionPane.showMessageDialog(this,
                        "Invalid port number", "Error",
                        JOptionPane.ERROR_MESSAGE);
            }
        }
    }

    private void connectToServer() {
        new Thread(() -> {
            try {
                updateStatus("Connecting to " + serverAddress + ":" + serverPort + "...");

                socket = new Socket(serverAddress, serverPort);
                out = new PrintWriter(socket.getOutputStream(), true);
                in = new BufferedReader(new InputStreamReader(socket.getInputStream()));

                updateStatus("Connected as " + username);

                // Enable UI components
                SwingUtilities.invokeLater(() -> {
                    messageField.setEnabled(true);
                    sendButton.setEnabled(true);
                    messageField.requestFocus();
                });

                // Send username
                out.println(username);

                // Read messages in a loop
                String message;
                while ((message = in.readLine()) != null) {
                    final String finalMessage = message;
                    SwingUtilities.invokeLater(() -> processMessage(finalMessage));
                }
            } catch (IOException e) {
                SwingUtilities.invokeLater(() -> {
                    updateStatus("Disconnected");
                    JOptionPane.showMessageDialog(EnhancedChatClient.this,
                            "Connection error: " + e.getMessage(),
                            "Connection Failed", JOptionPane.ERROR_MESSAGE);
                });
            } finally {
                disconnect();
            }
        }).start();
    }

    private void processMessage(String message) {
        if (message.startsWith("USERLIST:")) {
            // Update user list
            String[] users = message.substring(9).split(",");
            userListModel.clear();
            for (String user : users) {
                if (!user.isEmpty()) {
                    userListModel.addElement(user);
                }
            }
        } else if (message.startsWith("SYSTEM:")) {
            // System message
            appendToChatArea("[System] " + message.substring(7));
        } else {
            // Regular chat message
            appendToChatArea(message);
        }
    }

    private void appendToChatArea(String message) {
        chatArea.append(message + "\n");
        // Auto-scroll to bottom
        chatArea.setCaretPosition(chatArea.getDocument().getLength());
    }

    private void updateStatus(String status) {
        SwingUtilities.invokeLater(() -> {
            statusLabel.setText(status);
            setTitle(TITLE + " - " + status);
        });
    }

    private void sendMessage() {
        String message = messageField.getText().trim();
        if (!message.isEmpty()) {
            out.println(message);
            messageField.setText("");
        }
        messageField.requestFocus();
    }

    private void disconnect() {
        try {
            if (out != null) {
                out.println("DISCONNECT");
            }
            if (socket != null && !socket.isClosed()) {
                socket.close();
            }
        } catch (IOException e) {
            e.printStackTrace();
        }

        // Reset UI
        SwingUtilities.invokeLater(() -> {
            messageField.setEnabled(false);
            sendButton.setEnabled(false);
            userListModel.clear();
            updateStatus("Disconnected");
        });
    }

    public static void main(String[] args) {
        // Set look and feel to the system look and feel
        try {
            UIManager.setLookAndFeel(UIManager.getSystemLookAndFeelClassName());
        } catch (Exception e) {
            e.printStackTrace();
        }

        SwingUtilities.invokeLater(() -> {
            EnhancedChatClient client = new EnhancedChatClient();
            client.setVisible(true);
        });
    }
}
 */