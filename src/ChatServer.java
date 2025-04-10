/**


import java.io.*;
import java.net.*;
import java.util.*;
import java.text.SimpleDateFormat;
import java.util.concurrent.ConcurrentHashMap;

public class ChatServer {
    private static final int PORT = 9001;
    private static final SimpleDateFormat TIME_FORMAT = new SimpleDateFormat("HH:mm:ss");

    // Map to store client handlers with usernames
    private static Map<String, ClientHandler> clients = new ConcurrentHashMap<>();

    public static void main(String[] args) {
        System.out.println("Chat Server starting on port " + PORT);

        try (ServerSocket serverSocket = new ServerSocket(PORT)) {
            System.out.println("Server started successfully. Waiting for clients...");

            while (true) {
                Socket clientSocket = serverSocket.accept();
                System.out.println("New client connected: " + clientSocket.getInetAddress().getHostAddress());

                // Create a new handler thread for the client
                ClientHandler handler = new ClientHandler(clientSocket);
                handler.start();
            }
        } catch (IOException e) {
            System.out.println("Server error: " + e.getMessage());
            e.printStackTrace();
        }
    }

    // Broadcast message to all clients
    public static void broadcast(String message) {
        System.out.println("Broadcasting: " + message);
        String timestamp = "[" + TIME_FORMAT.format(new Date()) + "] ";

        for (ClientHandler client : clients.values()) {
            client.sendMessage(timestamp + message);
        }
    }

    // Send the updated list of users to all clients
    public static void updateUserList() {
        StringBuilder userList = new StringBuilder("USERLIST:");
        for (String username : clients.keySet()) {
            userList.append(username).append(",");
        }

        for (ClientHandler client : clients.values()) {
            client.sendMessage(userList.toString());
        }
    }

    // Handle client connection
    private static class ClientHandler extends Thread {
        private Socket socket;
        private PrintWriter out;
        private BufferedReader in;
        private String username;

        public ClientHandler(Socket socket) {
            this.socket = socket;
        }

        @Override
        public void run() {
            try {
                // Set up input and output streams
                out = new PrintWriter(socket.getOutputStream(), true);
                in = new BufferedReader(new InputStreamReader(socket.getInputStream()));

                // Get username
                username = in.readLine();

                // Check if username is already taken
                if (clients.containsKey(username)) {
                    sendMessage("SYSTEM:Username already taken. Please reconnect with a different name.");
                    socket.close();
                    return;
                }

                // Add client to the map
                clients.put(username, this);

                // Notify all users about the new client
                broadcast("SYSTEM:" + username + " has joined the chat");
                updateUserList();

                // Welcome message to the new client
                sendMessage("SYSTEM:Welcome to the chat server, " + username + "!");
                sendMessage("SYSTEM:There are " + clients.size() + " users online.");

                // Read messages from client
                String message;
                while ((message = in.readLine()) != null) {
                    if (message.equals("DISCONNECT")) {
                        break;
                    }
                    broadcast(username + ": " + message);
                }
            } catch (IOException e) {
                System.out.println("Error handling client " + username + ": " + e.getMessage());
            } finally {
                // Clean up
                disconnect();
            }
        }

        // Send a message to this client
        public void sendMessage(String message) {
            out.println(message);
        }

        // Disconnect the client
        public void disconnect() {
            try {
                if (username != null) {
                    clients.remove(username);
                    broadcast("SYSTEM:" + username + " has left the chat");
                    updateUserList();
                }

                if (socket != null && !socket.isClosed()) {
                    socket.close();
                }
            } catch (IOException e) {
                System.out.println("Error disconnecting client: " + e.getMessage());
            }
        }
    }
}
 */