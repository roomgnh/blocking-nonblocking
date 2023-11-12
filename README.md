# Simple Java Socket Programs

This repository contains two simple Java socket programs that demonstrate both the blocking and non-blocking socket programming models. The programs consist of a server and a client for each model.

## Blocking Model

### BlockingServer.java

- Listens for connections from multiple clients simultaneously.
- Handles incoming messages from clients.
- Identifies the sender in the received messages.
- Notifies clients and the server upon connection/disconnection.

### BlockingClient.java

- Connects to the server.
- Sends messages to the server and receives server responses.
- Allows the user to gracefully exit by typing 'exit'.

## Non-Blocking Model

### NonBlockingServer.java

- Utilizes `java.nio` for non-blocking socket programming.
- Uses `Selector` and `SelectionKey` to monitor connections and receive messages from connected clients.
- Manages multiple clients concurrently.
- Notifies clients and the server upon connection/disconnection.
- Broadcasts messages to all connected clients.

### NonBlockingClient.java

- Connects to the non-blocking server.
- Sends messages to the server and receives server responses.
- Allows the user to gracefully exit by typing 'exit'.

## Execution

1. Compile the Java files.
2. Run `BlockingServer` and `BlockingClient` in separate terminals or processes.
3. Run `NonBlockingServer` and `NonBlockingClient` in separate terminals or processes.

Ensure that both models operate independently and manage connections appropriately.

## Assessment Criteria

The functionality is assessed based on the correct implementation of both models, clarity and cleanliness of the code, effective management of connections and messages, and the ability to handle special conditions such as disconnected connections. Creativity in managing solutions is also appreciated.

Feel free to explore and add your creative touch to enhance the functionality of the programs.

## Images
![Screenshot 2023-11-12 194355](https://github.com/roomgnh/blocking-nonblocking/assets/149757857/59999726-d7f5-4ec7-a085-b6e9bf57524d)

![Screenshot 2023-11-12 195957](https://github.com/roomgnh/blocking-nonblocking/assets/149757857/96f4e6d0-e166-4c25-b30e-5d3638dcbeed)

