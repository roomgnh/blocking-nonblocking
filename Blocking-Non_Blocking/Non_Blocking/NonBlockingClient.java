import java.io.IOException;
import java.net.InetSocketAddress;
import java.nio.ByteBuffer;
import java.nio.channels.SocketChannel;
import java.util.Scanner;

public class NonBlockingClient {
    public static void main(String[] args) throws IOException {
        String serverAddress = "localhost";
        int portNumber = 2323;

        SocketChannel socketChannel = SocketChannel.open(new InetSocketAddress(serverAddress, portNumber));
        socketChannel.configureBlocking(false);

        System.out.println("Connected to the server. Type 'exit' to disconnect.");

        Scanner scanner = new Scanner(System.in);
        ByteBuffer buffer = ByteBuffer.allocate(1024);

        while (true) {
            System.out.print("Enter a message: ");
            String message = scanner.nextLine();

            if ("exit".equalsIgnoreCase(message)) {
                break;
            }

            buffer.clear();
            buffer.put(message.getBytes());
            buffer.flip();

            while (buffer.hasRemaining()) {
                socketChannel.write(buffer);
            }

            buffer.clear();
            socketChannel.read(buffer);

            buffer.flip();
            byte[] responseBytes = new byte[buffer.remaining()];
            buffer.get(responseBytes);
            String response = new String(responseBytes);

            System.out.println("Server response: " + response.trim());
        }

        socketChannel.close();
    }
}
