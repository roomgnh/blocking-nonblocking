import java.io.IOException;
import java.net.InetSocketAddress;
import java.nio.ByteBuffer;
import java.nio.channels.SelectionKey;
import java.nio.channels.Selector;
import java.nio.channels.ServerSocketChannel;
import java.nio.channels.SocketChannel;
import java.util.Iterator;
import java.util.concurrent.ConcurrentHashMap;

public class NonBlockingServer {
    private static ConcurrentHashMap<String, SocketChannel> clients = new ConcurrentHashMap<>();

    public static void main(String[] args) throws IOException {
        int portNumber = 2323;
        ServerSocketChannel serverSocketChannel = ServerSocketChannel.open();
        serverSocketChannel.bind(new InetSocketAddress(portNumber));
        serverSocketChannel.configureBlocking(false);

        Selector selector = Selector.open();
        serverSocketChannel.register(selector, SelectionKey.OP_ACCEPT);

        System.out.println("Non-blocking Server is running and waiting for clients...");

        while (true) {
            selector.select();

            Iterator<SelectionKey> keyIterator = selector.selectedKeys().iterator();

            while (keyIterator.hasNext()) {
                SelectionKey key = keyIterator.next();
                keyIterator.remove();

                if (key.isAcceptable()) {
                    acceptConnection(serverSocketChannel, selector);
                } else if (key.isReadable()) {
                    readFromClient(key);
                }
            }
        }
    }

    private static void acceptConnection(ServerSocketChannel serverSocketChannel, Selector selector) throws IOException {
        SocketChannel clientChannel = serverSocketChannel.accept();
        clientChannel.configureBlocking(false);
        clientChannel.register(selector, SelectionKey.OP_READ);
        String clientId = clientChannel.getRemoteAddress().toString();
        System.out.println("Client connected: " + clientId);
        clients.put(clientId, clientChannel);
        sendMessage(clientId, "Welcome to the server, " + clientId);
    }

    private static void readFromClient(SelectionKey key) throws IOException {
        SocketChannel clientChannel = (SocketChannel) key.channel();
        ByteBuffer buffer = ByteBuffer.allocate(1024);
        int bytesRead = clientChannel.read(buffer);

        if (bytesRead == -1) {
            String clientId = clientChannel.getRemoteAddress().toString();
            System.out.println("Client disconnected: " + clientId);
            clients.remove(clientId);
            sendMessage("Server", clientId + " has disconnected.");
            clientChannel.close();
        } else if (bytesRead > 0) {
            buffer.flip();
            byte[] data = new byte[buffer.remaining()];
            buffer.get(data);
            String clientId = clientChannel.getRemoteAddress().toString();
            String message = new String(data);

            System.out.println("Received message from " + clientId + ": " + message.trim());

            // Broadcast the message to all clients
            broadcastMessage(clientId, message);
        }
    }

    private static void broadcastMessage(String sender, String message) {
        clients.forEach((clientId, clientChannel) -> sendMessage(sender, message));
    }

    private static void sendMessage(String clientId, String message) {
        try {
            ByteBuffer buffer = ByteBuffer.wrap((clientId + ": " + message).getBytes());
            clients.get(clientId).write(buffer);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
