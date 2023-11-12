import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.HashMap;
import java.util.Map;

public class BlockingServer {
    private static Map<String, PrintWriter> clients = new HashMap<>();

    public static void main(String[] args) throws IOException {
        int portNumber = 2323;
        ServerSocket serverSocket = new ServerSocket(portNumber);

        System.out.println("Blocking Server is running and waiting for clients...");

        try {
            while (true) {
                Socket clientSocket = serverSocket.accept();
                System.out.println("Client connected: " + clientSocket);

                // Handle the client in a new thread
                new Thread(() -> handleClient(clientSocket)).start();
            }
        } finally {
            serverSocket.close();
        }
    }

    private static void handleClient(Socket clientSocket) {
        try (
            PrintWriter out = new PrintWriter(clientSocket.getOutputStream(), true);
            BufferedReader in = new BufferedReader(new InputStreamReader(clientSocket.getInputStream()))
        ) {
            String clientId = clientSocket.getInetAddress().toString();
            System.out.println("Handling client: " + clientId);

            clients.put(clientId, out);

            out.println("Welcome to the server, " + clientId);

            String inputLine;
            while ((inputLine = in.readLine()) != null) {
                System.out.println("Received message from " + clientId + ": " + inputLine);

                // Broadcast the message to all clients
                broadcastMessage(clientId, inputLine);
            }

            System.out.println("Client disconnected: " + clientId);
            clients.remove(clientId);
            broadcastMessage("Server", clientId + " has disconnected.");

        } catch (IOException e) {
            System.err.println("Error handling client: " + e.getMessage());
        }
    }

    private static void broadcastMessage(String sender, String message) {
        for (PrintWriter client : clients.values()) {
            client.println(sender + ": " + message);
        }
    }
}
