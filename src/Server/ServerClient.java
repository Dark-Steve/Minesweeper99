package Server;

import java.net.InetAddress;

import Minesweeper.Game;

public class ServerClient {
    public InetAddress address;
    public int port;
    public int id;
    public Game game;

    public ServerClient(InetAddress address, int port, int id) {
        this.address = address;
        this.port = port;
        this.id = id;
    }
}