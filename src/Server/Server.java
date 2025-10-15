package Server;
import java.net.DatagramPacket;
import java.net.DatagramSocket;
import java.net.SocketException;
import java.util.*;
import java.util.concurrent.BlockingQueue;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.CopyOnWriteArrayList;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.LinkedBlockingQueue;

import Client.Client;
import Minesweeper.Game;

// A simple UDP server for Minesweeper99
public class Server {

    private DatagramSocket socket;

    private ExecutorService processingPool = Executors.newFixedThreadPool(4);
    private BlockingQueue<DatagramPacket> messageQueue = new LinkedBlockingQueue<>();
    private CopyOnWriteArrayList<ServerClient> clients = new CopyOnWriteArrayList<>();

    public Server() throws SocketException {
        socket = new DatagramSocket(12345);

        // Initialize receiver thread
        Thread receiver = new Thread(() -> {
            while (true) {
                try {
                    byte[] buffer = new byte[1024];
                    DatagramPacket packet = new DatagramPacket(buffer, buffer.length);
                    socket.receive(packet);
                    messageQueue.put(packet);
                    // If new connection, add to clients list
                    clients.add(new ServerClient(packet.getAddress(), packet.getPort(), packet.getPort()));
                    System.out.println("New client connected: " + packet.getAddress() + ":" + packet.getPort());
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
        });
        receiver.start();

        // Initialize processing threads
        for (int i = 0; i < 4; i++) {
            processingPool.submit(() -> {
                while (true) {
                    try {
                        DatagramPacket packet = messageQueue.take();
                        processPacket(packet);
                    } catch (Exception e) {
                        e.printStackTrace();
                    }
                }
            });
        }
    }

    public void processPacket(DatagramPacket packet) {
        // Process the packet (e.g., parse message, update game state, etc.)
        String message = new String(packet.getData(), 0, packet.getLength());
        System.out.println("Received: " + message + " from " + packet.getAddress() + ":" + packet.getPort());

        // Example: Echo the message back to the sender
        try {
            DatagramPacket response = new DatagramPacket(packet.getData(), packet.getLength(), packet.getAddress(), packet.getPort());
            socket.send(response);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
