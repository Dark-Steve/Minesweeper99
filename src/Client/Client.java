package Client;

import java.net.DatagramPacket;
import java.net.DatagramSocket;
import java.net.InetAddress;
import java.util.concurrent.BlockingQueue;
import java.util.concurrent.LinkedBlockingQueue;

// Base class for client connecting to the server
public class Client  {
    public long clientId;

    protected InetAddress serverAddress;
    protected int serverPort;

    protected boolean connected = false;
    protected DatagramSocket socket;

    protected BlockingQueue<byte[]> receivedQueue = new LinkedBlockingQueue<>();

    public Client(String serverAddress, int serverPort, int id) throws Exception {
        this.serverAddress = InetAddress.getByName(serverAddress);
        this.serverPort = serverPort;
        this.clientId = id;
    }

    // Method to send messages to the server
    public void sendMessage(byte[] message) throws Exception {
        if (!connected) {
            throw new IllegalStateException("Client is not connected to the server.");
        }   
        // Sending logic here
        socket.send(new DatagramPacket(message, message.length, serverAddress, serverPort));
    }

    // Method to receive and store incoming messages
    public void receiveMessage() throws Exception {
        if (!connected) {
            throw new IllegalStateException("Client is not connected to the server.");
        }
        // Receiving logic here
        byte[] buffer = new byte[1024];
        DatagramPacket packet = new DatagramPacket(buffer, buffer.length);
        socket.receive(packet);
        System.out.println("Received: " + new String(packet.getData(), 0, packet.getLength()));
        receivedQueue.put(packet.getData());
    }

    // Method to connect to the server
    public void connect() throws Exception {
        // Connection logic here
        socket = new DatagramSocket();
        connected = true;
    }

    // Method to disconnect from the server
    public void disconnect() {
        // Disconnection logic here
        if (socket != null && !socket.isClosed()) {
            socket.close();
        }
        connected = false;
    }
}
