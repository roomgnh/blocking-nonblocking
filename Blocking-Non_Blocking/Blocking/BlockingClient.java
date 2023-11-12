import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.net.Socket;
import java.util.Scanner;

public class BlockingClient {
    public static void main(String[] args) throws IOException {
        String serverAddress = "localhost";
        int portNumber = 2323;

        try (
            Socket socket = new Socket(serverAddress, portNumber);
            PrintWriter out = new PrintWriter(socket.getOutputStream(), true);
            BufferedReader in = new BufferedReader(new InputStreamReader(socket.getInputStream()));
            Scanner scanner = new Scanner(System.in)
        ) {
            System.out.println("Connected to the server. Type 'exit' to disconnect.");

            // Read messages from the user and send them to the server
            while (true) {
                System.out.print("Enter a message: ");
                String message = scanner.nextLine();

                if ("exit".equalsIgnoreCase(message)) {
                    break;
                }

                out.println(message);

                // Receive and print the server's response
                String serverResponse = in.readLine();
                System.out.println("Server response: " + serverResponse);
            }
        }
    }
}
